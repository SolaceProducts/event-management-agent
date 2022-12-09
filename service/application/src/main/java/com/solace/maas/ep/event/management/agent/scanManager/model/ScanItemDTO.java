package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ScanItemDTO {
    private String id;
    private String messagingServiceId;
    private String messagingServiceName;
    private String messagingServiceType;
    private Instant createdAt;
}
