# CertMatchingRuleApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createMsgVpnCertMatchingRule**](CertMatchingRuleApi.md#createMsgVpnCertMatchingRule) | **POST** /msgVpns/{msgVpnName}/certMatchingRules | Create a Certificate Matching Rule object. |
| [**createMsgVpnCertMatchingRuleAttributeFilter**](CertMatchingRuleApi.md#createMsgVpnCertMatchingRuleAttributeFilter) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object. |
| [**createMsgVpnCertMatchingRuleCondition**](CertMatchingRuleApi.md#createMsgVpnCertMatchingRuleCondition) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object. |
| [**deleteMsgVpnCertMatchingRule**](CertMatchingRuleApi.md#deleteMsgVpnCertMatchingRule) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object. |
| [**deleteMsgVpnCertMatchingRuleAttributeFilter**](CertMatchingRuleApi.md#deleteMsgVpnCertMatchingRuleAttributeFilter) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object. |
| [**deleteMsgVpnCertMatchingRuleCondition**](CertMatchingRuleApi.md#deleteMsgVpnCertMatchingRuleCondition) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object. |
| [**getMsgVpnCertMatchingRule**](CertMatchingRuleApi.md#getMsgVpnCertMatchingRule) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object. |
| [**getMsgVpnCertMatchingRuleAttributeFilter**](CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleAttributeFilter) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object. |
| [**getMsgVpnCertMatchingRuleAttributeFilters**](CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleAttributeFilters) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects. |
| [**getMsgVpnCertMatchingRuleCondition**](CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleCondition) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object. |
| [**getMsgVpnCertMatchingRuleConditions**](CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleConditions) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects. |
| [**getMsgVpnCertMatchingRules**](CertMatchingRuleApi.md#getMsgVpnCertMatchingRules) | **GET** /msgVpns/{msgVpnName}/certMatchingRules | Get a list of Certificate Matching Rule objects. |
| [**replaceMsgVpnCertMatchingRule**](CertMatchingRuleApi.md#replaceMsgVpnCertMatchingRule) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object. |
| [**replaceMsgVpnCertMatchingRuleAttributeFilter**](CertMatchingRuleApi.md#replaceMsgVpnCertMatchingRuleAttributeFilter) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object. |
| [**updateMsgVpnCertMatchingRule**](CertMatchingRuleApi.md#updateMsgVpnCertMatchingRule) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object. |
| [**updateMsgVpnCertMatchingRuleAttributeFilter**](CertMatchingRuleApi.md#updateMsgVpnCertMatchingRuleAttributeFilter) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object. |



## createMsgVpnCertMatchingRule

> MsgVpnCertMatchingRuleResponse createMsgVpnCertMatchingRule(msgVpnName, body, opaquePassword, select)

Create a Certificate Matching Rule object.

Create a Certificate Matching Rule object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given username.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        MsgVpnCertMatchingRule body = new MsgVpnCertMatchingRule(); // MsgVpnCertMatchingRule | The Certificate Matching Rule object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleResponse result = apiInstance.createMsgVpnCertMatchingRule(msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#createMsgVpnCertMatchingRule");
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
| **body** | [**MsgVpnCertMatchingRule**](MsgVpnCertMatchingRule.md)| The Certificate Matching Rule object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleResponse**](MsgVpnCertMatchingRuleResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createMsgVpnCertMatchingRuleAttributeFilter

> MsgVpnCertMatchingRuleAttributeFilterResponse createMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, body, opaquePassword, select)

Create a Certificate Matching Rule Attribute Filter object.

Create a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Cert Matching Rule Attribute Filter compares a username attribute to a string.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: filterName|x|x|||| msgVpnName|x||x||| ruleName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        MsgVpnCertMatchingRuleAttributeFilter body = new MsgVpnCertMatchingRuleAttributeFilter(); // MsgVpnCertMatchingRuleAttributeFilter | The Certificate Matching Rule Attribute Filter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleAttributeFilterResponse result = apiInstance.createMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#createMsgVpnCertMatchingRuleAttributeFilter");
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
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**MsgVpnCertMatchingRuleAttributeFilter**](MsgVpnCertMatchingRuleAttributeFilter.md)| The Certificate Matching Rule Attribute Filter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleAttributeFilterResponse**](MsgVpnCertMatchingRuleAttributeFilterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule Attribute Filter object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createMsgVpnCertMatchingRuleCondition

> MsgVpnCertMatchingRuleConditionResponse createMsgVpnCertMatchingRuleCondition(msgVpnName, ruleName, body, opaquePassword, select)

Create a Certificate Matching Rule Condition object.

Create a Certificate Matching Rule Condition object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Cert Matching Rule Condition compares data extracted from a certificate to a username attribute or an expression.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x||| ruleName|x||x||| source|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        MsgVpnCertMatchingRuleCondition body = new MsgVpnCertMatchingRuleCondition(); // MsgVpnCertMatchingRuleCondition | The Certificate Matching Rule Condition object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleConditionResponse result = apiInstance.createMsgVpnCertMatchingRuleCondition(msgVpnName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#createMsgVpnCertMatchingRuleCondition");
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
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**MsgVpnCertMatchingRuleCondition**](MsgVpnCertMatchingRuleCondition.md)| The Certificate Matching Rule Condition object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleConditionResponse**](MsgVpnCertMatchingRuleConditionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule Condition object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteMsgVpnCertMatchingRule

> SempMetaOnlyResponse deleteMsgVpnCertMatchingRule(msgVpnName, ruleName)

Delete a Certificate Matching Rule object.

Delete a Certificate Matching Rule object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given username.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnCertMatchingRule(msgVpnName, ruleName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#deleteMsgVpnCertMatchingRule");
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
| **ruleName** | **String**| The name of the rule. | |

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


## deleteMsgVpnCertMatchingRuleAttributeFilter

> SempMetaOnlyResponse deleteMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName)

Delete a Certificate Matching Rule Attribute Filter object.

Delete a Certificate Matching Rule Attribute Filter object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Cert Matching Rule Attribute Filter compares a username attribute to a string.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#deleteMsgVpnCertMatchingRuleAttributeFilter");
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
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |

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


## deleteMsgVpnCertMatchingRuleCondition

> SempMetaOnlyResponse deleteMsgVpnCertMatchingRuleCondition(msgVpnName, ruleName, source)

Delete a Certificate Matching Rule Condition object.

Delete a Certificate Matching Rule Condition object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Cert Matching Rule Condition compares data extracted from a certificate to a username attribute or an expression.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String source = "source_example"; // String | Certificate field to be compared with the Attribute.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnCertMatchingRuleCondition(msgVpnName, ruleName, source);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#deleteMsgVpnCertMatchingRuleCondition");
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
| **ruleName** | **String**| The name of the rule. | |
| **source** | **String**| Certificate field to be compared with the Attribute. | |

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


## getMsgVpnCertMatchingRule

> MsgVpnCertMatchingRuleResponse getMsgVpnCertMatchingRule(msgVpnName, ruleName, opaquePassword, select)

Get a Certificate Matching Rule object.

Get a Certificate Matching Rule object.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given username.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleResponse result = apiInstance.getMsgVpnCertMatchingRule(msgVpnName, ruleName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#getMsgVpnCertMatchingRule");
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
| **ruleName** | **String**| The name of the rule. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleResponse**](MsgVpnCertMatchingRuleResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnCertMatchingRuleAttributeFilter

> MsgVpnCertMatchingRuleAttributeFilterResponse getMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName, opaquePassword, select)

Get a Certificate Matching Rule Attribute Filter object.

Get a Certificate Matching Rule Attribute Filter object.  A Cert Matching Rule Attribute Filter compares a username attribute to a string.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: filterName|x||| msgVpnName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleAttributeFilterResponse result = apiInstance.getMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#getMsgVpnCertMatchingRuleAttributeFilter");
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
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleAttributeFilterResponse**](MsgVpnCertMatchingRuleAttributeFilterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule Attribute Filter object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnCertMatchingRuleAttributeFilters

> MsgVpnCertMatchingRuleAttributeFiltersResponse getMsgVpnCertMatchingRuleAttributeFilters(msgVpnName, ruleName, count, cursor, opaquePassword, where, select)

Get a list of Certificate Matching Rule Attribute Filter objects.

Get a list of Certificate Matching Rule Attribute Filter objects.  A Cert Matching Rule Attribute Filter compares a username attribute to a string.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: filterName|x||| msgVpnName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleAttributeFiltersResponse result = apiInstance.getMsgVpnCertMatchingRuleAttributeFilters(msgVpnName, ruleName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#getMsgVpnCertMatchingRuleAttributeFilters");
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
| **ruleName** | **String**| The name of the rule. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleAttributeFiltersResponse**](MsgVpnCertMatchingRuleAttributeFiltersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Certificate Matching Rule Attribute Filter objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnCertMatchingRuleCondition

> MsgVpnCertMatchingRuleConditionResponse getMsgVpnCertMatchingRuleCondition(msgVpnName, ruleName, source, opaquePassword, select)

Get a Certificate Matching Rule Condition object.

Get a Certificate Matching Rule Condition object.  A Cert Matching Rule Condition compares data extracted from a certificate to a username attribute or an expression.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| ruleName|x||| source|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String source = "source_example"; // String | Certificate field to be compared with the Attribute.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleConditionResponse result = apiInstance.getMsgVpnCertMatchingRuleCondition(msgVpnName, ruleName, source, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#getMsgVpnCertMatchingRuleCondition");
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
| **ruleName** | **String**| The name of the rule. | |
| **source** | **String**| Certificate field to be compared with the Attribute. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleConditionResponse**](MsgVpnCertMatchingRuleConditionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule Condition object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnCertMatchingRuleConditions

> MsgVpnCertMatchingRuleConditionsResponse getMsgVpnCertMatchingRuleConditions(msgVpnName, ruleName, count, cursor, opaquePassword, where, select)

Get a list of Certificate Matching Rule Condition objects.

Get a list of Certificate Matching Rule Condition objects.  A Cert Matching Rule Condition compares data extracted from a certificate to a username attribute or an expression.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| ruleName|x||| source|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleConditionsResponse result = apiInstance.getMsgVpnCertMatchingRuleConditions(msgVpnName, ruleName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#getMsgVpnCertMatchingRuleConditions");
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
| **ruleName** | **String**| The name of the rule. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleConditionsResponse**](MsgVpnCertMatchingRuleConditionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Certificate Matching Rule Condition objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnCertMatchingRules

> MsgVpnCertMatchingRulesResponse getMsgVpnCertMatchingRules(msgVpnName, count, cursor, opaquePassword, where, select)

Get a list of Certificate Matching Rule objects.

Get a list of Certificate Matching Rule objects.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given username.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRulesResponse result = apiInstance.getMsgVpnCertMatchingRules(msgVpnName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#getMsgVpnCertMatchingRules");
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

[**MsgVpnCertMatchingRulesResponse**](MsgVpnCertMatchingRulesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Certificate Matching Rule objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnCertMatchingRule

> MsgVpnCertMatchingRuleResponse replaceMsgVpnCertMatchingRule(msgVpnName, ruleName, body, opaquePassword, select)

Replace a Certificate Matching Rule object.

Replace a Certificate Matching Rule object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given username.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| ruleName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        MsgVpnCertMatchingRule body = new MsgVpnCertMatchingRule(); // MsgVpnCertMatchingRule | The Certificate Matching Rule object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleResponse result = apiInstance.replaceMsgVpnCertMatchingRule(msgVpnName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#replaceMsgVpnCertMatchingRule");
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
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**MsgVpnCertMatchingRule**](MsgVpnCertMatchingRule.md)| The Certificate Matching Rule object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleResponse**](MsgVpnCertMatchingRuleResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnCertMatchingRuleAttributeFilter

> MsgVpnCertMatchingRuleAttributeFilterResponse replaceMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName, body, opaquePassword, select)

Replace a Certificate Matching Rule Attribute Filter object.

Replace a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cert Matching Rule Attribute Filter compares a username attribute to a string.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- filterName|x||x|||| msgVpnName|x||x|||| ruleName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        MsgVpnCertMatchingRuleAttributeFilter body = new MsgVpnCertMatchingRuleAttributeFilter(); // MsgVpnCertMatchingRuleAttributeFilter | The Certificate Matching Rule Attribute Filter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleAttributeFilterResponse result = apiInstance.replaceMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#replaceMsgVpnCertMatchingRuleAttributeFilter");
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
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |
| **body** | [**MsgVpnCertMatchingRuleAttributeFilter**](MsgVpnCertMatchingRuleAttributeFilter.md)| The Certificate Matching Rule Attribute Filter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleAttributeFilterResponse**](MsgVpnCertMatchingRuleAttributeFilterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule Attribute Filter object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnCertMatchingRule

> MsgVpnCertMatchingRuleResponse updateMsgVpnCertMatchingRule(msgVpnName, ruleName, body, opaquePassword, select)

Update a Certificate Matching Rule object.

Update a Certificate Matching Rule object. Any attribute missing from the request will be left unchanged.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given username.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.27.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        MsgVpnCertMatchingRule body = new MsgVpnCertMatchingRule(); // MsgVpnCertMatchingRule | The Certificate Matching Rule object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleResponse result = apiInstance.updateMsgVpnCertMatchingRule(msgVpnName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#updateMsgVpnCertMatchingRule");
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
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**MsgVpnCertMatchingRule**](MsgVpnCertMatchingRule.md)| The Certificate Matching Rule object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleResponse**](MsgVpnCertMatchingRuleResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnCertMatchingRuleAttributeFilter

> MsgVpnCertMatchingRuleAttributeFilterResponse updateMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName, body, opaquePassword, select)

Update a Certificate Matching Rule Attribute Filter object.

Update a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be left unchanged.  A Cert Matching Rule Attribute Filter compares a username attribute to a string.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- filterName|x|x|||| msgVpnName|x|x|||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertMatchingRuleApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertMatchingRuleApi apiInstance = new CertMatchingRuleApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        MsgVpnCertMatchingRuleAttributeFilter body = new MsgVpnCertMatchingRuleAttributeFilter(); // MsgVpnCertMatchingRuleAttributeFilter | The Certificate Matching Rule Attribute Filter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnCertMatchingRuleAttributeFilterResponse result = apiInstance.updateMsgVpnCertMatchingRuleAttributeFilter(msgVpnName, ruleName, filterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertMatchingRuleApi#updateMsgVpnCertMatchingRuleAttributeFilter");
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
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |
| **body** | [**MsgVpnCertMatchingRuleAttributeFilter**](MsgVpnCertMatchingRuleAttributeFilter.md)| The Certificate Matching Rule Attribute Filter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnCertMatchingRuleAttributeFilterResponse**](MsgVpnCertMatchingRuleAttributeFilterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Matching Rule Attribute Filter object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

