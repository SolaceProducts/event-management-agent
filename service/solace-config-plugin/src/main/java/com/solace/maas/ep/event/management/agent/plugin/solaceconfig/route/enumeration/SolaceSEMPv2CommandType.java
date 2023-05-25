package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfilePublishTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsername;
import org.apache.commons.text.CaseUtils;

public enum SolaceSEMPv2CommandType {
    MsgVpnAclProfile(MsgVpnAclProfile.class.getSimpleName()),
    MsgVpnAclProfileClientConnectException(MsgVpnAclProfileClientConnectException.class.getSimpleName()),
    MsgVpnAclProfileSubscribeTopicException(MsgVpnAclProfileSubscribeTopicException.class.getSimpleName()),
    MsgVpnAclProfilePublishTopicException(MsgVpnAclProfilePublishTopicException.class.getSimpleName()),
    MsgVpnClientUsername(MsgVpnClientUsername.class.getSimpleName());

    public final String label;

    SolaceSEMPv2CommandType(String label) {
        this.label = label;
    }
}
