package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAuthorizationGroup;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAuthorizationGroupResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationGroupTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnAuthorizationGroup> {
    public AuthorizationGroupTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnAuthorizationGroup> config) {
        try {
            MsgVpnAuthorizationGroupResponse response = this.client.getMsgVpnApi().getMsgVpnAuthorizationGroup(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAuthorizationGroupName(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnAuthorizationGroup> config) {
        try {
            MsgVpnAuthorizationGroupResponse response=
            this.client.getMsgVpnApi().createMsgVpnAuthorizationGroup(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getAuthorizationGroupName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnAuthorizationGroup> config) {
        try {
            MsgVpnAuthorizationGroup requestObject = config.getConfigObject();
            Boolean enabled = requestObject.getEnabled() == null || requestObject.getEnabled();
            requestObject.setEnabled(enabled);
            MsgVpnAuthorizationGroup disableRequest = new MsgVpnAuthorizationGroup();
            disableRequest.setEnabled(false);
            this.client.getMsgVpnApi().updateMsgVpnAuthorizationGroup(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getAuthorizationGroupName(),
                    disableRequest,
                    null, null);
            MsgVpnAuthorizationGroupResponse response = this.client.getMsgVpnApi().updateMsgVpnAuthorizationGroup(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getAuthorizationGroupName(),
                    requestObject,
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnAuthorizationGroup> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnAuthorizationGroup(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAuthorizationGroupName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getAuthorizationGroupName(), config.getState(), e);
        }
    }
}
