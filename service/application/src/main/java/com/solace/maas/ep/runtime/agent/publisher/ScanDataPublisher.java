package com.solace.maas.ep.runtime.agent.publisher;

import com.solace.maas.ep.common.messages.ScanDataMessage;
import com.solace.maas.ep.runtime.agent.config.SolaceConfiguration;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolacePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataPublisher {

    private final SolacePublisher solacePublisher;
    private final String topic;

    public ScanDataPublisher(SolaceConfiguration solaceConfiguration,
                             SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
        topic = solaceConfiguration.getTopicPrefix() + "ep/v1";
    }

    public void sendScanData(ScanDataMessage message) {
        solacePublisher.publish(message, topic);
    }
}
