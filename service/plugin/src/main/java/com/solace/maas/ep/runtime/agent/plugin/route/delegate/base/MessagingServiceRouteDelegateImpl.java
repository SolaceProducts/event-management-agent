package com.solace.maas.ep.runtime.agent.plugin.route.delegate.base;

import com.solace.maas.ep.runtime.agent.plugin.manager.loader.PluginLoader;
import com.solace.maas.ep.runtime.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;

import java.util.List;

public abstract class MessagingServiceRouteDelegateImpl implements MessagingServiceRouteDelegate {
    public MessagingServiceRouteDelegateImpl(String pluginId) {
        register(pluginId);
    }

    @Override
    public RouteBundle createRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                         String scanType, String messagingServiceId, String routeId, Boolean firstRoute) {
        return RouteBundle.builder()
                .destinations(destinations)
                .routeId(routeId)
                .scanType(scanType)
                .recipients(recipients)
                .messagingServiceId(messagingServiceId)
                .firstRouteInChain(firstRoute)
                .build();
    }

    @Override
    public void register(String id) {
        PluginLoader.addPlugin(id, this);
    }
}
