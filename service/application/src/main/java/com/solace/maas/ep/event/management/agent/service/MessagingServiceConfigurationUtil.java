package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServicePropertyIf;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.AuthenticationDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.CredentialDetailsEntity;

import java.util.List;
import java.util.Optional;

public class MessagingServiceConfigurationUtil {
    public static <T extends MessagingServicePropertyIf> String getProperty(List<T> properties, String name) {
        if (properties == null) {
            return null;
        }
        Optional<T> property = properties.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
        return property.map(MessagingServicePropertyIf::getProperty)
                .orElse(null);
    }

    public static String getMsgVpn(ConnectionDetailsEntity connectionDetails) {
        return getProperty(connectionDetails.getProperties(), "msgVpn");
    }

    public static String getSempPageSize(ConnectionDetailsEntity connectionDetails) {
        return getProperty(connectionDetails.getProperties(), "sempPageSize");
    }

    public static String getUsername(CredentialDetailsEntity credentialDetails) {
        return getProperty(credentialDetails.getProperties(), "username");
    }

    public static String getPassword(CredentialDetailsEntity credentialDetails) {
        return getProperty(credentialDetails.getProperties(), "password");
    }

    public static String getAuthenticationType(AuthenticationDetailsEntity authenticationDetails) {
        return getProperty(authenticationDetails.getProperties(), "type");
    }
}
