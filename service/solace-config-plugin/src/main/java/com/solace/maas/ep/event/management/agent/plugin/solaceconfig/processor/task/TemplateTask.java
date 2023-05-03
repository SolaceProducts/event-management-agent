package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task;

public abstract class TemplateTask<T> implements ITask<T> {

    protected TaskConfig taskConfig;

    public TemplateTask(TaskConfig taskConfig) {
        this.taskConfig = taskConfig;
    }

    protected abstract boolean isPresent();

    protected abstract TaskResult create();

    protected abstract TaskResult update();

    protected abstract TaskResult delete();

    public TaskConfig<T> getConfig() {
        return this.taskConfig;
    }

    public TaskResult execute() {
        TaskState desiredState = this.taskConfig.getTaskState();
        switch (desiredState) {
            case PRESENT: {
                if (this.isPresent()) {
                    return this.update();
                } else {
                    return this.create();
                }
            }
            case ABSENT: {
                if (this.isPresent()) {
                    return this.delete();
                } else {
                    TaskLog log = TaskLog.builder().action("skipped").info("Object is not present").build();
                    TaskResult result = TaskResult.builder().state(desiredState).success(true).log(log).build();
                    return result;
                }
            }
            default: {
                TaskLog log = TaskLog.builder().action("error").info("Task execution internal error").build();
                TaskResult result = TaskResult.builder().state(desiredState).success(false).log(log).build();
                return result;
            }
        }
    }


}

