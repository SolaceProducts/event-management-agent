package com.solace.maas.ep.runtime.agent.service;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.config.eventPortal.GatewayMessagingProperties;
import com.solace.maas.ep.runtime.agent.config.eventPortal.GatewayProperties;
import com.solace.maas.ep.runtime.agent.logging.FileLoggerFactory;
import com.solace.maas.ep.runtime.agent.logging.StreamLoggerFactory;
import com.solace.maas.ep.runtime.agent.logging.StreamingAppender;
import com.solace.maas.ep.runtime.agent.processor.LoggingProcessor;
import com.solace.maas.ep.runtime.agent.service.logging.LoggingService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class LoggingServiceTests {

    @Mock
    FileLoggerFactory fileLoggerFactory;

    @Mock
    EventPortalProperties eventPortalProperties;

    @Mock
    StreamLoggerFactory streamLoggerFactory;

    @InjectMocks
    LoggingService loggingService;

    @SneakyThrows
    @Test
    public void testLoggingService() throws Exception {
        LoggingProcessor firstLoggingProcessor = mock(LoggingProcessor.class);
        LoggingProcessor secondLoggingProcessor = mock(LoggingProcessor.class);

        loggingService.addLoggingProcessor("scanId", firstLoggingProcessor);
        loggingService.addLoggingProcessor("scanId", secondLoggingProcessor);

        loggingService.removeLoggingProcessor("scanId");
        loggingService.hasLoggingProcessor("scanId");
        loggingService.getLoggingProcessor("scanId");

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testPrepareLoggers() throws Exception {
        when(streamLoggerFactory.getStreamingAppender())
                .thenReturn(mock(StreamingAppender.class));

        when(eventPortalProperties.getGateway())
                .thenReturn(mock(GatewayProperties.class));

         when(eventPortalProperties.getGateway().getMessaging())
                .thenReturn(mock(GatewayMessagingProperties.class));

         when(eventPortalProperties.getGateway().getMessaging().isStandalone())
                .thenReturn(true);

        assertThatNoException();
    }
}
