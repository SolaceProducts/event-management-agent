package com.solace.maas.ep.event.management.agent.testConfigs;

import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolaceWebPublisher;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataPublisher;
import com.solace.maas.ep.event.management.agent.publisher.ScanLogsPublisher;
import com.solace.maas.ep.event.management.agent.publisher.ScanStatusPublisher;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("TEST")
@Slf4j
public class PublisherTestConfig {
    @Bean
    @Primary
    public MessagingService messagingService() {
        return mock(MessagingService.class);
    }

    @Bean
    @Primary
    public SolacePublisher solacePublisher() {
        return mock(SolacePublisher.class);
    }

    @Bean
    @Primary
    public SolaceWebPublisher solaceWebPublisher() {
        return mock(SolaceWebPublisher.class);
    }

    @Bean
    @Primary
    public ScanDataPublisher scanDataPublisher() {
        return mock(ScanDataPublisher.class);
    }

    @Bean
    @Primary
    public DirectMessagePublisher directMessagePublisher() {
        return mock(DirectMessagePublisher.class);
    }

    @Bean
    @Primary
    public ScanLogsPublisher scanLogsPublisher() {
        return mock(ScanLogsPublisher.class);
    }

    @Bean
    @Primary
    public ScanStatusPublisher scanStatusPublisher() {
        return mock(ScanStatusPublisher.class);
    }
}
