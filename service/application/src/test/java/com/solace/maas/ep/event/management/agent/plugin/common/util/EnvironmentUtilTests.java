package com.solace.maas.ep.event.management.agent.plugin.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentUtilTests {

    private final EnvironmentUtil environmentUtil = new EnvironmentUtil();

    @AfterEach
    void tearDown() {
        System.clearProperty(CaBundleTrustStore.BUNDLE_PROPERTY);
    }

    @Test
    void caBundlePathIsNullWhenNoBundleInstalled() {
        System.clearProperty(CaBundleTrustStore.BUNDLE_PROPERTY);

        assertThat(environmentUtil.getCaBundlePath()).isNull();
    }

    @Test
    void caBundlePathIsTheInstalledBundle() {
        System.setProperty(CaBundleTrustStore.BUNDLE_PROPERTY, "/ca/bundle.pem");

        assertThat(environmentUtil.getCaBundlePath()).isEqualTo("/ca/bundle.pem");
    }
}
