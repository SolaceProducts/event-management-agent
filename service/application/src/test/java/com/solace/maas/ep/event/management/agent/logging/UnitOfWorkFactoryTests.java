package com.solace.maas.ep.event.management.agent.logging;

import com.solace.maas.ep.event.management.agent.processor.RouteCompleteProcessorImpl;
import com.solace.maas.ep.event.management.agent.TestConfig;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.support.DefaultExchange;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.apache.camel.spi.UnitOfWork.MDC_EXCHANGE_ID;
import static org.apache.camel.spi.UnitOfWork.MDC_MESSAGE_ID;
import static org.apache.camel.spi.UnitOfWork.MDC_BREADCRUMB_ID;
import static org.apache.camel.spi.UnitOfWork.MDC_CORRELATION_ID;
import static org.apache.camel.spi.UnitOfWork.MDC_CAMEL_CONTEXT_ID;
import static org.apache.camel.spi.UnitOfWork.MDC_ROUTE_ID;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class UnitOfWorkFactoryTests {

    @Autowired
    CamelContext camelContext;

    @InjectMocks
    RouteCompleteProcessorImpl routeCompleteProcessor;

    @SneakyThrows
    @Test
    public void testCustomUnitOfWorkFactory() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody("test exchange");

        CustomUnitOfWorkFactory factory = new CustomUnitOfWorkFactory();
        UnitOfWork unitOfWork = factory.createUnitOfWork(exchange);

        MDC.put(MDC_EXCHANGE_ID, "exchange");
        MDC.put(MDC_MESSAGE_ID, "message");
        MDC.put(MDC_BREADCRUMB_ID, "breadcrumb");
        MDC.put(MDC_CORRELATION_ID, "correlation");
        MDC.put(MDC_CAMEL_CONTEXT_ID, "context");
        MDC.put(MDC_ROUTE_ID, "route");

        unitOfWork.beforeProcess(routeCompleteProcessor, exchange, doneSync -> {
        });

        assertThatNoException();
    }
}
