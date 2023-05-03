package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task.SEMPv2MsgVpnBaseTask;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.sempv2task.SEMPv2MsgVpnTaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;

@Slf4j
public class AclProfileClientConnectExceptionTask extends SEMPv2MsgVpnBaseTask<MsgVpnAclProfileClientConnectException> {
    public AclProfileClientConnectExceptionTask(SEMPv2MsgVpnTaskConfig taskConfig) {
        super(taskConfig);
    }

    @Override
    protected boolean isPresent() {
        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException> config =
                (SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException>) this.getConfig();
        try {
            MsgVpnAclProfileClientConnectExceptionResponse result =
                    config.getClient().getMsgVpnApi().getMsgVpnAclProfileClientConnectException(
                            config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAclProfileName(),
                            config.getConfigObject().getClientConnectExceptionAddress(), null, null);
            return (result != null && result.getData() != null);
        } catch (RestClientException re) {
            log.trace(re.getMessage(), re);
            return false;
        }
    }

    @Override
    protected TaskResult create() {
        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException> config =
                (SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException>) this.getConfig();
        try {
            MsgVpnAclProfileClientConnectExceptionResponse response =
                    config.getClient().getMsgVpnApi().createMsgVpnAclProfileClientConnectException(
                    config.getConfigObject().getMsgVpnName(),
                            config.getConfigObject().getAclProfileName(),
                            config.getConfigObject(),
                            null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(), config.getConfigObject().getAclProfileName(), config.getTaskState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(), config.getConfigObject().getAclProfileName(), config.getTaskState(), e);
        }
    }

    @Override
    protected TaskResult update() {
        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException> config =
                (SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException>) this.getConfig();
        return super.createSuccessfulTaskResult(super.getNoOpOperationName(), config.getConfigObject().getAclProfileName(), config.getTaskState(), null);
    }

    @Override
    protected TaskResult delete() {
        SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException> config =
                (SEMPv2MsgVpnTaskConfig<MsgVpnAclProfileClientConnectException>) this.getConfig();
        try {
            SempMetaOnlyResponse response = config.getClient().getMsgVpnApi().deleteMsgVpnAclProfileClientConnectException(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAclProfileName(), config.getConfigObject().getClientConnectExceptionAddress());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(), config.getConfigObject().getAclProfileName(), config.getTaskState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(), config.getConfigObject().getAclProfileName(), config.getTaskState(), e);
        }
    }
}
