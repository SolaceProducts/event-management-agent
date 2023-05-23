package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.ClientCertAuthoritiesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.ClientCertAuthority;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.ClientCertAuthorityOcspTlsTrustedCommonName;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.ClientCertAuthorityOcspTlsTrustedCommonNameResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.ClientCertAuthorityOcspTlsTrustedCommonNamesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.ClientCertAuthorityResponse;
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
public class ClientCertAuthorityApi {
    private ApiClient apiClient;

    public ClientCertAuthorityApi() {
        this(new ApiClient());
    }

    public ClientCertAuthorityApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create a Client Certificate Authority object.
     * Create a Client Certificate Authority object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- ClientCertAuthority|crlDayList|crlTimeList| ClientCertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param body The Client Certificate Authority object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityResponse createClientCertAuthority(ClientCertAuthority body, String opaquePassword, List<String> select) throws RestClientException {
        return createClientCertAuthorityWithHttpInfo(body, opaquePassword, select).getBody();
    }

    /**
     * Create a Client Certificate Authority object.
     * Create a Client Certificate Authority object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- ClientCertAuthority|crlDayList|crlTimeList| ClientCertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param body The Client Certificate Authority object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityResponse> createClientCertAuthorityWithHttpInfo(ClientCertAuthority body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createClientCertAuthority");
        }
        

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

        ParameterizedTypeReference<ClientCertAuthorityResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities", HttpMethod.POST, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create an OCSP Responder Trusted Common Name object.
     * Create an OCSP Responder Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x||x||| ocspTlsTrustedCommonName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The OCSP Responder Trusted Common Name object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param body The OCSP Responder Trusted Common Name object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityOcspTlsTrustedCommonNameResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityOcspTlsTrustedCommonNameResponse createClientCertAuthorityOcspTlsTrustedCommonName(String certAuthorityName, ClientCertAuthorityOcspTlsTrustedCommonName body, String opaquePassword, List<String> select) throws RestClientException {
        return createClientCertAuthorityOcspTlsTrustedCommonNameWithHttpInfo(certAuthorityName, body, opaquePassword, select).getBody();
    }

    /**
     * Create an OCSP Responder Trusted Common Name object.
     * Create an OCSP Responder Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x||x||| ocspTlsTrustedCommonName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The OCSP Responder Trusted Common Name object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param body The OCSP Responder Trusted Common Name object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityOcspTlsTrustedCommonNameResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityOcspTlsTrustedCommonNameResponse> createClientCertAuthorityOcspTlsTrustedCommonNameWithHttpInfo(String certAuthorityName, ClientCertAuthorityOcspTlsTrustedCommonName body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling createClientCertAuthorityOcspTlsTrustedCommonName");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createClientCertAuthorityOcspTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);

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

        ParameterizedTypeReference<ClientCertAuthorityOcspTlsTrustedCommonNameResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityOcspTlsTrustedCommonNameResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Client Certificate Authority object.
     * Delete a Client Certificate Authority object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteClientCertAuthority(String certAuthorityName) throws RestClientException {
        return deleteClientCertAuthorityWithHttpInfo(certAuthorityName).getBody();
    }

    /**
     * Delete a Client Certificate Authority object.
     * Delete a Client Certificate Authority object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteClientCertAuthorityWithHttpInfo(String certAuthorityName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling deleteClientCertAuthority");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);

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
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete an OCSP Responder Trusted Common Name object.
     * Delete an OCSP Responder Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates via config-sync.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param ocspTlsTrustedCommonName The expected Trusted Common Name of the OCSP responder remote certificate. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteClientCertAuthorityOcspTlsTrustedCommonName(String certAuthorityName, String ocspTlsTrustedCommonName) throws RestClientException {
        return deleteClientCertAuthorityOcspTlsTrustedCommonNameWithHttpInfo(certAuthorityName, ocspTlsTrustedCommonName).getBody();
    }

    /**
     * Delete an OCSP Responder Trusted Common Name object.
     * Delete an OCSP Responder Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates via config-sync.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param ocspTlsTrustedCommonName The expected Trusted Common Name of the OCSP responder remote certificate. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteClientCertAuthorityOcspTlsTrustedCommonNameWithHttpInfo(String certAuthorityName, String ocspTlsTrustedCommonName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling deleteClientCertAuthorityOcspTlsTrustedCommonName");
        }
        
        // verify the required parameter 'ocspTlsTrustedCommonName' is set
        if (ocspTlsTrustedCommonName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ocspTlsTrustedCommonName' when calling deleteClientCertAuthorityOcspTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);
        uriVariables.put("ocspTlsTrustedCommonName", ocspTlsTrustedCommonName);

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
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Client Certificate Authority objects.
     * Get a list of Client Certificate Authority objects.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The list of Client Certificate Authority objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthoritiesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthoritiesResponse getClientCertAuthorities(Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getClientCertAuthoritiesWithHttpInfo(count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Client Certificate Authority objects.
     * Get a list of Client Certificate Authority objects.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The list of Client Certificate Authority objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthoritiesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthoritiesResponse> getClientCertAuthoritiesWithHttpInfo(Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        

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

        ParameterizedTypeReference<ClientCertAuthoritiesResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthoritiesResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Client Certificate Authority object.
     * Get a Client Certificate Authority object.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityResponse getClientCertAuthority(String certAuthorityName, String opaquePassword, List<String> select) throws RestClientException {
        return getClientCertAuthorityWithHttpInfo(certAuthorityName, opaquePassword, select).getBody();
    }

    /**
     * Get a Client Certificate Authority object.
     * Get a Client Certificate Authority object.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityResponse> getClientCertAuthorityWithHttpInfo(String certAuthorityName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling getClientCertAuthority");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);

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

        ParameterizedTypeReference<ClientCertAuthorityResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get an OCSP Responder Trusted Common Name object.
     * Get an OCSP Responder Trusted Common Name object.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||| ocspTlsTrustedCommonName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The OCSP Responder Trusted Common Name object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param ocspTlsTrustedCommonName The expected Trusted Common Name of the OCSP responder remote certificate. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityOcspTlsTrustedCommonNameResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityOcspTlsTrustedCommonNameResponse getClientCertAuthorityOcspTlsTrustedCommonName(String certAuthorityName, String ocspTlsTrustedCommonName, String opaquePassword, List<String> select) throws RestClientException {
        return getClientCertAuthorityOcspTlsTrustedCommonNameWithHttpInfo(certAuthorityName, ocspTlsTrustedCommonName, opaquePassword, select).getBody();
    }

    /**
     * Get an OCSP Responder Trusted Common Name object.
     * Get an OCSP Responder Trusted Common Name object.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||| ocspTlsTrustedCommonName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The OCSP Responder Trusted Common Name object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param ocspTlsTrustedCommonName The expected Trusted Common Name of the OCSP responder remote certificate. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityOcspTlsTrustedCommonNameResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityOcspTlsTrustedCommonNameResponse> getClientCertAuthorityOcspTlsTrustedCommonNameWithHttpInfo(String certAuthorityName, String ocspTlsTrustedCommonName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling getClientCertAuthorityOcspTlsTrustedCommonName");
        }
        
        // verify the required parameter 'ocspTlsTrustedCommonName' is set
        if (ocspTlsTrustedCommonName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'ocspTlsTrustedCommonName' when calling getClientCertAuthorityOcspTlsTrustedCommonName");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);
        uriVariables.put("ocspTlsTrustedCommonName", ocspTlsTrustedCommonName);

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

        ParameterizedTypeReference<ClientCertAuthorityOcspTlsTrustedCommonNameResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityOcspTlsTrustedCommonNameResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of OCSP Responder Trusted Common Name objects.
     * Get a list of OCSP Responder Trusted Common Name objects.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||| ocspTlsTrustedCommonName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The list of OCSP Responder Trusted Common Name objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityOcspTlsTrustedCommonNamesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityOcspTlsTrustedCommonNamesResponse getClientCertAuthorityOcspTlsTrustedCommonNames(String certAuthorityName, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getClientCertAuthorityOcspTlsTrustedCommonNamesWithHttpInfo(certAuthorityName, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of OCSP Responder Trusted Common Name objects.
     * Get a list of OCSP Responder Trusted Common Name objects.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||| ocspTlsTrustedCommonName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The list of OCSP Responder Trusted Common Name objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityOcspTlsTrustedCommonNamesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityOcspTlsTrustedCommonNamesResponse> getClientCertAuthorityOcspTlsTrustedCommonNamesWithHttpInfo(String certAuthorityName, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling getClientCertAuthorityOcspTlsTrustedCommonNames");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);

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

        ParameterizedTypeReference<ClientCertAuthorityOcspTlsTrustedCommonNamesResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityOcspTlsTrustedCommonNamesResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Client Certificate Authority object.
     * Replace a Client Certificate Authority object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- certAuthorityName|x||x|||| crlUrl|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- ClientCertAuthority|crlDayList|crlTimeList| ClientCertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param body The Client Certificate Authority object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityResponse replaceClientCertAuthority(String certAuthorityName, ClientCertAuthority body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceClientCertAuthorityWithHttpInfo(certAuthorityName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Client Certificate Authority object.
     * Replace a Client Certificate Authority object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- certAuthorityName|x||x|||| crlUrl|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- ClientCertAuthority|crlDayList|crlTimeList| ClientCertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param body The Client Certificate Authority object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityResponse> replaceClientCertAuthorityWithHttpInfo(String certAuthorityName, ClientCertAuthority body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling replaceClientCertAuthority");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceClientCertAuthority");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);

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

        ParameterizedTypeReference<ClientCertAuthorityResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Client Certificate Authority object.
     * Update a Client Certificate Authority object. Any attribute missing from the request will be left unchanged.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- certAuthorityName|x|x|||| crlUrl||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- ClientCertAuthority|crlDayList|crlTimeList| ClientCertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param body The Client Certificate Authority object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ClientCertAuthorityResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ClientCertAuthorityResponse updateClientCertAuthority(String certAuthorityName, ClientCertAuthority body, String opaquePassword, List<String> select) throws RestClientException {
        return updateClientCertAuthorityWithHttpInfo(certAuthorityName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Client Certificate Authority object.
     * Update a Client Certificate Authority object. Any attribute missing from the request will be left unchanged.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- certAuthorityName|x|x|||| crlUrl||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- ClientCertAuthority|crlDayList|crlTimeList| ClientCertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.
     * <p><b>200</b> - The Client Certificate Authority object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param certAuthorityName The name of the Certificate Authority. (required)
     * @param body The Client Certificate Authority object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;ClientCertAuthorityResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ClientCertAuthorityResponse> updateClientCertAuthorityWithHttpInfo(String certAuthorityName, ClientCertAuthority body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'certAuthorityName' is set
        if (certAuthorityName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'certAuthorityName' when calling updateClientCertAuthority");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateClientCertAuthority");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("certAuthorityName", certAuthorityName);

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

        ParameterizedTypeReference<ClientCertAuthorityResponse> localReturnType = new ParameterizedTypeReference<ClientCertAuthorityResponse>() {};
        return apiClient.invokeAPI("/clientCertAuthorities/{certAuthorityName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
