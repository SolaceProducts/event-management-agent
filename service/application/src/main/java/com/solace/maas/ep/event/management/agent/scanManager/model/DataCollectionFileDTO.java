package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataCollectionFileDTO {
    private String id;

    private String path;

    private boolean purged;
}
