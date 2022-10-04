package com.solace.maas.ep.event.management.agent.plugin.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.DataPublisherRouteBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

import java.util.List;

import static org.mockito.Mockito.mock;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {"camel.springboot.name=routeHandlerTest"}
)
@ActiveProfiles("TEST")
public class DataPublisherRouteBuilderTests {

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:direct:result")
    private MockEndpoint mockResult;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    static class TestEvent {
        private String data;
    }

    public static List<TestEvent> generateTestData() {
        return List.of(
                TestEvent.builder()
                        .data("test")
                        .build(),
                TestEvent.builder()
                        .data("data")
                        .build(),
                TestEvent.builder()
                        .data("end")
                        .build()
        );
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public static RoutesBuilder createRouteBuilder() {
            MDCProcessor mdcProcessor = mock(MDCProcessor.class);

            return new DataPublisherRouteBuilder(exchange -> {
                List<TestEvent> testData = generateTestData();

                exchange.getIn().setBody(testData);
            }, "dataPublisherRoute", "topicListing", null, mdcProcessor);
        }
    }

    @Test
    public void testMockRoute() throws Exception {
        AdviceWith.adviceWith(camelContext, "dataPublisherRoute",
                route -> {
                    route.replaceFromWith("direct:dataPublisherRoute");
                    route.weaveAddLast().to("mock:direct:result");
                });

        mockResult.expectedMessageCount(1);

        template.sendBody("direct:dataPublisherRoute", null);

        mockResult.assertIsSatisfied();
    }
}
