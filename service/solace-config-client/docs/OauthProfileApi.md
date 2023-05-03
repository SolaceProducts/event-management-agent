# OauthProfileApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createOauthProfile**](OauthProfileApi.md#createOauthProfile) | **POST** /oauthProfiles | Create an OAuth Profile object. |
| [**createOauthProfileAccessLevelGroup**](OauthProfileApi.md#createOauthProfileAccessLevelGroup) | **POST** /oauthProfiles/{oauthProfileName}/accessLevelGroups | Create a Group Access Level object. |
| [**createOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileApi.md#createOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **POST** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions | Create a Message VPN Access-Level Exception object. |
| [**createOauthProfileClientAllowedHost**](OauthProfileApi.md#createOauthProfileClientAllowedHost) | **POST** /oauthProfiles/{oauthProfileName}/clientAllowedHosts | Create an Allowed Host Value object. |
| [**createOauthProfileClientAuthorizationParameter**](OauthProfileApi.md#createOauthProfileClientAuthorizationParameter) | **POST** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters | Create an Authorization Parameter object. |
| [**createOauthProfileClientRequiredClaim**](OauthProfileApi.md#createOauthProfileClientRequiredClaim) | **POST** /oauthProfiles/{oauthProfileName}/clientRequiredClaims | Create a Required Claim object. |
| [**createOauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileApi.md#createOauthProfileDefaultMsgVpnAccessLevelException) | **POST** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions | Create a Message VPN Access-Level Exception object. |
| [**createOauthProfileResourceServerRequiredClaim**](OauthProfileApi.md#createOauthProfileResourceServerRequiredClaim) | **POST** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Create a Required Claim object. |
| [**deleteOauthProfile**](OauthProfileApi.md#deleteOauthProfile) | **DELETE** /oauthProfiles/{oauthProfileName} | Delete an OAuth Profile object. |
| [**deleteOauthProfileAccessLevelGroup**](OauthProfileApi.md#deleteOauthProfileAccessLevelGroup) | **DELETE** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Delete a Group Access Level object. |
| [**deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileApi.md#deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **DELETE** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access-Level Exception object. |
| [**deleteOauthProfileClientAllowedHost**](OauthProfileApi.md#deleteOauthProfileClientAllowedHost) | **DELETE** /oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost} | Delete an Allowed Host Value object. |
| [**deleteOauthProfileClientAuthorizationParameter**](OauthProfileApi.md#deleteOauthProfileClientAuthorizationParameter) | **DELETE** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Delete an Authorization Parameter object. |
| [**deleteOauthProfileClientRequiredClaim**](OauthProfileApi.md#deleteOauthProfileClientRequiredClaim) | **DELETE** /oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Delete a Required Claim object. |
| [**deleteOauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileApi.md#deleteOauthProfileDefaultMsgVpnAccessLevelException) | **DELETE** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access-Level Exception object. |
| [**deleteOauthProfileResourceServerRequiredClaim**](OauthProfileApi.md#deleteOauthProfileResourceServerRequiredClaim) | **DELETE** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Delete a Required Claim object. |
| [**getOauthProfile**](OauthProfileApi.md#getOauthProfile) | **GET** /oauthProfiles/{oauthProfileName} | Get an OAuth Profile object. |
| [**getOauthProfileAccessLevelGroup**](OauthProfileApi.md#getOauthProfileAccessLevelGroup) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Get a Group Access Level object. |
| [**getOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileApi.md#getOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access-Level Exception object. |
| [**getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions**](OauthProfileApi.md#getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions | Get a list of Message VPN Access-Level Exception objects. |
| [**getOauthProfileAccessLevelGroups**](OauthProfileApi.md#getOauthProfileAccessLevelGroups) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups | Get a list of Group Access Level objects. |
| [**getOauthProfileClientAllowedHost**](OauthProfileApi.md#getOauthProfileClientAllowedHost) | **GET** /oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost} | Get an Allowed Host Value object. |
| [**getOauthProfileClientAllowedHosts**](OauthProfileApi.md#getOauthProfileClientAllowedHosts) | **GET** /oauthProfiles/{oauthProfileName}/clientAllowedHosts | Get a list of Allowed Host Value objects. |
| [**getOauthProfileClientAuthorizationParameter**](OauthProfileApi.md#getOauthProfileClientAuthorizationParameter) | **GET** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Get an Authorization Parameter object. |
| [**getOauthProfileClientAuthorizationParameters**](OauthProfileApi.md#getOauthProfileClientAuthorizationParameters) | **GET** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters | Get a list of Authorization Parameter objects. |
| [**getOauthProfileClientRequiredClaim**](OauthProfileApi.md#getOauthProfileClientRequiredClaim) | **GET** /oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Get a Required Claim object. |
| [**getOauthProfileClientRequiredClaims**](OauthProfileApi.md#getOauthProfileClientRequiredClaims) | **GET** /oauthProfiles/{oauthProfileName}/clientRequiredClaims | Get a list of Required Claim objects. |
| [**getOauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileApi.md#getOauthProfileDefaultMsgVpnAccessLevelException) | **GET** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access-Level Exception object. |
| [**getOauthProfileDefaultMsgVpnAccessLevelExceptions**](OauthProfileApi.md#getOauthProfileDefaultMsgVpnAccessLevelExceptions) | **GET** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions | Get a list of Message VPN Access-Level Exception objects. |
| [**getOauthProfileResourceServerRequiredClaim**](OauthProfileApi.md#getOauthProfileResourceServerRequiredClaim) | **GET** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Get a Required Claim object. |
| [**getOauthProfileResourceServerRequiredClaims**](OauthProfileApi.md#getOauthProfileResourceServerRequiredClaims) | **GET** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Get a list of Required Claim objects. |
| [**getOauthProfiles**](OauthProfileApi.md#getOauthProfiles) | **GET** /oauthProfiles | Get a list of OAuth Profile objects. |
| [**replaceOauthProfile**](OauthProfileApi.md#replaceOauthProfile) | **PUT** /oauthProfiles/{oauthProfileName} | Replace an OAuth Profile object. |
| [**replaceOauthProfileAccessLevelGroup**](OauthProfileApi.md#replaceOauthProfileAccessLevelGroup) | **PUT** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Replace a Group Access Level object. |
| [**replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileApi.md#replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **PUT** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access-Level Exception object. |
| [**replaceOauthProfileClientAuthorizationParameter**](OauthProfileApi.md#replaceOauthProfileClientAuthorizationParameter) | **PUT** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Replace an Authorization Parameter object. |
| [**replaceOauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileApi.md#replaceOauthProfileDefaultMsgVpnAccessLevelException) | **PUT** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access-Level Exception object. |
| [**updateOauthProfile**](OauthProfileApi.md#updateOauthProfile) | **PATCH** /oauthProfiles/{oauthProfileName} | Update an OAuth Profile object. |
| [**updateOauthProfileAccessLevelGroup**](OauthProfileApi.md#updateOauthProfileAccessLevelGroup) | **PATCH** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Update a Group Access Level object. |
| [**updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileApi.md#updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **PATCH** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access-Level Exception object. |
| [**updateOauthProfileClientAuthorizationParameter**](OauthProfileApi.md#updateOauthProfileClientAuthorizationParameter) | **PATCH** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Update an Authorization Parameter object. |
| [**updateOauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileApi.md#updateOauthProfileDefaultMsgVpnAccessLevelException) | **PATCH** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access-Level Exception object. |



## createOauthProfile

> OauthProfileResponse createOauthProfile(body, opaquePassword, select)

Create an OAuth Profile object.

Create an OAuth Profile object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientSecret||||x||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        OauthProfile body = new OauthProfile(); // OauthProfile | The OAuth Profile object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResponse result = apiInstance.createOauthProfile(body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **body** | [**OauthProfile**](OauthProfile.md)| The OAuth Profile object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResponse**](OauthProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Profile object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileAccessLevelGroup

> OauthProfileAccessLevelGroupResponse createOauthProfileAccessLevelGroup(oauthProfileName, body, opaquePassword, select)

Create a Group Access Level object.

Create a Group Access Level object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfileAccessLevelGroup body = new OauthProfileAccessLevelGroup(); // OauthProfileAccessLevelGroup | The Group Access Level object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupResponse result = apiInstance.createOauthProfileAccessLevelGroup(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileAccessLevelGroup");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfileAccessLevelGroup**](OauthProfileAccessLevelGroup.md)| The Group Access Level object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupResponse**](OauthProfileAccessLevelGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Group Access Level object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileAccessLevelGroupMsgVpnAccessLevelException

> OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse createOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, body, opaquePassword, select)

Create a Message VPN Access-Level Exception object.

Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x||x||| msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        OauthProfileAccessLevelGroupMsgVpnAccessLevelException body = new OauthProfileAccessLevelGroupMsgVpnAccessLevelException(); // OauthProfileAccessLevelGroupMsgVpnAccessLevelException | The Message VPN Access-Level Exception object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse result = apiInstance.createOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **body** | [**OauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileAccessLevelGroupMsgVpnAccessLevelException.md)| The Message VPN Access-Level Exception object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse**](OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileClientAllowedHost

> OauthProfileClientAllowedHostResponse createOauthProfileClientAllowedHost(oauthProfileName, body, opaquePassword, select)

Create an Allowed Host Value object.

Create an Allowed Host Value object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: allowedHost|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfileClientAllowedHost body = new OauthProfileClientAllowedHost(); // OauthProfileClientAllowedHost | The Allowed Host Value object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAllowedHostResponse result = apiInstance.createOauthProfileClientAllowedHost(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileClientAllowedHost");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfileClientAllowedHost**](OauthProfileClientAllowedHost.md)| The Allowed Host Value object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAllowedHostResponse**](OauthProfileClientAllowedHostResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Allowed Host Value object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileClientAuthorizationParameter

> OauthProfileClientAuthorizationParameterResponse createOauthProfileClientAuthorizationParameter(oauthProfileName, body, opaquePassword, select)

Create an Authorization Parameter object.

Create an Authorization Parameter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authorizationParameterName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfileClientAuthorizationParameter body = new OauthProfileClientAuthorizationParameter(); // OauthProfileClientAuthorizationParameter | The Authorization Parameter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAuthorizationParameterResponse result = apiInstance.createOauthProfileClientAuthorizationParameter(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileClientAuthorizationParameter");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfileClientAuthorizationParameter**](OauthProfileClientAuthorizationParameter.md)| The Authorization Parameter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAuthorizationParameterResponse**](OauthProfileClientAuthorizationParameterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Authorization Parameter object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileClientRequiredClaim

> OauthProfileClientRequiredClaimResponse createOauthProfileClientRequiredClaim(oauthProfileName, body, opaquePassword, select)

Create a Required Claim object.

Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientRequiredClaimName|x|x|||| clientRequiredClaimValue||x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfileClientRequiredClaim body = new OauthProfileClientRequiredClaim(); // OauthProfileClientRequiredClaim | The Required Claim object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientRequiredClaimResponse result = apiInstance.createOauthProfileClientRequiredClaim(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileClientRequiredClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfileClientRequiredClaim**](OauthProfileClientRequiredClaim.md)| The Required Claim object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientRequiredClaimResponse**](OauthProfileClientRequiredClaimResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Required Claim object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileDefaultMsgVpnAccessLevelException

> OauthProfileDefaultMsgVpnAccessLevelExceptionResponse createOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, body, opaquePassword, select)

Create a Message VPN Access-Level Exception object.

Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfileDefaultMsgVpnAccessLevelException body = new OauthProfileDefaultMsgVpnAccessLevelException(); // OauthProfileDefaultMsgVpnAccessLevelException | The Message VPN Access-Level Exception object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileDefaultMsgVpnAccessLevelExceptionResponse result = apiInstance.createOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileDefaultMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileDefaultMsgVpnAccessLevelException.md)| The Message VPN Access-Level Exception object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileDefaultMsgVpnAccessLevelExceptionResponse**](OauthProfileDefaultMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createOauthProfileResourceServerRequiredClaim

> OauthProfileResourceServerRequiredClaimResponse createOauthProfileResourceServerRequiredClaim(oauthProfileName, body, opaquePassword, select)

Create a Required Claim object.

Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: oauthProfileName|x||x||| resourceServerRequiredClaimName|x|x|||| resourceServerRequiredClaimValue||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfileResourceServerRequiredClaim body = new OauthProfileResourceServerRequiredClaim(); // OauthProfileResourceServerRequiredClaim | The Required Claim object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResourceServerRequiredClaimResponse result = apiInstance.createOauthProfileResourceServerRequiredClaim(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#createOauthProfileResourceServerRequiredClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfileResourceServerRequiredClaim**](OauthProfileResourceServerRequiredClaim.md)| The Required Claim object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResourceServerRequiredClaimResponse**](OauthProfileResourceServerRequiredClaimResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Required Claim object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfile

> SempMetaOnlyResponse deleteOauthProfile(oauthProfileName)

Delete an OAuth Profile object.

Delete an OAuth Profile object. The deletion of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfile(oauthProfileName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileAccessLevelGroup

> SempMetaOnlyResponse deleteOauthProfileAccessLevelGroup(oauthProfileName, groupName)

Delete a Group Access Level object.

Delete a Group Access Level object. The deletion of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileAccessLevelGroup(oauthProfileName, groupName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileAccessLevelGroup");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException

> SempMetaOnlyResponse deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName)

Delete a Message VPN Access-Level Exception object.

Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **msgVpnName** | **String**| The name of the message VPN. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileClientAllowedHost

> SempMetaOnlyResponse deleteOauthProfileClientAllowedHost(oauthProfileName, allowedHost)

Delete an Allowed Host Value object.

Delete an Allowed Host Value object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String allowedHost = "allowedHost_example"; // String | An allowed value for the Host header.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileClientAllowedHost(oauthProfileName, allowedHost);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileClientAllowedHost");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **allowedHost** | **String**| An allowed value for the Host header. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileClientAuthorizationParameter

> SempMetaOnlyResponse deleteOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName)

Delete an Authorization Parameter object.

Delete an Authorization Parameter object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String authorizationParameterName = "authorizationParameterName_example"; // String | The name of the authorization parameter.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileClientAuthorizationParameter");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **authorizationParameterName** | **String**| The name of the authorization parameter. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileClientRequiredClaim

> SempMetaOnlyResponse deleteOauthProfileClientRequiredClaim(oauthProfileName, clientRequiredClaimName)

Delete a Required Claim object.

Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String clientRequiredClaimName = "clientRequiredClaimName_example"; // String | The name of the ID token claim to verify.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileClientRequiredClaim(oauthProfileName, clientRequiredClaimName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileClientRequiredClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **clientRequiredClaimName** | **String**| The name of the ID token claim to verify. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileDefaultMsgVpnAccessLevelException

> SempMetaOnlyResponse deleteOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName)

Delete a Message VPN Access-Level Exception object.

Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileDefaultMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **msgVpnName** | **String**| The name of the message VPN. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteOauthProfileResourceServerRequiredClaim

> SempMetaOnlyResponse deleteOauthProfileResourceServerRequiredClaim(oauthProfileName, resourceServerRequiredClaimName)

Delete a Required Claim object.

Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String resourceServerRequiredClaimName = "resourceServerRequiredClaimName_example"; // String | The name of the access token claim to verify.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteOauthProfileResourceServerRequiredClaim(oauthProfileName, resourceServerRequiredClaimName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#deleteOauthProfileResourceServerRequiredClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **resourceServerRequiredClaimName** | **String**| The name of the access token claim to verify. | |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfile

> OauthProfileResponse getOauthProfile(oauthProfileName, opaquePassword, select)

Get an OAuth Profile object.

Get an OAuth Profile object.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResponse result = apiInstance.getOauthProfile(oauthProfileName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResponse**](OauthProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Profile object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileAccessLevelGroup

> OauthProfileAccessLevelGroupResponse getOauthProfileAccessLevelGroup(oauthProfileName, groupName, opaquePassword, select)

Get a Group Access Level object.

Get a Group Access Level object.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupResponse result = apiInstance.getOauthProfileAccessLevelGroup(oauthProfileName, groupName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileAccessLevelGroup");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupResponse**](OauthProfileAccessLevelGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Group Access Level object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileAccessLevelGroupMsgVpnAccessLevelException

> OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse getOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, opaquePassword, select)

Get a Message VPN Access-Level Exception object.

Get a Message VPN Access-Level Exception object.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse result = apiInstance.getOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **msgVpnName** | **String**| The name of the message VPN. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse**](OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions

> OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions(oauthProfileName, groupName, count, cursor, opaquePassword, where, select)

Get a list of Message VPN Access-Level Exception objects.

Get a list of Message VPN Access-Level Exception objects.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse result = apiInstance.getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions(oauthProfileName, groupName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse**](OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Message VPN Access-Level Exception objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileAccessLevelGroups

> OauthProfileAccessLevelGroupsResponse getOauthProfileAccessLevelGroups(oauthProfileName, count, cursor, opaquePassword, where, select)

Get a list of Group Access Level objects.

Get a list of Group Access Level objects.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupsResponse result = apiInstance.getOauthProfileAccessLevelGroups(oauthProfileName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileAccessLevelGroups");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupsResponse**](OauthProfileAccessLevelGroupsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Group Access Level objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileClientAllowedHost

> OauthProfileClientAllowedHostResponse getOauthProfileClientAllowedHost(oauthProfileName, allowedHost, opaquePassword, select)

Get an Allowed Host Value object.

Get an Allowed Host Value object.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String allowedHost = "allowedHost_example"; // String | An allowed value for the Host header.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAllowedHostResponse result = apiInstance.getOauthProfileClientAllowedHost(oauthProfileName, allowedHost, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileClientAllowedHost");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **allowedHost** | **String**| An allowed value for the Host header. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAllowedHostResponse**](OauthProfileClientAllowedHostResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Allowed Host Value object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileClientAllowedHosts

> OauthProfileClientAllowedHostsResponse getOauthProfileClientAllowedHosts(oauthProfileName, count, cursor, opaquePassword, where, select)

Get a list of Allowed Host Value objects.

Get a list of Allowed Host Value objects.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAllowedHostsResponse result = apiInstance.getOauthProfileClientAllowedHosts(oauthProfileName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileClientAllowedHosts");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAllowedHostsResponse**](OauthProfileClientAllowedHostsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Allowed Host Value objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileClientAuthorizationParameter

> OauthProfileClientAuthorizationParameterResponse getOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, opaquePassword, select)

Get an Authorization Parameter object.

Get an Authorization Parameter object.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String authorizationParameterName = "authorizationParameterName_example"; // String | The name of the authorization parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAuthorizationParameterResponse result = apiInstance.getOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileClientAuthorizationParameter");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **authorizationParameterName** | **String**| The name of the authorization parameter. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAuthorizationParameterResponse**](OauthProfileClientAuthorizationParameterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Authorization Parameter object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileClientAuthorizationParameters

> OauthProfileClientAuthorizationParametersResponse getOauthProfileClientAuthorizationParameters(oauthProfileName, count, cursor, opaquePassword, where, select)

Get a list of Authorization Parameter objects.

Get a list of Authorization Parameter objects.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAuthorizationParametersResponse result = apiInstance.getOauthProfileClientAuthorizationParameters(oauthProfileName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileClientAuthorizationParameters");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAuthorizationParametersResponse**](OauthProfileClientAuthorizationParametersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Authorization Parameter objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileClientRequiredClaim

> OauthProfileClientRequiredClaimResponse getOauthProfileClientRequiredClaim(oauthProfileName, clientRequiredClaimName, opaquePassword, select)

Get a Required Claim object.

Get a Required Claim object.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String clientRequiredClaimName = "clientRequiredClaimName_example"; // String | The name of the ID token claim to verify.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientRequiredClaimResponse result = apiInstance.getOauthProfileClientRequiredClaim(oauthProfileName, clientRequiredClaimName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileClientRequiredClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **clientRequiredClaimName** | **String**| The name of the ID token claim to verify. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientRequiredClaimResponse**](OauthProfileClientRequiredClaimResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Required Claim object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileClientRequiredClaims

> OauthProfileClientRequiredClaimsResponse getOauthProfileClientRequiredClaims(oauthProfileName, count, cursor, opaquePassword, where, select)

Get a list of Required Claim objects.

Get a list of Required Claim objects.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientRequiredClaimsResponse result = apiInstance.getOauthProfileClientRequiredClaims(oauthProfileName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileClientRequiredClaims");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientRequiredClaimsResponse**](OauthProfileClientRequiredClaimsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Required Claim objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileDefaultMsgVpnAccessLevelException

> OauthProfileDefaultMsgVpnAccessLevelExceptionResponse getOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, opaquePassword, select)

Get a Message VPN Access-Level Exception object.

Get a Message VPN Access-Level Exception object.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileDefaultMsgVpnAccessLevelExceptionResponse result = apiInstance.getOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileDefaultMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **msgVpnName** | **String**| The name of the message VPN. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileDefaultMsgVpnAccessLevelExceptionResponse**](OauthProfileDefaultMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileDefaultMsgVpnAccessLevelExceptions

> OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse getOauthProfileDefaultMsgVpnAccessLevelExceptions(oauthProfileName, count, cursor, opaquePassword, where, select)

Get a list of Message VPN Access-Level Exception objects.

Get a list of Message VPN Access-Level Exception objects.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse result = apiInstance.getOauthProfileDefaultMsgVpnAccessLevelExceptions(oauthProfileName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileDefaultMsgVpnAccessLevelExceptions");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse**](OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Message VPN Access-Level Exception objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileResourceServerRequiredClaim

> OauthProfileResourceServerRequiredClaimResponse getOauthProfileResourceServerRequiredClaim(oauthProfileName, resourceServerRequiredClaimName, opaquePassword, select)

Get a Required Claim object.

Get a Required Claim object.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String resourceServerRequiredClaimName = "resourceServerRequiredClaimName_example"; // String | The name of the access token claim to verify.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResourceServerRequiredClaimResponse result = apiInstance.getOauthProfileResourceServerRequiredClaim(oauthProfileName, resourceServerRequiredClaimName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileResourceServerRequiredClaim");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **resourceServerRequiredClaimName** | **String**| The name of the access token claim to verify. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResourceServerRequiredClaimResponse**](OauthProfileResourceServerRequiredClaimResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Required Claim object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfileResourceServerRequiredClaims

> OauthProfileResourceServerRequiredClaimsResponse getOauthProfileResourceServerRequiredClaims(oauthProfileName, count, cursor, opaquePassword, where, select)

Get a list of Required Claim objects.

Get a list of Required Claim objects.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResourceServerRequiredClaimsResponse result = apiInstance.getOauthProfileResourceServerRequiredClaims(oauthProfileName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfileResourceServerRequiredClaims");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResourceServerRequiredClaimsResponse**](OauthProfileResourceServerRequiredClaimsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Required Claim objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getOauthProfiles

> OauthProfilesResponse getOauthProfiles(count, cursor, opaquePassword, where, select)

Get a list of OAuth Profile objects.

Get a list of OAuth Profile objects.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfilesResponse result = apiInstance.getOauthProfiles(count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#getOauthProfiles");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfilesResponse**](OauthProfilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of OAuth Profile objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceOauthProfile

> OauthProfileResponse replaceOauthProfile(oauthProfileName, body, opaquePassword, select)

Replace an OAuth Profile object.

Replace an OAuth Profile object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- clientSecret||||x|||x oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfile body = new OauthProfile(); // OauthProfile | The OAuth Profile object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResponse result = apiInstance.replaceOauthProfile(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#replaceOauthProfile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfile**](OauthProfile.md)| The OAuth Profile object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResponse**](OauthProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Profile object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceOauthProfileAccessLevelGroup

> OauthProfileAccessLevelGroupResponse replaceOauthProfileAccessLevelGroup(oauthProfileName, groupName, body, opaquePassword, select)

Replace a Group Access Level object.

Replace a Group Access Level object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        OauthProfileAccessLevelGroup body = new OauthProfileAccessLevelGroup(); // OauthProfileAccessLevelGroup | The Group Access Level object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupResponse result = apiInstance.replaceOauthProfileAccessLevelGroup(oauthProfileName, groupName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#replaceOauthProfileAccessLevelGroup");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **body** | [**OauthProfileAccessLevelGroup**](OauthProfileAccessLevelGroup.md)| The Group Access Level object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupResponse**](OauthProfileAccessLevelGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Group Access Level object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException

> OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select)

Replace a Message VPN Access-Level Exception object.

Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        OauthProfileAccessLevelGroupMsgVpnAccessLevelException body = new OauthProfileAccessLevelGroupMsgVpnAccessLevelException(); // OauthProfileAccessLevelGroupMsgVpnAccessLevelException | The Message VPN Access-Level Exception object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse result = apiInstance.replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **msgVpnName** | **String**| The name of the message VPN. | |
| **body** | [**OauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileAccessLevelGroupMsgVpnAccessLevelException.md)| The Message VPN Access-Level Exception object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse**](OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceOauthProfileClientAuthorizationParameter

> OauthProfileClientAuthorizationParameterResponse replaceOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, body, opaquePassword, select)

Replace an Authorization Parameter object.

Replace an Authorization Parameter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String authorizationParameterName = "authorizationParameterName_example"; // String | The name of the authorization parameter.
        OauthProfileClientAuthorizationParameter body = new OauthProfileClientAuthorizationParameter(); // OauthProfileClientAuthorizationParameter | The Authorization Parameter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAuthorizationParameterResponse result = apiInstance.replaceOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#replaceOauthProfileClientAuthorizationParameter");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **authorizationParameterName** | **String**| The name of the authorization parameter. | |
| **body** | [**OauthProfileClientAuthorizationParameter**](OauthProfileClientAuthorizationParameter.md)| The Authorization Parameter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAuthorizationParameterResponse**](OauthProfileClientAuthorizationParameterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Authorization Parameter object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceOauthProfileDefaultMsgVpnAccessLevelException

> OauthProfileDefaultMsgVpnAccessLevelExceptionResponse replaceOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, body, opaquePassword, select)

Replace a Message VPN Access-Level Exception object.

Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Default message VPN access-level exceptions.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        OauthProfileDefaultMsgVpnAccessLevelException body = new OauthProfileDefaultMsgVpnAccessLevelException(); // OauthProfileDefaultMsgVpnAccessLevelException | The Message VPN Access-Level Exception object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileDefaultMsgVpnAccessLevelExceptionResponse result = apiInstance.replaceOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#replaceOauthProfileDefaultMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **msgVpnName** | **String**| The name of the message VPN. | |
| **body** | [**OauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileDefaultMsgVpnAccessLevelException.md)| The Message VPN Access-Level Exception object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileDefaultMsgVpnAccessLevelExceptionResponse**](OauthProfileDefaultMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateOauthProfile

> OauthProfileResponse updateOauthProfile(oauthProfileName, body, opaquePassword, select)

Update an OAuth Profile object.

Update an OAuth Profile object. Any attribute missing from the request will be left unchanged.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- clientSecret|||x|||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        OauthProfile body = new OauthProfile(); // OauthProfile | The OAuth Profile object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileResponse result = apiInstance.updateOauthProfile(oauthProfileName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#updateOauthProfile");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **body** | [**OauthProfile**](OauthProfile.md)| The OAuth Profile object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileResponse**](OauthProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Profile object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateOauthProfileAccessLevelGroup

> OauthProfileAccessLevelGroupResponse updateOauthProfileAccessLevelGroup(oauthProfileName, groupName, body, opaquePassword, select)

Update a Group Access Level object.

Update a Group Access Level object. Any attribute missing from the request will be left unchanged.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        OauthProfileAccessLevelGroup body = new OauthProfileAccessLevelGroup(); // OauthProfileAccessLevelGroup | The Group Access Level object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupResponse result = apiInstance.updateOauthProfileAccessLevelGroup(oauthProfileName, groupName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#updateOauthProfileAccessLevelGroup");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **body** | [**OauthProfileAccessLevelGroup**](OauthProfileAccessLevelGroup.md)| The Group Access Level object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupResponse**](OauthProfileAccessLevelGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Group Access Level object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException

> OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select)

Update a Message VPN Access-Level Exception object.

Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String groupName = "groupName_example"; // String | The name of the group.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        OauthProfileAccessLevelGroupMsgVpnAccessLevelException body = new OauthProfileAccessLevelGroupMsgVpnAccessLevelException(); // OauthProfileAccessLevelGroupMsgVpnAccessLevelException | The Message VPN Access-Level Exception object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse result = apiInstance.updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **groupName** | **String**| The name of the group. | |
| **msgVpnName** | **String**| The name of the message VPN. | |
| **body** | [**OauthProfileAccessLevelGroupMsgVpnAccessLevelException**](OauthProfileAccessLevelGroupMsgVpnAccessLevelException.md)| The Message VPN Access-Level Exception object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse**](OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateOauthProfileClientAuthorizationParameter

> OauthProfileClientAuthorizationParameterResponse updateOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, body, opaquePassword, select)

Update an Authorization Parameter object.

Update an Authorization Parameter object. Any attribute missing from the request will be left unchanged.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String authorizationParameterName = "authorizationParameterName_example"; // String | The name of the authorization parameter.
        OauthProfileClientAuthorizationParameter body = new OauthProfileClientAuthorizationParameter(); // OauthProfileClientAuthorizationParameter | The Authorization Parameter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileClientAuthorizationParameterResponse result = apiInstance.updateOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#updateOauthProfileClientAuthorizationParameter");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **authorizationParameterName** | **String**| The name of the authorization parameter. | |
| **body** | [**OauthProfileClientAuthorizationParameter**](OauthProfileClientAuthorizationParameter.md)| The Authorization Parameter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileClientAuthorizationParameterResponse**](OauthProfileClientAuthorizationParameterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Authorization Parameter object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateOauthProfileDefaultMsgVpnAccessLevelException

> OauthProfileDefaultMsgVpnAccessLevelExceptionResponse updateOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, body, opaquePassword, select)

Update a Message VPN Access-Level Exception object.

Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Default message VPN access-level exceptions.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.OauthProfileApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        OauthProfileApi apiInstance = new OauthProfileApi(defaultClient);
        String oauthProfileName = "oauthProfileName_example"; // String | The name of the OAuth profile.
        String msgVpnName = "msgVpnName_example"; // String | The name of the message VPN.
        OauthProfileDefaultMsgVpnAccessLevelException body = new OauthProfileDefaultMsgVpnAccessLevelException(); // OauthProfileDefaultMsgVpnAccessLevelException | The Message VPN Access-Level Exception object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            OauthProfileDefaultMsgVpnAccessLevelExceptionResponse result = apiInstance.updateOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling OauthProfileApi#updateOauthProfileDefaultMsgVpnAccessLevelException");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **oauthProfileName** | **String**| The name of the OAuth profile. | |
| **msgVpnName** | **String**| The name of the message VPN. | |
| **body** | [**OauthProfileDefaultMsgVpnAccessLevelException**](OauthProfileDefaultMsgVpnAccessLevelException.md)| The Message VPN Access-Level Exception object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**OauthProfileDefaultMsgVpnAccessLevelExceptionResponse**](OauthProfileDefaultMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Message VPN Access-Level Exception object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

