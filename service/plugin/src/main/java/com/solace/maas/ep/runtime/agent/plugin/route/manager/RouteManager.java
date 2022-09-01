package com.solace.maas.ep.runtime.agent.plugin.route.manager;

import java.util.List;

public interface RouteManager {
    List<String> getRecipientList(String scanId, String parentRoute);

    List<String> getDestinationList(String scanId, String parentRoute);

    void setupRoute(String routeId);
}
