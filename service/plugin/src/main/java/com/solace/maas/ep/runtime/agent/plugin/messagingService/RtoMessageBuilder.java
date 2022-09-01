package com.solace.maas.ep.runtime.agent.plugin.messagingService;

import com.solace.maas.ep.runtime.agent.plugin.config.VMRProperties;
import com.solace.messaging.resources.Topic;
import com.solace.messaging.util.internal.Internal;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.ContextHandle;
import com.solacesystems.solclientj.core.handle.Handle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.osgi.annotation.versioning.ProviderType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

@ProviderType
@ConditionalOnBean(VMRProperties.class)
@ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
public interface RtoMessageBuilder {
    RtoMessageBuilder fromProperties(ArrayList<String> sessionConfiguration);

    RtoMessageBuilder createContext();

    RtoMessageBuilder createSession();

    RtoMessageBuilder connect();

    RtoMessageBuilder publish(String message, Topic topic);

    RtoMessageBuilder destroy();

    @Slf4j
    @Internal
    @ProviderType
    @Data
    class RtoMessageBuilderImpl implements RtoMessageBuilder {
        private final MessageCallback messageCallback;
        private final SessionEventCallback sessionEventCallback;
        private ArrayList<String> sessionConfig;
        private ContextHandle contextHandle;
        private SessionHandle sessionHandle;
        private MessageHandle messageHandle;

        public RtoMessageBuilderImpl(MessageCallback messageCallback, SessionEventCallback sessionEventCallback) {
            this.messageCallback = messageCallback;
            this.sessionEventCallback = sessionEventCallback;
        }

        public RtoMessageBuilder fromProperties(ArrayList<String> sessionConfiguration) {
            this.sessionConfig = sessionConfiguration;
            return this;
        }

        public RtoMessageBuilder createContext() {
            // Initialize the API
            Solclient.init(new String[0]);

            log.info(" Creating the context ...");
            contextHandle = Solclient.Allocator.newContextHandle();
            Solclient.createContextForHandle(contextHandle, new String[0]);
            return this;
        }

        public RtoMessageBuilder createSession() {
            log.info(" Creating the session ...");
            String[] props = new String[sessionConfig.size()];

            // create a session handle and the actual session
            sessionHandle = Solclient.Allocator.newSessionHandle();
            contextHandle.createSessionForHandle(sessionHandle, sessionConfig.toArray(props), messageCallback,
                    sessionEventCallback);
            return this;
        }

        public RtoMessageBuilder connect() {
            log.info("Connecting the session ...");
            sessionHandle.connect();
            return this;
        }

        public RtoMessageBuilder publish(String message, Topic topic) {
            log.info("Creating the message to publish ...");
            messageHandle = Solclient.Allocator.newMessageHandle();

            if (sessionHandle == null) {
                throw new IllegalArgumentException("SessionHandle may not be null");
            }

            if (!messageHandle.isBound()) {
                // Allocate the message
                Solclient.createMessageForHandle(messageHandle);
            }

            // Set the destination on the message
            String topicStr = topic.getName();
            com.solacesystems.solclientj.core.resource.Topic destination = Solclient.Allocator.newTopic(topicStr);
            messageHandle.setDestination(destination);

            // Create the content to publish and attach to message
            ByteBuffer messageContentBuffer = ByteBuffer.allocateDirect(message.length());

            messageContentBuffer.clear();
            messageContentBuffer.put(message.getBytes(Charset.defaultCharset()));
            messageContentBuffer.flip();
            messageHandle.setBinaryAttachment(messageContentBuffer);

            // Send the message
            sessionHandle.send(messageHandle);

            return this;
        }

        public RtoMessageBuilder destroy() {
            try {
                // destroy the message
                destroyHandle(messageHandle, "messageHandle");

                // disconnect the session
                disconnect(sessionHandle);
                destroyHandle(sessionHandle, "sessionHandle");

                // destroy the context
                destroyHandle(contextHandle, "contextHandle");
            } catch (Throwable t) {
                System.err.println("Unable to call destroy on messageCallback " + t.getCause());
            }
            return this;
        }

        private void destroyHandle(Handle handle, String description) {
            try {
                if (handle != null && handle.isBound()) {
                    handle.destroy();
                    log.info("Destroyed [" + description + "]");
                }
            } catch (Throwable t) {
                log.error("Unable to destroy [" + description + "]", t);
            }
        }

        private void disconnect(SessionHandle sessionHandle) {
            try {
                if (sessionHandle != null && sessionHandle.isBound()) {
                    sessionHandle.disconnect();
                    log.info("sessionHandle disconnected");
                }
            } catch (Throwable t) {
                log.error("Unable to disconnect the sessionHandle ", t);
            }
        }
    }
}
