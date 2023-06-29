package com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class KafkaClientConnection {
    private KafkaClientConnectionConfig timeout;
    private KafkaClientConnectionConfig maxIdle;
    private KafkaClientConnectionConfig requestTimeout;
}
