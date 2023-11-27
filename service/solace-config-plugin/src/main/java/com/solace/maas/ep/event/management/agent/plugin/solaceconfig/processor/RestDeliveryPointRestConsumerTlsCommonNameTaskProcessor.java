package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semptask.SEMPv2MsgVpnBaseTaskProcessor;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestDeliveryPointRestConsumerTlsCommonNameTaskProcessor extends SEMPv2MsgVpnBaseTaskProcessor<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName> {
    public RestDeliveryPointRestConsumerTlsCommonNameTaskProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super(messagingServiceDelegateService);
    }

    @Override
    protected TaskResult read(TaskConfig<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName> config) {
        try {
            MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse response = this.client.getMsgVpnApi().getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName(), config.getConfigObject().getTlsTrustedCommonName(),
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
    protected TaskResult create(TaskConfig<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName> config) {
        try {
            MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse response=
            this.client.getMsgVpnApi().createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(
                    config.getConfigObject().getMsgVpnName(),
                    config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName(),
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
    protected TaskResult update(TaskConfig<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName> config) {
        try {

            return super.createSuccessfulTaskResult(super.getNoOpOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(getUpdateOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), e);
        }
    }

    @Override
    protected TaskResult delete(TaskConfig<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName> config) {
        try {
            SempMetaOnlyResponse response = this.client.getMsgVpnApi().deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(
                    config.getConfigObject().getMsgVpnName(), config.getConfigObject().getRestDeliveryPointName(),
                    config.getConfigObject().getRestConsumerName(), config.getConfigObject().getTlsTrustedCommonName());
            return super.createSuccessfulTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), response.getMeta());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return super.createFailureTaskResult(super.getDeleteOperationName(config),
                    config.getConfigObject().getRestConsumerName(), config.getState(), e);
        }
    }
}
