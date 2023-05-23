# BridgeApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createMsgVpnBridge**](BridgeApi.md#createMsgVpnBridge) | **POST** /msgVpns/{msgVpnName}/bridges | Create a Bridge object. |
| [**createMsgVpnBridgeRemoteMsgVpn**](BridgeApi.md#createMsgVpnBridgeRemoteMsgVpn) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Create a Remote Message VPN object. |
| [**createMsgVpnBridgeRemoteSubscription**](BridgeApi.md#createMsgVpnBridgeRemoteSubscription) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Create a Remote Subscription object. |
| [**createMsgVpnBridgeTlsTrustedCommonName**](BridgeApi.md#createMsgVpnBridgeTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Create a Trusted Common Name object. |
| [**deleteMsgVpnBridge**](BridgeApi.md#deleteMsgVpnBridge) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Delete a Bridge object. |
| [**deleteMsgVpnBridgeRemoteMsgVpn**](BridgeApi.md#deleteMsgVpnBridgeRemoteMsgVpn) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Delete a Remote Message VPN object. |
| [**deleteMsgVpnBridgeRemoteSubscription**](BridgeApi.md#deleteMsgVpnBridgeRemoteSubscription) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Delete a Remote Subscription object. |
| [**deleteMsgVpnBridgeTlsTrustedCommonName**](BridgeApi.md#deleteMsgVpnBridgeTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object. |
| [**getMsgVpnBridge**](BridgeApi.md#getMsgVpnBridge) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Get a Bridge object. |
| [**getMsgVpnBridgeRemoteMsgVpn**](BridgeApi.md#getMsgVpnBridgeRemoteMsgVpn) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Get a Remote Message VPN object. |
| [**getMsgVpnBridgeRemoteMsgVpns**](BridgeApi.md#getMsgVpnBridgeRemoteMsgVpns) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Get a list of Remote Message VPN objects. |
| [**getMsgVpnBridgeRemoteSubscription**](BridgeApi.md#getMsgVpnBridgeRemoteSubscription) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Get a Remote Subscription object. |
| [**getMsgVpnBridgeRemoteSubscriptions**](BridgeApi.md#getMsgVpnBridgeRemoteSubscriptions) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Get a list of Remote Subscription objects. |
| [**getMsgVpnBridgeTlsTrustedCommonName**](BridgeApi.md#getMsgVpnBridgeTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object. |
| [**getMsgVpnBridgeTlsTrustedCommonNames**](BridgeApi.md#getMsgVpnBridgeTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects. |
| [**getMsgVpnBridges**](BridgeApi.md#getMsgVpnBridges) | **GET** /msgVpns/{msgVpnName}/bridges | Get a list of Bridge objects. |
| [**replaceMsgVpnBridge**](BridgeApi.md#replaceMsgVpnBridge) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Replace a Bridge object. |
| [**replaceMsgVpnBridgeRemoteMsgVpn**](BridgeApi.md#replaceMsgVpnBridgeRemoteMsgVpn) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Replace a Remote Message VPN object. |
| [**updateMsgVpnBridge**](BridgeApi.md#updateMsgVpnBridge) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Update a Bridge object. |
| [**updateMsgVpnBridgeRemoteMsgVpn**](BridgeApi.md#updateMsgVpnBridgeRemoteMsgVpn) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Update a Remote Message VPN object. |



## createMsgVpnBridge

> MsgVpnBridgeResponse createMsgVpnBridge(msgVpnName, body, opaquePassword, select)

Create a Bridge object.

Create a Bridge object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| msgVpnName|x||x||| remoteAuthenticationBasicPassword||||x||x remoteAuthenticationClientCertContent||||x||x remoteAuthenticationClientCertPassword||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        MsgVpnBridge body = new MsgVpnBridge(); // MsgVpnBridge | The Bridge object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeResponse result = apiInstance.createMsgVpnBridge(msgVpnName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#createMsgVpnBridge");
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
| **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Bridge object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createMsgVpnBridgeRemoteMsgVpn

> MsgVpnBridgeRemoteMsgVpnResponse createMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select)

Create a Remote Message VPN object.

Create a Remote Message VPN object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||| bridgeVirtualRouter|x||x||| msgVpnName|x||x||| password||||x||x remoteMsgVpnInterface|x||||| remoteMsgVpnLocation|x|x|||| remoteMsgVpnName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        MsgVpnBridgeRemoteMsgVpn body = new MsgVpnBridgeRemoteMsgVpn(); // MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteMsgVpnResponse result = apiInstance.createMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#createMsgVpnBridgeRemoteMsgVpn");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Message VPN object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createMsgVpnBridgeRemoteSubscription

> MsgVpnBridgeRemoteSubscriptionResponse createMsgVpnBridgeRemoteSubscription(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select)

Create a Remote Subscription object.

Create a Remote Subscription object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||| bridgeVirtualRouter|x||x||| deliverAlwaysEnabled||x|||| msgVpnName|x||x||| remoteSubscriptionTopic|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        MsgVpnBridgeRemoteSubscription body = new MsgVpnBridgeRemoteSubscription(); // MsgVpnBridgeRemoteSubscription | The Remote Subscription object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteSubscriptionResponse result = apiInstance.createMsgVpnBridgeRemoteSubscription(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#createMsgVpnBridgeRemoteSubscription");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **body** | [**MsgVpnBridgeRemoteSubscription**](MsgVpnBridgeRemoteSubscription.md)| The Remote Subscription object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteSubscriptionResponse**](MsgVpnBridgeRemoteSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Subscription object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createMsgVpnBridgeTlsTrustedCommonName

> MsgVpnBridgeTlsTrustedCommonNameResponse createMsgVpnBridgeTlsTrustedCommonName(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select)

Create a Trusted Common Name object.

Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: bridgeName|x||x||x| bridgeVirtualRouter|x||x||x| msgVpnName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        MsgVpnBridgeTlsTrustedCommonName body = new MsgVpnBridgeTlsTrustedCommonName(); // MsgVpnBridgeTlsTrustedCommonName | The Trusted Common Name object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeTlsTrustedCommonNameResponse result = apiInstance.createMsgVpnBridgeTlsTrustedCommonName(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#createMsgVpnBridgeTlsTrustedCommonName");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **body** | [**MsgVpnBridgeTlsTrustedCommonName**](MsgVpnBridgeTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeTlsTrustedCommonNameResponse**](MsgVpnBridgeTlsTrustedCommonNameResponse.md)

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


## deleteMsgVpnBridge

> SempMetaOnlyResponse deleteMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter)

Delete a Bridge object.

Delete a Bridge object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#deleteMsgVpnBridge");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |

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


## deleteMsgVpnBridgeRemoteMsgVpn

> SempMetaOnlyResponse deleteMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface)

Delete a Remote Message VPN object.

Delete a Remote Message VPN object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Remote Message VPN is the Message VPN that the Bridge connects to.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String remoteMsgVpnName = "remoteMsgVpnName_example"; // String | The name of the remote Message VPN.
        String remoteMsgVpnLocation = "remoteMsgVpnLocation_example"; // String | The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \"v:\").
        String remoteMsgVpnInterface = "remoteMsgVpnInterface_example"; // String | The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, `remoteMsgVpnLocation` must not be a virtual router name.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#deleteMsgVpnBridgeRemoteMsgVpn");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **remoteMsgVpnName** | **String**| The name of the remote Message VPN. | |
| **remoteMsgVpnLocation** | **String**| The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). | |
| **remoteMsgVpnInterface** | **String**| The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. | |

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


## deleteMsgVpnBridgeRemoteSubscription

> SempMetaOnlyResponse deleteMsgVpnBridgeRemoteSubscription(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic)

Delete a Remote Subscription object.

Delete a Remote Subscription object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String remoteSubscriptionTopic = "remoteSubscriptionTopic_example"; // String | The topic of the Bridge remote subscription.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnBridgeRemoteSubscription(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#deleteMsgVpnBridgeRemoteSubscription");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **remoteSubscriptionTopic** | **String**| The topic of the Bridge remote subscription. | |

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


## deleteMsgVpnBridgeTlsTrustedCommonName

> SempMetaOnlyResponse deleteMsgVpnBridgeTlsTrustedCommonName(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName)

Delete a Trusted Common Name object.

Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates and replication sites via config-sync.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String tlsTrustedCommonName = "tlsTrustedCommonName_example"; // String | The expected trusted common name of the remote certificate.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteMsgVpnBridgeTlsTrustedCommonName(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#deleteMsgVpnBridgeTlsTrustedCommonName");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
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


## getMsgVpnBridge

> MsgVpnBridgeResponse getMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, select)

Get a Bridge object.

Get a Bridge object.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteAuthenticationBasicPassword||x||x remoteAuthenticationClientCertContent||x||x remoteAuthenticationClientCertPassword||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeResponse result = apiInstance.getMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridge");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Bridge object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnBridgeRemoteMsgVpn

> MsgVpnBridgeRemoteMsgVpnResponse getMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, opaquePassword, select)

Get a Remote Message VPN object.

Get a Remote Message VPN object.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| password||x||x remoteMsgVpnInterface|x||| remoteMsgVpnLocation|x||| remoteMsgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String remoteMsgVpnName = "remoteMsgVpnName_example"; // String | The name of the remote Message VPN.
        String remoteMsgVpnLocation = "remoteMsgVpnLocation_example"; // String | The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \"v:\").
        String remoteMsgVpnInterface = "remoteMsgVpnInterface_example"; // String | The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, `remoteMsgVpnLocation` must not be a virtual router name.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteMsgVpnResponse result = apiInstance.getMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridgeRemoteMsgVpn");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **remoteMsgVpnName** | **String**| The name of the remote Message VPN. | |
| **remoteMsgVpnLocation** | **String**| The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). | |
| **remoteMsgVpnInterface** | **String**| The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Message VPN object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnBridgeRemoteMsgVpns

> MsgVpnBridgeRemoteMsgVpnsResponse getMsgVpnBridgeRemoteMsgVpns(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, where, select)

Get a list of Remote Message VPN objects.

Get a list of Remote Message VPN objects.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| password||x||x remoteMsgVpnInterface|x||| remoteMsgVpnLocation|x||| remoteMsgVpnName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteMsgVpnsResponse result = apiInstance.getMsgVpnBridgeRemoteMsgVpns(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridgeRemoteMsgVpns");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteMsgVpnsResponse**](MsgVpnBridgeRemoteMsgVpnsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Remote Message VPN objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnBridgeRemoteSubscription

> MsgVpnBridgeRemoteSubscriptionResponse getMsgVpnBridgeRemoteSubscription(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, opaquePassword, select)

Get a Remote Subscription object.

Get a Remote Subscription object.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteSubscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String remoteSubscriptionTopic = "remoteSubscriptionTopic_example"; // String | The topic of the Bridge remote subscription.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteSubscriptionResponse result = apiInstance.getMsgVpnBridgeRemoteSubscription(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridgeRemoteSubscription");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **remoteSubscriptionTopic** | **String**| The topic of the Bridge remote subscription. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteSubscriptionResponse**](MsgVpnBridgeRemoteSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Subscription object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnBridgeRemoteSubscriptions

> MsgVpnBridgeRemoteSubscriptionsResponse getMsgVpnBridgeRemoteSubscriptions(msgVpnName, bridgeName, bridgeVirtualRouter, count, cursor, opaquePassword, where, select)

Get a list of Remote Subscription objects.

Get a list of Remote Subscription objects.  A Remote Subscription is a topic subscription used by the Message VPN Bridge to attract messages from the remote message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteSubscriptionTopic|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteSubscriptionsResponse result = apiInstance.getMsgVpnBridgeRemoteSubscriptions(msgVpnName, bridgeName, bridgeVirtualRouter, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridgeRemoteSubscriptions");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10] |
| **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional] |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteSubscriptionsResponse**](MsgVpnBridgeRemoteSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Remote Subscription objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getMsgVpnBridgeTlsTrustedCommonName

> MsgVpnBridgeTlsTrustedCommonNameResponse getMsgVpnBridgeTlsTrustedCommonName(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, opaquePassword, select)

Get a Trusted Common Name object.

Get a Trusted Common Name object.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||x| bridgeVirtualRouter|x||x| msgVpnName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String tlsTrustedCommonName = "tlsTrustedCommonName_example"; // String | The expected trusted common name of the remote certificate.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeTlsTrustedCommonNameResponse result = apiInstance.getMsgVpnBridgeTlsTrustedCommonName(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridgeTlsTrustedCommonName");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **tlsTrustedCommonName** | **String**| The expected trusted common name of the remote certificate. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeTlsTrustedCommonNameResponse**](MsgVpnBridgeTlsTrustedCommonNameResponse.md)

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


## getMsgVpnBridgeTlsTrustedCommonNames

> MsgVpnBridgeTlsTrustedCommonNamesResponse getMsgVpnBridgeTlsTrustedCommonNames(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, where, select)

Get a list of Trusted Common Name objects.

Get a list of Trusted Common Name objects.  The Trusted Common Names for the Bridge are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||x| bridgeVirtualRouter|x||x| msgVpnName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeTlsTrustedCommonNamesResponse result = apiInstance.getMsgVpnBridgeTlsTrustedCommonNames(msgVpnName, bridgeName, bridgeVirtualRouter, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridgeTlsTrustedCommonNames");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeTlsTrustedCommonNamesResponse**](MsgVpnBridgeTlsTrustedCommonNamesResponse.md)

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


## getMsgVpnBridges

> MsgVpnBridgesResponse getMsgVpnBridges(msgVpnName, count, cursor, opaquePassword, where, select)

Get a list of Bridge objects.

Get a list of Bridge objects.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: bridgeName|x||| bridgeVirtualRouter|x||| msgVpnName|x||| remoteAuthenticationBasicPassword||x||x remoteAuthenticationClientCertContent||x||x remoteAuthenticationClientCertPassword||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgesResponse result = apiInstance.getMsgVpnBridges(msgVpnName, count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#getMsgVpnBridges");
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

[**MsgVpnBridgesResponse**](MsgVpnBridgesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Bridge objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnBridge

> MsgVpnBridgeResponse replaceMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select)

Replace a Bridge object.

Replace a Bridge object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- bridgeName|x||x|||| bridgeVirtualRouter|x||x|||| maxTtl|||||x|| msgVpnName|x||x|||| remoteAuthenticationBasicClientUsername|||||x|| remoteAuthenticationBasicPassword||||x|x||x remoteAuthenticationClientCertContent||||x|x||x remoteAuthenticationClientCertPassword||||x|x|| remoteAuthenticationScheme|||||x|| remoteDeliverToOnePriority|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        MsgVpnBridge body = new MsgVpnBridge(); // MsgVpnBridge | The Bridge object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeResponse result = apiInstance.replaceMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#replaceMsgVpnBridge");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Bridge object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceMsgVpnBridgeRemoteMsgVpn

> MsgVpnBridgeRemoteMsgVpnResponse replaceMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, opaquePassword, select)

Replace a Remote Message VPN object.

Replace a Remote Message VPN object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- bridgeName|x||x|||| bridgeVirtualRouter|x||x|||| clientUsername|||||x|| compressedDataEnabled|||||x|| egressFlowWindowSize|||||x|| msgVpnName|x||x|||| password||||x|x||x remoteMsgVpnInterface|x||x|||| remoteMsgVpnLocation|x||x|||| remoteMsgVpnName|x||x|||| tlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String remoteMsgVpnName = "remoteMsgVpnName_example"; // String | The name of the remote Message VPN.
        String remoteMsgVpnLocation = "remoteMsgVpnLocation_example"; // String | The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \"v:\").
        String remoteMsgVpnInterface = "remoteMsgVpnInterface_example"; // String | The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, `remoteMsgVpnLocation` must not be a virtual router name.
        MsgVpnBridgeRemoteMsgVpn body = new MsgVpnBridgeRemoteMsgVpn(); // MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteMsgVpnResponse result = apiInstance.replaceMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#replaceMsgVpnBridgeRemoteMsgVpn");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **remoteMsgVpnName** | **String**| The name of the remote Message VPN. | |
| **remoteMsgVpnLocation** | **String**| The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). | |
| **remoteMsgVpnInterface** | **String**| The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. | |
| **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Message VPN object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnBridge

> MsgVpnBridgeResponse updateMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select)

Update a Bridge object.

Update a Bridge object. Any attribute missing from the request will be left unchanged.  Bridges can be used to link two Message VPNs so that messages published to one Message VPN that match the topic subscriptions set for the bridge are also delivered to the linked Message VPN.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| maxTtl||||x|| msgVpnName|x|x|||| remoteAuthenticationBasicClientUsername||||x|| remoteAuthenticationBasicPassword|||x|x||x remoteAuthenticationClientCertContent|||x|x||x remoteAuthenticationClientCertPassword|||x|x|| remoteAuthenticationScheme||||x|| remoteDeliverToOnePriority||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername| MsgVpnBridge|remoteAuthenticationClientCertPassword|remoteAuthenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        MsgVpnBridge body = new MsgVpnBridge(); // MsgVpnBridge | The Bridge object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeResponse result = apiInstance.updateMsgVpnBridge(msgVpnName, bridgeName, bridgeVirtualRouter, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#updateMsgVpnBridge");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Bridge object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateMsgVpnBridgeRemoteMsgVpn

> MsgVpnBridgeRemoteMsgVpnResponse updateMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, opaquePassword, select)

Update a Remote Message VPN object.

Update a Remote Message VPN object. Any attribute missing from the request will be left unchanged.  The Remote Message VPN is the Message VPN that the Bridge connects to.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- bridgeName|x|x|||| bridgeVirtualRouter|x|x|||| clientUsername||||x|| compressedDataEnabled||||x|| egressFlowWindowSize||||x|| msgVpnName|x|x|||| password|||x|x||x remoteMsgVpnInterface|x|x|||| remoteMsgVpnLocation|x|x|||| remoteMsgVpnName|x|x|||| tlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.BridgeApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        BridgeApi apiInstance = new BridgeApi(defaultClient);
        String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
        String bridgeName = "bridgeName_example"; // String | The name of the Bridge.
        String bridgeVirtualRouter = "bridgeVirtualRouter_example"; // String | The virtual router of the Bridge.
        String remoteMsgVpnName = "remoteMsgVpnName_example"; // String | The name of the remote Message VPN.
        String remoteMsgVpnLocation = "remoteMsgVpnLocation_example"; // String | The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \"v:\").
        String remoteMsgVpnInterface = "remoteMsgVpnInterface_example"; // String | The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, `remoteMsgVpnLocation` must not be a virtual router name.
        MsgVpnBridgeRemoteMsgVpn body = new MsgVpnBridgeRemoteMsgVpn(); // MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            MsgVpnBridgeRemoteMsgVpnResponse result = apiInstance.updateMsgVpnBridgeRemoteMsgVpn(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling BridgeApi#updateMsgVpnBridgeRemoteMsgVpn");
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
| **bridgeName** | **String**| The name of the Bridge. | |
| **bridgeVirtualRouter** | **String**| The virtual router of the Bridge. | |
| **remoteMsgVpnName** | **String**| The name of the remote Message VPN. | |
| **remoteMsgVpnLocation** | **String**| The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;). | |
| **remoteMsgVpnInterface** | **String**| The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name. | |
| **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Remote Message VPN object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

