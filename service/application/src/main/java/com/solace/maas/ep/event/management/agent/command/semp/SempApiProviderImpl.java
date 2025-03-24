package com.solace.maas.ep.event.management.agent.command.semp;

import com.solace.client.sempv2.ApiClient;
import com.solace.client.sempv2.api.AclProfileApi;
import com.solace.client.sempv2.api.AuthorizationGroupApi;
import com.solace.client.sempv2.api.ClientUsernameApi;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("PMD")
public class SempApiProviderImpl implements SempApiProvider {

    private final ApiClient apiClient;
    private final EventPortalProperties eventPortalProperties;
    private AclProfileApi aclProfileApi;
    private AuthorizationGroupApi authorizationGroupApi;
    private ClientUsernameApi clientUsernameApi;
    private QueueApi queueApi;


    public SempApiProviderImpl(SolaceHttpSemp solaceClient,
                               EventPortalProperties eventPortalProperties) {
        this.apiClient = setupApiClient(solaceClient);
        this.eventPortalProperties = eventPortalProperties;
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

    private ApiClient setupApiClient(SolaceHttpSemp solaceClient) {
        SempClient sempClient = solaceClient.getSempClient();
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(sempClient.getConnectionUrl() + "/SEMP/v2/config");
        apiClient.setUsername(sempClient.getUsername());
        apiClient.setPassword(sempClient.getPassword());
        log.info("SetVerifyingSsl? {} (application properties skipTlsVerify: {})", eventPortalProperties != null && eventPortalProperties.getSkipTlsVerify(),
                eventPortalProperties == null ? "false" : eventPortalProperties.getSkipTlsVerify());
        apiClient.setVerifyingSsl(eventPortalProperties != null && eventPortalProperties.getSkipTlsVerify());
        return apiClient;
    }
}
