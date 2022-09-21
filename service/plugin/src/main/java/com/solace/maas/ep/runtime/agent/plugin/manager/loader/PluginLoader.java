package com.solace.maas.ep.runtime.agent.plugin.manager.loader;

import com.solace.maas.ep.runtime.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PluginLoader {
    private final static Map<String, MessagingServiceRouteDelegate> scanDelegates = new LinkedCaseInsensitiveMap<>();

    public static void addPlugin(String id, MessagingServiceRouteDelegate scanDelegate) {
        scanDelegates.put(id, scanDelegate);
    }

    public static MessagingServiceRouteDelegate findPlugin(String id) {
        return scanDelegates.get(id);
    }

    public static List<String> getKeys() {
        return scanDelegates.keySet()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
