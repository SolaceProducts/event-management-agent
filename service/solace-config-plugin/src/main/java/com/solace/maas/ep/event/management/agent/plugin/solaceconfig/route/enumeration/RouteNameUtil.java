package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.route.enumeration;

public class RouteNameUtil {
    public static String getRouteName(Class c){
        return String.format("solaceConfig%s", c.getSimpleName());
    }
}
