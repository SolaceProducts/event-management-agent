# CertAuthorityApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createCertAuthority**](CertAuthorityApi.md#createCertAuthority) | **POST** /certAuthorities | Create a Certificate Authority object. |
| [**createCertAuthorityOcspTlsTrustedCommonName**](CertAuthorityApi.md#createCertAuthorityOcspTlsTrustedCommonName) | **POST** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Create an OCSP Responder Trusted Common Name object. |
| [**deleteCertAuthority**](CertAuthorityApi.md#deleteCertAuthority) | **DELETE** /certAuthorities/{certAuthorityName} | Delete a Certificate Authority object. |
| [**deleteCertAuthorityOcspTlsTrustedCommonName**](CertAuthorityApi.md#deleteCertAuthorityOcspTlsTrustedCommonName) | **DELETE** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Delete an OCSP Responder Trusted Common Name object. |
| [**getCertAuthorities**](CertAuthorityApi.md#getCertAuthorities) | **GET** /certAuthorities | Get a list of Certificate Authority objects. |
| [**getCertAuthority**](CertAuthorityApi.md#getCertAuthority) | **GET** /certAuthorities/{certAuthorityName} | Get a Certificate Authority object. |
| [**getCertAuthorityOcspTlsTrustedCommonName**](CertAuthorityApi.md#getCertAuthorityOcspTlsTrustedCommonName) | **GET** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Get an OCSP Responder Trusted Common Name object. |
| [**getCertAuthorityOcspTlsTrustedCommonNames**](CertAuthorityApi.md#getCertAuthorityOcspTlsTrustedCommonNames) | **GET** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Get a list of OCSP Responder Trusted Common Name objects. |
| [**replaceCertAuthority**](CertAuthorityApi.md#replaceCertAuthority) | **PUT** /certAuthorities/{certAuthorityName} | Replace a Certificate Authority object. |
| [**updateCertAuthority**](CertAuthorityApi.md#updateCertAuthority) | **PATCH** /certAuthorities/{certAuthorityName} | Update a Certificate Authority object. |



## createCertAuthority

> CertAuthorityResponse createCertAuthority(body, opaquePassword, select)

Create a Certificate Authority object.

Create a Certificate Authority object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x|x|||x| certContent|||||x| crlDayList|||||x| crlTimeList|||||x| crlUrl|||||x| ocspNonResponderCertEnabled|||||x| ocspOverrideUrl|||||x| ocspTimeout|||||x| revocationCheckEnabled|||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- CertAuthority|crlDayList|crlTimeList| CertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities and domainCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        CertAuthority body = new CertAuthority(); // CertAuthority | The Certificate Authority object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityResponse result = apiInstance.createCertAuthority(body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#createCertAuthority");
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
| **body** | [**CertAuthority**](CertAuthority.md)| The Certificate Authority object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**CertAuthorityResponse**](CertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Authority object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## createCertAuthorityOcspTlsTrustedCommonName

> CertAuthorityOcspTlsTrustedCommonNameResponse createCertAuthorityOcspTlsTrustedCommonName(certAuthorityName, body, opaquePassword, select)

Create an OCSP Responder Trusted Common Name object.

Create an OCSP Responder Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: certAuthorityName|x||x||x| ocspTlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        CertAuthorityOcspTlsTrustedCommonName body = new CertAuthorityOcspTlsTrustedCommonName(); // CertAuthorityOcspTlsTrustedCommonName | The OCSP Responder Trusted Common Name object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityOcspTlsTrustedCommonNameResponse result = apiInstance.createCertAuthorityOcspTlsTrustedCommonName(certAuthorityName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#createCertAuthorityOcspTlsTrustedCommonName");
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
| **body** | [**CertAuthorityOcspTlsTrustedCommonName**](CertAuthorityOcspTlsTrustedCommonName.md)| The OCSP Responder Trusted Common Name object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**CertAuthorityOcspTlsTrustedCommonNameResponse**](CertAuthorityOcspTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OCSP Responder Trusted Common Name object&#39;s attributes after being created, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## deleteCertAuthority

> SempMetaOnlyResponse deleteCertAuthority(certAuthorityName)

Delete a Certificate Authority object.

Delete a Certificate Authority object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities and domainCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteCertAuthority(certAuthorityName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#deleteCertAuthority");
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


## deleteCertAuthorityOcspTlsTrustedCommonName

> SempMetaOnlyResponse deleteCertAuthorityOcspTlsTrustedCommonName(certAuthorityName, ocspTlsTrustedCommonName)

Delete an OCSP Responder Trusted Common Name object.

Delete an OCSP Responder Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates via config-sync.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        String ocspTlsTrustedCommonName = "ocspTlsTrustedCommonName_example"; // String | The expected Trusted Common Name of the OCSP responder remote certificate.
        try {
            SempMetaOnlyResponse result = apiInstance.deleteCertAuthorityOcspTlsTrustedCommonName(certAuthorityName, ocspTlsTrustedCommonName);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#deleteCertAuthorityOcspTlsTrustedCommonName");
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
| **ocspTlsTrustedCommonName** | **String**| The expected Trusted Common Name of the OCSP responder remote certificate. | |

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


## getCertAuthorities

> CertAuthoritiesResponse getCertAuthorities(count, cursor, opaquePassword, where, select)

Get a list of Certificate Authority objects.

Get a list of Certificate Authority objects.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||x| certContent|||x| crlDayList|||x| crlTimeList|||x| crlUrl|||x| ocspNonResponderCertEnabled|||x| ocspOverrideUrl|||x| ocspTimeout|||x| revocationCheckEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities and domainCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
        String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthoritiesResponse result = apiInstance.getCertAuthorities(count, cursor, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#getCertAuthorities");
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

[**CertAuthoritiesResponse**](CertAuthoritiesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of Certificate Authority objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getCertAuthority

> CertAuthorityResponse getCertAuthority(certAuthorityName, opaquePassword, select)

Get a Certificate Authority object.

Get a Certificate Authority object.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||x| certContent|||x| crlDayList|||x| crlTimeList|||x| crlUrl|||x| ocspNonResponderCertEnabled|||x| ocspOverrideUrl|||x| ocspTimeout|||x| revocationCheckEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities and domainCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityResponse result = apiInstance.getCertAuthority(certAuthorityName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#getCertAuthority");
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

[**CertAuthorityResponse**](CertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Authority object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getCertAuthorityOcspTlsTrustedCommonName

> CertAuthorityOcspTlsTrustedCommonNameResponse getCertAuthorityOcspTlsTrustedCommonName(certAuthorityName, ocspTlsTrustedCommonName, opaquePassword, select)

Get an OCSP Responder Trusted Common Name object.

Get an OCSP Responder Trusted Common Name object.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||x| ocspTlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        String ocspTlsTrustedCommonName = "ocspTlsTrustedCommonName_example"; // String | The expected Trusted Common Name of the OCSP responder remote certificate.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityOcspTlsTrustedCommonNameResponse result = apiInstance.getCertAuthorityOcspTlsTrustedCommonName(certAuthorityName, ocspTlsTrustedCommonName, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#getCertAuthorityOcspTlsTrustedCommonName");
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
| **ocspTlsTrustedCommonName** | **String**| The expected Trusted Common Name of the OCSP responder remote certificate. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**CertAuthorityOcspTlsTrustedCommonNameResponse**](CertAuthorityOcspTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The OCSP Responder Trusted Common Name object&#39;s attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## getCertAuthorityOcspTlsTrustedCommonNames

> CertAuthorityOcspTlsTrustedCommonNamesResponse getCertAuthorityOcspTlsTrustedCommonNames(certAuthorityName, opaquePassword, where, select)

Get a list of OCSP Responder Trusted Common Name objects.

Get a list of OCSP Responder Trusted Common Name objects.  When an OCSP override URL is configured, the OCSP responder will be required to sign the OCSP responses with certificates issued to these Trusted Common Names. A maximum of 8 common names can be configured as valid response signers.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: certAuthorityName|x||x| ocspTlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> where = Arrays.asList(); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityOcspTlsTrustedCommonNamesResponse result = apiInstance.getCertAuthorityOcspTlsTrustedCommonNames(certAuthorityName, opaquePassword, where, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#getCertAuthorityOcspTlsTrustedCommonNames");
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
| **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**CertAuthorityOcspTlsTrustedCommonNamesResponse**](CertAuthorityOcspTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The list of OCSP Responder Trusted Common Name objects&#39; attributes, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## replaceCertAuthority

> CertAuthorityResponse replaceCertAuthority(certAuthorityName, body, opaquePassword, select)

Replace a Certificate Authority object.

Replace a Certificate Authority object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- certAuthorityName|x||x|||x| certContent||||||x| crlDayList||||||x| crlTimeList||||||x| crlUrl|||||x|x| ocspNonResponderCertEnabled||||||x| ocspOverrideUrl||||||x| ocspTimeout||||||x| revocationCheckEnabled||||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- CertAuthority|crlDayList|crlTimeList| CertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities and domainCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        CertAuthority body = new CertAuthority(); // CertAuthority | The Certificate Authority object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityResponse result = apiInstance.replaceCertAuthority(certAuthorityName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#replaceCertAuthority");
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
| **body** | [**CertAuthority**](CertAuthority.md)| The Certificate Authority object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**CertAuthorityResponse**](CertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Authority object&#39;s attributes after being replaced, and the request metadata. |  -  |
| **0** | The error response. |  -  |


## updateCertAuthority

> CertAuthorityResponse updateCertAuthority(certAuthorityName, body, opaquePassword, select)

Update a Certificate Authority object.

Update a Certificate Authority object. Any attribute missing from the request will be left unchanged.  Clients can authenticate with the message broker over TLS by presenting a valid client certificate. The message broker authenticates the client certificate by constructing a full certificate chain (from the client certificate to intermediate CAs to a configured root CA). The intermediate CAs in this chain can be provided by the client, or configured in the message broker. The root CA must be configured on the message broker.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- certAuthorityName|x|x|||x| certContent|||||x| crlDayList|||||x| crlTimeList|||||x| crlUrl||||x|x| ocspNonResponderCertEnabled|||||x| ocspOverrideUrl|||||x| ocspTimeout|||||x| revocationCheckEnabled|||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- CertAuthority|crlDayList|crlTimeList| CertAuthority|crlTimeList|crlDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been deprecated since 2.19. Replaced by clientCertAuthorities and domainCertAuthorities.

### Example

```java
// Import classes:
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiClient;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.ApiException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.Configuration;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.models.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.CertAuthorityApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        CertAuthorityApi apiInstance = new CertAuthorityApi(defaultClient);
        String certAuthorityName = "certAuthorityName_example"; // String | The name of the Certificate Authority.
        CertAuthority body = new CertAuthority(); // CertAuthority | The Certificate Authority object's attributes.
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            CertAuthorityResponse result = apiInstance.updateCertAuthority(certAuthorityName, body, opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CertAuthorityApi#updateCertAuthority");
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
| **body** | [**CertAuthority**](CertAuthority.md)| The Certificate Authority object&#39;s attributes. | |
| **opaquePassword** | **String**| Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the &#x60;opaquePassword&#x60; parameter. | [optional] |
| **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional] |

### Return type

[**CertAuthorityResponse**](CertAuthorityResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The Certificate Authority object&#39;s attributes after being updated, and the request metadata. |  -  |
| **0** | The error response. |  -  |

