package com.solace.maas.ep.runtime.agent.plugin.route.delegate;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.ScanDataPublisherDelegateImpl;
import com.solace.maas.ep.runtime.agent.plugin.route.RouteBundle;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanDataPublisherDelegateImplTests {

    @InjectMocks
    private ScanDataPublisherDelegateImpl scanDataPublisherDelegate;

    private List<RouteBundle> destinations = List.of(
            RouteBundle.builder()
                    .destinations(List.of())
                    .recipients(List.of())
                    .routeId("testRoute")
                    .firstRouteInChain(false)
                    .messagingServiceId("service1")
                    .build()
    );

    @Test
    public void testGenerateSolaceQueueListingRouteList() {
        List<RouteBundle> routeBundles =
                scanDataPublisherDelegate.generateRouteList(destinations, List.of(), "testScanType",
                        "service1");

        assertThatNoException();
        assertThat(!routeBundles.isEmpty());
    }
}
