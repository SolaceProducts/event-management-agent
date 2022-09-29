package com.solace.maas.ep.runtime.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.runtime.agent.plugin.kafka.processor.consumer.KafkaConsumerGroupProcessor;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteId;
import com.solace.maas.ep.runtime.agent.plugin.kafka.route.enumeration.KafkaRouteType;
import com.solace.maas.ep.runtime.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerGroupDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor    The Processor handling the Data Collection for a Scan.
     * @param routeManager The list of Route Destinations the Data Collection events will be streamed to.
     */
    @Autowired
    public KafkaConsumerGroupDataPublisherRouteBuilder(KafkaConsumerGroupProcessor processor, RouteManager routeManager, MDCProcessor mdcProcessor) {
        super(processor, KafkaRouteId.KAFKA_CONSUMER_GROUPS.label, KafkaRouteType.KAFKA_CONSUMER_GROUPS.label,
                routeManager, mdcProcessor);
    }
}
