package com.solace.maas.ep.runtime.agent.processor;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.maas.ep.runtime.agent.plugin.publisher.SolaceWebPublisher;
import com.solace.maas.ep.runtime.agent.plugin.vmr.WebProcessor;
import com.solace.messaging.resources.Topic;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class,
        properties = {"camel.springboot.name=webProcessorTests"})
@ActiveProfiles("TEST")
public class WebProcessorTests {

    @Mock
    SolaceWebPublisher solaceWebPublisher;

    @InjectMocks
    WebProcessor webProcessor;

    @Autowired
    private CamelContext camelContext;

    @SneakyThrows
    @Test
    public void whenSolaceWebPublisherCalledVerified() {
        RtoMessageBuilder RtoMessageBuilder = mock(RtoMessageBuilder.class);
        Topic solaceTopic = Topic.of("test");

        SolaceWebPublisher solaceWebPublisher = new SolaceWebPublisher(RtoMessageBuilder, solaceTopic);
        solaceWebPublisher.publish("data", "id");

        verify(RtoMessageBuilder, times(1)).publish("data", Topic.of("test/id"));
    }

    @SneakyThrows
    @Test
    public void whenWebProcessorCalledVerified() {
        Exchange exchange = new DefaultExchange(camelContext);

        exchange.getIn().setHeader("TOPIC","a");
        exchange.getIn().setHeader("TOPIC_ID","b");
        exchange.getIn().setBody("test exchange");

        webProcessor.process(exchange);

        verify(solaceWebPublisher, times(1)).publish(Mockito.anyString(), Mockito.anyString());
    }


}
