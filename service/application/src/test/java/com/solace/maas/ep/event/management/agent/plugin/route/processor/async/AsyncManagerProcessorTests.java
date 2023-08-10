package com.solace.maas.ep.event.management.agent.plugin.route.processor.async;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.async.AsyncManagerProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.Mockito.mock;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class AsyncManagerProcessorTests {
    @Mock
    AsyncManager asyncManager = mock(AsyncManager.class);

    @InjectMocks
    AsyncManagerProcessor asyncManagerProcessor;

    @Test
    @SneakyThrows
    public void testProcess() {
        Map<String, String> body = Map.of(
                RouteConstants.SCAN_ID, "scan1",
                RouteConstants.SCAN_TYPE, "testScan"
        );

        Exchange exchange = ExchangeBuilder.anExchange(mock(CamelContext.class))
                .withBody(body)
                .build();

        asyncManagerProcessor.process(exchange);

        assertThatNoException();
    }
}
