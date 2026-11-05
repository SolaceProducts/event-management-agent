package com.solace.maas.ep.event.management.agent.plugin.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

/**
 * Builds the JVM trust store from a mounted PEM CA bundle at startup.
 * <p>
 * When EMA_CA_BUNDLE names a PEM file, every certificate in it goes into a fresh PKCS12 trust store
 * under EMA_TRUSTSTORE_DIR (default: java.io.tmpdir/ema-truststore, an emptyDir in Kubernetes), and the
 * javax.net.ssl.trustStore* system properties point at it. Every Java TLS client (SEMP, Kafka, schema
 * registry) and the EVMR link (VMRProperties) then trust exactly the bundle; OpenTofu gets the same PEM
 * file as SSL_CERT_FILE. A missing, unreadable or empty bundle stops EMA instead of falling back to the
 * built-in CA list. When EMA_CA_BUNDLE is not set, nothing changes.
 */
@Slf4j
public final class CaBundleTrustStore {

    public static final String BUNDLE_ENV = "EMA_CA_BUNDLE";
    public static final String DIR_ENV = "EMA_TRUSTSTORE_DIR";
    /** System property holding the bundle path once installed; read when launching OpenTofu. */
    public static final String BUNDLE_PROPERTY = "ema.caBundle";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CaBundleTrustStore() {
    }

    /** Installs the trust store if EMA_CA_BUNDLE is set; does nothing otherwise. */
    public static void installFromEnvironment() {
        String bundle = System.getenv(BUNDLE_ENV);
        if (StringUtils.isBlank(bundle)) {
            return;
        }
        String dir = System.getenv(DIR_ENV);
        install(Path.of(bundle), StringUtils.isBlank(dir)
                ? Path.of(System.getProperty("java.io.tmpdir"), "ema-truststore")
                : Path.of(dir));
    }

    static Path install(Path bundle, Path dir) {
        List<X509Certificate> certificates = readBundle(bundle);
        String password = randomPassword();
        Path trustStore = dir.resolve("truststore.p12");
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            for (int i = 0; i < certificates.size(); i++) {
                keyStore.setCertificateEntry("ca-" + i, certificates.get(i));
            }
            Files.createDirectories(dir);
            try (OutputStream out = Files.newOutputStream(trustStore)) {
                keyStore.store(out, password.toCharArray());
            }
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException("Could not write the trust store " + trustStore + " from " + BUNDLE_ENV
                    + "=" + bundle + ": " + e.getMessage(), e);
        }
        System.setProperty("javax.net.ssl.trustStore", trustStore.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", password);
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
        System.setProperty(BUNDLE_PROPERTY, bundle.toString());
        log.info("Built trust store {} from {}={} with {} certificate(s): {}", trustStore, BUNDLE_ENV, bundle,
                certificates.size(), certificates.stream().map(c -> c.getSubjectX500Principal().getName()).toList());
        return trustStore;
    }

    static List<X509Certificate> readBundle(Path bundle) {
        if (!Files.isRegularFile(bundle) || !Files.isReadable(bundle)) {
            throw new IllegalStateException(BUNDLE_ENV + "=" + bundle + " is not a readable file");
        }
        Collection<? extends Certificate> parsed;
        try (InputStream in = Files.newInputStream(bundle)) {
            parsed = CertificateFactory.getInstance("X.509").generateCertificates(in);
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
        return certificates;
    }

    private static String randomPassword() {
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
