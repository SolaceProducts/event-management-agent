package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttRetainCache;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttRetainCacheResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.MsgVpnMqttRetainCachesResponse;
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
public class MqttRetainCacheApi {
    private ApiClient apiClient;

    public MqttRetainCacheApi() {
        this(new ApiClient());
    }

    public MqttRetainCacheApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create an MQTT Retain Cache object.
     * Create an MQTT Retain Cache object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: cacheName|x|x|||| msgVpnName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The MQTT Retain Cache object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttRetainCacheResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttRetainCacheResponse createMsgVpnMqttRetainCache(String msgVpnName, MsgVpnMqttRetainCache body, String opaquePassword, List<String> select) throws RestClientException {
        return createMsgVpnMqttRetainCacheWithHttpInfo(msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Create an MQTT Retain Cache object.
     * Create an MQTT Retain Cache object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: cacheName|x|x|||| msgVpnName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param body The MQTT Retain Cache object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttRetainCacheResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttRetainCacheResponse> createMsgVpnMqttRetainCacheWithHttpInfo(String msgVpnName, MsgVpnMqttRetainCache body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling createMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createMsgVpnMqttRetainCache");
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

        ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttRetainCaches", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete an MQTT Retain Cache object.
     * Delete an MQTT Retain Cache object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteMsgVpnMqttRetainCache(String msgVpnName, String cacheName) throws RestClientException {
        return deleteMsgVpnMqttRetainCacheWithHttpInfo(msgVpnName, cacheName).getBody();
    }

    /**
     * Delete an MQTT Retain Cache object.
     * Delete an MQTT Retain Cache object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteMsgVpnMqttRetainCacheWithHttpInfo(String msgVpnName, String cacheName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'cacheName' is set
        if (cacheName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'cacheName' when calling deleteMsgVpnMqttRetainCache");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("cacheName", cacheName);

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
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get an MQTT Retain Cache object.
     * Get an MQTT Retain Cache object.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: cacheName|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttRetainCacheResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttRetainCacheResponse getMsgVpnMqttRetainCache(String msgVpnName, String cacheName, String opaquePassword, List<String> select) throws RestClientException {
        return getMsgVpnMqttRetainCacheWithHttpInfo(msgVpnName, cacheName, opaquePassword, select).getBody();
    }

    /**
     * Get an MQTT Retain Cache object.
     * Get an MQTT Retain Cache object.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: cacheName|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttRetainCacheResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttRetainCacheResponse> getMsgVpnMqttRetainCacheWithHttpInfo(String msgVpnName, String cacheName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'cacheName' is set
        if (cacheName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'cacheName' when calling getMsgVpnMqttRetainCache");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("cacheName", cacheName);

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

        ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of MQTT Retain Cache objects.
     * Get a list of MQTT Retain Cache objects.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: cacheName|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The list of MQTT Retain Cache objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttRetainCachesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttRetainCachesResponse getMsgVpnMqttRetainCaches(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getMsgVpnMqttRetainCachesWithHttpInfo(msgVpnName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of MQTT Retain Cache objects.
     * Get a list of MQTT Retain Cache objects.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: cacheName|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The list of MQTT Retain Cache objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttRetainCachesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttRetainCachesResponse> getMsgVpnMqttRetainCachesWithHttpInfo(String msgVpnName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttRetainCaches");
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

        ParameterizedTypeReference<MsgVpnMqttRetainCachesResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttRetainCachesResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttRetainCaches", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace an MQTT Retain Cache object.
     * Replace an MQTT Retain Cache object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- cacheName|x||x|||| msgVpnName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @param body The MQTT Retain Cache object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttRetainCacheResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttRetainCacheResponse replaceMsgVpnMqttRetainCache(String msgVpnName, String cacheName, MsgVpnMqttRetainCache body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceMsgVpnMqttRetainCacheWithHttpInfo(msgVpnName, cacheName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace an MQTT Retain Cache object.
     * Replace an MQTT Retain Cache object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- cacheName|x||x|||| msgVpnName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @param body The MQTT Retain Cache object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttRetainCacheResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttRetainCacheResponse> replaceMsgVpnMqttRetainCacheWithHttpInfo(String msgVpnName, String cacheName, MsgVpnMqttRetainCache body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'cacheName' is set
        if (cacheName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'cacheName' when calling replaceMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceMsgVpnMqttRetainCache");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("cacheName", cacheName);

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

        ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update an MQTT Retain Cache object.
     * Update an MQTT Retain Cache object. Any attribute missing from the request will be left unchanged.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- cacheName|x|x|||| msgVpnName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @param body The MQTT Retain Cache object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return MsgVpnMqttRetainCacheResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public MsgVpnMqttRetainCacheResponse updateMsgVpnMqttRetainCache(String msgVpnName, String cacheName, MsgVpnMqttRetainCache body, String opaquePassword, List<String> select) throws RestClientException {
        return updateMsgVpnMqttRetainCacheWithHttpInfo(msgVpnName, cacheName, body, opaquePassword, select).getBody();
    }

    /**
     * Update an MQTT Retain Cache object.
     * Update an MQTT Retain Cache object. Any attribute missing from the request will be left unchanged.  Using MQTT retained messages allows publishing MQTT clients to indicate that a message must be stored for later delivery to subscribing clients when those subscribing clients add subscriptions matching the retained message&#39;s topic. An MQTT Retain Cache processes all retained messages for a Message VPN.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- cacheName|x|x|||| msgVpnName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     * <p><b>200</b> - The MQTT Retain Cache object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param msgVpnName The name of the Message VPN. (required)
     * @param cacheName The name of the MQTT Retain Cache. (required)
     * @param body The MQTT Retain Cache object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;MsgVpnMqttRetainCacheResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<MsgVpnMqttRetainCacheResponse> updateMsgVpnMqttRetainCacheWithHttpInfo(String msgVpnName, String cacheName, MsgVpnMqttRetainCache body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'cacheName' is set
        if (cacheName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'cacheName' when calling updateMsgVpnMqttRetainCache");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateMsgVpnMqttRetainCache");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("msgVpnName", msgVpnName);
        uriVariables.put("cacheName", cacheName);

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

        ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse> localReturnType = new ParameterizedTypeReference<MsgVpnMqttRetainCacheResponse>() {};
        return apiClient.invokeAPI("/msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
