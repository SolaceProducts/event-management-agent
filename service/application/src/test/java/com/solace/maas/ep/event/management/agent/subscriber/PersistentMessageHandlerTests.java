package com.solace.maas.ep.event.management.agent.subscriber;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.common.messages.ScanDataImportMessage;
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

import static com.solace.maas.ep.common.model.ScanDestination.EVENT_PORTAL;
import static com.solace.maas.ep.common.model.ScanType.SOLACE_ALL;
import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
public class PersistentMessageHandlerTests {

    @MockBean
    private ScanManager scanManager;

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
    void testScanCommandMessageHandler() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );

        solacePersistentMessageHandler.onMessage(inboundMessage);

        verify(scanCommandMessageProcessor, times(1)).castToMessageClass(any());
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());

        // There must be no interaction with commandMessageProcessor
        verify(commandMessageProcessor, times(0)).castToMessageClass(any());
        verify(commandMessageProcessor, times(0)).processMessage(any());


        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
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

    @Test
    void testUnsupportedMessageHandling() {
        //ScanDataImportMessage is unsupported for persistent message handler
        ScanDataImportMessage scanDataImportMessage = new ScanDataImportMessage();
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanDataImportMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanDataImportMessage.class.getCanonicalName()
        );
        solacePersistentMessageHandler.onMessage(inboundMessage);
        List<ILoggingEvent> logs = listAppender.list;
        assertThat(logs.get(logs.size() - 1).getFormattedMessage()).isEqualTo("Unsupported message and/or processor encountered. Skipping processing");
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
        //unsupported message type, so we don't have an appropriate processor to handle the failure scenario
        verify(scanCommandMessageProcessor, times(0)).onFailure(any(), any());
        verify(commandMessageProcessor, times(0)).onFailure(any(), any());
    }


    @Test
    void testMessageAcknowledgementWhenProcessingError() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));

        doThrow(new IllegalArgumentException("Test processing error msg")).when(scanCommandMessageProcessor).processMessage(scanCommandMessage);
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );

        solacePersistentMessageHandler.onMessage(inboundMessage);
        List<ILoggingEvent> logs = listAppender.list;
        assertThat(logs.get(logs.size() - 1).getFormattedMessage())
                .isEqualTo("Error while processing inbound message from queue for mopMessageSubclass: " + ScanCommandMessage.class.getCanonicalName());
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);

        // scan command message processor MUST handle the exception
        verify(scanCommandMessageProcessor, times(1)).onFailure(any(), any());

        //commandMessageProcessor MUST do nothing (not a config push command)
        verify(commandMessageProcessor, times(0)).onFailure(any(), any());
    }


    private String jsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
