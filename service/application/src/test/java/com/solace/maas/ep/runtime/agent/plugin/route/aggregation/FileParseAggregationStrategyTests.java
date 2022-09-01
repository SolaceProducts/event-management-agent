package com.solace.maas.ep.runtime.agent.plugin.route.aggregation;

import com.solace.maas.ep.runtime.agent.TestConfig;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class FileParseAggregationStrategyTests {
    private final FileParseAggregationStrategyImpl fileParseAggregationStrategy = new FileParseAggregationStrategyImpl();

    @Test
    public void testAggregate() {
        Exchange oldExchange = mock(Exchange.class);
        Exchange newExchange = mock(Exchange.class);

        Message message = mock(Message.class);

        when(oldExchange.getIn())
                .thenReturn(message);
        when(newExchange.getIn())
                .thenReturn(message);
        when(message.getHeader(any(String.class)))
                .thenReturn("TEST_HEADER_VALUE");

        fileParseAggregationStrategy.aggregate(oldExchange, newExchange);

        assertThatNoException();
    }
}