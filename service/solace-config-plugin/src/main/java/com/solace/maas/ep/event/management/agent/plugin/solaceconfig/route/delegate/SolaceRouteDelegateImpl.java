package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteId;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteType;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2ScanType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component
public class SolaceRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {
    public SolaceRouteDelegateImpl() {
        super("SOLACE");
    }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               String scanType, String messagingServiceId) {
        List<RouteBundle> result = new ArrayList<>();

        final SolaceSEMPv2ScanType solaceScanType = SolaceSEMPv2ScanType.valueOf(scanType);

        switch (solaceScanType) {
            case SOLACE_SEMPv2_COMMAND:
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId));
                break;
        }

        return result;
    }

    private RouteBundle commandProcessingRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                     String messagingServiceId) {
        return createRouteBundle(destinations, recipients, SolaceSEMPv2RouteType.SOLACE_SEMPv2_COMMAND.label,
                messagingServiceId, SolaceSEMPv2RouteId.SOLACE_SEMPv2_COMMAND.label, true);
    }

}
