package com.solace.maas.ep.event.management.agent.plugin.ibmmq.manager.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.solace.maas.ep.event.management.agent.plugin.ibmmq.client.http.IbmMqHttpClient;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * Implementation of the IBM MQ Administration client.
 * <p>
 * This client provides access to the various administrative endpoints exposed by IBM MQ.
 */
@Slf4j
@Data
@Component
public class IbmMqClientManagerImpl implements MessagingServiceClientManager<IbmMqHttpClient> {

    public IbmMqClientManagerImpl() {
    }

    @Override
    public IbmMqHttpClient getClient(ConnectionDetailsEvent connectionDetailsEvent) {

        log.trace("Creating IBM MQ RESTful client for event broker [{}].",
                connectionDetailsEvent.getMessagingServiceId());

        //get authentication details from config file
        AuthenticationDetailsEvent authenticationDetailsEvent = connectionDetailsEvent.getAuthenticationDetails()
                .stream()
                .findFirst().orElseThrow(() -> {
                    String message = String.format("Could not find authentication details for service with id [%s].",
                            connectionDetailsEvent.getMessagingServiceId());
                    log.error(message);
                    return new NoSuchElementException(message);
                });

        String username = MessagingServiceConfigurationUtil.getUsername(authenticationDetailsEvent);
        String password = MessagingServiceConfigurationUtil.getPassword(authenticationDetailsEvent);
        String url = connectionDetailsEvent.getUrl();

        /*so that we can configure Jackson to ignore unknown properties in the
          response json.
         */
        ObjectMapper mapper = JsonMapper
                .builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();

        return Feign.builder()
                .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder(mapper))
                .target(IbmMqHttpClient.class, url);
    }

}
