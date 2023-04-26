package com.solace.maas.ep.event.management.agent.plugin.contentstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class ContentStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentStorageApplication.class, args);
    }
}
