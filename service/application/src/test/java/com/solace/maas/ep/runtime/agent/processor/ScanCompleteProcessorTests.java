package com.solace.maas.ep.runtime.agent.processor;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.service.logging.LoggingService;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class ScanCompleteProcessorTests {

    @Autowired
    CamelContext camelContext;

    @InjectMocks
    ScanCompleteProcessorImp scanCompleteProcessor;

    @Mock
    LoggingService loggingService;

    @SneakyThrows
    @Test
    public void testScanCompleteProcessor() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "scanType");
        exchange.getIn().setBody("test exchange");

        scanCompleteProcessor.process(exchange);

        assertThatNoException();
    }
}