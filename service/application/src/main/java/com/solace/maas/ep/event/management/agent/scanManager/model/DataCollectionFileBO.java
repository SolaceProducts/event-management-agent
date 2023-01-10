package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DataCollectionFileBO implements Serializable {
    private String id;

    private String path;

    private boolean purged;
}
