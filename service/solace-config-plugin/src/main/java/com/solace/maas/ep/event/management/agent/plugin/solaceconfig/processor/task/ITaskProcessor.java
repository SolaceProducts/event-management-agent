package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

public interface ITaskProcessor<T> {
    public TaskResult execute(TaskConfig<T> config);
}
