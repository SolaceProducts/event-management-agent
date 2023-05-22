package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfilePublishTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsername;


public enum SolaceSEMPv2RouteType {
    MsgVpnAclProfile(MsgVpnAclProfile.class),
    MsgVpnAclProfileClientConnectException(MsgVpnAclProfileClientConnectException.class),
    MsgVpnAclProfileSubscribeTopicException(MsgVpnAclProfileSubscribeTopicException.class),
    MsgVpnAclProfilePublishTopicException(MsgVpnAclProfilePublishTopicException.class),
    MsgVpnClientUsername(MsgVpnClientUsername.class);


    public final String label;

    SolaceSEMPv2RouteType(Class c) {
        this.label = RouteNameUtil.getRouteName(c);
    }
}
