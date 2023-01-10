package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ScanItemDTO {
    private String id;
    private String messagingServiceId;
    private String messagingServiceName;
    private String messagingServiceType;
    private String emaId;
    private List<ScanTypeDTO> scanTypes;
    private Instant createdAt;
}
