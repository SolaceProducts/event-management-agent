package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor;

import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client.SolaceSempApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.when;

@Testcontainers
public abstract class BaseTaskProcessorTest {
    public static final String MSG_VPN_NAME = "default";
    public static final String SEMP_USER = "admin";
    public static final String SEMP_PASSWORD = "admin";

    public static final String MSG_SVC_ID = "testService";

    @Container
    public static final GenericContainer solace= new GenericContainer("solace/solace-pubsub-standard")
            .withEnv("username_admin_globalaccesslevel", SEMP_USER)
            .withEnv("username_admin_password", SEMP_PASSWORD)
            .withSharedMemorySize(2L * 1024L * 1024L * 1024L)
            .withReuse(true)
            .withExposedPorts(8080);;
    @Mock
    public MessagingServiceDelegateService messagingServiceDelegateService;

    @BeforeEach
    public void setup() throws Throwable{
        final String sempHost = String.format("http://localhost:%s/SEMP/v2/config", solace.getMappedPort(8080));
        SempApiClient sempApiClient = SempApiClient.builder().apiClient(new ApiClient()).baseurl(sempHost).msgVpn(MSG_VPN_NAME).password(SEMP_PASSWORD).username(SEMP_USER).build();
        SolaceSempApiClient sempClient = new SolaceSempApiClient(sempApiClient);
        when(messagingServiceDelegateService.getMessagingServiceClient(MSG_SVC_ID))
                .thenReturn(sempClient);
        this.setupTest();
    }

    public abstract void setupTest() throws Throwable;
}
