package com.solace.maas.ep.event.management.agent.util.config.idgenerator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "id-generator")
@Profile("!TEST")
public class IDGeneratorProperties {
    private String originId;
}
