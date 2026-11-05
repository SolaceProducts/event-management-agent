package com.solace.maas.ep.event.management.agent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class EMAApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testGetDefaults() {
        EMAApplication.getDefault();

        assertThatNoException();
    }

    @Test
    void startupContinuesWhenTheCaBundleInstalls() {
        assertThat(EMAApplication.installCaBundleTrustStore(() -> { })).isTrue();
    }

    @Test
    void startupStopsWhenTheCaBundleCannotBeInstalled() {
        assertThat(EMAApplication.installCaBundleTrustStore(() -> {
            throw new IllegalStateException("EMA_CA_BUNDLE=/absent.pem is not a readable file");
        })).isFalse();
    }
}
