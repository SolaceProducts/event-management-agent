package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingRequestHeader;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestDeliveryPointQueueBindingRequestHeaderTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnRestDeliveryPointQueueBindingRequestHeader> {
    public RestDeliveryPointQueueBindingRequestHeaderTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnRestDeliveryPointQueueBindingRequestHeader> config) {
        try {
            MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse response = this.client.getMsgVpnApi().getMsgVpnRestDeliveryPointQueueBindingRequestHeader(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getQueueBindingName(), config.getConfigObject().getHeaderName(),
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
    protected TaskResult create(TaskConfig<MsgVpnRestDeliveryPointQueueBindingRequestHeader> config) {
        try {
            MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse response=
            this.client.getMsgVpnApi().createMsgVpnRestDeliveryPointQueueBindingRequestHeader(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getQueueBindingName(),
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
    protected TaskResult update(TaskConfig<MsgVpnRestDeliveryPointQueueBindingRequestHeader> config) {
        try {
            MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse response =
                    this.client.getMsgVpnApi().updateMsgVpnRestDeliveryPointQueueBindingRequestHeader(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                            config.getConfigObject().getQueueBindingName(),
                            config.getConfigObject().getHeaderName(),
                            config.getConfigObject(),
                            null, null);
            return super.createSuccessfulTaskResult(this.getUpdateOperationName(config),
                    config.getConfigObject().getHeaderName(),
                    config.getState(), response);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnRestDeliveryPointQueueBindingRequestHeader> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getQueueBindingName(), config.getConfigObject().getHeaderName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getQueueBindingName(), config.getState(), e);
        }
    }
}
