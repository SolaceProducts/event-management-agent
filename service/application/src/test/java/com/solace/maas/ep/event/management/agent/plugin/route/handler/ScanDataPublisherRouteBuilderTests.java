package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.processor.ScanDataProcessor;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataPublisher;
import com.solace.maas.ep.event.management.agent.route.ep.ScanDataPublisherRouteBuilder;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.noop.NoopCounter;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
class ScanDataPublisherRouteBuilderTests {

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:result")
    private MockEndpoint mockResult;


    @Test
    @SneakyThrows
    void testMockRoute() {
        AdviceWith.adviceWith(camelContext, "scanDataPublisher",
                route -> {
                    route.replaceFromWith("direct:eventPortal");
                    route.weaveAddLast().to("mock:direct:result");
                });

        mockResult.expectedMessageCount(1);
        template.sendBody("direct:eventPortal", null);
        mockResult.assertIsSatisfied();
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            SolacePublisher solacePublisher = mock(SolacePublisher.class);
            EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);
            MeterRegistry meterRegistry = mock(MeterRegistry.class);
            when(meterRegistry.counter(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(new NoopCounter(new Meter.Id("noop", null, null, null, null)));

            ScanDataPublisher scanDataPublisher = new ScanDataPublisher(solacePublisher, meterRegistry);
            ScanDataProcessor scanDataProcessor = new ScanDataProcessor(scanDataPublisher, eventPortalProperties);
            ScanTypeDescendentsProcessor scanTypeDescendentsProcessor = mock(ScanTypeDescendentsProcessor.class);

            return new ScanDataPublisherRouteBuilder(scanDataProcessor, scanTypeDescendentsProcessor);
        }
    }
}
