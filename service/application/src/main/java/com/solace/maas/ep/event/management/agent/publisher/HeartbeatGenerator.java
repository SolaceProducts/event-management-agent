package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.HeartbeatMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.info.BuildProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@ExcludeFromJacocoGeneratedReport
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false and ${eventPortal.gateway.messaging.enableHeartbeats}")
public class HeartbeatGenerator {

    private final SolacePublisher solacePublisher;
    private final String runtimeAgentId;
    private final String topic;
    private final String runtimeAgentVersion;

    public HeartbeatGenerator(SolaceConfiguration solaceConfiguration,
                              EventPortalProperties eventPortalProperties,
                              SolacePublisher solacePublisher,
                              BuildProperties buildProperties) {
        this.solacePublisher = solacePublisher;
        this.runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
        topic = solaceConfiguration.getTopicPrefix() + "heartbeat/v1";
        this.runtimeAgentVersion = getFormattedVersion(buildProperties.getVersion());
    }

    @Scheduled(fixedRate = 5000)
    public void sendHeartbeat() {
        HeartbeatMessage message = new HeartbeatMessage(runtimeAgentId, Instant.now().toString(), runtimeAgentVersion);
        solacePublisher.publish(message, topic);
    }

    private String getFormattedVersion(String version) {
        if (version.endsWith("-SNAPSHOT")) {
            return version.replace("-SNAPSHOT", "");
        }
        return version;
    }
}
