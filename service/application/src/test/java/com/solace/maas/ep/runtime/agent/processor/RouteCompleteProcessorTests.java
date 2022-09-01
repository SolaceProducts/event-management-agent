package com.solace.maas.ep.runtime.agent.processor;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.service.lifecycle.ScanLifecycleService;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class RouteCompleteProcessorTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    CamelContext camelContext;

    @InjectMocks
    RouteCompleteProcessorImpl routeCompleteProcessor;

    @Mock
    ScanLifecycleService scanLifecycleService;

    @SneakyThrows
    @Test
    public void testRouteCompleteProcessor() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setHeader(RouteConstants.SCAN_ID, "scanId");
        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, "scanType");
        exchange.getIn().setBody("test exchange");

        when(scanLifecycleService.scanRouteCompleted(any(String.class)))
                .thenReturn(true);

        routeCompleteProcessor.process(exchange);

        assertThatNoException();

        exchange.setProperty(Exchange.EXCEPTION_CAUGHT, new Exception());
        routeCompleteProcessor.process(exchange);

        exception.expect(Exception.class);
    }
}