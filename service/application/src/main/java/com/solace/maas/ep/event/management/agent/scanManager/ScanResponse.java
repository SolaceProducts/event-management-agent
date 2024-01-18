package com.solace.maas.ep.event.management.agent.scanManager;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScanResponse {
    private String id;
    private List<String> scanTypes;
}
