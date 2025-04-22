package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.common.model.ScanDestination;
import com.solace.maas.ep.common.model.ScanType;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.event.management.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.SingleScanSpecification;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestPropertySource(properties = {"event-portal.gateway.messaging.standalone=false", "event-portal.managed=false"})
@ActiveProfiles("TEST")
class ScanCommandMessageProcessorTests {

    @MockitoSpyBean
    private ScanManager scanManager;

    @MockitoBean
    private ScanService scanService;

    @MockitoSpyBean
    private ScanCommandMessageProcessor scanCommandMessageProcessor;

    @Autowired
    private MessagingServiceRepository repository;

    @MockitoBean
    private DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper;

    private ArgumentCaptor<SingleScanSpecification> captor;

    @BeforeEach
    void setUp() {
        reset(repository, scanManager, scanService, dynamicResourceConfigurationHelper);
        captor = ArgumentCaptor.forClass(SingleScanSpecification.class);
        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .url("localhost:9090")
                .build();
        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(MessagingServiceEntity.builder()
                        .type(MessagingServiceType.SOLACE.name())
                        .name("service1")
                        .id("messagingServiceId")
                        .connections(List.of(connectionDetailsEntity))
                        .build()));
    }

    @Test
    void processMessageWithoutResourceConfiguration() {
        ScanCommandMessage message = buildScanCommandMessage(null);
        when(scanService.singleScan(any())).thenReturn("scanId");
        scanCommandMessageProcessor.processMessage(message);
        verify(scanService).singleScan(captor.capture());
        SingleScanSpecification capturedSpec = captor.getValue();
        verifySingleScanSpec(capturedSpec, message);
        verifyNoInteractions(dynamicResourceConfigurationHelper);
        verify(scanManager, times(1)).scan(any());
    }

    @Test
    void processMessageWithResourceConfiguration() {
        ScanCommandMessage message = buildScanCommandMessage(List.of(
                EventBrokerResourceConfigTestHelper.buildResourceConfiguration(ResourceConfigurationType.SOLACE))
        );
        when(scanService.singleScan(any())).thenReturn("scanId");
        scanCommandMessageProcessor.processMessage(message);
        verify(scanService).singleScan(captor.capture());
        SingleScanSpecification capturedSpec = captor.getValue();
        verifySingleScanSpec(capturedSpec, message);
        verify(dynamicResourceConfigurationHelper, times(1)).loadSolaceBrokerResourceConfigurations(any());
        verify(scanManager, times(1)).scan(any());
    }

    private ScanCommandMessage buildScanCommandMessage(List<EventBrokerResourceConfiguration> resources) {
        ScanCommandMessage msg = new ScanCommandMessage(
                "messagingServiceId",
                "scanId",
                List.of(ScanType.SOLACE_ALL),
                List.of(ScanDestination.EVENT_PORTAL),
                resources);
        msg.setOrgId("orgId");
        msg.setTraceId("traceId");
        msg.setActorId("actorId");
        return msg;
    }

    private static void verifySingleScanSpec(SingleScanSpecification capturedSpec, ScanCommandMessage msg) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(capturedSpec.getScanId()).isEqualTo(msg.getScanId());
            softly.assertThat(capturedSpec.getTraceId()).isEqualTo(msg.getTraceId());
            softly.assertThat(capturedSpec.getOrgId()).isEqualTo(msg.getOrgId());
            softly.assertThat(capturedSpec.getMessagingServiceEntity().getId()).isEqualTo(msg.getMessagingServiceId());
            softly.assertThat(capturedSpec.getGroupId()).isNotBlank();
            softly.assertThat(capturedSpec.getRouteBundles()).hasSize(2);

        });
    }
}
