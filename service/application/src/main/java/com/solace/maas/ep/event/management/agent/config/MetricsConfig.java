package com.solace.maas.ep.event.management.agent.config;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Configuration
@Profile("!TEST")
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class MetricsConfig {

    private final SolaceConfiguration solaceConfiguration;

    @Autowired
    public MetricsConfig(SolaceConfiguration solaceConfiguration) {
        this.solaceConfiguration = solaceConfiguration;
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("ema_web_proxy_enabled", String.valueOf(solaceConfiguration.isProxyEnabled()));
    }
}
