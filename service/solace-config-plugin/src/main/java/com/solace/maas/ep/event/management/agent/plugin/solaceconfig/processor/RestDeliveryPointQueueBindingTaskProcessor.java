package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBinding;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestDeliveryPointQueueBindingTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnRestDeliveryPointQueueBinding> {
    public RestDeliveryPointQueueBindingTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnRestDeliveryPointQueueBinding> config) {
        try {
            MsgVpnRestDeliveryPointQueueBindingResponse response = this.client.getMsgVpnApi().getMsgVpnRestDeliveryPointQueueBinding(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getQueueBindingName(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnRestDeliveryPointQueueBinding> config) {
        try {
            MsgVpnRestDeliveryPointQueueBindingResponse response=
            this.client.getMsgVpnApi().createMsgVpnRestDeliveryPointQueueBinding(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getQueueBindingName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnRestDeliveryPointQueueBinding> config) {
        try {
            MsgVpnRestDeliveryPointQueueBindingResponse response = this.client.getMsgVpnApi().updateMsgVpnRestDeliveryPointQueueBinding(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getQueueBindingName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnRestDeliveryPointQueueBinding> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnRestDeliveryPointQueueBinding(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getQueueBindingName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), e);
        }
    }
}
