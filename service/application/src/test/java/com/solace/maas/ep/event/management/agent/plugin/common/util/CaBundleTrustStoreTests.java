package com.solace.maas.ep.event.management.agent.plugin.common.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CaBundleTrustStoreTests {

    @TempDir
    Path tmp;

    @AfterEach
    void clearProperties() {
        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStorePassword");
        System.clearProperty("javax.net.ssl.trustStoreType");
        System.clearProperty(CaBundleTrustStore.BUNDLE_PROPERTY);
    }

    @Test
    @SneakyThrows
    void buildsPkcs12FromEveryCertificateInTheBundle() {
        Path bundle = Path.of(getClass().getResource("/cabundle/two-cas.pem").toURI());

        Path trustStore = CaBundleTrustStore.install(bundle, tmp.resolve("ts"));

        assertThat(System.getProperty("javax.net.ssl.trustStore")).isEqualTo(trustStore.toString());
        assertThat(System.getProperty("javax.net.ssl.trustStoreType")).isEqualTo("PKCS12");
        assertThat(System.getProperty(CaBundleTrustStore.BUNDLE_PROPERTY)).isEqualTo(bundle.toString());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream in = Files.newInputStream(trustStore)) {
            keyStore.load(in, System.getProperty("javax.net.ssl.trustStorePassword").toCharArray());
        }
        assertThat(Collections.list(keyStore.aliases())).hasSize(2)
                .allSatisfy(alias -> assertThat(keyStore.isCertificateEntry(alias)).isTrue());
    }

    @Test
    void missingBundleFailsAndLeavesJvmUntouched() {
        assertThatThrownBy(() -> CaBundleTrustStore.install(tmp.resolve("absent.pem"), tmp.resolve("ts")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("is not a readable file");
        assertThat(System.getProperty("javax.net.ssl.trustStore")).isNull();
    }

    @Test
    @SneakyThrows
    void bundleWithoutCertificatesFails() {
        Path empty = Files.writeString(tmp.resolve("empty.pem"), "");

        assertThatThrownBy(() -> CaBundleTrustStore.install(empty, tmp.resolve("ts")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("holds no certificates");
        assertThat(System.getProperty("javax.net.ssl.trustStore")).isNull();
    }

    @Test
    @SneakyThrows
    void bundleThatIsNotPemFails() {
        Path broken = Files.writeString(tmp.resolve("broken.pem"),
                "-----BEGIN CERTIFICATE-----\nnot base64 at all\n-----END CERTIFICATE-----\n");

        assertThatThrownBy(() -> CaBundleTrustStore.install(broken, tmp.resolve("ts")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("is not a PEM certificate bundle");
        assertThat(System.getProperty("javax.net.ssl.trustStore")).isNull();
        assertThat(System.getProperty(CaBundleTrustStore.BUNDLE_PROPERTY)).isNull();
    }
}
