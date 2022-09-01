package com.solace.maas.ep.runtime.agent.processor;

import com.solace.maas.ep.runtime.agent.TestConfig;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class LoggingProcessorTests {
    @Autowired
    CamelContext camelContext;

    @InjectMocks
    LoggingProcessor loggingProcessor;

    @SneakyThrows
    @Test
    public void testInMemoryBufferProcessor() throws Exception {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody("test exchange");

        loggingProcessor.process(exchange);

        assertThatNoException();
    }
}
