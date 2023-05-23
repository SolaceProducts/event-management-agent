# DomainCertAuthorityApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createDomainCertAuthority**](DomainCertAuthorityApi.md#createDomainCertAuthority) | **POST** /domainCertAuthorities | Create a Domain Certificate Authority object. |
| [**deleteDomainCertAuthority**](DomainCertAuthorityApi.md#deleteDomainCertAuthority) | **DELETE** /domainCertAuthorities/{certAuthorityName} | Delete a Domain Certificate Authority object. |
| [**getDomainCertAuthorities**](DomainCertAuthorityApi.md#getDomainCertAuthorities) | **GET** /domainCertAuthorities | Get a list of Domain Certificate Authority objects. |
| [**getDomainCertAuthority**](DomainCertAuthorityApi.md#getDomainCertAuthority) | **GET** /domainCertAuthorities/{certAuthorityName} | Get a Domain Certificate Authority object. |
| [**replaceDomainCertAuthority**](DomainCertAuthorityApi.md#replaceDomainCertAuthority) | **PUT** /domainCertAuthorities/{certAuthorityName} | Replace a Domain Certificate Authority object. |
| [**updateDomainCertAuthority**](DomainCertAuthorityApi.md#updateDomainCertAuthority) | **PATCH** /domainCertAuthorities/{certAuthorityName} | Update a Domain Certificate Authority object. |



## createDomainCertAuthority

> DomainCertAuthorityResponse createDomainCertAuthority(body, opaquePassword, select)

Create a Domain Certificate Authority object.

Create a Domain Certificate Authority object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Certificate Authorities trusted for domain verification.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DomainCertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DomainCertAuthorityApi apiInstance = new DomainCertAuthorityApi(defaultClient);
        DomainCertAuthority body = new DomainCertAuthority(); // DomainCertAuthority | The Domain Certificate Authority object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DomainCertAuthorityResponse result = apiInstance.createDomainCertAuthority(body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DomainCertAuthorityApi#createDomainCertAuthority");
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
| **body** | [**DomainCertAuthority**](DomainCertAuthority.md)| The Domain Certificate Authority object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DomainCertAuthorityResponse**](DomainCertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Domain Certificate Authority object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteDomainCertAuthority

> SempMetaOnlyResponse deleteDomainCertAuthority(certAuthorityName)

Delete a Domain Certificate Authority object.

Delete a Domain Certificate Authority object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Certificate Authorities trusted for domain verification.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DomainCertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DomainCertAuthorityApi apiInstance = new DomainCertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDomainCertAuthority(certAuthorityName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DomainCertAuthorityApi#deleteDomainCertAuthority");
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
| **certAuthorityName** | **String**| The name of the Certificate Authority. | |

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


## getDomainCertAuthorities

> DomainCertAuthoritiesResponse getDomainCertAuthorities(count, cursor, opaquePassword, where, select)

Get a list of Domain Certificate Authority objects.

Get a list of Domain Certificate Authority objects.  Certificate Authorities trusted for domain verification.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DomainCertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DomainCertAuthorityApi apiInstance = new DomainCertAuthorityApi(defaultClient);
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DomainCertAuthoritiesResponse result = apiInstance.getDomainCertAuthorities(count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DomainCertAuthorityApi#getDomainCertAuthorities");
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

[**DomainCertAuthoritiesResponse**](DomainCertAuthoritiesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Domain Certificate Authority objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDomainCertAuthority

> DomainCertAuthorityResponse getDomainCertAuthority(certAuthorityName, opaquePassword, select)

Get a Domain Certificate Authority object.

Get a Domain Certificate Authority object.  Certificate Authorities trusted for domain verification.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.19.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DomainCertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DomainCertAuthorityApi apiInstance = new DomainCertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DomainCertAuthorityResponse result = apiInstance.getDomainCertAuthority(certAuthorityName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DomainCertAuthorityApi#getDomainCertAuthority");
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
| **certAuthorityName** | **String**| The name of the Certificate Authority. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DomainCertAuthorityResponse**](DomainCertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Domain Certificate Authority object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceDomainCertAuthority

> DomainCertAuthorityResponse replaceDomainCertAuthority(certAuthorityName, body, opaquePassword, select)

Replace a Domain Certificate Authority object.

Replace a Domain Certificate Authority object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Certificate Authorities trusted for domain verification.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- certAuthorityName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DomainCertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DomainCertAuthorityApi apiInstance = new DomainCertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        DomainCertAuthority body = new DomainCertAuthority(); // DomainCertAuthority | The Domain Certificate Authority object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DomainCertAuthorityResponse result = apiInstance.replaceDomainCertAuthority(certAuthorityName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DomainCertAuthorityApi#replaceDomainCertAuthority");
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
| **certAuthorityName** | **String**| The name of the Certificate Authority. | |
| **body** | [**DomainCertAuthority**](DomainCertAuthority.md)| The Domain Certificate Authority object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DomainCertAuthorityResponse**](DomainCertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Domain Certificate Authority object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateDomainCertAuthority

> DomainCertAuthorityResponse updateDomainCertAuthority(certAuthorityName, body, opaquePassword, select)

Update a Domain Certificate Authority object.

Update a Domain Certificate Authority object. Any attribute missing from the request will be left unchanged.  Certificate Authorities trusted for domain verification.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- certAuthorityName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.19.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DomainCertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DomainCertAuthorityApi apiInstance = new DomainCertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        DomainCertAuthority body = new DomainCertAuthority(); // DomainCertAuthority | The Domain Certificate Authority object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DomainCertAuthorityResponse result = apiInstance.updateDomainCertAuthority(certAuthorityName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DomainCertAuthorityApi#updateDomainCertAuthority");
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
| **certAuthorityName** | **String**| The name of the Certificate Authority. | |
| **body** | [**DomainCertAuthority**](DomainCertAuthority.md)| The Domain Certificate Authority object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DomainCertAuthorityResponse**](DomainCertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Domain Certificate Authority object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

