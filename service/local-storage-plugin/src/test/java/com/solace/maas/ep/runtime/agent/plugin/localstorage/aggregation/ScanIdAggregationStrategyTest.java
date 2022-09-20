package com.solace.maas.ep.runtime.agent.plugin.localstorage.aggregation;

import com.solace.maas.ep.runtime.agent.plugin.route.aggregation.base.ScanIdAggregationStrategyImpl;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCAN_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScanIdAggregationStrategyTest {

    @Autowired
    private CamelContext camelContext;

    @Test
    public void testScanIdAggregationStrategy() {
        ScanIdAggregationStrategyImpl scanIdAggregationStrategyImpl =
                Mockito.mock(ScanIdAggregationStrategyImpl.class, Mockito.CALLS_REAL_METHODS);

        Exchange oldExchange = new DefaultExchange(camelContext);
        oldExchange.getIn().setHeader(SCAN_ID, "oldId");
        oldExchange.getIn().setBody("old exchange");

        Exchange newExchange = new DefaultExchange(camelContext);
        newExchange.getIn().setHeader(SCAN_ID, "newId");
        newExchange.getIn().setBody("new exchange");

        scanIdAggregationStrategyImpl.aggregate(oldExchange, newExchange);

        oldExchange.getIn().setHeader(SCAN_ID, "sameId");
        newExchange.getIn().setHeader(SCAN_ID, "sameId");
        scanIdAggregationStrategyImpl.aggregate(oldExchange, newExchange);

        scanIdAggregationStrategyImpl.aggregate(null, newExchange);

        assertThatNoException();
    }
}
