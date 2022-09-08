package com.solace.maas.ep.runtime.agent.plugin.kafka.manager.client;

import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp.SempClient;
import com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp.SolaceHttpSemp;
import com.solace.maas.ep.runtime.agent.plugin.properties.SolacePluginProperties;
import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.NoSuchElementException;

@ExcludeFromJacocoGeneratedReport
@Data
public class SolaceSempClientManagerImpl implements MessagingServiceClientManager<SolaceHttpSemp> {

    private SolacePluginProperties solacePluginProperties;

    public SolaceSempClientManagerImpl(SolacePluginProperties solacePluginProperties) {
        this.solacePluginProperties = solacePluginProperties;
    }

    @Override
    public SolaceHttpSemp getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        AuthenticationDetailsEvent authenticationDetailsEvent =
                connectionDetailsEvent.getAuthenticationDetails()
                        .stream()
                        .findFirst().orElseThrow(() -> new NoSuchElementException(
                                String.format("Could not find authentication details for service with id %s",
                                        connectionDetailsEvent.getMessagingServiceId())));

        SempClient sempClient = SempClient.builder()
                .webClient(WebClient.builder().build())
                .username(authenticationDetailsEvent.getUsername())
                .password(authenticationDetailsEvent.getPassword())
                .msgVpn(connectionDetailsEvent.getMsgVpn())
                .connectionUrl(connectionDetailsEvent.getConnectionUrl())
                .build();

        SolaceHttpSemp solaceHttpSemp = new SolaceHttpSemp(sempClient);
        return solaceHttpSemp;
    }
}
