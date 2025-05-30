package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.HeartbeatMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.solace.maas.ep.common.metrics.ObservabilityConstants.MAAS_EMA_HEARTBEAT_EVENT_SENT;
import static com.solace.maas.ep.common.metrics.ObservabilityConstants.ORG_ID_TAG;

@ExcludeFromJacocoGeneratedReport
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.enableHeartbeats}")
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class HeartbeatGenerator {

    private final SolacePublisher solacePublisher;
    private final String runtimeAgentId;
    private final String topic;
    private final String runtimeAgentVersion;
    private final MeterRegistry meterRegistry;
    private final EventPortalProperties eventPortalProperties;

    public HeartbeatGenerator(SolaceConfiguration solaceConfiguration,
                              EventPortalProperties eventPortalProperties,
                              SolacePublisher solacePublisher,
                              BuildProperties buildProperties,
                              MeterRegistry meterRegistry) {
        this.solacePublisher = solacePublisher;
        this.runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
        topic = solaceConfiguration.getTopicPrefix() + "heartbeat/v1";
        this.runtimeAgentVersion = getFormattedVersion(buildProperties.getVersion());
        this.meterRegistry = meterRegistry;
        this.eventPortalProperties = eventPortalProperties;
    }

    @Scheduled(fixedRate = 5000)
    public void sendHeartbeat() {
        HeartbeatMessage message = new HeartbeatMessage(runtimeAgentId, Instant.now().toString(), runtimeAgentVersion);
        message.setOrgId(eventPortalProperties.getOrganizationId());
        boolean result = solacePublisher.publish(message, topic);
        logHealthMetric(message, result);
    }

    private void logHealthMetric(HeartbeatMessage message, boolean isHealthy) {
        List<Tag> tags = new ArrayList<>();
        if (Objects.nonNull(message.getOrgId())) {
            tags.add(Tag.of(ORG_ID_TAG, message.getOrgId()));
            tags.add(Tag.of("datacenter_id", Optional.ofNullable(meterRegistry.find("jvm.threads.live").meter())
                    .map(meter -> meter.getId().getTag("maas_datacenter_id"))
                    .orElse("Unknown")));
            tags.add(Tag.of("public_cema",  StringUtils.equals(eventPortalProperties.getOrganizationId(), "*") ? "true" : "false"));
            tags.add(Tag.of("cema_id",  eventPortalProperties.getRuntimeAgentId()));
        }
        meterRegistry.gauge(MAAS_EMA_HEARTBEAT_EVENT_SENT, tags, isHealthy ? 1 : 0);
    }

    private String getFormattedVersion(String version) {
        if (version.endsWith("-SNAPSHOT")) {
            return version.replace("-SNAPSHOT", "");
        }
        return version;
    }
}
