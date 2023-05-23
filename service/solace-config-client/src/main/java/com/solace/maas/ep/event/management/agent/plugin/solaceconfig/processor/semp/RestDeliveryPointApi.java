package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPoint;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBinding;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingRequestHeader;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointQueueBindingsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumer;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerOauthJwtClaim;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointRestConsumersResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnRestDeliveryPointsResponse;
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
public class RestDeliveryPointApi {
    private ApiClient apiClient;

    public RestDeliveryPointApi() {
        this(new ApiClient());
    }

    public RestDeliveryPointApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create a REST Delivery Point object.
     * Create a REST Delivery Point object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointResponse createMsgVpnRestDeliveryPoint(String msgVpnName, MsgVpnRestDeliveryPoint body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a REST Delivery Point object.
     * Create a REST Delivery Point object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointResponse> createMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, MsgVpnRestDeliveryPoint body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPoint");
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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Queue Binding object.
     * Create a Queue Binding object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| queueBindingName|x|x|||| restDeliveryPointName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse createMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointQueueBinding body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Queue Binding object.
     * Create a Queue Binding object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| queueBindingName|x|x|||| restDeliveryPointName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingResponse> createMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointQueueBinding body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Request Header object.
     * Create a Request Header object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A request header to be added to the HTTP request.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: headerName|x|x|||| msgVpnName|x||x||| queueBindingName|x||x||| restDeliveryPointName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param body The Request Header object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse createMsgVpnRestDeliveryPointQueueBindingRequestHeader(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBindingRequestHeader body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Request Header object.
     * Create a Request Header object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A request header to be added to the HTTP request.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: headerName|x|x|||| msgVpnName|x||x||| queueBindingName|x||x||| restDeliveryPointName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param body The Request Header object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> createMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBindingRequestHeader body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling createMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a REST Consumer object.
     * Create a REST Consumer object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authenticationAwsSecretAccessKey||||x||x authenticationClientCertContent||||x||x authenticationClientCertPassword||||x|| authenticationHttpBasicPassword||||x||x authenticationHttpHeaderValue||||x||x authenticationOauthClientSecret||||x||x authenticationOauthJwtSecretKey||||x||x msgVpnName|x||x||| restConsumerName|x|x|||| restDeliveryPointName|x||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationClientCertPassword|authenticationClientCertContent| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse createMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointRestConsumer body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a REST Consumer object.
     * Create a REST Consumer object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authenticationAwsSecretAccessKey||||x||x authenticationClientCertContent||||x||x authenticationClientCertPassword||||x|| authenticationHttpBasicPassword||||x||x authenticationHttpHeaderValue||||x||x authenticationOauthClientSecret||||x||x authenticationOauthJwtSecretKey||||x||x msgVpnName|x||x||| restConsumerName|x|x|||| restDeliveryPointName|x||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationClientCertPassword|authenticationClientCertContent| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerResponse> createMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointRestConsumer body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Claim object.
     * Create a Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Claim is added to the JWT sent to the OAuth token request endpoint.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| oauthJwtClaimName|x|x|||| oauthJwtClaimValue||x|||| restConsumerName|x||x||| restDeliveryPointName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The Claim object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The Claim object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerOauthJwtClaim body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Claim object.
     * Create a Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Claim is added to the JWT sent to the OAuth token request endpoint.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| oauthJwtClaimName|x|x|||| oauthJwtClaimValue||x|||| restConsumerName|x||x||| restDeliveryPointName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The Claim object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The Claim object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse> createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerOauthJwtClaim body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Trusted Common Name object.
     * Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||x| restConsumerName|x||x||x| restDeliveryPointName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Trusted Common Name object.
     * Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||x| restConsumerName|x||x||x| restDeliveryPointName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a REST Delivery Point object.
     * Delete a REST Delivery Point object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName) throws RestClientException {
        return deleteMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName).getBody();
    }

