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

import static com.solace.maas.ep.common.model.ScanDestination.EVENT_PORTAL;
import static com.solace.maas.ep.common.model.ScanType.SOLACE_ALL;
import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link SolacePersistentMessageHandler} correct dispatch of messages to appropriate processors
 */
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "eventPortal.gateway.messaging.standalone=false",
        "eventPortal.managed=true",
        "eventPortal.incomingRequestQueueName = ep_core_ema_requests_123456_123123",
        "eventPortal.waitAckScanCompletePollIntervalSec=1",
        "eventPortal.waitAckScanCompleteTimeoutSec=10",
        "eventPortal.commandThreadPoolMinSize=5",
        "eventPortal.commandThreadPoolMaxSize=10",
        "eventPortal.commandThreadPoolQueueSize=20"
})
@Slf4j
public class PersistentMessageHandlerTests {

    @MockBean
    private ScanManager scanManager;

    @MockBean
    private PersistentMessageReceiver persistentMessageReceiver;

    @Autowired
    private MessagingService messagingServicex;

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

    private TestingSupportSolacePersistentMessageHandlerObserver messageHandlerObserver;

    @BeforeEach
    void setup() {
        Logger scanLogger = (Logger) LoggerFactory.getLogger(SolacePersistentMessageHandler.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        scanLogger.addAppender(listAppender);
        inboundMessage = mock(InboundMessage.class);
        messageHandlerObserver = new TestingSupportSolacePersistentMessageHandlerObserver();
        solacePersistentMessageHandler.setMessageHandlerObserver(messageHandlerObserver);
    }

    @Test
    void testCommandMessageHandler() {
        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId("messagingServiceId");
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
        doNothing().when(commandMessageProcessor).processMessage(any());

        solacePersistentMessageHandler.onMessage(inboundMessage);
        // Wait for the executor to process the message
        await().atMost(5, SECONDS).until(() -> messageHandlerObserver.hasInitiatedMessageProcessing(inboundMessage));

        verify(commandMessageProcessor, times(2)).castToMessageClass(any());
        verify(commandMessageProcessor, times(1)).processMessage(any());

        // There must be no interaction with scanCommandMessageProcessor
        verify(scanCommandMessageProcessor, times(0)).castToMessageClass(any());
        verify(scanCommandMessageProcessor, times(0)).processMessage(any());
    }

    @Test
    void testScanCommandMessageHandler() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        scanCommandMessage.setOrgId(eventPortalProperties.getOrganizationId());
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );

        solacePersistentMessageHandler.onMessage(inboundMessage);
        await().atMost(5, SECONDS).until(() -> messageHandlerObserver.hasInitiatedMessageProcessing(inboundMessage));

        verify(commandMessageProcessor, times(0)).castToMessageClass(any());
        verify(commandMessageProcessor, times(0)).processMessage(any());

        // There must be an interaction with scanCommandMessageProcessor
        verify(scanCommandMessageProcessor, times(1)).castToMessageClass(any());
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
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
        await().atMost(5, SECONDS).until(() -> messageHandlerObserver.hasFailedMessage(inboundMessage));
        assertThat(messageHandlerObserver.hasReceivedMessage(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasInitiatedMessageProcessing(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasCompletedMessageProcessing(inboundMessage)).isFalse();
        assertThat(messageHandlerObserver.hasAcknowledgedMessage(inboundMessage)).isTrue();

        List<ILoggingEvent> logs = listAppender.list;
        assertThat(logs.get(logs.size() - 1).getFormattedMessage()).isEqualTo("Unsupported message and/or processor encountered. Skipping processing");
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
        //unsupported message type, so we don't have an appropriate processor to handle the failure scenario
        verify(scanCommandMessageProcessor, times(0)).onFailure(any(), any());
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
