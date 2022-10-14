package com.solace.maas.ep.event.management.agent.plugin.kafka.processor.event.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessagingServiceEvent {
    private String name;
    private String bootstrapServer;
}
