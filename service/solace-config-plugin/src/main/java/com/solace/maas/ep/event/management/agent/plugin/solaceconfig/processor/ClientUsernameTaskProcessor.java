package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsername;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsernameResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientUsernameTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnClientUsername> {
    public ClientUsernameTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnClientUsername> config) {
        try {
            MsgVpnClientUsernameResponse response = this.client.getMsgVpnApi().getMsgVpnClientUsername(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getClientUsername(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnClientUsername> config) {
        try {
            MsgVpnClientUsernameResponse response=
            this.client.getMsgVpnApi().createMsgVpnClientUsername(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getClientUsername(), config.getTaskState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnClientUsername> config) {
        try {
            MsgVpnClientUsername requestObject = config.getConfigObject();
            Boolean enabled = requestObject.getEnabled() == null || requestObject.getEnabled();
            requestObject.setEnabled(enabled);
            MsgVpnClientUsername disableRequest = new MsgVpnClientUsername();
            disableRequest.setEnabled(false);
            this.client.getMsgVpnApi().updateMsgVpnClientUsername(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getClientUsername(),
                    disableRequest,
                    null, null);
            MsgVpnClientUsernameResponse response = this.client.getMsgVpnApi().updateMsgVpnClientUsername(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getClientUsername(),
                    requestObject,
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnClientUsername> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnClientUsername(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getClientUsername());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getClientUsername(), config.getTaskState(), e);
        }
    }
}
