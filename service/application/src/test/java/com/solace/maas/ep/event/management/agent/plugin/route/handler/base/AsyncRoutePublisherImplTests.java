package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.Message;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class AsyncRoutePublisherImplTests {
    CamelContext camelContext = mock(CamelContext.class);

    AsyncManager asyncManager = mock(AsyncManager.class);

    CamelReactiveStreamsService camelReactiveStreamsService = mock(CamelReactiveStreamsService.class);

    @Test
    @SneakyThrows
    public void testStart() {
        AsyncWrapper asyncWrapper = mock(AsyncWrapper.class);

        Exchange exchange = ExchangeBuilder.anExchange(mock(ExtendedCamelContext.class))
                .withHeader(RouteConstants.SCAN_ID, "scan1")
                .withHeader(RouteConstants.SCAN_TYPE, "testScan")
                .build();

        try(MockedStatic<CamelReactiveStreams> reactiveStreams = mockStatic(CamelReactiveStreams.class)) {
            reactiveStreams.when(() -> CamelReactiveStreams.get(camelContext))
                    .thenReturn(camelReactiveStreamsService);

            AsyncRoutePublisherImpl asyncRoutePublisher = mock(AsyncRoutePublisherImpl.class, Mockito.withSettings()
                    .useConstructor(camelContext, asyncManager)
                    .defaultAnswer(Mockito.CALLS_REAL_METHODS));

            when(asyncRoutePublisher.run(exchange))
                    .thenReturn(asyncWrapper);
            doNothing().when(asyncManager).storeAsync(asyncWrapper, "scan1", "testScan");

            asyncRoutePublisher.start(exchange);
        }

        assertThatNoException();
    }

    @Test
    @SneakyThrows
    public void testSendMessage() {
        Exchange exchange = ExchangeBuilder.anExchange(mock(ExtendedCamelContext.class))
                .withHeader(RouteConstants.SCAN_ID, "scan1")
                .withHeader(RouteConstants.SCAN_TYPE, "testScan")
                .build();

        CamelContext extendedCamelContext = mock(ExtendedCamelContext.class);

        Exchange newExchange = mock(Exchange.class);
        Message message = mock(Message.class);

        when(camelReactiveStreamsService.getCamelContext())
                .thenReturn(extendedCamelContext);

        try(MockedStatic<CamelReactiveStreams> reactiveStreams = mockStatic(CamelReactiveStreams.class)) {
            reactiveStreams.when(() -> CamelReactiveStreams.get(camelContext))
                    .thenReturn(camelReactiveStreamsService);

            ExchangeBuilder exchangeBuilder = mock(ExchangeBuilder.class, Mockito.RETURNS_SELF);
            when(exchangeBuilder.build())
                    .thenReturn(newExchange);

            when(newExchange.getIn()).thenReturn(message);
            when(message.getHeaders())
                    .thenReturn(Map.of());
            doNothing().when(message).setHeaders(anyMap());

            AsyncRoutePublisherImpl asyncRoutePublisher = mock(AsyncRoutePublisherImpl.class, Mockito.withSettings()
                    .useConstructor(camelContext, asyncManager)
                    .defaultAnswer(Mockito.CALLS_REAL_METHODS));

            asyncRoutePublisher.sendMesage("test", exchange);
        }

        assertThatNoException();
    }
}
