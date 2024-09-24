package com.solace.maas.ep.event.management.agent.applicationContextTests;

import com.solace.maas.ep.event.management.agent.plugin.config.VMRProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "eventPortal.gateway.messaging.standalone=false",
        "eventPortal.managed=true",
        "eventPortal.incomingRequestQueueName = ep_core_ema_requests_123456_123123",
        "event-portal.gateway.messaging.rto-session=false",
        "event-portal.runtimeAgentId = testManagedAgentId",
})
class ManagedAgentMessageHandlerBeansTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private VMRProperties vmrProperties;

    @Test
    void testPersistentMessageHandlerBeansAreLoaded() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        assertThat(
                Arrays.stream(allBeanNames)
                        .anyMatch(bean -> StringUtils.equalsIgnoreCase(bean, "SolacePersistentMessageHandler"))).isTrue();
    }

    @Test
    void testDirectMessageHandlerBeansAreNotLoaded() {
        Set<String> directMessageHandlerBeanNames =
                Set.of(
                        "commandMessageHandler",
                        "scanCommandMessageHandler",
                        "discoveryMessageHandler",
                        "startImportScanCommandMessageHandler",
                        "heartbeatMessageHandler"


                );
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        assertThat(
                Arrays.stream(allBeanNames)
                        .map(StringUtils::lowerCase)
                        .collect(Collectors.toSet()))
                .doesNotContainAnyElementsOf(
                        directMessageHandlerBeanNames.stream()
                                .map(StringUtils::lowerCase)
                                .collect(Collectors.toSet())
                );

    }

    @Test
    void testCommandLogStreamingProcessorBeanIsNotLoaded() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        assertThat(
                Arrays.stream(allBeanNames)
                        .map(StringUtils::lowerCase)
                        .collect(Collectors.toSet()))
                .doesNotContain(StringUtils.lowerCase("commandLogStreamingProcessor"));

    }

    @Test
    void testClientNameIsGeneratedBasedOnHostNameAndAgentId() throws UnknownHostException {
        String hostnameHash = DigestUtils.sha256Hex(InetAddress.getLocalHost().getHostName());
        assertThat(vmrProperties.getClientName()).isEqualTo("ema-testManagedAgentId-" + hostnameHash);

    }
}
