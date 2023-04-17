package com.solace.maas.ep.event.management.agent.plugin.kafka.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.kafka.processor.acl.KafkaAclListingProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.stereotype.Component;

@Component
public class KafkaAclDataPublisherRoute extends DataPublisherRouteBuilder {
    /**
     * @param processor                The Processor handling the Data Collection for a Scan.
     * @param routeManager             The list of Route Destinations the Data Collection events will be streamed to.
     * @param mdcProcessor             The Processor handling the MDC data for logging.
     * @param emptyScanEntityProcessor
     */
    public KafkaAclDataPublisherRoute(KafkaAclListingProcessor processor, RouteManager routeManager,
                                      MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, "kafkaAclListing", "aclListing", routeManager, mdcProcessor, emptyScanEntityProcessor);
    }
}
