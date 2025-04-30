package com.solace.maas.ep.event.management.agent.plugin.config;

import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceUsersProperties;
import com.solace.messaging.config.SolaceConstants;
import com.solace.messaging.config.SolaceProperties;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private final EventPortalPluginProperties eventPortalPluginProperties;

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
    public VMRProperties(EventPortalPluginProperties eventPortalPluginProperties) {
        this.eventPortalPluginProperties = eventPortalPluginProperties;
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
        System.setProperty("solace.proxy.type", "http");
        System.setProperty("solace.proxy.host", "localhost");
        System.setProperty("solace.proxy.port", "8443");
        parseVmrProperties();

        Properties properties = new Properties();

        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, url);
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, msgVpn);

        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME,
                SolaceConstants.AuthenticationConstants.AUTHENTICATION_SCHEME_BASIC);

        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, username);
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, password);
        properties.setProperty(SolaceProperties.ClientProperties.NAME, clientName);
        return properties;
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
}
