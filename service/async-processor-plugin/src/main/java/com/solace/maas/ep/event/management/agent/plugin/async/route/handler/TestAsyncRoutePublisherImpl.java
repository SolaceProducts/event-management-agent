package com.solace.maas.ep.event.management.agent.plugin.async.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncRoutePublisherImpl;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class TestAsyncRoutePublisherImpl extends AsyncRoutePublisherImpl {
    public TestAsyncRoutePublisherImpl(CamelContext camelContext, AsyncManager asyncManager) {
        super(camelContext, asyncManager);
    }

    @Override
    public AsyncWrapper run(Exchange exchange) {
        Disposable subscription = Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .map(i -> sendMesage(i, exchange))
                .subscribe();

        return TestWrapperImpl.builder()
                .asyncProcess(subscription)
                .build();
    }
}
