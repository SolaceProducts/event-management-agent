package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileClientConnectException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfilePublishTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnAclProfileSubscribeTopicException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnClientUsername;
import org.apache.commons.text.CaseUtils;

public enum SolaceSEMPv2CommandType {
    MsgVpnAclProfile(CaseUtils.toCamelCase(MsgVpnAclProfile.class.getSimpleName(), false)),
    MsgVpnAclProfileClientConnectException(CaseUtils.toCamelCase(MsgVpnAclProfileClientConnectException.class.getSimpleName(), false)),
    MsgVpnAclProfileSubscribeTopicException(CaseUtils.toCamelCase(MsgVpnAclProfileSubscribeTopicException.class.getSimpleName(), false)),
    MsgVpnAclProfilePublishTopicException(CaseUtils.toCamelCase(MsgVpnAclProfilePublishTopicException.class.getSimpleName(), false)),
    MsgVpnClientUsername(CaseUtils.toCamelCase(MsgVpnClientUsername.class.getSimpleName(), false));

    public final String label;

    SolaceSEMPv2CommandType(String label) {
        this.label = label;
    }
}
