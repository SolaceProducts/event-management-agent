package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class MetaInfFileBO implements Serializable {
    private String scanId;

    private List<String> files;

    private String messagingServiceId;

    private String scheduleId;
}
