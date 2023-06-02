package com.solace.maas.ep.event.management.agent.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import lombok.SneakyThrows;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class StreamingAppenderTests {

    @Mock
    ProducerTemplate producerTemplate;

    @InjectMocks
    StreamingAppender streamingAppender;

    @SneakyThrows
    @Test
    public void testStreamingAppender() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ILoggingEvent event = new LoggingEvent(null, logger, Level.DEBUG,
                "test message", new Throwable("throwable message"), null);

        RouteEntity.builder()
                .id("seda:scanLogsPublisher")
                .active(true)
                .build();

        streamingAppender.setStandalone(false);

        MDC.put(RouteConstants.SCAN_ID, "12345");
        MDC.put(RouteConstants.SCAN_TYPE, "topicListing");
        MDC.put(RouteConstants.SCHEDULE_ID, "groupId");
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, "messagingServiceId");

        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        streamingAppender.append(event);

        assertThatNoException();
    }
}
