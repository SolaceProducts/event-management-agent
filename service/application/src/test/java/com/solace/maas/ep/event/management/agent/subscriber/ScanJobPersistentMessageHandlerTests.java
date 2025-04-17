package com.solace.maas.ep.event.management.agent.subscriber;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.ScanCommandMessageProcessor;
import com.solace.messaging.MessagingService;
import com.solace.messaging.receiver.InboundMessage;
import com.solace.messaging.receiver.PersistentMessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static com.solace.maas.ep.common.model.ScanDestination.EVENT_PORTAL;
import static com.solace.maas.ep.common.model.ScanDestination.FILE_WRITER;
import static com.solace.maas.ep.common.model.ScanType.SOLACE_ALL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "eventPortal.gateway.messaging.standalone=false",
        "eventPortal.managed=true",
        "eventPortal.incomingRequestQueueName = ep_core_ema_requests_123456_123123",
        "eventPortal.waitAckScanCompletePollIntervalSec=1",
        "eventPortal.waitAckScanCompleteTimeoutSec=10",


})
@Slf4j
class ScanJobPersistentMessageHandlerTests {

    @MockitoBean
    private ScanManager scanManager;

    @MockitoBean
    private PersistentMessageReceiver persistentMessageReceiver;

    @Autowired
    private MessagingService messagingService;

    @MockitoSpyBean
    private ScanCommandMessageProcessor scanCommandMessageProcessor;

    @Autowired
    private EventPortalProperties eventPortalProperties;

    @MockitoSpyBean
    private SolacePersistentMessageHandler solacePersistentMessageHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private InboundMessage inboundMessage;

    private ListAppender<ILoggingEvent> listAppenderPersistentMessageHandler;
    private ListAppender<ILoggingEvent> listAppendersCanCommandMessageProcessor;

    private TestingSupportSolacePersistentMessageHandlerObserver messageHandlerObserver;

    @BeforeEach
    void setup() {
        Logger loggerPersistentMessageHandler = (Logger) LoggerFactory.getLogger(SolacePersistentMessageHandler.class);
        listAppenderPersistentMessageHandler = new ListAppender<>();
        listAppenderPersistentMessageHandler.start();
        loggerPersistentMessageHandler.addAppender(listAppenderPersistentMessageHandler);

        Logger loggerScanCommandMessageProcessor = (Logger) LoggerFactory.getLogger(ScanCommandMessageProcessor.class);
        listAppendersCanCommandMessageProcessor = new ListAppender<>();
        listAppendersCanCommandMessageProcessor.start();
        loggerScanCommandMessageProcessor.addAppender(listAppendersCanCommandMessageProcessor);

        inboundMessage = mock(InboundMessage.class);
        messageHandlerObserver = new TestingSupportSolacePersistentMessageHandlerObserver();
        solacePersistentMessageHandler.setMessageHandlerObserver(messageHandlerObserver);
    }

    @Test
    void testScanManagerIsInvokedWithCorrectScanRequest() {
        ArgumentCaptor<ScanRequestBO> captor = ArgumentCaptor.forClass(ScanRequestBO.class);
        setupMocks(true);
        solacePersistentMessageHandler.onMessage(inboundMessage);
        await().atMost(5, SECONDS).until(() -> messageHandlerObserver.hasAcknowledgedMessage(inboundMessage));
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
        verify(scanManager).scan(captor.capture());
        ScanRequestBO capturedArgument = captor.getValue();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(capturedArgument.getOrgId()).isEqualTo("orgId");
        softly.assertThat(capturedArgument.getMessagingServiceId()).isEqualTo("messagingServiceId");
        softly.assertThat(capturedArgument.getScanId()).isEqualTo("scanId");
        softly.assertThat(capturedArgument.getScanTypes()).containsExactly(SOLACE_ALL.name());
        softly.assertThat(capturedArgument.getDestinations()).contains(EVENT_PORTAL.name(), FILE_WRITER.name());
        softly.assertAll();

        verify(scanCommandMessageProcessor, times(1)).processMessage(any());


    }


    @Test
    void testOnFailureIsInvokedWithCorrectScanCommandMessage() {
        ArgumentCaptor<ScanCommandMessage> captor = ArgumentCaptor.forClass(ScanCommandMessage.class);
        setupMocksForScanFailure();
        solacePersistentMessageHandler.onMessage(inboundMessage);
        await().atMost(5, SECONDS).until(() -> messageHandlerObserver.hasAcknowledgedMessage(inboundMessage));
        verify(scanCommandMessageProcessor).onFailure(any(), captor.capture());
        ScanCommandMessage capturedArgument = captor.getValue();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(capturedArgument.getOrgId()).isEqualTo("orgId");
        softly.assertThat(capturedArgument.getMessagingServiceId()).isEqualTo("messagingServiceId");
        softly.assertThat(capturedArgument.getScanId()).isEqualTo("scanId");
        softly.assertThat(capturedArgument.getScanTypes()).containsExactly(SOLACE_ALL);
        softly.assertAll();

        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
        verify(scanCommandMessageProcessor, times(1)).onFailure(any(), any());


    }

