package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.repository.route.RouteRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class RouteServiceTests {
    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    private final String routeId = UUID.randomUUID().toString();

    @Test
    public void testSetupRoute() {
        RouteEntity returnedEntity = RouteEntity.builder()
                .id(routeId)
                .childRouteIds("")
                .active(true)
                .build();

        when(routeRepository.save(any(RouteEntity.class)))
                .thenReturn(returnedEntity);

        RouteEntity result = routeService.setupRoute(routeId);

        assertThat(result.getId())
                .isEqualTo(routeId);
    }

    @Test
    public void testStopRoute() {
        RouteEntity route = RouteEntity.builder()
                .id(routeId)
                .childRouteIds("")
                .active(true)
                .build();

        when(routeRepository.save(any(RouteEntity.class)))
                .thenReturn(RouteEntity.builder()
                        .id(routeId)
                        .childRouteIds("")
                        .active(false)
                        .build());

        routeService.stopRoute(route);

        assertThatNoException();
    }
}
