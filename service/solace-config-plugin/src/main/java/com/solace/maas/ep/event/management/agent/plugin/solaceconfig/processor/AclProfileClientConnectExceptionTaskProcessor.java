package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AclProfileClientConnectExceptionTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnAclProfileClientConnectException> {

    public AclProfileClientConnectExceptionTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnAclProfileClientConnectException> config) {
        try {
            MsgVpnAclProfileClientConnectExceptionResponse result =
                    super.client.getMsgVpnApi().getMsgVpnAclProfileClientConnectException(
                            config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAclProfileName(),
                            config.getConfigObject().getClientConnectExceptionAddress(), null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), result.getData());
        } catch (Exception re) {
            log.error(re.getMessage(), re);
            return super.createFailureTaskResult(super.getCreateOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), re);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnAclProfileClientConnectException> config) {
        try {
            MsgVpnAclProfileClientConnectExceptionResponse response =
                    super.client.getMsgVpnApi().createMsgVpnAclProfileClientConnectException(
                    config.getConfigObject().getMsgVpnName(),
                            config.getConfigObject().getAclProfileName(),
                            config.getConfigObject(),
                            null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnAclProfileClientConnectException> config) {
        return super.createSuccessfulTaskResult(super.getNoOpOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), null);
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnAclProfileClientConnectException> config) {
        try {
            SempMetaOnlyResponse response = super.client.getMsgVpnApi().deleteMsgVpnAclProfileClientConnectException(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getAclProfileName(), config.getConfigObject().getClientConnectExceptionAddress());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }
}
