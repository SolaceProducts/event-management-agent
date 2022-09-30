package com.solace.maas.ep.runtime.agent.publisher;

import com.solace.maas.ep.common.messages.ScanLogMessage;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolacePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanLogsPublisher {

    private final SolacePublisher solacePublisher;

    public ScanLogsPublisher(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    public void sendScanLogData(ScanLogMessage message, Map<String, String> topicDetails) {
        String topicString = String.format("sc/ep/runtime/%s/%s/scan/logs/v1/%s/%s",
                topicDetails.get("orgId"),
                topicDetails.get("runtimeAgentId"),
                topicDetails.get("messagingServiceId"),
                topicDetails.get("scanId"));

        solacePublisher.publish(message, topicString);
    }
}
