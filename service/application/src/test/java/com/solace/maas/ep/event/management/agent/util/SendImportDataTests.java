package com.solace.maas.ep.event.management.agent.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.processor.util.SendImportData;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class SendImportDataTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private ProducerTemplate producerTemplate;

    @InjectMocks
    private SendImportData sendImportData;

    @SneakyThrows
    @Test
    public void testSendImportDataAsync() {
        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        sendImportData.sendImportDataAsync("groupId", "scanId",
                "scanType", "messagingServiceId", "body");

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testSendImportLogsAsync() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ILoggingEvent event = new LoggingEvent(null, logger, Level.DEBUG,
                "test message", new Throwable("throwable message"), null);

        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        sendImportData.sendImportLogsAsync("scanId", "scanType",
                "messagingServiceId", event);

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testSendImportDataAsyncExceptionally() {
        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.failedFuture(new Exception()));

        CompletableFuture<Exchange> result = sendImportData.sendImportDataAsync("groupId", "scanId",
                "scanType", "messagingServiceId", "body");

        try {
            result.get();
        } catch (ExecutionException e) {
            assertThat(e.getCause()).isInstanceOf(Exception.class);
            assertThat(result).isCompletedExceptionally();
            exception.expect(Exception.class);
        }
    }

    @SneakyThrows
    @Test
    public void testSendImportLogsAsyncExceptionally() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ILoggingEvent event = new LoggingEvent(null, logger, Level.DEBUG,
                "test message", new Throwable("throwable message"), null);

        when(producerTemplate.asyncSend(any(String.class), any(Processor.class)))
                .thenReturn(CompletableFuture.failedFuture(new Exception()));

        CompletableFuture<Exchange> result =
                sendImportData.sendImportLogsAsync("scanId", "scanType", "messagingServiceId", event);

        try {
            result.get();
        } catch (ExecutionException e) {
            assertThat(e.getCause()).isInstanceOf(Exception.class);
            assertThat(result).isCompletedExceptionally();
            exception.expect(Exception.class);
        }
    }

}
