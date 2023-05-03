package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

public interface ITask<T> {
    public TaskConfig getConfig();
    public TaskResult execute();
}
