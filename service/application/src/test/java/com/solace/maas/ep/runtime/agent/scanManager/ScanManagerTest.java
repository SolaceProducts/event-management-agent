package com.solace.maas.ep.runtime.agent.scanManager;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaScanType;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.SolaceScanType;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.runtime.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.runtime.agent.service.ScanService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.solace.maas.ep.common.model.ScanType.KAFKA_ALL;
import static com.solace.maas.ep.common.model.ScanType.SOLACE_ALL;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanManagerTest {

    @InjectMocks
    ScanManager scanManager;

    @Mock
    MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    @Mock
    private ScanService scanService;

    @Test
    public void testKafkaScanManager() {
        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id("id")
                .name("name")
                .messagingServiceType(MessagingServiceType.KAFKA)
                .managementDetails(List.of())
                .build();

        when(messagingServiceDelegateService.getMessagingServiceById("id"))
                .thenReturn(messagingServiceEntity);

        when(scanService.singleScan(List.of(), 4)).thenReturn(Mockito.anyString());

        ScanRequestBO scanRequestBO =
                new ScanRequestBO("id", KAFKA_ALL, List.of(), List.of());
        scanManager.scan(scanRequestBO);

        ScanRequestBO scanRequestBOTopics =
                new ScanRequestBO("id", KAFKA_ALL,
                        List.of(KafkaScanType.KAFKA_TOPIC_LISTING.name()), List.of());
        scanManager.scan(scanRequestBOTopics);

        ScanRequestBO scanRequestBOConsumerGroups =
                new ScanRequestBO("id", KAFKA_ALL,
                        List.of(KafkaScanType.KAFKA_CONSUMER_GROUPS.name()), List.of());
        scanManager.scan(scanRequestBOConsumerGroups);

        assertThatNoException();
    }


    @Test
    public void testSolaceScanManager() {
        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id("id")
                .name("name")
                .messagingServiceType(MessagingServiceType.SOLACE)
                .managementDetails(List.of())
                .build();

        when(messagingServiceDelegateService.getMessagingServiceById("id"))
                .thenReturn(messagingServiceEntity);

        when(scanService.singleScan(List.of(), 2)).thenReturn(Mockito.anyString());

        ScanRequestBO scanRequestBO =
                new ScanRequestBO("id", SOLACE_ALL, List.of(), List.of());
        scanManager.scan(scanRequestBO);

        ScanRequestBO scanRequestBOQueues =
                new ScanRequestBO("id", SOLACE_ALL,
                        List.of(SolaceScanType.SOLACE_QUEUE_LISTING.name()), List.of());
        scanManager.scan(scanRequestBOQueues);

        ScanRequestBO scanRequestBOQueuesConfig =
                new ScanRequestBO("id", SOLACE_ALL,
                        List.of(SolaceScanType.SOLACE_SUBSCRIPTION_CONFIG.name()), List.of());
        scanManager.scan(scanRequestBOQueuesConfig);

        assertThatNoException();
    }
}
