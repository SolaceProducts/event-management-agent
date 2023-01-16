package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.event.SolaceTopicEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SolaceTopicProcessor extends ResultProcessorImpl<List<SolaceTopicEvent>, String> {
    @Override
    public List<SolaceTopicEvent> handleEvent(Map<String, Object> properties, String body) {
        log.info("Processing topic {}", body);
        return List.of(SolaceTopicEvent.builder()
                .topic(body)
                .build());
    }
}
