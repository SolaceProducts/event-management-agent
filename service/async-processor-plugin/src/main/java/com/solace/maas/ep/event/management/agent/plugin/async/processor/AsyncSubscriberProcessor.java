package com.solace.maas.ep.event.management.agent.plugin.async.processor;

import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AsyncSubscriberProcessor extends ResultProcessorImpl<List<TestEvent>, Long> {
    @Override
    public List<TestEvent> handleEvent(Map<String, Object> properties, Long body) throws Exception {
        return List.of(TestEvent.builder()
                .data("data_" + body)
                .count(body)
                .build());
    }
}
