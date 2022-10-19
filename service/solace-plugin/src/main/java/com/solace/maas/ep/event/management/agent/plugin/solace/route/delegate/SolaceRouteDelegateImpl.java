package com.solace.maas.ep.event.management.agent.plugin.solace.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteId;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceRouteType;
import com.solace.maas.ep.event.management.agent.plugin.solace.route.enumeration.SolaceScanType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SolaceRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public SolaceRouteDelegateImpl() {
        super("SOLACE");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        List<RouteBundle> result = new ArrayList<>();

        final SolaceScanType solaceScanType = SolaceScanType.valueOf(scanType);

        switch (solaceScanType) {
            case SOLACE_QUEUE_LISTING:
                result.add(queueListingRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case SOLACE_QUEUE_CONFIG:
                result.add(queueConfigRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case SOLACE_SUBSCRIPTION_CONFIG:
                result.add(subscriptionConfigRouteBundle(destinations, recipients, messagingServiceId));

                break;
            case SOLACE_ALL:
                result.add(subscriptionConfigRouteBundle(destinations, recipients, messagingServiceId));
                result.add(queueConfigRouteBundle(destinations, recipients, messagingServiceId));

                break;
        }

        return result;
    }

    private RouteBundle subscriptionConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                      String messagingServiceId) {
        RouteBundle solaceSubscription = createRouteBundle(destinations, recipients,
                SolaceRouteType.SOLACE_SUBSCRIPTION_CONFIG.label, messagingServiceId,
                SolaceRouteId.SOLACE_SUBSCRIPTION_CONFIG.label, false);
        return queueListingRouteBundle(destinations, List.of(solaceSubscription), messagingServiceId);
    }

    private RouteBundle queueListingRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                String messagingServiceId) {
        return createRouteBundle(destinations, recipients, SolaceRouteType.SOLACE_QUEUE_LISTING.label,
                messagingServiceId, SolaceRouteId.SOLACE_QUEUE_LISTING.label, true);
    }

    private RouteBundle queueConfigRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String messagingServiceId) {
        return createRouteBundle(destinations, recipients, SolaceRouteType.SOLACE_QUEUE_CONFIG.label, messagingServiceId,
                SolaceRouteId.SOLACE_QUEUE_CONFIG.label, true);
    }
}
