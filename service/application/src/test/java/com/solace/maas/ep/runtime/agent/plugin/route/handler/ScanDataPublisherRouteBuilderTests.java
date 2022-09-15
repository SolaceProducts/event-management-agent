package com.solace.maas.ep.runtime.agent.plugin.route.handler;

import com.solace.maas.ep.runtime.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.runtime.agent.processor.ScanDataProcessor;
import com.solace.maas.ep.runtime.agent.publisher.ScanDataPublisher;
import com.solace.maas.ep.runtime.agent.route.ep.ScanDataPublisherRouteBuilder;
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

import static org.mockito.Mockito.mock;


@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class ScanDataPublisherRouteBuilderTests {

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:result")
    private MockEndpoint mockResult;


    @Test
    @SneakyThrows
    public void testMockRoute() throws Exception {
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

            ScanDataPublisher scanDataPublisher = new ScanDataPublisher(solacePublisher);
            ScanDataProcessor scanDataProcessor = new ScanDataProcessor(scanDataPublisher, eventPortalProperties);

            return new ScanDataPublisherRouteBuilder(scanDataProcessor);
        }
    }
}
