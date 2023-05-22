package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.delegate.base.MessagingServiceRouteDelegateImpl;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2CommandType;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteId;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration.SolaceSEMPv2RouteType;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component
public class SolaceConfigRouteDelegateImpl extends MessagingServiceRouteDelegateImpl {

    public SolaceConfigRouteDelegateImpl() {
        super("SOLACE-CONFIG");
   }

    @Override
    public List<RouteBundle> generateRouteList(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                               final String command, String messagingServiceId) {
        List<RouteBundle> result = new ArrayList<>();

        final SolaceSEMPv2CommandType commandType = SolaceSEMPv2CommandType.valueOf(command);

        switch (commandType) {
            case MsgVpnAclProfile: {
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnAclProfile, SolaceSEMPv2RouteId.MsgVpnAclProfile));
                break;
            }
            case MsgVpnAclProfileClientConnectException: {
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnAclProfileClientConnectException, SolaceSEMPv2RouteId.MsgVpnAclProfileClientConnectException));
                break;
            }
            case MsgVpnAclProfilePublishTopicException:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnAclProfilePublishTopicException, SolaceSEMPv2RouteId.MsgVpnAclProfilePublishTopicException));
                break;
            }
            case MsgVpnAclProfileSubscribeTopicException: {
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnAclProfileSubscribeTopicException, SolaceSEMPv2RouteId.MsgVpnAclProfileSubscribeTopicException));
                break;
            }
            case MsgVpnClientUsername:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnClientUsername, SolaceSEMPv2RouteId.MsgVpnClientUsername));
            }
        }



        return result;
    }

    private RouteBundle commandProcessingRouteBundle(List<RouteBundle> destinations, List<RouteBundle> recipients,
                                                              String messagingServiceId, SolaceSEMPv2RouteType routeType, SolaceSEMPv2RouteId routeId) {
        return createRouteBundle(destinations, recipients, routeType.label,
                messagingServiceId, routeId.label, true);
    }

}
