package com.solace.maas.ep.event.management.agent.plugin.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
public class Command {
    private CommandType commandType; // "terraform", "bash", SEMP, etc...
    private String command; // A tf command like `apply` or `state rm`
    private String body; // The body of the .tf file
    private Map<String, Object> parameters;
    private Boolean ignoreResult;
    private CommandResult result;
    private Boolean isPreFlightCheck = false;
    private PreFlightCheckType preFlightCheckType;

    public boolean hasSignificantErrorResult() {
        if (!Boolean.TRUE.equals(ignoreResult) && result != null) {
            return JobStatus.error.equals(result.getStatus());
        }
        return false;
    }
}
