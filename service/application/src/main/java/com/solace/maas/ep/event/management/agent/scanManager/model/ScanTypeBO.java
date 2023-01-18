package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ScanTypeBO implements Serializable {
    private String name;

    private String status;
}
