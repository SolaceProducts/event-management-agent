package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRepository;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanItemMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemDTO;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanControllerTest {
    @Autowired
    private ScanItemMapper scanItemMapper;

    @Test
    public void scanListTest() {
        ScanRepository repository = mock(ScanRepository.class);
        ScanService scanService = new ScanService(repository, null, null, null);

        ScanManager scanManager = new ScanManager(null, scanService);

        when(repository.findAll())
                .thenReturn(List.of(ScanEntity.builder()
                        .id("scan_id")
                        .createdAt(Instant.now())
                        .messagingService(MessagingServiceEntity.builder()
                                .id("msg_svc_id")
                                .name("messaging service")
                                .type("SOLACE")
                                .build())
                        .build()));

        ScanController controller = new ScanControllerImpl(scanManager, scanItemMapper);

        ResponseEntity<List<ScanItemDTO>> reply =
                controller.list();

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reply.getBody().size()).isEqualTo(1);
        ScanItemDTO scanItemDTO = reply.getBody().get(0);
        assertThat(scanItemDTO.getId()).isEqualTo("scan_id");
        assertThat(scanItemDTO.getMessagingServiceId()).isEqualTo("msg_svc_id");
        assertThat(scanItemDTO.getMessagingServiceName()).isEqualTo("messaging service");
        assertThat(scanItemDTO.getMessagingServiceType()).isEqualTo("SOLACE");
        assertThat(scanItemDTO.getCreatedAt()).isNotNull();

        assertThatNoException();
    }
}
