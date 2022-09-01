package com.solace.maas.ep.runtime.agent.plugin.publisher;

import com.solace.maas.ep.runtime.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.messaging.resources.Topic;
import com.solacesystems.solclientj.core.SolclientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnBean(VMRProperties.class)
@ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
public class SolaceWebPublisher {
    private final RtoMessageBuilder webMessagingService;

    private final Topic solaceTopic;

    @Autowired
    public SolaceWebPublisher(RtoMessageBuilder webMessagingService, @Qualifier("vmrTopic") Topic solaceTopic) {
        this.webMessagingService = webMessagingService;
        this.solaceTopic = solaceTopic;
    }

    public void publish(String data, String id) {
        Topic topic = Topic.of(solaceTopic.getName() + "/" + id);

        try {
            webMessagingService.publish(data, topic);
            webMessagingService.destroy();
        } catch (SolclientException e) {
            log.error("Solclient Exception while attempting to publish message: {}", data, e);
        }
    }
}
