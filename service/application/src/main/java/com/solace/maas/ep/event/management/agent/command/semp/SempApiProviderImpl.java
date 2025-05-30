package com.solace.maas.ep.event.management.agent.command.semp;

import com.solace.client.sempv2.ApiClient;
import com.solace.client.sempv2.api.AclProfileApi;
import com.solace.client.sempv2.api.AuthorizationGroupApi;
import com.solace.client.sempv2.api.ClientProfileApi;
import com.solace.client.sempv2.api.ClientUsernameApi;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.client.sempv2.api.RestDeliveryPointApi;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("PMD")
public class SempApiProviderImpl implements SempApiProvider {

    private final ApiClient apiClient;
    private AclProfileApi aclProfileApi;
    private AuthorizationGroupApi authorizationGroupApi;
    private ClientUsernameApi clientUsernameApi;
    private QueueApi queueApi;
    private RestDeliveryPointApi restDeliveryPointApi;
    private ClientProfileApi clientProfileApi;

    public SempApiProviderImpl(SolaceHttpSemp solaceClient,
                               EventPortalProperties eventPortalProperties) {
        apiClient = setupApiClient(solaceClient, eventPortalProperties);
    }

    @Override
    public AclProfileApi getAclProfileApi() {
        if (aclProfileApi == null) {
            aclProfileApi = new AclProfileApi(apiClient);
        }
        return aclProfileApi;
    }

    @Override
    public AuthorizationGroupApi getAuthorizationGroupApi() {
        if (authorizationGroupApi == null) {
            authorizationGroupApi = new AuthorizationGroupApi(apiClient);
        }
        return authorizationGroupApi;
    }

    @Override
    public ClientUsernameApi getClientUsernameApi() {
        if (clientUsernameApi == null) {
            clientUsernameApi = new ClientUsernameApi(apiClient);
        }
        return clientUsernameApi;
    }

    @Override
    public QueueApi getQueueApi() {
        if (queueApi == null) {
            queueApi = new QueueApi(apiClient);
        }
        return queueApi;
    }

    @Override
    public ClientProfileApi getClientProfileApi() {
        if (clientProfileApi == null) {
            clientProfileApi = new ClientProfileApi(apiClient);
        }
        return clientProfileApi;
    }

    private ApiClient setupApiClient(SolaceHttpSemp solaceClient, EventPortalProperties eventPortalProperties) {
        SempClient sempClient = solaceClient.getSempClient();
        ApiClient client = new ApiClient();
        client.setBasePath(sempClient.getConnectionUrl() + "/SEMP/v2/config");
        client.setUsername(sempClient.getUsername());
        client.setPassword(sempClient.getPassword());
        client.setVerifyingSsl(eventPortalProperties == null || !eventPortalProperties.getSkipTlsVerify());
        return client;
    }

    @Override
    public RestDeliveryPointApi getRestDeliveryPointApi() {
        if (restDeliveryPointApi == null) {
            restDeliveryPointApi = new RestDeliveryPointApi(apiClient);
        }
        return restDeliveryPointApi;
    }
}
