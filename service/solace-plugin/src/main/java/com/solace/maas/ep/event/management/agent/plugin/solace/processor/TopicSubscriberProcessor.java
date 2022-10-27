package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.event.TopicEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TopicSubscriberProcessor extends ResultProcessorImpl<List<TopicEvent>, String> {
    @Override
    public List<TopicEvent> handleEvent(Map<String, Object> properties, String body) {
        log.info("Processing topic {}", body);
        return List.of(TopicEvent.builder()
                .data("data_" + body)
                .subscription(body)
                .build());
    }
}
