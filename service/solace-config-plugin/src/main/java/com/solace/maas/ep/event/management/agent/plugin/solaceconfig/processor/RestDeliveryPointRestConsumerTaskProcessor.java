package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumer;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestDeliveryPointRestConsumerTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnRestDeliveryPointRestConsumer> {
    public RestDeliveryPointRestConsumerTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnRestDeliveryPointRestConsumer> config) {
        try {
            MsgVpnRestDeliveryPointRestConsumerResponse response = this.client.getMsgVpnApi().getMsgVpnRestDeliveryPointRestConsumer(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnRestDeliveryPointRestConsumer> config) {
        try {
            MsgVpnRestDeliveryPointRestConsumerResponse response=
            this.client.getMsgVpnApi().createMsgVpnRestDeliveryPointRestConsumer(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getRestConsumerName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnRestDeliveryPointRestConsumer> config) {
        try {
            MsgVpnRestDeliveryPointRestConsumer requestObject = config.getConfigObject();
            Boolean enabled = requestObject.getEnabled() == null || requestObject.getEnabled();
            requestObject.setEnabled(enabled);
            MsgVpnRestDeliveryPointRestConsumer disableRequest = new MsgVpnRestDeliveryPointRestConsumer();
            disableRequest.setEnabled(false);
            this.client.getMsgVpnApi().updateMsgVpnRestDeliveryPointRestConsumer(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName(),
                    disableRequest,
                    null, null);
            MsgVpnRestDeliveryPointRestConsumerResponse response = this.client.getMsgVpnApi().updateMsgVpnRestDeliveryPointRestConsumer(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName(),
                    requestObject,
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnRestDeliveryPointRestConsumer> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnRestDeliveryPointRestConsumer(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), e);
        }
    }
}
