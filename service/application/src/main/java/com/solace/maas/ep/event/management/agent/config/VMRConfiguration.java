package com.solace.maas.ep.event.management.agent.config;

import com.solace.maas.ep.event.management.agent.plugin.config.EnableRtoCondition;
import com.solace.maas.ep.event.management.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
@ExcludeFromJacocoGeneratedReport
public class VMRConfiguration {

    @Bean
    @ConditionalOnMissingBean(EnableRtoCondition.class)
    public Properties vmrConfig(VMRProperties vmrProperties) {
        return vmrProperties.getVmrProperties();
    }

    @Bean
    @ConditionalOnBean(VMRProperties.class)
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
    public List<String> sessionConfig(VMRProperties sessionProperties) {
        return sessionProperties.getRTOSessionProperties();
    }
}
