package com.solace.maas.ep.event.management.agent.plugin.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandRequest {
    private String commandCorrelationId;
    private String context;
    private String serviceId;
    private JobStatus status;
    private List<CommandBundle> commandBundles;
    private Instant createdTime;
    private Instant updatedTime;
    private String orgId;

    public long getLifetime(TemporalUnit timeUnit) {
        if (createdTime == null) {
            // micrometer won't record negative numbers
            return -1L;
        }
        if (updatedTime == null) {
            return createdTime.until(Instant.now(), timeUnit);
        }
        return createdTime.until(updatedTime, timeUnit);
    }

    public void determineStatus() {
        boolean hasAtLeastOneError = false;
        if (CollectionUtils.isNotEmpty(commandBundles)) {
            hasAtLeastOneError = commandBundles.stream().anyMatch(bundle ->
                    bundle.getCommands().stream().anyMatch(Command::hasSignificantErrorResult));
        }
        setStatus(hasAtLeastOneError ? JobStatus.error : JobStatus.success);
    }

}
