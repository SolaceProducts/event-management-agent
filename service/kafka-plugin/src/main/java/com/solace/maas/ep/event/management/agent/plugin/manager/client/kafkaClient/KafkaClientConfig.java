package com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Builder
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "kafka.client.config")
public class KafkaClientConfig {
    KafkaClientConnection connections;
    KafkaClientReconnection reconnections;
}
