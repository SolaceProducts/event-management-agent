package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolaceHttpSempTest {

    @ParameterizedTest
    @MethodSource("uriBuilderFunctionTestCases")
    void testCreateUriBuilderFunction(String connectionUrl, String expectedUri) {
        // Arrange
        String uriPath = "/SEMP/v2/config/msgVpns/{msgvpn}/queues";
        String selectFields = "queueName";
        Map<String, String> substitutionMap = Map.of("msgvpn", "testVpn");
        SolaceHttpSemp solaceHttpSemp = new SolaceHttpSemp(
                SempClient.builder()
                        .username("myUsername")
                        .password("myPassword")
                        .connectionUrl(connectionUrl)
                        .msgVpn("testVpn")
                        .build());

        // Act
        Function<UriBuilder, URI> uriBuilderFunction = solaceHttpSemp.createUriBuilderFunction(uriPath, substitutionMap, selectFields);
        URI resultUri = uriBuilderFunction.apply(UriComponentsBuilder.newInstance());

        // Assert
        assertEquals(expectedUri, resultUri.toString());
    }

    static Stream<Arguments> uriBuilderFunctionTestCases() {
        return Stream.of(
                Arguments.of(
                        "https://ews-emea.api.foo.com:943",
                        "https://ews-emea.api.foo.com:943/SEMP/v2/config/msgVpns/testVpn/queues?select=queueName&count=100"
                ),
                Arguments.of(
                        "https://ews-emea.api.foo.com:943/it/technology/foobar/enterprise-foobar-bus/siOfoobar449-AsyncAPl/dq",
                        "https://ews-emea.api.foo.com:943/it/technology/foobar/enterprise-foobar-bus/siOfoobar449-AsyncAPl/dq/SEMP/v2/config/msgVpns/testVpn" +
                                "/queues?select=queueName&count=100"
                )
        );
    }
}