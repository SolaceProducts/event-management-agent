package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistrySchemaProcessor;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration.ConfluentSchemaRegistryRouteId;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration.ConfluentSchemaRegistryRouteType;
import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfluentSchemaRegistryDataPublisherRouteBuilder extends DataPublisherRouteBuilder {
    /**
     * @param processor The Processor handling the Data Collection for a Scan.
     */
    @Autowired
    public ConfluentSchemaRegistryDataPublisherRouteBuilder(ConfluentSchemaRegistrySchemaProcessor processor,
                                                            RouteManager routeManager,
                                                            MDCProcessor mdcProcessor,
                                                            ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super(processor, ConfluentSchemaRegistryRouteId.CONFLUENT_SCHEMA_REGISTRY_SCHEMA.label,
                ConfluentSchemaRegistryRouteType.CONFLUENT_SCHEMA_REGISTRY_SCHEMA.label,
                routeManager, mdcProcessor, scanTypeDescendentsProcessor);
    }
}
