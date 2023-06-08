package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnQueueSubscription;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnQueueSubscriptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueSubscriptionTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnQueueSubscription> {
    public QueueSubscriptionTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnQueueSubscription> config) {
        try {
            MsgVpnQueueSubscriptionResponse response = this.client.getMsgVpnApi().getMsgVpnQueueSubscription(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getQueueName(),
                    config.getConfigObject().getSubscriptionTopic(),
                    null, null);
            return super.createSuccessfulTaskResult(
                    super.getReadOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(
                    super.getReadOperationName(config), config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnQueueSubscription> config) {
        try {
            MsgVpnQueueSubscriptionResponse response = this.client.getMsgVpnApi().createMsgVpnQueueSubscription(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getQueueName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(
                    super.getCreateOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(
                    super.getCreateOperationName(config), config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnQueueSubscription> config) {
        return super.createSuccessfulTaskResult(super.getNoOpOperationName(config),
                config.getConfigObject().getQueueName(), config.getState(), null);
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnQueueSubscription> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnQueueSubscription(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getQueueName(),
                    config.getConfigObject().getSubscriptionTopic());
            return super.createSuccessfulTaskResult(
                    super.getDeleteOperationName(config), config.getConfigObject().getQueueName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueName(), config.getState(), e);
        }
    }
}
