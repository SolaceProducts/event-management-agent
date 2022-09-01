package com.solace.maas.ep.runtime.agent.plugin.route.handler.kafka.builder;

import com.solace.maas.ep.runtime.agent.plugin.processor.kafka.feature.KafkaFeaturesProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaFeatureDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    @Autowired
    public KafkaFeatureDataPublisherRouteBuilder(KafkaFeaturesProcessor processor, RouteManager routeManager) {
        super(processor, KafkaRouteId.KAFKA_FEATURES.label, KafkaRouteType.KAFKA_FEATURES.label, routeManager);
    }
}
