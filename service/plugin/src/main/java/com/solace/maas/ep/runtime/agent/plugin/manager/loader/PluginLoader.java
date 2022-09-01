package com.solace.maas.ep.runtime.agent.plugin.manager.loader;

import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;

import java.util.HashMap;
import java.util.Map;

public class PluginLoader {
    private final static Map<String, MessagingServiceRouteDelegate> scanDelegates = new HashMap<>();

    public static void addPlugin(String id, MessagingServiceRouteDelegate scanDelegate) {
        scanDelegates.put(id, scanDelegate);
    }

    public static MessagingServiceRouteDelegate findPlugin(String id) {
        return scanDelegates.get(id);
    }
}
