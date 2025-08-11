package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClientFactory;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TerraformManagerTest {

    @MockitoBean
    private TerraformLogProcessingService terraformLogProcessingService;

    @MockitoBean
    private TerraformProperties terraformProperties;

    @MockitoBean
    private TerraformClientFactory terraformClientFactory;

    private TerraformManager terraformManager;

    @BeforeEach
    void setUp() {
        terraformManager = new TerraformManager(
                terraformLogProcessingService,
                terraformProperties,
                terraformClientFactory
        );
    }

    @Nested
    class IsNoSuchHostErrorTests {
        @Test
        void withNoSuchHostInDifferentFormat() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "Error: SEMP call failed - no such host found for hostname"
            );

            // Act & Assert
            assertTrue(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withNoSuchHostInDetail() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "dial tcp: lookup msg-solace-test.lcag-cmlz-n.lhgroup.de on 172.25.0.10:53: no such host"
            );

            // Act & Assert
            assertTrue(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withoutNoSuchHost() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "Connection refused by server"
            );

            // Act & Assert
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        @ParameterizedTest
        @MethodSource("invalidDiagnosticScenarios")
        void shouldReturnFalseForInvalidDiagnosticScenarios(Map<String, Object> logMap) {
            // Act & Assert
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withEmptyDetail() {
            // Arrange
            Map<String, Object> diagnostic = new HashMap<>();
            diagnostic.put("detail", "");

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("diagnostic", diagnostic);

            // Act & Assert
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withNullDetail() {
            // Arrange
            Map<String, Object> diagnostic = new HashMap<>();
            diagnostic.put("detail", null);

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("diagnostic", diagnostic);

            // Act & Assert
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withNonStringDetail() {
            // Arrange
            Map<String, Object> diagnostic = new HashMap<>();
            diagnostic.put("detail", 12345); // Non-string detail

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("diagnostic", diagnostic);

            // Act & Assert
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withExceptionDuringParsing() {
            // Arrange - Create a map that will cause an exception during parsing
            Map<String, Object> logMap = new HashMap<>() {
                @Override
                public Object get(Object key) {
                    if ("diagnostic".equals(key)) {
                        throw new RuntimeException("Simulated parsing error");
                    }
                    return super.get(key);
                }
            };
            logMap.put("@level", "error");

            // Act & Assert - Should return false when exception occurs
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        static Stream<Arguments> invalidDiagnosticScenarios() {
            // No diagnostic
            Map<String, Object> noDiagnostic = new HashMap<>();
            noDiagnostic.put("@level", "error");
            noDiagnostic.put("@message", "Some error message");

            // Null diagnostic
            Map<String, Object> nullDiagnostic = new HashMap<>();
            nullDiagnostic.put("@level", "error");
            nullDiagnostic.put("diagnostic", null);

            // Invalid diagnostic structure
            Map<String, Object> invalidDiagnostic = new HashMap<>();
            invalidDiagnostic.put("@level", "error");
            invalidDiagnostic.put("diagnostic", "invalid structure"); // Should be a Map

            return Stream.of(
                    Arguments.of(noDiagnostic),
                    Arguments.of(nullDiagnostic),
                    Arguments.of(invalidDiagnostic)
            );
        }

        private Map<String, Object> createTerraformLogMap(String level, String detail) {
            Map<String, Object> diagnostic = new HashMap<>();
            diagnostic.put("severity", "error");
            diagnostic.put("summary", "SEMP call failed");
            diagnostic.put("detail", detail);

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", level);
            logMap.put("@message", "Error: SEMP call failed");
            logMap.put("diagnostic", diagnostic);

            return logMap;
        }
    }

    @Nested
    class IsSslCertificateErrorTests {
        @ParameterizedTest
        @ValueSource(strings = {
                "Get \"https://mr-connection-z9vl7alolx7.messaging.mymaas.net:943/SEMP/v2/config/msgVpns/moodi-test-dont-touch\": tls: failed to verify certificate: x509: certificate is not valid for any names, but wanted to match mr-connection-z9vl7alolx7.messaging.mymaas.net",
                "SSL connection failed: certificate is not valid for hostname",
                "x509: certificate signed by unknown authority",
                "tls: failed to verify certificate chain",
                "certificate verification failed: unable to verify the first certificate",
                "PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target",
                "unable to find valid certification path to requested target"
        })
        void shouldDetectSslCertificateErrors(String errorDetail) {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap("error", errorDetail);

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withNonSslError() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "Connection refused by server"
            );

            // Act & Assert
            assertFalse(terraformManager.isSslCertificateError(logMap));
        }

        @ParameterizedTest
        @MethodSource("invalidDiagnosticScenarios")
        void shouldReturnFalseForInvalidDiagnosticScenarios(Map<String, Object> logMap) {
            // Act & Assert
            assertFalse(terraformManager.isSslCertificateError(logMap));
        }

        static Stream<Arguments> invalidDiagnosticScenarios() {
            // No diagnostic
            Map<String, Object> noDiagnostic = new HashMap<>();
            noDiagnostic.put("@level", "error");
            noDiagnostic.put("@message", "Some error message");

            // Null diagnostic
            Map<String, Object> nullDiagnostic = new HashMap<>();
            nullDiagnostic.put("@level", "error");
            nullDiagnostic.put("diagnostic", null);

            // Invalid diagnostic structure
            Map<String, Object> invalidDiagnostic = new HashMap<>();
            invalidDiagnostic.put("@level", "error");
            invalidDiagnostic.put("diagnostic", "invalid structure"); // Should be a Map

            return Stream.of(
                    Arguments.of(noDiagnostic),
                    Arguments.of(nullDiagnostic),
                    Arguments.of(invalidDiagnostic)
            );
        }

        @Test
        void withNullDetail() {
            // Arrange
            Map<String, Object> diagnostic = new HashMap<>();
            diagnostic.put("detail", null);

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("diagnostic", diagnostic);

            // Act & Assert
            assertFalse(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withExceptionDuringParsing() {
            // Arrange - Create a map that will cause an exception during parsing
            Map<String, Object> logMap = new HashMap<>() {
                @Override
                public Object get(Object key) {
                    if ("diagnostic".equals(key)) {
                        throw new RuntimeException("Simulated parsing error");
                    }
                    return super.get(key);
                }
            };
            logMap.put("@level", "error");

            // Act & Assert - Should return false when exception occurs
            assertFalse(terraformManager.isSslCertificateError(logMap));
        }

        private Map<String, Object> createTerraformLogMap(String level, String detail) {
            Map<String, Object> diagnostic = new HashMap<>();
            diagnostic.put("severity", "error");
            diagnostic.put("summary", "SEMP call failed");
            diagnostic.put("detail", detail);

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", level);
            logMap.put("@message", "Error: SEMP call failed");
            logMap.put("diagnostic", diagnostic);

            return logMap;
        }
    }
}
