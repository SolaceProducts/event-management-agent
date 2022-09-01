package com.solace.maas.ep.runtime.agent.plugin.mop;

public enum MOPProtocol {
    discovery(100),
    agentAssociation(200),
    topology(300),
    topologyUpdateEvent(400),
    topologySyncEvent(401),
    accounting(450),
    topologyOperation(500),
    template(600),
    service(700),
    node(800),
    userInvitation(900),
    notification(950),
    signupRequestValidation(1000),
    asyncAPI(1100),
    user(1200),
    organization(1201),
    vmrImage(1300),
    event(1400),
    dns(1500),
    featureDescriptor(1600),
    servicePackage(1700),
    servicePackageUpdate(1701),
    restProxy(1800),
    eventMgmtSync(1900),
    eventMgmtSyncV2(1901),
    configRefresh(2000),
    auditing(2100),
    monitoring(2200),
    syslog(2300),
    auditMarketoUpdate(2400),
    keyManagement(2500),
    organizationServices(2600),
    organizationStats(2700),
    eventMesh(2800),
    eventMeshOperation(2801),
    eventMeshInfo(3600),
    trialExpiry(2900),
    semp(3000),
    monitoringConfigs(3100),
    cloudAgentControl(3200),
    eventPortalJob(3300),
    eventMgmtRrbacMigration(3400),
    organizationLimits(3500),
    oauthProfile(3600);

    private final int id;

    MOPProtocol(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
