package com.solace.maas.ep.runtime.agent.plugin.config;

import com.solace.maas.ep.runtime.agent.plugin.config.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientConnectionDetailsImpl implements ClientConnectionDetails {
    @Override
    public ConnectionDetailsEvent createConnectionDetails(String messagingServiceId,
                                                          MessagingServiceConnectionProperties messagingServiceConnection,
                                                          MessagingServiceType messagingServiceType) {
        List<AuthenticationDetailsEvent> authenticationDetailsEvents =
                messagingServiceConnection.getUsers().stream()
                        .map(messagingServiceUser ->
                                AuthenticationDetailsEvent.builder()
                                        .username(messagingServiceUser.getUsername())
                                        .password(messagingServiceUser.getPassword())
                                        .build())
                        .collect(Collectors.toList());

        ConnectionDetailsEvent connectionDetailsEvent =
                ConnectionDetailsEvent.builder()
                        .messagingServiceId(messagingServiceId)
                        .name(messagingServiceConnection.getName())
                        .authenticationDetails(authenticationDetailsEvents)
                        .build();

        if (messagingServiceType == MessagingServiceType.SOLACE) {
            connectionDetailsEvent.setConnectionUrl(messagingServiceConnection.getUrl());
            connectionDetailsEvent.setMsgVpn(messagingServiceConnection.getMsgVpn());
        } else if (messagingServiceType == MessagingServiceType.KAFKA) {
            connectionDetailsEvent.setConnectionUrl(messagingServiceConnection.getBootstrapServer());
        }

        return connectionDetailsEvent;
    }
}
