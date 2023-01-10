package com.solace.maas.ep.event.management.agent.scanManager.model;

import com.solace.maas.ep.event.management.agent.model.AbstractBaseBO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ScanItemBO extends AbstractBaseBO<String> {
    private String id;
    private String messagingServiceId;
    private String messagingServiceName;
    private String messagingServiceType;
    private String emaId;
    private List<ScanTypeBO> scanTypes;
    private Instant createdAt;
}
