package com.solace.maas.ep.event.management.agent.util;

import com.solace.maas.ep.event.management.agent.plugin.util.SempErrorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SempErrorUtilTest {

    @Test
    void returnsStatusAndCodeWithoutDescription() {
        String body = "{\"meta\":{\"error\":{\"code\":6,"
                + "\"description\":\"Could not find match for msgVpn 'secret-vpn'\",\"status\":\"NOT_FOUND\"},"
                + "\"request\":{\"method\":\"GET\",\"uri\":\"https://host:943/SEMP/v2/config/msgVpns/secret-vpn/queues\"},"
                + "\"responseCode\":400}}";

        String summary = SempErrorUtil.getErrorSummary(body);

        assertThat(summary).isEqualTo("NOT_FOUND (code 6)");
        assertThat(summary).doesNotContain("secret-vpn");
    }

    @Test
    void returnsStatusWhenCodeIsMissing() {
        assertThat(SempErrorUtil.getErrorSummary("{\"meta\":{\"error\":{\"status\":\"INVALID_PARAMETER\"}}}"))
                .isEqualTo("INVALID_PARAMETER");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"not json", "{\"meta\":{\"responseCode\":400}}", "{\"meta\":{\"error\":{\"code\":6}}}"})
    void returnsUnknownWhenStatusCannotBeExtracted(String body) {
        assertThat(SempErrorUtil.getErrorSummary(body)).isEqualTo("unknown");
    }
}
