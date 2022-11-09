package com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.enumeration.RabbitMqScanType;
import com.solace.maas.ep.event.management.agent.plugin.rabbitmq.route.enumeration.RabbitMqRouteId;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component("rabbitMqRouteDelegateImpl")
public class RabbitMqRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public RabbitMqRouteDelegateImpl() {
        super("RABBITMQ");
        System.out.println("RABBITMQ PLUGIN WAS LOADED!!!!");
    }

    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        List<RouteBundle> result = new ArrayList<>();

        final RabbitMqScanType rabbitMqScanType = RabbitMqScanType.valueOf(scanType);

        switch (rabbitMqScanType) {
            case RABBITMQ_ALL:
                break;
            case RABBITMQ_QUEUE:
                result.add(queueRouteBundle(destinations, recipients, messagingServiceId,
                        RabbitMqScanType.RABBITMQ_QUEUE.name()));

                break;
        }

        return result;
    }

    private RouteBundle queueRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                            String messagingServiceId, String scanType) {
        return createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                RabbitMqRouteId.RABBIT_MQ_QUEUE.label, true);
    }
}
