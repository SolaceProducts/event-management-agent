package com.solace.maas.ep.event.management.agent.plugin.solace.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import com.solace.maas.ep.event.management.agent.plugin.solace.SolaceTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SolaceTestConfig.class)
public class SolaceSempClientManagementTests {

    private final SolaceSempClientManagerImpl sempClientManager = new SolaceSempClientManagerImpl();

    @Test
    public void createSolaceSempClient() {
        ConnectionDetailsEvent connectionDetailsEvent = ConnectionDetailsEvent.builder()
                .url("localhost:1000")
                .messagingServiceId("messaging_service_id")
                .name("conn_name")
                .authenticationDetails(List.of(AuthenticationDetailsEvent.builder()
                        .protocol("SEMP")
                        .credentials(List.of(CredentialDetailsEvent.builder()
                                .properties(List.of(
                                        EventProperty.builder()
                                                .name("username")
                                                .value("user")
                                                .build(),
                                        EventProperty.builder()
                                                .name("password")
                                                .value("pass")
                                                .build()
                                )).build()))
                        .build()))
                .properties(List.of(
                        EventProperty.builder()
                                .name("msgVpn")
                                .value("messageVpn")
                                .build(),
                        EventProperty.builder()
                                .name("sempPageSize")
                                .value("100")
                                .build()))
                .build();

        SolaceHttpSemp semp = sempClientManager.getClient(connectionDetailsEvent);
        assertThat(semp.getSempClient().getConnectionUrl()).isEqualTo("localhost:1000");
        assertThat(semp.getSempClient().getMsgVpn()).isEqualTo("messageVpn");
        assertThat(semp.getSempClient().getUsername()).isEqualTo("user");
        assertThat(semp.getSempClient().getPassword()).isEqualTo("pass");
    }
}
