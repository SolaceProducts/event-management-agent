package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.reactivestreams.Publisher;

public abstract class AsyncRoutePublisherImpl implements AsyncRoutePublisher {
    private final CamelReactiveStreamsService camel;

    private final AsyncManager asyncManager;

    public AsyncRoutePublisherImpl(CamelContext camelContext, AsyncManager asyncManager) {
        this.camel = CamelReactiveStreams.get(camelContext);
        this.asyncManager = asyncManager;
    }

    @Override
    public void start(Exchange exchange) {
        String scanId = exchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class);
        String scanType = exchange.getIn().getHeader(RouteConstants.SCAN_TYPE, String.class);

        AsyncWrapper asyncWrapper = run(exchange);

        asyncManager.storeAsync(asyncWrapper, scanId, scanType);
    }

    public Publisher<Exchange> sendMesage(Object body, Exchange exchange) {
        String routeId = exchange.getFromRouteId();
        Exchange newExchange = ExchangeBuilder.anExchange(camel.getCamelContext())
                .withBody(body)
                .build();

        newExchange.getIn().setHeaders(exchange.getIn().getHeaders());
        return camel.to("seda:asyncEvent_" + routeId, newExchange);
    }
}
