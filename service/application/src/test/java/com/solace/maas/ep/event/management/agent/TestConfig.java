package com.solace.maas.ep.event.management.agent;

import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.command.SempDeleteCommandManager;
import com.solace.maas.ep.event.management.agent.command.SempGetCommandManager;
import com.solace.maas.ep.event.management.agent.command.SempPatchCommandManager;
import com.solace.maas.ep.event.management.agent.command.mapper.CommandMapper;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.messagingServices.RtoMessagingService;
import com.solace.maas.ep.event.management.agent.plugin.config.VMRProperties;
import com.solace.maas.ep.event.management.agent.plugin.config.eventPortal.EventPortalPluginProperties;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientConnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnection;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.kafkaClient.KafkaClientReconnectionConfig;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformLogProcessingService;
import com.solace.maas.ep.event.management.agent.plugin.terraform.manager.TerraformManager;
import com.solace.maas.ep.event.management.agent.plugin.vmr.VmrProcessor;
import com.solace.maas.ep.event.management.agent.processor.CommandLogStreamingProcessor;
import com.solace.maas.ep.event.management.agent.publisher.CommandLogsPublisher;
import com.solace.maas.ep.event.management.agent.publisher.CommandPublisher;
import com.solace.maas.ep.event.management.agent.testConfigs.MessagingServiceTestConfig;
import com.solace.maas.ep.event.management.agent.testConfigs.PublisherTestConfig;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import com.solace.maas.ep.event.management.agent.util.config.idgenerator.IDGeneratorProperties;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.resources.Topic;
import io.micrometer.core.instrument.MeterRegistry;
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

import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@Profile("TEST")
@Import({PublisherTestConfig.class, MessagingServiceTestConfig.class})
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class TestConfig {

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @Bean
    @Primary
    public SolaceConfiguration solaceConfiguration() {
        return mock(SolaceConfiguration.class);
    }


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
        VmrProcessor processor = mock(VmrProcessor.class);
        Topic topic = mock(Topic.class);
        when(topic.getName()).thenReturn("test");

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody("test exchange");

        return processor;
    }

    @Bean
    @Primary
    public MessagingServiceDelegateService getMessagingServiceDelegateService() {
        return mock(MessagingServiceDelegateService.class);
    }

    @Bean
    @Primary
    public TerraformManager getTerraformManager() {
        return mock(TerraformManager.class);
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

    @Bean
    @Primary
    public CommandPublisher getCommandPublisher() {
        return mock(CommandPublisher.class);
    }

    @Bean
    @Primary
    public CommandLogsPublisher getComaCommandLogsPublisher() {
        return mock(CommandLogsPublisher.class);
    }

    @Bean
    @Primary
    public CommandManager getCommandManager(TerraformManager terraformManager,
                                            CommandMapper commandMapper,
                                            CommandPublisher commandPublisher,
                                            MessagingServiceDelegateService messagingServiceDelegateService,
                                            EventPortalProperties eventPortalProperties,
                                            Optional<CommandLogStreamingProcessor> commandLogStreamingProcessor,
                                            MeterRegistry meterRegistry,
                                            SempDeleteCommandManager sempDeleteCommandManager,
                                            TerraformLogProcessingService terraformLogProcessingService,
                                            SempPatchCommandManager sempPatchCommandManager,
                                            SempGetCommandManager sempGetCommandManager) {
        return new CommandManager(
                terraformManager,
                commandMapper,
                commandPublisher,
                messagingServiceDelegateService,
                eventPortalProperties,
                commandLogStreamingProcessor,
                meterRegistry,
                sempDeleteCommandManager,
                terraformLogProcessingService,
                sempPatchCommandManager,
                sempGetCommandManager
        );
    }

    @Bean
    @Primary
    KafkaClientConnection kafkaClientConnection() {
        return mock(KafkaClientConnection.class);
    }

    @Bean
    @Primary
    KafkaClientReconnection kafkaClientReconnection() {
        return mock(KafkaClientReconnection.class);
    }

    @Bean
    @Primary
    public KafkaClientConfig kafkaClientConfig() {
        KafkaClientConfig kafkaClientConfig = mock(KafkaClientConfig.class);
        KafkaClientConnection kafkaClientConnection = mock(KafkaClientConnection.class);
        KafkaClientReconnection kafkaClientReconnection = mock(KafkaClientReconnection.class);

        KafkaClientConnectionConfig kafkaClientConnectionConfigTimeout = mock(KafkaClientConnectionConfig.class);
        KafkaClientConnectionConfig kafkaClientConnectionConfigMaxIdle = mock(KafkaClientConnectionConfig.class);
        KafkaClientConnectionConfig kafkaClientConnectionConfigRequestTimeout = mock(KafkaClientConnectionConfig.class);

        KafkaClientReconnectionConfig kafkaClientReconnectionConfigBackoff = mock(KafkaClientReconnectionConfig.class);
        KafkaClientReconnectionConfig kafkaClientReconnectionConfigBackoffMax = mock(KafkaClientReconnectionConfig.class);

        when(kafkaClientConfig.getConnections()).thenReturn(kafkaClientConnection);
        when(kafkaClientConfig.getReconnections()).thenReturn(kafkaClientReconnection);

        when(kafkaClientConnection.getTimeout()).thenReturn(kafkaClientConnectionConfigTimeout);
        when(kafkaClientConnectionConfigTimeout.getValue()).thenReturn(60_000);
        when(kafkaClientConnectionConfigTimeout.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientConnection.getMaxIdle()).thenReturn(kafkaClientConnectionConfigMaxIdle);
        when(kafkaClientConnectionConfigMaxIdle.getValue()).thenReturn(10_000);
        when(kafkaClientConnectionConfigMaxIdle.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientConnection.getRequestTimeout()).thenReturn(kafkaClientConnectionConfigRequestTimeout);
        when(kafkaClientConnectionConfigRequestTimeout.getValue()).thenReturn(5_000);
        when(kafkaClientConnectionConfigRequestTimeout.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientReconnection.getBackoff()).thenReturn(kafkaClientReconnectionConfigBackoff);
        when(kafkaClientReconnectionConfigBackoff.getValue()).thenReturn(50);
        when(kafkaClientReconnectionConfigBackoff.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        when(kafkaClientReconnection.getMaxBackoff()).thenReturn(kafkaClientReconnectionConfigBackoffMax);
        when(kafkaClientReconnectionConfigBackoffMax.getValue()).thenReturn(1000);
        when(kafkaClientReconnectionConfigBackoffMax.getUnit()).thenReturn(TimeUnit.MILLISECONDS);

        return kafkaClientConfig;
    }
}
