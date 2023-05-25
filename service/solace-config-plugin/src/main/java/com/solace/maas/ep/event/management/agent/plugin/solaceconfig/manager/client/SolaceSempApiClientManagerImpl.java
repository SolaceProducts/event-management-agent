package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Data
public class SolaceSempApiClientManagerImpl implements MessagingServiceClientManager<SolaceSempApiClient> {

    public static final String SEMP_V2_BASE_PATH = "/SEMP/v2/config";

    public SolaceSempApiClientManagerImpl() {
    }

    @Override
    public SolaceSempApiClient getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.trace("Creating Solace SEMP API client for messaging service [{}].",
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
        String baseUrl = connectionDetailsEvent.getUrl().contains(SEMP_V2_BASE_PATH)
                ?connectionDetailsEvent.getUrl()
                :(connectionDetailsEvent.getUrl() + SEMP_V2_BASE_PATH);
        SempApiClient sempClient = SempApiClient.builder()
                .apiClient(new ApiClient())
                .username(MessagingServiceConfigurationUtil.getUsername(authenticationDetailsEvent))
                .password(MessagingServiceConfigurationUtil.getPassword(authenticationDetailsEvent))
                .msgVpn(MessagingServiceConfigurationUtil.getMsgVpn(connectionDetailsEvent))
                .baseurl(baseUrl)
                .build();

        log.trace("Solace SEMP API client created for {}.", connectionDetailsEvent.getMessagingServiceId());
        return new SolaceSempApiClient(sempClient);
    }
}
