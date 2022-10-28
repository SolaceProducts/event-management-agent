package com.solace.maas.ep.event.management.agent.plugin.messagingService.event;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.MessagingServicePropertyIf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventProperty implements MessagingServicePropertyIf {
    private String id;
    private String name;
    private String property;
}
