package com.solace.maas.ep.event.management.agent;

import com.solace.maas.ep.event.management.agent.messagingServices.RtoMessagingService;
import com.solace.maas.ep.event.management.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.maas.ep.event.management.agent.plugin.vmr.VmrProcessor;
import com.solace.maas.ep.event.management.agent.testConfigs.MessagingServiceTestConfig;
import com.solace.maas.ep.event.management.agent.testConfigs.PublisherTestConfig;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import com.solace.maas.ep.event.management.agent.util.config.idgenerator.IDGeneratorProperties;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.resources.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Properties;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@TestConfiguration
@Profile("TEST")
@Import({PublisherTestConfig.class, MessagingServiceTestConfig.class})
public class TestConfig {

    @Autowired
    ProducerTemplate producerTemplate;
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
    public IDGeneratorProperties idGeneratorProperties() {
        IDGeneratorProperties idGeneratorProperties = mock(IDGeneratorProperties.class);
        when(idGeneratorProperties.getOriginId()).thenReturn("12345");
        return idGeneratorProperties;
    }

    @Bean
    @Primary
    public Random random() {
        return new Random();
    }

    @Bean
    @Primary
    public IDGenerator idGenerator() {
        IDGenerator idGenerator = new IDGenerator(idGeneratorProperties());
        idGenerator.setRandom(random());
        return idGenerator;
    }
}
