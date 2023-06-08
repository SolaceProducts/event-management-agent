package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AclProfileSubscribeExceptionTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnAclProfileSubscribeTopicException> {
    public AclProfileSubscribeExceptionTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnAclProfileSubscribeTopicException> config) {
        try {
            MsgVpnAclProfileSubscribeTopicExceptionResponse response = this.client.getMsgVpnApi().getMsgVpnAclProfileSubscribeTopicException(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getAclProfileName(),
                    config.getConfigObject().getSubscribeTopicExceptionSyntax().getValue(),
                    config.getConfigObject().getSubscribeTopicException(),
                    null, null);
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
    protected TaskResult create(TaskConfig<MsgVpnAclProfileSubscribeTopicException> config) {
        try {
            MsgVpnAclProfileSubscribeTopicExceptionResponse response = this.client.getMsgVpnApi().createMsgVpnAclProfileSubscribeTopicException(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getAclProfileName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(
                    super.getCreateOperationName(config),
                    config.getConfigObject().getAclProfileName(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(
                    super.getCreateOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnAclProfileSubscribeTopicException> config) {
        return super.createSuccessfulTaskResult(super.getNoOpOperationName(config),
                config.getConfigObject().getAclProfileName(), config.getState(), null);
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnAclProfileSubscribeTopicException> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnAclProfileSubscribeTopicException(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getAclProfileName(),
                    config.getConfigObject().getSubscribeTopicExceptionSyntax().getValue(),
                    config.getConfigObject().getSubscribeTopicException());
            return super.createSuccessfulTaskResult(
                    super.getDeleteOperationName(config), config.getConfigObject().getAclProfileName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getAclProfileName(), config.getState(), e);
        }
    }
}
