package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskLog;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskResult;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskState;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TemplateTask;
import org.springframework.web.client.HttpStatusCodeException;

public abstract class SEMPv2MsgVpnBaseTask<T> extends TemplateTask<T> {
    protected String apiVersion;
    protected SolaceSempApiClient client;
    public SEMPv2MsgVpnBaseTask(SEMPv2MsgVpnTaskConfig taskConfig, SolaceSempApiClient client){
        super(taskConfig);
        this.client = client;
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

    protected String getNoOpOperationName(){
        return String.format("noOp%",((SEMPv2MsgVpnTaskConfig)this.getConfig()).getObjectType());
    }
    protected String getCreateOperationName(){
        return String.format("create%",((SEMPv2MsgVpnTaskConfig)this.getConfig()).getObjectType());
    }
    protected String getUpdateOperationName(){
        return String.format("update%",((SEMPv2MsgVpnTaskConfig)this.getConfig()).getObjectType());
    }
    protected String getDeleteOperationName(){
        return String.format("delete%",((SEMPv2MsgVpnTaskConfig)this.getConfig()).getObjectType());
    }
}
