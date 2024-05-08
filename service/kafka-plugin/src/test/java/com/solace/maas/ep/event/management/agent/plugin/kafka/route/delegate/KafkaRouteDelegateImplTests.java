package com.solace.maas.ep.event.management.agent.plugin.kafka.route.delegate;

import com.solace.maas.ep.event.management.agent.plugin.KafkaTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.kafka.route.enumeration.KafkaScanType;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KafkaTestConfig.class)
class KafkaRouteDelegateImplTests {
    @InjectMocks
    private KafkaRouteDelegateImpl kafkaRouteDelegate;

    private final List<RouteBundle> destinations = List.of(
            RouteBundle.builder()
                    .destinations(List.of())
                    .recipients(List.of())
                    .routeId("testRoute")
                    .firstRouteInChain(false)
                    .messagingServiceId("service1")
                    .build()
    );

    @Test
    void testGenerateKafkaTopicListingRouteList() {
        List<RouteBundle> routeBundles =
                kafkaRouteDelegate.generateRouteList(destinations, List.of(), KafkaScanType.KAFKA_TOPIC_LISTING.name(),
                "service1");

        assertThat(routeBundles).isNotEmpty();
    }

    @Test
    void testGenerateKafkaTopicConfigurationRouteList() {
        List<RouteBundle> routeBundles =
                kafkaRouteDelegate.generateRouteList(destinations, List.of(), KafkaScanType.KAFKA_TOPIC_CONFIGURATION.name(),
                        "service1");

        assertThat(routeBundles).isNotEmpty();
    }

    @Test
    void testGenerateKafkaTopicConfigurationFullRouteList() {
        List<RouteBundle> routeBundles =
                kafkaRouteDelegate.generateRouteList(destinations, List.of(), KafkaScanType.KAFKA_TOPIC_CONFIGURATION_FULL.name(),
                        "service1");

        assertThat(routeBundles).isNotEmpty();
    }

    @Test
    void testGenerateKafkaConsumerGroupsRouteList() {
        List<RouteBundle> routeBundles =
                kafkaRouteDelegate.generateRouteList(destinations, List.of(), KafkaScanType.KAFKA_CONSUMER_GROUPS.name(),
                        "service1");

        assertThat(routeBundles).isNotEmpty();
    }

    @Test
    void testGenerateKafkaConsumerGroupsConfigRouteList() {
        List<RouteBundle> routeBundles =
                kafkaRouteDelegate.generateRouteList(destinations, List.of(),
                        KafkaScanType.KAFKA_CONSUMER_GROUPS_CONFIGURATION.name(),
                        "service1");

        assertThat(routeBundles).isNotEmpty();
    }

    @Test
    void testGenerateKafkaAllRouteList() {
        List<RouteBundle> routeBundles =
                kafkaRouteDelegate.generateRouteList(destinations, List.of(), KafkaScanType.KAFKA_ALL.name(),
                        "service1");

        assertThat(routeBundles).isNotEmpty();
    }
}
