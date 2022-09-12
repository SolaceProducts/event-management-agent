package com.solace.maas.ep.runtime.agent.scanManager;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.runtime.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.runtime.agent.service.ScanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
    public void testScanManager() {
        MessagingServiceEntity messagingServiceEntity = MessagingServiceEntity.builder()
                .id("id")
                .name("name")
                .messagingServiceType(MessagingServiceType.KAFKA.name())
                .managementDetails(List.of())
                .build();

        when(messagingServiceDelegateService.getMessagingServiceById("id"))
                .thenReturn(messagingServiceEntity);

        when(scanService.singleScan(List.of(), 4)).thenReturn(Mockito.anyString());

        ScanRequestBO scanRequestBO =
                new ScanRequestBO("id", List.of("topics"), List.of());

        Assertions.assertThrows(NullPointerException.class, () -> scanManager.scan(scanRequestBO));

        ScanRequestBO scanRequestBOTopics =
                new ScanRequestBO("id", List.of("TEST_SCAN_1"), List.of());
        Assertions.assertThrows(NullPointerException.class, () -> scanManager.scan(scanRequestBO));

        ScanRequestBO scanRequestBOConsumerGroups =
                new ScanRequestBO("id", List.of("TEST_SCAN_2"), List.of());
        Assertions.assertThrows(NullPointerException.class, () -> scanManager.scan(scanRequestBO));
    }
}
