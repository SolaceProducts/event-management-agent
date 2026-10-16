package com.solace.maas.ep.event.management.agent.command;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.solace.client.sempv2.ApiException;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractSempCommandManagerLoggingTest {
    private static final String SEMP_ERROR_BODY = "{\"meta\":{\"error\":{\"code\":6,"
            + "\"description\":\"Could not find match for msgVpn 'secret-vpn'\",\"status\":\"NOT_FOUND\"},"
            + "\"request\":{\"method\":\"POST\",\"uri\":\"https://host:943/SEMP/v2/config/msgVpns/secret-vpn/queues\"},"
            + "\"responseCode\":400}}";

    private final AbstractSempCommandManager commandManager = new AbstractSempCommandManager() {
        @Override
        public String supportedSempCommand() {
            return "testCommand";
        }

        @Override
        protected void executeSempCommand(Command command, SempApiProvider sempApiProvider) {
            // not needed for these tests
        }
    };

    private Logger managerLogger;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        managerLogger = (Logger) LoggerFactory.getLogger(AbstractSempCommandManager.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        managerLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        managerLogger.detachAppender(listAppender);
    }

    @Test
    void badRequestLogsSempErrorStatusAndCode() {
        commandManager.logSempCommandError(new ApiException(400, "Bad Request", Map.of(), SEMP_ERROR_BODY));

        assertThat(errorMessages()).singleElement().satisfies(message -> {
            assertThat(message).isEqualTo("SEMP testCommand command not executed successfully. SEMP error: NOT_FOUND (code 6)");
            assertThat(message).doesNotContain("secret-vpn");
        });
    }

    @Test
    void nonBadRequestApiExceptionKeepsExistingMessage() {
        commandManager.logSempCommandError(new ApiException(500, "Server Error", Map.of(), SEMP_ERROR_BODY));

        assertThat(errorMessages()).singleElement().satisfies(message ->
                assertThat(message).isEqualTo("SEMP testCommand command not executed successfully"));
    }

    private List<String> errorMessages() {
        return listAppender.list.stream()
                .filter(event -> event.getLevel().isGreaterOrEqual(Level.ERROR))
                .map(ILoggingEvent::getFormattedMessage)
                .toList();
    }
}
