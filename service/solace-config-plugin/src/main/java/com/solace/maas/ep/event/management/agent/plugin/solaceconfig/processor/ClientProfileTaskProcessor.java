package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientProfileResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientProfileResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientProfileTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnClientProfile> {
    public ClientProfileTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnClientProfile> config) {
        try {
            MsgVpnClientProfileResponse response = this.client.getMsgVpnApi().getMsgVpnClientProfile(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getClientProfileName(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnClientProfile> config) {
        try {
            MsgVpnClientProfileResponse response=
            this.client.getMsgVpnApi().createMsgVpnClientProfile(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getClientProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnClientProfile> config) {
        try {
            MsgVpnClientProfileResponse response = this.client.getMsgVpnApi().updateMsgVpnClientProfile(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getClientProfileName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnClientProfile> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnClientProfile(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getClientProfileName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getClientProfileName(), config.getState(), e);
        }
    }
}
