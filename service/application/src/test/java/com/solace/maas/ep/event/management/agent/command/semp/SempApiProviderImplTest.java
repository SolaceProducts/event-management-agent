package com.solace.maas.ep.event.management.agent.command.semp;

import com.solace.client.sempv2.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Request;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SempApiProviderImplTest {

    @Test
    void leavesTlsVersionsAndCipherSuitesToTheJvm() {
        ApiClient apiClient = apiClient("https://broker:943");

        List<ConnectionSpec> specs = apiClient.getHttpClient().getConnectionSpecs();
        assertEquals(1, specs.size());
        assertTrue(specs.get(0).isTls());
        assertNull(specs.get(0).tlsVersions());
        assertNull(specs.get(0).cipherSuites());
    }

    @Test
    void offersTls13OnTheSocket() throws Exception {
        RecordingSslSocketFactory sslSocketFactory = new RecordingSslSocketFactory();
        try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            Thread.ofVirtual().start(() -> {
                try {
                    server.accept().close();
                } catch (IOException ignored) {
                    // the test closes the server
                }
            });
            ApiClient apiClient = apiClient("https://localhost:" + server.getLocalPort());
            apiClient.getHttpClient().setSslSocketFactory(sslSocketFactory);

            Request request = new Request.Builder().url(apiClient.getBasePath() + "/about").build();
            assertThrows(IOException.class, () -> apiClient.getHttpClient().newCall(request).execute());
        }

        assertFalse(sslSocketFactory.sockets.isEmpty());
        for (SSLSocket socket : sslSocketFactory.sockets) {
            try (socket) {
                assertTrue(Arrays.asList(socket.getEnabledProtocols()).contains("TLSv1.3"),
                        "enabled protocols: " + Arrays.toString(socket.getEnabledProtocols()));
                assertTrue(Arrays.asList(socket.getEnabledCipherSuites()).contains("TLS_AES_256_GCM_SHA384"),
                        "enabled cipher suites: " + Arrays.toString(socket.getEnabledCipherSuites()));
            }
        }
    }

    private static ApiClient apiClient(String connectionUrl) {
        SempClient sempClient = SempClient.builder()
                .connectionUrl(connectionUrl)
                .username("user")
                .password("pass")
                .msgVpn("vpn")
                .build();
        return new SempApiProviderImpl(new SolaceHttpSemp(sempClient), null).getAclProfileApi().getApiClient();
    }

    /** Hands out the JVM's default SSL sockets and keeps them, so the test can read what OkHttp enabled. */
    private static final class RecordingSslSocketFactory extends SSLSocketFactory {
        private final SSLSocketFactory delegate;
        private final List<SSLSocket> sockets = new CopyOnWriteArrayList<>();

        private RecordingSslSocketFactory() throws NoSuchAlgorithmException {
            super();
            this.delegate = SSLContext.getDefault().getSocketFactory();
        }

        private Socket record(Socket socket) {
            sockets.add((SSLSocket) socket);
            return socket;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return record(delegate.createSocket(socket, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return record(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return record(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return record(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
                throws IOException {
            return record(delegate.createSocket(address, port, localAddress, localPort));
        }
    }
}
