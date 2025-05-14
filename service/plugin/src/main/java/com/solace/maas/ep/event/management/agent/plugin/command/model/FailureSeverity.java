package com.solace.maas.ep.event.management.agent.plugin.command.model;

public enum FailureSeverity {
    ERROR, // Default, will abort the job
    WARNING // Will log a warning but allow the job to continue -> TODO: should also abort the job
}
