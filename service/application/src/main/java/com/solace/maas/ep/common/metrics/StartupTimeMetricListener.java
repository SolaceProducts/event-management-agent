package com.solace.maas.ep.common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupTimeMetricListener implements ApplicationListener<ApplicationStartedEvent> {
    private final MeterRegistry meterRegistry;

    public StartupTimeMetricListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        double startupTime = event.getTimeTaken().toMillis() / 1000.0;
        meterRegistry.gauge("maas.application.startup_time", Tags.of("application", event.getSpringApplication().getMainApplicationClass().getSimpleName(),
                "service", System.getProperty("spring.application.name", "unknown")), startupTime);
    }
}