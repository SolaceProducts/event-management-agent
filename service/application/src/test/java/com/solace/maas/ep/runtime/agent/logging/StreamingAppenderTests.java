package com.solace.maas.ep.runtime.agent.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.runtime.agent.service.logging.LoggingService;
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

import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCAN_ID;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class StreamingAppenderTests {
    @Mock
    LoggingService loggingService;

    @Mock
    EventPortalProperties eventPortalProperties;

    @Mock
    ProducerTemplate producerTemplate;

    @InjectMocks
    StreamingAppender streamingAppender;

    @SneakyThrows
    @Test
    public void testStreamLoggerFactory() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ILoggingEvent event = new LoggingEvent(null, logger, Level.DEBUG,
                "test message", new Throwable("throwable message"), null);

        RouteEntity route = RouteEntity.builder()
                .id("seda:scanLogsPublisher")
                .active(true)
                .build();

        streamingAppender.setRoute(route);
        streamingAppender.setStandalone(false);

        MDC.put(SCAN_ID, "12345");

        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        streamingAppender.append(event);

        assertThatNoException();
    }
}
