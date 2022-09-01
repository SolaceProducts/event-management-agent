package com.solace.maas.ep.runtime.agent.messagingServices;

import com.solace.maas.ep.runtime.agent.service.rtoMessagingServiceCallbacks.MessageCallbackImpl;
import com.solace.maas.ep.runtime.agent.service.rtoMessagingServiceCallbacks.SessionCallbackImpl;
import com.solace.maas.ep.runtime.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.RtoMessageBuilder;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
@ConditionalOnBean(VMRProperties.class)
@ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
public class RtoMessagingService {

    public static RtoMessageBuilder createRtoMessagingServiceBuilder() {
        return new RtoMessageBuilder.RtoMessageBuilderImpl(getMessageCallback(true),
                getSessionEventCallback());
    }

    public static MessageCallback getMessageCallback(boolean keepRxMessages) {
        // A message callback to receive messages asynchronously
        MessageCallbackImpl messageCallbackImpl = new MessageCallbackImpl(UUID.randomUUID().toString(), true);
        messageCallbackImpl.keepRxMessages(keepRxMessages);
        return messageCallbackImpl;
    }

    public static SessionEventCallback getSessionEventCallback() {
        // A session event callback to events such as connect/disconnect
        return new SessionCallbackImpl(UUID.randomUUID().toString(), true);
    }
}
