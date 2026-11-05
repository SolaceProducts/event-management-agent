package com.solace.maas.ep.event.management.agent.plugin.common.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class CaBundleTrustStoreTests {

    private static final String END_LINE = "-----END CERTIFICATE-----";

    @TempDir
    Path tmp;

    private Properties systemPropertiesBackup;

    @BeforeEach
    void setUp() {
        systemPropertiesBackup = new Properties();
        systemPropertiesBackup.putAll(System.getProperties());
        System.clearProperty("javax.net.ssl.trustStore");
        CaBundleTrustStore.clearInstalledBundle();
    }

    @AfterEach
    void tearDown() {
        System.setProperties(systemPropertiesBackup);
        CaBundleTrustStore.clearInstalledBundle();
    }

    @Test
    @SneakyThrows
    void buildsPkcs12FromEveryCertificateInTheBundle() {
        Path bundle = twoCas();

        Path trustStore = CaBundleTrustStore.install(bundle, tmp);

        assertThat(trustStore.getParent().getParent()).isEqualTo(tmp);
        assertThat(trustStore.getParent().getFileName().toString()).startsWith("ema-truststore-");
        assertThat(System.getProperty("javax.net.ssl.trustStore")).isEqualTo(trustStore.toString());
        assertThat(System.getProperty("javax.net.ssl.trustStoreType")).isEqualTo("PKCS12");
        assertThat(CaBundleTrustStore.installedBundlePath()).isEqualTo(bundle.toString());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream in = Files.newInputStream(trustStore)) {
            keyStore.load(in, System.getProperty("javax.net.ssl.trustStorePassword").toCharArray());
        }
        assertThat(Collections.list(keyStore.aliases())).hasSize(2)
                .allSatisfy(alias -> assertThat(keyStore.isCertificateEntry(alias)).isTrue());
    }

    @Test
    @SneakyThrows
    void providesAnEmptyCertDirectoryForOpenTofu() {
        Path trustStore = CaBundleTrustStore.install(twoCas(), tmp);

        Path certDir = Path.of(CaBundleTrustStore.emptyCertDirPath());
        assertThat(certDir.getParent()).isEqualTo(trustStore.getParent());
        assertThat(certDir).isEmptyDirectory();
    }

    @Test
    @SneakyThrows
    void trustStoreDirectoryIsOwnerOnly() {
        assumeTrue(FileSystems.getDefault().supportedFileAttributeViews().contains("posix"));

        Path trustStore = CaBundleTrustStore.install(twoCas(), tmp);

        assertThat(PosixFilePermissions.toString(Files.getPosixFilePermissions(trustStore.getParent())))
                .isEqualTo("rwx------");
    }

    @Test
    @SneakyThrows
    void eachInstallGetsItsOwnDirectory() {
        Path first = CaBundleTrustStore.install(twoCas(), tmp);
        Path second = CaBundleTrustStore.install(twoCas(), tmp);

        assertThat(first.getParent()).isNotEqualTo(second.getParent());
        assertThat(first).exists();
        assertThat(second).exists();
    }

    @Test
    void missingBundleFailsAndLeavesJvmUntouched() {
        assertThatThrownBy(() -> CaBundleTrustStore.install(tmp.resolve("absent.pem"), tmp))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("is not a readable file");
        assertJvmUntouched();
    }

    @Test
    @SneakyThrows
    void bundleWithoutCertificatesFails() {
        Path empty = Files.writeString(tmp.resolve("empty.pem"), "");

        assertThatThrownBy(() -> CaBundleTrustStore.install(empty, tmp))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("holds no certificates");
        assertJvmUntouched();
    }

    @Test
    @SneakyThrows
    void bundleThatIsNotPemFails() {
        Path broken = Files.writeString(tmp.resolve("broken.pem"),
                "-----BEGIN CERTIFICATE-----\nnot base64 at all\n-----END CERTIFICATE-----\n");

        assertThatThrownBy(() -> CaBundleTrustStore.install(broken, tmp))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("is not a PEM certificate bundle");
        assertJvmUntouched();
    }

    @Test
    @SneakyThrows
    void bundleWithEntryMissingItsEndLineFails() {
        String pem = Files.readString(twoCas());
        int lastEnd = pem.lastIndexOf(END_LINE);
        Path truncated = Files.writeString(tmp.resolve("truncated.pem"), pem.substring(0, lastEnd));

        assertThatThrownBy(() -> CaBundleTrustStore.install(truncated, tmp))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("lists 2 certificate(s) but only 1 could be read");
        assertJvmUntouched();
    }

    @Test
    @SneakyThrows
    void bundleEndingInABeginLineFails() {
        String pem = Files.readString(twoCas());
        String firstOnly = pem.substring(0, pem.indexOf(END_LINE) + END_LINE.length()) + "\n";
        Path dangling = Files.writeString(tmp.resolve("dangling.pem"), firstOnly + "-----BEGIN CERTIFICATE-----\n");

        assertThatThrownBy(() -> CaBundleTrustStore.install(dangling, tmp))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("lists 2 certificate(s) but only 1 could be read");
        assertJvmUntouched();
    }

    @Test
    @SneakyThrows
    void trustStoreThatCannotBeWrittenFails() {
        Path notADirectory = Files.writeString(tmp.resolve("not-a-directory"), "");

        assertThatThrownBy(() -> CaBundleTrustStore.install(twoCas(), notADirectory))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Could not write the trust store under " + notADirectory);
        assertJvmUntouched();
    }

    @Test
    void blankBundleVariableChangesNothing() {
        assertThat(CaBundleTrustStore.installFromEnvironment(Map.of(CaBundleTrustStore.BUNDLE_ENV, " ")::get)).isNull();
        assertThat(CaBundleTrustStore.installFromEnvironment(name -> null)).isNull();
        assertJvmUntouched();
    }

    @Test
    @SneakyThrows
    void environmentChoosesTheTrustStoreDirectory() {
        Path dir = tmp.resolve("configured");

        Path trustStore = CaBundleTrustStore.installFromEnvironment(Map.of(
                CaBundleTrustStore.BUNDLE_ENV, twoCas().toString(),
                CaBundleTrustStore.DIR_ENV, dir.toString())::get);

        assertThat(trustStore.getParent().getParent()).isEqualTo(dir);
        assertThat(CaBundleTrustStore.installedBundlePath()).isEqualTo(twoCas().toString());
    }

    @Test
    @SneakyThrows
    void trustStoreDirectoryDefaultsToTmpdir() {
        System.setProperty("java.io.tmpdir", tmp.toString());

        Path trustStore = CaBundleTrustStore.installFromEnvironment(Map.of(
                CaBundleTrustStore.BUNDLE_ENV, twoCas().toString(),
                CaBundleTrustStore.DIR_ENV, "")::get);

        assertThat(trustStore.getParent().getParent()).isEqualTo(tmp);
    }

    @SneakyThrows
    private Path twoCas() {
        return Path.of(getClass().getResource("/cabundle/two-cas.pem").toURI());
    }

    private static void assertJvmUntouched() {
        assertThat(System.getProperty("javax.net.ssl.trustStore")).isNull();
        assertThat(CaBundleTrustStore.installedBundlePath()).isNull();
        assertThat(CaBundleTrustStore.emptyCertDirPath()).isNull();
    }
}
