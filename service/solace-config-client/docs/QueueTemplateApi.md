# QueueTemplateApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createMsgVpnQueueTemplate**](QueueTemplateApi.md#createMsgVpnQueueTemplate) | **POST** /msgVpns/{msgVpnName}/queueTemplates | Create a Queue Template object. |
| [**deleteMsgVpnQueueTemplate**](QueueTemplateApi.md#deleteMsgVpnQueueTemplate) | **DELETE** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Delete a Queue Template object. |
| [**getMsgVpnQueueTemplate**](QueueTemplateApi.md#getMsgVpnQueueTemplate) | **GET** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Get a Queue Template object. |
| [**getMsgVpnQueueTemplates**](QueueTemplateApi.md#getMsgVpnQueueTemplates) | **GET** /msgVpns/{msgVpnName}/queueTemplates | Get a list of Queue Template objects. |
| [**replaceMsgVpnQueueTemplate**](QueueTemplateApi.md#replaceMsgVpnQueueTemplate) | **PUT** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Replace a Queue Template object. |
| [**updateMsgVpnQueueTemplate**](QueueTemplateApi.md#updateMsgVpnQueueTemplate) | **PATCH** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Update a Queue Template object. |



## createMsgVpnQueueTemplate

> MsgVpnQueueTemplateResponse createMsgVpnQueueTemplate(msgVpnName, body, opaquePassword, select)

Create a Queue Template object.

Create a Queue Template object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Queue Template provides a mechanism for specifying the initial state for client created queues.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| queueTemplateName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.14.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.QueueTemplateApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        QueueTemplateApi apiInstance = new QueueTemplateApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        MsgVpnQueueTemplate body = new MsgVpnQueueTemplate(); // MsgVpnQueueTemplate | The Queue Template object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnQueueTemplateResponse result = apiInstance.createMsgVpnQueueTemplate(msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueueTemplateApi#createMsgVpnQueueTemplate");
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
| **body** | [**MsgVpnQueueTemplate**](MsgVpnQueueTemplate.md)| The Queue Template object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnQueueTemplateResponse**](MsgVpnQueueTemplateResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Queue Template object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteMsgVpnQueueTemplate

> SempMetaOnlyResponse deleteMsgVpnQueueTemplate(msgVpnName, queueTemplateName)

Delete a Queue Template object.

Delete a Queue Template object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Queue Template provides a mechanism for specifying the initial state for client created queues.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.14.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.QueueTemplateApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        QueueTemplateApi apiInstance = new QueueTemplateApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String queueTemplateName = "queueTemplateName_example"; // String | The name of the Queue Template.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnQueueTemplate(msgVpnName, queueTemplateName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueueTemplateApi#deleteMsgVpnQueueTemplate");
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
| **queueTemplateName** | **String**| The name of the Queue Template. | |

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


## getMsgVpnQueueTemplate

> MsgVpnQueueTemplateResponse getMsgVpnQueueTemplate(msgVpnName, queueTemplateName, opaquePassword, select)

Get a Queue Template object.

Get a Queue Template object.  A Queue Template provides a mechanism for specifying the initial state for client created queues.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| queueTemplateName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.14.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.QueueTemplateApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        QueueTemplateApi apiInstance = new QueueTemplateApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String queueTemplateName = "queueTemplateName_example"; // String | The name of the Queue Template.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnQueueTemplateResponse result = apiInstance.getMsgVpnQueueTemplate(msgVpnName, queueTemplateName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueueTemplateApi#getMsgVpnQueueTemplate");
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
| **queueTemplateName** | **String**| The name of the Queue Template. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnQueueTemplateResponse**](MsgVpnQueueTemplateResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Queue Template object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnQueueTemplates

> MsgVpnQueueTemplatesResponse getMsgVpnQueueTemplates(msgVpnName, count, cursor, opaquePassword, where, select)

Get a list of Queue Template objects.

Get a list of Queue Template objects.  A Queue Template provides a mechanism for specifying the initial state for client created queues.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| queueTemplateName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.14.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.QueueTemplateApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        QueueTemplateApi apiInstance = new QueueTemplateApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnQueueTemplatesResponse result = apiInstance.getMsgVpnQueueTemplates(msgVpnName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueueTemplateApi#getMsgVpnQueueTemplates");
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

[**MsgVpnQueueTemplatesResponse**](MsgVpnQueueTemplatesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Queue Template objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnQueueTemplate

> MsgVpnQueueTemplateResponse replaceMsgVpnQueueTemplate(msgVpnName, queueTemplateName, body, opaquePassword, select)

Replace a Queue Template object.

Replace a Queue Template object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Queue Template provides a mechanism for specifying the initial state for client created queues.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| queueTemplateName|x||x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.14.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.QueueTemplateApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        QueueTemplateApi apiInstance = new QueueTemplateApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String queueTemplateName = "queueTemplateName_example"; // String | The name of the Queue Template.
        MsgVpnQueueTemplate body = new MsgVpnQueueTemplate(); // MsgVpnQueueTemplate | The Queue Template object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnQueueTemplateResponse result = apiInstance.replaceMsgVpnQueueTemplate(msgVpnName, queueTemplateName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueueTemplateApi#replaceMsgVpnQueueTemplate");
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
| **queueTemplateName** | **String**| The name of the Queue Template. | |
| **body** | [**MsgVpnQueueTemplate**](MsgVpnQueueTemplate.md)| The Queue Template object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnQueueTemplateResponse**](MsgVpnQueueTemplateResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Queue Template object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnQueueTemplate

> MsgVpnQueueTemplateResponse updateMsgVpnQueueTemplate(msgVpnName, queueTemplateName, body, opaquePassword, select)

Update a Queue Template object.

Update a Queue Template object. Any attribute missing from the request will be left unchanged.  A Queue Template provides a mechanism for specifying the initial state for client created queues.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| queueTemplateName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.14.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.QueueTemplateApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        QueueTemplateApi apiInstance = new QueueTemplateApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String queueTemplateName = "queueTemplateName_example"; // String | The name of the Queue Template.
        MsgVpnQueueTemplate body = new MsgVpnQueueTemplate(); // MsgVpnQueueTemplate | The Queue Template object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnQueueTemplateResponse result = apiInstance.updateMsgVpnQueueTemplate(msgVpnName, queueTemplateName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling QueueTemplateApi#updateMsgVpnQueueTemplate");
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
| **queueTemplateName** | **String**| The name of the Queue Template. | |
| **body** | [**MsgVpnQueueTemplate**](MsgVpnQueueTemplate.md)| The Queue Template object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnQueueTemplateResponse**](MsgVpnQueueTemplateResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Queue Template object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

