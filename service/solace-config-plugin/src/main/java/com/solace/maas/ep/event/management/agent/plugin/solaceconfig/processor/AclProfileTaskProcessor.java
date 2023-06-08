package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AclProfileTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnAclProfile> {
    public AclProfileTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnAclProfile> config) {
        try {
            MsgVpnAclProfileResponse response = super.client.getMsgVpnApi().getMsgVpnAclProfile(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getAclProfileName(),
                    null, null
            );
            return super.createSuccessfulTaskResult(
                    super.getReadOperationName(config),
                    config.getConfigObject().getAclProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(
                    super.getReadOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnAclProfile> config) {

        try {
            MsgVpnAclProfileResponse response = this.client.getMsgVpnApi().createMsgVpnAclProfile(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject(),
                    null, null
            );
            return super.createSuccessfulTaskResult(
                    super.getCreateOperationName(config),
                    config.getConfigObject().getAclProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(
                    super.getCreateOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), e);
        }

    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnAclProfile> config) {
        try {
            MsgVpnAclProfileResponse response = this.client.getMsgVpnApi().updateMsgVpnAclProfile(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAclProfileName(), config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getUpdateOperationName(config),config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnAclProfile> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnAclProfile(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAclProfileName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }
}
