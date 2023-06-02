package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.manualimport.ManualImportFilesEntity;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.event.management.agent.service.ManualImportDetailsService;
import com.solace.maas.ep.event.management.agent.service.ManualImportFilesService;
import com.solace.messaging.receiver.InboundMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.solace.maas.ep.common.model.ScanDestination.EVENT_PORTAL;
import static com.solace.maas.ep.common.model.ScanType.KAFKA_ALL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class MessageReceiverTests {

    @Mock
    SolaceConfiguration solaceConfiguration;

    @Mock
    ScanManager scanManager;

    @Mock
    SolaceSubscriber solaceSubscriber;

    @Mock
    InboundMessage inboundMessage;

    @Autowired
    ScanRequestMapper scanRequestMapper;

    @Mock
    ProducerTemplate producerTemplate;

    @Mock
    ManualImportFilesService manualImportFilesService;

    @Mock
    ManualImportDetailsService manualImportDetailsService;

    @Test
    @SneakyThrows
    public void scanReceiver() {

        String basePayload = "{\n" +
                "  \"mopVer\" : \"1\",\n" +
                "  \"mopProtocol\" : \"event\",\n" +
                "  \"mopMsgType\" : \"generic\",\n" +
                "  \"msgUh\" : \"ignore\",\n" +
                "  \"repeat\" : false,\n" +
                "  \"isReplyMessage\" : false,\n" +
                "  \"msgPriority\" : 4,\n" +
                "  \"traceId\" : \"80817f0d335b6221\",\n" +
                "  \"scanTypes\" : [\"KAFKA_ALL\"],\n" +
                "  \"messagingServiceId\" : \"someId\"";

        String payloadWithoutDestinations = basePayload + "\n }";
        when(inboundMessage.getPayloadAsString()).thenReturn(payloadWithoutDestinations);
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                "com.solace.maas.ep.common.messages.ScanCommandMessage");
        when(inboundMessage.getDestinationName()).thenReturn("anyTopic");

        ScanCommandMessageHandler scanCommandMessageHandler = new ScanCommandMessageHandler(
                solaceConfiguration, solaceSubscriber, scanManager);

        String topic = scanCommandMessageHandler.getTopicString();
        log.info("topic: {}", topic);
        scanCommandMessageHandler.onMessage(inboundMessage);

        String payloadWithDestinations = basePayload + "," + "\n  \"destinations\" : [\"EVENT_PORTAL\"]\n" + "}";
        when(inboundMessage.getPayloadAsString()).thenReturn(payloadWithDestinations);
        scanCommandMessageHandler.onMessage(inboundMessage);
    }

    @Test
    @SneakyThrows
    public void testBadClass() {
        assertThrows(RuntimeException.class, () -> {
            when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn("badClass");
            ScanCommandMessageHandler scanCommandMessageHandler = new ScanCommandMessageHandler(
                    solaceConfiguration, solaceSubscriber, scanManager);
            scanCommandMessageHandler.onMessage(inboundMessage);
        });
    }

    @Test
    @SneakyThrows
    public void testScanCommandMessage() {
        ScanCommandMessageHandler scanCommandMessageHandler = new ScanCommandMessageHandler(
                solaceConfiguration, solaceSubscriber, scanManager);

        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(KAFKA_ALL), List.of(EVENT_PORTAL));

        scanCommandMessageHandler.receiveMessage("test", scanCommandMessage);
        assertThatNoException();
    }

    @Test
    public void testScanCommandMessageMOPProtocol() {
        ScanCommandMessage scanCommandMessage =
                new ScanCommandMessage("messagingServiceId",
                        "scanId", List.of(KAFKA_ALL), List.of(EVENT_PORTAL));
        assertThat(scanCommandMessage.getMopProtocol()).isEqualTo(MOPProtocol.scanDataControl);
    }

    @Test
    @SneakyThrows
    public void startImportScanCommandMessageHandlerTest() {
        String payload = "{\n" +
                "  \"mopVer\" : \"1\",\n" +
                "  \"mopProtocol\" : \"event\",\n" +
                "  \"mopMsgType\" : \"generic\",\n" +
                "  \"msgUh\" : \"ignore\",\n" +
                "  \"repeat\" : false,\n" +
                "  \"isReplyMessage\" : false,\n" +
                "  \"msgPriority\" : 4,\n" +
                "  \"traceId\" : \"80817f0d335b6221\",\n" +
                "  \"scanId\" : \"someScanId\",\n" +
                "  \"scanTypes\" : [\"a\",\"b\",\"c\"]\n" +
                "}";

        when(inboundMessage.getPayloadAsString()).thenReturn(payload);
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                "com.solace.maas.ep.common.messages.ScanDataImportMessage");
        when(inboundMessage.getDestinationName()).thenReturn("anyTopic");

        List<ManualImportFilesEntity> mockedManualImportFilesEntity = new ArrayList<>();
        mockedManualImportFilesEntity.add(
                ManualImportFilesEntity.builder()
                        .id("idManualImportFilesEntity")
                        .fileName("someFileName.json")
                        .dataEntityType("someDataEntityType")
                        .scanId("someScan")
                        .build());
        when(manualImportFilesService.getAllByScanId(anyString())).thenReturn(mockedManualImportFilesEntity);

        ManualImportDetailsEntity mockedManualImportDetailsEntity = ManualImportDetailsEntity.builder()
                .id("idManualImportDetailsEntity")
                .importId("someImportId")
                .scheduleId("someScheduleId")
                .emaId("someEmaId")
                .scanId("someScanId")
                .build();
        when(manualImportDetailsService.getByScanId(anyString())).thenReturn(Optional.ofNullable(mockedManualImportDetailsEntity));

        StartImportScanCommandMessageHandler startImportScanCommandMessageHandler = new StartImportScanCommandMessageHandler(solaceConfiguration,
                solaceSubscriber, producerTemplate, manualImportFilesService, manualImportDetailsService);
        startImportScanCommandMessageHandler.onMessage(inboundMessage);
        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void heartbeatReceiverTest() {
        String payload = "{\n" +
                "  \"mopVer\" : \"1\",\n" +
                "  \"mopProtocol\" : \"event\",\n" +
                "  \"mopMsgType\" : \"generic\",\n" +
                "  \"msgUh\" : \"ignore\",\n" +
                "  \"repeat\" : false,\n" +
                "  \"isReplyMessage\" : false,\n" +
                "  \"msgPriority\" : 4,\n" +
                "  \"traceId\" : \"80817f0d335b6221\",\n" +
                "  \"runtimeAgentId\" : \"someId\",\n" +
                "  \"timestamp\" : \"2022-07-21T20:16:21.982427Z\"\n" +
                "}";

        when(inboundMessage.getPayloadAsString()).thenReturn(payload);
        when(inboundMessage.getProperty(MOPConstants.MOP_MSG_META_DECODER)).thenReturn(
                "com.solace.maas.ep.common.messages.HeartbeatMessage");
        when(inboundMessage.getDestinationName()).thenReturn("anyTopic");

        HeartbeatMessageHandler heartbeatMessageHandler = new HeartbeatMessageHandler(solaceConfiguration,
                solaceSubscriber);
        heartbeatMessageHandler.onMessage(inboundMessage);

    }
}
