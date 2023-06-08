package com.solace.maas.ep.event.management.agent.plugin.task;

public interface ITaskProcessor<T> {
    TaskResult execute(TaskConfig<T> config);
}
