package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration.ConfluentSchemaRegistryRouteId;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration.ConfluentSchemaRegistryRouteType;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.route.enumeration.ConfluentSchemaRegistryScanType;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component("confluentSchemaRegistryRouteDelegateImpl")
@Slf4j // todo remove this @Slf4j
public class ConfluentSchemaRegistryRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public ConfluentSchemaRegistryRouteDelegateImpl() {
        super("CONFLUENT_SCHEMA_REGISTRY");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String confluentSchemaRegistryId) {
        List<RouteBundle> result = new ArrayList<>();

        ConfluentSchemaRegistryScanType confluentSchemaRegistryScanType = null;
        try {
            confluentSchemaRegistryScanType = ConfluentSchemaRegistryScanType.valueOf(scanType);
        } catch (Exception e) {
            log.info("moodi xxx1 : e {}", e);
            return List.of();
        }


        switch (confluentSchemaRegistryScanType) {
            case CONFLUENT_SCHEMA_REGISTRY_SCHEMA:
                result.add(createRouteBundle(destinations, recipients,
                        ConfluentSchemaRegistryRouteType.CONFLUENT_SCHEMA_REGISTRY_SCHEMA.label, confluentSchemaRegistryId,
                        ConfluentSchemaRegistryRouteId.CONFLUENT_SCHEMA_REGISTRY_SCHEMA.label, true));
                break;
        }

        return result;
    }
}
