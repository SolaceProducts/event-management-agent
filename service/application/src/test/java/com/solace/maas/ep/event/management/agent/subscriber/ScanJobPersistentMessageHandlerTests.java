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
import static org.assertj.core.api.Assertions.assertThat;
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
        "eventPortal.waitAckScanCompletePollInterval=1",
        "eventPortal.waitAckScanCompleteTimeout=10",


})
@Slf4j
class ScanJobPersistentMessageHandlerTests {

    @MockBean
    private ScanManager scanManager;

    @MockBean
    private PersistentMessageReceiver persistentMessageReceiver;

    @Autowired
    private MessagingService messagingService;

    @SpyBean
    private ScanCommandMessageProcessor scanCommandMessageProcessor;

    @Autowired
    private EventPortalProperties eventPortalProperties;

    @SpyBean
    private SolacePersistentMessageHandler solacePersistentMessageHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private InboundMessage inboundMessage;

    private ListAppender<ILoggingEvent> listAppenderPersistentMessageHandler;
    private ListAppender<ILoggingEvent> listAppendersCanCommandMessageProcessor;

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
    }

    @Test
    void testPersistentMessageHandlerScanCommandMsgAcked() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );
        when(scanManager.scan(any())).thenReturn("scanId");
        when(scanManager.isScanComplete("scanId")).thenReturn(true);
        solacePersistentMessageHandler.onMessage(inboundMessage);
       // sleep for a while to allow the scan complete poll interval to pass
        waitForScanCompletePolling(2);
        // the scan command message processor should be called once by the persistent message handler
       verify(scanCommandMessageProcessor, times(1)).processMessage(any());
       // if the EMA is managed, the waitForScanCompletion method should be called
        verify(scanCommandMessageProcessor, atLeastOnce()).waitForScanCompletion(any());
        // the message should be acked after the scan is complete
       verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
    }

    @Test
    void testPersistentMessageHandlerScanCommandTimeoutMsgAcked() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(SOLACE_ALL), List.of(EVENT_PORTAL));
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );
        when(scanManager.scan(any())).thenReturn("scanId");
        // the scan is not complete and will never be ;-)
        // the waitForScanCompletion method will throw an exception after the timeout
        when(scanManager.isScanComplete("scanId")).thenReturn(false);
        solacePersistentMessageHandler.onMessage(inboundMessage);
        // sleep for a while to allow the scan complete poll interval to pass
        waitForScanCompletePolling(5);


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
        when(inboundMessage.getPayloadAsString()).thenReturn(jsonString(scanCommandMessage));
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                ScanCommandMessage.class.getCanonicalName()
        );
        when(scanManager.scan(any())).thenThrow(new RuntimeException("Test exception thrown on purpose"));
        solacePersistentMessageHandler.onMessage(inboundMessage);
        // sleep for a while to allow the scan complete poll interval to pass
        waitForScanCompletePolling(2);
        List<ILoggingEvent> logs = listAppenderPersistentMessageHandler.list;
        assertThat(logs.get(logs.size() - 1).getFormattedMessage())
                .isEqualTo("Error while processing inbound message from queue for mopMessageSubclass: " + ScanCommandMessage.class.getCanonicalName());

        // the scan command message processor should be called once by the persistent message handler
        verify(scanCommandMessageProcessor, times(1)).processMessage(any());
        verify(scanCommandMessageProcessor, atLeastOnce()).onFailure(any(), any());
        // the message should be acked after the scan is complete
        verify(solacePersistentMessageHandler.getPersistentMessageReceiver(), times(1)).ack(inboundMessage);
    }

    private void waitForScanCompletePolling(Integer additionalSeconds) {
        try {
            int timeout =eventPortalProperties.getWaitAckScanCompleteTimeoutSec() + additionalSeconds;
            Thread.sleep(timeout* 1000L );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String jsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
