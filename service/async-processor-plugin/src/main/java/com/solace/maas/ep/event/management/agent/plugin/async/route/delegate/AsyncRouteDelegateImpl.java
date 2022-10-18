package com.solace.maas.ep.event.management.agent.plugin.async.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("AsyncRouteDelegateImpl")
public class AsyncRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public AsyncRouteDelegateImpl() {
        super("ASYNC_PLUGIN");
    }

    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        return List.of(
                RouteBundle.builder()
                        .messagingServiceId(messagingServiceId)
                        .firstRouteInChain(true)
                        .routeId("asyncSubscriber")
                        .recipients(recipients)
                        .destinations(destinations)
                        .scanType(scanType)
                        .build()
        );
    }
}
