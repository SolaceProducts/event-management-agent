package com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.rabbitmq.processor.queue.RabbitMqQueueProcessor;
import com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.enumeration.RabbitMqRouteId;
import com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.enumeration.RabbitMqRouteType;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.RouteManager;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqQueueDataPublisherRoute extends DataPublisherRouteBuilder {
    public RabbitMqQueueDataPublisherRoute(RabbitMqQueueProcessor processor, RouteManager routeManager,
                                           MDCProcessor mdcProcessor, EmptyScanEntityProcessor emptyScanEntityProcessor) {
        super(processor, RabbitMqRouteId.RABBIT_MQ_QUEUE.label, RabbitMqRouteType.RABBIT_MQ_QUEUE.label,
                routeManager, mdcProcessor, emptyScanEntityProcessor);
    }
}
