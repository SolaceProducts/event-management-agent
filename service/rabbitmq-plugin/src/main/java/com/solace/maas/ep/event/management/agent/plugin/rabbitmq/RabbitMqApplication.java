package com.solace.maas.ep.event.management.agent.plugin.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args);
    }
}
