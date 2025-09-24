package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.util.MdcUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.IS_LINKED_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_CONFIG_PUSH_EVENT_CYCLE_TIME;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_CONFIG_PUSH_EVENT_RECEIVED;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORIGIN_ORG_ID_TAG;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.STATUS_TAG;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandMessageProcessor implements MessageProcessor<CommandMessage> {

    private final CommandManager commandManager;

    private final DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper;
    private final MeterRegistry meterRegistry;

    public CommandMessageProcessor(CommandManager commandManager,
                                   DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper,
                                   MeterRegistry meterRegistry) {
        this.commandManager = commandManager;
        this.dynamicResourceConfigurationHelper = dynamicResourceConfigurationHelper;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void processMessage(CommandMessage message) {
        MDC.put(RouteConstants.COMMAND_CORRELATION_ID, message.getCommandCorrelationId());
        MDC.put(RouteConstants.MESSAGING_SERVICE_ID, message.getServiceId());
        MDC.put(RouteConstants.ORG_ID, message.getOrgId());
        MDC.put(RouteConstants.ORIGIN_ORG_ID, message.getOriginOrgId());
        MDC.put(RouteConstants.IS_LINKED, MdcUtil.isLinked(message.getOrgId(), message.getOriginOrgId()) ? "true" : "false");
        log.info("Config push command processor started. context={} orgId={} actorId={} ",
                message.getContext(), message.getOrgId(), message.getActorId());
        logConfigPushMetric(message);
        if (CollectionUtils.isNotEmpty(message.getResources())) {
            dynamicResourceConfigurationHelper.loadSolaceBrokerResourceConfigurations(message.getResources());
        }
        commandManager.execute(message);
    }

    private void logConfigPushMetric(CommandMessage message) {
        List<Tag> tags = new ArrayList<>();
        if (Objects.nonNull(message.getStatus())) {
            tags.add(Tag.of(STATUS_TAG, message.getStatus().name()));
        }
        if (StringUtils.isNotBlank(message.getOrgId())) {
            tags.add(Tag.of(ORG_ID_TAG, message.getOrgId()));
        }
        tags.add(Tag.of(ORIGIN_ORG_ID_TAG, message.getOriginOrgId()));
        tags.add(Tag.of(IS_LINKED_TAG, MdcUtil.isLinked(message.getOrgId(), message.getOriginOrgId()) ? "true" : "false"));
        meterRegistry.counter(MAAS_EMA_CONFIG_PUSH_EVENT_RECEIVED, tags).increment();
    }

    @Override
    public Class supportedClass() {
        return CommandMessage.class;
    }

    @Override
    public CommandMessage castToMessageClass(Object message) {
        return (CommandMessage) message;
    }

    @Override
    public void onFailure(Exception e, CommandMessage message) {
        commandManager.handleError(e, message);
    }

    @Override
    public void sendCycleTimeMetric(Instant startTime, CommandMessage message, String status) {
        Instant endTime = Instant.now();
        long duration = endTime.toEpochMilli() - startTime.toEpochMilli();
        Timer jobCycleTime = Timer
                .builder(MAAS_EMA_CONFIG_PUSH_EVENT_CYCLE_TIME)
                .tag(ORG_ID_TAG, message.getOrgId())
                .tag(ORIGIN_ORG_ID_TAG, message.getOriginOrgId())
                .tag(IS_LINKED_TAG, MdcUtil.isLinked(message.getOrgId(), message.getOriginOrgId()) ? "true" : "false")
                .tag(STATUS_TAG, status)
                .register(meterRegistry);
        jobCycleTime.record(duration, TimeUnit.MILLISECONDS);
    }
}