    /**
     * Delete a REST Delivery Point object.
     * Delete a REST Delivery Point object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPoint");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Queue Binding object.
     * Delete a Queue Binding object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName) throws RestClientException {
        return deleteMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName).getBody();
    }

    /**
     * Delete a Queue Binding object.
     * Delete a Queue Binding object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling deleteMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Request Header object.
     * Delete a Request Header object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A request header to be added to the HTTP request.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName) throws RestClientException {
        return deleteMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, headerName).getBody();
    }

    /**
     * Delete a Request Header object.
     * Delete a Request Header object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A request header to be added to the HTTP request.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'headerName' is set
        if (headerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headerName' when calling deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);
        uriVariables.put("headerName", headerName);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a REST Consumer object.
     * Delete a REST Consumer object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName) throws RestClientException {
        return deleteMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName).getBody();
    }

    /**
     * Delete a REST Consumer object.
     * Delete a REST Consumer object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling deleteMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Claim object.
     * Delete a Claim object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Claim is added to the JWT sent to the OAuth token request endpoint.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param oauthJwtClaimName The name of the additional claim. Cannot be \&quot;exp\&quot;, \&quot;iat\&quot;, or \&quot;jti\&quot;. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(String msgVpnName, String restDeliveryPointName, String restConsumerName, String oauthJwtClaimName) throws RestClientException {
        return deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, oauthJwtClaimName).getBody();
    }

    /**
     * Delete a Claim object.
     * Delete a Claim object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Claim is added to the JWT sent to the OAuth token request endpoint.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param oauthJwtClaimName The name of the additional claim. Cannot be \&quot;exp\&quot;, \&quot;iat\&quot;, or \&quot;jti\&quot;. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String oauthJwtClaimName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'oauthJwtClaimName' is set
        if (oauthJwtClaimName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthJwtClaimName' when calling deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);
        uriVariables.put("oauthJwtClaimName", oauthJwtClaimName);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Trusted Common Name object.
     * Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName) throws RestClientException {
        return deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName).getBody();
    }

    /**
     * Delete a Trusted Common Name object.
     * Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tlsTrustedCommonName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);
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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a REST Delivery Point object.
     * Get a REST Delivery Point object.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointResponse getMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName, opaquePassword, select).getBody();
    }

    /**
     * Get a REST Delivery Point object.
     * Get a REST Delivery Point object.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointResponse> getMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPoint");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Queue Binding object.
     * Get a Queue Binding object.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse getMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, opaquePassword, select).getBody();
    }

    /**
     * Get a Queue Binding object.
     * Get a Queue Binding object.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingResponse> getMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling getMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Request Header object.
     * Get a Request Header object.  A request header to be added to the HTTP request.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: headerName|x||| msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse getMsgVpnRestDeliveryPointQueueBindingRequestHeader(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, headerName, opaquePassword, select).getBody();
    }

    /**
     * Get a Request Header object.
     * Get a Request Header object.  A request header to be added to the HTTP request.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: headerName|x||| msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> getMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'headerName' is set
        if (headerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headerName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);
        uriVariables.put("headerName", headerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Request Header objects.
     * Get a list of Request Header objects.  A request header to be added to the HTTP request.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: headerName|x||| msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The list of Request Header objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse getMsgVpnRestDeliveryPointQueueBindingRequestHeaders(String msgVpnName, String restDeliveryPointName, String queueBindingName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointQueueBindingRequestHeadersWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Request Header objects.
     * Get a list of Request Header objects.  A request header to be added to the HTTP request.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: headerName|x||| msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The list of Request Header objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse> getMsgVpnRestDeliveryPointQueueBindingRequestHeadersWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeaders");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeaders");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling getMsgVpnRestDeliveryPointQueueBindingRequestHeaders");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Queue Binding objects.
     * Get a list of Queue Binding objects.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Queue Binding objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingsResponse getMsgVpnRestDeliveryPointQueueBindings(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointQueueBindingsWithHttpInfo(msgVpnName, restDeliveryPointName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Queue Binding objects.
     * Get a list of Queue Binding objects.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| queueBindingName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of Queue Binding objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingsResponse> getMsgVpnRestDeliveryPointQueueBindingsWithHttpInfo(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointQueueBindings");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointQueueBindings");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a REST Consumer object.
     * Get a REST Consumer object.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationAwsSecretAccessKey||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| authenticationHttpBasicPassword||x||x authenticationHttpHeaderValue||x||x authenticationOauthClientSecret||x||x authenticationOauthJwtSecretKey||x||x msgVpnName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse getMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, opaquePassword, select).getBody();
    }

    /**
     * Get a REST Consumer object.
     * Get a REST Consumer object.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationAwsSecretAccessKey||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| authenticationHttpBasicPassword||x||x authenticationHttpHeaderValue||x||x authenticationOauthClientSecret||x||x authenticationOauthJwtSecretKey||x||x msgVpnName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerResponse> getMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Claim object.
     * Get a Claim object.  A Claim is added to the JWT sent to the OAuth token request endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthJwtClaimName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The Claim object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param oauthJwtClaimName The name of the additional claim. Cannot be \&quot;exp\&quot;, \&quot;iat\&quot;, or \&quot;jti\&quot;. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim(String msgVpnName, String restDeliveryPointName, String restConsumerName, String oauthJwtClaimName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, oauthJwtClaimName, opaquePassword, select).getBody();
    }

    /**
     * Get a Claim object.
     * Get a Claim object.  A Claim is added to the JWT sent to the OAuth token request endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthJwtClaimName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The Claim object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param oauthJwtClaimName The name of the additional claim. Cannot be \&quot;exp\&quot;, \&quot;iat\&quot;, or \&quot;jti\&quot;. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse> getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String oauthJwtClaimName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // verify the required parameter 'oauthJwtClaimName' is set
        if (oauthJwtClaimName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthJwtClaimName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);
        uriVariables.put("oauthJwtClaimName", oauthJwtClaimName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Claim objects.
     * Get a list of Claim objects.  A Claim is added to the JWT sent to the OAuth token request endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthJwtClaimName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The list of Claim objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims(String msgVpnName, String restDeliveryPointName, String restConsumerName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Claim objects.
     * Get a list of Claim objects.  A Claim is added to the JWT sent to the OAuth token request endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthJwtClaimName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.21.
     * <p><b>200</b> - The list of Claim objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse> getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Trusted Common Name object.
     * Get a Trusted Common Name object.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||x| restConsumerName|x||x| restDeliveryPointName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName, opaquePassword, select).getBody();
    }

    /**
     * Get a Trusted Common Name object.
     * Get a Trusted Common Name object.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||x| restConsumerName|x||x| restDeliveryPointName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The Trusted Common Name object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'tlsTrustedCommonName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);
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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Trusted Common Name objects.
     * Get a list of Trusted Common Name objects.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||x| restConsumerName|x||x| restDeliveryPointName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The list of Trusted Common Name objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames(String msgVpnName, String restDeliveryPointName, String restConsumerName, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Trusted Common Name objects.
     * Get a list of Trusted Common Name objects.  The Trusted Common Names for the REST Consumer are used by encrypted transports to verify the name in the certificate presented by the remote REST consumer. They must include the common name of the remote REST consumer&#39;s server certificate.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||x| restConsumerName|x||x| restDeliveryPointName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since (will be deprecated in next SEMP version). Common Name validation has been replaced by Server Certificate Name validation.
     * <p><b>200</b> - The list of Trusted Common Name objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse> getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of REST Consumer objects.
     * Get a list of REST Consumer objects.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationAwsSecretAccessKey||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| authenticationHttpBasicPassword||x||x authenticationHttpHeaderValue||x||x authenticationOauthClientSecret||x||x authenticationOauthJwtSecretKey||x||x msgVpnName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of REST Consumer objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumersResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumersResponse getMsgVpnRestDeliveryPointRestConsumers(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointRestConsumersWithHttpInfo(msgVpnName, restDeliveryPointName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of REST Consumer objects.
     * Get a list of REST Consumer objects.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationAwsSecretAccessKey||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| authenticationHttpBasicPassword||x||x authenticationHttpHeaderValue||x||x authenticationOauthClientSecret||x||x authenticationOauthJwtSecretKey||x||x msgVpnName|x||| restConsumerName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of REST Consumer objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumersResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumersResponse> getMsgVpnRestDeliveryPointRestConsumersWithHttpInfo(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumers");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumers");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumersResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumersResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of REST Delivery Point objects.
     * Get a list of REST Delivery Point objects.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of REST Delivery Point objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointsResponse getMsgVpnRestDeliveryPoints(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnRestDeliveryPointsWithHttpInfo(msgVpnName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of REST Delivery Point objects.
     * Get a list of REST Delivery Point objects.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| restDeliveryPointName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The list of REST Delivery Point objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointsResponse> getMsgVpnRestDeliveryPointsWithHttpInfo(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPoints");
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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointsResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointsResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a REST Delivery Point object.
     * Replace a REST Delivery Point object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- clientProfileName|||||x|| msgVpnName|x||x|||| restDeliveryPointName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointResponse replaceMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a REST Delivery Point object.
     * Replace a REST Delivery Point object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- clientProfileName|||||x|| msgVpnName|x||x|||| restDeliveryPointName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointResponse> replaceMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPoint");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Queue Binding object.
     * Replace a Queue Binding object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| queueBindingName|x||x|||| restDeliveryPointName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse replaceMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Queue Binding object.
     * Replace a Queue Binding object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| queueBindingName|x||x|||| restDeliveryPointName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingResponse> replaceMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling replaceMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Request Header object.
     * Replace a Request Header object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A request header to be added to the HTTP request.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- headerName|x||x|||| msgVpnName|x||x|||| queueBindingName|x||x|||| restDeliveryPointName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @param body The Request Header object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName, MsgVpnRestDeliveryPointQueueBindingRequestHeader body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, headerName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Request Header object.
     * Replace a Request Header object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A request header to be added to the HTTP request.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- headerName|x||x|||| msgVpnName|x||x|||| queueBindingName|x||x|||| restDeliveryPointName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @param body The Request Header object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> replaceMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName, MsgVpnRestDeliveryPointQueueBindingRequestHeader body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'headerName' is set
        if (headerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headerName' when calling replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);
        uriVariables.put("headerName", headerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a REST Consumer object.
     * Replace a REST Consumer object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authenticationAwsSecretAccessKey||||x|||x authenticationClientCertContent||||x|x||x authenticationClientCertPassword||||x|x|| authenticationHttpBasicPassword||||x|x||x authenticationHttpBasicUsername|||||x|| authenticationHttpHeaderValue||||x|||x authenticationOauthClientId|||||x|| authenticationOauthClientScope|||||x|| authenticationOauthClientSecret||||x|x||x authenticationOauthClientTokenEndpoint|||||x|| authenticationOauthJwtSecretKey||||x|x||x authenticationOauthJwtTokenEndpoint|||||x|| authenticationScheme|||||x|| msgVpnName|x||x|||| outgoingConnectionCount|||||x|| remoteHost|||||x|| remotePort|||||x|| restConsumerName|x||x|||| restDeliveryPointName|x||x|||| tlsCipherSuiteList|||||x|| tlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationClientCertPassword|authenticationClientCertContent| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse replaceMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a REST Consumer object.
     * Replace a REST Consumer object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authenticationAwsSecretAccessKey||||x|||x authenticationClientCertContent||||x|x||x authenticationClientCertPassword||||x|x|| authenticationHttpBasicPassword||||x|x||x authenticationHttpBasicUsername|||||x|| authenticationHttpHeaderValue||||x|||x authenticationOauthClientId|||||x|| authenticationOauthClientScope|||||x|| authenticationOauthClientSecret||||x|x||x authenticationOauthClientTokenEndpoint|||||x|| authenticationOauthJwtSecretKey||||x|x||x authenticationOauthJwtTokenEndpoint|||||x|| authenticationScheme|||||x|| msgVpnName|x||x|||| outgoingConnectionCount|||||x|| remoteHost|||||x|| remotePort|||||x|| restConsumerName|x||x|||| restDeliveryPointName|x||x|||| tlsCipherSuiteList|||||x|| tlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationClientCertPassword|authenticationClientCertContent| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerResponse> replaceMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling replaceMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a REST Delivery Point object.
     * Update a REST Delivery Point object. Any attribute missing from the request will be left unchanged.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- clientProfileName||||x|| msgVpnName|x|x|||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointResponse updateMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a REST Delivery Point object.
     * Update a REST Delivery Point object. Any attribute missing from the request will be left unchanged.  A REST Delivery Point manages delivery of messages from queues to a named list of REST Consumers.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- clientProfileName||||x|| msgVpnName|x|x|||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Delivery Point object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointResponse> updateMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPoint");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPoint");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Queue Binding object.
     * Update a Queue Binding object. Any attribute missing from the request will be left unchanged.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| queueBindingName|x|x|||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse updateMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Queue Binding object.
     * Update a Queue Binding object. Any attribute missing from the request will be left unchanged.  A Queue Binding for a REST Delivery Point attracts messages to be delivered to REST consumers. If the queue does not exist it can be created subsequently, and once the queue is operational the broker performs the queue binding. Removing the queue binding does not delete the queue itself. Similarly, removing the queue does not remove the queue binding, which fails until the queue is recreated or the queue binding is deleted.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| queueBindingName|x|x|||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The Queue Binding object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingResponse> updateMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling updateMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPointQueueBinding");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Request Header object.
     * Update a Request Header object. Any attribute missing from the request will be left unchanged.  A request header to be added to the HTTP request.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- headerName|x|x|||| msgVpnName|x|x|||| queueBindingName|x|x|||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @param body The Request Header object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse updateMsgVpnRestDeliveryPointQueueBindingRequestHeader(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName, MsgVpnRestDeliveryPointQueueBindingRequestHeader body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, headerName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Request Header object.
     * Update a Request Header object. Any attribute missing from the request will be left unchanged.  A request header to be added to the HTTP request.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- headerName|x|x|||| msgVpnName|x|x|||| queueBindingName|x|x|||| restDeliveryPointName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.23.
     * <p><b>200</b> - The Request Header object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param queueBindingName The name of a queue in the Message VPN. (required)
     * @param headerName The name of the HTTP request header. (required)
     * @param body The Request Header object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> updateMsgVpnRestDeliveryPointQueueBindingRequestHeaderWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, String headerName, MsgVpnRestDeliveryPointQueueBindingRequestHeader body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'queueBindingName' when calling updateMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'headerName' is set
        if (headerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headerName' when calling updateMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPointQueueBindingRequestHeader");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("queueBindingName", queueBindingName);
        uriVariables.put("headerName", headerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a REST Consumer object.
     * Update a REST Consumer object. Any attribute missing from the request will be left unchanged.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authenticationAwsSecretAccessKey|||x|||x authenticationClientCertContent|||x|x||x authenticationClientCertPassword|||x|x|| authenticationHttpBasicPassword|||x|x||x authenticationHttpBasicUsername||||x|| authenticationHttpHeaderValue|||x|||x authenticationOauthClientId||||x|| authenticationOauthClientScope||||x|| authenticationOauthClientSecret|||x|x||x authenticationOauthClientTokenEndpoint||||x|| authenticationOauthJwtSecretKey|||x|x||x authenticationOauthJwtTokenEndpoint||||x|| authenticationScheme||||x|| msgVpnName|x|x|||| outgoingConnectionCount||||x|| remoteHost||||x|| remotePort||||x|| restConsumerName|x|x|||| restDeliveryPointName|x|x|||| tlsCipherSuiteList||||x|| tlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationClientCertPassword|authenticationClientCertContent| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse updateMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a REST Consumer object.
     * Update a REST Consumer object. Any attribute missing from the request will be left unchanged.  REST Consumer objects establish HTTP connectivity to REST consumer applications who wish to receive messages from a broker.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authenticationAwsSecretAccessKey|||x|||x authenticationClientCertContent|||x|x||x authenticationClientCertPassword|||x|x|| authenticationHttpBasicPassword|||x|x||x authenticationHttpBasicUsername||||x|| authenticationHttpHeaderValue|||x|||x authenticationOauthClientId||||x|| authenticationOauthClientScope||||x|| authenticationOauthClientSecret|||x|x||x authenticationOauthClientTokenEndpoint||||x|| authenticationOauthJwtSecretKey|||x|x||x authenticationOauthJwtTokenEndpoint||||x|| authenticationScheme||||x|| msgVpnName|x|x|||| outgoingConnectionCount||||x|| remoteHost||||x|| remotePort||||x|| restConsumerName|x|x|||| restDeliveryPointName|x|x|||| tlsCipherSuiteList||||x|| tlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationClientCertPassword|authenticationClientCertContent| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.
     * <p><b>200</b> - The REST Consumer object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param restDeliveryPointName The name of the REST Delivery Point. (required)
     * @param restConsumerName The name of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnRestDeliveryPointRestConsumerResponse> updateMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'restConsumerName' when calling updateMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPointRestConsumer");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("restDeliveryPointName", restDeliveryPointName);
        uriVariables.put("restConsumerName", restConsumerName);

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

        ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse> localReturnType = new ParameterizedTypeReference<MsgVpnRestDeliveryPointRestConsumerResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
