package com.solace.maas.ep.event.management.agent.plugin.common.util;

import org.springframework.stereotype.Component;

@Component
public class EnvironmentUtil {

    /**
     * Checks if custom CA certificates are present via the CUSTOM_CA_CERTS_PRESENT environment variable.
     * Currently, Only Private CEMAs are capable of importing and using custom ca certs for operating ep runtime operations
     * on brokers that may be setup with certs signed by custom ca
     * @return true if CUSTOM_CA_CERTS_PRESENT is set to "1", false otherwise
     */
    public boolean isCustomCACertPresent() {
        String customCaCertsPresent = System.getenv("CUSTOM_CA_CERTS_PRESENT");
        return "1".equals(customCaCertsPresent);
    }
}