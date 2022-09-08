package com.solace.maas.ep.runtime.agent.plugin.localstorage.route.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.runtime.agent.plugin.constants.AggregationConstants;
import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ExcludeFromJacocoGeneratedReport
@Slf4j
public class FileDataMergeAggregationStrategyImpl implements AggregationStrategy {
    private final ObjectMapper objectMapper;

    public FileDataMergeAggregationStrategyImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        String objKey = newExchange.getIn().getHeader(AggregationConstants.OBJECT_KEY, String.class);

        if (Objects.isNull(oldExchange)) {
            Map<String, List<Map<String, Object>>> newBody = new HashMap<>();
            String body = newExchange.getIn().getBody(String.class);

            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> bodyElement = objectMapper.readValue(body, Map.class);
                newBody.put(objKey, List.of(bodyElement));

                newExchange.getIn().setBody(newBody);
            } catch (JsonProcessingException e) {
                log.error("Failed to merge File Data >>> ", e);
            }
        } else {
            @SuppressWarnings("unchecked")
            Map<String, List<Map<String, Object>>> oldBody = oldExchange.getIn().getBody(Map.class);
            String body = newExchange.getIn().getBody(String.class);

            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> bodyElement = objectMapper.readValue(body, Map.class);

                List<Map<String, Object>> element = oldBody.getOrDefault(objKey, new ArrayList<>());
                List<Map<String, Object>> newList = new ArrayList<>(element);
                newList.add(bodyElement);

                oldBody.put(objKey, newList);

                newExchange.getIn().setBody(oldBody);
            } catch (JsonProcessingException e) {
                log.error("Failed to merge File Data >>> ", e);
            }
        }

        return newExchange;
    }
}
