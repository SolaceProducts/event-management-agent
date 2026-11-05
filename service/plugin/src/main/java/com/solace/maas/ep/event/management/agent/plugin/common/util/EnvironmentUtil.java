package com.solace.maas.ep.event.management.agent.plugin.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component
public class EnvironmentUtil {

    private final UnaryOperator<String> env;

    public EnvironmentUtil() {
        this(System::getenv);
    }

    EnvironmentUtil(UnaryOperator<String> env) {
        this.env = env;
    }

    /**
     * Checks if custom CA certificates are present via the CUSTOM_CA_CERTS_PRESENT environment variable.
     * Currently, Only Private CEMAs are capable of importing and using custom ca certs for operating ep runtime operations
     * on brokers that may be setup with certs signed by custom ca
     * @return true if CUSTOM_CA_CERTS_PRESENT is set to "1", false otherwise
     */
    public boolean isCustomCACertPresent() {
        String customCaCertsPresent = env.apply("CUSTOM_CA_CERTS_PRESENT");
        return "1".equals(customCaCertsPresent);
    }

    /**
     * @return the path of the CA bundle EMA built its trust store from (see {@link CaBundleTrustStore}),
     * or null if EMA_CA_BUNDLE is not set
     */
    public String getCaBundlePath() {
        return CaBundleTrustStore.installedBundlePath();
    }

    /**
     * @return an empty directory to pass to OpenTofu as SSL_CERT_DIR alongside the CA bundle, or null if
     * EMA_CA_BUNDLE is not set
     */
    public String getCaBundleCertDirPath() {
        return CaBundleTrustStore.emptyCertDirPath();
    }

    /**
     * @return true if the SSL_CERT_FILE environment variable is set and not blank. Go, and so OpenTofu,
     * ignores a blank SSL_CERT_FILE.
     */
    public boolean isSslCertFileSet() {
        return StringUtils.isNotBlank(env.apply("SSL_CERT_FILE"));
    }

    /**
     * @return true if the SSL_CERT_DIR environment variable is set and not blank
     */
    public boolean isSslCertDirSet() {
        return StringUtils.isNotBlank(env.apply("SSL_CERT_DIR"));
    }
}
