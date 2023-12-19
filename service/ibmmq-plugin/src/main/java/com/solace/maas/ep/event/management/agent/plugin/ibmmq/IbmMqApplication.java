package com.solace.maas.ep.event.management.agent.plugin.ibmmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IbmMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(IbmMqApplication.class, args);
    }
}
