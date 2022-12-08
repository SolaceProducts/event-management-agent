package com.solace.maas.ep.event.management.agent.plugin.solace.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.NoSuchElementException;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Data
public class SolaceSempClientManagerImpl implements MessagingServiceClientManager<SolaceHttpSemp> {

    public SolaceSempClientManagerImpl() {
    }

    @Override
    public SolaceHttpSemp getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.trace("Creating Solace SEMP client for messaging service [{}].",
                connectionDetailsEvent.getMessagingServiceId());

        AuthenticationDetailsEvent authenticationDetailsEvent =
                connectionDetailsEvent.getAuthenticationDetails()
                        .stream()
                        .findFirst().orElseThrow(() -> {
                            String message = String.format("Could not find authentication details for service with id [%s].",
                                    connectionDetailsEvent.getMessagingServiceId());
                            log.error(message);
                            return new NoSuchElementException(message);
                        });

        SempClient sempClient = SempClient.builder()
                .webClient(WebClient.builder().build())
                .username(MessagingServiceConfigurationUtil.getUsername(authenticationDetailsEvent))
                .password(MessagingServiceConfigurationUtil.getPassword(authenticationDetailsEvent))
                .msgVpn(MessagingServiceConfigurationUtil.getMsgVpn(connectionDetailsEvent))
                .connectionUrl(connectionDetailsEvent.getUrl())
                .build();

        log.trace("Solace SEMP client created for {}.", connectionDetailsEvent.getMessagingServiceId());
        return new SolaceHttpSemp(sempClient);
    }
}
