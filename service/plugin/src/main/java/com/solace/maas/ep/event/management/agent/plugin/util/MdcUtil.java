package com.solace.maas.ep.event.management.agent.plugin.util;

import org.apache.commons.lang3.ObjectUtils;

public class MdcUtil {
    public static Boolean isLinked(String orgId, String originOrgId) {
        if (ObjectUtils.isEmpty(orgId) || ObjectUtils.isEmpty(originOrgId)) {
            return Boolean.FALSE;
        }
        return !originOrgId.equals(orgId);
    }
}
