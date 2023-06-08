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
        super("SOLACE_CONFIG");
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
                break;
            }
            case MsgVpnClientProfile:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnClientProfile, SolaceSEMPv2RouteId.MsgVpnClientProfile));
                break;
            }
            case MsgVpnMqttSession:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnMqttSession, SolaceSEMPv2RouteId.MsgVpnMqttSession));
                break;
            }
            case MsgVpnAuthorizationGroup:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnAuthorizationGroup, SolaceSEMPv2RouteId.MsgVpnAuthorizationGroup));
                break;
            }
            case MsgVpnQueue:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnQueue, SolaceSEMPv2RouteId.MsgVpnQueue));
                break;
            }
            case MsgVpnQueueSubscription:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnQueueSubscription, SolaceSEMPv2RouteId.MsgVpnQueueSubscription));
                break;
            }
            case MsgVpnRestDeliveryPoint:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnRestDeliveryPoint, SolaceSEMPv2RouteId.MsgVpnRestDeliveryPoint));
                break;
            }
            case MsgVpnRestDeliveryPointRestConsumer:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnRestDeliveryPointRestConsumer, SolaceSEMPv2RouteId.MsgVpnRestDeliveryPointRestConsumer));
                break;
            }
            case MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName, SolaceSEMPv2RouteId.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName));
                break;
            }
            case MsgVpnRestDeliveryPointQueueBinding:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnRestDeliveryPointQueueBinding, SolaceSEMPv2RouteId.MsgVpnRestDeliveryPointQueueBinding));
                break;
            }
            case MsgVpnRestDeliveryPointQueueBindingRequestHeader:{
                result.add(commandProcessingRouteBundle(destinations, recipients, messagingServiceId, SolaceSEMPv2RouteType.MsgVpnRestDeliveryPointQueueBindingRequestHeader, SolaceSEMPv2RouteId.MsgVpnRestDeliveryPointQueueBindingRequestHeader));
                break;
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
