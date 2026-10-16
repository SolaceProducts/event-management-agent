package com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.solace.maas.ep.event.management.agent.plugin.exception.PluginClientException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolaceHttpSempLoggingTest {
    private static final String SEMP_ERROR_BODY = "{\"meta\":{\"error\":{\"code\":6,"
            + "\"description\":\"Could not find match for msgVpn 'secret-vpn'\",\"status\":\"NOT_FOUND\"},"
            + "\"request\":{\"method\":\"GET\",\"uri\":\"https://host:943/SEMP/v2/config/msgVpns/secret-vpn/queues\"},"
            + "\"responseCode\":400}}";

    private Logger sempLogger;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        sempLogger = (Logger) LoggerFactory.getLogger(SolaceHttpSemp.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        sempLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        sempLogger.detachAppender(listAppender);
    }

    @Test
    void badRequestLogsSempErrorStatusAndCodeAndMentionsMessageVpn() {
        SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp(HttpStatus.BAD_REQUEST, SEMP_ERROR_BODY);

        assertThatThrownBy(solaceHttpSemp::getQueues).isInstanceOf(PluginClientException.class);

        assertThat(errorMessages()).singleElement().satisfies(message -> {
            assertThat(message).contains("SEMP error: NOT_FOUND (code 6)");
            assertThat(message).contains("message VPN");
            assertThat(message).doesNotContain("secret-vpn");
        });
    }

    @Test
    void otherNon401ErrorLogsSempErrorStatusAndCode() {
        SolaceHttpSemp solaceHttpSemp = createSolaceHttpSemp(HttpStatus.BAD_GATEWAY, SEMP_ERROR_BODY);

        assertThatThrownBy(solaceHttpSemp::getQueues).isInstanceOf(PluginClientException.class);

        assertThat(errorMessages()).singleElement().satisfies(message -> {
            assertThat(message).contains("SEMP error: NOT_FOUND (code 6)");
            assertThat(message).doesNotContain("secret-vpn");
        });
    }

    private List<String> errorMessages() {
        return listAppender.list.stream()
                .filter(event -> event.getLevel().isGreaterOrEqual(Level.ERROR))
                .map(ILoggingEvent::getFormattedMessage)
                .toList();
    }

    private SolaceHttpSemp createSolaceHttpSemp(HttpStatus status, String body) {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(request -> Mono.just(ClientResponse.create(status)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .body(body)
                        .build()))
                .build();
        return new SolaceHttpSemp(SempClient.builder()
                .webClient(webClient)
                .username("user")
                .password("pass")
                .connectionUrl("https://host:943")
                .msgVpn("vpn")
                .build());
    }
}
