package com.solace.maas.ep.event.management.agent.plugin.solace.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.NoSuchElementException;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Data
public class SolaceSempClientManagerImpl implements MessagingServiceClientManager<SolaceHttpSemp> {

    @Override
    public SolaceHttpSemp getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.trace("Creating Solace SEMP client for messaging service [{}].",
                connectionDetailsEvent.getMessagingServiceId());

        AuthenticationDetailsEvent authenticationDetailsEvent =
                connectionDetailsEvent.getAuthenticationDetails()
                        .stream()
                        .findFirst().orElseThrow(() -> {
                            String message = String.format("Could not find authentication details for service with id [%s].",
                                    connectionDetailsEvent.getMessagingServiceId());
                            log.error(message);
                            return new NoSuchElementException(message);
                        });
        WebClient.Builder webClient = WebClient.builder();
        if (Boolean.TRUE.equals(connectionDetailsEvent.getSkipTlsVerify())) {
            log.info("Skipping TLS verification for new Solace SEMP client.");
            webClient.clientConnector(new ReactorClientHttpConnector(HttpClient.create().secure(t -> {
                        try {
                            t.sslContext(SslContextBuilder.forClient()
                                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                    .build());
                        } catch (SSLException e) {
                            log.error("Failed to configure SSL context to skip verification on the Solace SEMP client.", e);
                        }
                    }))).build();
        }

        SempClient sempClient = SempClient.builder()
                .webClient(webClient.build())
                .username(MessagingServiceConfigurationUtil.getUsername(authenticationDetailsEvent))
                .password(MessagingServiceConfigurationUtil.getPassword(authenticationDetailsEvent))
                .msgVpn(MessagingServiceConfigurationUtil.getMsgVpn(connectionDetailsEvent))
                .connectionUrl(connectionDetailsEvent.getUrl())
                .build();

        log.trace("Solace SEMP client created for {}.", connectionDetailsEvent.getMessagingServiceId());
        return new SolaceHttpSemp(sempClient);
    }
}
