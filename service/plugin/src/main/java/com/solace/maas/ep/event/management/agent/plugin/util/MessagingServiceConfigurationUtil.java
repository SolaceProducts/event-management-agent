package com.solace.maas.ep.event.management.agent.plugin.util;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessagingServiceConfigurationUtil {
    public static <T extends EventProperty> String getProperty(List<T> properties, String name) {
        if (properties == null) {
            return null;
        }
        Optional<T> property = properties.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
        return property.map(EventProperty::getValue)
                .orElse(null);
    }

    public static String getMsgVpn(ConnectionDetailsEvent connectionDetailsEvent) {
        return getProperty(connectionDetailsEvent.getProperties(), "msgVpn");
    }

    public static String getUsername(AuthenticationDetailsEvent authenticationDetailsEvent) {
        return authenticationDetailsEvent.getCredentials().stream()
                .findFirst().
                map(credential -> getProperty(credential.getProperties(), "username"))
                .orElse(null);
    }

    public static String getPassword(AuthenticationDetailsEvent authenticationDetailsEvent) {
        return authenticationDetailsEvent.getCredentials().stream()
                .findFirst().
                map(credential -> getProperty(credential.getProperties(), "password"))
                .orElse(null);
    }

    public static Map<String, String> getCredentialsProperties(AuthenticationDetailsEvent authenticationDetailsEvent) {
        return authenticationDetailsEvent.getCredentials().stream()
                .findFirst().
                map(credential -> credential.getProperties()
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(EventProperty::getName, EventProperty::getValue)))
                .orElse(null);
    }
}
