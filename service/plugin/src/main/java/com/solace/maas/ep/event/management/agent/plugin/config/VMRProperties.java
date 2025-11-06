package com.solace.maas.ep.event.management.agent.plugin.config;

import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceUsersProperties;
import com.solace.maas.ep.event.management.agent.plugin.common.util.EnvironmentUtil;
import com.solace.messaging.config.SolaceConstants;
import com.solace.messaging.config.SolaceProperties;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Slf4j
@Data
@Configuration
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class VMRProperties {

    private static final String SOLACE_PROXY_HOST = "solace.proxy.host";
    private static final String SOLACE_PROXY_PORT = "solace.proxy.port";
    private static final String SOLACE_PROXY_TYPE = "solace.proxy.type";
    private static final String SOLACE_PROXY_USERNAME = "solace.proxy.username";
    private static final String SOLACE_PROXY_PASSWORD = "solace.proxy.password";

    private final EventPortalPluginProperties eventPortalPluginProperties;
    private final EnvironmentUtil environmentUtil;

    /**
     * The host used to connect to the VMR
     */
    private String url;

    /**
     * The client username used to connect to the VMR
     */
    private String username;

    /**
     * The client username's password used to connect to the VMR
     */
    private String password;

    /**
     * The name of the VPN used to connect to the VMR
     */
    private String msgVpn;

    /**
     * The client name that this component will connect as to the VMR
     */
    private String clientName;

    /**
     * The absolute path to the TrustStore PEM certificate.
     */
    private String trustStoreDir;

    /**
     * Indicates if the agents is connecting back to EP or running in standalone mode
     */

    @Autowired
    public VMRProperties(EventPortalPluginProperties eventPortalPluginProperties, EnvironmentUtil environmentUtil) {
        this.eventPortalPluginProperties = eventPortalPluginProperties;
        this.environmentUtil = environmentUtil;
    }

    public void parseVmrProperties() {
        try {
            MessagingServiceConnectionProperties vmrConnectionProperties =
                    eventPortalPluginProperties.getGateway().getMessaging().getConnections().stream()
                            .findFirst().orElseThrow(() ->
                                    new NoSuchElementException("Could not find the connection properties."));

            MessagingServiceUsersProperties messagingServiceUsersProperties = vmrConnectionProperties.getUsers().stream()
                    .findFirst().orElseThrow(() ->
                            new NoSuchElementException("Could not find the user properties."));

            url = vmrConnectionProperties.getUrl();
            msgVpn = vmrConnectionProperties.getMsgVpn();

            if (eventPortalPluginProperties.getGateway().getMessaging().isRtoSession()) {
                trustStoreDir = vmrConnectionProperties.getTrustStoreDir();
            }
            username = messagingServiceUsersProperties.getUsername();
            password = messagingServiceUsersProperties.getPassword();
            String computedClientName = determineClientName();
            clientName = StringUtils.isEmpty(computedClientName)
                    ? messagingServiceUsersProperties.getClientName()
                    : computedClientName;
        } catch (NoSuchElementException e) {
            log.error("An error occurred while connecting to EP gateway: {}", e.getMessage());
        }
    }

    public Properties getVmrProperties() {
        parseVmrProperties();
        applyProxyConfiguration();

        Properties properties = new Properties();

        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, url);
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, msgVpn);

        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME,
                SolaceConstants.AuthenticationConstants.AUTHENTICATION_SCHEME_BASIC);

        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, username);
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, password);
        properties.setProperty(SolaceProperties.ClientProperties.NAME, clientName);

        //We will always use the default jks truststore for connecting to the EVMR
        configureDefaultTrustStore(properties);

        return properties;
    }

    private void configureDefaultTrustStore(Properties properties) {
        if (properties == null) {
            log.warn("Properties object is null");
            return;
        }

        if (!environmentUtil.isCustomCACertPresent()) {
            log.info("Custom CA certificates not present. Skipping explicit default truststore configuration.");
            return;
        }

        setDefaultTrustStore(properties);
    }

    void setDefaultTrustStore(Properties properties) {
        String javaHome = System.getProperty("java.home");
        if (StringUtils.isBlank(javaHome)) {
            log.warn("java.home system property is not set. Cannot configure default truststore for JCSMP.");
            return;
        }
        Path defaultTrustStorePath = Paths.get(javaHome, "lib", "security", "cacerts");
        File trustStoreFile = defaultTrustStorePath.toFile();

        if (!trustStoreFile.exists() || !trustStoreFile.canRead()) {
            log.warn("Default truststore not found or not readable at: {}. JCSMP connection may fail.", defaultTrustStorePath);
            return;
        }

        log.info("Custom CA certificates present. Explicitly configuring EVMR connection to use default truststore: {}", defaultTrustStorePath);
        properties.setProperty(SolaceProperties.TransportLayerSecurityProperties.TRUST_STORE_PATH, defaultTrustStorePath.toString());
    }

    public List<String> getRTOSessionProperties() {
        parseVmrProperties();

        List<String> sessionProperties = new ArrayList<>();

        sessionProperties.add(SessionHandle.PROPERTIES.HOST);
        sessionProperties.add(url);

        sessionProperties.add(SessionHandle.PROPERTIES.USERNAME);
        sessionProperties.add(username);

        sessionProperties.add(SessionHandle.PROPERTIES.VPN_NAME);
        sessionProperties.add(msgVpn);

        sessionProperties.add(SessionHandle.PROPERTIES.PASSWORD);
        sessionProperties.add(password);

        sessionProperties.add(SessionHandle.PROPERTIES.SSL_TRUST_STORE_DIR);
        sessionProperties.add(trustStoreDir);

        return sessionProperties;
    }

    private String determineClientName() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String hostNameHash = DigestUtils.sha256Hex(hostName);
            return String.format("%s-%s-%s", "ema", eventPortalPluginProperties.getRuntimeAgentId(), hostNameHash);
        } catch (UnknownHostException e) {
            log.warn("Could not determine host name when determining client name.", e);
            return StringUtils.EMPTY;
        }
    }

    private void applyProxyConfiguration() {
        try {
            MessagingServiceConnectionProperties connectionProps = getEventPortalGatewayConnectionProperties();
            if (Boolean.TRUE.equals(connectionProps.getProxyEnabled())) {
                validateProxySettings(connectionProps);
                setSolaceProxySystemProperties(connectionProps);
            } else {
                clearSolaceProxySystemProperties();
            }
        } catch (NoSuchElementException e) {
            log.error(
                    "Critical configuration error: Could not find Event Portal gateway connection properties for proxy setup. {}",
                    e.getMessage()
            );
            throw new IllegalArgumentException("Missing Event Portal gateway connection properties for proxy: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid proxy configuration: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid proxy configuration: " + e.getMessage(), e);
        }
    }

    private MessagingServiceConnectionProperties getEventPortalGatewayConnectionProperties() {
        return eventPortalPluginProperties.getGateway().getMessaging().getConnections().stream()
                .filter(c -> "eventPortalGateway".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Event Portal gateway connection properties not found."));
    }

    private void validateProxySettings(MessagingServiceConnectionProperties props) {
        Validate.isTrue(!StringUtils.isBlank(props.getProxyHost()), "Proxy host must be configured when proxy is enabled.");
        Validate.notNull(props.getProxyPort(), "Proxy port must be configured when proxy is enabled.");
        Validate.isTrue(props.getProxyPort() > 0 && props.getProxyPort() <= 65_535,
                "Proxy port must be a valid port number (1-65535).");
        Validate.isTrue("http".equalsIgnoreCase(props.getProxyType()), "Proxy type must be 'http'.");

        if (StringUtils.isNotBlank(props.getProxyUsername())) {
            Validate.isTrue(!StringUtils.isBlank(props.getProxyPassword()),
                    "Proxy password must be configured when proxy username is provided.");
        }
    }

    private void setSolaceProxySystemProperties(MessagingServiceConnectionProperties props) {
        log.info("Event management agent will be operating in web proxy mode. Applying HTTP proxy configuration: host={}, port={}, type={}",
                props.getProxyHost(), props.getProxyPort(), props.getProxyType());
        System.setProperty(SOLACE_PROXY_HOST, props.getProxyHost());
        System.setProperty(SOLACE_PROXY_PORT, String.valueOf(props.getProxyPort()));
        System.setProperty(SOLACE_PROXY_TYPE, props.getProxyType()); // JCSMP uses "http" or "socks5"

        if (StringUtils.isNotBlank(props.getProxyUsername())) {
            System.setProperty(SOLACE_PROXY_USERNAME, props.getProxyUsername());
            System.setProperty(SOLACE_PROXY_PASSWORD, props.getProxyPassword());
        } else {
            System.clearProperty(SOLACE_PROXY_USERNAME);
            System.clearProperty(SOLACE_PROXY_PASSWORD);
        }
    }

    private void clearSolaceProxySystemProperties() {
        log.info("Web proxy is disabled for this Event management agent");
        System.clearProperty(SOLACE_PROXY_HOST);
        System.clearProperty(SOLACE_PROXY_PORT);
        System.clearProperty(SOLACE_PROXY_TYPE);
        System.clearProperty(SOLACE_PROXY_USERNAME);
        System.clearProperty(SOLACE_PROXY_PASSWORD);
    }
}
