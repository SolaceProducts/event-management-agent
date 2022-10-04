package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanDestinationEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanDestinationRepository;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientRepository;
import com.solace.maas.ep.event.management.agent.TestConfig;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@SuppressWarnings("PMD")
public class ScanRouteServiceTests {
    @Mock
    private ScanDestinationRepository scanDestinationRepository;

    @Mock
    private ScanRecipientRepository scanRecipientRepository;

    @Mock
    private RouteService routeService;

    @InjectMocks
    private ScanRouteService scanRouteService;

    @Test
    public void testSaveRecipients() {
        when(scanRecipientRepository.saveAll(any(Iterable.class)))
                .thenReturn(
                        List.of(
                                ScanRecipientEntity.builder()
                                        .build()
                        )
                );

        scanRouteService.saveRecipients(List.of(ScanRecipientEntity.builder().build()));

        assertThatNoException();
    }

    @Test
    public void testSaveDestination() {
        when(scanDestinationRepository.save(any(ScanDestinationEntity.class)))
                .thenReturn(ScanDestinationEntity.builder().build());

        scanRouteService.saveDestination(ScanDestinationEntity.builder().build());

        assertThatNoException();
    }

    @Test
    public void testSaveDestinations() {
        when(scanDestinationRepository.saveAll(any(Iterable.class)))
                .thenReturn(
                        List.of(
                                ScanDestinationEntity.builder()
                                        .build()
                        )
                );

        scanRouteService.saveDestinations(List.of(ScanDestinationEntity.builder().build()));

        assertThatNoException();
    }

    @Test
    public void testGetRecipientList() {
        when(scanRecipientRepository.findAllByScanIdAndRouteId(any(String.class), any(String.class)))
                .thenReturn(List.of(
                                ScanRecipientEntity.builder()
                                        .recipient("testRecipient")
                                        .scanDestinationId("destination1")
                                        .scan(ScanEntity.builder().build())
                                        .route(RouteEntity.builder().build())
                                        .build()
                        )
                );

        scanRouteService.getRecipientList("scan1", "route1");

        assertThatNoException();
    }

    @Test
    public void testGetDestionationList() {
        when(scanDestinationRepository.findAllByScanIdAndRouteId(any(String.class), any(String.class)))
                .thenReturn(List.of(
                                ScanDestinationEntity
                                        .builder()
                                        .route(RouteEntity.builder().build())
                                        .destination("testDestination")
                                        .build()
                        )
                );

        scanRouteService.getDestinationList("scan1", "route1");

        assertThatNoException();
    }

    @Test
    public void testSetupRoute() {
        scanRouteService.setupRoute("route1");

        assertThatNoException();
    }
}
