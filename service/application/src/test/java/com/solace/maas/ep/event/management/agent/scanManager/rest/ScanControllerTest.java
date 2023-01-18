package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanItemMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanControllerTest {

    @Mock
    private ScanItemMapper scanItemMapper;

    @Mock
    private EventPortalProperties eventPortalProperties;

    @Mock
    private ScanManager scanManager;

    @InjectMocks
    private ScanControllerImpl controller;

    @Test
    public void scanListTest() {
        List<ScanItemBO> responseList = List.of(ScanItemBO.builder()
                        .id("scan_id")
                        .createdAt(Instant.now())
                        .messagingServiceId("msg_svc_id")
                        .messagingServiceName("messaging service")
                        .messagingServiceType("SOLACE")
                        .emaId("ema1")
                        .build());

        ScanItemDTO mappedResponse = ScanItemDTO.builder()
                .id("scan_id")
                .createdAt(Instant.now())
                .messagingServiceId("msg_svc_id")
                .messagingServiceName("messaging service")
                .messagingServiceType("SOLACE")
                .emaId("ema1")
                .build();

        Page<ScanItemBO> response = new PageImpl<>(responseList, PageRequest.of(0,1), responseList.size());

        when(scanManager.findAll(any(Pageable.class)))
                .thenReturn(response);
        when(scanItemMapper.map(any(ScanItemBO.class)))
                .thenReturn(mappedResponse);

        ResponseEntity<Page<ScanItemDTO>> reply =
                controller.list(PageRequest.of(0,1));

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(reply.getBody()).toList().size()).isEqualTo(1);
        ScanItemDTO scanItemDTO = reply.getBody().toList().get(0);
        assertThat(scanItemDTO.getId()).isEqualTo("scan_id");
        assertThat(scanItemDTO.getMessagingServiceId()).isEqualTo("msg_svc_id");
        assertThat(scanItemDTO.getMessagingServiceName()).isEqualTo("messaging service");
        assertThat(scanItemDTO.getMessagingServiceType()).isEqualTo("SOLACE");
        assertThat(scanItemDTO.getCreatedAt()).isNotNull();

        assertThatNoException();
    }
}
