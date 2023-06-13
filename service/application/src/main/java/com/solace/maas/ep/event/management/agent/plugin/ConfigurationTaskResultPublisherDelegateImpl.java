package com.solace.maas.ep.event.management.agent.plugin;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigurationTaskResultPublisherDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public ConfigurationTaskResultPublisherDelegateImpl() {
        super("CONFIGURATION_TASK_RESULT_PUBLISHER");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        return List.of(createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                "direct:configurationTaskResultPublisher", false));
    }
}
