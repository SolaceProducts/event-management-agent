package com.solace.maas.ep.event.management.agent.command.semp;

import com.solace.client.sempv2.api.AclProfileApi;
import com.solace.client.sempv2.api.AuthorizationGroupApi;
import com.solace.client.sempv2.api.ClientProfileApi;
import com.solace.client.sempv2.api.ClientUsernameApi;
import com.solace.client.sempv2.api.QueueApi;
import com.solace.client.sempv2.api.RestDeliveryPointApi;

public interface SempApiProvider {

    AclProfileApi getAclProfileApi();

    AuthorizationGroupApi getAuthorizationGroupApi();

    ClientUsernameApi getClientUsernameApi();

    QueueApi getQueueApi();

    RestDeliveryPointApi getRestDeliveryPointApi();

    ClientProfileApi getClientProfileApi();

}
