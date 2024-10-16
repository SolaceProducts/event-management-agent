package com.solace.maas.ep.event.management.agent.subscriber;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.CommandMessageProcessor;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.ScanCommandMessageProcessor;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "eventPortal.gateway.messaging.standalone=false",
        "eventPortal.managed=true",
        "eventPortal.incomingRequestQueueName = ep_core_ema_requests_123456_123123"
})
@Slf4j
public class CommandPersistentMessageHandlerTests {

    @MockBean
    private ScanManager scanManager;

    @MockBean
    private PersistentMessageReceiver persistentMessageReceiver;

    @Autowired
    private MessagingService messagingService;

    @SpyBean
    private ScanCommandMessageProcessor scanCommandMessageProcessor;

    @SpyBean
    private CommandMessageProcessor commandMessageProcessor;

    @Autowired
    private EventPortalProperties eventPortalProperties;

    @SpyBean
    private SolacePersistentMessageHandler solacePersistentMessageHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private InboundMessage inboundMessage;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setup() {
        Logger scanLogger = (Logger) LoggerFactory.getLogger(SolacePersistentMessageHandler.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        scanLogger.addAppender(listAppender);
        inboundMessage = mock(InboundMessage.class);
    }

    @Test
    void testCommandMessageHandler() {
        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId("someSvcId");
        message.setActorId("myActorId");
        message.setOrgId(eventPortalProperties.getOrganizationId());
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("myCorrelationId");
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(true)
                        .commands(List.of())
                        .build()));
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(message));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                CommandMessage.class.getCanonicalName()
        );

        solacePersistentMessageHandler.onMessage(inboundMessage);

        verify(commandMessageProcessor, times(1)).castToMessageClass(any());
        verify(commandMessageProcessor, times(1)).processMessage(any());

        // There must be no interaction with scanCommandMessageProcessor
        verify(scanCommandMessageProcessor, times(0)).castToMessageClass(any());
        verify(scanCommandMessageProcessor, times(0)).processMessage(any());


        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
    }




    private String jsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
