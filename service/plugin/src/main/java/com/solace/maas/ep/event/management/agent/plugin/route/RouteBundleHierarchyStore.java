package com.solace.maas.ep.event.management.agent.plugin.route;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Data
public class RouteBundleHierarchyStore {
    private static final AtomicInteger STORE_KEY = new AtomicInteger();
    private Map<String, String> store;

    public RouteBundleHierarchyStore() {
        store = new LinkedHashMap<>();
    }

    public static int getStoreKey() {
        return STORE_KEY.getAndIncrement();
    }
}
