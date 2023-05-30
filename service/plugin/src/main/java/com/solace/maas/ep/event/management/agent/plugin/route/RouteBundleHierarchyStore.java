package com.solace.maas.ep.event.management.agent.plugin.route;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class RouteBundleHierarchyStore {
    private static int storeKey;
    private Map<String, String> store;

    public RouteBundleHierarchyStore() {
        store = new LinkedHashMap<>();
    }

    public static int getStoreKey() {
        return storeKey++;
    }
}
