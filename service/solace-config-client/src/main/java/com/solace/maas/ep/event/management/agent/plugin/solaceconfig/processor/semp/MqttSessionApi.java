package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSession;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSessionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSessionSubscription;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSessionSubscriptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSessionSubscriptionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttSessionsResponse;
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
public class MqttSessionApi {
    private ApiClient apiClient;

    public MqttSessionApi() {
        this(new ApiClient());
    }

    public MqttSessionApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create an MQTT Session object.
     * Create an MQTT Session object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x|||| mqttSessionVirtualRouter|x|x|||| msgVpnName|x||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The MQTT Session object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionResponse createMsgVpnMqttSession(String msgVpnName, MsgVpnMqttSession body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnMqttSessionWithHttpInfo(msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Create an MQTT Session object.
     * Create an MQTT Session object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x|||| mqttSessionVirtualRouter|x|x|||| msgVpnName|x||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The MQTT Session object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionResponse> createMsgVpnMqttSessionWithHttpInfo(String msgVpnName, MsgVpnMqttSession body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnMqttSession");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnMqttSession");
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

        ParameterizedTypeReference<MsgVpnMqttSessionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Subscription object.
     * Create a Subscription object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x||| mqttSessionVirtualRouter|x||x||| msgVpnName|x||x||| subscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param body The Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionSubscriptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionSubscriptionResponse createMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSessionSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnMqttSessionSubscriptionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Subscription object.
     * Create a Subscription object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x||| mqttSessionVirtualRouter|x||x||| msgVpnName|x||x||| subscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param body The Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionSubscriptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionSubscriptionResponse> createMsgVpnMqttSessionSubscriptionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSessionSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling createMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling createMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnMqttSessionSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);

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

        ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete an MQTT Session object.
     * Delete an MQTT Session object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter) throws RestClientException {
        return deleteMsgVpnMqttSessionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter).getBody();
    }

    /**
     * Delete an MQTT Session object.
     * Delete an MQTT Session object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnMqttSessionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling deleteMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling deleteMsgVpnMqttSession");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Subscription object.
     * Delete a Subscription object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic) throws RestClientException {
        return deleteMsgVpnMqttSessionSubscriptionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, subscriptionTopic).getBody();
    }

    /**
     * Delete a Subscription object.
     * Delete a Subscription object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnMqttSessionSubscriptionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling deleteMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling deleteMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'subscriptionTopic' is set
        if (subscriptionTopic == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'subscriptionTopic' when calling deleteMsgVpnMqttSessionSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);
        uriVariables.put("subscriptionTopic", subscriptionTopic);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get an MQTT Session object.
     * Get an MQTT Session object.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionResponse getMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnMqttSessionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, opaquePassword, select).getBody();
    }

    /**
     * Get an MQTT Session object.
     * Get an MQTT Session object.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionResponse> getMsgVpnMqttSessionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling getMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling getMsgVpnMqttSession");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);

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

        ParameterizedTypeReference<MsgVpnMqttSessionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Subscription object.
     * Get a Subscription object.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x||| subscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionSubscriptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionSubscriptionResponse getMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnMqttSessionSubscriptionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, subscriptionTopic, opaquePassword, select).getBody();
    }

    /**
     * Get a Subscription object.
     * Get a Subscription object.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x||| subscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionSubscriptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionSubscriptionResponse> getMsgVpnMqttSessionSubscriptionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling getMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling getMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'subscriptionTopic' is set
        if (subscriptionTopic == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'subscriptionTopic' when calling getMsgVpnMqttSessionSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);
        uriVariables.put("subscriptionTopic", subscriptionTopic);

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

        ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Subscription objects.
     * Get a list of Subscription objects.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x||| subscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The list of Subscription objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionSubscriptionsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionSubscriptionsResponse getMsgVpnMqttSessionSubscriptions(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnMqttSessionSubscriptionsWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Subscription objects.
     * Get a list of Subscription objects.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x||| subscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The list of Subscription objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionSubscriptionsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionSubscriptionsResponse> getMsgVpnMqttSessionSubscriptionsWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSessionSubscriptions");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling getMsgVpnMqttSessionSubscriptions");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling getMsgVpnMqttSessionSubscriptions");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);

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

        ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of MQTT Session objects.
     * Get a list of MQTT Session objects.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The list of MQTT Session objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionsResponse getMsgVpnMqttSessions(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnMqttSessionsWithHttpInfo(msgVpnName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of MQTT Session objects.
     * Get a list of MQTT Session objects.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: mqttSessionClientId|x||| mqttSessionVirtualRouter|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The list of MQTT Session objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionsResponse> getMsgVpnMqttSessionsWithHttpInfo(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSessions");
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

        ParameterizedTypeReference<MsgVpnMqttSessionsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace an MQTT Session object.
     * Replace an MQTT Session object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x||x|||| mqttSessionVirtualRouter|x||x|||| msgVpnName|x||x|||| owner|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param body The MQTT Session object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionResponse replaceMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSession body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnMqttSessionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Replace an MQTT Session object.
     * Replace an MQTT Session object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x||x|||| mqttSessionVirtualRouter|x||x|||| msgVpnName|x||x|||| owner|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param body The MQTT Session object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionResponse> replaceMsgVpnMqttSessionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSession body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling replaceMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling replaceMsgVpnMqttSession");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnMqttSession");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);

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

        ParameterizedTypeReference<MsgVpnMqttSessionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Subscription object.
     * Replace a Subscription object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x||x|||| mqttSessionVirtualRouter|x||x|||| msgVpnName|x||x|||| subscriptionTopic|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @param body The Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionSubscriptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionSubscriptionResponse replaceMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, MsgVpnMqttSessionSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnMqttSessionSubscriptionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, subscriptionTopic, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Subscription object.
     * Replace a Subscription object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x||x|||| mqttSessionVirtualRouter|x||x|||| msgVpnName|x||x|||| subscriptionTopic|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @param body The Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionSubscriptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionSubscriptionResponse> replaceMsgVpnMqttSessionSubscriptionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, MsgVpnMqttSessionSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling replaceMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling replaceMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'subscriptionTopic' is set
        if (subscriptionTopic == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'subscriptionTopic' when calling replaceMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnMqttSessionSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);
        uriVariables.put("subscriptionTopic", subscriptionTopic);

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

        ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update an MQTT Session object.
     * Update an MQTT Session object. Any attribute missing from the request will be left unchanged.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x|x|||| mqttSessionVirtualRouter|x|x|||| msgVpnName|x|x|||| owner||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param body The MQTT Session object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionResponse updateMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSession body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnMqttSessionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, body, opaquePassword, select).getBody();
    }

    /**
     * Update an MQTT Session object.
     * Update an MQTT Session object. Any attribute missing from the request will be left unchanged.  An MQTT Session object is a virtual representation of an MQTT client connection. An MQTT session holds the state of an MQTT client (that is, it is used to contain a client&#39;s QoS 0 and QoS 1 subscription sets and any undelivered QoS 1 messages).   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x|x|||| mqttSessionVirtualRouter|x|x|||| msgVpnName|x|x|||| owner||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The MQTT Session object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param body The MQTT Session object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionResponse> updateMsgVpnMqttSessionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSession body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling updateMsgVpnMqttSession");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling updateMsgVpnMqttSession");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnMqttSession");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);

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

        ParameterizedTypeReference<MsgVpnMqttSessionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Subscription object.
     * Update a Subscription object. Any attribute missing from the request will be left unchanged.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x|x|||| mqttSessionVirtualRouter|x|x|||| msgVpnName|x|x|||| subscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @param body The Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttSessionSubscriptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttSessionSubscriptionResponse updateMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, MsgVpnMqttSessionSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnMqttSessionSubscriptionWithHttpInfo(msgVpnName, mqttSessionClientId, mqttSessionVirtualRouter, subscriptionTopic, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Subscription object.
     * Update a Subscription object. Any attribute missing from the request will be left unchanged.  An MQTT session contains a client&#39;s QoS 0 and QoS 1 subscription sets. On creation, a subscription defaults to QoS 0.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- mqttSessionClientId|x|x|||| mqttSessionVirtualRouter|x|x|||| msgVpnName|x|x|||| subscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
     * <p><b>200</b> - The Subscription object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
     * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
     * @param subscriptionTopic The MQTT subscription topic. (required)
     * @param body The Subscription object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttSessionSubscriptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttSessionSubscriptionResponse> updateMsgVpnMqttSessionSubscriptionWithHttpInfo(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, MsgVpnMqttSessionSubscription body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionClientId' is set
        if (mqttSessionClientId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionClientId' when calling updateMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'mqttSessionVirtualRouter' is set
        if (mqttSessionVirtualRouter == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'mqttSessionVirtualRouter' when calling updateMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'subscriptionTopic' is set
        if (subscriptionTopic == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'subscriptionTopic' when calling updateMsgVpnMqttSessionSubscription");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnMqttSessionSubscription");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("mqttSessionClientId", mqttSessionClientId);
        uriVariables.put("mqttSessionVirtualRouter", mqttSessionVirtualRouter);
        uriVariables.put("subscriptionTopic", subscriptionTopic);

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

        ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttSessionSubscriptionResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
