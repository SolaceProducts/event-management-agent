package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.common.messages.VmrProcessorMessage;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.plugin.vmr.VmrProcessor;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class,
        properties = {"camel.springboot.name=vmrProcessorTest"})
@ActiveProfiles("TEST")
public class VmrProcessorTests {

    @Mock
    SolacePublisher solacePublisher;

    @InjectMocks
    VmrProcessor vmrProcessor;

    @Autowired
    private CamelContext camelContext;

    @SneakyThrows
    @Test
    public void whenSolacePublisherCalledVerified() {
        DirectMessagePublisher directMessagePublisher = mock(DirectMessagePublisher.class);
        OutboundMessageBuilder outboundMessageBuilder = mock(OutboundMessageBuilder.class);
        OutboundMessage outboundMessage = mock(OutboundMessage.class);

        SolacePublisher solacePublisher = new SolacePublisher(outboundMessageBuilder, directMessagePublisher);
        VmrProcessorMessage message = new VmrProcessorMessage("data");
        when(outboundMessageBuilder.fromProperties(any())).thenReturn(outboundMessageBuilder);
        when(outboundMessageBuilder.build(anyString()))
                .thenReturn(outboundMessage);

        solacePublisher.publish(message, "test");

        verify(directMessagePublisher, times(1)).publish(any(), any(), any());
    }

    @SneakyThrows
    @Test
    public void whenVmrProcessorCalledVerified() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody("test exchange");

        vmrProcessor.process(exchange);

        verify(solacePublisher, times(1)).publish(any(), anyString());
    }

    @Test
    public void testVmrProcessorMessageMOPProtocol() {
        VmrProcessorMessage message = new VmrProcessorMessage("body");
        assertThat(message.getMopProtocol()).isEqualTo(MOPProtocol.scanDataControl);
    }
}
