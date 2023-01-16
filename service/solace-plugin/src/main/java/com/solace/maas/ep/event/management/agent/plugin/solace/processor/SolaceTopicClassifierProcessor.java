package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.event.SolaceTopicEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SolaceTopicClassifierProcessor extends ResultProcessorImpl<Void, List<SolaceTopicEvent>> {
    @Override
    public Void handleEvent(Map<String, Object> properties, List<SolaceTopicEvent> body) {
        log.info("Classifying topic {}", body);
        return null;
    }
}
