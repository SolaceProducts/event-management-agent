package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncRoutePublisherImpl;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import com.solace.maas.ep.event.management.agent.plugin.solace.properties.SolaceMessagingProperties;
import com.solace.messaging.MessagingService;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class TopicRoutePublisherImpl extends AsyncRoutePublisherImpl {

    private final static ExecutorService executor = Executors.newFixedThreadPool(10);
    private final SolaceMessagingProperties solaceMessagingProperties;

    public TopicRoutePublisherImpl(CamelContext camelContext, AsyncManager asyncManager,
                                   SolaceMessagingProperties solaceMessagingProperties) {
        super(camelContext, asyncManager);
        this.solaceMessagingProperties = solaceMessagingProperties;
    }

    @Override
    public AsyncWrapper run(Exchange exchange) {

        final Properties properties = new Properties();
        // Greg's enterprise broker
        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, solaceMessagingProperties.getUrl());
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, solaceMessagingProperties.getMsgVpnName());
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, solaceMessagingProperties.getUsername());
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, solaceMessagingProperties.getPassword());

        final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties)
                .build()
                .connect();

        String subscription = ">";

        SolaceSubscriberNoThread subscriber = SolaceSubscriberNoThread.builder()
                .service(messagingService)
                .subscription(subscription)
                .maxMessages(50000)
                .publisher(this)
                .exchange(exchange)
                .build();
        subscriber.consumerMessages();
        //Future<Integer> future = executor.submit(subscriber);

//        Disposable subscription = Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
//                .map(i -> sendMesage(i, exchange))
//                .subscribe();

        return TopicWrapperImpl.builder()
                .solaceSubscriber(subscriber)
                .build();
    }
}
