package com.solace.maas.ep.runtime.agent.service;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.processor.LoggingProcessor;
import com.solace.maas.ep.runtime.agent.service.logging.LoggingService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class LoggingServiceTests {

    @InjectMocks
    LoggingService loggingService;

    @SneakyThrows
    @Test
    public void testInMemoryLoggingService() throws Exception {
        LoggingProcessor loggingProcessor = mock(LoggingProcessor.class);

        loggingService.addLoggingProcessor("scanId", loggingProcessor);
        loggingService.removeLoggingProcessor("scanId");
        loggingService.hasLoggingProcessor("scanId");
        loggingService.getLoggingProcessor("scanId");

        assertThatNoException();
    }
}
