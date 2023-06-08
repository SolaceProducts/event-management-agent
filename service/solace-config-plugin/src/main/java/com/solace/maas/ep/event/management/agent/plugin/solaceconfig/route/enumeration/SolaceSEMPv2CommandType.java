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


public enum SolaceSEMPv2CommandType {
    MsgVpnAclProfile(MsgVpnAclProfile.class.getSimpleName()),
    MsgVpnAclProfileClientConnectException(MsgVpnAclProfileClientConnectException.class.getSimpleName()),
    MsgVpnAclProfileSubscribeTopicException(MsgVpnAclProfileSubscribeTopicException.class.getSimpleName()),
    MsgVpnAclProfilePublishTopicException(MsgVpnAclProfilePublishTopicException.class.getSimpleName()),
    MsgVpnClientUsername(MsgVpnClientUsername.class.getSimpleName()),
    MsgVpnAuthorizationGroup(MsgVpnAuthorizationGroup.class.getSimpleName()),
    MsgVpnClientProfile(MsgVpnClientProfile.class.getSimpleName()),
    MsgVpnMqttSession(MsgVpnMqttSession.class.getSimpleName()),
    MsgVpnQueue(MsgVpnQueue.class.getSimpleName()),
    MsgVpnQueueSubscription(MsgVpnQueueSubscription.class.getSimpleName()),
    MsgVpnRestDeliveryPoint(MsgVpnRestDeliveryPoint.class.getSimpleName()),
    MsgVpnRestDeliveryPointRestConsumer(MsgVpnRestDeliveryPointRestConsumer.class.getSimpleName()),
    MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName.class.getSimpleName()),
    MsgVpnRestDeliveryPointQueueBinding(MsgVpnRestDeliveryPointQueueBinding.class.getSimpleName()),
    MsgVpnRestDeliveryPointQueueBindingRequestHeader(MsgVpnRestDeliveryPointQueueBindingRequestHeader.class.getSimpleName());
    public final String label;

    SolaceSEMPv2CommandType(String label) {
        this.label = label;
    }
}
