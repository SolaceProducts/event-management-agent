package com.solace.maas.ep.runtime.agent.logging;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreamLoggerFactory {
    private final ProducerTemplate producerTemplate;

    public StreamLoggerFactory(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public StreamingAppender getStreamingAppender() {
        Logger logger = (Logger) LoggerFactory.getLogger("com.solace.maas");
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(true);

        StreamingAppender streamingAppender = (StreamingAppender) logger.getAppender("StreamingAppender");
        streamingAppender.setProducerTemplate(producerTemplate);

        return streamingAppender;
    }
}
