# DmrClusterApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createDmrCluster**](DmrClusterApi.md#createDmrCluster) | **POST** /dmrClusters | Create a Cluster object. |
| [**createDmrClusterCertMatchingRule**](DmrClusterApi.md#createDmrClusterCertMatchingRule) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules | Create a Certificate Matching Rule object. |
| [**createDmrClusterCertMatchingRuleAttributeFilter**](DmrClusterApi.md#createDmrClusterCertMatchingRuleAttributeFilter) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object. |
| [**createDmrClusterCertMatchingRuleCondition**](DmrClusterApi.md#createDmrClusterCertMatchingRuleCondition) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object. |
| [**createDmrClusterLink**](DmrClusterApi.md#createDmrClusterLink) | **POST** /dmrClusters/{dmrClusterName}/links | Create a Link object. |
| [**createDmrClusterLinkAttribute**](DmrClusterApi.md#createDmrClusterLinkAttribute) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes | Create a Link Attribute object. |
| [**createDmrClusterLinkRemoteAddress**](DmrClusterApi.md#createDmrClusterLinkRemoteAddress) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Create a Remote Address object. |
| [**createDmrClusterLinkTlsTrustedCommonName**](DmrClusterApi.md#createDmrClusterLinkTlsTrustedCommonName) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Create a Trusted Common Name object. |
| [**deleteDmrCluster**](DmrClusterApi.md#deleteDmrCluster) | **DELETE** /dmrClusters/{dmrClusterName} | Delete a Cluster object. |
| [**deleteDmrClusterCertMatchingRule**](DmrClusterApi.md#deleteDmrClusterCertMatchingRule) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object. |
| [**deleteDmrClusterCertMatchingRuleAttributeFilter**](DmrClusterApi.md#deleteDmrClusterCertMatchingRuleAttributeFilter) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object. |
| [**deleteDmrClusterCertMatchingRuleCondition**](DmrClusterApi.md#deleteDmrClusterCertMatchingRuleCondition) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object. |
| [**deleteDmrClusterLink**](DmrClusterApi.md#deleteDmrClusterLink) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Delete a Link object. |
| [**deleteDmrClusterLinkAttribute**](DmrClusterApi.md#deleteDmrClusterLinkAttribute) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes/{attributeName},{attributeValue} | Delete a Link Attribute object. |
| [**deleteDmrClusterLinkRemoteAddress**](DmrClusterApi.md#deleteDmrClusterLinkRemoteAddress) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Delete a Remote Address object. |
| [**deleteDmrClusterLinkTlsTrustedCommonName**](DmrClusterApi.md#deleteDmrClusterLinkTlsTrustedCommonName) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object. |
| [**getDmrCluster**](DmrClusterApi.md#getDmrCluster) | **GET** /dmrClusters/{dmrClusterName} | Get a Cluster object. |
| [**getDmrClusterCertMatchingRule**](DmrClusterApi.md#getDmrClusterCertMatchingRule) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object. |
| [**getDmrClusterCertMatchingRuleAttributeFilter**](DmrClusterApi.md#getDmrClusterCertMatchingRuleAttributeFilter) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object. |
| [**getDmrClusterCertMatchingRuleAttributeFilters**](DmrClusterApi.md#getDmrClusterCertMatchingRuleAttributeFilters) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects. |
| [**getDmrClusterCertMatchingRuleCondition**](DmrClusterApi.md#getDmrClusterCertMatchingRuleCondition) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object. |
| [**getDmrClusterCertMatchingRuleConditions**](DmrClusterApi.md#getDmrClusterCertMatchingRuleConditions) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects. |
| [**getDmrClusterCertMatchingRules**](DmrClusterApi.md#getDmrClusterCertMatchingRules) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules | Get a list of Certificate Matching Rule objects. |
| [**getDmrClusterLink**](DmrClusterApi.md#getDmrClusterLink) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Get a Link object. |
| [**getDmrClusterLinkAttribute**](DmrClusterApi.md#getDmrClusterLinkAttribute) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes/{attributeName},{attributeValue} | Get a Link Attribute object. |
| [**getDmrClusterLinkAttributes**](DmrClusterApi.md#getDmrClusterLinkAttributes) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes | Get a list of Link Attribute objects. |
| [**getDmrClusterLinkRemoteAddress**](DmrClusterApi.md#getDmrClusterLinkRemoteAddress) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Get a Remote Address object. |
| [**getDmrClusterLinkRemoteAddresses**](DmrClusterApi.md#getDmrClusterLinkRemoteAddresses) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Get a list of Remote Address objects. |
| [**getDmrClusterLinkTlsTrustedCommonName**](DmrClusterApi.md#getDmrClusterLinkTlsTrustedCommonName) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object. |
| [**getDmrClusterLinkTlsTrustedCommonNames**](DmrClusterApi.md#getDmrClusterLinkTlsTrustedCommonNames) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects. |
| [**getDmrClusterLinks**](DmrClusterApi.md#getDmrClusterLinks) | **GET** /dmrClusters/{dmrClusterName}/links | Get a list of Link objects. |
| [**getDmrClusters**](DmrClusterApi.md#getDmrClusters) | **GET** /dmrClusters | Get a list of Cluster objects. |
| [**replaceDmrCluster**](DmrClusterApi.md#replaceDmrCluster) | **PUT** /dmrClusters/{dmrClusterName} | Replace a Cluster object. |
| [**replaceDmrClusterCertMatchingRule**](DmrClusterApi.md#replaceDmrClusterCertMatchingRule) | **PUT** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object. |
| [**replaceDmrClusterCertMatchingRuleAttributeFilter**](DmrClusterApi.md#replaceDmrClusterCertMatchingRuleAttributeFilter) | **PUT** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object. |
| [**replaceDmrClusterLink**](DmrClusterApi.md#replaceDmrClusterLink) | **PUT** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Replace a Link object. |
| [**updateDmrCluster**](DmrClusterApi.md#updateDmrCluster) | **PATCH** /dmrClusters/{dmrClusterName} | Update a Cluster object. |
| [**updateDmrClusterCertMatchingRule**](DmrClusterApi.md#updateDmrClusterCertMatchingRule) | **PATCH** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object. |
| [**updateDmrClusterCertMatchingRuleAttributeFilter**](DmrClusterApi.md#updateDmrClusterCertMatchingRuleAttributeFilter) | **PATCH** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object. |
| [**updateDmrClusterLink**](DmrClusterApi.md#updateDmrClusterLink) | **PATCH** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Update a Link object. |



## createDmrCluster

> DmrClusterResponse createDmrCluster(body, opaquePassword, select)

Create a Cluster object.

Create a Cluster object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x||x authenticationClientCertContent||||x||x authenticationClientCertPassword||||x|| dmrClusterName|x|x|||| nodeName|||x||| tlsServerCertEnforceTrustedCommonNameEnabled|||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        DmrCluster body = new DmrCluster(); // DmrCluster | The Cluster object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterResponse result = apiInstance.createDmrCluster(body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrCluster");
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
| **body** | [**DmrCluster**](DmrCluster.md)| The Cluster object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Cluster object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createDmrClusterCertMatchingRule

> DmrClusterCertMatchingRuleResponse createDmrClusterCertMatchingRule(dmrClusterName, body, opaquePassword, select)

Create a Certificate Matching Rule object.

Create a Certificate Matching Rule object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        DmrClusterCertMatchingRule body = new DmrClusterCertMatchingRule(); // DmrClusterCertMatchingRule | The Certificate Matching Rule object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleResponse result = apiInstance.createDmrClusterCertMatchingRule(dmrClusterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterCertMatchingRule");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **body** | [**DmrClusterCertMatchingRule**](DmrClusterCertMatchingRule.md)| The Certificate Matching Rule object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleResponse**](DmrClusterCertMatchingRuleResponse.md)

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


## createDmrClusterCertMatchingRuleAttributeFilter

> DmrClusterCertMatchingRuleAttributeFilterResponse createDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, body, opaquePassword, select)

Create a Certificate Matching Rule Attribute Filter object.

Create a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| filterName|x|x|||| ruleName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        DmrClusterCertMatchingRuleAttributeFilter body = new DmrClusterCertMatchingRuleAttributeFilter(); // DmrClusterCertMatchingRuleAttributeFilter | The Certificate Matching Rule Attribute Filter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleAttributeFilterResponse result = apiInstance.createDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterCertMatchingRuleAttributeFilter");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**DmrClusterCertMatchingRuleAttributeFilter**](DmrClusterCertMatchingRuleAttributeFilter.md)| The Certificate Matching Rule Attribute Filter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleAttributeFilterResponse**](DmrClusterCertMatchingRuleAttributeFilterResponse.md)

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


## createDmrClusterCertMatchingRuleCondition

> DmrClusterCertMatchingRuleConditionResponse createDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, body, opaquePassword, select)

Create a Certificate Matching Rule Condition object.

Create a Certificate Matching Rule Condition object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| ruleName|x||x||| source|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        DmrClusterCertMatchingRuleCondition body = new DmrClusterCertMatchingRuleCondition(); // DmrClusterCertMatchingRuleCondition | The Certificate Matching Rule Condition object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleConditionResponse result = apiInstance.createDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterCertMatchingRuleCondition");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**DmrClusterCertMatchingRuleCondition**](DmrClusterCertMatchingRuleCondition.md)| The Certificate Matching Rule Condition object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleConditionResponse**](DmrClusterCertMatchingRuleConditionResponse.md)

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


## createDmrClusterLink

> DmrClusterLinkResponse createDmrClusterLink(dmrClusterName, body, opaquePassword, select)

Create a Link object.

Create a Link object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x||x dmrClusterName|x||x||| remoteNodeName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        DmrClusterLink body = new DmrClusterLink(); // DmrClusterLink | The Link object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkResponse result = apiInstance.createDmrClusterLink(dmrClusterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterLink");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **body** | [**DmrClusterLink**](DmrClusterLink.md)| The Link object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Link object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createDmrClusterLinkAttribute

> DmrClusterLinkAttributeResponse createDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, body, opaquePassword, select)

Create a Link Attribute object.

Create a Link Attribute object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: attributeName|x|x|||| attributeValue|x|x|||| dmrClusterName|x||x||| remoteNodeName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        DmrClusterLinkAttribute body = new DmrClusterLinkAttribute(); // DmrClusterLinkAttribute | The Link Attribute object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkAttributeResponse result = apiInstance.createDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterLinkAttribute");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **body** | [**DmrClusterLinkAttribute**](DmrClusterLinkAttribute.md)| The Link Attribute object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkAttributeResponse**](DmrClusterLinkAttributeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Link Attribute object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createDmrClusterLinkRemoteAddress

> DmrClusterLinkRemoteAddressResponse createDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, body, opaquePassword, select)

Create a Remote Address object.

Create a Remote Address object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| remoteAddress|x|x|||| remoteNodeName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        DmrClusterLinkRemoteAddress body = new DmrClusterLinkRemoteAddress(); // DmrClusterLinkRemoteAddress | The Remote Address object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkRemoteAddressResponse result = apiInstance.createDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterLinkRemoteAddress");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **body** | [**DmrClusterLinkRemoteAddress**](DmrClusterLinkRemoteAddress.md)| The Remote Address object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkRemoteAddressResponse**](DmrClusterLinkRemoteAddressResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Address object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createDmrClusterLinkTlsTrustedCommonName

> DmrClusterLinkTlsTrustedCommonNameResponse createDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, body, opaquePassword, select)

Create a Trusted Common Name object.

Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||x| remoteNodeName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        DmrClusterLinkTlsTrustedCommonName body = new DmrClusterLinkTlsTrustedCommonName(); // DmrClusterLinkTlsTrustedCommonName | The Trusted Common Name object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkTlsTrustedCommonNameResponse result = apiInstance.createDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#createDmrClusterLinkTlsTrustedCommonName");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **body** | [**DmrClusterLinkTlsTrustedCommonName**](DmrClusterLinkTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkTlsTrustedCommonNameResponse**](DmrClusterLinkTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Trusted Common Name object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteDmrCluster

> SempMetaOnlyResponse deleteDmrCluster(dmrClusterName)

Delete a Cluster object.

Delete a Cluster object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrCluster(dmrClusterName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrCluster");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |

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


## deleteDmrClusterCertMatchingRule

> SempMetaOnlyResponse deleteDmrClusterCertMatchingRule(dmrClusterName, ruleName)

Delete a Certificate Matching Rule object.

Delete a Certificate Matching Rule object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterCertMatchingRule(dmrClusterName, ruleName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterCertMatchingRule");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
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


## deleteDmrClusterCertMatchingRuleAttributeFilter

> SempMetaOnlyResponse deleteDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName)

Delete a Certificate Matching Rule Attribute Filter object.

Delete a Certificate Matching Rule Attribute Filter object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterCertMatchingRuleAttributeFilter");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
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


## deleteDmrClusterCertMatchingRuleCondition

> SempMetaOnlyResponse deleteDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, source)

Delete a Certificate Matching Rule Condition object.

Delete a Certificate Matching Rule Condition object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String source = "source_example"; // String | Certificate field to be compared with the Attribute.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, source);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterCertMatchingRuleCondition");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
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


## deleteDmrClusterLink

> SempMetaOnlyResponse deleteDmrClusterLink(dmrClusterName, remoteNodeName)

Delete a Link object.

Delete a Link object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLink(dmrClusterName, remoteNodeName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLink");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |

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


## deleteDmrClusterLinkAttribute

> SempMetaOnlyResponse deleteDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, attributeName, attributeValue)

Delete a Link Attribute object.

Delete a Link Attribute object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String attributeName = "attributeName_example"; // String | The name of the Attribute.
        String attributeValue = "attributeValue_example"; // String | The value of the Attribute.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, attributeName, attributeValue);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLinkAttribute");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
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


## deleteDmrClusterLinkRemoteAddress

> SempMetaOnlyResponse deleteDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress)

Delete a Remote Address object.

Delete a Remote Address object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String remoteAddress = "remoteAddress_example"; // String | The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed).
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLinkRemoteAddress");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **remoteAddress** | **String**| The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed). | |

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


## deleteDmrClusterLinkTlsTrustedCommonName

> SempMetaOnlyResponse deleteDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName)

Delete a Trusted Common Name object.

Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates via config-sync.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String tlsTrustedCommonName = "tlsTrustedCommonName_example"; // String | The expected trusted common name of the remote certificate.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLinkTlsTrustedCommonName");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **tlsTrustedCommonName** | **String**| The expected trusted common name of the remote certificate. | |

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


## getDmrCluster

> DmrClusterResponse getDmrCluster(dmrClusterName, opaquePassword, select)

Get a Cluster object.

Get a Cluster object.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| dmrClusterName|x||| tlsServerCertEnforceTrustedCommonNameEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterResponse result = apiInstance.getDmrCluster(dmrClusterName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrCluster");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Cluster object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterCertMatchingRule

> DmrClusterCertMatchingRuleResponse getDmrClusterCertMatchingRule(dmrClusterName, ruleName, opaquePassword, select)

Get a Certificate Matching Rule object.

Get a Certificate Matching Rule object.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleResponse result = apiInstance.getDmrClusterCertMatchingRule(dmrClusterName, ruleName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterCertMatchingRule");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleResponse**](DmrClusterCertMatchingRuleResponse.md)

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


## getDmrClusterCertMatchingRuleAttributeFilter

> DmrClusterCertMatchingRuleAttributeFilterResponse getDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, opaquePassword, select)

Get a Certificate Matching Rule Attribute Filter object.

Get a Certificate Matching Rule Attribute Filter object.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| filterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleAttributeFilterResponse result = apiInstance.getDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterCertMatchingRuleAttributeFilter");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleAttributeFilterResponse**](DmrClusterCertMatchingRuleAttributeFilterResponse.md)

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


## getDmrClusterCertMatchingRuleAttributeFilters

> DmrClusterCertMatchingRuleAttributeFiltersResponse getDmrClusterCertMatchingRuleAttributeFilters(dmrClusterName, ruleName, count, cursor, opaquePassword, where, select)

Get a list of Certificate Matching Rule Attribute Filter objects.

Get a list of Certificate Matching Rule Attribute Filter objects.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| filterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleAttributeFiltersResponse result = apiInstance.getDmrClusterCertMatchingRuleAttributeFilters(dmrClusterName, ruleName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterCertMatchingRuleAttributeFilters");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleAttributeFiltersResponse**](DmrClusterCertMatchingRuleAttributeFiltersResponse.md)

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


## getDmrClusterCertMatchingRuleCondition

> DmrClusterCertMatchingRuleConditionResponse getDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, source, opaquePassword, select)

Get a Certificate Matching Rule Condition object.

Get a Certificate Matching Rule Condition object.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x||| source|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String source = "source_example"; // String | Certificate field to be compared with the Attribute.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleConditionResponse result = apiInstance.getDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, source, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterCertMatchingRuleCondition");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **source** | **String**| Certificate field to be compared with the Attribute. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleConditionResponse**](DmrClusterCertMatchingRuleConditionResponse.md)

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


## getDmrClusterCertMatchingRuleConditions

> DmrClusterCertMatchingRuleConditionsResponse getDmrClusterCertMatchingRuleConditions(dmrClusterName, ruleName, count, cursor, opaquePassword, where, select)

Get a list of Certificate Matching Rule Condition objects.

Get a list of Certificate Matching Rule Condition objects.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x||| source|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleConditionsResponse result = apiInstance.getDmrClusterCertMatchingRuleConditions(dmrClusterName, ruleName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterCertMatchingRuleConditions");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleConditionsResponse**](DmrClusterCertMatchingRuleConditionsResponse.md)

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


## getDmrClusterCertMatchingRules

> DmrClusterCertMatchingRulesResponse getDmrClusterCertMatchingRules(dmrClusterName, count, cursor, opaquePassword, where, select)

Get a list of Certificate Matching Rule objects.

Get a list of Certificate Matching Rule objects.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRulesResponse result = apiInstance.getDmrClusterCertMatchingRules(dmrClusterName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterCertMatchingRules");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRulesResponse**](DmrClusterCertMatchingRulesResponse.md)

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


## getDmrClusterLink

> DmrClusterLinkResponse getDmrClusterLink(dmrClusterName, remoteNodeName, opaquePassword, select)

Get a Link object.

Get a Link object.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkResponse result = apiInstance.getDmrClusterLink(dmrClusterName, remoteNodeName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLink");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Link object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinkAttribute

> DmrClusterLinkAttributeResponse getDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, attributeName, attributeValue, opaquePassword, select)

Get a Link Attribute object.

Get a Link Attribute object.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: attributeName|x||| attributeValue|x||| dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String attributeName = "attributeName_example"; // String | The name of the Attribute.
        String attributeValue = "attributeValue_example"; // String | The value of the Attribute.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkAttributeResponse result = apiInstance.getDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, attributeName, attributeValue, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkAttribute");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **attributeName** | **String**| The name of the Attribute. | |
| **attributeValue** | **String**| The value of the Attribute. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkAttributeResponse**](DmrClusterLinkAttributeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Link Attribute object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinkAttributes

> DmrClusterLinkAttributesResponse getDmrClusterLinkAttributes(dmrClusterName, remoteNodeName, count, cursor, opaquePassword, where, select)

Get a list of Link Attribute objects.

Get a list of Link Attribute objects.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: attributeName|x||| attributeValue|x||| dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkAttributesResponse result = apiInstance.getDmrClusterLinkAttributes(dmrClusterName, remoteNodeName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkAttributes");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkAttributesResponse**](DmrClusterLinkAttributesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Link Attribute objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinkRemoteAddress

> DmrClusterLinkRemoteAddressResponse getDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress, opaquePassword, select)

Get a Remote Address object.

Get a Remote Address object.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| remoteAddress|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String remoteAddress = "remoteAddress_example"; // String | The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed).
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkRemoteAddressResponse result = apiInstance.getDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkRemoteAddress");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **remoteAddress** | **String**| The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed). | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkRemoteAddressResponse**](DmrClusterLinkRemoteAddressResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Address object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinkRemoteAddresses

> DmrClusterLinkRemoteAddressesResponse getDmrClusterLinkRemoteAddresses(dmrClusterName, remoteNodeName, opaquePassword, where, select)

Get a list of Remote Address objects.

Get a list of Remote Address objects.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| remoteAddress|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkRemoteAddressesResponse result = apiInstance.getDmrClusterLinkRemoteAddresses(dmrClusterName, remoteNodeName, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkRemoteAddresses");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkRemoteAddressesResponse**](DmrClusterLinkRemoteAddressesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Remote Address objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinkTlsTrustedCommonName

> DmrClusterLinkTlsTrustedCommonNameResponse getDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName, opaquePassword, select)

Get a Trusted Common Name object.

Get a Trusted Common Name object.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||x| remoteNodeName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String tlsTrustedCommonName = "tlsTrustedCommonName_example"; // String | The expected trusted common name of the remote certificate.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkTlsTrustedCommonNameResponse result = apiInstance.getDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkTlsTrustedCommonName");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **tlsTrustedCommonName** | **String**| The expected trusted common name of the remote certificate. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkTlsTrustedCommonNameResponse**](DmrClusterLinkTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Trusted Common Name object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinkTlsTrustedCommonNames

> DmrClusterLinkTlsTrustedCommonNamesResponse getDmrClusterLinkTlsTrustedCommonNames(dmrClusterName, remoteNodeName, opaquePassword, where, select)

Get a list of Trusted Common Name objects.

Get a list of Trusted Common Name objects.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||x| remoteNodeName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkTlsTrustedCommonNamesResponse result = apiInstance.getDmrClusterLinkTlsTrustedCommonNames(dmrClusterName, remoteNodeName, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkTlsTrustedCommonNames");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkTlsTrustedCommonNamesResponse**](DmrClusterLinkTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Trusted Common Name objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusterLinks

> DmrClusterLinksResponse getDmrClusterLinks(dmrClusterName, count, cursor, opaquePassword, where, select)

Get a list of Link objects.

Get a list of Link objects.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinksResponse result = apiInstance.getDmrClusterLinks(dmrClusterName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinks");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinksResponse**](DmrClusterLinksResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Link objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getDmrClusters

> DmrClustersResponse getDmrClusters(count, cursor, opaquePassword, where, select)

Get a list of Cluster objects.

Get a list of Cluster objects.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| dmrClusterName|x||| tlsServerCertEnforceTrustedCommonNameEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClustersResponse result = apiInstance.getDmrClusters(count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#getDmrClusters");
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

[**DmrClustersResponse**](DmrClustersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Cluster objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceDmrCluster

> DmrClusterResponse replaceDmrCluster(dmrClusterName, body, opaquePassword, select)

Replace a Cluster object.

Replace a Cluster object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword||||x|x||x authenticationClientCertContent||||x|x||x authenticationClientCertPassword||||x|x|| directOnlyEnabled||x||||| dmrClusterName|x||x|||| nodeName|||x|||| tlsServerCertEnforceTrustedCommonNameEnabled||||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        DmrCluster body = new DmrCluster(); // DmrCluster | The Cluster object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterResponse result = apiInstance.replaceDmrCluster(dmrClusterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#replaceDmrCluster");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **body** | [**DmrCluster**](DmrCluster.md)| The Cluster object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Cluster object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceDmrClusterCertMatchingRule

> DmrClusterCertMatchingRuleResponse replaceDmrClusterCertMatchingRule(dmrClusterName, ruleName, body, opaquePassword, select)

Replace a Certificate Matching Rule object.

Replace a Certificate Matching Rule object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- dmrClusterName|x||x|||| ruleName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        DmrClusterCertMatchingRule body = new DmrClusterCertMatchingRule(); // DmrClusterCertMatchingRule | The Certificate Matching Rule object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleResponse result = apiInstance.replaceDmrClusterCertMatchingRule(dmrClusterName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#replaceDmrClusterCertMatchingRule");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**DmrClusterCertMatchingRule**](DmrClusterCertMatchingRule.md)| The Certificate Matching Rule object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleResponse**](DmrClusterCertMatchingRuleResponse.md)

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


## replaceDmrClusterCertMatchingRuleAttributeFilter

> DmrClusterCertMatchingRuleAttributeFilterResponse replaceDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, body, opaquePassword, select)

Replace a Certificate Matching Rule Attribute Filter object.

Replace a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- dmrClusterName|x||x|||| filterName|x||x|||| ruleName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        DmrClusterCertMatchingRuleAttributeFilter body = new DmrClusterCertMatchingRuleAttributeFilter(); // DmrClusterCertMatchingRuleAttributeFilter | The Certificate Matching Rule Attribute Filter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleAttributeFilterResponse result = apiInstance.replaceDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#replaceDmrClusterCertMatchingRuleAttributeFilter");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |
| **body** | [**DmrClusterCertMatchingRuleAttributeFilter**](DmrClusterCertMatchingRuleAttributeFilter.md)| The Certificate Matching Rule Attribute Filter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleAttributeFilterResponse**](DmrClusterCertMatchingRuleAttributeFilterResponse.md)

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


## replaceDmrClusterLink

> DmrClusterLinkResponse replaceDmrClusterLink(dmrClusterName, remoteNodeName, body, opaquePassword, select)

Replace a Link object.

Replace a Link object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword||||x|x||x authenticationScheme|||||x|| dmrClusterName|x||x|||| egressFlowWindowSize|||||x|| initiator|||||x|| remoteNodeName|x||x|||| span|||||x|| transportCompressedEnabled|||||x|| transportTlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        DmrClusterLink body = new DmrClusterLink(); // DmrClusterLink | The Link object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkResponse result = apiInstance.replaceDmrClusterLink(dmrClusterName, remoteNodeName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#replaceDmrClusterLink");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **body** | [**DmrClusterLink**](DmrClusterLink.md)| The Link object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Link object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateDmrCluster

> DmrClusterResponse updateDmrCluster(dmrClusterName, body, opaquePassword, select)

Update a Cluster object.

Update a Cluster object. Any attribute missing from the request will be left unchanged.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword|||x|x||x authenticationClientCertContent|||x|x||x authenticationClientCertPassword|||x|x|| directOnlyEnabled||x|||| dmrClusterName|x|x|||| nodeName||x|||| tlsServerCertEnforceTrustedCommonNameEnabled|||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        DmrCluster body = new DmrCluster(); // DmrCluster | The Cluster object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterResponse result = apiInstance.updateDmrCluster(dmrClusterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#updateDmrCluster");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **body** | [**DmrCluster**](DmrCluster.md)| The Cluster object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Cluster object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateDmrClusterCertMatchingRule

> DmrClusterCertMatchingRuleResponse updateDmrClusterCertMatchingRule(dmrClusterName, ruleName, body, opaquePassword, select)

Update a Certificate Matching Rule object.

Update a Certificate Matching Rule object. Any attribute missing from the request will be left unchanged.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- dmrClusterName|x|x|||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        DmrClusterCertMatchingRule body = new DmrClusterCertMatchingRule(); // DmrClusterCertMatchingRule | The Certificate Matching Rule object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleResponse result = apiInstance.updateDmrClusterCertMatchingRule(dmrClusterName, ruleName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#updateDmrClusterCertMatchingRule");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **body** | [**DmrClusterCertMatchingRule**](DmrClusterCertMatchingRule.md)| The Certificate Matching Rule object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleResponse**](DmrClusterCertMatchingRuleResponse.md)

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


## updateDmrClusterCertMatchingRuleAttributeFilter

> DmrClusterCertMatchingRuleAttributeFilterResponse updateDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, body, opaquePassword, select)

Update a Certificate Matching Rule Attribute Filter object.

Update a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be left unchanged.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- dmrClusterName|x|x|||| filterName|x|x|||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String ruleName = "ruleName_example"; // String | The name of the rule.
        String filterName = "filterName_example"; // String | The name of the filter.
        DmrClusterCertMatchingRuleAttributeFilter body = new DmrClusterCertMatchingRuleAttributeFilter(); // DmrClusterCertMatchingRuleAttributeFilter | The Certificate Matching Rule Attribute Filter object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterCertMatchingRuleAttributeFilterResponse result = apiInstance.updateDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#updateDmrClusterCertMatchingRuleAttributeFilter");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **ruleName** | **String**| The name of the rule. | |
| **filterName** | **String**| The name of the filter. | |
| **body** | [**DmrClusterCertMatchingRuleAttributeFilter**](DmrClusterCertMatchingRuleAttributeFilter.md)| The Certificate Matching Rule Attribute Filter object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterCertMatchingRuleAttributeFilterResponse**](DmrClusterCertMatchingRuleAttributeFilterResponse.md)

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


## updateDmrClusterLink

> DmrClusterLinkResponse updateDmrClusterLink(dmrClusterName, remoteNodeName, body, opaquePassword, select)

Update a Link object.

Update a Link object. Any attribute missing from the request will be left unchanged.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword|||x|x||x authenticationScheme||||x|| dmrClusterName|x|x|||| egressFlowWindowSize||||x|| initiator||||x|| remoteNodeName|x|x|||| span||||x|| transportCompressedEnabled||||x|| transportTlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.DmrClusterApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        DmrClusterApi apiInstance = new DmrClusterApi(defaultClient);
        String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
        String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
        DmrClusterLink body = new DmrClusterLink(); // DmrClusterLink | The Link object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            DmrClusterLinkResponse result = apiInstance.updateDmrClusterLink(dmrClusterName, remoteNodeName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DmrClusterApi#updateDmrClusterLink");
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
| **dmrClusterName** | **String**| The name of the Cluster. | |
| **remoteNodeName** | **String**| The name of the node at the remote end of the Link. | |
| **body** | [**DmrClusterLink**](DmrClusterLink.md)| The Link object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Link object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

