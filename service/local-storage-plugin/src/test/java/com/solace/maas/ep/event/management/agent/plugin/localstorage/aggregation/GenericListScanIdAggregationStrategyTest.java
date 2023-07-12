package com.solace.maas.ep.event.management.agent.plugin.localstorage.aggregation;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.route.aggregation.GenericListScanIdAggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class GenericListScanIdAggregationStrategyTest {

    @Autowired
    CamelContext camelContext;

    @Test
    public void testGenericListScanIdAggregationStrategy() {
        GenericListScanIdAggregationStrategy genericListScanIdAggregationStrategy =
                new GenericListScanIdAggregationStrategy();

        Exchange oldExchange = new DefaultExchange(camelContext);
        oldExchange.getIn().setHeader(RouteConstants.SCAN_ID, "oldId");
        oldExchange.getIn().setBody("old exchange");

        Exchange newExchange = new DefaultExchange(camelContext);
        newExchange.getIn().setHeader(RouteConstants.SCAN_ID, "newId");
        newExchange.getIn().setBody("new exchange");

        genericListScanIdAggregationStrategy.handleExchange(oldExchange);
        genericListScanIdAggregationStrategy.handleExchange(oldExchange, newExchange);

        assertThatNoException();
    }
}

