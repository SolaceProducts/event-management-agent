package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfilePublishTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAuthorizationGroup;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsername;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSession;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnQueue;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnQueueSubscription;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPoint;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBinding;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingRequestHeader;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumer;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName;

public enum SolaceSEMPv2RouteId {
    MsgVpnAclProfile(MsgVpnAclProfile.class),
    MsgVpnAclProfileClientConnectException(MsgVpnAclProfileClientConnectException.class),
    MsgVpnAclProfileSubscribeTopicException(MsgVpnAclProfileSubscribeTopicException.class),
    MsgVpnAclProfilePublishTopicException(MsgVpnAclProfilePublishTopicException.class),
    MsgVpnClientUsername(MsgVpnClientUsername.class),
    MsgVpnAuthorizationGroup(MsgVpnAuthorizationGroup.class),
    MsgVpnClientProfile(MsgVpnClientProfile.class),
    MsgVpnMqttSession(MsgVpnMqttSession.class),
    MsgVpnQueue(MsgVpnQueue.class),
    MsgVpnQueueSubscription(MsgVpnQueueSubscription.class),
    MsgVpnRestDeliveryPoint(MsgVpnRestDeliveryPoint.class),
    MsgVpnRestDeliveryPointRestConsumer(MsgVpnRestDeliveryPointRestConsumer.class),
    MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName.class),
    MsgVpnRestDeliveryPointQueueBinding(MsgVpnRestDeliveryPointQueueBinding.class),
    MsgVpnRestDeliveryPointQueueBindingRequestHeader(MsgVpnRestDeliveryPointQueueBindingRequestHeader.class);

    public final String label;

    SolaceSEMPv2RouteId(Class c) {
        this.label = RouteNameUtil.getRouteName(c);
    }
}
