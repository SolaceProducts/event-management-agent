package com.solace.maas.ep.event.management.agent.plugin.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot 4 J2 compat bridge (DATAGO-142260): SB4 auto-configures only a Jackson 3
 * ({@code tools.jackson}) {@code ObjectMapper}, but several EMA beans constructor-inject the Jackson 2
 * {@code com.fasterxml.jackson.databind.ObjectMapper}. This restores a Jackson 2 mapper mirroring
 * SB 3.x's defaults (JavaTimeModule, ISO-8601 dates, lenient on unknown properties) so they keep wiring.
 *
 * <p>An {@code @AutoConfiguration} (not a component-scanned {@code @Configuration}) so every plugin
 * context gets it regardless of scan packages; backs off via {@code @ConditionalOnMissingBean}.
 * Remove when EMA migrates fully to Jackson 3.
 */
@AutoConfiguration
public class Jackson2CompatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jackson2ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // findAndRegisterModules matches SB 3.x's default module discovery. ParameterNamesModule (so
        // registered) is what lets Jackson use Lombok @Builder all-args constructors as creators;
        // without it, @Data @Builder models with no no-arg constructor fail with "no Creators".
        objectMapper.findAndRegisterModules();
        return objectMapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
