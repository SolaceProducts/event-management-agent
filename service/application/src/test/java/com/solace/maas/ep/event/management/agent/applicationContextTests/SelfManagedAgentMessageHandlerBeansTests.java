package com.solace.maas.ep.event.management.agent.applicationContextTests;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "eventPortal.gateway.messaging.standalone=false",
        "eventPortal.managed=false"
})
class SelfManagedAgentMessageHandlerBeansTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testPersistentMessageHandlerBeansAreNotLoaded() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        assertThat(
                Arrays.stream(allBeanNames)
                        .noneMatch(bean -> StringUtils.equalsIgnoreCase(bean, "SolacePersistentMessageHandler"))).isTrue();
    }

    @Test
    void testDirectMessageHandlerBeansAreLoaded() {
        Set<String> directMessageHandlerBeanNames =
                Set.of(
                        "commandMessageHandler",
                        "scanCommandMessageHandler",
                        "discoveryMessageHandler",
                        "startImportScanCommandMessageHandler"

                );
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        assertThat(
                Arrays.stream(allBeanNames)
                        .map(StringUtils::lowerCase)
                        .collect(Collectors.toSet()))
                .containsOnlyOnceElementsOf(
                        directMessageHandlerBeanNames.stream()
                                .map(StringUtils::lowerCase)
                                .collect(Collectors.toSet())
                );

    }

    @Test
    void testCommandLogStreamingProcessorBeanIsLoaded() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        assertThat(
                Arrays.stream(allBeanNames)
                        .map(StringUtils::lowerCase)
                        .collect(Collectors.toSet())).contains(StringUtils.lowerCase("commandLogStreamingProcessor"));

    }
}
