package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSession;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSessionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSession;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttSessionTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnMqttSession> {
    public MqttSessionTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnMqttSession> config) {
        try {
            MsgVpnMqttSessionResponse response = this.client.getMsgVpnApi().getMsgVpnMqttSession(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getMqttSessionClientId(),
                    config.getConfigObject().getMqttSessionVirtualRouter().getValue(), null, null);
            return super.createSuccessfulTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), response.getData());
        } catch (Exception e) {
            log.trace(e.getMessage(), e);
            return super.createFailureTaskResult(super.getReadOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult create(TaskConfig<MsgVpnMqttSession> config) {
        try {
            MsgVpnMqttSessionResponse response=
            this.client.getMsgVpnApi().createMsgVpnMqttSession(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getCreateOperationName(config),
            config.getConfigObject().getMqttSessionClientId(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getCreateOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult update(TaskConfig<MsgVpnMqttSession> config) {
        try {
            MsgVpnMqttSessionResponse response = this.client.getMsgVpnApi().updateMsgVpnMqttSession(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getMqttSessionClientId(),
                    config.getConfigObject().getMqttSessionVirtualRouter().getValue(),
                    config.getConfigObject(),
                    null, null);
            return super.createSuccessfulTaskResult(super.getUpdateOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), response.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnMqttSession> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnMqttSession(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getMqttSessionClientId(),
                    config.getConfigObject().getMqttSessionVirtualRouter().getValue());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getMqttSessionClientId(), config.getState(), e);
        }
    }
}
