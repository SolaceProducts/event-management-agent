package com.solace.maas.ep.event.management.agent.plugin.route.manager;

import java.util.List;

public interface RouteManager {
    List<String> getRecipientList(String scanId, String parentRoute);

    List<String> getDestinationList(String scanId, String parentRoute);

    void setupRoute(String routeId);
}
