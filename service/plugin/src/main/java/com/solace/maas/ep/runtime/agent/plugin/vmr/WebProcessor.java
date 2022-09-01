package com.solace.maas.ep.runtime.agent.plugin.vmr;

import com.solace.maas.ep.runtime.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolaceWebPublisher;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(VMRProperties.class)
@ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
public class WebProcessor implements Processor {

    private final SolaceWebPublisher solaceWebPublisher;

    @Autowired
    public WebProcessor(SolaceWebPublisher solaceWebPublisher) {
        this.solaceWebPublisher = solaceWebPublisher;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        String topicSuffix = exchange.getIn().getHeader(RouteConstants.TOPIC, String.class);
        String topicId = exchange.getIn().getHeader(RouteConstants.TOPIC_ID, String.class);

        solaceWebPublisher.publish(body, topicSuffix + "/" + topicId);
    }
}
