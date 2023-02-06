package com.solace.maas.ep.event.management.agent.plugin.route;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;


@Data
public class RouteBundleRecipientsStore {
    private static int storeKey = 0;
    private Map<String, String> store;

    public RouteBundleRecipientsStore() {
        store = new LinkedHashMap<>();
    }

    public static int getStoreKey() {
        return storeKey++;
    }
}
