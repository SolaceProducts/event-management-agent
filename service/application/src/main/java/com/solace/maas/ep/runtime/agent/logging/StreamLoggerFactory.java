package com.solace.maas.ep.runtime.agent.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.solace.maas.ep.runtime.agent.service.logging.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreamLoggerFactory {
    private final LoggingService loggingService;

    public StreamLoggerFactory(LoggingService loggingService) {
        this.loggingService = loggingService;
    }


    public StreamingAppender create(ProducerTemplate producerTemplate) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();

        StreamingAppender streamingAppender = new StreamingAppender(producerTemplate, loggingService);
        streamingAppender.setName("streamingAppender");
        streamingAppender.setContext(lc);

        Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);
        logger.addAppender(streamingAppender);

        return streamingAppender;
    }
}
