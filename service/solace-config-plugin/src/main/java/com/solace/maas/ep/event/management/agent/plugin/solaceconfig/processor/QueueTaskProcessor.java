package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnQueue;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnQueueResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnQueue> {
    public QueueTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnQueue> config) {
        try {
            MsgVpnQueueResponse response = this.client.getMsgVpnApi().getMsgVpnQueue(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getQueueName(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnQueue> config) {
        try {
            MsgVpnQueueResponse response=
            this.client.getMsgVpnApi().createMsgVpnQueue(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getQueueName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnQueue> config) {
        try {
            MsgVpnQueue requestObject = config.getConfigObject();
            Boolean ingressEnabled = requestObject.getIngressEnabled() == null || requestObject.getIngressEnabled();
            requestObject.setIngressEnabled(ingressEnabled);
            Boolean egressEnabled = requestObject.getEgressEnabled() == null || requestObject.getEgressEnabled();
            requestObject.setEgressEnabled(egressEnabled);
            MsgVpnQueue disableRequest = new MsgVpnQueue();
            disableRequest.setIngressEnabled(false);
            disableRequest.setEgressEnabled(false);
            this.client.getMsgVpnApi().updateMsgVpnQueue(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getQueueName(),
                    disableRequest,
                    null, null);
            MsgVpnQueueResponse response = this.client.getMsgVpnApi().updateMsgVpnQueue(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getQueueName(),
                    requestObject,
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnQueue> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnQueue(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getQueueName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }
}
