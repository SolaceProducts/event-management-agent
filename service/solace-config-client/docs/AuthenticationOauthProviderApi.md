# AuthenticationOauthProviderApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createMsgVpnAuthenticationOauthProvider**](AuthenticationOauthProviderApi.md#createMsgVpnAuthenticationOauthProvider) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProviders | Create an OAuth Provider object. |
| [**deleteMsgVpnAuthenticationOauthProvider**](AuthenticationOauthProviderApi.md#deleteMsgVpnAuthenticationOauthProvider) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Delete an OAuth Provider object. |
| [**getMsgVpnAuthenticationOauthProvider**](AuthenticationOauthProviderApi.md#getMsgVpnAuthenticationOauthProvider) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Get an OAuth Provider object. |
| [**getMsgVpnAuthenticationOauthProviders**](AuthenticationOauthProviderApi.md#getMsgVpnAuthenticationOauthProviders) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders | Get a list of OAuth Provider objects. |
| [**replaceMsgVpnAuthenticationOauthProvider**](AuthenticationOauthProviderApi.md#replaceMsgVpnAuthenticationOauthProvider) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Replace an OAuth Provider object. |
| [**updateMsgVpnAuthenticationOauthProvider**](AuthenticationOauthProviderApi.md#updateMsgVpnAuthenticationOauthProvider) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Update an OAuth Provider object. |



## createMsgVpnAuthenticationOauthProvider

> MsgVpnAuthenticationOauthProviderResponse createMsgVpnAuthenticationOauthProvider(msgVpnName, body, opaquePassword, select)

Create an OAuth Provider object.

Create an OAuth Provider object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  OAuth Providers contain information about the issuer of an OAuth token that is needed to validate the token and derive a client username from it.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: audienceClaimName|||||x| audienceClaimSource|||||x| audienceClaimValue|||||x| audienceValidationEnabled|||||x| authorizationGroupClaimName|||||x| authorizationGroupClaimSource|||||x| authorizationGroupEnabled|||||x| disconnectOnTokenExpirationEnabled|||||x| enabled|||||x| jwksRefreshInterval|||||x| jwksUri|||||x| msgVpnName|x||x||x| oauthProviderName|x|x|||x| tokenIgnoreTimeLimitsEnabled|||||x| tokenIntrospectionParameterName|||||x| tokenIntrospectionPassword||||x|x|x tokenIntrospectionTimeout|||||x| tokenIntrospectionUri|||||x| tokenIntrospectionUsername|||||x| usernameClaimName|||||x| usernameClaimSource|||||x| usernameValidateEnabled|||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.25. Replaced by authenticationOauthProfiles.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AuthenticationOauthProviderApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AuthenticationOauthProviderApi apiInstance = new AuthenticationOauthProviderApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        MsgVpnAuthenticationOauthProvider body = new MsgVpnAuthenticationOauthProvider(); // MsgVpnAuthenticationOauthProvider | The OAuth Provider object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnAuthenticationOauthProviderResponse result = apiInstance.createMsgVpnAuthenticationOauthProvider(msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationOauthProviderApi#createMsgVpnAuthenticationOauthProvider");
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
| **msgVpnName** | **String**| The name of the Message VPN. | |
| **body** | [**MsgVpnAuthenticationOauthProvider**](MsgVpnAuthenticationOauthProvider.md)| The OAuth Provider object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnAuthenticationOauthProviderResponse**](MsgVpnAuthenticationOauthProviderResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Provider object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteMsgVpnAuthenticationOauthProvider

> SempMetaOnlyResponse deleteMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName)

Delete an OAuth Provider object.

Delete an OAuth Provider object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  OAuth Providers contain information about the issuer of an OAuth token that is needed to validate the token and derive a client username from it.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.25. Replaced by authenticationOauthProfiles.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AuthenticationOauthProviderApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AuthenticationOauthProviderApi apiInstance = new AuthenticationOauthProviderApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String oauthProviderName = "oauthProviderName_example"; // String | The name of the OAuth Provider.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationOauthProviderApi#deleteMsgVpnAuthenticationOauthProvider");
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
| **msgVpnName** | **String**| The name of the Message VPN. | |
| **oauthProviderName** | **String**| The name of the OAuth Provider. | |

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


## getMsgVpnAuthenticationOauthProvider

> MsgVpnAuthenticationOauthProviderResponse getMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName, opaquePassword, select)

Get an OAuth Provider object.

Get an OAuth Provider object.  OAuth Providers contain information about the issuer of an OAuth token that is needed to validate the token and derive a client username from it.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: audienceClaimName|||x| audienceClaimSource|||x| audienceClaimValue|||x| audienceValidationEnabled|||x| authorizationGroupClaimName|||x| authorizationGroupClaimSource|||x| authorizationGroupEnabled|||x| disconnectOnTokenExpirationEnabled|||x| enabled|||x| jwksRefreshInterval|||x| jwksUri|||x| msgVpnName|x||x| oauthProviderName|x||x| tokenIgnoreTimeLimitsEnabled|||x| tokenIntrospectionParameterName|||x| tokenIntrospectionPassword||x|x|x tokenIntrospectionTimeout|||x| tokenIntrospectionUri|||x| tokenIntrospectionUsername|||x| usernameClaimName|||x| usernameClaimSource|||x| usernameValidateEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.25. Replaced by authenticationOauthProfiles.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AuthenticationOauthProviderApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AuthenticationOauthProviderApi apiInstance = new AuthenticationOauthProviderApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String oauthProviderName = "oauthProviderName_example"; // String | The name of the OAuth Provider.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnAuthenticationOauthProviderResponse result = apiInstance.getMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationOauthProviderApi#getMsgVpnAuthenticationOauthProvider");
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
| **msgVpnName** | **String**| The name of the Message VPN. | |
| **oauthProviderName** | **String**| The name of the OAuth Provider. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnAuthenticationOauthProviderResponse**](MsgVpnAuthenticationOauthProviderResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Provider object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnAuthenticationOauthProviders

> MsgVpnAuthenticationOauthProvidersResponse getMsgVpnAuthenticationOauthProviders(msgVpnName, count, cursor, opaquePassword, where, select)

Get a list of OAuth Provider objects.

Get a list of OAuth Provider objects.  OAuth Providers contain information about the issuer of an OAuth token that is needed to validate the token and derive a client username from it.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: audienceClaimName|||x| audienceClaimSource|||x| audienceClaimValue|||x| audienceValidationEnabled|||x| authorizationGroupClaimName|||x| authorizationGroupClaimSource|||x| authorizationGroupEnabled|||x| disconnectOnTokenExpirationEnabled|||x| enabled|||x| jwksRefreshInterval|||x| jwksUri|||x| msgVpnName|x||x| oauthProviderName|x||x| tokenIgnoreTimeLimitsEnabled|||x| tokenIntrospectionParameterName|||x| tokenIntrospectionPassword||x|x|x tokenIntrospectionTimeout|||x| tokenIntrospectionUri|||x| tokenIntrospectionUsername|||x| usernameClaimName|||x| usernameClaimSource|||x| usernameValidateEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.25. Replaced by authenticationOauthProfiles.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AuthenticationOauthProviderApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AuthenticationOauthProviderApi apiInstance = new AuthenticationOauthProviderApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnAuthenticationOauthProvidersResponse result = apiInstance.getMsgVpnAuthenticationOauthProviders(msgVpnName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationOauthProviderApi#getMsgVpnAuthenticationOauthProviders");
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
| **msgVpnName** | **String**| The name of the Message VPN. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnAuthenticationOauthProvidersResponse**](MsgVpnAuthenticationOauthProvidersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of OAuth Provider objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnAuthenticationOauthProvider

> MsgVpnAuthenticationOauthProviderResponse replaceMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName, body, opaquePassword, select)

Replace an OAuth Provider object.

Replace an OAuth Provider object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  OAuth Providers contain information about the issuer of an OAuth token that is needed to validate the token and derive a client username from it.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- audienceClaimName||||||x| audienceClaimSource||||||x| audienceClaimValue||||||x| audienceValidationEnabled||||||x| authorizationGroupClaimName||||||x| authorizationGroupClaimSource||||||x| authorizationGroupEnabled||||||x| disconnectOnTokenExpirationEnabled||||||x| enabled||||||x| jwksRefreshInterval||||||x| jwksUri||||||x| msgVpnName|x||x|||x| oauthProviderName|x||x|||x| tokenIgnoreTimeLimitsEnabled||||||x| tokenIntrospectionParameterName||||||x| tokenIntrospectionPassword||||x||x|x tokenIntrospectionTimeout||||||x| tokenIntrospectionUri||||||x| tokenIntrospectionUsername||||||x| usernameClaimName||||||x| usernameClaimSource||||||x| usernameValidateEnabled||||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.25. Replaced by authenticationOauthProfiles.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AuthenticationOauthProviderApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AuthenticationOauthProviderApi apiInstance = new AuthenticationOauthProviderApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String oauthProviderName = "oauthProviderName_example"; // String | The name of the OAuth Provider.
        MsgVpnAuthenticationOauthProvider body = new MsgVpnAuthenticationOauthProvider(); // MsgVpnAuthenticationOauthProvider | The OAuth Provider object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnAuthenticationOauthProviderResponse result = apiInstance.replaceMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationOauthProviderApi#replaceMsgVpnAuthenticationOauthProvider");
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
| **msgVpnName** | **String**| The name of the Message VPN. | |
| **oauthProviderName** | **String**| The name of the OAuth Provider. | |
| **body** | [**MsgVpnAuthenticationOauthProvider**](MsgVpnAuthenticationOauthProvider.md)| The OAuth Provider object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnAuthenticationOauthProviderResponse**](MsgVpnAuthenticationOauthProviderResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Provider object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnAuthenticationOauthProvider

> MsgVpnAuthenticationOauthProviderResponse updateMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName, body, opaquePassword, select)

Update an OAuth Provider object.

Update an OAuth Provider object. Any attribute missing from the request will be left unchanged.  OAuth Providers contain information about the issuer of an OAuth token that is needed to validate the token and derive a client username from it.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- audienceClaimName|||||x| audienceClaimSource|||||x| audienceClaimValue|||||x| audienceValidationEnabled|||||x| authorizationGroupClaimName|||||x| authorizationGroupClaimSource|||||x| authorizationGroupEnabled|||||x| disconnectOnTokenExpirationEnabled|||||x| enabled|||||x| jwksRefreshInterval|||||x| jwksUri|||||x| msgVpnName|x|x|||x| oauthProviderName|x|x|||x| tokenIgnoreTimeLimitsEnabled|||||x| tokenIntrospectionParameterName|||||x| tokenIntrospectionPassword|||x||x|x tokenIntrospectionTimeout|||||x| tokenIntrospectionUri|||||x| tokenIntrospectionUsername|||||x| usernameClaimName|||||x| usernameClaimSource|||||x| usernameValidateEnabled|||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.25. Replaced by authenticationOauthProfiles.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AuthenticationOauthProviderApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AuthenticationOauthProviderApi apiInstance = new AuthenticationOauthProviderApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String oauthProviderName = "oauthProviderName_example"; // String | The name of the OAuth Provider.
        MsgVpnAuthenticationOauthProvider body = new MsgVpnAuthenticationOauthProvider(); // MsgVpnAuthenticationOauthProvider | The OAuth Provider object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnAuthenticationOauthProviderResponse result = apiInstance.updateMsgVpnAuthenticationOauthProvider(msgVpnName, oauthProviderName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationOauthProviderApi#updateMsgVpnAuthenticationOauthProvider");
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
| **msgVpnName** | **String**| The name of the Message VPN. | |
| **oauthProviderName** | **String**| The name of the OAuth Provider. | |
| **body** | [**MsgVpnAuthenticationOauthProvider**](MsgVpnAuthenticationOauthProvider.md)| The OAuth Provider object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnAuthenticationOauthProviderResponse**](MsgVpnAuthenticationOauthProviderResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OAuth Provider object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

