package com.solace.maas.ep.runtime.agent.plugin.route.handler.kafka.builder;

import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.topic.KafkaTopicListingProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public KafkaDataPublisherRouteBuilder(KafkaTopicListingProcessor processor, RouteManager routeManager) {
        super(processor, KafkaRouteId.KAFKA_TOPIC_LISTING.label, KafkaRouteType.KAFKA_TOPIC_LISTING.label, routeManager);
    }
}
