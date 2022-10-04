package com.solace.maas.ep.event.management.agent.plugin.localstorage.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataCollectionFileWriterDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public DataCollectionFileWriterDelegateImpl() {
        super("FILE_WRITER");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        return List.of(createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                "seda:dataCollectionFileWrite", false));
    }
}
