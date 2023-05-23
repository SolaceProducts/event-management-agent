# ClientUsernameApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createMsgVpnClientUsername**](ClientUsernameApi.md#createMsgVpnClientUsername) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Create a Client Username object. |
| [**createMsgVpnClientUsernameAttribute**](ClientUsernameApi.md#createMsgVpnClientUsernameAttribute) | **POST** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Create a Client Username Attribute object. |
| [**deleteMsgVpnClientUsername**](ClientUsernameApi.md#deleteMsgVpnClientUsername) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Delete a Client Username object. |
| [**deleteMsgVpnClientUsernameAttribute**](ClientUsernameApi.md#deleteMsgVpnClientUsernameAttribute) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Delete a Client Username Attribute object. |
| [**getMsgVpnClientUsername**](ClientUsernameApi.md#getMsgVpnClientUsername) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Get a Client Username object. |
| [**getMsgVpnClientUsernameAttribute**](ClientUsernameApi.md#getMsgVpnClientUsernameAttribute) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Get a Client Username Attribute object. |
| [**getMsgVpnClientUsernameAttributes**](ClientUsernameApi.md#getMsgVpnClientUsernameAttributes) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Get a list of Client Username Attribute objects. |
| [**getMsgVpnClientUsernames**](ClientUsernameApi.md#getMsgVpnClientUsernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Get a list of Client Username objects. |
| [**replaceMsgVpnClientUsername**](ClientUsernameApi.md#replaceMsgVpnClientUsername) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replace a Client Username object. |
| [**updateMsgVpnClientUsername**](ClientUsernameApi.md#updateMsgVpnClientUsername) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Update a Client Username object. |



## createMsgVpnClientUsername

> MsgVpnClientUsernameResponse createMsgVpnClientUsername(msgVpnName, body, opaquePassword, select)

Create a Client Username object.

Create a Client Username object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A client is only authorized to connect to a Message VPN that is associated with a Client Username that the client has been assigned.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientUsername|x|x|||| msgVpnName|x||x||| password||||x||x    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        MsgVpnClientUsername body = new MsgVpnClientUsername(); // MsgVpnClientUsername | The Client Username object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameResponse result = apiInstance.createMsgVpnClientUsername(msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#createMsgVpnClientUsername");
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
| **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Client Username object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createMsgVpnClientUsernameAttribute

> MsgVpnClientUsernameAttributeResponse createMsgVpnClientUsernameAttribute(msgVpnName, clientUsername, body, opaquePassword, select)

Create a Client Username Attribute object.

Create a Client Username Attribute object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A ClientUsername Attribute is a key+value pair that can be used to locate a client username, for example when using client certificate mapping.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: attributeName|x|x|||| attributeValue|x|x|||| clientUsername|x||x||| msgVpnName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        MsgVpnClientUsernameAttribute body = new MsgVpnClientUsernameAttribute(); // MsgVpnClientUsernameAttribute | The Client Username Attribute object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameAttributeResponse result = apiInstance.createMsgVpnClientUsernameAttribute(msgVpnName, clientUsername, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#createMsgVpnClientUsernameAttribute");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **body** | [**MsgVpnClientUsernameAttribute**](MsgVpnClientUsernameAttribute.md)| The Client Username Attribute object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameAttributeResponse**](MsgVpnClientUsernameAttributeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Client Username Attribute object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteMsgVpnClientUsername

> SempMetaOnlyResponse deleteMsgVpnClientUsername(msgVpnName, clientUsername)

Delete a Client Username object.

Delete a Client Username object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A client is only authorized to connect to a Message VPN that is associated with a Client Username that the client has been assigned.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnClientUsername(msgVpnName, clientUsername);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#deleteMsgVpnClientUsername");
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
| **clientUsername** | **String**| The name of the Client Username. | |

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


## deleteMsgVpnClientUsernameAttribute

> SempMetaOnlyResponse deleteMsgVpnClientUsernameAttribute(msgVpnName, clientUsername, attributeName, attributeValue)

Delete a Client Username Attribute object.

Delete a Client Username Attribute object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A ClientUsername Attribute is a key+value pair that can be used to locate a client username, for example when using client certificate mapping.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        String attributeName = "attributeName_example"; // String | The name of the Attribute.
        String attributeValue = "attributeValue_example"; // String | The value of the Attribute.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnClientUsernameAttribute(msgVpnName, clientUsername, attributeName, attributeValue);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#deleteMsgVpnClientUsernameAttribute");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **attributeName** | **String**| The name of the Attribute. | |
| **attributeValue** | **String**| The value of the Attribute. | |

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


## getMsgVpnClientUsername

> MsgVpnClientUsernameResponse getMsgVpnClientUsername(msgVpnName, clientUsername, opaquePassword, select)

Get a Client Username object.

Get a Client Username object.  A client is only authorized to connect to a Message VPN that is associated with a Client Username that the client has been assigned.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientUsername|x||| msgVpnName|x||| password||x||x    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameResponse result = apiInstance.getMsgVpnClientUsername(msgVpnName, clientUsername, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#getMsgVpnClientUsername");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Client Username object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnClientUsernameAttribute

> MsgVpnClientUsernameAttributeResponse getMsgVpnClientUsernameAttribute(msgVpnName, clientUsername, attributeName, attributeValue, opaquePassword, select)

Get a Client Username Attribute object.

Get a Client Username Attribute object.  A ClientUsername Attribute is a key+value pair that can be used to locate a client username, for example when using client certificate mapping.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: attributeName|x||| attributeValue|x||| clientUsername|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        String attributeName = "attributeName_example"; // String | The name of the Attribute.
        String attributeValue = "attributeValue_example"; // String | The value of the Attribute.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameAttributeResponse result = apiInstance.getMsgVpnClientUsernameAttribute(msgVpnName, clientUsername, attributeName, attributeValue, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#getMsgVpnClientUsernameAttribute");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **attributeName** | **String**| The name of the Attribute. | |
| **attributeValue** | **String**| The value of the Attribute. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameAttributeResponse**](MsgVpnClientUsernameAttributeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Client Username Attribute object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnClientUsernameAttributes

> MsgVpnClientUsernameAttributesResponse getMsgVpnClientUsernameAttributes(msgVpnName, clientUsername, count, cursor, opaquePassword, where, select)

Get a list of Client Username Attribute objects.

Get a list of Client Username Attribute objects.  A ClientUsername Attribute is a key+value pair that can be used to locate a client username, for example when using client certificate mapping.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: attributeName|x||| attributeValue|x||| clientUsername|x||| msgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameAttributesResponse result = apiInstance.getMsgVpnClientUsernameAttributes(msgVpnName, clientUsername, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#getMsgVpnClientUsernameAttributes");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameAttributesResponse**](MsgVpnClientUsernameAttributesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Client Username Attribute objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnClientUsernames

> MsgVpnClientUsernamesResponse getMsgVpnClientUsernames(msgVpnName, count, cursor, opaquePassword, where, select)

Get a list of Client Username objects.

Get a list of Client Username objects.  A client is only authorized to connect to a Message VPN that is associated with a Client Username that the client has been assigned.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientUsername|x||| msgVpnName|x||| password||x||x    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernamesResponse result = apiInstance.getMsgVpnClientUsernames(msgVpnName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#getMsgVpnClientUsernames");
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

[**MsgVpnClientUsernamesResponse**](MsgVpnClientUsernamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Client Username objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnClientUsername

> MsgVpnClientUsernameResponse replaceMsgVpnClientUsername(msgVpnName, clientUsername, body, opaquePassword, select)

Replace a Client Username object.

Replace a Client Username object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A client is only authorized to connect to a Message VPN that is associated with a Client Username that the client has been assigned.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- aclProfileName|||||x|| clientProfileName|||||x|| clientUsername|x||x|||| msgVpnName|x||x|||| password||||x|||x    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        MsgVpnClientUsername body = new MsgVpnClientUsername(); // MsgVpnClientUsername | The Client Username object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameResponse result = apiInstance.replaceMsgVpnClientUsername(msgVpnName, clientUsername, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#replaceMsgVpnClientUsername");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Client Username object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnClientUsername

> MsgVpnClientUsernameResponse updateMsgVpnClientUsername(msgVpnName, clientUsername, body, opaquePassword, select)

Update a Client Username object.

Update a Client Username object. Any attribute missing from the request will be left unchanged.  A client is only authorized to connect to a Message VPN that is associated with a Client Username that the client has been assigned.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- aclProfileName||||x|| clientProfileName||||x|| clientUsername|x|x|||| msgVpnName|x|x|||| password|||x|||x    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.ClientUsernameApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        ClientUsernameApi apiInstance = new ClientUsernameApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String clientUsername = "clientUsername_example"; // String | The name of the Client Username.
        MsgVpnClientUsername body = new MsgVpnClientUsername(); // MsgVpnClientUsername | The Client Username object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnClientUsernameResponse result = apiInstance.updateMsgVpnClientUsername(msgVpnName, clientUsername, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ClientUsernameApi#updateMsgVpnClientUsername");
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
| **clientUsername** | **String**| The name of the Client Username. | |
| **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Client Username object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

