package com.solace.maas.ep.event.management.agent.webProxy;

import com.solace.maas.ep.event.management.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.GatewayProperties;
import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.GatewayMessagingProperties;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceConnectionProperties;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServiceUsersProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@ExtendWith(MockitoExtension.class)
class VMRPropertiesTests {

    @Mock
    private EventPortalPluginProperties eventPortalPluginProperties;

    @Mock
    private GatewayProperties gatewayProperties;

    @Mock
    private GatewayMessagingProperties gatewayMessagingProperties;

    // Class under test
    private VMRProperties vmrProperties;

    private Properties systemPropertiesBackup;

    @BeforeEach
    void setUp() {
        // MockitoExtension handles mock initialization
        when(eventPortalPluginProperties.getGateway()).thenReturn(gatewayProperties);
        when(gatewayProperties.getMessaging()).thenReturn(gatewayMessagingProperties);
        vmrProperties = new VMRProperties(eventPortalPluginProperties);

        // Backup system properties
        systemPropertiesBackup = new Properties();
        systemPropertiesBackup.putAll(System.getProperties());

        // Clear any solace proxy properties before each test
        System.clearProperty("solace.proxy.host");
        System.clearProperty("solace.proxy.port");
        System.clearProperty("solace.proxy.type");
        System.clearProperty("solace.proxy.username");
        System.clearProperty("solace.proxy.password");
    }

    @AfterEach
    void tearDown() {
        // Restore system properties
        System.setProperties(systemPropertiesBackup);
    }

    private MessagingServiceConnectionProperties createConnectionProperties(
            Boolean proxyEnabled, String proxyHost, Integer proxyPort, String proxyType,
            String proxyUsername, String proxyPassword) {
        return MessagingServiceConnectionProperties.builder()
                .name("eventPortalGateway")
                .url("tcp://localhost:55555")
                .msgVpn("default")
                .users(Collections.singletonList(MessagingServiceUsersProperties.builder()
                        .username("testuser")
                        .password("testpass")
                        .clientName("testClient")
                        .build()))
                .proxyEnabled(proxyEnabled)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyType(proxyType)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .build();
    }

    @Test
    @SneakyThrows
    void testGetVmrPropertiesProxyDisabled() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(false, null, null, null, null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        vmrProperties.getVmrProperties();

        assertThat(System.getProperty("solace.proxy.host")).isNull();
        assertThat(System.getProperty("solace.proxy.port")).isNull();
        assertThat(System.getProperty("solace.proxy.type")).isNull();
        assertThat(System.getProperty("solace.proxy.username")).isNull();
        assertThat(System.getProperty("solace.proxy.password")).isNull();
    }

    @Test
    @SneakyThrows
    void testGetVmrPropertiesProxyEnabledNoAuth() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", 8080, "http", null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        vmrProperties.getVmrProperties();

        assertThat(System.getProperty("solace.proxy.host")).isEqualTo("proxy.example.com");
        assertThat(System.getProperty("solace.proxy.port")).isEqualTo("8080");
        assertThat(System.getProperty("solace.proxy.type")).isEqualTo("http");
        assertThat(System.getProperty("solace.proxy.username")).isNull();
        assertThat(System.getProperty("solace.proxy.password")).isNull();
    }

    @Test
    @SneakyThrows
    void testGetVmrPropertiesProxyEnabledWithAuth() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(
                true,
                "secureproxy.example.com",
                8443,
                "http",
                "proxyuser",
                "proxypass"
        );
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        vmrProperties.getVmrProperties();

        assertThat(System.getProperty("solace.proxy.host")).isEqualTo("secureproxy.example.com");
        assertThat(System.getProperty("solace.proxy.port")).isEqualTo("8443");
        assertThat(System.getProperty("solace.proxy.type")).isEqualTo("http");
        assertThat(System.getProperty("solace.proxy.username")).isEqualTo("proxyuser");
        assertThat(System.getProperty("solace.proxy.password")).isEqualTo("proxypass");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledButHostBlank() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, " ", 8080, "http", null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy host must be configured when proxy is enabled.");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledButPortNull() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", null, "http", null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy port must be configured when proxy is enabled.");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledButPortInvalidZero() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", 0, "http", null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy port must be a valid port number (1-65535).");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledButPortInvalidNegative() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", -1, "http", null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy port must be a valid port number (1-65535).");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledButPortInvalidTooLarge() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(
                true,
                "proxy.example.com",
                65_536, "http",
                null,
                null
        );
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy port must be a valid port number (1-65535).");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledButTypeInvalid() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", 8080, "socks5", null, null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy type must be 'http'.");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledUsernamePresentPasswordBlank() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", 8080, "http", "useronly", " ");
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy password must be configured when proxy username is provided.");
    }

    @Test
    @SneakyThrows
    void testProxyEnabledUsernamePresentPasswordNull() {
        MessagingServiceConnectionProperties connectionProps = createConnectionProperties(true, "proxy.example.com", 8080, "http", "useronly", null);
        when(gatewayMessagingProperties.getConnections()).thenReturn(Collections.singletonList(connectionProps));

        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Proxy password must be configured when proxy username is provided.");
    }

    @Test
    @SneakyThrows
    void testNoGatewayConnectionProperties() {
        // Simulate basic properties being set
        vmrProperties.setUrl("tcp://localhost:55555");
        vmrProperties.setMsgVpn("default");
        vmrProperties.setUsername("testuser");
        vmrProperties.setPassword("testpass");

        // Simulate that the list of connections does not contain "eventPortalGateway"
        MessagingServiceConnectionProperties otherConnection = MessagingServiceConnectionProperties.builder()
                .name("otherGateway")
                .users(Collections.emptyList()) // Initialize users with an empty list
                .build();
        when(gatewayMessagingProperties.getConnections()).thenReturn(List.of(otherConnection));

        // The call to getVmrProperties will trigger parseVmrProperties and then applyProxyConfiguration.
        // applyProxyConfiguration will then fail to find the "eventPortalGateway".
        assertThatThrownBy(() -> vmrProperties.getVmrProperties())
                .hasCauseExactlyInstanceOf(NoSuchElementException.class)
                .hasRootCauseMessage("Event Portal gateway connection properties not found.");
    }
}