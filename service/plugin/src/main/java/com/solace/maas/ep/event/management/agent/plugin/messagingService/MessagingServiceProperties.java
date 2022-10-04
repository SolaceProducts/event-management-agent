package com.solace.maas.ep.event.management.agent.plugin.messagingService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagingServiceProperties {
    private String id;

    private String name;

    private MessagingServiceManagementProperties management;
}