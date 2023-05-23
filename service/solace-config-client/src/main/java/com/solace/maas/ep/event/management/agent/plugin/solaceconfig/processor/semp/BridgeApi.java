package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridge;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeRemoteMsgVpn;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeRemoteMsgVpnResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeRemoteMsgVpnsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeRemoteSubscription;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeRemoteSubscriptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeRemoteSubscriptionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeTlsTrustedCommonName;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeTlsTrustedCommonNameResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgeTlsTrustedCommonNamesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnBridgesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-17T23:49:01.929728+01:00[Europe/London]")
public class BridgeApi {
    private ApiClient apiClient;

    public BridgeApi() {
        this(new ApiClient());
    }

    public BridgeApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create a Bridge object.
     * Create a Bridge object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| msgVpnName|x||x||| remoteAuthenticationBasicPassword||||x||x remoteAuthenticationClientCertContent||||x||x remoteAuthenticationClientCertPassword||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeResponse createMsgVpnBridge(String msgVpnName, MsgVpnBridge body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnBridgeWithHttpInfo(msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Bridge object.
     * Create a Bridge object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| msgVpnName|x||x||| remoteAuthenticationBasicPassword||||x||x remoteAuthenticationClientCertContent||||x||x remoteAuthenticationClientCertPassword||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeResponse> createMsgVpnBridgeWithHttpInfo(String msgVpnName, MsgVpnBridge body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnBridge");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnBridge");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Remote Message VPN object.
     * Create a Remote Message VPN object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||| bridgeVirtualRouter|x||x||| msgVpnName|x||x||| password||||x||x remoteMsgVpnInterface|x||||| remoteMsgVpnLocation|x|x|||| remoteMsgVpnName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteMsgVpnResponse createMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteMsgVpn body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Remote Message VPN object.
     * Create a Remote Message VPN object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||| bridgeVirtualRouter|x||x||| msgVpnName|x||x||| password||||x||x remoteMsgVpnInterface|x||||| remoteMsgVpnLocation|x|x|||| remoteMsgVpnName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteMsgVpnResponse> createMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteMsgVpn body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling createMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling createMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnBridgeRemoteMsgVpn");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Remote Subscription object.
     * Create a Remote Subscription object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||| bridgeVirtualRouter|x||x||| deliverAlwaysEnabled||x|||| msgVpnName|x||x||| remoteSubscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Subscription object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Remote Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteSubscriptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteSubscriptionResponse createMsgVpnBridgeRemoteSubscription(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnBridgeRemoteSubscriptionWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Remote Subscription object.
     * Create a Remote Subscription object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||| bridgeVirtualRouter|x||x||| deliverAlwaysEnabled||x|||| msgVpnName|x||x||| remoteSubscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Subscription object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Remote Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteSubscriptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteSubscriptionResponse> createMsgVpnBridgeRemoteSubscriptionWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling createMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling createMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnBridgeRemoteSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteSubscriptionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteSubscriptionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Trusted Common Name object.
     * Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||x| bridgeVirtualRouter|x||x||x| msgVpnName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeTlsTrustedCommonNameResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public MsgVpnBridgeTlsTrustedCommonNameResponse createMsgVpnBridgeTlsTrustedCommonName(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeTlsTrustedCommonName body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Trusted Common Name object.
     * Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||x| bridgeVirtualRouter|x||x||x| msgVpnName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeTlsTrustedCommonNameResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<MsgVpnBridgeTlsTrustedCommonNameResponse> createMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeTlsTrustedCommonName body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling createMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling createMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeTlsTrustedCommonNameResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeTlsTrustedCommonNameResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Bridge object.
     * Delete a Bridge object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter) throws RestClientException {
        return deleteMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter).getBody();
    }

    /**
     * Delete a Bridge object.
     * Delete a Bridge object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridge");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<SempMetaOnlyResponse> localReturnType = new ParameterizedTypeReference<SempMetaOnlyResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Remote Message VPN object.
     * Delete a Remote Message VPN object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Remote Message VPN is the Message VPN that the Bridge connects to.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface) throws RestClientException {
        return deleteMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface).getBody();
    }

    /**
     * Delete a Remote Message VPN object.
     * Delete a Remote Message VPN object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Remote Message VPN is the Message VPN that the Bridge connects to.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnName' when calling deleteMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnLocation' when calling deleteMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnInterface' when calling deleteMsgVpnBridgeRemoteMsgVpn");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("remoteMsgVpnName", remoteMsgVpnName);
        uriVariables.put("remoteMsgVpnLocation", remoteMsgVpnLocation);
        uriVariables.put("remoteMsgVpnInterface", remoteMsgVpnInterface);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<SempMetaOnlyResponse> localReturnType = new ParameterizedTypeReference<SempMetaOnlyResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Remote Subscription object.
     * Delete a Remote Subscription object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteSubscriptionTopic The topic of the Bridge remote subscription. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnBridgeRemoteSubscription(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic) throws RestClientException {
        return deleteMsgVpnBridgeRemoteSubscriptionWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic).getBody();
    }

    /**
     * Delete a Remote Subscription object.
     * Delete a Remote Subscription object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteSubscriptionTopic The topic of the Bridge remote subscription. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnBridgeRemoteSubscriptionWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'remoteSubscriptionTopic' is set
        if (remoteSubscriptionTopic == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteSubscriptionTopic' when calling deleteMsgVpnBridgeRemoteSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("remoteSubscriptionTopic", remoteSubscriptionTopic);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<SempMetaOnlyResponse> localReturnType = new ParameterizedTypeReference<SempMetaOnlyResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Trusted Common Name object.
     * Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public SempMetaOnlyResponse deleteMsgVpnBridgeTlsTrustedCommonName(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName) throws RestClientException {
        return deleteMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName).getBody();
    }

    /**
     * Delete a Trusted Common Name object.
     * Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tlsTrustedCommonName' when calling deleteMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("tlsTrustedCommonName", tlsTrustedCommonName);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<SempMetaOnlyResponse> localReturnType = new ParameterizedTypeReference<SempMetaOnlyResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Bridge object.
     * Get a Bridge object.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteAuthenticationBasicPassword||x||x remoteAuthenticationClientCertContent||x||x remoteAuthenticationClientCertPassword||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeResponse getMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, select).getBody();
    }

    /**
     * Get a Bridge object.
     * Get a Bridge object.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteAuthenticationBasicPassword||x||x remoteAuthenticationClientCertContent||x||x remoteAuthenticationClientCertPassword||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeResponse> getMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridge");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Remote Message VPN object.
     * Get a Remote Message VPN object.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| password||x||x remoteMsgVpnInterface|x||| remoteMsgVpnLocation|x||| remoteMsgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteMsgVpnResponse getMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, opaquePassword, select).getBody();
    }

    /**
     * Get a Remote Message VPN object.
     * Get a Remote Message VPN object.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| password||x||x remoteMsgVpnInterface|x||| remoteMsgVpnLocation|x||| remoteMsgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteMsgVpnResponse> getMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnName' when calling getMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnLocation' when calling getMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnInterface' when calling getMsgVpnBridgeRemoteMsgVpn");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("remoteMsgVpnName", remoteMsgVpnName);
        uriVariables.put("remoteMsgVpnLocation", remoteMsgVpnLocation);
        uriVariables.put("remoteMsgVpnInterface", remoteMsgVpnInterface);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Remote Message VPN objects.
     * Get a list of Remote Message VPN objects.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| password||x||x remoteMsgVpnInterface|x||| remoteMsgVpnLocation|x||| remoteMsgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Remote Message VPN objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteMsgVpnsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteMsgVpnsResponse getMsgVpnBridgeRemoteMsgVpns(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnBridgeRemoteMsgVpnsWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Remote Message VPN objects.
     * Get a list of Remote Message VPN objects.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| password||x||x remoteMsgVpnInterface|x||| remoteMsgVpnLocation|x||| remoteMsgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Remote Message VPN objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteMsgVpnsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteMsgVpnsResponse> getMsgVpnBridgeRemoteMsgVpnsWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteMsgVpns");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteMsgVpns");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteMsgVpns");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "where", where));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Remote Subscription object.
     * Get a Remote Subscription object.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteSubscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Subscription object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteSubscriptionTopic The topic of the Bridge remote subscription. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteSubscriptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteSubscriptionResponse getMsgVpnBridgeRemoteSubscription(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnBridgeRemoteSubscriptionWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, opaquePassword, select).getBody();
    }

    /**
     * Get a Remote Subscription object.
     * Get a Remote Subscription object.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteSubscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Subscription object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteSubscriptionTopic The topic of the Bridge remote subscription. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteSubscriptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteSubscriptionResponse> getMsgVpnBridgeRemoteSubscriptionWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteSubscription");
        }
        
        // verify the required parameter 'remoteSubscriptionTopic' is set
        if (remoteSubscriptionTopic == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteSubscriptionTopic' when calling getMsgVpnBridgeRemoteSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("remoteSubscriptionTopic", remoteSubscriptionTopic);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteSubscriptionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteSubscriptionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Remote Subscription objects.
     * Get a list of Remote Subscription objects.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteSubscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Remote Subscription objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteSubscriptionsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteSubscriptionsResponse getMsgVpnBridgeRemoteSubscriptions(String msgVpnName, String bridgeName, String bridgeVirtualRouter, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnBridgeRemoteSubscriptionsWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Remote Subscription objects.
     * Get a list of Remote Subscription objects.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteSubscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Remote Subscription objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteSubscriptionsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteSubscriptionsResponse> getMsgVpnBridgeRemoteSubscriptionsWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteSubscriptions");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteSubscriptions");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteSubscriptions");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "count", count));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "cursor", cursor));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "where", where));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteSubscriptionsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteSubscriptionsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Trusted Common Name object.
     * Get a Trusted Common Name object.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||x| bridgeVirtualRouter|x||x| msgVpnName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeTlsTrustedCommonNameResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public MsgVpnBridgeTlsTrustedCommonNameResponse getMsgVpnBridgeTlsTrustedCommonName(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, opaquePassword, select).getBody();
    }

    /**
     * Get a Trusted Common Name object.
     * Get a Trusted Common Name object.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||x| bridgeVirtualRouter|x||x| msgVpnName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeTlsTrustedCommonNameResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<MsgVpnBridgeTlsTrustedCommonNameResponse> getMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tlsTrustedCommonName' when calling getMsgVpnBridgeTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("tlsTrustedCommonName", tlsTrustedCommonName);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeTlsTrustedCommonNameResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeTlsTrustedCommonNameResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Trusted Common Name objects.
     * Get a list of Trusted Common Name objects.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||x| bridgeVirtualRouter|x||x| msgVpnName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The list of Trusted Common Name objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeTlsTrustedCommonNamesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public MsgVpnBridgeTlsTrustedCommonNamesResponse getMsgVpnBridgeTlsTrustedCommonNames(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnBridgeTlsTrustedCommonNamesWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Trusted Common Name objects.
     * Get a list of Trusted Common Name objects.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||x| bridgeVirtualRouter|x||x| msgVpnName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The list of Trusted Common Name objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeTlsTrustedCommonNamesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<MsgVpnBridgeTlsTrustedCommonNamesResponse> getMsgVpnBridgeTlsTrustedCommonNamesWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeTlsTrustedCommonNames");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeTlsTrustedCommonNames");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeTlsTrustedCommonNames");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "where", where));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeTlsTrustedCommonNamesResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeTlsTrustedCommonNamesResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Bridge objects.
     * Get a list of Bridge objects.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteAuthenticationBasicPassword||x||x remoteAuthenticationClientCertContent||x||x remoteAuthenticationClientCertPassword||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Bridge objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgesResponse getMsgVpnBridges(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnBridgesWithHttpInfo(msgVpnName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Bridge objects.
     * Get a list of Bridge objects.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteAuthenticationBasicPassword||x||x remoteAuthenticationClientCertContent||x||x remoteAuthenticationClientCertPassword||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Bridge objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgesResponse> getMsgVpnBridgesWithHttpInfo(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnBridges");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "count", count));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "cursor", cursor));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "where", where));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgesResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgesResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Bridge object.
     * Replace a Bridge object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- bridgeName|x||x|||| bridgeVirtualRouter|x||x|||| maxTtl|||||x|| msgVpnName|x||x|||| remoteAuthenticationBasicClientUsername|||||x|| remoteAuthenticationBasicPassword||||x|x||x remoteAuthenticationClientCertContent||||x|x||x remoteAuthenticationClientCertPassword||||x|x|| remoteAuthenticationScheme|||||x|| remoteDeliverToOnePriority|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeResponse replaceMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Bridge object.
     * Replace a Bridge object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- bridgeName|x||x|||| bridgeVirtualRouter|x||x|||| maxTtl|||||x|| msgVpnName|x||x|||| remoteAuthenticationBasicClientUsername|||||x|| remoteAuthenticationBasicPassword||||x|x||x remoteAuthenticationClientCertContent||||x|x||x remoteAuthenticationClientCertPassword||||x|x|| remoteAuthenticationScheme|||||x|| remoteDeliverToOnePriority|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeResponse> replaceMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling replaceMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling replaceMsgVpnBridge");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnBridge");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Remote Message VPN object.
     * Replace a Remote Message VPN object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- bridgeName|x||x|||| bridgeVirtualRouter|x||x|||| clientUsername|||||x|| compressedDataEnabled|||||x|| egressFlowWindowSize|||||x|| msgVpnName|x||x|||| password||||x|x||x remoteMsgVpnInterface|x||x|||| remoteMsgVpnLocation|x||x|||| remoteMsgVpnName|x||x|||| tlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteMsgVpnResponse replaceMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Remote Message VPN object.
     * Replace a Remote Message VPN object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- bridgeName|x||x|||| bridgeVirtualRouter|x||x|||| clientUsername|||||x|| compressedDataEnabled|||||x|| egressFlowWindowSize|||||x|| msgVpnName|x||x|||| password||||x|x||x remoteMsgVpnInterface|x||x|||| remoteMsgVpnLocation|x||x|||| remoteMsgVpnName|x||x|||| tlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteMsgVpnResponse> replaceMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnName' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnLocation' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnInterface' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnBridgeRemoteMsgVpn");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("remoteMsgVpnName", remoteMsgVpnName);
        uriVariables.put("remoteMsgVpnLocation", remoteMsgVpnLocation);
        uriVariables.put("remoteMsgVpnInterface", remoteMsgVpnInterface);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Bridge object.
     * Update a Bridge object. Any attribute missing from the request will be left unchanged.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| maxTtl||||x|| msgVpnName|x|x|||| remoteAuthenticationBasicClientUsername||||x|| remoteAuthenticationBasicPassword|||x|x||x remoteAuthenticationClientCertContent|||x|x||x remoteAuthenticationClientCertPassword|||x|x|| remoteAuthenticationScheme||||x|| remoteDeliverToOnePriority||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeResponse updateMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Bridge object.
     * Update a Bridge object. Any attribute missing from the request will be left unchanged.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| maxTtl||||x|| msgVpnName|x|x|||| remoteAuthenticationBasicClientUsername||||x|| remoteAuthenticationBasicPassword|||x|x||x remoteAuthenticationClientCertContent|||x|x||x remoteAuthenticationClientCertPassword|||x|x|| remoteAuthenticationScheme||||x|| remoteDeliverToOnePriority||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Bridge object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeResponse> updateMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling updateMsgVpnBridge");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling updateMsgVpnBridge");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnBridge");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Remote Message VPN object.
     * Update a Remote Message VPN object. Any attribute missing from the request will be left unchanged.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| clientUsername||||x|| compressedDataEnabled||||x|| egressFlowWindowSize||||x|| msgVpnName|x|x|||| password|||x|x||x remoteMsgVpnInterface|x|x|||| remoteMsgVpnLocation|x|x|||| remoteMsgVpnName|x|x|||| tlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnBridgeRemoteMsgVpnResponse updateMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Remote Message VPN object.
     * Update a Remote Message VPN object. Any attribute missing from the request will be left unchanged.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| clientUsername||||x|| compressedDataEnabled||||x|| egressFlowWindowSize||||x|| msgVpnName|x|x|||| password|||x|x||x remoteMsgVpnInterface|x|x|||| remoteMsgVpnLocation|x|x|||| remoteMsgVpnName|x|x|||| tlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Remote Message VPN object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param bridgeName The name of the Bridge. (required)
     * @param bridgeVirtualRouter The virtual router of the Bridge. (required)
     * @param remoteMsgVpnName The name of the remote Message VPN. (required)
     * @param remoteMsgVpnLocation The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). (required)
     * @param remoteMsgVpnInterface The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnBridgeRemoteMsgVpnResponse> updateMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeName' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'bridgeVirtualRouter' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnName' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnLocation' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'remoteMsgVpnInterface' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnBridgeRemoteMsgVpn");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("bridgeName", bridgeName);
        uriVariables.put("bridgeVirtualRouter", bridgeVirtualRouter);
        uriVariables.put("remoteMsgVpnName", remoteMsgVpnName);
        uriVariables.put("remoteMsgVpnLocation", remoteMsgVpnLocation);
        uriVariables.put("remoteMsgVpnInterface", remoteMsgVpnInterface);

        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "opaquePassword", opaquePassword));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("csv".toUpperCase(Locale.ROOT)), "select", select));

        final String[] localVarAccepts = { 
            "application/json"
         };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
         };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse> localReturnType = new ParameterizedTypeReference<MsgVpnBridgeRemoteMsgVpnResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