    // Test that the message handler is able to process a scan command message without an observer,
    // which will be the case when EMA is executed and not as unit / it test
    @Test
    void testPersistentMessageHandlerScanCommandMsgAckedWithoutObserver() {
        solacePersistentMessageHandler.setMessageHandlerObserver(null);
        setupMocks(true);
        solacePersistentMessageHandler.onMessage(inboundMessage);
        // we have to wait now for a second as there is no observer being notified
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());


    }

    @Test
    void testPersistentMessageHandlerScanCommandMsgAcked() {
        setupMocks(true);
        solacePersistentMessageHandler.onMessage(inboundMessage);

        //happy path - the message should be processed and acked
        await().atMost(5, SECONDS).until(() -> messageHandlerObserver.hasAcknowledgedMessage(inboundMessage));

        assertThat(messageHandlerObserver.hasReceivedMessage(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasInitiatedMessageProcessing(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasCompletedMessageProcessing(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasFailedMessage(inboundMessage)).isFalse();

        // the scan command message processor should be called once by the persistent message handler
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
        // if the EMA is managed, the waitForScanCompletion method should be called
        verify(scanCommandMessageProcessor, atLeastOnce()).waitForScanCompletion(any());
        // the message should be acked after the scan is complete
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
    }

    @Test
    void testPersistentMessageHandlerScanCommandTimeoutMsgAcked() {
        setupMocks(false);
        solacePersistentMessageHandler.onMessage(inboundMessage);
        // sleep for a while to allow the scan complete poll interval to pass

        await().atMost(eventPortalProperties.getWaitAckScanCompleteTimeoutSec() + 5, SECONDS).until(()
                -> messageHandlerObserver.hasAcknowledgedMessage(inboundMessage));
        assertThat(messageHandlerObserver.hasReceivedMessage(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasInitiatedMessageProcessing(inboundMessage)).isTrue();
        // timeout should be logged but the message should be still acked
        // timeout error handling is ultimately the responsibility of Event Portal
        assertThat(messageHandlerObserver.hasFailedMessage(inboundMessage)).isFalse();
        assertThat(messageHandlerObserver.hasCompletedMessageProcessing(inboundMessage)).isTrue();

        // the scan command message processor should be called once by the persistent message handler
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
        // if the EMA is managed, the waitForScanCompletion method should be called
        verify(scanCommandMessageProcessor, atLeastOnce()).waitForScanCompletion(any());
        // the message should be acked after the scan is complete, even though it is timed out
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
        List<ILoggingEvent> logs = listAppendersCanCommandMessageProcessor.list;
        assertThat(logs.get(logs.size() - 1).getFormattedMessage())
                .isEqualTo("Scan with id scanId did not complete within the expected time");
    }

    @Test
    void testPersistentMessageHandlerScanCommandExceptionThrownMsgAcked() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        scanCommandMessage.setOrgId("orgId");
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );
        when(scanManager.scan(any())).thenThrow(new RuntimeException("Test exception thrown on purpose"));
        solacePersistentMessageHandler.onMessage(inboundMessage);
        // sleep for a while to allow the scan complete poll interval to pass
        await().atMost(eventPortalProperties.getWaitAckScanCompleteTimeoutSec() + 2, SECONDS).until(()
                -> messageHandlerObserver.hasAcknowledgedMessage(inboundMessage));
        assertThat(messageHandlerObserver.hasReceivedMessage(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasInitiatedMessageProcessing(inboundMessage)).isTrue();
        // timeout should be logged but the message should be still acked
        // timeout error handling is ultimately the responsibility of Event Portal
        assertThat(messageHandlerObserver.hasFailedMessage(inboundMessage)).isTrue();
        assertThat(messageHandlerObserver.hasCompletedMessageProcessing(inboundMessage)).isFalse();

        List<ILoggingEvent> logs = listAppenderPersistentMessageHandler.list;
        assertThat(logs.get(logs.size() - 1).getFormattedMessage())
                .isEqualTo("Error while processing inbound message from queue for mopMessageSubclass: "
                        + ScanCommandMessage.class.getCanonicalName());

        // the scan command message processor should be called once by the persistent message handler
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
        verify(scanCommandMessageProcessor, atLeastOnce()).onFailure(any(), any());
        // the message should be acked after the scan is complete
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
    }


    private void setupMocks(boolean isScanComplete) {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        scanCommandMessage.setOrgId("orgId");
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );
        when(scanManager.scan(any())).thenReturn("scanId");
        when(scanManager.isScanComplete("scanId")).thenReturn(isScanComplete);
    }

    private void setupMocksForScanFailure() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        scanCommandMessage.setOrgId("orgId");
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );
        when(scanManager.scan(any())).thenThrow(new RuntimeException("Test exception thrown on purpose"));
    }

    private String jsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
