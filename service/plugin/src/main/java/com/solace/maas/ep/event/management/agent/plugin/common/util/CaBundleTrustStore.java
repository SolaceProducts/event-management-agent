package com.solace.maas.ep.event.management.agent.plugin.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

/**
 * Builds the JVM trust store from a mounted PEM CA bundle at startup.
 * <p>
 * When EMA_CA_BUNDLE names a PEM file, every certificate in it goes into a fresh PKCS12 trust store in a new
 * owner-only directory under EMA_TRUSTSTORE_DIR (default: java.io.tmpdir), and the javax.net.ssl.trustStore*
 * system properties point at it. Every Java TLS client (SEMP, Kafka, schema registry) and the EVMR link
 * (VMRProperties) then trust exactly the bundle; OpenTofu gets the same PEM file as SSL_CERT_FILE, and an empty
 * directory as SSL_CERT_DIR so Go does not also load the system certificate directories. A missing,
 * unreadable, empty or incomplete bundle stops EMA instead of falling back to the built-in CA list. When
 * EMA_CA_BUNDLE is not set, nothing changes.
 */
@Slf4j
public final class CaBundleTrustStore {

    public static final String BUNDLE_ENV = "EMA_CA_BUNDLE";
    public static final String DIR_ENV = "EMA_TRUSTSTORE_DIR";
    private static final String PEM_CERTIFICATE_HEADER = "-----BEGIN CERTIFICATE-----";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final AtomicReference<Installed> INSTALLED = new AtomicReference<>();

    private record Installed(Path bundle, Path emptyCertDir) {
    }

    private CaBundleTrustStore() {
    }

    /** Installs the trust store if EMA_CA_BUNDLE is set; does nothing otherwise. */
    public static void installFromEnvironment() {
        installFromEnvironment(System::getenv);
    }

    static Path installFromEnvironment(UnaryOperator<String> env) {
        String bundle = env.apply(BUNDLE_ENV);
        if (StringUtils.isBlank(bundle)) {
            return null;
        }
        String dir = env.apply(DIR_ENV);
        return install(Path.of(bundle), Path.of(StringUtils.isBlank(dir) ? System.getProperty("java.io.tmpdir") : dir));
    }

    /**
     * @return the path of the CA bundle the JVM trust store was built from, or null if none was installed
     */
    public static String installedBundlePath() {
        Installed installed = INSTALLED.get();
        return installed == null ? null : installed.bundle().toString();
    }

    /**
     * @return an empty directory to pass to OpenTofu as SSL_CERT_DIR, or null if no bundle was installed
     */
    public static String emptyCertDirPath() {
        Installed installed = INSTALLED.get();
        return installed == null ? null : installed.emptyCertDir().toString();
    }

    static void clearInstalledBundle() {
        INSTALLED.set(null);
    }

    static Path install(Path bundle, Path baseDir) {
        List<X509Certificate> certificates = readBundle(bundle);
        String password = randomPassword();
        Path trustStore;
        Path emptyCertDir;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            for (int i = 0; i < certificates.size(); i++) {
                keyStore.setCertificateEntry("ca-" + i, certificates.get(i));
            }
            Files.createDirectories(baseDir);
            Path dir = Files.createTempDirectory(baseDir, "ema-truststore-", ownerOnly());
            trustStore = dir.resolve("truststore.p12");
            emptyCertDir = Files.createDirectory(dir.resolve("empty-cert-dir"));
            dir.toFile().deleteOnExit();
            emptyCertDir.toFile().deleteOnExit();
            trustStore.toFile().deleteOnExit();
            try (OutputStream out = Files.newOutputStream(trustStore)) {
                keyStore.store(out, password.toCharArray());
            }
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException("Could not write the trust store under " + baseDir + " from " + BUNDLE_ENV
                    + "=" + bundle + ": " + e.getMessage(), e);
        }
        System.setProperty("javax.net.ssl.trustStore", trustStore.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", password);
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
        INSTALLED.set(new Installed(bundle, emptyCertDir));
        log.info("Built trust store {} from {}={} with {} certificate(s): {}", trustStore, BUNDLE_ENV, bundle,
                certificates.size(), certificates.stream().map(c -> c.getSubjectX500Principal().getName()).toList());
        return trustStore;
    }

    static List<X509Certificate> readBundle(Path bundle) {
        if (!Files.isRegularFile(bundle) || !Files.isReadable(bundle)) {
            throw new IllegalStateException(BUNDLE_ENV + "=" + bundle + " is not a readable file");
        }
        byte[] content;
        Collection<? extends Certificate> parsed;
        try {
            content = Files.readAllBytes(bundle);
            parsed = CertificateFactory.getInstance("X.509").generateCertificates(new ByteArrayInputStream(content));
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException(BUNDLE_ENV + "=" + bundle + " is not a PEM certificate bundle: " + e.getMessage(), e);
        }
        List<X509Certificate> certificates = new ArrayList<>();
        for (Certificate certificate : parsed) {
            X509Certificate x509 = (X509Certificate) certificate;
            if (x509.getBasicConstraints() < 0) {
                log.warn("{} entry {} is not a CA certificate", BUNDLE_ENV, x509.getSubjectX500Principal().getName());
            }
            certificates.add(x509);
        }
        if (certificates.isEmpty()) {
            throw new IllegalStateException(BUNDLE_ENV + "=" + bundle + " holds no certificates");
        }
        int headers = StringUtils.countMatches(new String(content, StandardCharsets.US_ASCII), PEM_CERTIFICATE_HEADER);
        if (headers != certificates.size()) {
            throw new IllegalStateException(BUNDLE_ENV + "=" + bundle + " lists " + headers + " certificate(s) but only "
                    + certificates.size() + " could be read; check for an incomplete entry");
        }
        return certificates;
    }

    private static FileAttribute<?>[] ownerOnly() {
        if (!FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
            return new FileAttribute<?>[0];
        }
        return new FileAttribute<?>[] {PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"))};
    }

    private static String randomPassword() {
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
