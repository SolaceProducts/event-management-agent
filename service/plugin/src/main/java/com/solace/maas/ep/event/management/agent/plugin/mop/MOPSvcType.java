package com.solace.maas.ep.event.management.agent.plugin.mop;

public enum MOPSvcType {
    maasGateway("maas-gateway"),
    maasCore("maas-core"),
    maasCloudAgent("maas-cloud-agent"),
    maasAccountingService("maas-accounting"),
    maasSearchService("maas-search"),
    maasOpsSearchService("maas-ops-search"),
    maasSolutionConfig("maas-solution-config"),
    maasBilling("maas-billing"),
    maasOpsAgent("maas-ops-agent"),
    maasDNS("maas-dns-agent"),
    maasEventMgmt("maas-event-mgmt"),
    maasEventCore("maas-event-core"),
    maasEventMesh("maas-event-mesh"),
    maasEpRuntime("maas-ep-runtime"),
    maasEventDesigner("maas-event-designer"),
    maasEventDiscoveryAgent("maas-event-discovery-agent"),
    maasMonitoring("maas-monitoring"),
    maasAuditing("maas-auditing"),
    maasPubsubAgent("maas-pubsub-agent"),
    maasEpApiProducts("maas-ep-api-products"),
    maasQueue("maas-queue"),
    maasEpCore("maas-ep-core");

    private final String id;

    MOPSvcType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static MOPSvcType getMOPSvcTypeById(String id) {
        for (MOPSvcType e : values()) {
            if (e.id.equals(id)) {
                return e;
            }
        }
        return null;
    }
}


