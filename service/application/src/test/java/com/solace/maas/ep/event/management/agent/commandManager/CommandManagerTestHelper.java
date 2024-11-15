package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandBundle;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandResult;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.ExecutionType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType.generic;

public final class CommandManagerTestHelper {

    private CommandManagerTestHelper() {
        throw new UnsupportedOperationException();
    }

    public static Boolean verifyCommandPublisherIsInvoked(CommandPublisher commandPublisher, int numberOfExpectedInvocations) {
        return Mockito.mockingDetails(commandPublisher).getInvocations().size() == numberOfExpectedInvocations;
    }


    public static Path setCommandStatusAndReturnExecutionLog(Command targetCommand,
                                                             JobStatus targetStatus,
                                                             boolean ignoreResult,
                                                             Path basePath) {

        if (targetStatus == JobStatus.success) {
            targetCommand.setResult(CommandResult.builder()
                    .status(JobStatus.success)
                    .result(Map.of()).build());
            return basePath.resolve(targetCommand.getCommand());
        } else {
            //simulating a failed command
            targetCommand.setResult(null);
            targetCommand.setIgnoreResult(ignoreResult);
            return null;
        }

    }

    public static CommandMessage buildCommandMessageForConfigPush(String targetOrgId,
                                                                  String targetMessagingServiceId) {

        List<Command> commands = List.of(
                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(false)
                        .body("asdfasdfadsf")
                        .command("write_HCL")
                        .build(),
                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(false)
                        .body("asdfasdfadsf")
                        .command("write_HCL")
                        .build(),
                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(true)
                        .body("asdfasdfadsf")
                        .command("sync")
                        .build(),

                Command.builder()
                        .commandType(CommandType.terraform)
                        .ignoreResult(false)
                        .body("asdfasdfadsf")
                        .command("apply")
                        .build());

        CommandMessage message = new CommandMessage();
        message.setOrigType(MOPSvcType.maasEventMgmt);
        message.withMessageType(generic);
        message.setContext("abc");
        message.setServiceId(targetMessagingServiceId);
        message.setActorId("myActorId");
        message.setOrgId(targetOrgId);
        message.setTraceId("myTraceId");
        message.setCommandCorrelationId("myCorrelationIdabc");
        message.setCommandBundles(List.of(
                CommandBundle.builder()
                        .executionType(ExecutionType.serial)
                        .exitOnFailure(true)
                        .commands(commands)
                        .build()));
        return message;
    }
}
