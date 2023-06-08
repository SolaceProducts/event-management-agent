package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskLog;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskState;
import com.solace.maas.ep.event.management.agent.plugin.task.TemplateTaskProcessor;
import org.springframework.web.client.HttpStatusCodeException;

public abstract class SEMPv2MsgVpnBaseTaskProcessor<T> extends TemplateTaskProcessor<T> {
    protected String apiVersion;
    protected SolaceSempApiClient client;

    public SEMPv2MsgVpnBaseTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    protected final boolean isPresent(TaskConfig<T> config) {
        TaskResult result = this.read(config);
        return result.isSuccess();
    }
    protected void init(String messagingServiceId){
        this.client = super.messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);
        this.apiVersion = client.getAboutApi().getAboutApi(null, null).getData().getSempVersion();
    }

    protected TaskResult createSuccessfulTaskResult(String operationName, String name, TaskState state, Object data) {
        TaskLog log = TaskLog.builder().action(operationName).info(String.format("%s %s successful", operationName, name)).build();
        TaskResult result = TaskResult.builder().success(true).data(data).state(state).log(log).build();
        return result;
    }
    protected TaskResult createFailureTaskResult(String operationName, String name, TaskState state, Throwable e) {
        Object data = e;
        String msg = String.format("%s name failure: %s", name, e.getMessage());
        if (e instanceof HttpStatusCodeException){
            HttpStatusCodeException hse = (HttpStatusCodeException) e;
            data = hse.getResponseBodyAsString();
        }
        TaskLog log = TaskLog.builder().action(operationName).info(msg).build();
        TaskResult result = TaskResult.builder().success(false).data(data).state(state).log(log).build();

        return result;
    }

    protected String getNoOpOperationName(TaskConfig<T> config){
        return String.format("noOp%s",config.getObjectType());
    }
    protected String getCreateOperationName(TaskConfig<T> config){
        return String.format("create%s",config.getObjectType());
    }
    protected String getReadOperationName(TaskConfig<T> config){
        return String.format("read%s",config.getObjectType());
    }
    protected String getUpdateOperationName(TaskConfig<T> config){
        return String.format("update%s",config.getObjectType());
    }
    protected String getDeleteOperationName(TaskConfig<T> config){
        return String.format("delete%s",config.getObjectType());
    }
}
