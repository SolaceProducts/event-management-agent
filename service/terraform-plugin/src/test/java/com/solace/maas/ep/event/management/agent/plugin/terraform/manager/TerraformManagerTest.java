package com.solace.maas.ep.event.management.agent.plugin.terraform.manager;

import com.solace.maas.ep.event.management.agent.plugin.terraform.client.TerraformClientFactory;
import com.solace.maas.ep.event.management.agent.plugin.terraform.configuration.TerraformProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

        @Test
        void withNoDiagnostic() {
            // Arrange
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("@message", "Some error message");

            // Act & Assert
            assertFalse(terraformManager.isNoSuchHostError(logMap));
        }

        @Test
        void withNullDiagnostic() {
            // Arrange
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("diagnostic", null);

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
        void withInvalidDiagnosticStructure() {
            // Arrange
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("diagnostic", "invalid structure"); // Should be a Map

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
        @Test
        void withFailedToVerifyCertificate() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "Get \"https://mr-connection-z9vl7alolx7.messaging.mymaas.net:943/SEMP/v2/config/msgVpns/moodi-test-dont-touch\": tls: failed to verify certificate: x509: certificate is not valid for any names, but wanted to match mr-connection-z9vl7alolx7.messaging.mymaas.net"
            );

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withCertificateIsNotValid() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "SSL connection failed: certificate is not valid for hostname"
            );

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withX509Certificate() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "x509: certificate signed by unknown authority"
            );

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withTlsFailedToVerifyCertificate() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "tls: failed to verify certificate chain"
            );

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withCertificateVerificationFailed() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "certificate verification failed: unable to verify the first certificate"
            );

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withPKIXPathBuildingFailed() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target"
            );

            // Act & Assert
            assertTrue(terraformManager.isSslCertificateError(logMap));
        }

        @Test
        void withUnableToFindValidCertificationPath() {
            // Arrange
            Map<String, Object> logMap = createTerraformLogMap(
                    "error",
                    "unable to find valid certification path to requested target"
            );

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

        @Test
        void withNoDiagnostic() {
            // Arrange
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("@level", "error");
            logMap.put("@message", "Some error message");

            // Act & Assert
            assertFalse(terraformManager.isSslCertificateError(logMap));
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
