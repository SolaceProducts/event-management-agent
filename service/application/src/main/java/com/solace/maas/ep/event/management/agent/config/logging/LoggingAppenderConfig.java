package com.solace.maas.ep.event.management.agent.config.logging;

import ch.qos.logback.classic.Logger;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.logging.StreamingAppender;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import org.apache.camel.ProducerTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ExcludeFromJacocoGeneratedReport
@Profile("!TEST")
public class LoggingAppenderConfig {
    private final StreamingAppender streamingAppender;

    private final ProducerTemplate producerTemplate;

    private final EventPortalProperties eventPortalProperties;

    public LoggingAppenderConfig(ProducerTemplate producerTemplate, EventPortalProperties eventPortalProperties) {
        this.eventPortalProperties = eventPortalProperties;
        this.producerTemplate = producerTemplate;

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        streamingAppender = (StreamingAppender) logger.getAppender("StreamingAppender");
    }

    @Bean
    public StreamingAppender configuredStreamingAppender() {
        streamingAppender.setProducerTemplate(producerTemplate);
        streamingAppender.setStandalone(eventPortalProperties.getGateway().getMessaging().isStandalone());

        return streamingAppender;
    }
}
