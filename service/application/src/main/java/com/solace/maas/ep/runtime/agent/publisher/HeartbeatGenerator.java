package com.solace.maas.ep.runtime.agent.publisher;

import com.solace.maas.ep.common.messages.HeartbeatMessage;
import com.solace.maas.ep.runtime.agent.config.SolaceConfiguration;
import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@ExcludeFromJacocoGeneratedReport
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false and ${eventPortal.gateway.messaging.enableHeartbeats}")
public class HeartbeatGenerator {

    private final SolacePublisher solacePublisher;
    private String runtimeAgentId;
    private String topic;

    public HeartbeatGenerator(SolaceConfiguration solaceConfiguration,
                              EventPortalProperties eventPortalProperties,
                              SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
        this.runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
        topic = solaceConfiguration.getTopicPrefix() + "heartbeat/v1";

    }

    @Scheduled(fixedRate = 5000)
    public void sendHeartbeat() {
        HeartbeatMessage message = new HeartbeatMessage(runtimeAgentId, Instant.now().toString());
        solacePublisher.publish(message, topic);
    }

}
