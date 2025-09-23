package com.solace.maas.ep.event.management.agent.plugin.util;

public class MdcUtil {
    public static Boolean isLinked(String orgId, String originOrgId) {
        return !originOrgId.equals(orgId);
    }
}
