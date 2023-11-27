package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.SolaceTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2CommandType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SolaceTestConfig.class)
public class SolaceConfigRouteDelegateImplTests {
    @InjectMocks
    private SolaceConfigRouteDelegateImpl solaceRouteDelegate;

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
    public void testGenerateMsgVpnAclProfileRouteList() {
        List<RouteBundle> routeBundles =
                solaceRouteDelegate.generateRouteList(destinations, List.of(), SolaceSEMPv2CommandType.MsgVpnAclProfile.name(),
                        "service1");

        assertThatNoException();
        assertThat(!routeBundles.isEmpty());
        assertThat(routeBundles.size()== 1);
        assertThat(MsgVpnAclProfile.class.getSimpleName().equals(routeBundles.get(0).getScanType()));
    }

}
