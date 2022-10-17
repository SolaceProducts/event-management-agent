package com.solace.maas.ep.event.management.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.feature.KafkaFeaturesProcessor;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaFeatureDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    @Autowired
    public KafkaFeatureDataPublisherRouteBuilder(KafkaFeaturesProcessor processor, RouteManager routeManager,
                                                 MDCProcessor mdcProcessor, RouteCompleteProcessor routeCompleteProcessor) {
        super(processor, KafkaRouteId.KAFKA_FEATURES.label, KafkaRouteType.KAFKA_FEATURES.label,
                routeManager, mdcProcessor, routeCompleteProcessor);
    }
}
