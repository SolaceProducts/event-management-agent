package com.solace.maas.ep.event.management.agent.plugin.vmr;

import com.solace.maas.ep.event.management.agent.plugin.common.messages.VmrProcessorMessage;
import com.solace.maas.ep.event.management.agent.plugin.config.EnableRtoCondition;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnMissingBean(EnableRtoCondition.class)
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class VmrProcessor implements Processor {
    private final SolacePublisher solacePublisher;

    @Autowired
    public VmrProcessor(SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        String topicSuffix = exchange.getIn().getHeader(RouteConstants.TOPIC, String.class);
        String topicId = exchange.getIn().getHeader(RouteConstants.TOPIC_ID, String.class);
        VmrProcessorMessage message = new VmrProcessorMessage(body);
        solacePublisher.publish(message, topicSuffix + "/" + topicId);
    }
}
