package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLHandshakeException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.security.cert.CertPathBuilderException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class SolaceHttpSempTest {

    @ParameterizedTest
    @MethodSource("uriBuilderFunctionTestCases")
    void testCreateUriBuilderFunction(String connectionUrl, String expectedUri) {
        // Arrange
        String uriPath = "/SEMP/v2/config/msgVpns/{msgvpn}/queues";
        String selectFields = "queueName";
        Map<String, String> substitutionMap = Map.of("msgvpn", "testVpn");
        SolaceHttpSemp solaceHttpSemp = new SolaceHttpSemp(
                SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl(connectionUrl)
                        .msgVpn("testVpn")
                        .build());

        // Act
        Function<UriBuilder, URI> uriBuilderFunction = solaceHttpSemp.createUriBuilderFunction(uriPath, substitutionMap, selectFields);
        URI resultUri = uriBuilderFunction.apply(UriComponentsBuilder.newInstance());

        // Assert
        assertEquals(expectedUri, resultUri.toString());
    }

    static Stream<Arguments> uriBuilderFunctionTestCases() {
        return Stream.of(
                Arguments.of(
                        "https://ews-emea.api.foo.com:943",
                        "https://ews-emea.api.foo.com:943/SEMP/v2/config/msgVpns/testVpn/queues?select=queueName&count=100"
                ),
                Arguments.of(
                        "https://ews-emea.api.foo.com:943/it/technology/foobar/enterprise-foobar-bus/siOfoobar449-AsyncAPl/dq",
                        "https://ews-emea.api.foo.com:943/it/technology/foobar/enterprise-foobar-bus/siOfoobar449-AsyncAPl/dq/SEMP/v2/config/msgVpns/testVpn" +
                                "/queues?select=queueName&count=100"
                )
        );
    }

    @Nested
    class IsSslCertificateError {

        @Test
        void withPKIXPathBuildingFailedMessage() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            WebClientRequestException exception = new WebClientRequestException(
                    new RuntimeException("PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target"),
                    null, null, new HttpHeaders());

            // Act & Assert
            assertTrue(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withUnableToFindValidCertificationPathMessage() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            WebClientRequestException exception = new WebClientRequestException(
                    new RuntimeException("unable to find valid certification path to requested target"),
                    null, null, new HttpHeaders());

            // Act & Assert
            assertTrue(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withSSLHandshakeExceptionInCause() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            SSLHandshakeException sslException = new SSLHandshakeException("SSL handshake failed");
            WebClientRequestException exception = new WebClientRequestException(sslException, null, null, new HttpHeaders());

            // Act & Assert
            assertTrue(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withCertPathBuilderExceptionInCause() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            CertPathBuilderException certException = new CertPathBuilderException("Certificate path building failed");
            WebClientRequestException exception = new WebClientRequestException(certException, null, null, new HttpHeaders());

            // Act & Assert
            assertTrue(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withNestedSSLException() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            SSLHandshakeException sslException = new SSLHandshakeException("PKIX path building failed");
            RuntimeException wrapperException = new RuntimeException("Wrapper", sslException);
            WebClientRequestException exception = new WebClientRequestException(wrapperException, null, null, new HttpHeaders());

            // Act & Assert
            assertTrue(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withNonSSLException() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            WebClientRequestException exception = new WebClientRequestException(
                    new RuntimeException("Connection refused"),
                    null, null, new HttpHeaders());

            // Act & Assert
            assertFalse(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withDNSResolutionError() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            WebClientRequestException exception = new WebClientRequestException(
                    new RuntimeException("Failed to resolve hostname"),
                    null, null, new HttpHeaders());

            // Act & Assert
            assertFalse(solaceHttpSemp.isSslCertificateError(exception));
        }

        @Test
        void withNullMessage() {
            // Arrange
            SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp();
            WebClientRequestException exception = new WebClientRequestException(
                    new RuntimeException((String) null),
                    null, null, new HttpHeaders());

            // Act & Assert
            assertFalse(solaceHttpSemp.isSslCertificateError(exception));
        }
    }

    private SolaceHttpSemp createSolaceHttpSemp() {
        return new SolaceHttpSemp(
                SempClient.builder()
                        .username("testUser")
                        .password("testPass")
                        .connectionUrl("https://test.example.com:943")
                        .msgVpn("testVpn")
                        .build());
    }
}
