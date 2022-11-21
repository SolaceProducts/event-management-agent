package com.solace.maas.ep.event.management.agent.scanManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class MetaInfFileDetailsBO {

    String fileName;

    String dataEntityType;
}
