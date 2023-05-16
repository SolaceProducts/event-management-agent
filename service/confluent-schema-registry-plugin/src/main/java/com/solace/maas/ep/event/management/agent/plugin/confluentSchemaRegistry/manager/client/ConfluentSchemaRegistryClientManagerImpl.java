package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.manager.client;

import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.ConfluentSchemaRegistryHttp;
import com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.processor.schema.HttpClient;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.manager.client.MessagingServiceClientManager;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.util.MessagingServiceConfigurationUtil;
import com.solace.maas.ep.event.management.agent.plugin.util.UriUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ExcludeFromJacocoGeneratedReport
@Data
public class ConfluentSchemaRegistryClientManagerImpl implements MessagingServiceClientManager<ConfluentSchemaRegistryHttp> {
    @Override
    public ConfluentSchemaRegistryHttp getClient(ConnectionDetailsEvent connectionDetailsEvent) {
        log.debug("creating Confluent Schema Registry client for: {}", connectionDetailsEvent.getMessagingServiceId());

        HttpClient httpClient = HttpClient.builder()
                .webClient(buildClient(connectionDetailsEvent))
                .connectionUrl(connectionDetailsEvent.getUrl())
                .build();
        log.debug("Confluent Schema Registry client created for {}.", connectionDetailsEvent.getMessagingServiceId());
        return new ConfluentSchemaRegistryHttp(httpClient);
    }

    private CloseableHttpClient buildClient(ConnectionDetailsEvent connectionDetailsEvent) {

        HttpClientBuilder clientBuilder = HttpClients.custom();

        Optional<AuthenticationDetailsEvent> authenticationDetailsEvent =
                connectionDetailsEvent.getAuthenticationDetails().stream().findFirst();

        authenticationDetailsEvent.ifPresent(auth -> {
            Map<String, String> credentialProperties = MessagingServiceConfigurationUtil.getCredentialsProperties(auth);

            if (StringUtils.isNotEmpty(credentialProperties.get("username"))) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                URI uri = UriUtil.getURI(connectionDetailsEvent.getUrl());
                AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
                UsernamePasswordCredentials usernamePasswordCredentials =
                        new UsernamePasswordCredentials(MessagingServiceConfigurationUtil.getUsername(auth),
                                MessagingServiceConfigurationUtil.getPassword(auth));
                credentialsProvider.setCredentials(authScope, usernamePasswordCredentials);
                clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }

            if (credentialProperties.containsKey("ssl.truststore.location")) {
                SSLContextBuilder SSLBuilder = SSLContexts.custom();

                File trustStoreFile = new File(credentialProperties.get("ssl.truststore.location"));
                try {
                    SSLBuilder.loadTrustMaterial(trustStoreFile,
                            credentialProperties.get("ssl.truststore.password").toCharArray());
                } catch (Exception e) {
                    log.error("Exception occurred during loading truststore credentials of properties {}\nexception: {}",
                            credentialProperties,
                            e.getMessage());
                    e.printStackTrace();
                }

                if (credentialProperties.containsKey("ssl.keystore.location")) {
                    File keyStoreFile = new File(credentialProperties.get("ssl.keystore.location"));
                    try {
                        SSLBuilder.loadKeyMaterial(keyStoreFile,
                                credentialProperties.get("ssl.keystore.password").toCharArray(),
                                credentialProperties.get("ssl.key.password").toCharArray());
                    } catch (Exception e) {
                        log.error("Exception occurred during loading keystore credentials of properties {}\nexception: {}",
                                credentialProperties,
                                e.getMessage());
                        e.printStackTrace();
                    }
                }

                try {
                    SSLContext sslcontext = SSLBuilder.build();
                    SSLConnectionSocketFactory sslConnectionSocketFactory =
                            new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());

                    clientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
                } catch (Exception e) {
                    log.error("Exception occurred during setting SSL configurations, exception was: {}",
                            e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return clientBuilder.build();
    }
}
