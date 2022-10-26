package com.solace.maas.ep.event.management.agent.plugin.route.delegate;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.ScanDataImportDelegateImpl;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataImportDelegateImplTests {

    @InjectMocks
    private ScanDataImportDelegateImpl scanDataImportDelegate;

    private List<RouteBundle> destinations = List.of(
            RouteBundle.builder()
                    .destinations(List.of())
                    .recipients(List.of())
                    .routeId("testRoute")
                    .firstRouteInChain(false)
                    .messagingServiceId("service1")
                    .build());

    @Test
    public void testGenerateRouteList() {
        List<RouteBundle> routeBundles =
                scanDataImportDelegate.generateRouteList(destinations, List.of(), "testScanType",
                        "service1");

        assertThatNoException();
        assertThat(!routeBundles.isEmpty());

        assertThat(routeBundles.stream().findFirst().orElseThrow().getMessagingServiceId()).isEqualTo("service1");
        assertThat(routeBundles.stream().findFirst().orElseThrow().getRouteId()).isEqualTo("seda:manualImport");
        assertThat(routeBundles.stream().findFirst().orElseThrow().getScanType()).isEqualTo("testScanType");
        assertThat(routeBundles.stream().findFirst().orElseThrow().getDestinations()).isEqualTo(destinations);
    }
}
