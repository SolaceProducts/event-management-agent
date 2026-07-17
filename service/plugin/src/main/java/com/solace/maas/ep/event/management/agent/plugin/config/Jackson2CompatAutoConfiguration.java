package com.solace.maas.ep.event.management.agent.plugin.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot 4 J2 compat bridge (DATAGO-142260).
 *
 * <p>Spring Boot 4 auto-configures only a Jackson 3 ({@code tools.jackson}) {@code ObjectMapper}
 * bean. Several EMA beans constructor-inject the Jackson 2
 * {@code com.fasterxml.jackson.databind.ObjectMapper} (e.g. {@code TerraformLogProcessingService},
 * {@code FileDataMergeAggregationStrategyImpl}, the {@code Semp*CommandManager}s,
 * {@code CommandLogStreamingProcessor}), which under Spring Boot 3.x resolved to the framework's
 * auto-configured Jackson 2 mapper. This restores a Jackson 2 {@code ObjectMapper} bean mirroring
 * Spring Boot 3.x's Jackson auto-config defaults (JavaTimeModule registered, ISO-8601 dates,
 * lenient on unknown properties) so those injection points keep wiring under Spring Boot 4.
 *
 * <p>Contributed as an {@code @AutoConfiguration} (registered via
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}) rather
 * than a component-scanned {@code @Configuration} because the plugin modules each boot their own
 * Spring context with differing component-scan base packages; auto-configuration guarantees the
 * bean is present in every context that has this module on the classpath. Backs off via
 * {@code @ConditionalOnMissingBean} so tests can still supply their own primary mapper.
 *
 * <p>Remove if/when EMA migrates fully to Jackson 3.
 */
@AutoConfiguration
public class Jackson2CompatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jackson2ObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
