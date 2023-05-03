package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroup;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupMsgVpnAccessLevelException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAllowedHost;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAllowedHostResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAllowedHostsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAuthorizationParameter;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAuthorizationParameterResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAuthorizationParametersResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientRequiredClaim;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientRequiredClaimResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientRequiredClaimsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileDefaultMsgVpnAccessLevelException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileDefaultMsgVpnAccessLevelExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResourceServerRequiredClaim;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResourceServerRequiredClaimResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResourceServerRequiredClaimsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfilesResponse;
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

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class OauthProfileApi {
    private ApiClient apiClient;

    public OauthProfileApi() {
        this(new ApiClient());
    }

    public OauthProfileApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create an OAuth Profile object.
     * Create an OAuth Profile object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientSecret||||x||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param body The OAuth Profile object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResponse createOauthProfile(OauthProfile body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileWithHttpInfo(body, opaquePassword, select).getBody();
    }

    /**
     * Create an OAuth Profile object.
     * Create an OAuth Profile object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientSecret||||x||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param body The OAuth Profile object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResponse> createOauthProfileWithHttpInfo(OauthProfile body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfile");
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

        ParameterizedTypeReference<OauthProfileResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles", HttpMethod.POST, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Group Access Level object.
     * Create a Group Access Level object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Group Access Level object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupResponse createOauthProfileAccessLevelGroup(String oauthProfileName, OauthProfileAccessLevelGroup body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileAccessLevelGroupWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Group Access Level object.
     * Create a Group Access Level object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Group Access Level object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupResponse> createOauthProfileAccessLevelGroupWithHttpInfo(String oauthProfileName, OauthProfileAccessLevelGroup body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileAccessLevelGroup");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Message VPN Access-Level Exception object.
     * Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x||x||| msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse createOauthProfileAccessLevelGroupMsgVpnAccessLevelException(String oauthProfileName, String groupName, OauthProfileAccessLevelGroupMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, groupName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Message VPN Access-Level Exception object.
     * Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x||x||| msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> createOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String groupName, OauthProfileAccessLevelGroupMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling createOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create an Allowed Host Value object.
     * Create an Allowed Host Value object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: allowedHost|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Allowed Host Value object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Allowed Host Value object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAllowedHostResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAllowedHostResponse createOauthProfileClientAllowedHost(String oauthProfileName, OauthProfileClientAllowedHost body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileClientAllowedHostWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Create an Allowed Host Value object.
     * Create an Allowed Host Value object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: allowedHost|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Allowed Host Value object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Allowed Host Value object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAllowedHostResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAllowedHostResponse> createOauthProfileClientAllowedHostWithHttpInfo(String oauthProfileName, OauthProfileClientAllowedHost body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileClientAllowedHost");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileClientAllowedHost");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileClientAllowedHostResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAllowedHostResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAllowedHosts", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create an Authorization Parameter object.
     * Create an Authorization Parameter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authorizationParameterName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Authorization Parameter object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAuthorizationParameterResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAuthorizationParameterResponse createOauthProfileClientAuthorizationParameter(String oauthProfileName, OauthProfileClientAuthorizationParameter body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileClientAuthorizationParameterWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Create an Authorization Parameter object.
     * Create an Authorization Parameter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authorizationParameterName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Authorization Parameter object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAuthorizationParameterResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAuthorizationParameterResponse> createOauthProfileClientAuthorizationParameterWithHttpInfo(String oauthProfileName, OauthProfileClientAuthorizationParameter body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileClientAuthorizationParameter");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAuthorizationParameters", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Required Claim object.
     * Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientRequiredClaimName|x|x|||| clientRequiredClaimValue||x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Required Claim object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientRequiredClaimResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientRequiredClaimResponse createOauthProfileClientRequiredClaim(String oauthProfileName, OauthProfileClientRequiredClaim body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileClientRequiredClaimWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Required Claim object.
     * Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientRequiredClaimName|x|x|||| clientRequiredClaimValue||x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Required Claim object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientRequiredClaimResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientRequiredClaimResponse> createOauthProfileClientRequiredClaimWithHttpInfo(String oauthProfileName, OauthProfileClientRequiredClaim body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileClientRequiredClaim");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileClientRequiredClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileClientRequiredClaimResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientRequiredClaimResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientRequiredClaims", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Message VPN Access-Level Exception object.
     * Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileDefaultMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileDefaultMsgVpnAccessLevelExceptionResponse createOauthProfileDefaultMsgVpnAccessLevelException(String oauthProfileName, OauthProfileDefaultMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Message VPN Access-Level Exception object.
     * Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileDefaultMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> createOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, OauthProfileDefaultMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Create a Required Claim object.
     * Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: oauthProfileName|x||x||| resourceServerRequiredClaimName|x|x|||| resourceServerRequiredClaimValue||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Required Claim object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResourceServerRequiredClaimResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResourceServerRequiredClaimResponse createOauthProfileResourceServerRequiredClaim(String oauthProfileName, OauthProfileResourceServerRequiredClaim body, String opaquePassword, List<String> select) throws RestClientException {
        return createOauthProfileResourceServerRequiredClaimWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Create a Required Claim object.
     * Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: oauthProfileName|x||x||| resourceServerRequiredClaimName|x|x|||| resourceServerRequiredClaimValue||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes after being created, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The Required Claim object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResourceServerRequiredClaimResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResourceServerRequiredClaimResponse> createOauthProfileResourceServerRequiredClaimWithHttpInfo(String oauthProfileName, OauthProfileResourceServerRequiredClaim body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling createOauthProfileResourceServerRequiredClaim");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling createOauthProfileResourceServerRequiredClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileResourceServerRequiredClaimResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResourceServerRequiredClaimResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims", HttpMethod.POST, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete an OAuth Profile object.
     * Delete an OAuth Profile object. The deletion of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfile(String oauthProfileName) throws RestClientException {
        return deleteOauthProfileWithHttpInfo(oauthProfileName).getBody();
    }

    /**
     * Delete an OAuth Profile object.
     * Delete an OAuth Profile object. The deletion of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileWithHttpInfo(String oauthProfileName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfile");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Group Access Level object.
     * Delete a Group Access Level object. The deletion of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileAccessLevelGroup(String oauthProfileName, String groupName) throws RestClientException {
        return deleteOauthProfileAccessLevelGroupWithHttpInfo(oauthProfileName, groupName).getBody();
    }

    /**
     * Delete a Group Access Level object.
     * Delete a Group Access Level object. The deletion of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileAccessLevelGroupWithHttpInfo(String oauthProfileName, String groupName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling deleteOauthProfileAccessLevelGroup");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Message VPN Access-Level Exception object.
     * Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException(String oauthProfileName, String groupName, String msgVpnName) throws RestClientException {
        return deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, groupName, msgVpnName).getBody();
    }

    /**
     * Delete a Message VPN Access-Level Exception object.
     * Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String groupName, String msgVpnName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);
        uriVariables.put("msgVpnName", msgVpnName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete an Allowed Host Value object.
     * Delete an Allowed Host Value object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param allowedHost An allowed value for the Host header. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileClientAllowedHost(String oauthProfileName, String allowedHost) throws RestClientException {
        return deleteOauthProfileClientAllowedHostWithHttpInfo(oauthProfileName, allowedHost).getBody();
    }

    /**
     * Delete an Allowed Host Value object.
     * Delete an Allowed Host Value object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param allowedHost An allowed value for the Host header. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileClientAllowedHostWithHttpInfo(String oauthProfileName, String allowedHost) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileClientAllowedHost");
        }
        
        // verify the required parameter 'allowedHost' is set
        if (allowedHost == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'allowedHost' when calling deleteOauthProfileClientAllowedHost");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("allowedHost", allowedHost);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete an Authorization Parameter object.
     * Delete an Authorization Parameter object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileClientAuthorizationParameter(String oauthProfileName, String authorizationParameterName) throws RestClientException {
        return deleteOauthProfileClientAuthorizationParameterWithHttpInfo(oauthProfileName, authorizationParameterName).getBody();
    }

    /**
     * Delete an Authorization Parameter object.
     * Delete an Authorization Parameter object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileClientAuthorizationParameterWithHttpInfo(String oauthProfileName, String authorizationParameterName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'authorizationParameterName' is set
        if (authorizationParameterName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'authorizationParameterName' when calling deleteOauthProfileClientAuthorizationParameter");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("authorizationParameterName", authorizationParameterName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Required Claim object.
     * Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param clientRequiredClaimName The name of the ID token claim to verify. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileClientRequiredClaim(String oauthProfileName, String clientRequiredClaimName) throws RestClientException {
        return deleteOauthProfileClientRequiredClaimWithHttpInfo(oauthProfileName, clientRequiredClaimName).getBody();
    }

    /**
     * Delete a Required Claim object.
     * Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param clientRequiredClaimName The name of the ID token claim to verify. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileClientRequiredClaimWithHttpInfo(String oauthProfileName, String clientRequiredClaimName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileClientRequiredClaim");
        }
        
        // verify the required parameter 'clientRequiredClaimName' is set
        if (clientRequiredClaimName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'clientRequiredClaimName' when calling deleteOauthProfileClientRequiredClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("clientRequiredClaimName", clientRequiredClaimName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Message VPN Access-Level Exception object.
     * Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileDefaultMsgVpnAccessLevelException(String oauthProfileName, String msgVpnName) throws RestClientException {
        return deleteOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, msgVpnName).getBody();
    }

    /**
     * Delete a Message VPN Access-Level Exception object.
     * Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String msgVpnName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling deleteOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("msgVpnName", msgVpnName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Delete a Required Claim object.
     * Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param resourceServerRequiredClaimName The name of the access token claim to verify. (required)
     * @return SempMetaOnlyResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SempMetaOnlyResponse deleteOauthProfileResourceServerRequiredClaim(String oauthProfileName, String resourceServerRequiredClaimName) throws RestClientException {
        return deleteOauthProfileResourceServerRequiredClaimWithHttpInfo(oauthProfileName, resourceServerRequiredClaimName).getBody();
    }

    /**
     * Delete a Required Claim object.
     * Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param resourceServerRequiredClaimName The name of the access token claim to verify. (required)
     * @return ResponseEntity&lt;SempMetaOnlyResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<SempMetaOnlyResponse> deleteOauthProfileResourceServerRequiredClaimWithHttpInfo(String oauthProfileName, String resourceServerRequiredClaimName) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling deleteOauthProfileResourceServerRequiredClaim");
        }
        
        // verify the required parameter 'resourceServerRequiredClaimName' is set
        if (resourceServerRequiredClaimName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'resourceServerRequiredClaimName' when calling deleteOauthProfileResourceServerRequiredClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("resourceServerRequiredClaimName", resourceServerRequiredClaimName);

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
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName}", HttpMethod.DELETE, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get an OAuth Profile object.
     * Get an OAuth Profile object.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResponse getOauthProfile(String oauthProfileName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileWithHttpInfo(oauthProfileName, opaquePassword, select).getBody();
    }

    /**
     * Get an OAuth Profile object.
     * Get an OAuth Profile object.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResponse> getOauthProfileWithHttpInfo(String oauthProfileName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfile");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Group Access Level object.
     * Get a Group Access Level object.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupResponse getOauthProfileAccessLevelGroup(String oauthProfileName, String groupName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileAccessLevelGroupWithHttpInfo(oauthProfileName, groupName, opaquePassword, select).getBody();
    }

    /**
     * Get a Group Access Level object.
     * Get a Group Access Level object.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupResponse> getOauthProfileAccessLevelGroupWithHttpInfo(String oauthProfileName, String groupName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling getOauthProfileAccessLevelGroup");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Message VPN Access-Level Exception object.
     * Get a Message VPN Access-Level Exception object.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse getOauthProfileAccessLevelGroupMsgVpnAccessLevelException(String oauthProfileName, String groupName, String msgVpnName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, groupName, msgVpnName, opaquePassword, select).getBody();
    }

    /**
     * Get a Message VPN Access-Level Exception object.
     * Get a Message VPN Access-Level Exception object.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String groupName, String msgVpnName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling getOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);
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
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Message VPN Access-Level Exception objects.
     * Get a list of Message VPN Access-Level Exception objects.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Message VPN Access-Level Exception objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions(String oauthProfileName, String groupName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsWithHttpInfo(oauthProfileName, groupName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Message VPN Access-Level Exception objects.
     * Get a list of Message VPN Access-Level Exception objects.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Message VPN Access-Level Exception objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse> getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsWithHttpInfo(String oauthProfileName, String groupName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Group Access Level objects.
     * Get a list of Group Access Level objects.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Group Access Level objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupsResponse getOauthProfileAccessLevelGroups(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileAccessLevelGroupsWithHttpInfo(oauthProfileName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Group Access Level objects.
     * Get a list of Group Access Level objects.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Group Access Level objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupsResponse> getOauthProfileAccessLevelGroupsWithHttpInfo(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileAccessLevelGroups");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupsResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupsResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get an Allowed Host Value object.
     * Get an Allowed Host Value object.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Allowed Host Value object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param allowedHost An allowed value for the Host header. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAllowedHostResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAllowedHostResponse getOauthProfileClientAllowedHost(String oauthProfileName, String allowedHost, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileClientAllowedHostWithHttpInfo(oauthProfileName, allowedHost, opaquePassword, select).getBody();
    }

    /**
     * Get an Allowed Host Value object.
     * Get an Allowed Host Value object.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Allowed Host Value object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param allowedHost An allowed value for the Host header. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAllowedHostResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAllowedHostResponse> getOauthProfileClientAllowedHostWithHttpInfo(String oauthProfileName, String allowedHost, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileClientAllowedHost");
        }
        
        // verify the required parameter 'allowedHost' is set
        if (allowedHost == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'allowedHost' when calling getOauthProfileClientAllowedHost");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("allowedHost", allowedHost);

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

        ParameterizedTypeReference<OauthProfileClientAllowedHostResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAllowedHostResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Allowed Host Value objects.
     * Get a list of Allowed Host Value objects.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Allowed Host Value objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAllowedHostsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAllowedHostsResponse getOauthProfileClientAllowedHosts(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileClientAllowedHostsWithHttpInfo(oauthProfileName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Allowed Host Value objects.
     * Get a list of Allowed Host Value objects.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Allowed Host Value objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAllowedHostsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAllowedHostsResponse> getOauthProfileClientAllowedHostsWithHttpInfo(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileClientAllowedHosts");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileClientAllowedHostsResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAllowedHostsResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAllowedHosts", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get an Authorization Parameter object.
     * Get an Authorization Parameter object.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAuthorizationParameterResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAuthorizationParameterResponse getOauthProfileClientAuthorizationParameter(String oauthProfileName, String authorizationParameterName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileClientAuthorizationParameterWithHttpInfo(oauthProfileName, authorizationParameterName, opaquePassword, select).getBody();
    }

    /**
     * Get an Authorization Parameter object.
     * Get an Authorization Parameter object.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAuthorizationParameterResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAuthorizationParameterResponse> getOauthProfileClientAuthorizationParameterWithHttpInfo(String oauthProfileName, String authorizationParameterName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'authorizationParameterName' is set
        if (authorizationParameterName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'authorizationParameterName' when calling getOauthProfileClientAuthorizationParameter");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("authorizationParameterName", authorizationParameterName);

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

        ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Authorization Parameter objects.
     * Get a list of Authorization Parameter objects.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Authorization Parameter objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAuthorizationParametersResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAuthorizationParametersResponse getOauthProfileClientAuthorizationParameters(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileClientAuthorizationParametersWithHttpInfo(oauthProfileName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Authorization Parameter objects.
     * Get a list of Authorization Parameter objects.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Authorization Parameter objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAuthorizationParametersResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAuthorizationParametersResponse> getOauthProfileClientAuthorizationParametersWithHttpInfo(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileClientAuthorizationParameters");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileClientAuthorizationParametersResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAuthorizationParametersResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAuthorizationParameters", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Required Claim object.
     * Get a Required Claim object.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param clientRequiredClaimName The name of the ID token claim to verify. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientRequiredClaimResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientRequiredClaimResponse getOauthProfileClientRequiredClaim(String oauthProfileName, String clientRequiredClaimName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileClientRequiredClaimWithHttpInfo(oauthProfileName, clientRequiredClaimName, opaquePassword, select).getBody();
    }

    /**
     * Get a Required Claim object.
     * Get a Required Claim object.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param clientRequiredClaimName The name of the ID token claim to verify. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientRequiredClaimResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientRequiredClaimResponse> getOauthProfileClientRequiredClaimWithHttpInfo(String oauthProfileName, String clientRequiredClaimName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileClientRequiredClaim");
        }
        
        // verify the required parameter 'clientRequiredClaimName' is set
        if (clientRequiredClaimName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'clientRequiredClaimName' when calling getOauthProfileClientRequiredClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("clientRequiredClaimName", clientRequiredClaimName);

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

        ParameterizedTypeReference<OauthProfileClientRequiredClaimResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientRequiredClaimResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Required Claim objects.
     * Get a list of Required Claim objects.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Required Claim objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientRequiredClaimsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientRequiredClaimsResponse getOauthProfileClientRequiredClaims(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileClientRequiredClaimsWithHttpInfo(oauthProfileName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Required Claim objects.
     * Get a list of Required Claim objects.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Required Claim objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientRequiredClaimsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientRequiredClaimsResponse> getOauthProfileClientRequiredClaimsWithHttpInfo(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileClientRequiredClaims");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileClientRequiredClaimsResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientRequiredClaimsResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientRequiredClaims", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Message VPN Access-Level Exception object.
     * Get a Message VPN Access-Level Exception object.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileDefaultMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileDefaultMsgVpnAccessLevelExceptionResponse getOauthProfileDefaultMsgVpnAccessLevelException(String oauthProfileName, String msgVpnName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, msgVpnName, opaquePassword, select).getBody();
    }

    /**
     * Get a Message VPN Access-Level Exception object.
     * Get a Message VPN Access-Level Exception object.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileDefaultMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> getOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String msgVpnName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling getOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
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
        final String[] localVarContentTypes = {  };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Message VPN Access-Level Exception objects.
     * Get a list of Message VPN Access-Level Exception objects.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Message VPN Access-Level Exception objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse getOauthProfileDefaultMsgVpnAccessLevelExceptions(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileDefaultMsgVpnAccessLevelExceptionsWithHttpInfo(oauthProfileName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Message VPN Access-Level Exception objects.
     * Get a list of Message VPN Access-Level Exception objects.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Message VPN Access-Level Exception objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse> getOauthProfileDefaultMsgVpnAccessLevelExceptionsWithHttpInfo(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileDefaultMsgVpnAccessLevelExceptions");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse> localReturnType = new ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a Required Claim object.
     * Get a Required Claim object.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param resourceServerRequiredClaimName The name of the access token claim to verify. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResourceServerRequiredClaimResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResourceServerRequiredClaimResponse getOauthProfileResourceServerRequiredClaim(String oauthProfileName, String resourceServerRequiredClaimName, String opaquePassword, List<String> select) throws RestClientException {
        return getOauthProfileResourceServerRequiredClaimWithHttpInfo(oauthProfileName, resourceServerRequiredClaimName, opaquePassword, select).getBody();
    }

    /**
     * Get a Required Claim object.
     * Get a Required Claim object.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Required Claim object&#39;s attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param resourceServerRequiredClaimName The name of the access token claim to verify. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResourceServerRequiredClaimResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResourceServerRequiredClaimResponse> getOauthProfileResourceServerRequiredClaimWithHttpInfo(String oauthProfileName, String resourceServerRequiredClaimName, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileResourceServerRequiredClaim");
        }
        
        // verify the required parameter 'resourceServerRequiredClaimName' is set
        if (resourceServerRequiredClaimName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'resourceServerRequiredClaimName' when calling getOauthProfileResourceServerRequiredClaim");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("resourceServerRequiredClaimName", resourceServerRequiredClaimName);

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

        ParameterizedTypeReference<OauthProfileResourceServerRequiredClaimResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResourceServerRequiredClaimResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName}", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of Required Claim objects.
     * Get a list of Required Claim objects.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Required Claim objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResourceServerRequiredClaimsResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResourceServerRequiredClaimsResponse getOauthProfileResourceServerRequiredClaims(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfileResourceServerRequiredClaimsWithHttpInfo(oauthProfileName, count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of Required Claim objects.
     * Get a list of Required Claim objects.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of Required Claim objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResourceServerRequiredClaimsResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResourceServerRequiredClaimsResponse> getOauthProfileResourceServerRequiredClaimsWithHttpInfo(String oauthProfileName, Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling getOauthProfileResourceServerRequiredClaims");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileResourceServerRequiredClaimsResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResourceServerRequiredClaimsResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims", HttpMethod.GET, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Get a list of OAuth Profile objects.
     * Get a list of OAuth Profile objects.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of OAuth Profile objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfilesResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfilesResponse getOauthProfiles(Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
        return getOauthProfilesWithHttpInfo(count, cursor, opaquePassword, where, select).getBody();
    }

    /**
     * Get a list of OAuth Profile objects.
     * Get a list of OAuth Profile objects.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The list of OAuth Profile objects&#39; attributes, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfilesResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfilesResponse> getOauthProfilesWithHttpInfo(Integer count, String cursor, String opaquePassword, List<String> where, List<String> select) throws RestClientException {
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

        ParameterizedTypeReference<OauthProfilesResponse> localReturnType = new ParameterizedTypeReference<OauthProfilesResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace an OAuth Profile object.
     * Replace an OAuth Profile object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- clientSecret||||x|||x oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The OAuth Profile object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResponse replaceOauthProfile(String oauthProfileName, OauthProfile body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceOauthProfileWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace an OAuth Profile object.
     * Replace an OAuth Profile object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- clientSecret||||x|||x oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The OAuth Profile object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResponse> replaceOauthProfileWithHttpInfo(String oauthProfileName, OauthProfile body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling replaceOauthProfile");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceOauthProfile");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Group Access Level object.
     * Replace a Group Access Level object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param body The Group Access Level object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupResponse replaceOauthProfileAccessLevelGroup(String oauthProfileName, String groupName, OauthProfileAccessLevelGroup body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceOauthProfileAccessLevelGroupWithHttpInfo(oauthProfileName, groupName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Group Access Level object.
     * Replace a Group Access Level object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param body The Group Access Level object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupResponse> replaceOauthProfileAccessLevelGroupWithHttpInfo(String oauthProfileName, String groupName, OauthProfileAccessLevelGroup body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling replaceOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling replaceOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceOauthProfileAccessLevelGroup");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Message VPN Access-Level Exception object.
     * Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException(String oauthProfileName, String groupName, String msgVpnName, OauthProfileAccessLevelGroupMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Message VPN Access-Level Exception object.
     * Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String groupName, String msgVpnName, OauthProfileAccessLevelGroupMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);
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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace an Authorization Parameter object.
     * Replace an Authorization Parameter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @param body The Authorization Parameter object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAuthorizationParameterResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAuthorizationParameterResponse replaceOauthProfileClientAuthorizationParameter(String oauthProfileName, String authorizationParameterName, OauthProfileClientAuthorizationParameter body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceOauthProfileClientAuthorizationParameterWithHttpInfo(oauthProfileName, authorizationParameterName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace an Authorization Parameter object.
     * Replace an Authorization Parameter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @param body The Authorization Parameter object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAuthorizationParameterResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAuthorizationParameterResponse> replaceOauthProfileClientAuthorizationParameterWithHttpInfo(String oauthProfileName, String authorizationParameterName, OauthProfileClientAuthorizationParameter body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling replaceOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'authorizationParameterName' is set
        if (authorizationParameterName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'authorizationParameterName' when calling replaceOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceOauthProfileClientAuthorizationParameter");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("authorizationParameterName", authorizationParameterName);

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

        ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Replace a Message VPN Access-Level Exception object.
     * Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Default message VPN access-level exceptions.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileDefaultMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileDefaultMsgVpnAccessLevelExceptionResponse replaceOauthProfileDefaultMsgVpnAccessLevelException(String oauthProfileName, String msgVpnName, OauthProfileDefaultMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        return replaceOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Replace a Message VPN Access-Level Exception object.
     * Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Default message VPN access-level exceptions.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being replaced, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileDefaultMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> replaceOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String msgVpnName, OauthProfileDefaultMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling replaceOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling replaceOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling replaceOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
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

        ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.PUT, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update an OAuth Profile object.
     * Update an OAuth Profile object. Any attribute missing from the request will be left unchanged.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- clientSecret|||x|||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The OAuth Profile object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileResponse updateOauthProfile(String oauthProfileName, OauthProfile body, String opaquePassword, List<String> select) throws RestClientException {
        return updateOauthProfileWithHttpInfo(oauthProfileName, body, opaquePassword, select).getBody();
    }

    /**
     * Update an OAuth Profile object.
     * Update an OAuth Profile object. Any attribute missing from the request will be left unchanged.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- clientSecret|||x|||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The OAuth Profile object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param body The OAuth Profile object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileResponse> updateOauthProfileWithHttpInfo(String oauthProfileName, OauthProfile body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling updateOauthProfile");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateOauthProfile");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);

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

        ParameterizedTypeReference<OauthProfileResponse> localReturnType = new ParameterizedTypeReference<OauthProfileResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Group Access Level object.
     * Update a Group Access Level object. Any attribute missing from the request will be left unchanged.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param body The Group Access Level object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupResponse updateOauthProfileAccessLevelGroup(String oauthProfileName, String groupName, OauthProfileAccessLevelGroup body, String opaquePassword, List<String> select) throws RestClientException {
        return updateOauthProfileAccessLevelGroupWithHttpInfo(oauthProfileName, groupName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Group Access Level object.
     * Update a Group Access Level object. Any attribute missing from the request will be left unchanged.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     * <p><b>200</b> - The Group Access Level object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param body The Group Access Level object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupResponse> updateOauthProfileAccessLevelGroupWithHttpInfo(String oauthProfileName, String groupName, OauthProfileAccessLevelGroup body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling updateOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling updateOauthProfileAccessLevelGroup");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateOauthProfileAccessLevelGroup");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);

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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Message VPN Access-Level Exception object.
     * Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException(String oauthProfileName, String groupName, String msgVpnName, OauthProfileAccessLevelGroupMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        return updateOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Message VPN Access-Level Exception object.
     * Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param groupName The name of the group. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> updateOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String groupName, String msgVpnName, OauthProfileAccessLevelGroupMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'groupName' is set
        if (groupName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'groupName' when calling updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("groupName", groupName);
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

        ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update an Authorization Parameter object.
     * Update an Authorization Parameter object. Any attribute missing from the request will be left unchanged.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @param body The Authorization Parameter object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileClientAuthorizationParameterResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileClientAuthorizationParameterResponse updateOauthProfileClientAuthorizationParameter(String oauthProfileName, String authorizationParameterName, OauthProfileClientAuthorizationParameter body, String opaquePassword, List<String> select) throws RestClientException {
        return updateOauthProfileClientAuthorizationParameterWithHttpInfo(oauthProfileName, authorizationParameterName, body, opaquePassword, select).getBody();
    }

    /**
     * Update an Authorization Parameter object.
     * Update an Authorization Parameter object. Any attribute missing from the request will be left unchanged.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Authorization Parameter object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param authorizationParameterName The name of the authorization parameter. (required)
     * @param body The Authorization Parameter object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileClientAuthorizationParameterResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileClientAuthorizationParameterResponse> updateOauthProfileClientAuthorizationParameterWithHttpInfo(String oauthProfileName, String authorizationParameterName, OauthProfileClientAuthorizationParameter body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling updateOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'authorizationParameterName' is set
        if (authorizationParameterName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'authorizationParameterName' when calling updateOauthProfileClientAuthorizationParameter");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateOauthProfileClientAuthorizationParameter");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
        uriVariables.put("authorizationParameterName", authorizationParameterName);

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

        ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse> localReturnType = new ParameterizedTypeReference<OauthProfileClientAuthorizationParameterResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
    /**
     * Update a Message VPN Access-Level Exception object.
     * Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Default message VPN access-level exceptions.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return OauthProfileDefaultMsgVpnAccessLevelExceptionResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public OauthProfileDefaultMsgVpnAccessLevelExceptionResponse updateOauthProfileDefaultMsgVpnAccessLevelException(String oauthProfileName, String msgVpnName, OauthProfileDefaultMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        return updateOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(oauthProfileName, msgVpnName, body, opaquePassword, select).getBody();
    }

    /**
     * Update a Message VPN Access-Level Exception object.
     * Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Default message VPN access-level exceptions.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     * <p><b>200</b> - The Message VPN Access-Level Exception object&#39;s attributes after being updated, and the request metadata.
     * <p><b>0</b> - The error response.
     * @param oauthProfileName The name of the OAuth profile. (required)
     * @param msgVpnName The name of the message VPN. (required)
     * @param body The Message VPN Access-Level Exception object&#39;s attributes. (required)
     * @param opaquePassword Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. (optional)
     * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
     * @return ResponseEntity&lt;OauthProfileDefaultMsgVpnAccessLevelExceptionResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> updateOauthProfileDefaultMsgVpnAccessLevelExceptionWithHttpInfo(String oauthProfileName, String msgVpnName, OauthProfileDefaultMsgVpnAccessLevelException body, String opaquePassword, List<String> select) throws RestClientException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'oauthProfileName' is set
        if (oauthProfileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'oauthProfileName' when calling updateOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'msgVpnName' when calling updateOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateOauthProfileDefaultMsgVpnAccessLevelException");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("oauthProfileName", oauthProfileName);
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

        ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse> localReturnType = new ParameterizedTypeReference<OauthProfileDefaultMsgVpnAccessLevelExceptionResponse>() {};
        return apiClient.invokeAPI("/oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName}", HttpMethod.PATCH, uriVariables, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
