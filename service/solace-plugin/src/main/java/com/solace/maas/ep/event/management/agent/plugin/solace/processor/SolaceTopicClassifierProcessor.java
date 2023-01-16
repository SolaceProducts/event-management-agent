package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.event.SolaceTopicEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SolaceTopicClassifierProcessor extends ResultProcessorImpl<List<String>, List<SolaceTopicEvent>> {
    @Override
    public List<String> handleEvent(Map<String, Object> properties, List<SolaceTopicEvent> body) {
        log.info("Classifying topic {}", body);
        return body.stream()
                .map(topicEvent -> topicEvent.getTopic())
                .collect(Collectors.toList());
    }
}
