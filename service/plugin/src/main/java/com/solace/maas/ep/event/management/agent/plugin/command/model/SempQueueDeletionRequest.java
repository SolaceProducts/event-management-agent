package com.solace.maas.ep.event.management.agent.plugin.command.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SempQueueDeletionRequest {
    private String msgVpn;
    private String queueName;
}
