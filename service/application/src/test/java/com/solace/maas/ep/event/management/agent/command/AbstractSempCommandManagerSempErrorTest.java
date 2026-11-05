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
    void execute_sempApiException_capturesResponseBodyInLogs() {
        String responseBody = "{\"meta\":{\"error\":{\"code\":89,\"description\":\"Queue limit exceeded\",\"status\":\"MAX_NUM_EXCEEDED\"}}}";
        manager.sempException = new ApiException(400, Map.of(), responseBody);

        manager.execute(command, null);

        CommandResult result = command.getResult();
        assertThat(result.getStatus()).isEqualTo(JobStatus.error);
        assertThat(result.getLogs()).isNotEmpty();
        assertThat(result.getLogs().get(0)).containsEntry("sempResponseBody", responseBody);
    }

    @Test
    void execute_sempApiException_setsUserMessageInLogs() {
        String responseBody = "{\"meta\":{\"error\":{\"code\":89,\"description\":\"Queue limit exceeded\",\"status\":\"MAX_NUM_EXCEEDED\"}}}";
        manager.sempException = new ApiException(400, Map.of(), responseBody);

        manager.execute(command, null);

        assertThat((String) result().getLogs().get(0).get("userMessage")).isNotBlank();
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
