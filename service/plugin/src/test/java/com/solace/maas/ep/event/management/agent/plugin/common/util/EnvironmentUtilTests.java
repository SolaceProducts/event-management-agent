package com.solace.maas.ep.event.management.agent.plugin.common.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentUtilTests {

    @TempDir
    Path tmp;

    private Properties systemPropertiesBackup;

    @BeforeEach
    void setUp() {
        systemPropertiesBackup = new Properties();
        systemPropertiesBackup.putAll(System.getProperties());
        CaBundleTrustStore.clearInstalledBundle();
    }

    @AfterEach
    void tearDown() {
        System.setProperties(systemPropertiesBackup);
        CaBundleTrustStore.clearInstalledBundle();
    }

    @Test
    void caBundlePathIsNullWhenNoBundleInstalled() {
        assertThat(new EnvironmentUtil().getCaBundlePath()).isNull();
        assertThat(new EnvironmentUtil().getCaBundleCertDirPath()).isNull();
    }

    @Test
    void caBundlePathIgnoresTheOldSystemProperty() {
        System.setProperty("ema.caBundle", "/ca/bundle.pem");

        assertThat(new EnvironmentUtil().getCaBundlePath()).isNull();
    }

    @Test
    @SneakyThrows
    void caBundlePathIsTheInstalledBundle() {
        Path bundle = Path.of(getClass().getResource("/cabundle/two-cas.pem").toURI());
        CaBundleTrustStore.install(bundle, tmp);

        assertThat(new EnvironmentUtil().getCaBundlePath()).isEqualTo(bundle.toString());
        assertThat(new EnvironmentUtil().getCaBundleCertDirPath()).isEqualTo(CaBundleTrustStore.emptyCertDirPath());
    }

    @Test
    void sslCertFileIsSetOnlyWhenNotBlank() {
        assertThat(new EnvironmentUtil(Map.of("SSL_CERT_FILE", "/etc/ssl/certs/ca.pem")::get).isSslCertFileSet()).isTrue();
        assertThat(new EnvironmentUtil(Map.of("SSL_CERT_FILE", "")::get).isSslCertFileSet()).isFalse();
        assertThat(new EnvironmentUtil(Map.of("SSL_CERT_FILE", "  ")::get).isSslCertFileSet()).isFalse();
        assertThat(new EnvironmentUtil(name -> null).isSslCertFileSet()).isFalse();
    }

    @Test
    void sslCertDirIsSetOnlyWhenNotBlank() {
        assertThat(new EnvironmentUtil(Map.of("SSL_CERT_DIR", "/etc/ssl/certs")::get).isSslCertDirSet()).isTrue();
        assertThat(new EnvironmentUtil(Map.of("SSL_CERT_DIR", " ")::get).isSslCertDirSet()).isFalse();
        assertThat(new EnvironmentUtil(name -> null).isSslCertDirSet()).isFalse();
    }

    @Test
    void customCaCertsPresentOnlyWhenSetToOne() {
        assertThat(new EnvironmentUtil(Map.of("CUSTOM_CA_CERTS_PRESENT", "1")::get).isCustomCACertPresent()).isTrue();
        assertThat(new EnvironmentUtil(Map.of("CUSTOM_CA_CERTS_PRESENT", "0")::get).isCustomCACertPresent()).isFalse();
        assertThat(new EnvironmentUtil(name -> null).isCustomCACertPresent()).isFalse();
    }
}
