package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.model.EventBrokerAuthenticationConfiguration;
import com.solace.maas.ep.common.model.EventBrokerConnectionConfiguration;
import com.solace.maas.ep.common.model.EventBrokerCredentialConfiguration;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public final class EventBrokerResourceConfigTestHelper {

    private EventBrokerResourceConfigTestHelper() {
        throw new UnsupportedOperationException("Instantiation not allowed");

    }

    public static EventBrokerResourceConfiguration buildResourceConfiguration(ResourceConfigurationType resourceConfigurationType) {
        EventBrokerResourceConfiguration resourceConfiguration = new EventBrokerResourceConfiguration();
        resourceConfiguration.setId(RandomStringUtils.randomAlphabetic(11));
        resourceConfiguration.setName(RandomStringUtils.randomAlphabetic(5));
        resourceConfiguration.setResourceConfigurationType(resourceConfigurationType);

        EventBrokerConnectionConfiguration connection = new EventBrokerConnectionConfiguration();
        connection.setUrl("https://localhost:8080");
        connection.setMsgVpn("myVpn");
        connection.setName(resourceConfiguration.getName());

        EventBrokerAuthenticationConfiguration auth = new EventBrokerAuthenticationConfiguration();

        EventBrokerCredentialConfiguration cred = new EventBrokerCredentialConfiguration();
        cred.setUserName(RandomStringUtils.randomAlphabetic(11));
        cred.setPassword(RandomStringUtils.randomAlphabetic(11));
        auth.setCredential(cred);
        connection.setAuthentication(auth);
        resourceConfiguration.setConnections(List.of(connection));
        return resourceConfiguration;
    }

}
