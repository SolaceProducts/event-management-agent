package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;

import java.util.List;

public interface MessagingServiceRouteDelegate {
    List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                        String scanType, String messagingServiceId);

    RouteBundle createRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                  String scanType, String messagingServiceId, String routeId, Boolean firstRoute);

    void register(String id);
}
