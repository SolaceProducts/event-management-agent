package com.solace.maas.ep.runtime.agent.plugin.route.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.runtime.agent.TestConfig;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class FileDataMergeAggregationStrategyTests {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final FileDataMergeAggregationStrategyImpl fileDataMergeAggregationStrategy =
            new FileDataMergeAggregationStrategyImpl(objectMapper);

    @Test
    public void testAggregateOldExchangeIsNull() throws JsonProcessingException {
        Map<String, Object> testDataMap = new HashMap<>();
        testDataMap.put("test", "data");

        String testData = objectMapper.writeValueAsString(testDataMap);

        Exchange newExchange = mock(Exchange.class);
        Message message = mock(Message.class);

        when(newExchange.getIn())
                .thenReturn(message);
        when(message.getBody(String.class))
                .thenReturn(testData);

        fileDataMergeAggregationStrategy.aggregate(null, newExchange);

        assertThatNoException();
    }

    @Test
    public void testAggregateOldExchangeNotNull() throws JsonProcessingException {
        Map<String, List<Map<String, Object>>> oldBody = new HashMap<>();

        Map<String, Object> testDataMap = new HashMap<>();
        testDataMap.put("test", "data");

        String testData = objectMapper.writeValueAsString(testDataMap);

        Exchange oldExchange = mock(Exchange.class);
        Exchange newExchange = mock(Exchange.class);
        Message oldMessage = mock(Message.class);
        Message newMessage = mock(Message.class);

        when(oldExchange.getIn())
                .thenReturn(oldMessage);
        when(oldMessage.getBody(Map.class))
                .thenReturn(oldBody);
        when(newExchange.getIn())
                .thenReturn(newMessage);
        when(newMessage.getBody(String.class))
                .thenReturn(testData);

        fileDataMergeAggregationStrategy.aggregate(oldExchange, newExchange);

        assertThatNoException();
    }
}
