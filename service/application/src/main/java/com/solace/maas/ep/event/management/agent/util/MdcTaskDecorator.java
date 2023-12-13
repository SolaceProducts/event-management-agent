package com.solace.maas.ep.event.management.agent.util;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = (Map) Optional.ofNullable(MDC.getCopyOfContextMap()).orElse(new HashMap());
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }

        };
    }
}
