package com.solace.maas.ep.runtime.agent;

import com.solace.maas.ep.runtime.agent.config.plugin.enumeration.MessagingServiceType;
import com.solace.maas.ep.runtime.agent.messagingServices.RtoMessagingService;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolaceWebPublisher;
import com.solace.maas.ep.runtime.agent.publisher.ScanDataPublisher;
import com.solace.maas.ep.runtime.agent.publisher.ScanLogsPublisher;
import com.solace.maas.ep.runtime.agent.repository.messagingservice.MessagingServiceRepository;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.ConnectionDetailsEntity;
import com.solace.maas.ep.runtime.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.runtime.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.runtime.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.maas.ep.runtime.agent.plugin.vmr.VmrProcessor;
import com.solace.messaging.MessagingService;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.resources.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@TestConfiguration
@Profile("TEST")
public class TestConfig {

    @Autowired
    private CamelContext camelContext;

    @Bean
    @Primary
    public VMRProperties vmrProperties(EventPortalPluginProperties eventPortalPluginProperties) {
        return new VMRProperties(eventPortalPluginProperties);
    }

    @Bean
    @Primary
    public Properties vmrConfig(VMRProperties vmrProperties) {
        return new Properties();
    }

    @Bean(name = "vmrTopic")
    @Primary
    public Topic solaceTopic() {
        Topic topic = mock(Topic.class);
        when(topic.getName()).thenReturn("VmrTest");
        return topic;
    }

    @Bean
    @Primary
    public VmrProcessor getVmrProcessor() {
        VmrProcessor processor = spy(mock(VmrProcessor.class));
        Topic topic = mock(Topic.class);
        when(topic.getName()).thenReturn("test");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody("test exchange");

        return processor;
    }

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
    public OutboundMessageBuilder outboundMessageBuilder() {
        return mock(OutboundMessageBuilder.class);
    }

    @Bean
    @Primary
    public RtoMessagingService rtoMessagingService() {
        return mock(RtoMessagingService.class);
    }

    @Bean
    @Primary
    public RtoMessageBuilder webMessagingService() {
        return mock(RtoMessageBuilder.class);
    }

    @Bean
    @Primary
    public MessagingServiceRepository getRepository() {
        MessagingServiceRepository repository = mock(MessagingServiceRepository.class);
        ConnectionDetailsEntity connectionDetailsEntity = ConnectionDetailsEntity.builder()
                .id(UUID.randomUUID().toString())
                .connectionUrl("localhost:9090")
                .build();

        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(MessagingServiceEntity.builder()
                        .messagingServiceType(MessagingServiceType.SOLACE.name())
                        .name("service1")
                        .id(UUID.randomUUID().toString())
                        .managementDetails(List.of(connectionDetailsEntity))
                        .build()));
        return repository;
    }
}
