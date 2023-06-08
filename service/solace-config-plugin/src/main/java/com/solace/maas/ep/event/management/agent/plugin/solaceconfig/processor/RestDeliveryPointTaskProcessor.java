package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPoint;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestDeliveryPointTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnRestDeliveryPoint> {
    public RestDeliveryPointTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnRestDeliveryPoint> config) {
        try {
            MsgVpnRestDeliveryPointResponse response = this.client.getMsgVpnApi().getMsgVpnRestDeliveryPoint(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnRestDeliveryPoint> config) {
        try {
            MsgVpnRestDeliveryPointResponse response=
            this.client.getMsgVpnApi().createMsgVpnRestDeliveryPoint(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getRestDeliveryPointName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnRestDeliveryPoint> config) {
        try {
            MsgVpnRestDeliveryPoint requestObject = config.getConfigObject();
            Boolean enabled = requestObject.getEnabled() == null || requestObject.getEnabled();
            requestObject.setEnabled(enabled);
            MsgVpnRestDeliveryPoint disableRequest = new MsgVpnRestDeliveryPoint();
            disableRequest.setEnabled(false);
            this.client.getMsgVpnApi().updateMsgVpnRestDeliveryPoint(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    disableRequest,
                    null, null);
            MsgVpnRestDeliveryPointResponse response = this.client.getMsgVpnApi().updateMsgVpnRestDeliveryPoint(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    requestObject,
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnRestDeliveryPoint> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnRestDeliveryPoint(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getRestDeliveryPointName(), config.getState(), e);
        }
    }
}
