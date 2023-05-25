package com.solace.maas.ep.event.management.agent.plugin.task;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class TemplateTaskProcessor<T>  extends ResultProcessorImpl<TaskResult, TaskConfig<T>> implements ITaskProcessor<T> {


    protected final MessagingServiceDelegateService messagingServiceDelegateService;
    @Autowired
    public TemplateTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    protected abstract void init(String messagingServiceId);
    protected abstract boolean isPresent(TaskConfig<T> config);

    protected abstract TaskResult read(TaskConfig<T> config);

    protected abstract TaskResult create(TaskConfig<T> config);

    protected abstract TaskResult update(TaskConfig<T> config);

    protected abstract TaskResult delete(TaskConfig<T> config);


    public TaskResult execute(TaskConfig<T> config) {
        TaskState desiredState = config.getState();
        switch (desiredState) {
            case PRESENT:
                return handlePresent(config);
            case ABSENT:
                return handleAbsent(config, desiredState);
            case READ:
                return handleRead(config, desiredState);
            default:
                return handleDefault(desiredState);
        }
    }

    private static TaskResult handleDefault(TaskState desiredState) {
        TaskLog log = TaskLog.builder().action("error").info("Task execution internal error").build();
        TaskResult result = TaskResult.builder().state(desiredState).success(false).log(log).build();
        return result;
    }

    private TaskResult handleRead(TaskConfig<T> config, TaskState desiredState) {
        if (this.isPresent(config)) {
            return this.read(config);
        } else {
            TaskLog log = TaskLog.builder().action("error").info("Object is not present").build();
            TaskResult result = TaskResult.builder().state(desiredState).success(false).log(log).build();
            return result;
        }
    }

    private TaskResult handleAbsent(TaskConfig<T> config, TaskState desiredState) {
        if (this.isPresent(config)) {
            return this.delete(config);
        } else {
            TaskLog log = TaskLog.builder().action("skipped").info("Object is not present").build();
            TaskResult result = TaskResult.builder().state(desiredState).success(true).log(log).build();
            return result;
        }
    }

    private TaskResult handlePresent(TaskConfig<T> config) {
        if (this.isPresent(config)) {
            return this.update(config);
        } else {
            return this.create(config);
        }
    }

    public final TaskResult handleEvent(Map<String, Object> properties, TaskConfig<T> body) throws Exception {
        this.init((String) properties.get(RouteConstants.MESSAGING_SERVICE_ID));
        return this.execute(body);
    }


}

