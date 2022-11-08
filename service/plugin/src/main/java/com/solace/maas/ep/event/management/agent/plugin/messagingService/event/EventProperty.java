package com.solace.maas.ep.event.management.agent.plugin.messagingService.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventProperty {
    private String id;
    private String name;
    private String value;
}
