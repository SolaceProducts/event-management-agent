package com.solace.maas.ep.event.management.agent.command;

import com.solace.client.sempv2.ApiException;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractSempCommandManagerSempErrorTest {

    private TestSempCommandManager manager;
    private Command command;

    static class TestSempCommandManager extends AbstractSempCommandManager {
        RuntimeException nonSempException;
        ApiException sempException;

        @Override
        public String supportedSempCommand() {
            return "delete";
        }

        @Override
        public void validate(Command command, SempApiProvider sempApiProvider) {
            // no-op for unit tests
        }

        @Override
        protected void executeSempCommand(Command command, SempApiProvider sempApiProvider) throws Exception {
            if (sempException != null) {
                throw sempException;
            }
            if (nonSempException != null) {
                throw nonSempException;
            }
        }
    }

    @BeforeEach
    void setUp() {
        manager = new TestSempCommandManager();
        command = new Command();
        command.setCommand("delete");
        command.setCommandType(CommandType.semp);
        command.setParameters(Map.of(
                SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE, "solaceQueue",
                SempCommandConstants.SEMP_COMMAND_DATA, Map.of()
        ));
    }

    @Test
    void execute_sempApiException_capturesResponseBodyAndUserMessageInLogs() {
        String responseBody = "{\"meta\":{\"error\":{\"code\":89,\"description\":\"Queue limit exceeded\",\"status\":\"MAX_NUM_EXCEEDED\"}}}";
        manager.sempException = new ApiException(400, Map.of(), responseBody);

        manager.execute(command, null);

        CommandResult result = command.getResult();
        assertThat(result.getStatus()).isEqualTo(JobStatus.error);
        assertThat(result.getLogs()).isNotEmpty();
        assertThat(result.getLogs().get(0)).containsEntry("sempResponseBody", responseBody);
        assertThat((String) result.getLogs().get(0).get("userMessage")).isNotBlank();
    }

    @Test
    void execute_sempApiException_non400_setsErrorStatus() {
        manager.sempException = new ApiException(500, Map.of(), "{\"error\":\"internal\"}");

        manager.execute(command, null);

        assertThat(result().getStatus()).isEqualTo(JobStatus.error);
        assertThat(result().getLogs().get(0)).containsEntry("sempResponseBody", "{\"error\":\"internal\"}");
    }

    @Test
    void handleSempOperationException_notFoundBody_doesNotThrow() {
        ApiException notFound = new ApiException(400, Map.of(), "{\"meta\":{\"error\":{\"status\":\"NOT_FOUND\"}}}");
        org.assertj.core.api.Assertions.assertThatNoException()
                .isThrownBy(() -> manager.handleSempOperationException(notFound, "solaceQueue", "delete"));
    }

    @Test
    void handleSempOperationException_otherError_rethrows() {
        ApiException other = new ApiException(400, Map.of(), "{\"meta\":{\"error\":{\"status\":\"MAX_NUM_EXCEEDED\"}}}");
        org.junit.jupiter.api.Assertions.assertThrows(ApiException.class,
                () -> manager.handleSempOperationException(other, "solaceQueue", "delete"));
    }

    @Test
    void execute_nonApiException_usesSetCommandErrorPath() {
        manager.nonSempException = new IllegalStateException("Connection refused");

        manager.execute(command, null);

        assertThat(result().getStatus()).isEqualTo(JobStatus.error);
        // setCommandError uses e.getMessage() — no sempResponseBody key
        assertThat(result().getLogs().get(0))
                .doesNotContainKey("sempResponseBody")
                .containsEntry("message", "Connection refused");
    }

    private CommandResult result() {
        return command.getResult();
    }
}
