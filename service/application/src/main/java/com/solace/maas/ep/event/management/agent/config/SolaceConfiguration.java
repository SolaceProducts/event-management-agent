package com.solace.maas.ep.event.management.agent.config;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.messagingServices.RtoMessagingService;
import com.solace.maas.ep.event.management.agent.plugin.config.EnableRtoCondition;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.subscriber.SolaceSubscriber;
import com.solace.messaging.MessagingService;
import com.solace.messaging.config.RetryStrategy;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Configuration
@Profile("!TEST")
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class SolaceConfiguration {
    private static final String TOPIC_PREFIX_FORMAT = "sc/ep/runtime/%s/%s/";
    private final Properties vmrConfiguration;
    private final List<String> sessionConfiguration;
    private final EventPortalProperties eventPortalProperties;
    private String topicPrefix;

    @Autowired
    @SuppressWarnings("PMD.LooseCoupling")
    public SolaceConfiguration(Properties vmrConfig, ArrayList<String> sessionConfig,
                               EventPortalProperties eventPortalProperties) {
        vmrConfiguration = vmrConfig;
        sessionConfiguration = sessionConfig;
        this.eventPortalProperties = eventPortalProperties;
    }

    public String getTopicPrefix() {

        if (topicPrefix == null) {
            topicPrefix = String.format(TOPIC_PREFIX_FORMAT,
                    eventPortalProperties.getOrganizationId(),
                    eventPortalProperties.getRuntimeAgentId());
        }
        return topicPrefix;
    }

    @Bean
    @ConditionalOnMissingBean(EnableRtoCondition.class)
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
    public MessagingService messagingService() {
        String clientName = "runtimeAgent-" + eventPortalProperties.getRuntimeAgentId();
        vmrConfiguration.setProperty(SolaceProperties.ClientProperties.NAME, clientName);

        log.info("Connecting to event portal using EMA client {}.", clientName);
        return MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(vmrConfiguration)
                .withReconnectionRetryStrategy(RetryStrategy.foreverRetry(15_000))
                .build()
                .connect();
    }

    @Bean
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
    public RtoMessageBuilder webMessagingService() {
        return RtoMessagingService.createRtoMessagingServiceBuilder()
                .fromProperties(sessionConfiguration)
                .createContext()
                .createSession()
                .connect();
    }

    @Bean
    @ConditionalOnMissingBean(EnableRtoCondition.class)
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
    public DirectMessagePublisher directMessagePublisher() {
        return messagingService().createDirectMessagePublisherBuilder()
                .build()
                .start();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnMissingBean(EnableRtoCondition.class)
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
    public OutboundMessageBuilder outboundMessageBuilder() {
        return messagingService().messageBuilder();
    }

    @Bean
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
    public SolaceSubscriber solaceSubscriber() {
        return new SolaceSubscriber(messagingService());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
    public SolacePublisher solacePublisher() {
        return new SolacePublisher(outboundMessageBuilder(),
                directMessagePublisher());
    }

}
