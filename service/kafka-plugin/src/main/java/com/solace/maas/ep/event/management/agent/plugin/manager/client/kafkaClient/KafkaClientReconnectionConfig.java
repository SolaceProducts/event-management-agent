package com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaClientReconnectionConfig {
    private int value;
    private TimeUnit unit;
}
