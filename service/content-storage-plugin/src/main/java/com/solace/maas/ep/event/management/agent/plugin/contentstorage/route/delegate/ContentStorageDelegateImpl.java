package com.solace.maas.ep.event.management.agent.plugin.contentstorage.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class ContentStorageDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public ContentStorageDelegateImpl() {
        super("CONTENT_STORAGE");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        return List.of(createRouteBundle(destinations, recipients, scanType, messagingServiceId,
                "direct:contentStorageFileWrite", false));
    }
}
