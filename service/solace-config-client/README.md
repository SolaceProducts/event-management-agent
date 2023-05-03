# ema-sempv2-api-client

SEMP (Solace Element Management Protocol)

- API version: 2.28

- Build date: 2023-04-25T11:27:30.946889+01:00[Europe/London]

SEMP (starting in `v2`, see note 1) is a RESTful API for configuring, monitoring, and administering a Solace PubSub+ broker.

SEMP uses URIs to address manageable **resources** of the Solace PubSub+ broker. Resources are individual **objects**, **collections** of objects, or (exclusively in the action API) **actions**. This document applies to the following API:


API|Base Path|Purpose|Comments
:---|:---|:---|:---
Configuration|/SEMP/v2/config|Reading and writing config state|See note 2



The following APIs are also available:


API|Base Path|Purpose|Comments
:---|:---|:---|:---
Action|/SEMP/v2/action|Performing actions|See note 2
Monitoring|/SEMP/v2/monitor|Querying operational parameters|See note 2



Resources are always nouns, with individual objects being singular and collections being plural.

Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`.

Actions within an object are identified by an `action-id`, which follows the object name with the form `obj-id/action-id`.

Some examples:

```
/SEMP/v2/config/msgVpns                        ; MsgVpn collection
/SEMP/v2/config/msgVpns/a                      ; MsgVpn object named \"a\"
/SEMP/v2/config/msgVpns/a/queues               ; Queue collection in MsgVpn \"a\"
/SEMP/v2/config/msgVpns/a/queues/b             ; Queue object named \"b\" in MsgVpn \"a\"
/SEMP/v2/action/msgVpns/a/queues/b/startReplay ; Action that starts a replay on Queue \"b\" in MsgVpn \"a\"
/SEMP/v2/monitor/msgVpns/a/clients             ; Client collection in MsgVpn \"a\"
/SEMP/v2/monitor/msgVpns/a/clients/c           ; Client object named \"c\" in MsgVpn \"a\"
```

## Collection Resources

Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. In the configuration API, the creation of a new object is done through its collection resource.

## Object and Action Resources

Objects are composed of attributes, actions, collections, and other objects. They are described by JSON objects as name/value pairs. The collections and actions of an object are not contained directly in the object's JSON content; rather the content includes an attribute containing a URI which points to the collections and actions. These contained resources must be managed through this URI. At a minimum, every object has one or more identifying attributes, and its own `uri` attribute which contains the URI pointing to itself.

Actions are also composed of attributes, and are described by JSON objects as name/value pairs. Unlike objects, however, they are not members of a collection and cannot be retrieved, only performed. Actions only exist in the action API.

Attributes in an object or action may have any combination of the following properties:


Property|Meaning|Comments
:---|:---|:---
Identifying|Attribute is involved in unique identification of the object, and appears in its URI|
Const|Attribute value can only be chosen during object creation|
Required|Attribute must be provided in the request|
Read-Only|Attribute can only be read, not written.|See note 3
Write-Only|Attribute can only be written, not read, unless the attribute is also opaque|See the documentation for the opaque property
Requires-Disable|Attribute can only be changed when object is disabled|
Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|
Opaque|Attribute can be set or retrieved in opaque form when the `opaquePassword` query parameter is present|See the `opaquePassword` query parameter documentation



In some requests, certain attributes may only be provided in certain combinations with other attributes:


Relationship|Meaning
:---|:---
Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request
Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request



In the monitoring API, any non-identifying attribute may not be returned in a GET.

## HTTP Methods

The following HTTP methods manipulate resources in accordance with these general principles. Note that some methods are only used in certain APIs:


Method|Resource|Meaning|Request Body|Response Body|Notes
:---|:---|:---|:---|:---|:---
POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Absent attributes are set to default. If object already exists, a 400 error is returned
PUT|Object|Update object|New attribute values|Object attributes and metadata|If does not exist, the object is first created. Absent attributes are set to default, with certain exceptions (see note 4)
PUT|Action|Performs action|Action arguments|Action metadata|
PATCH|Object|Update object|New attribute values|Object attributes and metadata|Absent attributes are left unchanged. If the object does not exist, a 404 error is returned
DELETE|Object|Delete object|Empty|Object metadata|If the object does not exist, a 404 is returned
GET|Object|Get object|Empty|Object attributes and metadata|If the object does not exist, a 404 is returned
GET|Collection|Get collection|Empty|Object attributes and collection metadata|If the collection is empty, then an empty collection is returned with a 200 code



## Common Query Parameters

The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:

```
; Request for the MsgVpns collection using two hypothetical query parameters
; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively
/SEMP/v2/config/msgVpns?q1=val1&q2=val2
```

### select

Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, return only those fields that are desired, or exclude fields that are not desired.

The value of `select` is a comma-separated list of attribute names. If the list contains attribute names that are not prefaced by `-`, only those attributes are included in the response. If the list contains attribute names that are prefaced by `-`, those attributes are excluded from the response. If the list contains both types, then the difference of the first set of attributes and the second set of attributes is returned. If the list is empty (i.e. `select=`), it is treated the same as if no `select` was provided: all attribute are returned.

All attributes that are prefaced by `-` must follow all attributes that are not prefaced by `-`. In addition, each attribute name in the list must match at least one attribute in the object.

Names may include the `*` wildcard (zero or more characters). Nested attribute names are supported using periods (e.g. `parentName.childName`).

Some examples:

```
; List of all MsgVpn names
/SEMP/v2/config/msgVpns?select=msgVpnName
; List of all MsgVpn and their attributes except for their names
/SEMP/v2/config/msgVpns?select=-msgVpnName
; Authentication attributes of MsgVpn \"finance\"
/SEMP/v2/config/msgVpns/finance?select=authentication*
; All attributes of MsgVpn \"finance\" except for authentication attributes
/SEMP/v2/config/msgVpns/finance?select=-authentication*
; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\"
/SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission
```

### where

Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.

The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:

```
expression  = attribute-name OP value
OP          = '==' | '!=' | '&lt;' | '&gt;' | '&lt;=' | '&gt;='
```

`value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard (zero or more characters). Some examples:

```
; Only enabled MsgVpns
/SEMP/v2/config/msgVpns?where=enabled==true
; Only MsgVpns using basic non-LDAP authentication
/SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap
; Only MsgVpns that allow more than 100 client connections
/SEMP/v2/config/msgVpns?where=maxConnectionCount>100
; Only MsgVpns with msgVpnName starting with \"B\":
/SEMP/v2/config/msgVpns?where=msgVpnName==B*
```

### count

Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. There is also a per-collection maximum value to limit request handling time.

`count` does not guarantee that a minimum number of objects will be returned. A page may contain fewer than `count` objects or even be empty. Additional objects may nonetheless be available for retrieval on subsequent pages. See the `cursor` query parameter documentation for more information on paging.

For example:
```
; Up to 25 MsgVpns
/SEMP/v2/config/msgVpns?count=25
```

### cursor

The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.

When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects.

Applications must continue to use the `cursorQuery` if one is provided in order to retrieve the full set of objects associated with the request, even if a page contains fewer than the requested number of objects (see the `count` query parameter documentation) or is empty.

### opaquePassword

Attributes with the opaque property are also write-only and so cannot normally be retrieved in a GET. However, when a password is provided in the `opaquePassword` query parameter, attributes with the opaque property are retrieved in a GET in opaque form, encrypted with this password. The query parameter can also be used on a POST, PATCH, or PUT to set opaque attributes using opaque attribute values retrieved in a GET, so long as:

1. the same password that was used to retrieve the opaque attribute values is provided; and

2. the broker to which the request is being sent has the same major and minor SEMP version as the broker that produced the opaque attribute values.

The password provided in the query parameter must be a minimum of 8 characters and a maximum of 128 characters.

The query parameter can only be used in the configuration API, and only over HTTPS.

## Authentication

When a client makes its first SEMPv2 request, it must supply a username and password using HTTP Basic authentication, or an OAuth token or tokens using HTTP Bearer authentication.

When HTTP Basic authentication is used, the broker returns a cookie containing a session key. The client can omit the username and password from subsequent requests, because the broker can use the session cookie for authentication instead. When the session expires or is deleted, the client must provide the username and password again, and the broker creates a new session.

There are a limited number of session slots available on the broker. The broker returns 529 No SEMP Session Available if it is not able to allocate a session.

If certain attributes—such as a user's password—are changed, the broker automatically deletes the affected sessions. These attributes are documented below. However, changes in external user configuration data stored on a RADIUS or LDAP server do not trigger the broker to delete the associated session(s), therefore you must do this manually, if required.

A client can retrieve its current session information using the /about/user endpoint and delete its own session using the /about/user/logout endpoint. A client with appropriate permissions can also manage all sessions using the /sessions endpoint.

Sessions are not created when authenticating with an OAuth token or tokens using HTTP Bearer authentication. If a session cookie is provided, it is ignored.

## Help

Visit [our website](https://solace.com) to learn more about Solace.

You can also download the SEMP API specifications by clicking [here](https://solace.com/downloads/).

If you need additional support, please contact us at [support@solace.com](mailto:support@solace.com).

## Notes

Note|Description
:---:|:---
1|This specification defines SEMP starting in \"v2\", and not the original SEMP \"v1\" interface. Request and response formats between \"v1\" and \"v2\" are entirely incompatible, although both protocols share a common port configuration on the Solace PubSub+ broker. They are differentiated by the initial portion of the URI path, one of either \"/SEMP/\" or \"/SEMP/v2/\"
2|This API is partially implemented. Only a subset of all objects are available.
3|Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH.
4|On a PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT, except in the following two cases: there is a mutual requires relationship with another non-write-only attribute, both attributes are absent from the request, and the non-write-only attribute is not currently set to its default value; or the attribute is also opaque and the `opaquePassword` query parameter is provided in the request.



  For more information, please visit [http://www.solace.com](http://www.solace.com)

*Automatically generated by the [OpenAPI Generator](https://openapi-generator.tech)*

## Requirements

Building the API client library requires:

1. Java 1.8+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.solace</groupId>
  <artifactId>ema-sempv2-api-client</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
  repositories {
    mavenCentral()     // Needed if the 'ema-sempv2-api-client' jar has been published to maven central.
    mavenLocal()       // Needed if the 'ema-sempv2-api-client' jar has been published to the local maven repo.
  }

  dependencies {
     implementation "com.solace:ema-sempv2-api-client:0.0.1-SNAPSHOT"
  }
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

- `target/ema-sempv2-api-client-0.0.1-SNAPSHOT.jar`
- `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.invoker.auth.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.*;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AboutApi;

public class AboutApiExample {

    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://www.solace.com/SEMP/v2/config");
        
        // Configure HTTP basic authorization: basicAuth
        HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
        basicAuth.setUsername("YOUR USERNAME");
        basicAuth.setPassword("YOUR PASSWORD");

        AboutApi apiInstance = new AboutApi(defaultClient);
        String opaquePassword = "opaquePassword_example"; // String | Accept opaque attributes in the request or return opaque attributes in the response, encrypted with the specified password. See the documentation for the `opaquePassword` parameter.
        List<String> select = Arrays.asList(); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
        try {
            AboutResponse result = apiInstance.getAbout(opaquePassword, select);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AboutApi#getAbout");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AboutApi* | [**getAbout**](docs/AboutApi.md#getAbout) | **GET** /about | Get an About object.
*AboutApi* | [**getAboutApi**](docs/AboutApi.md#getAboutApi) | **GET** /about/api | Get an API Description object.
*AboutApi* | [**getAboutUser**](docs/AboutApi.md#getAboutUser) | **GET** /about/user | Get a User object.
*AboutApi* | [**getAboutUserMsgVpn**](docs/AboutApi.md#getAboutUserMsgVpn) | **GET** /about/user/msgVpns/{msgVpnName} | Get a User Message VPN object.
*AboutApi* | [**getAboutUserMsgVpns**](docs/AboutApi.md#getAboutUserMsgVpns) | **GET** /about/user/msgVpns | Get a list of User Message VPN objects.
*AclProfileApi* | [**createMsgVpnAclProfile**](docs/AclProfileApi.md#createMsgVpnAclProfile) | **POST** /msgVpns/{msgVpnName}/aclProfiles | Create an ACL Profile object.
*AclProfileApi* | [**createMsgVpnAclProfileClientConnectException**](docs/AclProfileApi.md#createMsgVpnAclProfileClientConnectException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Create a Client Connect Exception object.
*AclProfileApi* | [**createMsgVpnAclProfilePublishException**](docs/AclProfileApi.md#createMsgVpnAclProfilePublishException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Create a Publish Topic Exception object.
*AclProfileApi* | [**createMsgVpnAclProfilePublishTopicException**](docs/AclProfileApi.md#createMsgVpnAclProfilePublishTopicException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions | Create a Publish Topic Exception object.
*AclProfileApi* | [**createMsgVpnAclProfileSubscribeException**](docs/AclProfileApi.md#createMsgVpnAclProfileSubscribeException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Create a Subscribe Topic Exception object.
*AclProfileApi* | [**createMsgVpnAclProfileSubscribeShareNameException**](docs/AclProfileApi.md#createMsgVpnAclProfileSubscribeShareNameException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions | Create a Subscribe Share Name Exception object.
*AclProfileApi* | [**createMsgVpnAclProfileSubscribeTopicException**](docs/AclProfileApi.md#createMsgVpnAclProfileSubscribeTopicException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions | Create a Subscribe Topic Exception object.
*AclProfileApi* | [**deleteMsgVpnAclProfile**](docs/AclProfileApi.md#deleteMsgVpnAclProfile) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Delete an ACL Profile object.
*AclProfileApi* | [**deleteMsgVpnAclProfileClientConnectException**](docs/AclProfileApi.md#deleteMsgVpnAclProfileClientConnectException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Delete a Client Connect Exception object.
*AclProfileApi* | [**deleteMsgVpnAclProfilePublishException**](docs/AclProfileApi.md#deleteMsgVpnAclProfilePublishException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Delete a Publish Topic Exception object.
*AclProfileApi* | [**deleteMsgVpnAclProfilePublishTopicException**](docs/AclProfileApi.md#deleteMsgVpnAclProfilePublishTopicException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions/{publishTopicExceptionSyntax},{publishTopicException} | Delete a Publish Topic Exception object.
*AclProfileApi* | [**deleteMsgVpnAclProfileSubscribeException**](docs/AclProfileApi.md#deleteMsgVpnAclProfileSubscribeException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Delete a Subscribe Topic Exception object.
*AclProfileApi* | [**deleteMsgVpnAclProfileSubscribeShareNameException**](docs/AclProfileApi.md#deleteMsgVpnAclProfileSubscribeShareNameException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions/{subscribeShareNameExceptionSyntax},{subscribeShareNameException} | Delete a Subscribe Share Name Exception object.
*AclProfileApi* | [**deleteMsgVpnAclProfileSubscribeTopicException**](docs/AclProfileApi.md#deleteMsgVpnAclProfileSubscribeTopicException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions/{subscribeTopicExceptionSyntax},{subscribeTopicException} | Delete a Subscribe Topic Exception object.
*AclProfileApi* | [**getMsgVpnAclProfile**](docs/AclProfileApi.md#getMsgVpnAclProfile) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Get an ACL Profile object.
*AclProfileApi* | [**getMsgVpnAclProfileClientConnectException**](docs/AclProfileApi.md#getMsgVpnAclProfileClientConnectException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Get a Client Connect Exception object.
*AclProfileApi* | [**getMsgVpnAclProfileClientConnectExceptions**](docs/AclProfileApi.md#getMsgVpnAclProfileClientConnectExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Get a list of Client Connect Exception objects.
*AclProfileApi* | [**getMsgVpnAclProfilePublishException**](docs/AclProfileApi.md#getMsgVpnAclProfilePublishException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Get a Publish Topic Exception object.
*AclProfileApi* | [**getMsgVpnAclProfilePublishExceptions**](docs/AclProfileApi.md#getMsgVpnAclProfilePublishExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Get a list of Publish Topic Exception objects.
*AclProfileApi* | [**getMsgVpnAclProfilePublishTopicException**](docs/AclProfileApi.md#getMsgVpnAclProfilePublishTopicException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions/{publishTopicExceptionSyntax},{publishTopicException} | Get a Publish Topic Exception object.
*AclProfileApi* | [**getMsgVpnAclProfilePublishTopicExceptions**](docs/AclProfileApi.md#getMsgVpnAclProfilePublishTopicExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions | Get a list of Publish Topic Exception objects.
*AclProfileApi* | [**getMsgVpnAclProfileSubscribeException**](docs/AclProfileApi.md#getMsgVpnAclProfileSubscribeException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Get a Subscribe Topic Exception object.
*AclProfileApi* | [**getMsgVpnAclProfileSubscribeExceptions**](docs/AclProfileApi.md#getMsgVpnAclProfileSubscribeExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Get a list of Subscribe Topic Exception objects.
*AclProfileApi* | [**getMsgVpnAclProfileSubscribeShareNameException**](docs/AclProfileApi.md#getMsgVpnAclProfileSubscribeShareNameException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions/{subscribeShareNameExceptionSyntax},{subscribeShareNameException} | Get a Subscribe Share Name Exception object.
*AclProfileApi* | [**getMsgVpnAclProfileSubscribeShareNameExceptions**](docs/AclProfileApi.md#getMsgVpnAclProfileSubscribeShareNameExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions | Get a list of Subscribe Share Name Exception objects.
*AclProfileApi* | [**getMsgVpnAclProfileSubscribeTopicException**](docs/AclProfileApi.md#getMsgVpnAclProfileSubscribeTopicException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions/{subscribeTopicExceptionSyntax},{subscribeTopicException} | Get a Subscribe Topic Exception object.
*AclProfileApi* | [**getMsgVpnAclProfileSubscribeTopicExceptions**](docs/AclProfileApi.md#getMsgVpnAclProfileSubscribeTopicExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions | Get a list of Subscribe Topic Exception objects.
*AclProfileApi* | [**getMsgVpnAclProfiles**](docs/AclProfileApi.md#getMsgVpnAclProfiles) | **GET** /msgVpns/{msgVpnName}/aclProfiles | Get a list of ACL Profile objects.
*AclProfileApi* | [**replaceMsgVpnAclProfile**](docs/AclProfileApi.md#replaceMsgVpnAclProfile) | **PUT** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Replace an ACL Profile object.
*AclProfileApi* | [**updateMsgVpnAclProfile**](docs/AclProfileApi.md#updateMsgVpnAclProfile) | **PATCH** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Update an ACL Profile object.
*AllApi* | [**createCertAuthority**](docs/AllApi.md#createCertAuthority) | **POST** /certAuthorities | Create a Certificate Authority object.
*AllApi* | [**createCertAuthorityOcspTlsTrustedCommonName**](docs/AllApi.md#createCertAuthorityOcspTlsTrustedCommonName) | **POST** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Create an OCSP Responder Trusted Common Name object.
*AllApi* | [**createClientCertAuthority**](docs/AllApi.md#createClientCertAuthority) | **POST** /clientCertAuthorities | Create a Client Certificate Authority object.
*AllApi* | [**createClientCertAuthorityOcspTlsTrustedCommonName**](docs/AllApi.md#createClientCertAuthorityOcspTlsTrustedCommonName) | **POST** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Create an OCSP Responder Trusted Common Name object.
*AllApi* | [**createDmrCluster**](docs/AllApi.md#createDmrCluster) | **POST** /dmrClusters | Create a Cluster object.
*AllApi* | [**createDmrClusterCertMatchingRule**](docs/AllApi.md#createDmrClusterCertMatchingRule) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules | Create a Certificate Matching Rule object.
*AllApi* | [**createDmrClusterCertMatchingRuleAttributeFilter**](docs/AllApi.md#createDmrClusterCertMatchingRuleAttributeFilter) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**createDmrClusterCertMatchingRuleCondition**](docs/AllApi.md#createDmrClusterCertMatchingRuleCondition) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object.
*AllApi* | [**createDmrClusterLink**](docs/AllApi.md#createDmrClusterLink) | **POST** /dmrClusters/{dmrClusterName}/links | Create a Link object.
*AllApi* | [**createDmrClusterLinkAttribute**](docs/AllApi.md#createDmrClusterLinkAttribute) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes | Create a Link Attribute object.
*AllApi* | [**createDmrClusterLinkRemoteAddress**](docs/AllApi.md#createDmrClusterLinkRemoteAddress) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Create a Remote Address object.
*AllApi* | [**createDmrClusterLinkTlsTrustedCommonName**](docs/AllApi.md#createDmrClusterLinkTlsTrustedCommonName) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*AllApi* | [**createDomainCertAuthority**](docs/AllApi.md#createDomainCertAuthority) | **POST** /domainCertAuthorities | Create a Domain Certificate Authority object.
*AllApi* | [**createMsgVpn**](docs/AllApi.md#createMsgVpn) | **POST** /msgVpns | Create a Message VPN object.
*AllApi* | [**createMsgVpnAclProfile**](docs/AllApi.md#createMsgVpnAclProfile) | **POST** /msgVpns/{msgVpnName}/aclProfiles | Create an ACL Profile object.
*AllApi* | [**createMsgVpnAclProfileClientConnectException**](docs/AllApi.md#createMsgVpnAclProfileClientConnectException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Create a Client Connect Exception object.
*AllApi* | [**createMsgVpnAclProfilePublishException**](docs/AllApi.md#createMsgVpnAclProfilePublishException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Create a Publish Topic Exception object.
*AllApi* | [**createMsgVpnAclProfilePublishTopicException**](docs/AllApi.md#createMsgVpnAclProfilePublishTopicException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions | Create a Publish Topic Exception object.
*AllApi* | [**createMsgVpnAclProfileSubscribeException**](docs/AllApi.md#createMsgVpnAclProfileSubscribeException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Create a Subscribe Topic Exception object.
*AllApi* | [**createMsgVpnAclProfileSubscribeShareNameException**](docs/AllApi.md#createMsgVpnAclProfileSubscribeShareNameException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions | Create a Subscribe Share Name Exception object.
*AllApi* | [**createMsgVpnAclProfileSubscribeTopicException**](docs/AllApi.md#createMsgVpnAclProfileSubscribeTopicException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions | Create a Subscribe Topic Exception object.
*AllApi* | [**createMsgVpnAuthenticationOauthProfile**](docs/AllApi.md#createMsgVpnAuthenticationOauthProfile) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles | Create an OAuth Profile object.
*AllApi* | [**createMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/AllApi.md#createMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims | Create a Required Claim object.
*AllApi* | [**createMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/AllApi.md#createMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Create a Required Claim object.
*AllApi* | [**createMsgVpnAuthenticationOauthProvider**](docs/AllApi.md#createMsgVpnAuthenticationOauthProvider) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProviders | Create an OAuth Provider object.
*AllApi* | [**createMsgVpnAuthorizationGroup**](docs/AllApi.md#createMsgVpnAuthorizationGroup) | **POST** /msgVpns/{msgVpnName}/authorizationGroups | Create an Authorization Group object.
*AllApi* | [**createMsgVpnBridge**](docs/AllApi.md#createMsgVpnBridge) | **POST** /msgVpns/{msgVpnName}/bridges | Create a Bridge object.
*AllApi* | [**createMsgVpnBridgeRemoteMsgVpn**](docs/AllApi.md#createMsgVpnBridgeRemoteMsgVpn) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Create a Remote Message VPN object.
*AllApi* | [**createMsgVpnBridgeRemoteSubscription**](docs/AllApi.md#createMsgVpnBridgeRemoteSubscription) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Create a Remote Subscription object.
*AllApi* | [**createMsgVpnBridgeTlsTrustedCommonName**](docs/AllApi.md#createMsgVpnBridgeTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*AllApi* | [**createMsgVpnCertMatchingRule**](docs/AllApi.md#createMsgVpnCertMatchingRule) | **POST** /msgVpns/{msgVpnName}/certMatchingRules | Create a Certificate Matching Rule object.
*AllApi* | [**createMsgVpnCertMatchingRuleAttributeFilter**](docs/AllApi.md#createMsgVpnCertMatchingRuleAttributeFilter) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**createMsgVpnCertMatchingRuleCondition**](docs/AllApi.md#createMsgVpnCertMatchingRuleCondition) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object.
*AllApi* | [**createMsgVpnClientProfile**](docs/AllApi.md#createMsgVpnClientProfile) | **POST** /msgVpns/{msgVpnName}/clientProfiles | Create a Client Profile object.
*AllApi* | [**createMsgVpnClientUsername**](docs/AllApi.md#createMsgVpnClientUsername) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Create a Client Username object.
*AllApi* | [**createMsgVpnClientUsernameAttribute**](docs/AllApi.md#createMsgVpnClientUsernameAttribute) | **POST** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Create a Client Username Attribute object.
*AllApi* | [**createMsgVpnDistributedCache**](docs/AllApi.md#createMsgVpnDistributedCache) | **POST** /msgVpns/{msgVpnName}/distributedCaches | Create a Distributed Cache object.
*AllApi* | [**createMsgVpnDistributedCacheCluster**](docs/AllApi.md#createMsgVpnDistributedCacheCluster) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters | Create a Cache Cluster object.
*AllApi* | [**createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/AllApi.md#createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters | Create a Home Cache Cluster object.
*AllApi* | [**createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/AllApi.md#createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes | Create a Topic Prefix object.
*AllApi* | [**createMsgVpnDistributedCacheClusterInstance**](docs/AllApi.md#createMsgVpnDistributedCacheClusterInstance) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances | Create a Cache Instance object.
*AllApi* | [**createMsgVpnDistributedCacheClusterTopic**](docs/AllApi.md#createMsgVpnDistributedCacheClusterTopic) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics | Create a Topic object.
*AllApi* | [**createMsgVpnDmrBridge**](docs/AllApi.md#createMsgVpnDmrBridge) | **POST** /msgVpns/{msgVpnName}/dmrBridges | Create a DMR Bridge object.
*AllApi* | [**createMsgVpnJndiConnectionFactory**](docs/AllApi.md#createMsgVpnJndiConnectionFactory) | **POST** /msgVpns/{msgVpnName}/jndiConnectionFactories | Create a JNDI Connection Factory object.
*AllApi* | [**createMsgVpnJndiQueue**](docs/AllApi.md#createMsgVpnJndiQueue) | **POST** /msgVpns/{msgVpnName}/jndiQueues | Create a JNDI Queue object.
*AllApi* | [**createMsgVpnJndiTopic**](docs/AllApi.md#createMsgVpnJndiTopic) | **POST** /msgVpns/{msgVpnName}/jndiTopics | Create a JNDI Topic object.
*AllApi* | [**createMsgVpnMqttRetainCache**](docs/AllApi.md#createMsgVpnMqttRetainCache) | **POST** /msgVpns/{msgVpnName}/mqttRetainCaches | Create an MQTT Retain Cache object.
*AllApi* | [**createMsgVpnMqttSession**](docs/AllApi.md#createMsgVpnMqttSession) | **POST** /msgVpns/{msgVpnName}/mqttSessions | Create an MQTT Session object.
*AllApi* | [**createMsgVpnMqttSessionSubscription**](docs/AllApi.md#createMsgVpnMqttSessionSubscription) | **POST** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Create a Subscription object.
*AllApi* | [**createMsgVpnQueue**](docs/AllApi.md#createMsgVpnQueue) | **POST** /msgVpns/{msgVpnName}/queues | Create a Queue object.
*AllApi* | [**createMsgVpnQueueSubscription**](docs/AllApi.md#createMsgVpnQueueSubscription) | **POST** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Create a Queue Subscription object.
*AllApi* | [**createMsgVpnQueueTemplate**](docs/AllApi.md#createMsgVpnQueueTemplate) | **POST** /msgVpns/{msgVpnName}/queueTemplates | Create a Queue Template object.
*AllApi* | [**createMsgVpnReplayLog**](docs/AllApi.md#createMsgVpnReplayLog) | **POST** /msgVpns/{msgVpnName}/replayLogs | Create a Replay Log object.
*AllApi* | [**createMsgVpnReplayLogTopicFilterSubscription**](docs/AllApi.md#createMsgVpnReplayLogTopicFilterSubscription) | **POST** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions | Create a Topic Filter Subscription object.
*AllApi* | [**createMsgVpnReplicatedTopic**](docs/AllApi.md#createMsgVpnReplicatedTopic) | **POST** /msgVpns/{msgVpnName}/replicatedTopics | Create a Replicated Topic object.
*AllApi* | [**createMsgVpnRestDeliveryPoint**](docs/AllApi.md#createMsgVpnRestDeliveryPoint) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints | Create a REST Delivery Point object.
*AllApi* | [**createMsgVpnRestDeliveryPointQueueBinding**](docs/AllApi.md#createMsgVpnRestDeliveryPointQueueBinding) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Create a Queue Binding object.
*AllApi* | [**createMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/AllApi.md#createMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders | Create a Request Header object.
*AllApi* | [**createMsgVpnRestDeliveryPointRestConsumer**](docs/AllApi.md#createMsgVpnRestDeliveryPointRestConsumer) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Create a REST Consumer object.
*AllApi* | [**createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/AllApi.md#createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims | Create a Claim object.
*AllApi* | [**createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/AllApi.md#createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*AllApi* | [**createMsgVpnSequencedTopic**](docs/AllApi.md#createMsgVpnSequencedTopic) | **POST** /msgVpns/{msgVpnName}/sequencedTopics | Create a Sequenced Topic object.
*AllApi* | [**createMsgVpnTopicEndpoint**](docs/AllApi.md#createMsgVpnTopicEndpoint) | **POST** /msgVpns/{msgVpnName}/topicEndpoints | Create a Topic Endpoint object.
*AllApi* | [**createMsgVpnTopicEndpointTemplate**](docs/AllApi.md#createMsgVpnTopicEndpointTemplate) | **POST** /msgVpns/{msgVpnName}/topicEndpointTemplates | Create a Topic Endpoint Template object.
*AllApi* | [**createOauthProfile**](docs/AllApi.md#createOauthProfile) | **POST** /oauthProfiles | Create an OAuth Profile object.
*AllApi* | [**createOauthProfileAccessLevelGroup**](docs/AllApi.md#createOauthProfileAccessLevelGroup) | **POST** /oauthProfiles/{oauthProfileName}/accessLevelGroups | Create a Group Access Level object.
*AllApi* | [**createOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/AllApi.md#createOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **POST** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions | Create a Message VPN Access-Level Exception object.
*AllApi* | [**createOauthProfileClientAllowedHost**](docs/AllApi.md#createOauthProfileClientAllowedHost) | **POST** /oauthProfiles/{oauthProfileName}/clientAllowedHosts | Create an Allowed Host Value object.
*AllApi* | [**createOauthProfileClientAuthorizationParameter**](docs/AllApi.md#createOauthProfileClientAuthorizationParameter) | **POST** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters | Create an Authorization Parameter object.
*AllApi* | [**createOauthProfileClientRequiredClaim**](docs/AllApi.md#createOauthProfileClientRequiredClaim) | **POST** /oauthProfiles/{oauthProfileName}/clientRequiredClaims | Create a Required Claim object.
*AllApi* | [**createOauthProfileDefaultMsgVpnAccessLevelException**](docs/AllApi.md#createOauthProfileDefaultMsgVpnAccessLevelException) | **POST** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions | Create a Message VPN Access-Level Exception object.
*AllApi* | [**createOauthProfileResourceServerRequiredClaim**](docs/AllApi.md#createOauthProfileResourceServerRequiredClaim) | **POST** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Create a Required Claim object.
*AllApi* | [**createVirtualHostname**](docs/AllApi.md#createVirtualHostname) | **POST** /virtualHostnames | Create a Virtual Hostname object.
*AllApi* | [**deleteCertAuthority**](docs/AllApi.md#deleteCertAuthority) | **DELETE** /certAuthorities/{certAuthorityName} | Delete a Certificate Authority object.
*AllApi* | [**deleteCertAuthorityOcspTlsTrustedCommonName**](docs/AllApi.md#deleteCertAuthorityOcspTlsTrustedCommonName) | **DELETE** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Delete an OCSP Responder Trusted Common Name object.
*AllApi* | [**deleteClientCertAuthority**](docs/AllApi.md#deleteClientCertAuthority) | **DELETE** /clientCertAuthorities/{certAuthorityName} | Delete a Client Certificate Authority object.
*AllApi* | [**deleteClientCertAuthorityOcspTlsTrustedCommonName**](docs/AllApi.md#deleteClientCertAuthorityOcspTlsTrustedCommonName) | **DELETE** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Delete an OCSP Responder Trusted Common Name object.
*AllApi* | [**deleteDmrCluster**](docs/AllApi.md#deleteDmrCluster) | **DELETE** /dmrClusters/{dmrClusterName} | Delete a Cluster object.
*AllApi* | [**deleteDmrClusterCertMatchingRule**](docs/AllApi.md#deleteDmrClusterCertMatchingRule) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object.
*AllApi* | [**deleteDmrClusterCertMatchingRuleAttributeFilter**](docs/AllApi.md#deleteDmrClusterCertMatchingRuleAttributeFilter) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**deleteDmrClusterCertMatchingRuleCondition**](docs/AllApi.md#deleteDmrClusterCertMatchingRuleCondition) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object.
*AllApi* | [**deleteDmrClusterLink**](docs/AllApi.md#deleteDmrClusterLink) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Delete a Link object.
*AllApi* | [**deleteDmrClusterLinkAttribute**](docs/AllApi.md#deleteDmrClusterLinkAttribute) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes/{attributeName},{attributeValue} | Delete a Link Attribute object.
*AllApi* | [**deleteDmrClusterLinkRemoteAddress**](docs/AllApi.md#deleteDmrClusterLinkRemoteAddress) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Delete a Remote Address object.
*AllApi* | [**deleteDmrClusterLinkTlsTrustedCommonName**](docs/AllApi.md#deleteDmrClusterLinkTlsTrustedCommonName) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*AllApi* | [**deleteDomainCertAuthority**](docs/AllApi.md#deleteDomainCertAuthority) | **DELETE** /domainCertAuthorities/{certAuthorityName} | Delete a Domain Certificate Authority object.
*AllApi* | [**deleteMsgVpn**](docs/AllApi.md#deleteMsgVpn) | **DELETE** /msgVpns/{msgVpnName} | Delete a Message VPN object.
*AllApi* | [**deleteMsgVpnAclProfile**](docs/AllApi.md#deleteMsgVpnAclProfile) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Delete an ACL Profile object.
*AllApi* | [**deleteMsgVpnAclProfileClientConnectException**](docs/AllApi.md#deleteMsgVpnAclProfileClientConnectException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Delete a Client Connect Exception object.
*AllApi* | [**deleteMsgVpnAclProfilePublishException**](docs/AllApi.md#deleteMsgVpnAclProfilePublishException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Delete a Publish Topic Exception object.
*AllApi* | [**deleteMsgVpnAclProfilePublishTopicException**](docs/AllApi.md#deleteMsgVpnAclProfilePublishTopicException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions/{publishTopicExceptionSyntax},{publishTopicException} | Delete a Publish Topic Exception object.
*AllApi* | [**deleteMsgVpnAclProfileSubscribeException**](docs/AllApi.md#deleteMsgVpnAclProfileSubscribeException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Delete a Subscribe Topic Exception object.
*AllApi* | [**deleteMsgVpnAclProfileSubscribeShareNameException**](docs/AllApi.md#deleteMsgVpnAclProfileSubscribeShareNameException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions/{subscribeShareNameExceptionSyntax},{subscribeShareNameException} | Delete a Subscribe Share Name Exception object.
*AllApi* | [**deleteMsgVpnAclProfileSubscribeTopicException**](docs/AllApi.md#deleteMsgVpnAclProfileSubscribeTopicException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions/{subscribeTopicExceptionSyntax},{subscribeTopicException} | Delete a Subscribe Topic Exception object.
*AllApi* | [**deleteMsgVpnAuthenticationOauthProfile**](docs/AllApi.md#deleteMsgVpnAuthenticationOauthProfile) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Delete an OAuth Profile object.
*AllApi* | [**deleteMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/AllApi.md#deleteMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Delete a Required Claim object.
*AllApi* | [**deleteMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/AllApi.md#deleteMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Delete a Required Claim object.
*AllApi* | [**deleteMsgVpnAuthenticationOauthProvider**](docs/AllApi.md#deleteMsgVpnAuthenticationOauthProvider) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Delete an OAuth Provider object.
*AllApi* | [**deleteMsgVpnAuthorizationGroup**](docs/AllApi.md#deleteMsgVpnAuthorizationGroup) | **DELETE** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Delete an Authorization Group object.
*AllApi* | [**deleteMsgVpnBridge**](docs/AllApi.md#deleteMsgVpnBridge) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Delete a Bridge object.
*AllApi* | [**deleteMsgVpnBridgeRemoteMsgVpn**](docs/AllApi.md#deleteMsgVpnBridgeRemoteMsgVpn) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Delete a Remote Message VPN object.
*AllApi* | [**deleteMsgVpnBridgeRemoteSubscription**](docs/AllApi.md#deleteMsgVpnBridgeRemoteSubscription) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Delete a Remote Subscription object.
*AllApi* | [**deleteMsgVpnBridgeTlsTrustedCommonName**](docs/AllApi.md#deleteMsgVpnBridgeTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*AllApi* | [**deleteMsgVpnCertMatchingRule**](docs/AllApi.md#deleteMsgVpnCertMatchingRule) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object.
*AllApi* | [**deleteMsgVpnCertMatchingRuleAttributeFilter**](docs/AllApi.md#deleteMsgVpnCertMatchingRuleAttributeFilter) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**deleteMsgVpnCertMatchingRuleCondition**](docs/AllApi.md#deleteMsgVpnCertMatchingRuleCondition) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object.
*AllApi* | [**deleteMsgVpnClientProfile**](docs/AllApi.md#deleteMsgVpnClientProfile) | **DELETE** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Delete a Client Profile object.
*AllApi* | [**deleteMsgVpnClientUsername**](docs/AllApi.md#deleteMsgVpnClientUsername) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Delete a Client Username object.
*AllApi* | [**deleteMsgVpnClientUsernameAttribute**](docs/AllApi.md#deleteMsgVpnClientUsernameAttribute) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Delete a Client Username Attribute object.
*AllApi* | [**deleteMsgVpnDistributedCache**](docs/AllApi.md#deleteMsgVpnDistributedCache) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Delete a Distributed Cache object.
*AllApi* | [**deleteMsgVpnDistributedCacheCluster**](docs/AllApi.md#deleteMsgVpnDistributedCacheCluster) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Delete a Cache Cluster object.
*AllApi* | [**deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/AllApi.md#deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName} | Delete a Home Cache Cluster object.
*AllApi* | [**deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/AllApi.md#deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix} | Delete a Topic Prefix object.
*AllApi* | [**deleteMsgVpnDistributedCacheClusterInstance**](docs/AllApi.md#deleteMsgVpnDistributedCacheClusterInstance) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Delete a Cache Instance object.
*AllApi* | [**deleteMsgVpnDistributedCacheClusterTopic**](docs/AllApi.md#deleteMsgVpnDistributedCacheClusterTopic) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic} | Delete a Topic object.
*AllApi* | [**deleteMsgVpnDmrBridge**](docs/AllApi.md#deleteMsgVpnDmrBridge) | **DELETE** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Delete a DMR Bridge object.
*AllApi* | [**deleteMsgVpnJndiConnectionFactory**](docs/AllApi.md#deleteMsgVpnJndiConnectionFactory) | **DELETE** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Delete a JNDI Connection Factory object.
*AllApi* | [**deleteMsgVpnJndiQueue**](docs/AllApi.md#deleteMsgVpnJndiQueue) | **DELETE** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Delete a JNDI Queue object.
*AllApi* | [**deleteMsgVpnJndiTopic**](docs/AllApi.md#deleteMsgVpnJndiTopic) | **DELETE** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Delete a JNDI Topic object.
*AllApi* | [**deleteMsgVpnMqttRetainCache**](docs/AllApi.md#deleteMsgVpnMqttRetainCache) | **DELETE** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Delete an MQTT Retain Cache object.
*AllApi* | [**deleteMsgVpnMqttSession**](docs/AllApi.md#deleteMsgVpnMqttSession) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Delete an MQTT Session object.
*AllApi* | [**deleteMsgVpnMqttSessionSubscription**](docs/AllApi.md#deleteMsgVpnMqttSessionSubscription) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Delete a Subscription object.
*AllApi* | [**deleteMsgVpnQueue**](docs/AllApi.md#deleteMsgVpnQueue) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName} | Delete a Queue object.
*AllApi* | [**deleteMsgVpnQueueSubscription**](docs/AllApi.md#deleteMsgVpnQueueSubscription) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Delete a Queue Subscription object.
*AllApi* | [**deleteMsgVpnQueueTemplate**](docs/AllApi.md#deleteMsgVpnQueueTemplate) | **DELETE** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Delete a Queue Template object.
*AllApi* | [**deleteMsgVpnReplayLog**](docs/AllApi.md#deleteMsgVpnReplayLog) | **DELETE** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Delete a Replay Log object.
*AllApi* | [**deleteMsgVpnReplayLogTopicFilterSubscription**](docs/AllApi.md#deleteMsgVpnReplayLogTopicFilterSubscription) | **DELETE** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions/{topicFilterSubscription} | Delete a Topic Filter Subscription object.
*AllApi* | [**deleteMsgVpnReplicatedTopic**](docs/AllApi.md#deleteMsgVpnReplicatedTopic) | **DELETE** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Delete a Replicated Topic object.
*AllApi* | [**deleteMsgVpnRestDeliveryPoint**](docs/AllApi.md#deleteMsgVpnRestDeliveryPoint) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Delete a REST Delivery Point object.
*AllApi* | [**deleteMsgVpnRestDeliveryPointQueueBinding**](docs/AllApi.md#deleteMsgVpnRestDeliveryPointQueueBinding) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Delete a Queue Binding object.
*AllApi* | [**deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/AllApi.md#deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Delete a Request Header object.
*AllApi* | [**deleteMsgVpnRestDeliveryPointRestConsumer**](docs/AllApi.md#deleteMsgVpnRestDeliveryPointRestConsumer) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Delete a REST Consumer object.
*AllApi* | [**deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/AllApi.md#deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName} | Delete a Claim object.
*AllApi* | [**deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/AllApi.md#deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*AllApi* | [**deleteMsgVpnSequencedTopic**](docs/AllApi.md#deleteMsgVpnSequencedTopic) | **DELETE** /msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic} | Delete a Sequenced Topic object.
*AllApi* | [**deleteMsgVpnTopicEndpoint**](docs/AllApi.md#deleteMsgVpnTopicEndpoint) | **DELETE** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Delete a Topic Endpoint object.
*AllApi* | [**deleteMsgVpnTopicEndpointTemplate**](docs/AllApi.md#deleteMsgVpnTopicEndpointTemplate) | **DELETE** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Delete a Topic Endpoint Template object.
*AllApi* | [**deleteOauthProfile**](docs/AllApi.md#deleteOauthProfile) | **DELETE** /oauthProfiles/{oauthProfileName} | Delete an OAuth Profile object.
*AllApi* | [**deleteOauthProfileAccessLevelGroup**](docs/AllApi.md#deleteOauthProfileAccessLevelGroup) | **DELETE** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Delete a Group Access Level object.
*AllApi* | [**deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/AllApi.md#deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **DELETE** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access-Level Exception object.
*AllApi* | [**deleteOauthProfileClientAllowedHost**](docs/AllApi.md#deleteOauthProfileClientAllowedHost) | **DELETE** /oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost} | Delete an Allowed Host Value object.
*AllApi* | [**deleteOauthProfileClientAuthorizationParameter**](docs/AllApi.md#deleteOauthProfileClientAuthorizationParameter) | **DELETE** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Delete an Authorization Parameter object.
*AllApi* | [**deleteOauthProfileClientRequiredClaim**](docs/AllApi.md#deleteOauthProfileClientRequiredClaim) | **DELETE** /oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Delete a Required Claim object.
*AllApi* | [**deleteOauthProfileDefaultMsgVpnAccessLevelException**](docs/AllApi.md#deleteOauthProfileDefaultMsgVpnAccessLevelException) | **DELETE** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access-Level Exception object.
*AllApi* | [**deleteOauthProfileResourceServerRequiredClaim**](docs/AllApi.md#deleteOauthProfileResourceServerRequiredClaim) | **DELETE** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Delete a Required Claim object.
*AllApi* | [**deleteVirtualHostname**](docs/AllApi.md#deleteVirtualHostname) | **DELETE** /virtualHostnames/{virtualHostname} | Delete a Virtual Hostname object.
*AllApi* | [**getAbout**](docs/AllApi.md#getAbout) | **GET** /about | Get an About object.
*AllApi* | [**getAboutApi**](docs/AllApi.md#getAboutApi) | **GET** /about/api | Get an API Description object.
*AllApi* | [**getAboutUser**](docs/AllApi.md#getAboutUser) | **GET** /about/user | Get a User object.
*AllApi* | [**getAboutUserMsgVpn**](docs/AllApi.md#getAboutUserMsgVpn) | **GET** /about/user/msgVpns/{msgVpnName} | Get a User Message VPN object.
*AllApi* | [**getAboutUserMsgVpns**](docs/AllApi.md#getAboutUserMsgVpns) | **GET** /about/user/msgVpns | Get a list of User Message VPN objects.
*AllApi* | [**getBroker**](docs/AllApi.md#getBroker) | **GET** / | Get a Broker object.
*AllApi* | [**getCertAuthorities**](docs/AllApi.md#getCertAuthorities) | **GET** /certAuthorities | Get a list of Certificate Authority objects.
*AllApi* | [**getCertAuthority**](docs/AllApi.md#getCertAuthority) | **GET** /certAuthorities/{certAuthorityName} | Get a Certificate Authority object.
*AllApi* | [**getCertAuthorityOcspTlsTrustedCommonName**](docs/AllApi.md#getCertAuthorityOcspTlsTrustedCommonName) | **GET** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Get an OCSP Responder Trusted Common Name object.
*AllApi* | [**getCertAuthorityOcspTlsTrustedCommonNames**](docs/AllApi.md#getCertAuthorityOcspTlsTrustedCommonNames) | **GET** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Get a list of OCSP Responder Trusted Common Name objects.
*AllApi* | [**getClientCertAuthorities**](docs/AllApi.md#getClientCertAuthorities) | **GET** /clientCertAuthorities | Get a list of Client Certificate Authority objects.
*AllApi* | [**getClientCertAuthority**](docs/AllApi.md#getClientCertAuthority) | **GET** /clientCertAuthorities/{certAuthorityName} | Get a Client Certificate Authority object.
*AllApi* | [**getClientCertAuthorityOcspTlsTrustedCommonName**](docs/AllApi.md#getClientCertAuthorityOcspTlsTrustedCommonName) | **GET** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Get an OCSP Responder Trusted Common Name object.
*AllApi* | [**getClientCertAuthorityOcspTlsTrustedCommonNames**](docs/AllApi.md#getClientCertAuthorityOcspTlsTrustedCommonNames) | **GET** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Get a list of OCSP Responder Trusted Common Name objects.
*AllApi* | [**getDmrCluster**](docs/AllApi.md#getDmrCluster) | **GET** /dmrClusters/{dmrClusterName} | Get a Cluster object.
*AllApi* | [**getDmrClusterCertMatchingRule**](docs/AllApi.md#getDmrClusterCertMatchingRule) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object.
*AllApi* | [**getDmrClusterCertMatchingRuleAttributeFilter**](docs/AllApi.md#getDmrClusterCertMatchingRuleAttributeFilter) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**getDmrClusterCertMatchingRuleAttributeFilters**](docs/AllApi.md#getDmrClusterCertMatchingRuleAttributeFilters) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects.
*AllApi* | [**getDmrClusterCertMatchingRuleCondition**](docs/AllApi.md#getDmrClusterCertMatchingRuleCondition) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object.
*AllApi* | [**getDmrClusterCertMatchingRuleConditions**](docs/AllApi.md#getDmrClusterCertMatchingRuleConditions) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects.
*AllApi* | [**getDmrClusterCertMatchingRules**](docs/AllApi.md#getDmrClusterCertMatchingRules) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules | Get a list of Certificate Matching Rule objects.
*AllApi* | [**getDmrClusterLink**](docs/AllApi.md#getDmrClusterLink) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Get a Link object.
*AllApi* | [**getDmrClusterLinkAttribute**](docs/AllApi.md#getDmrClusterLinkAttribute) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes/{attributeName},{attributeValue} | Get a Link Attribute object.
*AllApi* | [**getDmrClusterLinkAttributes**](docs/AllApi.md#getDmrClusterLinkAttributes) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes | Get a list of Link Attribute objects.
*AllApi* | [**getDmrClusterLinkRemoteAddress**](docs/AllApi.md#getDmrClusterLinkRemoteAddress) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Get a Remote Address object.
*AllApi* | [**getDmrClusterLinkRemoteAddresses**](docs/AllApi.md#getDmrClusterLinkRemoteAddresses) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Get a list of Remote Address objects.
*AllApi* | [**getDmrClusterLinkTlsTrustedCommonName**](docs/AllApi.md#getDmrClusterLinkTlsTrustedCommonName) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*AllApi* | [**getDmrClusterLinkTlsTrustedCommonNames**](docs/AllApi.md#getDmrClusterLinkTlsTrustedCommonNames) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*AllApi* | [**getDmrClusterLinks**](docs/AllApi.md#getDmrClusterLinks) | **GET** /dmrClusters/{dmrClusterName}/links | Get a list of Link objects.
*AllApi* | [**getDmrClusters**](docs/AllApi.md#getDmrClusters) | **GET** /dmrClusters | Get a list of Cluster objects.
*AllApi* | [**getDomainCertAuthorities**](docs/AllApi.md#getDomainCertAuthorities) | **GET** /domainCertAuthorities | Get a list of Domain Certificate Authority objects.
*AllApi* | [**getDomainCertAuthority**](docs/AllApi.md#getDomainCertAuthority) | **GET** /domainCertAuthorities/{certAuthorityName} | Get a Domain Certificate Authority object.
*AllApi* | [**getMsgVpn**](docs/AllApi.md#getMsgVpn) | **GET** /msgVpns/{msgVpnName} | Get a Message VPN object.
*AllApi* | [**getMsgVpnAclProfile**](docs/AllApi.md#getMsgVpnAclProfile) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Get an ACL Profile object.
*AllApi* | [**getMsgVpnAclProfileClientConnectException**](docs/AllApi.md#getMsgVpnAclProfileClientConnectException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Get a Client Connect Exception object.
*AllApi* | [**getMsgVpnAclProfileClientConnectExceptions**](docs/AllApi.md#getMsgVpnAclProfileClientConnectExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Get a list of Client Connect Exception objects.
*AllApi* | [**getMsgVpnAclProfilePublishException**](docs/AllApi.md#getMsgVpnAclProfilePublishException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Get a Publish Topic Exception object.
*AllApi* | [**getMsgVpnAclProfilePublishExceptions**](docs/AllApi.md#getMsgVpnAclProfilePublishExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Get a list of Publish Topic Exception objects.
*AllApi* | [**getMsgVpnAclProfilePublishTopicException**](docs/AllApi.md#getMsgVpnAclProfilePublishTopicException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions/{publishTopicExceptionSyntax},{publishTopicException} | Get a Publish Topic Exception object.
*AllApi* | [**getMsgVpnAclProfilePublishTopicExceptions**](docs/AllApi.md#getMsgVpnAclProfilePublishTopicExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions | Get a list of Publish Topic Exception objects.
*AllApi* | [**getMsgVpnAclProfileSubscribeException**](docs/AllApi.md#getMsgVpnAclProfileSubscribeException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Get a Subscribe Topic Exception object.
*AllApi* | [**getMsgVpnAclProfileSubscribeExceptions**](docs/AllApi.md#getMsgVpnAclProfileSubscribeExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Get a list of Subscribe Topic Exception objects.
*AllApi* | [**getMsgVpnAclProfileSubscribeShareNameException**](docs/AllApi.md#getMsgVpnAclProfileSubscribeShareNameException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions/{subscribeShareNameExceptionSyntax},{subscribeShareNameException} | Get a Subscribe Share Name Exception object.
*AllApi* | [**getMsgVpnAclProfileSubscribeShareNameExceptions**](docs/AllApi.md#getMsgVpnAclProfileSubscribeShareNameExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions | Get a list of Subscribe Share Name Exception objects.
*AllApi* | [**getMsgVpnAclProfileSubscribeTopicException**](docs/AllApi.md#getMsgVpnAclProfileSubscribeTopicException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions/{subscribeTopicExceptionSyntax},{subscribeTopicException} | Get a Subscribe Topic Exception object.
*AllApi* | [**getMsgVpnAclProfileSubscribeTopicExceptions**](docs/AllApi.md#getMsgVpnAclProfileSubscribeTopicExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions | Get a list of Subscribe Topic Exception objects.
*AllApi* | [**getMsgVpnAclProfiles**](docs/AllApi.md#getMsgVpnAclProfiles) | **GET** /msgVpns/{msgVpnName}/aclProfiles | Get a list of ACL Profile objects.
*AllApi* | [**getMsgVpnAuthenticationOauthProfile**](docs/AllApi.md#getMsgVpnAuthenticationOauthProfile) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Get an OAuth Profile object.
*AllApi* | [**getMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/AllApi.md#getMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Get a Required Claim object.
*AllApi* | [**getMsgVpnAuthenticationOauthProfileClientRequiredClaims**](docs/AllApi.md#getMsgVpnAuthenticationOauthProfileClientRequiredClaims) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims | Get a list of Required Claim objects.
*AllApi* | [**getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/AllApi.md#getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Get a Required Claim object.
*AllApi* | [**getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaims**](docs/AllApi.md#getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaims) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Get a list of Required Claim objects.
*AllApi* | [**getMsgVpnAuthenticationOauthProfiles**](docs/AllApi.md#getMsgVpnAuthenticationOauthProfiles) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles | Get a list of OAuth Profile objects.
*AllApi* | [**getMsgVpnAuthenticationOauthProvider**](docs/AllApi.md#getMsgVpnAuthenticationOauthProvider) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Get an OAuth Provider object.
*AllApi* | [**getMsgVpnAuthenticationOauthProviders**](docs/AllApi.md#getMsgVpnAuthenticationOauthProviders) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders | Get a list of OAuth Provider objects.
*AllApi* | [**getMsgVpnAuthorizationGroup**](docs/AllApi.md#getMsgVpnAuthorizationGroup) | **GET** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Get an Authorization Group object.
*AllApi* | [**getMsgVpnAuthorizationGroups**](docs/AllApi.md#getMsgVpnAuthorizationGroups) | **GET** /msgVpns/{msgVpnName}/authorizationGroups | Get a list of Authorization Group objects.
*AllApi* | [**getMsgVpnBridge**](docs/AllApi.md#getMsgVpnBridge) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Get a Bridge object.
*AllApi* | [**getMsgVpnBridgeRemoteMsgVpn**](docs/AllApi.md#getMsgVpnBridgeRemoteMsgVpn) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Get a Remote Message VPN object.
*AllApi* | [**getMsgVpnBridgeRemoteMsgVpns**](docs/AllApi.md#getMsgVpnBridgeRemoteMsgVpns) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Get a list of Remote Message VPN objects.
*AllApi* | [**getMsgVpnBridgeRemoteSubscription**](docs/AllApi.md#getMsgVpnBridgeRemoteSubscription) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Get a Remote Subscription object.
*AllApi* | [**getMsgVpnBridgeRemoteSubscriptions**](docs/AllApi.md#getMsgVpnBridgeRemoteSubscriptions) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Get a list of Remote Subscription objects.
*AllApi* | [**getMsgVpnBridgeTlsTrustedCommonName**](docs/AllApi.md#getMsgVpnBridgeTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*AllApi* | [**getMsgVpnBridgeTlsTrustedCommonNames**](docs/AllApi.md#getMsgVpnBridgeTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*AllApi* | [**getMsgVpnBridges**](docs/AllApi.md#getMsgVpnBridges) | **GET** /msgVpns/{msgVpnName}/bridges | Get a list of Bridge objects.
*AllApi* | [**getMsgVpnCertMatchingRule**](docs/AllApi.md#getMsgVpnCertMatchingRule) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object.
*AllApi* | [**getMsgVpnCertMatchingRuleAttributeFilter**](docs/AllApi.md#getMsgVpnCertMatchingRuleAttributeFilter) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**getMsgVpnCertMatchingRuleAttributeFilters**](docs/AllApi.md#getMsgVpnCertMatchingRuleAttributeFilters) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects.
*AllApi* | [**getMsgVpnCertMatchingRuleCondition**](docs/AllApi.md#getMsgVpnCertMatchingRuleCondition) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object.
*AllApi* | [**getMsgVpnCertMatchingRuleConditions**](docs/AllApi.md#getMsgVpnCertMatchingRuleConditions) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects.
*AllApi* | [**getMsgVpnCertMatchingRules**](docs/AllApi.md#getMsgVpnCertMatchingRules) | **GET** /msgVpns/{msgVpnName}/certMatchingRules | Get a list of Certificate Matching Rule objects.
*AllApi* | [**getMsgVpnClientProfile**](docs/AllApi.md#getMsgVpnClientProfile) | **GET** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Get a Client Profile object.
*AllApi* | [**getMsgVpnClientProfiles**](docs/AllApi.md#getMsgVpnClientProfiles) | **GET** /msgVpns/{msgVpnName}/clientProfiles | Get a list of Client Profile objects.
*AllApi* | [**getMsgVpnClientUsername**](docs/AllApi.md#getMsgVpnClientUsername) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Get a Client Username object.
*AllApi* | [**getMsgVpnClientUsernameAttribute**](docs/AllApi.md#getMsgVpnClientUsernameAttribute) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Get a Client Username Attribute object.
*AllApi* | [**getMsgVpnClientUsernameAttributes**](docs/AllApi.md#getMsgVpnClientUsernameAttributes) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Get a list of Client Username Attribute objects.
*AllApi* | [**getMsgVpnClientUsernames**](docs/AllApi.md#getMsgVpnClientUsernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Get a list of Client Username objects.
*AllApi* | [**getMsgVpnDistributedCache**](docs/AllApi.md#getMsgVpnDistributedCache) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Get a Distributed Cache object.
*AllApi* | [**getMsgVpnDistributedCacheCluster**](docs/AllApi.md#getMsgVpnDistributedCacheCluster) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Get a Cache Cluster object.
*AllApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/AllApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName} | Get a Home Cache Cluster object.
*AllApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/AllApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix} | Get a Topic Prefix object.
*AllApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes**](docs/AllApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes | Get a list of Topic Prefix objects.
*AllApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters**](docs/AllApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters | Get a list of Home Cache Cluster objects.
*AllApi* | [**getMsgVpnDistributedCacheClusterInstance**](docs/AllApi.md#getMsgVpnDistributedCacheClusterInstance) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Get a Cache Instance object.
*AllApi* | [**getMsgVpnDistributedCacheClusterInstances**](docs/AllApi.md#getMsgVpnDistributedCacheClusterInstances) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances | Get a list of Cache Instance objects.
*AllApi* | [**getMsgVpnDistributedCacheClusterTopic**](docs/AllApi.md#getMsgVpnDistributedCacheClusterTopic) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic} | Get a Topic object.
*AllApi* | [**getMsgVpnDistributedCacheClusterTopics**](docs/AllApi.md#getMsgVpnDistributedCacheClusterTopics) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics | Get a list of Topic objects.
*AllApi* | [**getMsgVpnDistributedCacheClusters**](docs/AllApi.md#getMsgVpnDistributedCacheClusters) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters | Get a list of Cache Cluster objects.
*AllApi* | [**getMsgVpnDistributedCaches**](docs/AllApi.md#getMsgVpnDistributedCaches) | **GET** /msgVpns/{msgVpnName}/distributedCaches | Get a list of Distributed Cache objects.
*AllApi* | [**getMsgVpnDmrBridge**](docs/AllApi.md#getMsgVpnDmrBridge) | **GET** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Get a DMR Bridge object.
*AllApi* | [**getMsgVpnDmrBridges**](docs/AllApi.md#getMsgVpnDmrBridges) | **GET** /msgVpns/{msgVpnName}/dmrBridges | Get a list of DMR Bridge objects.
*AllApi* | [**getMsgVpnJndiConnectionFactories**](docs/AllApi.md#getMsgVpnJndiConnectionFactories) | **GET** /msgVpns/{msgVpnName}/jndiConnectionFactories | Get a list of JNDI Connection Factory objects.
*AllApi* | [**getMsgVpnJndiConnectionFactory**](docs/AllApi.md#getMsgVpnJndiConnectionFactory) | **GET** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Get a JNDI Connection Factory object.
*AllApi* | [**getMsgVpnJndiQueue**](docs/AllApi.md#getMsgVpnJndiQueue) | **GET** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Get a JNDI Queue object.
*AllApi* | [**getMsgVpnJndiQueues**](docs/AllApi.md#getMsgVpnJndiQueues) | **GET** /msgVpns/{msgVpnName}/jndiQueues | Get a list of JNDI Queue objects.
*AllApi* | [**getMsgVpnJndiTopic**](docs/AllApi.md#getMsgVpnJndiTopic) | **GET** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Get a JNDI Topic object.
*AllApi* | [**getMsgVpnJndiTopics**](docs/AllApi.md#getMsgVpnJndiTopics) | **GET** /msgVpns/{msgVpnName}/jndiTopics | Get a list of JNDI Topic objects.
*AllApi* | [**getMsgVpnMqttRetainCache**](docs/AllApi.md#getMsgVpnMqttRetainCache) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Get an MQTT Retain Cache object.
*AllApi* | [**getMsgVpnMqttRetainCaches**](docs/AllApi.md#getMsgVpnMqttRetainCaches) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches | Get a list of MQTT Retain Cache objects.
*AllApi* | [**getMsgVpnMqttSession**](docs/AllApi.md#getMsgVpnMqttSession) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Get an MQTT Session object.
*AllApi* | [**getMsgVpnMqttSessionSubscription**](docs/AllApi.md#getMsgVpnMqttSessionSubscription) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Get a Subscription object.
*AllApi* | [**getMsgVpnMqttSessionSubscriptions**](docs/AllApi.md#getMsgVpnMqttSessionSubscriptions) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Get a list of Subscription objects.
*AllApi* | [**getMsgVpnMqttSessions**](docs/AllApi.md#getMsgVpnMqttSessions) | **GET** /msgVpns/{msgVpnName}/mqttSessions | Get a list of MQTT Session objects.
*AllApi* | [**getMsgVpnQueue**](docs/AllApi.md#getMsgVpnQueue) | **GET** /msgVpns/{msgVpnName}/queues/{queueName} | Get a Queue object.
*AllApi* | [**getMsgVpnQueueSubscription**](docs/AllApi.md#getMsgVpnQueueSubscription) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Get a Queue Subscription object.
*AllApi* | [**getMsgVpnQueueSubscriptions**](docs/AllApi.md#getMsgVpnQueueSubscriptions) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Get a list of Queue Subscription objects.
*AllApi* | [**getMsgVpnQueueTemplate**](docs/AllApi.md#getMsgVpnQueueTemplate) | **GET** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Get a Queue Template object.
*AllApi* | [**getMsgVpnQueueTemplates**](docs/AllApi.md#getMsgVpnQueueTemplates) | **GET** /msgVpns/{msgVpnName}/queueTemplates | Get a list of Queue Template objects.
*AllApi* | [**getMsgVpnQueues**](docs/AllApi.md#getMsgVpnQueues) | **GET** /msgVpns/{msgVpnName}/queues | Get a list of Queue objects.
*AllApi* | [**getMsgVpnReplayLog**](docs/AllApi.md#getMsgVpnReplayLog) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Get a Replay Log object.
*AllApi* | [**getMsgVpnReplayLogTopicFilterSubscription**](docs/AllApi.md#getMsgVpnReplayLogTopicFilterSubscription) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions/{topicFilterSubscription} | Get a Topic Filter Subscription object.
*AllApi* | [**getMsgVpnReplayLogTopicFilterSubscriptions**](docs/AllApi.md#getMsgVpnReplayLogTopicFilterSubscriptions) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions | Get a list of Topic Filter Subscription objects.
*AllApi* | [**getMsgVpnReplayLogs**](docs/AllApi.md#getMsgVpnReplayLogs) | **GET** /msgVpns/{msgVpnName}/replayLogs | Get a list of Replay Log objects.
*AllApi* | [**getMsgVpnReplicatedTopic**](docs/AllApi.md#getMsgVpnReplicatedTopic) | **GET** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Get a Replicated Topic object.
*AllApi* | [**getMsgVpnReplicatedTopics**](docs/AllApi.md#getMsgVpnReplicatedTopics) | **GET** /msgVpns/{msgVpnName}/replicatedTopics | Get a list of Replicated Topic objects.
*AllApi* | [**getMsgVpnRestDeliveryPoint**](docs/AllApi.md#getMsgVpnRestDeliveryPoint) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Get a REST Delivery Point object.
*AllApi* | [**getMsgVpnRestDeliveryPointQueueBinding**](docs/AllApi.md#getMsgVpnRestDeliveryPointQueueBinding) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Get a Queue Binding object.
*AllApi* | [**getMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/AllApi.md#getMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Get a Request Header object.
*AllApi* | [**getMsgVpnRestDeliveryPointQueueBindingRequestHeaders**](docs/AllApi.md#getMsgVpnRestDeliveryPointQueueBindingRequestHeaders) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders | Get a list of Request Header objects.
*AllApi* | [**getMsgVpnRestDeliveryPointQueueBindings**](docs/AllApi.md#getMsgVpnRestDeliveryPointQueueBindings) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Get a list of Queue Binding objects.
*AllApi* | [**getMsgVpnRestDeliveryPointRestConsumer**](docs/AllApi.md#getMsgVpnRestDeliveryPointRestConsumer) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Get a REST Consumer object.
*AllApi* | [**getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/AllApi.md#getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName} | Get a Claim object.
*AllApi* | [**getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims**](docs/AllApi.md#getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims | Get a list of Claim objects.
*AllApi* | [**getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/AllApi.md#getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*AllApi* | [**getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames**](docs/AllApi.md#getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*AllApi* | [**getMsgVpnRestDeliveryPointRestConsumers**](docs/AllApi.md#getMsgVpnRestDeliveryPointRestConsumers) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Get a list of REST Consumer objects.
*AllApi* | [**getMsgVpnRestDeliveryPoints**](docs/AllApi.md#getMsgVpnRestDeliveryPoints) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints | Get a list of REST Delivery Point objects.
*AllApi* | [**getMsgVpnSequencedTopic**](docs/AllApi.md#getMsgVpnSequencedTopic) | **GET** /msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic} | Get a Sequenced Topic object.
*AllApi* | [**getMsgVpnSequencedTopics**](docs/AllApi.md#getMsgVpnSequencedTopics) | **GET** /msgVpns/{msgVpnName}/sequencedTopics | Get a list of Sequenced Topic objects.
*AllApi* | [**getMsgVpnTopicEndpoint**](docs/AllApi.md#getMsgVpnTopicEndpoint) | **GET** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Get a Topic Endpoint object.
*AllApi* | [**getMsgVpnTopicEndpointTemplate**](docs/AllApi.md#getMsgVpnTopicEndpointTemplate) | **GET** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Get a Topic Endpoint Template object.
*AllApi* | [**getMsgVpnTopicEndpointTemplates**](docs/AllApi.md#getMsgVpnTopicEndpointTemplates) | **GET** /msgVpns/{msgVpnName}/topicEndpointTemplates | Get a list of Topic Endpoint Template objects.
*AllApi* | [**getMsgVpnTopicEndpoints**](docs/AllApi.md#getMsgVpnTopicEndpoints) | **GET** /msgVpns/{msgVpnName}/topicEndpoints | Get a list of Topic Endpoint objects.
*AllApi* | [**getMsgVpns**](docs/AllApi.md#getMsgVpns) | **GET** /msgVpns | Get a list of Message VPN objects.
*AllApi* | [**getOauthProfile**](docs/AllApi.md#getOauthProfile) | **GET** /oauthProfiles/{oauthProfileName} | Get an OAuth Profile object.
*AllApi* | [**getOauthProfileAccessLevelGroup**](docs/AllApi.md#getOauthProfileAccessLevelGroup) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Get a Group Access Level object.
*AllApi* | [**getOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/AllApi.md#getOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access-Level Exception object.
*AllApi* | [**getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions**](docs/AllApi.md#getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions | Get a list of Message VPN Access-Level Exception objects.
*AllApi* | [**getOauthProfileAccessLevelGroups**](docs/AllApi.md#getOauthProfileAccessLevelGroups) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups | Get a list of Group Access Level objects.
*AllApi* | [**getOauthProfileClientAllowedHost**](docs/AllApi.md#getOauthProfileClientAllowedHost) | **GET** /oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost} | Get an Allowed Host Value object.
*AllApi* | [**getOauthProfileClientAllowedHosts**](docs/AllApi.md#getOauthProfileClientAllowedHosts) | **GET** /oauthProfiles/{oauthProfileName}/clientAllowedHosts | Get a list of Allowed Host Value objects.
*AllApi* | [**getOauthProfileClientAuthorizationParameter**](docs/AllApi.md#getOauthProfileClientAuthorizationParameter) | **GET** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Get an Authorization Parameter object.
*AllApi* | [**getOauthProfileClientAuthorizationParameters**](docs/AllApi.md#getOauthProfileClientAuthorizationParameters) | **GET** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters | Get a list of Authorization Parameter objects.
*AllApi* | [**getOauthProfileClientRequiredClaim**](docs/AllApi.md#getOauthProfileClientRequiredClaim) | **GET** /oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Get a Required Claim object.
*AllApi* | [**getOauthProfileClientRequiredClaims**](docs/AllApi.md#getOauthProfileClientRequiredClaims) | **GET** /oauthProfiles/{oauthProfileName}/clientRequiredClaims | Get a list of Required Claim objects.
*AllApi* | [**getOauthProfileDefaultMsgVpnAccessLevelException**](docs/AllApi.md#getOauthProfileDefaultMsgVpnAccessLevelException) | **GET** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access-Level Exception object.
*AllApi* | [**getOauthProfileDefaultMsgVpnAccessLevelExceptions**](docs/AllApi.md#getOauthProfileDefaultMsgVpnAccessLevelExceptions) | **GET** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions | Get a list of Message VPN Access-Level Exception objects.
*AllApi* | [**getOauthProfileResourceServerRequiredClaim**](docs/AllApi.md#getOauthProfileResourceServerRequiredClaim) | **GET** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Get a Required Claim object.
*AllApi* | [**getOauthProfileResourceServerRequiredClaims**](docs/AllApi.md#getOauthProfileResourceServerRequiredClaims) | **GET** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Get a list of Required Claim objects.
*AllApi* | [**getOauthProfiles**](docs/AllApi.md#getOauthProfiles) | **GET** /oauthProfiles | Get a list of OAuth Profile objects.
*AllApi* | [**getSystemInformation**](docs/AllApi.md#getSystemInformation) | **GET** /systemInformation | Get a System Information object.
*AllApi* | [**getVirtualHostname**](docs/AllApi.md#getVirtualHostname) | **GET** /virtualHostnames/{virtualHostname} | Get a Virtual Hostname object.
*AllApi* | [**getVirtualHostnames**](docs/AllApi.md#getVirtualHostnames) | **GET** /virtualHostnames | Get a list of Virtual Hostname objects.
*AllApi* | [**replaceCertAuthority**](docs/AllApi.md#replaceCertAuthority) | **PUT** /certAuthorities/{certAuthorityName} | Replace a Certificate Authority object.
*AllApi* | [**replaceClientCertAuthority**](docs/AllApi.md#replaceClientCertAuthority) | **PUT** /clientCertAuthorities/{certAuthorityName} | Replace a Client Certificate Authority object.
*AllApi* | [**replaceDmrCluster**](docs/AllApi.md#replaceDmrCluster) | **PUT** /dmrClusters/{dmrClusterName} | Replace a Cluster object.
*AllApi* | [**replaceDmrClusterCertMatchingRule**](docs/AllApi.md#replaceDmrClusterCertMatchingRule) | **PUT** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object.
*AllApi* | [**replaceDmrClusterCertMatchingRuleAttributeFilter**](docs/AllApi.md#replaceDmrClusterCertMatchingRuleAttributeFilter) | **PUT** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**replaceDmrClusterLink**](docs/AllApi.md#replaceDmrClusterLink) | **PUT** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Replace a Link object.
*AllApi* | [**replaceDomainCertAuthority**](docs/AllApi.md#replaceDomainCertAuthority) | **PUT** /domainCertAuthorities/{certAuthorityName} | Replace a Domain Certificate Authority object.
*AllApi* | [**replaceMsgVpn**](docs/AllApi.md#replaceMsgVpn) | **PUT** /msgVpns/{msgVpnName} | Replace a Message VPN object.
*AllApi* | [**replaceMsgVpnAclProfile**](docs/AllApi.md#replaceMsgVpnAclProfile) | **PUT** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Replace an ACL Profile object.
*AllApi* | [**replaceMsgVpnAuthenticationOauthProfile**](docs/AllApi.md#replaceMsgVpnAuthenticationOauthProfile) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Replace an OAuth Profile object.
*AllApi* | [**replaceMsgVpnAuthenticationOauthProvider**](docs/AllApi.md#replaceMsgVpnAuthenticationOauthProvider) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Replace an OAuth Provider object.
*AllApi* | [**replaceMsgVpnAuthorizationGroup**](docs/AllApi.md#replaceMsgVpnAuthorizationGroup) | **PUT** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Replace an Authorization Group object.
*AllApi* | [**replaceMsgVpnBridge**](docs/AllApi.md#replaceMsgVpnBridge) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Replace a Bridge object.
*AllApi* | [**replaceMsgVpnBridgeRemoteMsgVpn**](docs/AllApi.md#replaceMsgVpnBridgeRemoteMsgVpn) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Replace a Remote Message VPN object.
*AllApi* | [**replaceMsgVpnCertMatchingRule**](docs/AllApi.md#replaceMsgVpnCertMatchingRule) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object.
*AllApi* | [**replaceMsgVpnCertMatchingRuleAttributeFilter**](docs/AllApi.md#replaceMsgVpnCertMatchingRuleAttributeFilter) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**replaceMsgVpnClientProfile**](docs/AllApi.md#replaceMsgVpnClientProfile) | **PUT** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Replace a Client Profile object.
*AllApi* | [**replaceMsgVpnClientUsername**](docs/AllApi.md#replaceMsgVpnClientUsername) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replace a Client Username object.
*AllApi* | [**replaceMsgVpnDistributedCache**](docs/AllApi.md#replaceMsgVpnDistributedCache) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Replace a Distributed Cache object.
*AllApi* | [**replaceMsgVpnDistributedCacheCluster**](docs/AllApi.md#replaceMsgVpnDistributedCacheCluster) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Replace a Cache Cluster object.
*AllApi* | [**replaceMsgVpnDistributedCacheClusterInstance**](docs/AllApi.md#replaceMsgVpnDistributedCacheClusterInstance) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Replace a Cache Instance object.
*AllApi* | [**replaceMsgVpnDmrBridge**](docs/AllApi.md#replaceMsgVpnDmrBridge) | **PUT** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Replace a DMR Bridge object.
*AllApi* | [**replaceMsgVpnJndiConnectionFactory**](docs/AllApi.md#replaceMsgVpnJndiConnectionFactory) | **PUT** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Replace a JNDI Connection Factory object.
*AllApi* | [**replaceMsgVpnJndiQueue**](docs/AllApi.md#replaceMsgVpnJndiQueue) | **PUT** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Replace a JNDI Queue object.
*AllApi* | [**replaceMsgVpnJndiTopic**](docs/AllApi.md#replaceMsgVpnJndiTopic) | **PUT** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Replace a JNDI Topic object.
*AllApi* | [**replaceMsgVpnMqttRetainCache**](docs/AllApi.md#replaceMsgVpnMqttRetainCache) | **PUT** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Replace an MQTT Retain Cache object.
*AllApi* | [**replaceMsgVpnMqttSession**](docs/AllApi.md#replaceMsgVpnMqttSession) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Replace an MQTT Session object.
*AllApi* | [**replaceMsgVpnMqttSessionSubscription**](docs/AllApi.md#replaceMsgVpnMqttSessionSubscription) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Replace a Subscription object.
*AllApi* | [**replaceMsgVpnQueue**](docs/AllApi.md#replaceMsgVpnQueue) | **PUT** /msgVpns/{msgVpnName}/queues/{queueName} | Replace a Queue object.
*AllApi* | [**replaceMsgVpnQueueTemplate**](docs/AllApi.md#replaceMsgVpnQueueTemplate) | **PUT** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Replace a Queue Template object.
*AllApi* | [**replaceMsgVpnReplayLog**](docs/AllApi.md#replaceMsgVpnReplayLog) | **PUT** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Replace a Replay Log object.
*AllApi* | [**replaceMsgVpnReplicatedTopic**](docs/AllApi.md#replaceMsgVpnReplicatedTopic) | **PUT** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Replace a Replicated Topic object.
*AllApi* | [**replaceMsgVpnRestDeliveryPoint**](docs/AllApi.md#replaceMsgVpnRestDeliveryPoint) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Replace a REST Delivery Point object.
*AllApi* | [**replaceMsgVpnRestDeliveryPointQueueBinding**](docs/AllApi.md#replaceMsgVpnRestDeliveryPointQueueBinding) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Replace a Queue Binding object.
*AllApi* | [**replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/AllApi.md#replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Replace a Request Header object.
*AllApi* | [**replaceMsgVpnRestDeliveryPointRestConsumer**](docs/AllApi.md#replaceMsgVpnRestDeliveryPointRestConsumer) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Replace a REST Consumer object.
*AllApi* | [**replaceMsgVpnTopicEndpoint**](docs/AllApi.md#replaceMsgVpnTopicEndpoint) | **PUT** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Replace a Topic Endpoint object.
*AllApi* | [**replaceMsgVpnTopicEndpointTemplate**](docs/AllApi.md#replaceMsgVpnTopicEndpointTemplate) | **PUT** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Replace a Topic Endpoint Template object.
*AllApi* | [**replaceOauthProfile**](docs/AllApi.md#replaceOauthProfile) | **PUT** /oauthProfiles/{oauthProfileName} | Replace an OAuth Profile object.
*AllApi* | [**replaceOauthProfileAccessLevelGroup**](docs/AllApi.md#replaceOauthProfileAccessLevelGroup) | **PUT** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Replace a Group Access Level object.
*AllApi* | [**replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/AllApi.md#replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **PUT** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access-Level Exception object.
*AllApi* | [**replaceOauthProfileClientAuthorizationParameter**](docs/AllApi.md#replaceOauthProfileClientAuthorizationParameter) | **PUT** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Replace an Authorization Parameter object.
*AllApi* | [**replaceOauthProfileDefaultMsgVpnAccessLevelException**](docs/AllApi.md#replaceOauthProfileDefaultMsgVpnAccessLevelException) | **PUT** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access-Level Exception object.
*AllApi* | [**replaceVirtualHostname**](docs/AllApi.md#replaceVirtualHostname) | **PUT** /virtualHostnames/{virtualHostname} | Replace a Virtual Hostname object.
*AllApi* | [**updateBroker**](docs/AllApi.md#updateBroker) | **PATCH** / | Update a Broker object.
*AllApi* | [**updateCertAuthority**](docs/AllApi.md#updateCertAuthority) | **PATCH** /certAuthorities/{certAuthorityName} | Update a Certificate Authority object.
*AllApi* | [**updateClientCertAuthority**](docs/AllApi.md#updateClientCertAuthority) | **PATCH** /clientCertAuthorities/{certAuthorityName} | Update a Client Certificate Authority object.
*AllApi* | [**updateDmrCluster**](docs/AllApi.md#updateDmrCluster) | **PATCH** /dmrClusters/{dmrClusterName} | Update a Cluster object.
*AllApi* | [**updateDmrClusterCertMatchingRule**](docs/AllApi.md#updateDmrClusterCertMatchingRule) | **PATCH** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object.
*AllApi* | [**updateDmrClusterCertMatchingRuleAttributeFilter**](docs/AllApi.md#updateDmrClusterCertMatchingRuleAttributeFilter) | **PATCH** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**updateDmrClusterLink**](docs/AllApi.md#updateDmrClusterLink) | **PATCH** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Update a Link object.
*AllApi* | [**updateDomainCertAuthority**](docs/AllApi.md#updateDomainCertAuthority) | **PATCH** /domainCertAuthorities/{certAuthorityName} | Update a Domain Certificate Authority object.
*AllApi* | [**updateMsgVpn**](docs/AllApi.md#updateMsgVpn) | **PATCH** /msgVpns/{msgVpnName} | Update a Message VPN object.
*AllApi* | [**updateMsgVpnAclProfile**](docs/AllApi.md#updateMsgVpnAclProfile) | **PATCH** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Update an ACL Profile object.
*AllApi* | [**updateMsgVpnAuthenticationOauthProfile**](docs/AllApi.md#updateMsgVpnAuthenticationOauthProfile) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Update an OAuth Profile object.
*AllApi* | [**updateMsgVpnAuthenticationOauthProvider**](docs/AllApi.md#updateMsgVpnAuthenticationOauthProvider) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Update an OAuth Provider object.
*AllApi* | [**updateMsgVpnAuthorizationGroup**](docs/AllApi.md#updateMsgVpnAuthorizationGroup) | **PATCH** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Update an Authorization Group object.
*AllApi* | [**updateMsgVpnBridge**](docs/AllApi.md#updateMsgVpnBridge) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Update a Bridge object.
*AllApi* | [**updateMsgVpnBridgeRemoteMsgVpn**](docs/AllApi.md#updateMsgVpnBridgeRemoteMsgVpn) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Update a Remote Message VPN object.
*AllApi* | [**updateMsgVpnCertMatchingRule**](docs/AllApi.md#updateMsgVpnCertMatchingRule) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object.
*AllApi* | [**updateMsgVpnCertMatchingRuleAttributeFilter**](docs/AllApi.md#updateMsgVpnCertMatchingRuleAttributeFilter) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object.
*AllApi* | [**updateMsgVpnClientProfile**](docs/AllApi.md#updateMsgVpnClientProfile) | **PATCH** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Update a Client Profile object.
*AllApi* | [**updateMsgVpnClientUsername**](docs/AllApi.md#updateMsgVpnClientUsername) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Update a Client Username object.
*AllApi* | [**updateMsgVpnDistributedCache**](docs/AllApi.md#updateMsgVpnDistributedCache) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Update a Distributed Cache object.
*AllApi* | [**updateMsgVpnDistributedCacheCluster**](docs/AllApi.md#updateMsgVpnDistributedCacheCluster) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Update a Cache Cluster object.
*AllApi* | [**updateMsgVpnDistributedCacheClusterInstance**](docs/AllApi.md#updateMsgVpnDistributedCacheClusterInstance) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Update a Cache Instance object.
*AllApi* | [**updateMsgVpnDmrBridge**](docs/AllApi.md#updateMsgVpnDmrBridge) | **PATCH** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Update a DMR Bridge object.
*AllApi* | [**updateMsgVpnJndiConnectionFactory**](docs/AllApi.md#updateMsgVpnJndiConnectionFactory) | **PATCH** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Update a JNDI Connection Factory object.
*AllApi* | [**updateMsgVpnJndiQueue**](docs/AllApi.md#updateMsgVpnJndiQueue) | **PATCH** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Update a JNDI Queue object.
*AllApi* | [**updateMsgVpnJndiTopic**](docs/AllApi.md#updateMsgVpnJndiTopic) | **PATCH** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Update a JNDI Topic object.
*AllApi* | [**updateMsgVpnMqttRetainCache**](docs/AllApi.md#updateMsgVpnMqttRetainCache) | **PATCH** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Update an MQTT Retain Cache object.
*AllApi* | [**updateMsgVpnMqttSession**](docs/AllApi.md#updateMsgVpnMqttSession) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Update an MQTT Session object.
*AllApi* | [**updateMsgVpnMqttSessionSubscription**](docs/AllApi.md#updateMsgVpnMqttSessionSubscription) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Update a Subscription object.
*AllApi* | [**updateMsgVpnQueue**](docs/AllApi.md#updateMsgVpnQueue) | **PATCH** /msgVpns/{msgVpnName}/queues/{queueName} | Update a Queue object.
*AllApi* | [**updateMsgVpnQueueTemplate**](docs/AllApi.md#updateMsgVpnQueueTemplate) | **PATCH** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Update a Queue Template object.
*AllApi* | [**updateMsgVpnReplayLog**](docs/AllApi.md#updateMsgVpnReplayLog) | **PATCH** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Update a Replay Log object.
*AllApi* | [**updateMsgVpnReplicatedTopic**](docs/AllApi.md#updateMsgVpnReplicatedTopic) | **PATCH** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Update a Replicated Topic object.
*AllApi* | [**updateMsgVpnRestDeliveryPoint**](docs/AllApi.md#updateMsgVpnRestDeliveryPoint) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Update a REST Delivery Point object.
*AllApi* | [**updateMsgVpnRestDeliveryPointQueueBinding**](docs/AllApi.md#updateMsgVpnRestDeliveryPointQueueBinding) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Update a Queue Binding object.
*AllApi* | [**updateMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/AllApi.md#updateMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Update a Request Header object.
*AllApi* | [**updateMsgVpnRestDeliveryPointRestConsumer**](docs/AllApi.md#updateMsgVpnRestDeliveryPointRestConsumer) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Update a REST Consumer object.
*AllApi* | [**updateMsgVpnTopicEndpoint**](docs/AllApi.md#updateMsgVpnTopicEndpoint) | **PATCH** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Update a Topic Endpoint object.
*AllApi* | [**updateMsgVpnTopicEndpointTemplate**](docs/AllApi.md#updateMsgVpnTopicEndpointTemplate) | **PATCH** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Update a Topic Endpoint Template object.
*AllApi* | [**updateOauthProfile**](docs/AllApi.md#updateOauthProfile) | **PATCH** /oauthProfiles/{oauthProfileName} | Update an OAuth Profile object.
*AllApi* | [**updateOauthProfileAccessLevelGroup**](docs/AllApi.md#updateOauthProfileAccessLevelGroup) | **PATCH** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Update a Group Access Level object.
*AllApi* | [**updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/AllApi.md#updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **PATCH** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access-Level Exception object.
*AllApi* | [**updateOauthProfileClientAuthorizationParameter**](docs/AllApi.md#updateOauthProfileClientAuthorizationParameter) | **PATCH** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Update an Authorization Parameter object.
*AllApi* | [**updateOauthProfileDefaultMsgVpnAccessLevelException**](docs/AllApi.md#updateOauthProfileDefaultMsgVpnAccessLevelException) | **PATCH** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access-Level Exception object.
*AllApi* | [**updateVirtualHostname**](docs/AllApi.md#updateVirtualHostname) | **PATCH** /virtualHostnames/{virtualHostname} | Update a Virtual Hostname object.
*AuthenticationOauthProfileApi* | [**createMsgVpnAuthenticationOauthProfile**](docs/AuthenticationOauthProfileApi.md#createMsgVpnAuthenticationOauthProfile) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles | Create an OAuth Profile object.
*AuthenticationOauthProfileApi* | [**createMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/AuthenticationOauthProfileApi.md#createMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims | Create a Required Claim object.
*AuthenticationOauthProfileApi* | [**createMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/AuthenticationOauthProfileApi.md#createMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Create a Required Claim object.
*AuthenticationOauthProfileApi* | [**deleteMsgVpnAuthenticationOauthProfile**](docs/AuthenticationOauthProfileApi.md#deleteMsgVpnAuthenticationOauthProfile) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Delete an OAuth Profile object.
*AuthenticationOauthProfileApi* | [**deleteMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/AuthenticationOauthProfileApi.md#deleteMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Delete a Required Claim object.
*AuthenticationOauthProfileApi* | [**deleteMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/AuthenticationOauthProfileApi.md#deleteMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Delete a Required Claim object.
*AuthenticationOauthProfileApi* | [**getMsgVpnAuthenticationOauthProfile**](docs/AuthenticationOauthProfileApi.md#getMsgVpnAuthenticationOauthProfile) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Get an OAuth Profile object.
*AuthenticationOauthProfileApi* | [**getMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/AuthenticationOauthProfileApi.md#getMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Get a Required Claim object.
*AuthenticationOauthProfileApi* | [**getMsgVpnAuthenticationOauthProfileClientRequiredClaims**](docs/AuthenticationOauthProfileApi.md#getMsgVpnAuthenticationOauthProfileClientRequiredClaims) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims | Get a list of Required Claim objects.
*AuthenticationOauthProfileApi* | [**getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/AuthenticationOauthProfileApi.md#getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Get a Required Claim object.
*AuthenticationOauthProfileApi* | [**getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaims**](docs/AuthenticationOauthProfileApi.md#getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaims) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Get a list of Required Claim objects.
*AuthenticationOauthProfileApi* | [**getMsgVpnAuthenticationOauthProfiles**](docs/AuthenticationOauthProfileApi.md#getMsgVpnAuthenticationOauthProfiles) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles | Get a list of OAuth Profile objects.
*AuthenticationOauthProfileApi* | [**replaceMsgVpnAuthenticationOauthProfile**](docs/AuthenticationOauthProfileApi.md#replaceMsgVpnAuthenticationOauthProfile) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Replace an OAuth Profile object.
*AuthenticationOauthProfileApi* | [**updateMsgVpnAuthenticationOauthProfile**](docs/AuthenticationOauthProfileApi.md#updateMsgVpnAuthenticationOauthProfile) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Update an OAuth Profile object.
*AuthenticationOauthProviderApi* | [**createMsgVpnAuthenticationOauthProvider**](docs/AuthenticationOauthProviderApi.md#createMsgVpnAuthenticationOauthProvider) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProviders | Create an OAuth Provider object.
*AuthenticationOauthProviderApi* | [**deleteMsgVpnAuthenticationOauthProvider**](docs/AuthenticationOauthProviderApi.md#deleteMsgVpnAuthenticationOauthProvider) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Delete an OAuth Provider object.
*AuthenticationOauthProviderApi* | [**getMsgVpnAuthenticationOauthProvider**](docs/AuthenticationOauthProviderApi.md#getMsgVpnAuthenticationOauthProvider) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Get an OAuth Provider object.
*AuthenticationOauthProviderApi* | [**getMsgVpnAuthenticationOauthProviders**](docs/AuthenticationOauthProviderApi.md#getMsgVpnAuthenticationOauthProviders) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders | Get a list of OAuth Provider objects.
*AuthenticationOauthProviderApi* | [**replaceMsgVpnAuthenticationOauthProvider**](docs/AuthenticationOauthProviderApi.md#replaceMsgVpnAuthenticationOauthProvider) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Replace an OAuth Provider object.
*AuthenticationOauthProviderApi* | [**updateMsgVpnAuthenticationOauthProvider**](docs/AuthenticationOauthProviderApi.md#updateMsgVpnAuthenticationOauthProvider) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Update an OAuth Provider object.
*AuthorizationGroupApi* | [**createMsgVpnAuthorizationGroup**](docs/AuthorizationGroupApi.md#createMsgVpnAuthorizationGroup) | **POST** /msgVpns/{msgVpnName}/authorizationGroups | Create an Authorization Group object.
*AuthorizationGroupApi* | [**deleteMsgVpnAuthorizationGroup**](docs/AuthorizationGroupApi.md#deleteMsgVpnAuthorizationGroup) | **DELETE** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Delete an Authorization Group object.
*AuthorizationGroupApi* | [**getMsgVpnAuthorizationGroup**](docs/AuthorizationGroupApi.md#getMsgVpnAuthorizationGroup) | **GET** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Get an Authorization Group object.
*AuthorizationGroupApi* | [**getMsgVpnAuthorizationGroups**](docs/AuthorizationGroupApi.md#getMsgVpnAuthorizationGroups) | **GET** /msgVpns/{msgVpnName}/authorizationGroups | Get a list of Authorization Group objects.
*AuthorizationGroupApi* | [**replaceMsgVpnAuthorizationGroup**](docs/AuthorizationGroupApi.md#replaceMsgVpnAuthorizationGroup) | **PUT** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Replace an Authorization Group object.
*AuthorizationGroupApi* | [**updateMsgVpnAuthorizationGroup**](docs/AuthorizationGroupApi.md#updateMsgVpnAuthorizationGroup) | **PATCH** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Update an Authorization Group object.
*BridgeApi* | [**createMsgVpnBridge**](docs/BridgeApi.md#createMsgVpnBridge) | **POST** /msgVpns/{msgVpnName}/bridges | Create a Bridge object.
*BridgeApi* | [**createMsgVpnBridgeRemoteMsgVpn**](docs/BridgeApi.md#createMsgVpnBridgeRemoteMsgVpn) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Create a Remote Message VPN object.
*BridgeApi* | [**createMsgVpnBridgeRemoteSubscription**](docs/BridgeApi.md#createMsgVpnBridgeRemoteSubscription) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Create a Remote Subscription object.
*BridgeApi* | [**createMsgVpnBridgeTlsTrustedCommonName**](docs/BridgeApi.md#createMsgVpnBridgeTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*BridgeApi* | [**deleteMsgVpnBridge**](docs/BridgeApi.md#deleteMsgVpnBridge) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Delete a Bridge object.
*BridgeApi* | [**deleteMsgVpnBridgeRemoteMsgVpn**](docs/BridgeApi.md#deleteMsgVpnBridgeRemoteMsgVpn) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Delete a Remote Message VPN object.
*BridgeApi* | [**deleteMsgVpnBridgeRemoteSubscription**](docs/BridgeApi.md#deleteMsgVpnBridgeRemoteSubscription) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Delete a Remote Subscription object.
*BridgeApi* | [**deleteMsgVpnBridgeTlsTrustedCommonName**](docs/BridgeApi.md#deleteMsgVpnBridgeTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*BridgeApi* | [**getMsgVpnBridge**](docs/BridgeApi.md#getMsgVpnBridge) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Get a Bridge object.
*BridgeApi* | [**getMsgVpnBridgeRemoteMsgVpn**](docs/BridgeApi.md#getMsgVpnBridgeRemoteMsgVpn) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Get a Remote Message VPN object.
*BridgeApi* | [**getMsgVpnBridgeRemoteMsgVpns**](docs/BridgeApi.md#getMsgVpnBridgeRemoteMsgVpns) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Get a list of Remote Message VPN objects.
*BridgeApi* | [**getMsgVpnBridgeRemoteSubscription**](docs/BridgeApi.md#getMsgVpnBridgeRemoteSubscription) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Get a Remote Subscription object.
*BridgeApi* | [**getMsgVpnBridgeRemoteSubscriptions**](docs/BridgeApi.md#getMsgVpnBridgeRemoteSubscriptions) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Get a list of Remote Subscription objects.
*BridgeApi* | [**getMsgVpnBridgeTlsTrustedCommonName**](docs/BridgeApi.md#getMsgVpnBridgeTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*BridgeApi* | [**getMsgVpnBridgeTlsTrustedCommonNames**](docs/BridgeApi.md#getMsgVpnBridgeTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*BridgeApi* | [**getMsgVpnBridges**](docs/BridgeApi.md#getMsgVpnBridges) | **GET** /msgVpns/{msgVpnName}/bridges | Get a list of Bridge objects.
*BridgeApi* | [**replaceMsgVpnBridge**](docs/BridgeApi.md#replaceMsgVpnBridge) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Replace a Bridge object.
*BridgeApi* | [**replaceMsgVpnBridgeRemoteMsgVpn**](docs/BridgeApi.md#replaceMsgVpnBridgeRemoteMsgVpn) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Replace a Remote Message VPN object.
*BridgeApi* | [**updateMsgVpnBridge**](docs/BridgeApi.md#updateMsgVpnBridge) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Update a Bridge object.
*BridgeApi* | [**updateMsgVpnBridgeRemoteMsgVpn**](docs/BridgeApi.md#updateMsgVpnBridgeRemoteMsgVpn) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Update a Remote Message VPN object.
*CertAuthorityApi* | [**createCertAuthority**](docs/CertAuthorityApi.md#createCertAuthority) | **POST** /certAuthorities | Create a Certificate Authority object.
*CertAuthorityApi* | [**createCertAuthorityOcspTlsTrustedCommonName**](docs/CertAuthorityApi.md#createCertAuthorityOcspTlsTrustedCommonName) | **POST** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Create an OCSP Responder Trusted Common Name object.
*CertAuthorityApi* | [**deleteCertAuthority**](docs/CertAuthorityApi.md#deleteCertAuthority) | **DELETE** /certAuthorities/{certAuthorityName} | Delete a Certificate Authority object.
*CertAuthorityApi* | [**deleteCertAuthorityOcspTlsTrustedCommonName**](docs/CertAuthorityApi.md#deleteCertAuthorityOcspTlsTrustedCommonName) | **DELETE** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Delete an OCSP Responder Trusted Common Name object.
*CertAuthorityApi* | [**getCertAuthorities**](docs/CertAuthorityApi.md#getCertAuthorities) | **GET** /certAuthorities | Get a list of Certificate Authority objects.
*CertAuthorityApi* | [**getCertAuthority**](docs/CertAuthorityApi.md#getCertAuthority) | **GET** /certAuthorities/{certAuthorityName} | Get a Certificate Authority object.
*CertAuthorityApi* | [**getCertAuthorityOcspTlsTrustedCommonName**](docs/CertAuthorityApi.md#getCertAuthorityOcspTlsTrustedCommonName) | **GET** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Get an OCSP Responder Trusted Common Name object.
*CertAuthorityApi* | [**getCertAuthorityOcspTlsTrustedCommonNames**](docs/CertAuthorityApi.md#getCertAuthorityOcspTlsTrustedCommonNames) | **GET** /certAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Get a list of OCSP Responder Trusted Common Name objects.
*CertAuthorityApi* | [**replaceCertAuthority**](docs/CertAuthorityApi.md#replaceCertAuthority) | **PUT** /certAuthorities/{certAuthorityName} | Replace a Certificate Authority object.
*CertAuthorityApi* | [**updateCertAuthority**](docs/CertAuthorityApi.md#updateCertAuthority) | **PATCH** /certAuthorities/{certAuthorityName} | Update a Certificate Authority object.
*CertMatchingRuleApi* | [**createMsgVpnCertMatchingRule**](docs/CertMatchingRuleApi.md#createMsgVpnCertMatchingRule) | **POST** /msgVpns/{msgVpnName}/certMatchingRules | Create a Certificate Matching Rule object.
*CertMatchingRuleApi* | [**createMsgVpnCertMatchingRuleAttributeFilter**](docs/CertMatchingRuleApi.md#createMsgVpnCertMatchingRuleAttributeFilter) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object.
*CertMatchingRuleApi* | [**createMsgVpnCertMatchingRuleCondition**](docs/CertMatchingRuleApi.md#createMsgVpnCertMatchingRuleCondition) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object.
*CertMatchingRuleApi* | [**deleteMsgVpnCertMatchingRule**](docs/CertMatchingRuleApi.md#deleteMsgVpnCertMatchingRule) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object.
*CertMatchingRuleApi* | [**deleteMsgVpnCertMatchingRuleAttributeFilter**](docs/CertMatchingRuleApi.md#deleteMsgVpnCertMatchingRuleAttributeFilter) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object.
*CertMatchingRuleApi* | [**deleteMsgVpnCertMatchingRuleCondition**](docs/CertMatchingRuleApi.md#deleteMsgVpnCertMatchingRuleCondition) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object.
*CertMatchingRuleApi* | [**getMsgVpnCertMatchingRule**](docs/CertMatchingRuleApi.md#getMsgVpnCertMatchingRule) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object.
*CertMatchingRuleApi* | [**getMsgVpnCertMatchingRuleAttributeFilter**](docs/CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleAttributeFilter) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object.
*CertMatchingRuleApi* | [**getMsgVpnCertMatchingRuleAttributeFilters**](docs/CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleAttributeFilters) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects.
*CertMatchingRuleApi* | [**getMsgVpnCertMatchingRuleCondition**](docs/CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleCondition) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object.
*CertMatchingRuleApi* | [**getMsgVpnCertMatchingRuleConditions**](docs/CertMatchingRuleApi.md#getMsgVpnCertMatchingRuleConditions) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects.
*CertMatchingRuleApi* | [**getMsgVpnCertMatchingRules**](docs/CertMatchingRuleApi.md#getMsgVpnCertMatchingRules) | **GET** /msgVpns/{msgVpnName}/certMatchingRules | Get a list of Certificate Matching Rule objects.
*CertMatchingRuleApi* | [**replaceMsgVpnCertMatchingRule**](docs/CertMatchingRuleApi.md#replaceMsgVpnCertMatchingRule) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object.
*CertMatchingRuleApi* | [**replaceMsgVpnCertMatchingRuleAttributeFilter**](docs/CertMatchingRuleApi.md#replaceMsgVpnCertMatchingRuleAttributeFilter) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object.
*CertMatchingRuleApi* | [**updateMsgVpnCertMatchingRule**](docs/CertMatchingRuleApi.md#updateMsgVpnCertMatchingRule) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object.
*CertMatchingRuleApi* | [**updateMsgVpnCertMatchingRuleAttributeFilter**](docs/CertMatchingRuleApi.md#updateMsgVpnCertMatchingRuleAttributeFilter) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object.
*ClientCertAuthorityApi* | [**createClientCertAuthority**](docs/ClientCertAuthorityApi.md#createClientCertAuthority) | **POST** /clientCertAuthorities | Create a Client Certificate Authority object.
*ClientCertAuthorityApi* | [**createClientCertAuthorityOcspTlsTrustedCommonName**](docs/ClientCertAuthorityApi.md#createClientCertAuthorityOcspTlsTrustedCommonName) | **POST** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Create an OCSP Responder Trusted Common Name object.
*ClientCertAuthorityApi* | [**deleteClientCertAuthority**](docs/ClientCertAuthorityApi.md#deleteClientCertAuthority) | **DELETE** /clientCertAuthorities/{certAuthorityName} | Delete a Client Certificate Authority object.
*ClientCertAuthorityApi* | [**deleteClientCertAuthorityOcspTlsTrustedCommonName**](docs/ClientCertAuthorityApi.md#deleteClientCertAuthorityOcspTlsTrustedCommonName) | **DELETE** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Delete an OCSP Responder Trusted Common Name object.
*ClientCertAuthorityApi* | [**getClientCertAuthorities**](docs/ClientCertAuthorityApi.md#getClientCertAuthorities) | **GET** /clientCertAuthorities | Get a list of Client Certificate Authority objects.
*ClientCertAuthorityApi* | [**getClientCertAuthority**](docs/ClientCertAuthorityApi.md#getClientCertAuthority) | **GET** /clientCertAuthorities/{certAuthorityName} | Get a Client Certificate Authority object.
*ClientCertAuthorityApi* | [**getClientCertAuthorityOcspTlsTrustedCommonName**](docs/ClientCertAuthorityApi.md#getClientCertAuthorityOcspTlsTrustedCommonName) | **GET** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames/{ocspTlsTrustedCommonName} | Get an OCSP Responder Trusted Common Name object.
*ClientCertAuthorityApi* | [**getClientCertAuthorityOcspTlsTrustedCommonNames**](docs/ClientCertAuthorityApi.md#getClientCertAuthorityOcspTlsTrustedCommonNames) | **GET** /clientCertAuthorities/{certAuthorityName}/ocspTlsTrustedCommonNames | Get a list of OCSP Responder Trusted Common Name objects.
*ClientCertAuthorityApi* | [**replaceClientCertAuthority**](docs/ClientCertAuthorityApi.md#replaceClientCertAuthority) | **PUT** /clientCertAuthorities/{certAuthorityName} | Replace a Client Certificate Authority object.
*ClientCertAuthorityApi* | [**updateClientCertAuthority**](docs/ClientCertAuthorityApi.md#updateClientCertAuthority) | **PATCH** /clientCertAuthorities/{certAuthorityName} | Update a Client Certificate Authority object.
*ClientProfileApi* | [**createMsgVpnClientProfile**](docs/ClientProfileApi.md#createMsgVpnClientProfile) | **POST** /msgVpns/{msgVpnName}/clientProfiles | Create a Client Profile object.
*ClientProfileApi* | [**deleteMsgVpnClientProfile**](docs/ClientProfileApi.md#deleteMsgVpnClientProfile) | **DELETE** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Delete a Client Profile object.
*ClientProfileApi* | [**getMsgVpnClientProfile**](docs/ClientProfileApi.md#getMsgVpnClientProfile) | **GET** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Get a Client Profile object.
*ClientProfileApi* | [**getMsgVpnClientProfiles**](docs/ClientProfileApi.md#getMsgVpnClientProfiles) | **GET** /msgVpns/{msgVpnName}/clientProfiles | Get a list of Client Profile objects.
*ClientProfileApi* | [**replaceMsgVpnClientProfile**](docs/ClientProfileApi.md#replaceMsgVpnClientProfile) | **PUT** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Replace a Client Profile object.
*ClientProfileApi* | [**updateMsgVpnClientProfile**](docs/ClientProfileApi.md#updateMsgVpnClientProfile) | **PATCH** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Update a Client Profile object.
*ClientUsernameApi* | [**createMsgVpnClientUsername**](docs/ClientUsernameApi.md#createMsgVpnClientUsername) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Create a Client Username object.
*ClientUsernameApi* | [**createMsgVpnClientUsernameAttribute**](docs/ClientUsernameApi.md#createMsgVpnClientUsernameAttribute) | **POST** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Create a Client Username Attribute object.
*ClientUsernameApi* | [**deleteMsgVpnClientUsername**](docs/ClientUsernameApi.md#deleteMsgVpnClientUsername) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Delete a Client Username object.
*ClientUsernameApi* | [**deleteMsgVpnClientUsernameAttribute**](docs/ClientUsernameApi.md#deleteMsgVpnClientUsernameAttribute) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Delete a Client Username Attribute object.
*ClientUsernameApi* | [**getMsgVpnClientUsername**](docs/ClientUsernameApi.md#getMsgVpnClientUsername) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Get a Client Username object.
*ClientUsernameApi* | [**getMsgVpnClientUsernameAttribute**](docs/ClientUsernameApi.md#getMsgVpnClientUsernameAttribute) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Get a Client Username Attribute object.
*ClientUsernameApi* | [**getMsgVpnClientUsernameAttributes**](docs/ClientUsernameApi.md#getMsgVpnClientUsernameAttributes) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Get a list of Client Username Attribute objects.
*ClientUsernameApi* | [**getMsgVpnClientUsernames**](docs/ClientUsernameApi.md#getMsgVpnClientUsernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Get a list of Client Username objects.
*ClientUsernameApi* | [**replaceMsgVpnClientUsername**](docs/ClientUsernameApi.md#replaceMsgVpnClientUsername) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replace a Client Username object.
*ClientUsernameApi* | [**updateMsgVpnClientUsername**](docs/ClientUsernameApi.md#updateMsgVpnClientUsername) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Update a Client Username object.
*DistributedCacheApi* | [**createMsgVpnDistributedCache**](docs/DistributedCacheApi.md#createMsgVpnDistributedCache) | **POST** /msgVpns/{msgVpnName}/distributedCaches | Create a Distributed Cache object.
*DistributedCacheApi* | [**createMsgVpnDistributedCacheCluster**](docs/DistributedCacheApi.md#createMsgVpnDistributedCacheCluster) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters | Create a Cache Cluster object.
*DistributedCacheApi* | [**createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/DistributedCacheApi.md#createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters | Create a Home Cache Cluster object.
*DistributedCacheApi* | [**createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/DistributedCacheApi.md#createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes | Create a Topic Prefix object.
*DistributedCacheApi* | [**createMsgVpnDistributedCacheClusterInstance**](docs/DistributedCacheApi.md#createMsgVpnDistributedCacheClusterInstance) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances | Create a Cache Instance object.
*DistributedCacheApi* | [**createMsgVpnDistributedCacheClusterTopic**](docs/DistributedCacheApi.md#createMsgVpnDistributedCacheClusterTopic) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics | Create a Topic object.
*DistributedCacheApi* | [**deleteMsgVpnDistributedCache**](docs/DistributedCacheApi.md#deleteMsgVpnDistributedCache) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Delete a Distributed Cache object.
*DistributedCacheApi* | [**deleteMsgVpnDistributedCacheCluster**](docs/DistributedCacheApi.md#deleteMsgVpnDistributedCacheCluster) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Delete a Cache Cluster object.
*DistributedCacheApi* | [**deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/DistributedCacheApi.md#deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName} | Delete a Home Cache Cluster object.
*DistributedCacheApi* | [**deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/DistributedCacheApi.md#deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix} | Delete a Topic Prefix object.
*DistributedCacheApi* | [**deleteMsgVpnDistributedCacheClusterInstance**](docs/DistributedCacheApi.md#deleteMsgVpnDistributedCacheClusterInstance) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Delete a Cache Instance object.
*DistributedCacheApi* | [**deleteMsgVpnDistributedCacheClusterTopic**](docs/DistributedCacheApi.md#deleteMsgVpnDistributedCacheClusterTopic) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic} | Delete a Topic object.
*DistributedCacheApi* | [**getMsgVpnDistributedCache**](docs/DistributedCacheApi.md#getMsgVpnDistributedCache) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Get a Distributed Cache object.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheCluster**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheCluster) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Get a Cache Cluster object.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName} | Get a Home Cache Cluster object.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix} | Get a Topic Prefix object.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes | Get a list of Topic Prefix objects.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters | Get a list of Home Cache Cluster objects.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterInstance**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterInstance) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Get a Cache Instance object.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterInstances**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterInstances) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances | Get a list of Cache Instance objects.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterTopic**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterTopic) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic} | Get a Topic object.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusterTopics**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusterTopics) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics | Get a list of Topic objects.
*DistributedCacheApi* | [**getMsgVpnDistributedCacheClusters**](docs/DistributedCacheApi.md#getMsgVpnDistributedCacheClusters) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters | Get a list of Cache Cluster objects.
*DistributedCacheApi* | [**getMsgVpnDistributedCaches**](docs/DistributedCacheApi.md#getMsgVpnDistributedCaches) | **GET** /msgVpns/{msgVpnName}/distributedCaches | Get a list of Distributed Cache objects.
*DistributedCacheApi* | [**replaceMsgVpnDistributedCache**](docs/DistributedCacheApi.md#replaceMsgVpnDistributedCache) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Replace a Distributed Cache object.
*DistributedCacheApi* | [**replaceMsgVpnDistributedCacheCluster**](docs/DistributedCacheApi.md#replaceMsgVpnDistributedCacheCluster) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Replace a Cache Cluster object.
*DistributedCacheApi* | [**replaceMsgVpnDistributedCacheClusterInstance**](docs/DistributedCacheApi.md#replaceMsgVpnDistributedCacheClusterInstance) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Replace a Cache Instance object.
*DistributedCacheApi* | [**updateMsgVpnDistributedCache**](docs/DistributedCacheApi.md#updateMsgVpnDistributedCache) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Update a Distributed Cache object.
*DistributedCacheApi* | [**updateMsgVpnDistributedCacheCluster**](docs/DistributedCacheApi.md#updateMsgVpnDistributedCacheCluster) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Update a Cache Cluster object.
*DistributedCacheApi* | [**updateMsgVpnDistributedCacheClusterInstance**](docs/DistributedCacheApi.md#updateMsgVpnDistributedCacheClusterInstance) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Update a Cache Instance object.
*DmrBridgeApi* | [**createMsgVpnDmrBridge**](docs/DmrBridgeApi.md#createMsgVpnDmrBridge) | **POST** /msgVpns/{msgVpnName}/dmrBridges | Create a DMR Bridge object.
*DmrBridgeApi* | [**deleteMsgVpnDmrBridge**](docs/DmrBridgeApi.md#deleteMsgVpnDmrBridge) | **DELETE** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Delete a DMR Bridge object.
*DmrBridgeApi* | [**getMsgVpnDmrBridge**](docs/DmrBridgeApi.md#getMsgVpnDmrBridge) | **GET** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Get a DMR Bridge object.
*DmrBridgeApi* | [**getMsgVpnDmrBridges**](docs/DmrBridgeApi.md#getMsgVpnDmrBridges) | **GET** /msgVpns/{msgVpnName}/dmrBridges | Get a list of DMR Bridge objects.
*DmrBridgeApi* | [**replaceMsgVpnDmrBridge**](docs/DmrBridgeApi.md#replaceMsgVpnDmrBridge) | **PUT** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Replace a DMR Bridge object.
*DmrBridgeApi* | [**updateMsgVpnDmrBridge**](docs/DmrBridgeApi.md#updateMsgVpnDmrBridge) | **PATCH** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Update a DMR Bridge object.
*DmrClusterApi* | [**createDmrCluster**](docs/DmrClusterApi.md#createDmrCluster) | **POST** /dmrClusters | Create a Cluster object.
*DmrClusterApi* | [**createDmrClusterCertMatchingRule**](docs/DmrClusterApi.md#createDmrClusterCertMatchingRule) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules | Create a Certificate Matching Rule object.
*DmrClusterApi* | [**createDmrClusterCertMatchingRuleAttributeFilter**](docs/DmrClusterApi.md#createDmrClusterCertMatchingRuleAttributeFilter) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object.
*DmrClusterApi* | [**createDmrClusterCertMatchingRuleCondition**](docs/DmrClusterApi.md#createDmrClusterCertMatchingRuleCondition) | **POST** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object.
*DmrClusterApi* | [**createDmrClusterLink**](docs/DmrClusterApi.md#createDmrClusterLink) | **POST** /dmrClusters/{dmrClusterName}/links | Create a Link object.
*DmrClusterApi* | [**createDmrClusterLinkAttribute**](docs/DmrClusterApi.md#createDmrClusterLinkAttribute) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes | Create a Link Attribute object.
*DmrClusterApi* | [**createDmrClusterLinkRemoteAddress**](docs/DmrClusterApi.md#createDmrClusterLinkRemoteAddress) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Create a Remote Address object.
*DmrClusterApi* | [**createDmrClusterLinkTlsTrustedCommonName**](docs/DmrClusterApi.md#createDmrClusterLinkTlsTrustedCommonName) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*DmrClusterApi* | [**deleteDmrCluster**](docs/DmrClusterApi.md#deleteDmrCluster) | **DELETE** /dmrClusters/{dmrClusterName} | Delete a Cluster object.
*DmrClusterApi* | [**deleteDmrClusterCertMatchingRule**](docs/DmrClusterApi.md#deleteDmrClusterCertMatchingRule) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object.
*DmrClusterApi* | [**deleteDmrClusterCertMatchingRuleAttributeFilter**](docs/DmrClusterApi.md#deleteDmrClusterCertMatchingRuleAttributeFilter) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object.
*DmrClusterApi* | [**deleteDmrClusterCertMatchingRuleCondition**](docs/DmrClusterApi.md#deleteDmrClusterCertMatchingRuleCondition) | **DELETE** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object.
*DmrClusterApi* | [**deleteDmrClusterLink**](docs/DmrClusterApi.md#deleteDmrClusterLink) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Delete a Link object.
*DmrClusterApi* | [**deleteDmrClusterLinkAttribute**](docs/DmrClusterApi.md#deleteDmrClusterLinkAttribute) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes/{attributeName},{attributeValue} | Delete a Link Attribute object.
*DmrClusterApi* | [**deleteDmrClusterLinkRemoteAddress**](docs/DmrClusterApi.md#deleteDmrClusterLinkRemoteAddress) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Delete a Remote Address object.
*DmrClusterApi* | [**deleteDmrClusterLinkTlsTrustedCommonName**](docs/DmrClusterApi.md#deleteDmrClusterLinkTlsTrustedCommonName) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*DmrClusterApi* | [**getDmrCluster**](docs/DmrClusterApi.md#getDmrCluster) | **GET** /dmrClusters/{dmrClusterName} | Get a Cluster object.
*DmrClusterApi* | [**getDmrClusterCertMatchingRule**](docs/DmrClusterApi.md#getDmrClusterCertMatchingRule) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object.
*DmrClusterApi* | [**getDmrClusterCertMatchingRuleAttributeFilter**](docs/DmrClusterApi.md#getDmrClusterCertMatchingRuleAttributeFilter) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object.
*DmrClusterApi* | [**getDmrClusterCertMatchingRuleAttributeFilters**](docs/DmrClusterApi.md#getDmrClusterCertMatchingRuleAttributeFilters) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects.
*DmrClusterApi* | [**getDmrClusterCertMatchingRuleCondition**](docs/DmrClusterApi.md#getDmrClusterCertMatchingRuleCondition) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object.
*DmrClusterApi* | [**getDmrClusterCertMatchingRuleConditions**](docs/DmrClusterApi.md#getDmrClusterCertMatchingRuleConditions) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects.
*DmrClusterApi* | [**getDmrClusterCertMatchingRules**](docs/DmrClusterApi.md#getDmrClusterCertMatchingRules) | **GET** /dmrClusters/{dmrClusterName}/certMatchingRules | Get a list of Certificate Matching Rule objects.
*DmrClusterApi* | [**getDmrClusterLink**](docs/DmrClusterApi.md#getDmrClusterLink) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Get a Link object.
*DmrClusterApi* | [**getDmrClusterLinkAttribute**](docs/DmrClusterApi.md#getDmrClusterLinkAttribute) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes/{attributeName},{attributeValue} | Get a Link Attribute object.
*DmrClusterApi* | [**getDmrClusterLinkAttributes**](docs/DmrClusterApi.md#getDmrClusterLinkAttributes) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/attributes | Get a list of Link Attribute objects.
*DmrClusterApi* | [**getDmrClusterLinkRemoteAddress**](docs/DmrClusterApi.md#getDmrClusterLinkRemoteAddress) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Get a Remote Address object.
*DmrClusterApi* | [**getDmrClusterLinkRemoteAddresses**](docs/DmrClusterApi.md#getDmrClusterLinkRemoteAddresses) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Get a list of Remote Address objects.
*DmrClusterApi* | [**getDmrClusterLinkTlsTrustedCommonName**](docs/DmrClusterApi.md#getDmrClusterLinkTlsTrustedCommonName) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*DmrClusterApi* | [**getDmrClusterLinkTlsTrustedCommonNames**](docs/DmrClusterApi.md#getDmrClusterLinkTlsTrustedCommonNames) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*DmrClusterApi* | [**getDmrClusterLinks**](docs/DmrClusterApi.md#getDmrClusterLinks) | **GET** /dmrClusters/{dmrClusterName}/links | Get a list of Link objects.
*DmrClusterApi* | [**getDmrClusters**](docs/DmrClusterApi.md#getDmrClusters) | **GET** /dmrClusters | Get a list of Cluster objects.
*DmrClusterApi* | [**replaceDmrCluster**](docs/DmrClusterApi.md#replaceDmrCluster) | **PUT** /dmrClusters/{dmrClusterName} | Replace a Cluster object.
*DmrClusterApi* | [**replaceDmrClusterCertMatchingRule**](docs/DmrClusterApi.md#replaceDmrClusterCertMatchingRule) | **PUT** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object.
*DmrClusterApi* | [**replaceDmrClusterCertMatchingRuleAttributeFilter**](docs/DmrClusterApi.md#replaceDmrClusterCertMatchingRuleAttributeFilter) | **PUT** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object.
*DmrClusterApi* | [**replaceDmrClusterLink**](docs/DmrClusterApi.md#replaceDmrClusterLink) | **PUT** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Replace a Link object.
*DmrClusterApi* | [**updateDmrCluster**](docs/DmrClusterApi.md#updateDmrCluster) | **PATCH** /dmrClusters/{dmrClusterName} | Update a Cluster object.
*DmrClusterApi* | [**updateDmrClusterCertMatchingRule**](docs/DmrClusterApi.md#updateDmrClusterCertMatchingRule) | **PATCH** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object.
*DmrClusterApi* | [**updateDmrClusterCertMatchingRuleAttributeFilter**](docs/DmrClusterApi.md#updateDmrClusterCertMatchingRuleAttributeFilter) | **PATCH** /dmrClusters/{dmrClusterName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object.
*DmrClusterApi* | [**updateDmrClusterLink**](docs/DmrClusterApi.md#updateDmrClusterLink) | **PATCH** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Update a Link object.
*DomainCertAuthorityApi* | [**createDomainCertAuthority**](docs/DomainCertAuthorityApi.md#createDomainCertAuthority) | **POST** /domainCertAuthorities | Create a Domain Certificate Authority object.
*DomainCertAuthorityApi* | [**deleteDomainCertAuthority**](docs/DomainCertAuthorityApi.md#deleteDomainCertAuthority) | **DELETE** /domainCertAuthorities/{certAuthorityName} | Delete a Domain Certificate Authority object.
*DomainCertAuthorityApi* | [**getDomainCertAuthorities**](docs/DomainCertAuthorityApi.md#getDomainCertAuthorities) | **GET** /domainCertAuthorities | Get a list of Domain Certificate Authority objects.
*DomainCertAuthorityApi* | [**getDomainCertAuthority**](docs/DomainCertAuthorityApi.md#getDomainCertAuthority) | **GET** /domainCertAuthorities/{certAuthorityName} | Get a Domain Certificate Authority object.
*DomainCertAuthorityApi* | [**replaceDomainCertAuthority**](docs/DomainCertAuthorityApi.md#replaceDomainCertAuthority) | **PUT** /domainCertAuthorities/{certAuthorityName} | Replace a Domain Certificate Authority object.
*DomainCertAuthorityApi* | [**updateDomainCertAuthority**](docs/DomainCertAuthorityApi.md#updateDomainCertAuthority) | **PATCH** /domainCertAuthorities/{certAuthorityName} | Update a Domain Certificate Authority object.
*JndiApi* | [**createMsgVpnJndiConnectionFactory**](docs/JndiApi.md#createMsgVpnJndiConnectionFactory) | **POST** /msgVpns/{msgVpnName}/jndiConnectionFactories | Create a JNDI Connection Factory object.
*JndiApi* | [**createMsgVpnJndiQueue**](docs/JndiApi.md#createMsgVpnJndiQueue) | **POST** /msgVpns/{msgVpnName}/jndiQueues | Create a JNDI Queue object.
*JndiApi* | [**createMsgVpnJndiTopic**](docs/JndiApi.md#createMsgVpnJndiTopic) | **POST** /msgVpns/{msgVpnName}/jndiTopics | Create a JNDI Topic object.
*JndiApi* | [**deleteMsgVpnJndiConnectionFactory**](docs/JndiApi.md#deleteMsgVpnJndiConnectionFactory) | **DELETE** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Delete a JNDI Connection Factory object.
*JndiApi* | [**deleteMsgVpnJndiQueue**](docs/JndiApi.md#deleteMsgVpnJndiQueue) | **DELETE** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Delete a JNDI Queue object.
*JndiApi* | [**deleteMsgVpnJndiTopic**](docs/JndiApi.md#deleteMsgVpnJndiTopic) | **DELETE** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Delete a JNDI Topic object.
*JndiApi* | [**getMsgVpnJndiConnectionFactories**](docs/JndiApi.md#getMsgVpnJndiConnectionFactories) | **GET** /msgVpns/{msgVpnName}/jndiConnectionFactories | Get a list of JNDI Connection Factory objects.
*JndiApi* | [**getMsgVpnJndiConnectionFactory**](docs/JndiApi.md#getMsgVpnJndiConnectionFactory) | **GET** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Get a JNDI Connection Factory object.
*JndiApi* | [**getMsgVpnJndiQueue**](docs/JndiApi.md#getMsgVpnJndiQueue) | **GET** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Get a JNDI Queue object.
*JndiApi* | [**getMsgVpnJndiQueues**](docs/JndiApi.md#getMsgVpnJndiQueues) | **GET** /msgVpns/{msgVpnName}/jndiQueues | Get a list of JNDI Queue objects.
*JndiApi* | [**getMsgVpnJndiTopic**](docs/JndiApi.md#getMsgVpnJndiTopic) | **GET** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Get a JNDI Topic object.
*JndiApi* | [**getMsgVpnJndiTopics**](docs/JndiApi.md#getMsgVpnJndiTopics) | **GET** /msgVpns/{msgVpnName}/jndiTopics | Get a list of JNDI Topic objects.
*JndiApi* | [**replaceMsgVpnJndiConnectionFactory**](docs/JndiApi.md#replaceMsgVpnJndiConnectionFactory) | **PUT** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Replace a JNDI Connection Factory object.
*JndiApi* | [**replaceMsgVpnJndiQueue**](docs/JndiApi.md#replaceMsgVpnJndiQueue) | **PUT** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Replace a JNDI Queue object.
*JndiApi* | [**replaceMsgVpnJndiTopic**](docs/JndiApi.md#replaceMsgVpnJndiTopic) | **PUT** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Replace a JNDI Topic object.
*JndiApi* | [**updateMsgVpnJndiConnectionFactory**](docs/JndiApi.md#updateMsgVpnJndiConnectionFactory) | **PATCH** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Update a JNDI Connection Factory object.
*JndiApi* | [**updateMsgVpnJndiQueue**](docs/JndiApi.md#updateMsgVpnJndiQueue) | **PATCH** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Update a JNDI Queue object.
*JndiApi* | [**updateMsgVpnJndiTopic**](docs/JndiApi.md#updateMsgVpnJndiTopic) | **PATCH** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Update a JNDI Topic object.
*MqttRetainCacheApi* | [**createMsgVpnMqttRetainCache**](docs/MqttRetainCacheApi.md#createMsgVpnMqttRetainCache) | **POST** /msgVpns/{msgVpnName}/mqttRetainCaches | Create an MQTT Retain Cache object.
*MqttRetainCacheApi* | [**deleteMsgVpnMqttRetainCache**](docs/MqttRetainCacheApi.md#deleteMsgVpnMqttRetainCache) | **DELETE** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Delete an MQTT Retain Cache object.
*MqttRetainCacheApi* | [**getMsgVpnMqttRetainCache**](docs/MqttRetainCacheApi.md#getMsgVpnMqttRetainCache) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Get an MQTT Retain Cache object.
*MqttRetainCacheApi* | [**getMsgVpnMqttRetainCaches**](docs/MqttRetainCacheApi.md#getMsgVpnMqttRetainCaches) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches | Get a list of MQTT Retain Cache objects.
*MqttRetainCacheApi* | [**replaceMsgVpnMqttRetainCache**](docs/MqttRetainCacheApi.md#replaceMsgVpnMqttRetainCache) | **PUT** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Replace an MQTT Retain Cache object.
*MqttRetainCacheApi* | [**updateMsgVpnMqttRetainCache**](docs/MqttRetainCacheApi.md#updateMsgVpnMqttRetainCache) | **PATCH** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Update an MQTT Retain Cache object.
*MqttSessionApi* | [**createMsgVpnMqttSession**](docs/MqttSessionApi.md#createMsgVpnMqttSession) | **POST** /msgVpns/{msgVpnName}/mqttSessions | Create an MQTT Session object.
*MqttSessionApi* | [**createMsgVpnMqttSessionSubscription**](docs/MqttSessionApi.md#createMsgVpnMqttSessionSubscription) | **POST** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Create a Subscription object.
*MqttSessionApi* | [**deleteMsgVpnMqttSession**](docs/MqttSessionApi.md#deleteMsgVpnMqttSession) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Delete an MQTT Session object.
*MqttSessionApi* | [**deleteMsgVpnMqttSessionSubscription**](docs/MqttSessionApi.md#deleteMsgVpnMqttSessionSubscription) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Delete a Subscription object.
*MqttSessionApi* | [**getMsgVpnMqttSession**](docs/MqttSessionApi.md#getMsgVpnMqttSession) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Get an MQTT Session object.
*MqttSessionApi* | [**getMsgVpnMqttSessionSubscription**](docs/MqttSessionApi.md#getMsgVpnMqttSessionSubscription) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Get a Subscription object.
*MqttSessionApi* | [**getMsgVpnMqttSessionSubscriptions**](docs/MqttSessionApi.md#getMsgVpnMqttSessionSubscriptions) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Get a list of Subscription objects.
*MqttSessionApi* | [**getMsgVpnMqttSessions**](docs/MqttSessionApi.md#getMsgVpnMqttSessions) | **GET** /msgVpns/{msgVpnName}/mqttSessions | Get a list of MQTT Session objects.
*MqttSessionApi* | [**replaceMsgVpnMqttSession**](docs/MqttSessionApi.md#replaceMsgVpnMqttSession) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Replace an MQTT Session object.
*MqttSessionApi* | [**replaceMsgVpnMqttSessionSubscription**](docs/MqttSessionApi.md#replaceMsgVpnMqttSessionSubscription) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Replace a Subscription object.
*MqttSessionApi* | [**updateMsgVpnMqttSession**](docs/MqttSessionApi.md#updateMsgVpnMqttSession) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Update an MQTT Session object.
*MqttSessionApi* | [**updateMsgVpnMqttSessionSubscription**](docs/MqttSessionApi.md#updateMsgVpnMqttSessionSubscription) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Update a Subscription object.
*MsgVpnApi* | [**createMsgVpn**](docs/MsgVpnApi.md#createMsgVpn) | **POST** /msgVpns | Create a Message VPN object.
*MsgVpnApi* | [**createMsgVpnAclProfile**](docs/MsgVpnApi.md#createMsgVpnAclProfile) | **POST** /msgVpns/{msgVpnName}/aclProfiles | Create an ACL Profile object.
*MsgVpnApi* | [**createMsgVpnAclProfileClientConnectException**](docs/MsgVpnApi.md#createMsgVpnAclProfileClientConnectException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Create a Client Connect Exception object.
*MsgVpnApi* | [**createMsgVpnAclProfilePublishException**](docs/MsgVpnApi.md#createMsgVpnAclProfilePublishException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Create a Publish Topic Exception object.
*MsgVpnApi* | [**createMsgVpnAclProfilePublishTopicException**](docs/MsgVpnApi.md#createMsgVpnAclProfilePublishTopicException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions | Create a Publish Topic Exception object.
*MsgVpnApi* | [**createMsgVpnAclProfileSubscribeException**](docs/MsgVpnApi.md#createMsgVpnAclProfileSubscribeException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Create a Subscribe Topic Exception object.
*MsgVpnApi* | [**createMsgVpnAclProfileSubscribeShareNameException**](docs/MsgVpnApi.md#createMsgVpnAclProfileSubscribeShareNameException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions | Create a Subscribe Share Name Exception object.
*MsgVpnApi* | [**createMsgVpnAclProfileSubscribeTopicException**](docs/MsgVpnApi.md#createMsgVpnAclProfileSubscribeTopicException) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions | Create a Subscribe Topic Exception object.
*MsgVpnApi* | [**createMsgVpnAuthenticationOauthProfile**](docs/MsgVpnApi.md#createMsgVpnAuthenticationOauthProfile) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles | Create an OAuth Profile object.
*MsgVpnApi* | [**createMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/MsgVpnApi.md#createMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims | Create a Required Claim object.
*MsgVpnApi* | [**createMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/MsgVpnApi.md#createMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Create a Required Claim object.
*MsgVpnApi* | [**createMsgVpnAuthenticationOauthProvider**](docs/MsgVpnApi.md#createMsgVpnAuthenticationOauthProvider) | **POST** /msgVpns/{msgVpnName}/authenticationOauthProviders | Create an OAuth Provider object.
*MsgVpnApi* | [**createMsgVpnAuthorizationGroup**](docs/MsgVpnApi.md#createMsgVpnAuthorizationGroup) | **POST** /msgVpns/{msgVpnName}/authorizationGroups | Create an Authorization Group object.
*MsgVpnApi* | [**createMsgVpnBridge**](docs/MsgVpnApi.md#createMsgVpnBridge) | **POST** /msgVpns/{msgVpnName}/bridges | Create a Bridge object.
*MsgVpnApi* | [**createMsgVpnBridgeRemoteMsgVpn**](docs/MsgVpnApi.md#createMsgVpnBridgeRemoteMsgVpn) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Create a Remote Message VPN object.
*MsgVpnApi* | [**createMsgVpnBridgeRemoteSubscription**](docs/MsgVpnApi.md#createMsgVpnBridgeRemoteSubscription) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Create a Remote Subscription object.
*MsgVpnApi* | [**createMsgVpnBridgeTlsTrustedCommonName**](docs/MsgVpnApi.md#createMsgVpnBridgeTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*MsgVpnApi* | [**createMsgVpnCertMatchingRule**](docs/MsgVpnApi.md#createMsgVpnCertMatchingRule) | **POST** /msgVpns/{msgVpnName}/certMatchingRules | Create a Certificate Matching Rule object.
*MsgVpnApi* | [**createMsgVpnCertMatchingRuleAttributeFilter**](docs/MsgVpnApi.md#createMsgVpnCertMatchingRuleAttributeFilter) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Create a Certificate Matching Rule Attribute Filter object.
*MsgVpnApi* | [**createMsgVpnCertMatchingRuleCondition**](docs/MsgVpnApi.md#createMsgVpnCertMatchingRuleCondition) | **POST** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Create a Certificate Matching Rule Condition object.
*MsgVpnApi* | [**createMsgVpnClientProfile**](docs/MsgVpnApi.md#createMsgVpnClientProfile) | **POST** /msgVpns/{msgVpnName}/clientProfiles | Create a Client Profile object.
*MsgVpnApi* | [**createMsgVpnClientUsername**](docs/MsgVpnApi.md#createMsgVpnClientUsername) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Create a Client Username object.
*MsgVpnApi* | [**createMsgVpnClientUsernameAttribute**](docs/MsgVpnApi.md#createMsgVpnClientUsernameAttribute) | **POST** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Create a Client Username Attribute object.
*MsgVpnApi* | [**createMsgVpnDistributedCache**](docs/MsgVpnApi.md#createMsgVpnDistributedCache) | **POST** /msgVpns/{msgVpnName}/distributedCaches | Create a Distributed Cache object.
*MsgVpnApi* | [**createMsgVpnDistributedCacheCluster**](docs/MsgVpnApi.md#createMsgVpnDistributedCacheCluster) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters | Create a Cache Cluster object.
*MsgVpnApi* | [**createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/MsgVpnApi.md#createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters | Create a Home Cache Cluster object.
*MsgVpnApi* | [**createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/MsgVpnApi.md#createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes | Create a Topic Prefix object.
*MsgVpnApi* | [**createMsgVpnDistributedCacheClusterInstance**](docs/MsgVpnApi.md#createMsgVpnDistributedCacheClusterInstance) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances | Create a Cache Instance object.
*MsgVpnApi* | [**createMsgVpnDistributedCacheClusterTopic**](docs/MsgVpnApi.md#createMsgVpnDistributedCacheClusterTopic) | **POST** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics | Create a Topic object.
*MsgVpnApi* | [**createMsgVpnDmrBridge**](docs/MsgVpnApi.md#createMsgVpnDmrBridge) | **POST** /msgVpns/{msgVpnName}/dmrBridges | Create a DMR Bridge object.
*MsgVpnApi* | [**createMsgVpnJndiConnectionFactory**](docs/MsgVpnApi.md#createMsgVpnJndiConnectionFactory) | **POST** /msgVpns/{msgVpnName}/jndiConnectionFactories | Create a JNDI Connection Factory object.
*MsgVpnApi* | [**createMsgVpnJndiQueue**](docs/MsgVpnApi.md#createMsgVpnJndiQueue) | **POST** /msgVpns/{msgVpnName}/jndiQueues | Create a JNDI Queue object.
*MsgVpnApi* | [**createMsgVpnJndiTopic**](docs/MsgVpnApi.md#createMsgVpnJndiTopic) | **POST** /msgVpns/{msgVpnName}/jndiTopics | Create a JNDI Topic object.
*MsgVpnApi* | [**createMsgVpnMqttRetainCache**](docs/MsgVpnApi.md#createMsgVpnMqttRetainCache) | **POST** /msgVpns/{msgVpnName}/mqttRetainCaches | Create an MQTT Retain Cache object.
*MsgVpnApi* | [**createMsgVpnMqttSession**](docs/MsgVpnApi.md#createMsgVpnMqttSession) | **POST** /msgVpns/{msgVpnName}/mqttSessions | Create an MQTT Session object.
*MsgVpnApi* | [**createMsgVpnMqttSessionSubscription**](docs/MsgVpnApi.md#createMsgVpnMqttSessionSubscription) | **POST** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Create a Subscription object.
*MsgVpnApi* | [**createMsgVpnQueue**](docs/MsgVpnApi.md#createMsgVpnQueue) | **POST** /msgVpns/{msgVpnName}/queues | Create a Queue object.
*MsgVpnApi* | [**createMsgVpnQueueSubscription**](docs/MsgVpnApi.md#createMsgVpnQueueSubscription) | **POST** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Create a Queue Subscription object.
*MsgVpnApi* | [**createMsgVpnQueueTemplate**](docs/MsgVpnApi.md#createMsgVpnQueueTemplate) | **POST** /msgVpns/{msgVpnName}/queueTemplates | Create a Queue Template object.
*MsgVpnApi* | [**createMsgVpnReplayLog**](docs/MsgVpnApi.md#createMsgVpnReplayLog) | **POST** /msgVpns/{msgVpnName}/replayLogs | Create a Replay Log object.
*MsgVpnApi* | [**createMsgVpnReplayLogTopicFilterSubscription**](docs/MsgVpnApi.md#createMsgVpnReplayLogTopicFilterSubscription) | **POST** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions | Create a Topic Filter Subscription object.
*MsgVpnApi* | [**createMsgVpnReplicatedTopic**](docs/MsgVpnApi.md#createMsgVpnReplicatedTopic) | **POST** /msgVpns/{msgVpnName}/replicatedTopics | Create a Replicated Topic object.
*MsgVpnApi* | [**createMsgVpnRestDeliveryPoint**](docs/MsgVpnApi.md#createMsgVpnRestDeliveryPoint) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints | Create a REST Delivery Point object.
*MsgVpnApi* | [**createMsgVpnRestDeliveryPointQueueBinding**](docs/MsgVpnApi.md#createMsgVpnRestDeliveryPointQueueBinding) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Create a Queue Binding object.
*MsgVpnApi* | [**createMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/MsgVpnApi.md#createMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders | Create a Request Header object.
*MsgVpnApi* | [**createMsgVpnRestDeliveryPointRestConsumer**](docs/MsgVpnApi.md#createMsgVpnRestDeliveryPointRestConsumer) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Create a REST Consumer object.
*MsgVpnApi* | [**createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/MsgVpnApi.md#createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims | Create a Claim object.
*MsgVpnApi* | [**createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/MsgVpnApi.md#createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*MsgVpnApi* | [**createMsgVpnSequencedTopic**](docs/MsgVpnApi.md#createMsgVpnSequencedTopic) | **POST** /msgVpns/{msgVpnName}/sequencedTopics | Create a Sequenced Topic object.
*MsgVpnApi* | [**createMsgVpnTopicEndpoint**](docs/MsgVpnApi.md#createMsgVpnTopicEndpoint) | **POST** /msgVpns/{msgVpnName}/topicEndpoints | Create a Topic Endpoint object.
*MsgVpnApi* | [**createMsgVpnTopicEndpointTemplate**](docs/MsgVpnApi.md#createMsgVpnTopicEndpointTemplate) | **POST** /msgVpns/{msgVpnName}/topicEndpointTemplates | Create a Topic Endpoint Template object.
*MsgVpnApi* | [**deleteMsgVpn**](docs/MsgVpnApi.md#deleteMsgVpn) | **DELETE** /msgVpns/{msgVpnName} | Delete a Message VPN object.
*MsgVpnApi* | [**deleteMsgVpnAclProfile**](docs/MsgVpnApi.md#deleteMsgVpnAclProfile) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Delete an ACL Profile object.
*MsgVpnApi* | [**deleteMsgVpnAclProfileClientConnectException**](docs/MsgVpnApi.md#deleteMsgVpnAclProfileClientConnectException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Delete a Client Connect Exception object.
*MsgVpnApi* | [**deleteMsgVpnAclProfilePublishException**](docs/MsgVpnApi.md#deleteMsgVpnAclProfilePublishException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Delete a Publish Topic Exception object.
*MsgVpnApi* | [**deleteMsgVpnAclProfilePublishTopicException**](docs/MsgVpnApi.md#deleteMsgVpnAclProfilePublishTopicException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions/{publishTopicExceptionSyntax},{publishTopicException} | Delete a Publish Topic Exception object.
*MsgVpnApi* | [**deleteMsgVpnAclProfileSubscribeException**](docs/MsgVpnApi.md#deleteMsgVpnAclProfileSubscribeException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Delete a Subscribe Topic Exception object.
*MsgVpnApi* | [**deleteMsgVpnAclProfileSubscribeShareNameException**](docs/MsgVpnApi.md#deleteMsgVpnAclProfileSubscribeShareNameException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions/{subscribeShareNameExceptionSyntax},{subscribeShareNameException} | Delete a Subscribe Share Name Exception object.
*MsgVpnApi* | [**deleteMsgVpnAclProfileSubscribeTopicException**](docs/MsgVpnApi.md#deleteMsgVpnAclProfileSubscribeTopicException) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions/{subscribeTopicExceptionSyntax},{subscribeTopicException} | Delete a Subscribe Topic Exception object.
*MsgVpnApi* | [**deleteMsgVpnAuthenticationOauthProfile**](docs/MsgVpnApi.md#deleteMsgVpnAuthenticationOauthProfile) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Delete an OAuth Profile object.
*MsgVpnApi* | [**deleteMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/MsgVpnApi.md#deleteMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Delete a Required Claim object.
*MsgVpnApi* | [**deleteMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/MsgVpnApi.md#deleteMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Delete a Required Claim object.
*MsgVpnApi* | [**deleteMsgVpnAuthenticationOauthProvider**](docs/MsgVpnApi.md#deleteMsgVpnAuthenticationOauthProvider) | **DELETE** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Delete an OAuth Provider object.
*MsgVpnApi* | [**deleteMsgVpnAuthorizationGroup**](docs/MsgVpnApi.md#deleteMsgVpnAuthorizationGroup) | **DELETE** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Delete an Authorization Group object.
*MsgVpnApi* | [**deleteMsgVpnBridge**](docs/MsgVpnApi.md#deleteMsgVpnBridge) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Delete a Bridge object.
*MsgVpnApi* | [**deleteMsgVpnBridgeRemoteMsgVpn**](docs/MsgVpnApi.md#deleteMsgVpnBridgeRemoteMsgVpn) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Delete a Remote Message VPN object.
*MsgVpnApi* | [**deleteMsgVpnBridgeRemoteSubscription**](docs/MsgVpnApi.md#deleteMsgVpnBridgeRemoteSubscription) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Delete a Remote Subscription object.
*MsgVpnApi* | [**deleteMsgVpnBridgeTlsTrustedCommonName**](docs/MsgVpnApi.md#deleteMsgVpnBridgeTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*MsgVpnApi* | [**deleteMsgVpnCertMatchingRule**](docs/MsgVpnApi.md#deleteMsgVpnCertMatchingRule) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Delete a Certificate Matching Rule object.
*MsgVpnApi* | [**deleteMsgVpnCertMatchingRuleAttributeFilter**](docs/MsgVpnApi.md#deleteMsgVpnCertMatchingRuleAttributeFilter) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Delete a Certificate Matching Rule Attribute Filter object.
*MsgVpnApi* | [**deleteMsgVpnCertMatchingRuleCondition**](docs/MsgVpnApi.md#deleteMsgVpnCertMatchingRuleCondition) | **DELETE** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Delete a Certificate Matching Rule Condition object.
*MsgVpnApi* | [**deleteMsgVpnClientProfile**](docs/MsgVpnApi.md#deleteMsgVpnClientProfile) | **DELETE** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Delete a Client Profile object.
*MsgVpnApi* | [**deleteMsgVpnClientUsername**](docs/MsgVpnApi.md#deleteMsgVpnClientUsername) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Delete a Client Username object.
*MsgVpnApi* | [**deleteMsgVpnClientUsernameAttribute**](docs/MsgVpnApi.md#deleteMsgVpnClientUsernameAttribute) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Delete a Client Username Attribute object.
*MsgVpnApi* | [**deleteMsgVpnDistributedCache**](docs/MsgVpnApi.md#deleteMsgVpnDistributedCache) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Delete a Distributed Cache object.
*MsgVpnApi* | [**deleteMsgVpnDistributedCacheCluster**](docs/MsgVpnApi.md#deleteMsgVpnDistributedCacheCluster) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Delete a Cache Cluster object.
*MsgVpnApi* | [**deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/MsgVpnApi.md#deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName} | Delete a Home Cache Cluster object.
*MsgVpnApi* | [**deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/MsgVpnApi.md#deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix} | Delete a Topic Prefix object.
*MsgVpnApi* | [**deleteMsgVpnDistributedCacheClusterInstance**](docs/MsgVpnApi.md#deleteMsgVpnDistributedCacheClusterInstance) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Delete a Cache Instance object.
*MsgVpnApi* | [**deleteMsgVpnDistributedCacheClusterTopic**](docs/MsgVpnApi.md#deleteMsgVpnDistributedCacheClusterTopic) | **DELETE** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic} | Delete a Topic object.
*MsgVpnApi* | [**deleteMsgVpnDmrBridge**](docs/MsgVpnApi.md#deleteMsgVpnDmrBridge) | **DELETE** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Delete a DMR Bridge object.
*MsgVpnApi* | [**deleteMsgVpnJndiConnectionFactory**](docs/MsgVpnApi.md#deleteMsgVpnJndiConnectionFactory) | **DELETE** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Delete a JNDI Connection Factory object.
*MsgVpnApi* | [**deleteMsgVpnJndiQueue**](docs/MsgVpnApi.md#deleteMsgVpnJndiQueue) | **DELETE** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Delete a JNDI Queue object.
*MsgVpnApi* | [**deleteMsgVpnJndiTopic**](docs/MsgVpnApi.md#deleteMsgVpnJndiTopic) | **DELETE** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Delete a JNDI Topic object.
*MsgVpnApi* | [**deleteMsgVpnMqttRetainCache**](docs/MsgVpnApi.md#deleteMsgVpnMqttRetainCache) | **DELETE** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Delete an MQTT Retain Cache object.
*MsgVpnApi* | [**deleteMsgVpnMqttSession**](docs/MsgVpnApi.md#deleteMsgVpnMqttSession) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Delete an MQTT Session object.
*MsgVpnApi* | [**deleteMsgVpnMqttSessionSubscription**](docs/MsgVpnApi.md#deleteMsgVpnMqttSessionSubscription) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Delete a Subscription object.
*MsgVpnApi* | [**deleteMsgVpnQueue**](docs/MsgVpnApi.md#deleteMsgVpnQueue) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName} | Delete a Queue object.
*MsgVpnApi* | [**deleteMsgVpnQueueSubscription**](docs/MsgVpnApi.md#deleteMsgVpnQueueSubscription) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Delete a Queue Subscription object.
*MsgVpnApi* | [**deleteMsgVpnQueueTemplate**](docs/MsgVpnApi.md#deleteMsgVpnQueueTemplate) | **DELETE** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Delete a Queue Template object.
*MsgVpnApi* | [**deleteMsgVpnReplayLog**](docs/MsgVpnApi.md#deleteMsgVpnReplayLog) | **DELETE** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Delete a Replay Log object.
*MsgVpnApi* | [**deleteMsgVpnReplayLogTopicFilterSubscription**](docs/MsgVpnApi.md#deleteMsgVpnReplayLogTopicFilterSubscription) | **DELETE** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions/{topicFilterSubscription} | Delete a Topic Filter Subscription object.
*MsgVpnApi* | [**deleteMsgVpnReplicatedTopic**](docs/MsgVpnApi.md#deleteMsgVpnReplicatedTopic) | **DELETE** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Delete a Replicated Topic object.
*MsgVpnApi* | [**deleteMsgVpnRestDeliveryPoint**](docs/MsgVpnApi.md#deleteMsgVpnRestDeliveryPoint) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Delete a REST Delivery Point object.
*MsgVpnApi* | [**deleteMsgVpnRestDeliveryPointQueueBinding**](docs/MsgVpnApi.md#deleteMsgVpnRestDeliveryPointQueueBinding) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Delete a Queue Binding object.
*MsgVpnApi* | [**deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/MsgVpnApi.md#deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Delete a Request Header object.
*MsgVpnApi* | [**deleteMsgVpnRestDeliveryPointRestConsumer**](docs/MsgVpnApi.md#deleteMsgVpnRestDeliveryPointRestConsumer) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Delete a REST Consumer object.
*MsgVpnApi* | [**deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/MsgVpnApi.md#deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName} | Delete a Claim object.
*MsgVpnApi* | [**deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/MsgVpnApi.md#deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*MsgVpnApi* | [**deleteMsgVpnSequencedTopic**](docs/MsgVpnApi.md#deleteMsgVpnSequencedTopic) | **DELETE** /msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic} | Delete a Sequenced Topic object.
*MsgVpnApi* | [**deleteMsgVpnTopicEndpoint**](docs/MsgVpnApi.md#deleteMsgVpnTopicEndpoint) | **DELETE** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Delete a Topic Endpoint object.
*MsgVpnApi* | [**deleteMsgVpnTopicEndpointTemplate**](docs/MsgVpnApi.md#deleteMsgVpnTopicEndpointTemplate) | **DELETE** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Delete a Topic Endpoint Template object.
*MsgVpnApi* | [**getMsgVpn**](docs/MsgVpnApi.md#getMsgVpn) | **GET** /msgVpns/{msgVpnName} | Get a Message VPN object.
*MsgVpnApi* | [**getMsgVpnAclProfile**](docs/MsgVpnApi.md#getMsgVpnAclProfile) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Get an ACL Profile object.
*MsgVpnApi* | [**getMsgVpnAclProfileClientConnectException**](docs/MsgVpnApi.md#getMsgVpnAclProfileClientConnectException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Get a Client Connect Exception object.
*MsgVpnApi* | [**getMsgVpnAclProfileClientConnectExceptions**](docs/MsgVpnApi.md#getMsgVpnAclProfileClientConnectExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Get a list of Client Connect Exception objects.
*MsgVpnApi* | [**getMsgVpnAclProfilePublishException**](docs/MsgVpnApi.md#getMsgVpnAclProfilePublishException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Get a Publish Topic Exception object.
*MsgVpnApi* | [**getMsgVpnAclProfilePublishExceptions**](docs/MsgVpnApi.md#getMsgVpnAclProfilePublishExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Get a list of Publish Topic Exception objects.
*MsgVpnApi* | [**getMsgVpnAclProfilePublishTopicException**](docs/MsgVpnApi.md#getMsgVpnAclProfilePublishTopicException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions/{publishTopicExceptionSyntax},{publishTopicException} | Get a Publish Topic Exception object.
*MsgVpnApi* | [**getMsgVpnAclProfilePublishTopicExceptions**](docs/MsgVpnApi.md#getMsgVpnAclProfilePublishTopicExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishTopicExceptions | Get a list of Publish Topic Exception objects.
*MsgVpnApi* | [**getMsgVpnAclProfileSubscribeException**](docs/MsgVpnApi.md#getMsgVpnAclProfileSubscribeException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Get a Subscribe Topic Exception object.
*MsgVpnApi* | [**getMsgVpnAclProfileSubscribeExceptions**](docs/MsgVpnApi.md#getMsgVpnAclProfileSubscribeExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Get a list of Subscribe Topic Exception objects.
*MsgVpnApi* | [**getMsgVpnAclProfileSubscribeShareNameException**](docs/MsgVpnApi.md#getMsgVpnAclProfileSubscribeShareNameException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions/{subscribeShareNameExceptionSyntax},{subscribeShareNameException} | Get a Subscribe Share Name Exception object.
*MsgVpnApi* | [**getMsgVpnAclProfileSubscribeShareNameExceptions**](docs/MsgVpnApi.md#getMsgVpnAclProfileSubscribeShareNameExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeShareNameExceptions | Get a list of Subscribe Share Name Exception objects.
*MsgVpnApi* | [**getMsgVpnAclProfileSubscribeTopicException**](docs/MsgVpnApi.md#getMsgVpnAclProfileSubscribeTopicException) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions/{subscribeTopicExceptionSyntax},{subscribeTopicException} | Get a Subscribe Topic Exception object.
*MsgVpnApi* | [**getMsgVpnAclProfileSubscribeTopicExceptions**](docs/MsgVpnApi.md#getMsgVpnAclProfileSubscribeTopicExceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeTopicExceptions | Get a list of Subscribe Topic Exception objects.
*MsgVpnApi* | [**getMsgVpnAclProfiles**](docs/MsgVpnApi.md#getMsgVpnAclProfiles) | **GET** /msgVpns/{msgVpnName}/aclProfiles | Get a list of ACL Profile objects.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProfile**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProfile) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Get an OAuth Profile object.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProfileClientRequiredClaim**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProfileClientRequiredClaim) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Get a Required Claim object.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProfileClientRequiredClaims**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProfileClientRequiredClaims) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/clientRequiredClaims | Get a list of Required Claim objects.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaim) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Get a Required Claim object.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaims**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProfileResourceServerRequiredClaims) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Get a list of Required Claim objects.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProfiles**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProfiles) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProfiles | Get a list of OAuth Profile objects.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProvider**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProvider) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Get an OAuth Provider object.
*MsgVpnApi* | [**getMsgVpnAuthenticationOauthProviders**](docs/MsgVpnApi.md#getMsgVpnAuthenticationOauthProviders) | **GET** /msgVpns/{msgVpnName}/authenticationOauthProviders | Get a list of OAuth Provider objects.
*MsgVpnApi* | [**getMsgVpnAuthorizationGroup**](docs/MsgVpnApi.md#getMsgVpnAuthorizationGroup) | **GET** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Get an Authorization Group object.
*MsgVpnApi* | [**getMsgVpnAuthorizationGroups**](docs/MsgVpnApi.md#getMsgVpnAuthorizationGroups) | **GET** /msgVpns/{msgVpnName}/authorizationGroups | Get a list of Authorization Group objects.
*MsgVpnApi* | [**getMsgVpnBridge**](docs/MsgVpnApi.md#getMsgVpnBridge) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Get a Bridge object.
*MsgVpnApi* | [**getMsgVpnBridgeRemoteMsgVpn**](docs/MsgVpnApi.md#getMsgVpnBridgeRemoteMsgVpn) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Get a Remote Message VPN object.
*MsgVpnApi* | [**getMsgVpnBridgeRemoteMsgVpns**](docs/MsgVpnApi.md#getMsgVpnBridgeRemoteMsgVpns) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Get a list of Remote Message VPN objects.
*MsgVpnApi* | [**getMsgVpnBridgeRemoteSubscription**](docs/MsgVpnApi.md#getMsgVpnBridgeRemoteSubscription) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Get a Remote Subscription object.
*MsgVpnApi* | [**getMsgVpnBridgeRemoteSubscriptions**](docs/MsgVpnApi.md#getMsgVpnBridgeRemoteSubscriptions) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Get a list of Remote Subscription objects.
*MsgVpnApi* | [**getMsgVpnBridgeTlsTrustedCommonName**](docs/MsgVpnApi.md#getMsgVpnBridgeTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*MsgVpnApi* | [**getMsgVpnBridgeTlsTrustedCommonNames**](docs/MsgVpnApi.md#getMsgVpnBridgeTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*MsgVpnApi* | [**getMsgVpnBridges**](docs/MsgVpnApi.md#getMsgVpnBridges) | **GET** /msgVpns/{msgVpnName}/bridges | Get a list of Bridge objects.
*MsgVpnApi* | [**getMsgVpnCertMatchingRule**](docs/MsgVpnApi.md#getMsgVpnCertMatchingRule) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Get a Certificate Matching Rule object.
*MsgVpnApi* | [**getMsgVpnCertMatchingRuleAttributeFilter**](docs/MsgVpnApi.md#getMsgVpnCertMatchingRuleAttributeFilter) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Get a Certificate Matching Rule Attribute Filter object.
*MsgVpnApi* | [**getMsgVpnCertMatchingRuleAttributeFilters**](docs/MsgVpnApi.md#getMsgVpnCertMatchingRuleAttributeFilters) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters | Get a list of Certificate Matching Rule Attribute Filter objects.
*MsgVpnApi* | [**getMsgVpnCertMatchingRuleCondition**](docs/MsgVpnApi.md#getMsgVpnCertMatchingRuleCondition) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions/{source} | Get a Certificate Matching Rule Condition object.
*MsgVpnApi* | [**getMsgVpnCertMatchingRuleConditions**](docs/MsgVpnApi.md#getMsgVpnCertMatchingRuleConditions) | **GET** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/conditions | Get a list of Certificate Matching Rule Condition objects.
*MsgVpnApi* | [**getMsgVpnCertMatchingRules**](docs/MsgVpnApi.md#getMsgVpnCertMatchingRules) | **GET** /msgVpns/{msgVpnName}/certMatchingRules | Get a list of Certificate Matching Rule objects.
*MsgVpnApi* | [**getMsgVpnClientProfile**](docs/MsgVpnApi.md#getMsgVpnClientProfile) | **GET** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Get a Client Profile object.
*MsgVpnApi* | [**getMsgVpnClientProfiles**](docs/MsgVpnApi.md#getMsgVpnClientProfiles) | **GET** /msgVpns/{msgVpnName}/clientProfiles | Get a list of Client Profile objects.
*MsgVpnApi* | [**getMsgVpnClientUsername**](docs/MsgVpnApi.md#getMsgVpnClientUsername) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Get a Client Username object.
*MsgVpnApi* | [**getMsgVpnClientUsernameAttribute**](docs/MsgVpnApi.md#getMsgVpnClientUsernameAttribute) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes/{attributeName},{attributeValue} | Get a Client Username Attribute object.
*MsgVpnApi* | [**getMsgVpnClientUsernameAttributes**](docs/MsgVpnApi.md#getMsgVpnClientUsernameAttributes) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername}/attributes | Get a list of Client Username Attribute objects.
*MsgVpnApi* | [**getMsgVpnClientUsernames**](docs/MsgVpnApi.md#getMsgVpnClientUsernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Get a list of Client Username objects.
*MsgVpnApi* | [**getMsgVpnDistributedCache**](docs/MsgVpnApi.md#getMsgVpnDistributedCache) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Get a Distributed Cache object.
*MsgVpnApi* | [**getMsgVpnDistributedCacheCluster**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheCluster) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Get a Cache Cluster object.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName} | Get a Home Cache Cluster object.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix} | Get a Topic Prefix object.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes | Get a list of Topic Prefix objects.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters | Get a list of Home Cache Cluster objects.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterInstance**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterInstance) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Get a Cache Instance object.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterInstances**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterInstances) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances | Get a list of Cache Instance objects.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterTopic**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterTopic) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic} | Get a Topic object.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusterTopics**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusterTopics) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics | Get a list of Topic objects.
*MsgVpnApi* | [**getMsgVpnDistributedCacheClusters**](docs/MsgVpnApi.md#getMsgVpnDistributedCacheClusters) | **GET** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters | Get a list of Cache Cluster objects.
*MsgVpnApi* | [**getMsgVpnDistributedCaches**](docs/MsgVpnApi.md#getMsgVpnDistributedCaches) | **GET** /msgVpns/{msgVpnName}/distributedCaches | Get a list of Distributed Cache objects.
*MsgVpnApi* | [**getMsgVpnDmrBridge**](docs/MsgVpnApi.md#getMsgVpnDmrBridge) | **GET** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Get a DMR Bridge object.
*MsgVpnApi* | [**getMsgVpnDmrBridges**](docs/MsgVpnApi.md#getMsgVpnDmrBridges) | **GET** /msgVpns/{msgVpnName}/dmrBridges | Get a list of DMR Bridge objects.
*MsgVpnApi* | [**getMsgVpnJndiConnectionFactories**](docs/MsgVpnApi.md#getMsgVpnJndiConnectionFactories) | **GET** /msgVpns/{msgVpnName}/jndiConnectionFactories | Get a list of JNDI Connection Factory objects.
*MsgVpnApi* | [**getMsgVpnJndiConnectionFactory**](docs/MsgVpnApi.md#getMsgVpnJndiConnectionFactory) | **GET** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Get a JNDI Connection Factory object.
*MsgVpnApi* | [**getMsgVpnJndiQueue**](docs/MsgVpnApi.md#getMsgVpnJndiQueue) | **GET** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Get a JNDI Queue object.
*MsgVpnApi* | [**getMsgVpnJndiQueues**](docs/MsgVpnApi.md#getMsgVpnJndiQueues) | **GET** /msgVpns/{msgVpnName}/jndiQueues | Get a list of JNDI Queue objects.
*MsgVpnApi* | [**getMsgVpnJndiTopic**](docs/MsgVpnApi.md#getMsgVpnJndiTopic) | **GET** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Get a JNDI Topic object.
*MsgVpnApi* | [**getMsgVpnJndiTopics**](docs/MsgVpnApi.md#getMsgVpnJndiTopics) | **GET** /msgVpns/{msgVpnName}/jndiTopics | Get a list of JNDI Topic objects.
*MsgVpnApi* | [**getMsgVpnMqttRetainCache**](docs/MsgVpnApi.md#getMsgVpnMqttRetainCache) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Get an MQTT Retain Cache object.
*MsgVpnApi* | [**getMsgVpnMqttRetainCaches**](docs/MsgVpnApi.md#getMsgVpnMqttRetainCaches) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches | Get a list of MQTT Retain Cache objects.
*MsgVpnApi* | [**getMsgVpnMqttSession**](docs/MsgVpnApi.md#getMsgVpnMqttSession) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Get an MQTT Session object.
*MsgVpnApi* | [**getMsgVpnMqttSessionSubscription**](docs/MsgVpnApi.md#getMsgVpnMqttSessionSubscription) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Get a Subscription object.
*MsgVpnApi* | [**getMsgVpnMqttSessionSubscriptions**](docs/MsgVpnApi.md#getMsgVpnMqttSessionSubscriptions) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Get a list of Subscription objects.
*MsgVpnApi* | [**getMsgVpnMqttSessions**](docs/MsgVpnApi.md#getMsgVpnMqttSessions) | **GET** /msgVpns/{msgVpnName}/mqttSessions | Get a list of MQTT Session objects.
*MsgVpnApi* | [**getMsgVpnQueue**](docs/MsgVpnApi.md#getMsgVpnQueue) | **GET** /msgVpns/{msgVpnName}/queues/{queueName} | Get a Queue object.
*MsgVpnApi* | [**getMsgVpnQueueSubscription**](docs/MsgVpnApi.md#getMsgVpnQueueSubscription) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Get a Queue Subscription object.
*MsgVpnApi* | [**getMsgVpnQueueSubscriptions**](docs/MsgVpnApi.md#getMsgVpnQueueSubscriptions) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Get a list of Queue Subscription objects.
*MsgVpnApi* | [**getMsgVpnQueueTemplate**](docs/MsgVpnApi.md#getMsgVpnQueueTemplate) | **GET** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Get a Queue Template object.
*MsgVpnApi* | [**getMsgVpnQueueTemplates**](docs/MsgVpnApi.md#getMsgVpnQueueTemplates) | **GET** /msgVpns/{msgVpnName}/queueTemplates | Get a list of Queue Template objects.
*MsgVpnApi* | [**getMsgVpnQueues**](docs/MsgVpnApi.md#getMsgVpnQueues) | **GET** /msgVpns/{msgVpnName}/queues | Get a list of Queue objects.
*MsgVpnApi* | [**getMsgVpnReplayLog**](docs/MsgVpnApi.md#getMsgVpnReplayLog) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Get a Replay Log object.
*MsgVpnApi* | [**getMsgVpnReplayLogTopicFilterSubscription**](docs/MsgVpnApi.md#getMsgVpnReplayLogTopicFilterSubscription) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions/{topicFilterSubscription} | Get a Topic Filter Subscription object.
*MsgVpnApi* | [**getMsgVpnReplayLogTopicFilterSubscriptions**](docs/MsgVpnApi.md#getMsgVpnReplayLogTopicFilterSubscriptions) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions | Get a list of Topic Filter Subscription objects.
*MsgVpnApi* | [**getMsgVpnReplayLogs**](docs/MsgVpnApi.md#getMsgVpnReplayLogs) | **GET** /msgVpns/{msgVpnName}/replayLogs | Get a list of Replay Log objects.
*MsgVpnApi* | [**getMsgVpnReplicatedTopic**](docs/MsgVpnApi.md#getMsgVpnReplicatedTopic) | **GET** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Get a Replicated Topic object.
*MsgVpnApi* | [**getMsgVpnReplicatedTopics**](docs/MsgVpnApi.md#getMsgVpnReplicatedTopics) | **GET** /msgVpns/{msgVpnName}/replicatedTopics | Get a list of Replicated Topic objects.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPoint**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPoint) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Get a REST Delivery Point object.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointQueueBinding**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointQueueBinding) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Get a Queue Binding object.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Get a Request Header object.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointQueueBindingRequestHeaders**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointQueueBindingRequestHeaders) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders | Get a list of Request Header objects.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointQueueBindings**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointQueueBindings) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Get a list of Queue Binding objects.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointRestConsumer**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointRestConsumer) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Get a REST Consumer object.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName} | Get a Claim object.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims | Get a list of Claim objects.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPointRestConsumers**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPointRestConsumers) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Get a list of REST Consumer objects.
*MsgVpnApi* | [**getMsgVpnRestDeliveryPoints**](docs/MsgVpnApi.md#getMsgVpnRestDeliveryPoints) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints | Get a list of REST Delivery Point objects.
*MsgVpnApi* | [**getMsgVpnSequencedTopic**](docs/MsgVpnApi.md#getMsgVpnSequencedTopic) | **GET** /msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic} | Get a Sequenced Topic object.
*MsgVpnApi* | [**getMsgVpnSequencedTopics**](docs/MsgVpnApi.md#getMsgVpnSequencedTopics) | **GET** /msgVpns/{msgVpnName}/sequencedTopics | Get a list of Sequenced Topic objects.
*MsgVpnApi* | [**getMsgVpnTopicEndpoint**](docs/MsgVpnApi.md#getMsgVpnTopicEndpoint) | **GET** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Get a Topic Endpoint object.
*MsgVpnApi* | [**getMsgVpnTopicEndpointTemplate**](docs/MsgVpnApi.md#getMsgVpnTopicEndpointTemplate) | **GET** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Get a Topic Endpoint Template object.
*MsgVpnApi* | [**getMsgVpnTopicEndpointTemplates**](docs/MsgVpnApi.md#getMsgVpnTopicEndpointTemplates) | **GET** /msgVpns/{msgVpnName}/topicEndpointTemplates | Get a list of Topic Endpoint Template objects.
*MsgVpnApi* | [**getMsgVpnTopicEndpoints**](docs/MsgVpnApi.md#getMsgVpnTopicEndpoints) | **GET** /msgVpns/{msgVpnName}/topicEndpoints | Get a list of Topic Endpoint objects.
*MsgVpnApi* | [**getMsgVpns**](docs/MsgVpnApi.md#getMsgVpns) | **GET** /msgVpns | Get a list of Message VPN objects.
*MsgVpnApi* | [**replaceMsgVpn**](docs/MsgVpnApi.md#replaceMsgVpn) | **PUT** /msgVpns/{msgVpnName} | Replace a Message VPN object.
*MsgVpnApi* | [**replaceMsgVpnAclProfile**](docs/MsgVpnApi.md#replaceMsgVpnAclProfile) | **PUT** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Replace an ACL Profile object.
*MsgVpnApi* | [**replaceMsgVpnAuthenticationOauthProfile**](docs/MsgVpnApi.md#replaceMsgVpnAuthenticationOauthProfile) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Replace an OAuth Profile object.
*MsgVpnApi* | [**replaceMsgVpnAuthenticationOauthProvider**](docs/MsgVpnApi.md#replaceMsgVpnAuthenticationOauthProvider) | **PUT** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Replace an OAuth Provider object.
*MsgVpnApi* | [**replaceMsgVpnAuthorizationGroup**](docs/MsgVpnApi.md#replaceMsgVpnAuthorizationGroup) | **PUT** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Replace an Authorization Group object.
*MsgVpnApi* | [**replaceMsgVpnBridge**](docs/MsgVpnApi.md#replaceMsgVpnBridge) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Replace a Bridge object.
*MsgVpnApi* | [**replaceMsgVpnBridgeRemoteMsgVpn**](docs/MsgVpnApi.md#replaceMsgVpnBridgeRemoteMsgVpn) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Replace a Remote Message VPN object.
*MsgVpnApi* | [**replaceMsgVpnCertMatchingRule**](docs/MsgVpnApi.md#replaceMsgVpnCertMatchingRule) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Replace a Certificate Matching Rule object.
*MsgVpnApi* | [**replaceMsgVpnCertMatchingRuleAttributeFilter**](docs/MsgVpnApi.md#replaceMsgVpnCertMatchingRuleAttributeFilter) | **PUT** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Replace a Certificate Matching Rule Attribute Filter object.
*MsgVpnApi* | [**replaceMsgVpnClientProfile**](docs/MsgVpnApi.md#replaceMsgVpnClientProfile) | **PUT** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Replace a Client Profile object.
*MsgVpnApi* | [**replaceMsgVpnClientUsername**](docs/MsgVpnApi.md#replaceMsgVpnClientUsername) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replace a Client Username object.
*MsgVpnApi* | [**replaceMsgVpnDistributedCache**](docs/MsgVpnApi.md#replaceMsgVpnDistributedCache) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Replace a Distributed Cache object.
*MsgVpnApi* | [**replaceMsgVpnDistributedCacheCluster**](docs/MsgVpnApi.md#replaceMsgVpnDistributedCacheCluster) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Replace a Cache Cluster object.
*MsgVpnApi* | [**replaceMsgVpnDistributedCacheClusterInstance**](docs/MsgVpnApi.md#replaceMsgVpnDistributedCacheClusterInstance) | **PUT** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Replace a Cache Instance object.
*MsgVpnApi* | [**replaceMsgVpnDmrBridge**](docs/MsgVpnApi.md#replaceMsgVpnDmrBridge) | **PUT** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Replace a DMR Bridge object.
*MsgVpnApi* | [**replaceMsgVpnJndiConnectionFactory**](docs/MsgVpnApi.md#replaceMsgVpnJndiConnectionFactory) | **PUT** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Replace a JNDI Connection Factory object.
*MsgVpnApi* | [**replaceMsgVpnJndiQueue**](docs/MsgVpnApi.md#replaceMsgVpnJndiQueue) | **PUT** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Replace a JNDI Queue object.
*MsgVpnApi* | [**replaceMsgVpnJndiTopic**](docs/MsgVpnApi.md#replaceMsgVpnJndiTopic) | **PUT** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Replace a JNDI Topic object.
*MsgVpnApi* | [**replaceMsgVpnMqttRetainCache**](docs/MsgVpnApi.md#replaceMsgVpnMqttRetainCache) | **PUT** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Replace an MQTT Retain Cache object.
*MsgVpnApi* | [**replaceMsgVpnMqttSession**](docs/MsgVpnApi.md#replaceMsgVpnMqttSession) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Replace an MQTT Session object.
*MsgVpnApi* | [**replaceMsgVpnMqttSessionSubscription**](docs/MsgVpnApi.md#replaceMsgVpnMqttSessionSubscription) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Replace a Subscription object.
*MsgVpnApi* | [**replaceMsgVpnQueue**](docs/MsgVpnApi.md#replaceMsgVpnQueue) | **PUT** /msgVpns/{msgVpnName}/queues/{queueName} | Replace a Queue object.
*MsgVpnApi* | [**replaceMsgVpnQueueTemplate**](docs/MsgVpnApi.md#replaceMsgVpnQueueTemplate) | **PUT** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Replace a Queue Template object.
*MsgVpnApi* | [**replaceMsgVpnReplayLog**](docs/MsgVpnApi.md#replaceMsgVpnReplayLog) | **PUT** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Replace a Replay Log object.
*MsgVpnApi* | [**replaceMsgVpnReplicatedTopic**](docs/MsgVpnApi.md#replaceMsgVpnReplicatedTopic) | **PUT** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Replace a Replicated Topic object.
*MsgVpnApi* | [**replaceMsgVpnRestDeliveryPoint**](docs/MsgVpnApi.md#replaceMsgVpnRestDeliveryPoint) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Replace a REST Delivery Point object.
*MsgVpnApi* | [**replaceMsgVpnRestDeliveryPointQueueBinding**](docs/MsgVpnApi.md#replaceMsgVpnRestDeliveryPointQueueBinding) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Replace a Queue Binding object.
*MsgVpnApi* | [**replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/MsgVpnApi.md#replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Replace a Request Header object.
*MsgVpnApi* | [**replaceMsgVpnRestDeliveryPointRestConsumer**](docs/MsgVpnApi.md#replaceMsgVpnRestDeliveryPointRestConsumer) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Replace a REST Consumer object.
*MsgVpnApi* | [**replaceMsgVpnTopicEndpoint**](docs/MsgVpnApi.md#replaceMsgVpnTopicEndpoint) | **PUT** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Replace a Topic Endpoint object.
*MsgVpnApi* | [**replaceMsgVpnTopicEndpointTemplate**](docs/MsgVpnApi.md#replaceMsgVpnTopicEndpointTemplate) | **PUT** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Replace a Topic Endpoint Template object.
*MsgVpnApi* | [**updateMsgVpn**](docs/MsgVpnApi.md#updateMsgVpn) | **PATCH** /msgVpns/{msgVpnName} | Update a Message VPN object.
*MsgVpnApi* | [**updateMsgVpnAclProfile**](docs/MsgVpnApi.md#updateMsgVpnAclProfile) | **PATCH** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Update an ACL Profile object.
*MsgVpnApi* | [**updateMsgVpnAuthenticationOauthProfile**](docs/MsgVpnApi.md#updateMsgVpnAuthenticationOauthProfile) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProfiles/{oauthProfileName} | Update an OAuth Profile object.
*MsgVpnApi* | [**updateMsgVpnAuthenticationOauthProvider**](docs/MsgVpnApi.md#updateMsgVpnAuthenticationOauthProvider) | **PATCH** /msgVpns/{msgVpnName}/authenticationOauthProviders/{oauthProviderName} | Update an OAuth Provider object.
*MsgVpnApi* | [**updateMsgVpnAuthorizationGroup**](docs/MsgVpnApi.md#updateMsgVpnAuthorizationGroup) | **PATCH** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Update an Authorization Group object.
*MsgVpnApi* | [**updateMsgVpnBridge**](docs/MsgVpnApi.md#updateMsgVpnBridge) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Update a Bridge object.
*MsgVpnApi* | [**updateMsgVpnBridgeRemoteMsgVpn**](docs/MsgVpnApi.md#updateMsgVpnBridgeRemoteMsgVpn) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Update a Remote Message VPN object.
*MsgVpnApi* | [**updateMsgVpnCertMatchingRule**](docs/MsgVpnApi.md#updateMsgVpnCertMatchingRule) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName} | Update a Certificate Matching Rule object.
*MsgVpnApi* | [**updateMsgVpnCertMatchingRuleAttributeFilter**](docs/MsgVpnApi.md#updateMsgVpnCertMatchingRuleAttributeFilter) | **PATCH** /msgVpns/{msgVpnName}/certMatchingRules/{ruleName}/attributeFilters/{filterName} | Update a Certificate Matching Rule Attribute Filter object.
*MsgVpnApi* | [**updateMsgVpnClientProfile**](docs/MsgVpnApi.md#updateMsgVpnClientProfile) | **PATCH** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Update a Client Profile object.
*MsgVpnApi* | [**updateMsgVpnClientUsername**](docs/MsgVpnApi.md#updateMsgVpnClientUsername) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Update a Client Username object.
*MsgVpnApi* | [**updateMsgVpnDistributedCache**](docs/MsgVpnApi.md#updateMsgVpnDistributedCache) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName} | Update a Distributed Cache object.
*MsgVpnApi* | [**updateMsgVpnDistributedCacheCluster**](docs/MsgVpnApi.md#updateMsgVpnDistributedCacheCluster) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName} | Update a Cache Cluster object.
*MsgVpnApi* | [**updateMsgVpnDistributedCacheClusterInstance**](docs/MsgVpnApi.md#updateMsgVpnDistributedCacheClusterInstance) | **PATCH** /msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName} | Update a Cache Instance object.
*MsgVpnApi* | [**updateMsgVpnDmrBridge**](docs/MsgVpnApi.md#updateMsgVpnDmrBridge) | **PATCH** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Update a DMR Bridge object.
*MsgVpnApi* | [**updateMsgVpnJndiConnectionFactory**](docs/MsgVpnApi.md#updateMsgVpnJndiConnectionFactory) | **PATCH** /msgVpns/{msgVpnName}/jndiConnectionFactories/{connectionFactoryName} | Update a JNDI Connection Factory object.
*MsgVpnApi* | [**updateMsgVpnJndiQueue**](docs/MsgVpnApi.md#updateMsgVpnJndiQueue) | **PATCH** /msgVpns/{msgVpnName}/jndiQueues/{queueName} | Update a JNDI Queue object.
*MsgVpnApi* | [**updateMsgVpnJndiTopic**](docs/MsgVpnApi.md#updateMsgVpnJndiTopic) | **PATCH** /msgVpns/{msgVpnName}/jndiTopics/{topicName} | Update a JNDI Topic object.
*MsgVpnApi* | [**updateMsgVpnMqttRetainCache**](docs/MsgVpnApi.md#updateMsgVpnMqttRetainCache) | **PATCH** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Update an MQTT Retain Cache object.
*MsgVpnApi* | [**updateMsgVpnMqttSession**](docs/MsgVpnApi.md#updateMsgVpnMqttSession) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Update an MQTT Session object.
*MsgVpnApi* | [**updateMsgVpnMqttSessionSubscription**](docs/MsgVpnApi.md#updateMsgVpnMqttSessionSubscription) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Update a Subscription object.
*MsgVpnApi* | [**updateMsgVpnQueue**](docs/MsgVpnApi.md#updateMsgVpnQueue) | **PATCH** /msgVpns/{msgVpnName}/queues/{queueName} | Update a Queue object.
*MsgVpnApi* | [**updateMsgVpnQueueTemplate**](docs/MsgVpnApi.md#updateMsgVpnQueueTemplate) | **PATCH** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Update a Queue Template object.
*MsgVpnApi* | [**updateMsgVpnReplayLog**](docs/MsgVpnApi.md#updateMsgVpnReplayLog) | **PATCH** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Update a Replay Log object.
*MsgVpnApi* | [**updateMsgVpnReplicatedTopic**](docs/MsgVpnApi.md#updateMsgVpnReplicatedTopic) | **PATCH** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Update a Replicated Topic object.
*MsgVpnApi* | [**updateMsgVpnRestDeliveryPoint**](docs/MsgVpnApi.md#updateMsgVpnRestDeliveryPoint) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Update a REST Delivery Point object.
*MsgVpnApi* | [**updateMsgVpnRestDeliveryPointQueueBinding**](docs/MsgVpnApi.md#updateMsgVpnRestDeliveryPointQueueBinding) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Update a Queue Binding object.
*MsgVpnApi* | [**updateMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/MsgVpnApi.md#updateMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Update a Request Header object.
*MsgVpnApi* | [**updateMsgVpnRestDeliveryPointRestConsumer**](docs/MsgVpnApi.md#updateMsgVpnRestDeliveryPointRestConsumer) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Update a REST Consumer object.
*MsgVpnApi* | [**updateMsgVpnTopicEndpoint**](docs/MsgVpnApi.md#updateMsgVpnTopicEndpoint) | **PATCH** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Update a Topic Endpoint object.
*MsgVpnApi* | [**updateMsgVpnTopicEndpointTemplate**](docs/MsgVpnApi.md#updateMsgVpnTopicEndpointTemplate) | **PATCH** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Update a Topic Endpoint Template object.
*OauthProfileApi* | [**createOauthProfile**](docs/OauthProfileApi.md#createOauthProfile) | **POST** /oauthProfiles | Create an OAuth Profile object.
*OauthProfileApi* | [**createOauthProfileAccessLevelGroup**](docs/OauthProfileApi.md#createOauthProfileAccessLevelGroup) | **POST** /oauthProfiles/{oauthProfileName}/accessLevelGroups | Create a Group Access Level object.
*OauthProfileApi* | [**createOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/OauthProfileApi.md#createOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **POST** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions | Create a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**createOauthProfileClientAllowedHost**](docs/OauthProfileApi.md#createOauthProfileClientAllowedHost) | **POST** /oauthProfiles/{oauthProfileName}/clientAllowedHosts | Create an Allowed Host Value object.
*OauthProfileApi* | [**createOauthProfileClientAuthorizationParameter**](docs/OauthProfileApi.md#createOauthProfileClientAuthorizationParameter) | **POST** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters | Create an Authorization Parameter object.
*OauthProfileApi* | [**createOauthProfileClientRequiredClaim**](docs/OauthProfileApi.md#createOauthProfileClientRequiredClaim) | **POST** /oauthProfiles/{oauthProfileName}/clientRequiredClaims | Create a Required Claim object.
*OauthProfileApi* | [**createOauthProfileDefaultMsgVpnAccessLevelException**](docs/OauthProfileApi.md#createOauthProfileDefaultMsgVpnAccessLevelException) | **POST** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions | Create a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**createOauthProfileResourceServerRequiredClaim**](docs/OauthProfileApi.md#createOauthProfileResourceServerRequiredClaim) | **POST** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Create a Required Claim object.
*OauthProfileApi* | [**deleteOauthProfile**](docs/OauthProfileApi.md#deleteOauthProfile) | **DELETE** /oauthProfiles/{oauthProfileName} | Delete an OAuth Profile object.
*OauthProfileApi* | [**deleteOauthProfileAccessLevelGroup**](docs/OauthProfileApi.md#deleteOauthProfileAccessLevelGroup) | **DELETE** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Delete a Group Access Level object.
*OauthProfileApi* | [**deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/OauthProfileApi.md#deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **DELETE** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**deleteOauthProfileClientAllowedHost**](docs/OauthProfileApi.md#deleteOauthProfileClientAllowedHost) | **DELETE** /oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost} | Delete an Allowed Host Value object.
*OauthProfileApi* | [**deleteOauthProfileClientAuthorizationParameter**](docs/OauthProfileApi.md#deleteOauthProfileClientAuthorizationParameter) | **DELETE** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Delete an Authorization Parameter object.
*OauthProfileApi* | [**deleteOauthProfileClientRequiredClaim**](docs/OauthProfileApi.md#deleteOauthProfileClientRequiredClaim) | **DELETE** /oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Delete a Required Claim object.
*OauthProfileApi* | [**deleteOauthProfileDefaultMsgVpnAccessLevelException**](docs/OauthProfileApi.md#deleteOauthProfileDefaultMsgVpnAccessLevelException) | **DELETE** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**deleteOauthProfileResourceServerRequiredClaim**](docs/OauthProfileApi.md#deleteOauthProfileResourceServerRequiredClaim) | **DELETE** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Delete a Required Claim object.
*OauthProfileApi* | [**getOauthProfile**](docs/OauthProfileApi.md#getOauthProfile) | **GET** /oauthProfiles/{oauthProfileName} | Get an OAuth Profile object.
*OauthProfileApi* | [**getOauthProfileAccessLevelGroup**](docs/OauthProfileApi.md#getOauthProfileAccessLevelGroup) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Get a Group Access Level object.
*OauthProfileApi* | [**getOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/OauthProfileApi.md#getOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions**](docs/OauthProfileApi.md#getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions | Get a list of Message VPN Access-Level Exception objects.
*OauthProfileApi* | [**getOauthProfileAccessLevelGroups**](docs/OauthProfileApi.md#getOauthProfileAccessLevelGroups) | **GET** /oauthProfiles/{oauthProfileName}/accessLevelGroups | Get a list of Group Access Level objects.
*OauthProfileApi* | [**getOauthProfileClientAllowedHost**](docs/OauthProfileApi.md#getOauthProfileClientAllowedHost) | **GET** /oauthProfiles/{oauthProfileName}/clientAllowedHosts/{allowedHost} | Get an Allowed Host Value object.
*OauthProfileApi* | [**getOauthProfileClientAllowedHosts**](docs/OauthProfileApi.md#getOauthProfileClientAllowedHosts) | **GET** /oauthProfiles/{oauthProfileName}/clientAllowedHosts | Get a list of Allowed Host Value objects.
*OauthProfileApi* | [**getOauthProfileClientAuthorizationParameter**](docs/OauthProfileApi.md#getOauthProfileClientAuthorizationParameter) | **GET** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Get an Authorization Parameter object.
*OauthProfileApi* | [**getOauthProfileClientAuthorizationParameters**](docs/OauthProfileApi.md#getOauthProfileClientAuthorizationParameters) | **GET** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters | Get a list of Authorization Parameter objects.
*OauthProfileApi* | [**getOauthProfileClientRequiredClaim**](docs/OauthProfileApi.md#getOauthProfileClientRequiredClaim) | **GET** /oauthProfiles/{oauthProfileName}/clientRequiredClaims/{clientRequiredClaimName} | Get a Required Claim object.
*OauthProfileApi* | [**getOauthProfileClientRequiredClaims**](docs/OauthProfileApi.md#getOauthProfileClientRequiredClaims) | **GET** /oauthProfiles/{oauthProfileName}/clientRequiredClaims | Get a list of Required Claim objects.
*OauthProfileApi* | [**getOauthProfileDefaultMsgVpnAccessLevelException**](docs/OauthProfileApi.md#getOauthProfileDefaultMsgVpnAccessLevelException) | **GET** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**getOauthProfileDefaultMsgVpnAccessLevelExceptions**](docs/OauthProfileApi.md#getOauthProfileDefaultMsgVpnAccessLevelExceptions) | **GET** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions | Get a list of Message VPN Access-Level Exception objects.
*OauthProfileApi* | [**getOauthProfileResourceServerRequiredClaim**](docs/OauthProfileApi.md#getOauthProfileResourceServerRequiredClaim) | **GET** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims/{resourceServerRequiredClaimName} | Get a Required Claim object.
*OauthProfileApi* | [**getOauthProfileResourceServerRequiredClaims**](docs/OauthProfileApi.md#getOauthProfileResourceServerRequiredClaims) | **GET** /oauthProfiles/{oauthProfileName}/resourceServerRequiredClaims | Get a list of Required Claim objects.
*OauthProfileApi* | [**getOauthProfiles**](docs/OauthProfileApi.md#getOauthProfiles) | **GET** /oauthProfiles | Get a list of OAuth Profile objects.
*OauthProfileApi* | [**replaceOauthProfile**](docs/OauthProfileApi.md#replaceOauthProfile) | **PUT** /oauthProfiles/{oauthProfileName} | Replace an OAuth Profile object.
*OauthProfileApi* | [**replaceOauthProfileAccessLevelGroup**](docs/OauthProfileApi.md#replaceOauthProfileAccessLevelGroup) | **PUT** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Replace a Group Access Level object.
*OauthProfileApi* | [**replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/OauthProfileApi.md#replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **PUT** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**replaceOauthProfileClientAuthorizationParameter**](docs/OauthProfileApi.md#replaceOauthProfileClientAuthorizationParameter) | **PUT** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Replace an Authorization Parameter object.
*OauthProfileApi* | [**replaceOauthProfileDefaultMsgVpnAccessLevelException**](docs/OauthProfileApi.md#replaceOauthProfileDefaultMsgVpnAccessLevelException) | **PUT** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**updateOauthProfile**](docs/OauthProfileApi.md#updateOauthProfile) | **PATCH** /oauthProfiles/{oauthProfileName} | Update an OAuth Profile object.
*OauthProfileApi* | [**updateOauthProfileAccessLevelGroup**](docs/OauthProfileApi.md#updateOauthProfileAccessLevelGroup) | **PATCH** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName} | Update a Group Access Level object.
*OauthProfileApi* | [**updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException**](docs/OauthProfileApi.md#updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException) | **PATCH** /oauthProfiles/{oauthProfileName}/accessLevelGroups/{groupName}/msgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access-Level Exception object.
*OauthProfileApi* | [**updateOauthProfileClientAuthorizationParameter**](docs/OauthProfileApi.md#updateOauthProfileClientAuthorizationParameter) | **PATCH** /oauthProfiles/{oauthProfileName}/clientAuthorizationParameters/{authorizationParameterName} | Update an Authorization Parameter object.
*OauthProfileApi* | [**updateOauthProfileDefaultMsgVpnAccessLevelException**](docs/OauthProfileApi.md#updateOauthProfileDefaultMsgVpnAccessLevelException) | **PATCH** /oauthProfiles/{oauthProfileName}/defaultMsgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access-Level Exception object.
*QueueApi* | [**createMsgVpnQueue**](docs/QueueApi.md#createMsgVpnQueue) | **POST** /msgVpns/{msgVpnName}/queues | Create a Queue object.
*QueueApi* | [**createMsgVpnQueueSubscription**](docs/QueueApi.md#createMsgVpnQueueSubscription) | **POST** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Create a Queue Subscription object.
*QueueApi* | [**deleteMsgVpnQueue**](docs/QueueApi.md#deleteMsgVpnQueue) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName} | Delete a Queue object.
*QueueApi* | [**deleteMsgVpnQueueSubscription**](docs/QueueApi.md#deleteMsgVpnQueueSubscription) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Delete a Queue Subscription object.
*QueueApi* | [**getMsgVpnQueue**](docs/QueueApi.md#getMsgVpnQueue) | **GET** /msgVpns/{msgVpnName}/queues/{queueName} | Get a Queue object.
*QueueApi* | [**getMsgVpnQueueSubscription**](docs/QueueApi.md#getMsgVpnQueueSubscription) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Get a Queue Subscription object.
*QueueApi* | [**getMsgVpnQueueSubscriptions**](docs/QueueApi.md#getMsgVpnQueueSubscriptions) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Get a list of Queue Subscription objects.
*QueueApi* | [**getMsgVpnQueues**](docs/QueueApi.md#getMsgVpnQueues) | **GET** /msgVpns/{msgVpnName}/queues | Get a list of Queue objects.
*QueueApi* | [**replaceMsgVpnQueue**](docs/QueueApi.md#replaceMsgVpnQueue) | **PUT** /msgVpns/{msgVpnName}/queues/{queueName} | Replace a Queue object.
*QueueApi* | [**updateMsgVpnQueue**](docs/QueueApi.md#updateMsgVpnQueue) | **PATCH** /msgVpns/{msgVpnName}/queues/{queueName} | Update a Queue object.
*QueueTemplateApi* | [**createMsgVpnQueueTemplate**](docs/QueueTemplateApi.md#createMsgVpnQueueTemplate) | **POST** /msgVpns/{msgVpnName}/queueTemplates | Create a Queue Template object.
*QueueTemplateApi* | [**deleteMsgVpnQueueTemplate**](docs/QueueTemplateApi.md#deleteMsgVpnQueueTemplate) | **DELETE** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Delete a Queue Template object.
*QueueTemplateApi* | [**getMsgVpnQueueTemplate**](docs/QueueTemplateApi.md#getMsgVpnQueueTemplate) | **GET** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Get a Queue Template object.
*QueueTemplateApi* | [**getMsgVpnQueueTemplates**](docs/QueueTemplateApi.md#getMsgVpnQueueTemplates) | **GET** /msgVpns/{msgVpnName}/queueTemplates | Get a list of Queue Template objects.
*QueueTemplateApi* | [**replaceMsgVpnQueueTemplate**](docs/QueueTemplateApi.md#replaceMsgVpnQueueTemplate) | **PUT** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Replace a Queue Template object.
*QueueTemplateApi* | [**updateMsgVpnQueueTemplate**](docs/QueueTemplateApi.md#updateMsgVpnQueueTemplate) | **PATCH** /msgVpns/{msgVpnName}/queueTemplates/{queueTemplateName} | Update a Queue Template object.
*ReplayLogApi* | [**createMsgVpnReplayLog**](docs/ReplayLogApi.md#createMsgVpnReplayLog) | **POST** /msgVpns/{msgVpnName}/replayLogs | Create a Replay Log object.
*ReplayLogApi* | [**createMsgVpnReplayLogTopicFilterSubscription**](docs/ReplayLogApi.md#createMsgVpnReplayLogTopicFilterSubscription) | **POST** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions | Create a Topic Filter Subscription object.
*ReplayLogApi* | [**deleteMsgVpnReplayLog**](docs/ReplayLogApi.md#deleteMsgVpnReplayLog) | **DELETE** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Delete a Replay Log object.
*ReplayLogApi* | [**deleteMsgVpnReplayLogTopicFilterSubscription**](docs/ReplayLogApi.md#deleteMsgVpnReplayLogTopicFilterSubscription) | **DELETE** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions/{topicFilterSubscription} | Delete a Topic Filter Subscription object.
*ReplayLogApi* | [**getMsgVpnReplayLog**](docs/ReplayLogApi.md#getMsgVpnReplayLog) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Get a Replay Log object.
*ReplayLogApi* | [**getMsgVpnReplayLogTopicFilterSubscription**](docs/ReplayLogApi.md#getMsgVpnReplayLogTopicFilterSubscription) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions/{topicFilterSubscription} | Get a Topic Filter Subscription object.
*ReplayLogApi* | [**getMsgVpnReplayLogTopicFilterSubscriptions**](docs/ReplayLogApi.md#getMsgVpnReplayLogTopicFilterSubscriptions) | **GET** /msgVpns/{msgVpnName}/replayLogs/{replayLogName}/topicFilterSubscriptions | Get a list of Topic Filter Subscription objects.
*ReplayLogApi* | [**getMsgVpnReplayLogs**](docs/ReplayLogApi.md#getMsgVpnReplayLogs) | **GET** /msgVpns/{msgVpnName}/replayLogs | Get a list of Replay Log objects.
*ReplayLogApi* | [**replaceMsgVpnReplayLog**](docs/ReplayLogApi.md#replaceMsgVpnReplayLog) | **PUT** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Replace a Replay Log object.
*ReplayLogApi* | [**updateMsgVpnReplayLog**](docs/ReplayLogApi.md#updateMsgVpnReplayLog) | **PATCH** /msgVpns/{msgVpnName}/replayLogs/{replayLogName} | Update a Replay Log object.
*ReplicatedTopicApi* | [**createMsgVpnReplicatedTopic**](docs/ReplicatedTopicApi.md#createMsgVpnReplicatedTopic) | **POST** /msgVpns/{msgVpnName}/replicatedTopics | Create a Replicated Topic object.
*ReplicatedTopicApi* | [**deleteMsgVpnReplicatedTopic**](docs/ReplicatedTopicApi.md#deleteMsgVpnReplicatedTopic) | **DELETE** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Delete a Replicated Topic object.
*ReplicatedTopicApi* | [**getMsgVpnReplicatedTopic**](docs/ReplicatedTopicApi.md#getMsgVpnReplicatedTopic) | **GET** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Get a Replicated Topic object.
*ReplicatedTopicApi* | [**getMsgVpnReplicatedTopics**](docs/ReplicatedTopicApi.md#getMsgVpnReplicatedTopics) | **GET** /msgVpns/{msgVpnName}/replicatedTopics | Get a list of Replicated Topic objects.
*ReplicatedTopicApi* | [**replaceMsgVpnReplicatedTopic**](docs/ReplicatedTopicApi.md#replaceMsgVpnReplicatedTopic) | **PUT** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Replace a Replicated Topic object.
*ReplicatedTopicApi* | [**updateMsgVpnReplicatedTopic**](docs/ReplicatedTopicApi.md#updateMsgVpnReplicatedTopic) | **PATCH** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Update a Replicated Topic object.
*RestDeliveryPointApi* | [**createMsgVpnRestDeliveryPoint**](docs/RestDeliveryPointApi.md#createMsgVpnRestDeliveryPoint) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints | Create a REST Delivery Point object.
*RestDeliveryPointApi* | [**createMsgVpnRestDeliveryPointQueueBinding**](docs/RestDeliveryPointApi.md#createMsgVpnRestDeliveryPointQueueBinding) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Create a Queue Binding object.
*RestDeliveryPointApi* | [**createMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/RestDeliveryPointApi.md#createMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders | Create a Request Header object.
*RestDeliveryPointApi* | [**createMsgVpnRestDeliveryPointRestConsumer**](docs/RestDeliveryPointApi.md#createMsgVpnRestDeliveryPointRestConsumer) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Create a REST Consumer object.
*RestDeliveryPointApi* | [**createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/RestDeliveryPointApi.md#createMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims | Create a Claim object.
*RestDeliveryPointApi* | [**createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/RestDeliveryPointApi.md#createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Create a Trusted Common Name object.
*RestDeliveryPointApi* | [**deleteMsgVpnRestDeliveryPoint**](docs/RestDeliveryPointApi.md#deleteMsgVpnRestDeliveryPoint) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Delete a REST Delivery Point object.
*RestDeliveryPointApi* | [**deleteMsgVpnRestDeliveryPointQueueBinding**](docs/RestDeliveryPointApi.md#deleteMsgVpnRestDeliveryPointQueueBinding) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Delete a Queue Binding object.
*RestDeliveryPointApi* | [**deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/RestDeliveryPointApi.md#deleteMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Delete a Request Header object.
*RestDeliveryPointApi* | [**deleteMsgVpnRestDeliveryPointRestConsumer**](docs/RestDeliveryPointApi.md#deleteMsgVpnRestDeliveryPointRestConsumer) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Delete a REST Consumer object.
*RestDeliveryPointApi* | [**deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/RestDeliveryPointApi.md#deleteMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName} | Delete a Claim object.
*RestDeliveryPointApi* | [**deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/RestDeliveryPointApi.md#deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPoint**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPoint) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Get a REST Delivery Point object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointQueueBinding**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointQueueBinding) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Get a Queue Binding object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Get a Request Header object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointQueueBindingRequestHeaders**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointQueueBindingRequestHeaders) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders | Get a list of Request Header objects.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointQueueBindings**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointQueueBindings) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Get a list of Queue Binding objects.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointRestConsumer**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointRestConsumer) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Get a REST Consumer object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaim) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims/{oauthJwtClaimName} | Get a Claim object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointRestConsumerOauthJwtClaims) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/oauthJwtClaims | Get a list of Claim objects.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPointRestConsumers**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPointRestConsumers) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Get a list of REST Consumer objects.
*RestDeliveryPointApi* | [**getMsgVpnRestDeliveryPoints**](docs/RestDeliveryPointApi.md#getMsgVpnRestDeliveryPoints) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints | Get a list of REST Delivery Point objects.
*RestDeliveryPointApi* | [**replaceMsgVpnRestDeliveryPoint**](docs/RestDeliveryPointApi.md#replaceMsgVpnRestDeliveryPoint) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Replace a REST Delivery Point object.
*RestDeliveryPointApi* | [**replaceMsgVpnRestDeliveryPointQueueBinding**](docs/RestDeliveryPointApi.md#replaceMsgVpnRestDeliveryPointQueueBinding) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Replace a Queue Binding object.
*RestDeliveryPointApi* | [**replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/RestDeliveryPointApi.md#replaceMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Replace a Request Header object.
*RestDeliveryPointApi* | [**replaceMsgVpnRestDeliveryPointRestConsumer**](docs/RestDeliveryPointApi.md#replaceMsgVpnRestDeliveryPointRestConsumer) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Replace a REST Consumer object.
*RestDeliveryPointApi* | [**updateMsgVpnRestDeliveryPoint**](docs/RestDeliveryPointApi.md#updateMsgVpnRestDeliveryPoint) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Update a REST Delivery Point object.
*RestDeliveryPointApi* | [**updateMsgVpnRestDeliveryPointQueueBinding**](docs/RestDeliveryPointApi.md#updateMsgVpnRestDeliveryPointQueueBinding) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Update a Queue Binding object.
*RestDeliveryPointApi* | [**updateMsgVpnRestDeliveryPointQueueBindingRequestHeader**](docs/RestDeliveryPointApi.md#updateMsgVpnRestDeliveryPointQueueBindingRequestHeader) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}/requestHeaders/{headerName} | Update a Request Header object.
*RestDeliveryPointApi* | [**updateMsgVpnRestDeliveryPointRestConsumer**](docs/RestDeliveryPointApi.md#updateMsgVpnRestDeliveryPointRestConsumer) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Update a REST Consumer object.
*SystemInformationApi* | [**getSystemInformation**](docs/SystemInformationApi.md#getSystemInformation) | **GET** /systemInformation | Get a System Information object.
*TopicEndpointApi* | [**createMsgVpnTopicEndpoint**](docs/TopicEndpointApi.md#createMsgVpnTopicEndpoint) | **POST** /msgVpns/{msgVpnName}/topicEndpoints | Create a Topic Endpoint object.
*TopicEndpointApi* | [**deleteMsgVpnTopicEndpoint**](docs/TopicEndpointApi.md#deleteMsgVpnTopicEndpoint) | **DELETE** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Delete a Topic Endpoint object.
*TopicEndpointApi* | [**getMsgVpnTopicEndpoint**](docs/TopicEndpointApi.md#getMsgVpnTopicEndpoint) | **GET** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Get a Topic Endpoint object.
*TopicEndpointApi* | [**getMsgVpnTopicEndpoints**](docs/TopicEndpointApi.md#getMsgVpnTopicEndpoints) | **GET** /msgVpns/{msgVpnName}/topicEndpoints | Get a list of Topic Endpoint objects.
*TopicEndpointApi* | [**replaceMsgVpnTopicEndpoint**](docs/TopicEndpointApi.md#replaceMsgVpnTopicEndpoint) | **PUT** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Replace a Topic Endpoint object.
*TopicEndpointApi* | [**updateMsgVpnTopicEndpoint**](docs/TopicEndpointApi.md#updateMsgVpnTopicEndpoint) | **PATCH** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Update a Topic Endpoint object.
*TopicEndpointTemplateApi* | [**createMsgVpnTopicEndpointTemplate**](docs/TopicEndpointTemplateApi.md#createMsgVpnTopicEndpointTemplate) | **POST** /msgVpns/{msgVpnName}/topicEndpointTemplates | Create a Topic Endpoint Template object.
*TopicEndpointTemplateApi* | [**deleteMsgVpnTopicEndpointTemplate**](docs/TopicEndpointTemplateApi.md#deleteMsgVpnTopicEndpointTemplate) | **DELETE** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Delete a Topic Endpoint Template object.
*TopicEndpointTemplateApi* | [**getMsgVpnTopicEndpointTemplate**](docs/TopicEndpointTemplateApi.md#getMsgVpnTopicEndpointTemplate) | **GET** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Get a Topic Endpoint Template object.
*TopicEndpointTemplateApi* | [**getMsgVpnTopicEndpointTemplates**](docs/TopicEndpointTemplateApi.md#getMsgVpnTopicEndpointTemplates) | **GET** /msgVpns/{msgVpnName}/topicEndpointTemplates | Get a list of Topic Endpoint Template objects.
*TopicEndpointTemplateApi* | [**replaceMsgVpnTopicEndpointTemplate**](docs/TopicEndpointTemplateApi.md#replaceMsgVpnTopicEndpointTemplate) | **PUT** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Replace a Topic Endpoint Template object.
*TopicEndpointTemplateApi* | [**updateMsgVpnTopicEndpointTemplate**](docs/TopicEndpointTemplateApi.md#updateMsgVpnTopicEndpointTemplate) | **PATCH** /msgVpns/{msgVpnName}/topicEndpointTemplates/{topicEndpointTemplateName} | Update a Topic Endpoint Template object.
*VirtualHostnameApi* | [**createVirtualHostname**](docs/VirtualHostnameApi.md#createVirtualHostname) | **POST** /virtualHostnames | Create a Virtual Hostname object.
*VirtualHostnameApi* | [**deleteVirtualHostname**](docs/VirtualHostnameApi.md#deleteVirtualHostname) | **DELETE** /virtualHostnames/{virtualHostname} | Delete a Virtual Hostname object.
*VirtualHostnameApi* | [**getVirtualHostname**](docs/VirtualHostnameApi.md#getVirtualHostname) | **GET** /virtualHostnames/{virtualHostname} | Get a Virtual Hostname object.
*VirtualHostnameApi* | [**getVirtualHostnames**](docs/VirtualHostnameApi.md#getVirtualHostnames) | **GET** /virtualHostnames | Get a list of Virtual Hostname objects.
*VirtualHostnameApi* | [**replaceVirtualHostname**](docs/VirtualHostnameApi.md#replaceVirtualHostname) | **PUT** /virtualHostnames/{virtualHostname} | Replace a Virtual Hostname object.
*VirtualHostnameApi* | [**updateVirtualHostname**](docs/VirtualHostnameApi.md#updateVirtualHostname) | **PATCH** /virtualHostnames/{virtualHostname} | Update a Virtual Hostname object.


## Documentation for Models

 - [AboutApi](docs/AboutApi.md)
 - [AboutApiLinks](docs/AboutApiLinks.md)
 - [AboutApiResponse](docs/AboutApiResponse.md)
 - [AboutLinks](docs/AboutLinks.md)
 - [AboutResponse](docs/AboutResponse.md)
 - [AboutUser](docs/AboutUser.md)
 - [AboutUserLinks](docs/AboutUserLinks.md)
 - [AboutUserMsgVpn](docs/AboutUserMsgVpn.md)
 - [AboutUserMsgVpnLinks](docs/AboutUserMsgVpnLinks.md)
 - [AboutUserMsgVpnResponse](docs/AboutUserMsgVpnResponse.md)
 - [AboutUserMsgVpnsResponse](docs/AboutUserMsgVpnsResponse.md)
 - [AboutUserResponse](docs/AboutUserResponse.md)
 - [Broker](docs/Broker.md)
 - [BrokerLinks](docs/BrokerLinks.md)
 - [BrokerResponse](docs/BrokerResponse.md)
 - [CertAuthoritiesResponse](docs/CertAuthoritiesResponse.md)
 - [CertAuthority](docs/CertAuthority.md)
 - [CertAuthorityLinks](docs/CertAuthorityLinks.md)
 - [CertAuthorityOcspTlsTrustedCommonName](docs/CertAuthorityOcspTlsTrustedCommonName.md)
 - [CertAuthorityOcspTlsTrustedCommonNameLinks](docs/CertAuthorityOcspTlsTrustedCommonNameLinks.md)
 - [CertAuthorityOcspTlsTrustedCommonNameResponse](docs/CertAuthorityOcspTlsTrustedCommonNameResponse.md)
 - [CertAuthorityOcspTlsTrustedCommonNamesResponse](docs/CertAuthorityOcspTlsTrustedCommonNamesResponse.md)
 - [CertAuthorityResponse](docs/CertAuthorityResponse.md)
 - [ClientCertAuthoritiesResponse](docs/ClientCertAuthoritiesResponse.md)
 - [ClientCertAuthority](docs/ClientCertAuthority.md)
 - [ClientCertAuthorityLinks](docs/ClientCertAuthorityLinks.md)
 - [ClientCertAuthorityOcspTlsTrustedCommonName](docs/ClientCertAuthorityOcspTlsTrustedCommonName.md)
 - [ClientCertAuthorityOcspTlsTrustedCommonNameLinks](docs/ClientCertAuthorityOcspTlsTrustedCommonNameLinks.md)
 - [ClientCertAuthorityOcspTlsTrustedCommonNameResponse](docs/ClientCertAuthorityOcspTlsTrustedCommonNameResponse.md)
 - [ClientCertAuthorityOcspTlsTrustedCommonNamesResponse](docs/ClientCertAuthorityOcspTlsTrustedCommonNamesResponse.md)
 - [ClientCertAuthorityResponse](docs/ClientCertAuthorityResponse.md)
 - [DmrCluster](docs/DmrCluster.md)
 - [DmrClusterCertMatchingRule](docs/DmrClusterCertMatchingRule.md)
 - [DmrClusterCertMatchingRuleAttributeFilter](docs/DmrClusterCertMatchingRuleAttributeFilter.md)
 - [DmrClusterCertMatchingRuleAttributeFilterLinks](docs/DmrClusterCertMatchingRuleAttributeFilterLinks.md)
 - [DmrClusterCertMatchingRuleAttributeFilterResponse](docs/DmrClusterCertMatchingRuleAttributeFilterResponse.md)
 - [DmrClusterCertMatchingRuleAttributeFiltersResponse](docs/DmrClusterCertMatchingRuleAttributeFiltersResponse.md)
 - [DmrClusterCertMatchingRuleCondition](docs/DmrClusterCertMatchingRuleCondition.md)
 - [DmrClusterCertMatchingRuleConditionLinks](docs/DmrClusterCertMatchingRuleConditionLinks.md)
 - [DmrClusterCertMatchingRuleConditionResponse](docs/DmrClusterCertMatchingRuleConditionResponse.md)
 - [DmrClusterCertMatchingRuleConditionsResponse](docs/DmrClusterCertMatchingRuleConditionsResponse.md)
 - [DmrClusterCertMatchingRuleLinks](docs/DmrClusterCertMatchingRuleLinks.md)
 - [DmrClusterCertMatchingRuleResponse](docs/DmrClusterCertMatchingRuleResponse.md)
 - [DmrClusterCertMatchingRulesResponse](docs/DmrClusterCertMatchingRulesResponse.md)
 - [DmrClusterLink](docs/DmrClusterLink.md)
 - [DmrClusterLinkAttribute](docs/DmrClusterLinkAttribute.md)
 - [DmrClusterLinkAttributeLinks](docs/DmrClusterLinkAttributeLinks.md)
 - [DmrClusterLinkAttributeResponse](docs/DmrClusterLinkAttributeResponse.md)
 - [DmrClusterLinkAttributesResponse](docs/DmrClusterLinkAttributesResponse.md)
 - [DmrClusterLinkLinks](docs/DmrClusterLinkLinks.md)
 - [DmrClusterLinkRemoteAddress](docs/DmrClusterLinkRemoteAddress.md)
 - [DmrClusterLinkRemoteAddressLinks](docs/DmrClusterLinkRemoteAddressLinks.md)
 - [DmrClusterLinkRemoteAddressResponse](docs/DmrClusterLinkRemoteAddressResponse.md)
 - [DmrClusterLinkRemoteAddressesResponse](docs/DmrClusterLinkRemoteAddressesResponse.md)
 - [DmrClusterLinkResponse](docs/DmrClusterLinkResponse.md)
 - [DmrClusterLinkTlsTrustedCommonName](docs/DmrClusterLinkTlsTrustedCommonName.md)
 - [DmrClusterLinkTlsTrustedCommonNameLinks](docs/DmrClusterLinkTlsTrustedCommonNameLinks.md)
 - [DmrClusterLinkTlsTrustedCommonNameResponse](docs/DmrClusterLinkTlsTrustedCommonNameResponse.md)
 - [DmrClusterLinkTlsTrustedCommonNamesResponse](docs/DmrClusterLinkTlsTrustedCommonNamesResponse.md)
 - [DmrClusterLinks](docs/DmrClusterLinks.md)
 - [DmrClusterLinksResponse](docs/DmrClusterLinksResponse.md)
 - [DmrClusterResponse](docs/DmrClusterResponse.md)
 - [DmrClustersResponse](docs/DmrClustersResponse.md)
 - [DomainCertAuthoritiesResponse](docs/DomainCertAuthoritiesResponse.md)
 - [DomainCertAuthority](docs/DomainCertAuthority.md)
 - [DomainCertAuthorityLinks](docs/DomainCertAuthorityLinks.md)
 - [DomainCertAuthorityResponse](docs/DomainCertAuthorityResponse.md)
 - [EventThreshold](docs/EventThreshold.md)
 - [EventThresholdByPercent](docs/EventThresholdByPercent.md)
 - [EventThresholdByValue](docs/EventThresholdByValue.md)
 - [MsgVpn](docs/MsgVpn.md)
 - [MsgVpnAclProfile](docs/MsgVpnAclProfile.md)
 - [MsgVpnAclProfileClientConnectException](docs/MsgVpnAclProfileClientConnectException.md)
 - [MsgVpnAclProfileClientConnectExceptionLinks](docs/MsgVpnAclProfileClientConnectExceptionLinks.md)
 - [MsgVpnAclProfileClientConnectExceptionResponse](docs/MsgVpnAclProfileClientConnectExceptionResponse.md)
 - [MsgVpnAclProfileClientConnectExceptionsResponse](docs/MsgVpnAclProfileClientConnectExceptionsResponse.md)
 - [MsgVpnAclProfileLinks](docs/MsgVpnAclProfileLinks.md)
 - [MsgVpnAclProfilePublishException](docs/MsgVpnAclProfilePublishException.md)
 - [MsgVpnAclProfilePublishExceptionLinks](docs/MsgVpnAclProfilePublishExceptionLinks.md)
 - [MsgVpnAclProfilePublishExceptionResponse](docs/MsgVpnAclProfilePublishExceptionResponse.md)
 - [MsgVpnAclProfilePublishExceptionsResponse](docs/MsgVpnAclProfilePublishExceptionsResponse.md)
 - [MsgVpnAclProfilePublishTopicException](docs/MsgVpnAclProfilePublishTopicException.md)
 - [MsgVpnAclProfilePublishTopicExceptionLinks](docs/MsgVpnAclProfilePublishTopicExceptionLinks.md)
 - [MsgVpnAclProfilePublishTopicExceptionResponse](docs/MsgVpnAclProfilePublishTopicExceptionResponse.md)
 - [MsgVpnAclProfilePublishTopicExceptionsResponse](docs/MsgVpnAclProfilePublishTopicExceptionsResponse.md)
 - [MsgVpnAclProfileResponse](docs/MsgVpnAclProfileResponse.md)
 - [MsgVpnAclProfileSubscribeException](docs/MsgVpnAclProfileSubscribeException.md)
 - [MsgVpnAclProfileSubscribeExceptionLinks](docs/MsgVpnAclProfileSubscribeExceptionLinks.md)
 - [MsgVpnAclProfileSubscribeExceptionResponse](docs/MsgVpnAclProfileSubscribeExceptionResponse.md)
 - [MsgVpnAclProfileSubscribeExceptionsResponse](docs/MsgVpnAclProfileSubscribeExceptionsResponse.md)
 - [MsgVpnAclProfileSubscribeShareNameException](docs/MsgVpnAclProfileSubscribeShareNameException.md)
 - [MsgVpnAclProfileSubscribeShareNameExceptionLinks](docs/MsgVpnAclProfileSubscribeShareNameExceptionLinks.md)
 - [MsgVpnAclProfileSubscribeShareNameExceptionResponse](docs/MsgVpnAclProfileSubscribeShareNameExceptionResponse.md)
 - [MsgVpnAclProfileSubscribeShareNameExceptionsResponse](docs/MsgVpnAclProfileSubscribeShareNameExceptionsResponse.md)
 - [MsgVpnAclProfileSubscribeTopicException](docs/MsgVpnAclProfileSubscribeTopicException.md)
 - [MsgVpnAclProfileSubscribeTopicExceptionLinks](docs/MsgVpnAclProfileSubscribeTopicExceptionLinks.md)
 - [MsgVpnAclProfileSubscribeTopicExceptionResponse](docs/MsgVpnAclProfileSubscribeTopicExceptionResponse.md)
 - [MsgVpnAclProfileSubscribeTopicExceptionsResponse](docs/MsgVpnAclProfileSubscribeTopicExceptionsResponse.md)
 - [MsgVpnAclProfilesResponse](docs/MsgVpnAclProfilesResponse.md)
 - [MsgVpnAuthenticationOauthProfile](docs/MsgVpnAuthenticationOauthProfile.md)
 - [MsgVpnAuthenticationOauthProfileClientRequiredClaim](docs/MsgVpnAuthenticationOauthProfileClientRequiredClaim.md)
 - [MsgVpnAuthenticationOauthProfileClientRequiredClaimLinks](docs/MsgVpnAuthenticationOauthProfileClientRequiredClaimLinks.md)
 - [MsgVpnAuthenticationOauthProfileClientRequiredClaimResponse](docs/MsgVpnAuthenticationOauthProfileClientRequiredClaimResponse.md)
 - [MsgVpnAuthenticationOauthProfileClientRequiredClaimsResponse](docs/MsgVpnAuthenticationOauthProfileClientRequiredClaimsResponse.md)
 - [MsgVpnAuthenticationOauthProfileLinks](docs/MsgVpnAuthenticationOauthProfileLinks.md)
 - [MsgVpnAuthenticationOauthProfileResourceServerRequiredClaim](docs/MsgVpnAuthenticationOauthProfileResourceServerRequiredClaim.md)
 - [MsgVpnAuthenticationOauthProfileResourceServerRequiredClaimLinks](docs/MsgVpnAuthenticationOauthProfileResourceServerRequiredClaimLinks.md)
 - [MsgVpnAuthenticationOauthProfileResourceServerRequiredClaimResponse](docs/MsgVpnAuthenticationOauthProfileResourceServerRequiredClaimResponse.md)
 - [MsgVpnAuthenticationOauthProfileResourceServerRequiredClaimsResponse](docs/MsgVpnAuthenticationOauthProfileResourceServerRequiredClaimsResponse.md)
 - [MsgVpnAuthenticationOauthProfileResponse](docs/MsgVpnAuthenticationOauthProfileResponse.md)
 - [MsgVpnAuthenticationOauthProfilesResponse](docs/MsgVpnAuthenticationOauthProfilesResponse.md)
 - [MsgVpnAuthenticationOauthProvider](docs/MsgVpnAuthenticationOauthProvider.md)
 - [MsgVpnAuthenticationOauthProviderLinks](docs/MsgVpnAuthenticationOauthProviderLinks.md)
 - [MsgVpnAuthenticationOauthProviderResponse](docs/MsgVpnAuthenticationOauthProviderResponse.md)
 - [MsgVpnAuthenticationOauthProvidersResponse](docs/MsgVpnAuthenticationOauthProvidersResponse.md)
 - [MsgVpnAuthorizationGroup](docs/MsgVpnAuthorizationGroup.md)
 - [MsgVpnAuthorizationGroupLinks](docs/MsgVpnAuthorizationGroupLinks.md)
 - [MsgVpnAuthorizationGroupResponse](docs/MsgVpnAuthorizationGroupResponse.md)
 - [MsgVpnAuthorizationGroupsResponse](docs/MsgVpnAuthorizationGroupsResponse.md)
 - [MsgVpnBridge](docs/MsgVpnBridge.md)
 - [MsgVpnBridgeLinks](docs/MsgVpnBridgeLinks.md)
 - [MsgVpnBridgeRemoteMsgVpn](docs/MsgVpnBridgeRemoteMsgVpn.md)
 - [MsgVpnBridgeRemoteMsgVpnLinks](docs/MsgVpnBridgeRemoteMsgVpnLinks.md)
 - [MsgVpnBridgeRemoteMsgVpnResponse](docs/MsgVpnBridgeRemoteMsgVpnResponse.md)
 - [MsgVpnBridgeRemoteMsgVpnsResponse](docs/MsgVpnBridgeRemoteMsgVpnsResponse.md)
 - [MsgVpnBridgeRemoteSubscription](docs/MsgVpnBridgeRemoteSubscription.md)
 - [MsgVpnBridgeRemoteSubscriptionLinks](docs/MsgVpnBridgeRemoteSubscriptionLinks.md)
 - [MsgVpnBridgeRemoteSubscriptionResponse](docs/MsgVpnBridgeRemoteSubscriptionResponse.md)
 - [MsgVpnBridgeRemoteSubscriptionsResponse](docs/MsgVpnBridgeRemoteSubscriptionsResponse.md)
 - [MsgVpnBridgeResponse](docs/MsgVpnBridgeResponse.md)
 - [MsgVpnBridgeTlsTrustedCommonName](docs/MsgVpnBridgeTlsTrustedCommonName.md)
 - [MsgVpnBridgeTlsTrustedCommonNameLinks](docs/MsgVpnBridgeTlsTrustedCommonNameLinks.md)
 - [MsgVpnBridgeTlsTrustedCommonNameResponse](docs/MsgVpnBridgeTlsTrustedCommonNameResponse.md)
 - [MsgVpnBridgeTlsTrustedCommonNamesResponse](docs/MsgVpnBridgeTlsTrustedCommonNamesResponse.md)
 - [MsgVpnBridgesResponse](docs/MsgVpnBridgesResponse.md)
 - [MsgVpnCertMatchingRule](docs/MsgVpnCertMatchingRule.md)
 - [MsgVpnCertMatchingRuleAttributeFilter](docs/MsgVpnCertMatchingRuleAttributeFilter.md)
 - [MsgVpnCertMatchingRuleAttributeFilterLinks](docs/MsgVpnCertMatchingRuleAttributeFilterLinks.md)
 - [MsgVpnCertMatchingRuleAttributeFilterResponse](docs/MsgVpnCertMatchingRuleAttributeFilterResponse.md)
 - [MsgVpnCertMatchingRuleAttributeFiltersResponse](docs/MsgVpnCertMatchingRuleAttributeFiltersResponse.md)
 - [MsgVpnCertMatchingRuleCondition](docs/MsgVpnCertMatchingRuleCondition.md)
 - [MsgVpnCertMatchingRuleConditionLinks](docs/MsgVpnCertMatchingRuleConditionLinks.md)
 - [MsgVpnCertMatchingRuleConditionResponse](docs/MsgVpnCertMatchingRuleConditionResponse.md)
 - [MsgVpnCertMatchingRuleConditionsResponse](docs/MsgVpnCertMatchingRuleConditionsResponse.md)
 - [MsgVpnCertMatchingRuleLinks](docs/MsgVpnCertMatchingRuleLinks.md)
 - [MsgVpnCertMatchingRuleResponse](docs/MsgVpnCertMatchingRuleResponse.md)
 - [MsgVpnCertMatchingRulesResponse](docs/MsgVpnCertMatchingRulesResponse.md)
 - [MsgVpnClientProfile](docs/MsgVpnClientProfile.md)
 - [MsgVpnClientProfileLinks](docs/MsgVpnClientProfileLinks.md)
 - [MsgVpnClientProfileResponse](docs/MsgVpnClientProfileResponse.md)
 - [MsgVpnClientProfilesResponse](docs/MsgVpnClientProfilesResponse.md)
 - [MsgVpnClientUsername](docs/MsgVpnClientUsername.md)
 - [MsgVpnClientUsernameAttribute](docs/MsgVpnClientUsernameAttribute.md)
 - [MsgVpnClientUsernameAttributeLinks](docs/MsgVpnClientUsernameAttributeLinks.md)
 - [MsgVpnClientUsernameAttributeResponse](docs/MsgVpnClientUsernameAttributeResponse.md)
 - [MsgVpnClientUsernameAttributesResponse](docs/MsgVpnClientUsernameAttributesResponse.md)
 - [MsgVpnClientUsernameLinks](docs/MsgVpnClientUsernameLinks.md)
 - [MsgVpnClientUsernameResponse](docs/MsgVpnClientUsernameResponse.md)
 - [MsgVpnClientUsernamesResponse](docs/MsgVpnClientUsernamesResponse.md)
 - [MsgVpnDistributedCache](docs/MsgVpnDistributedCache.md)
 - [MsgVpnDistributedCacheCluster](docs/MsgVpnDistributedCacheCluster.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeCluster](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeCluster.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClusterLinks](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClusterLinks.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixLinks](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixLinks.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse.md)
 - [MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse](docs/MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse.md)
 - [MsgVpnDistributedCacheClusterInstance](docs/MsgVpnDistributedCacheClusterInstance.md)
 - [MsgVpnDistributedCacheClusterInstanceLinks](docs/MsgVpnDistributedCacheClusterInstanceLinks.md)
 - [MsgVpnDistributedCacheClusterInstanceResponse](docs/MsgVpnDistributedCacheClusterInstanceResponse.md)
 - [MsgVpnDistributedCacheClusterInstancesResponse](docs/MsgVpnDistributedCacheClusterInstancesResponse.md)
 - [MsgVpnDistributedCacheClusterLinks](docs/MsgVpnDistributedCacheClusterLinks.md)
 - [MsgVpnDistributedCacheClusterResponse](docs/MsgVpnDistributedCacheClusterResponse.md)
 - [MsgVpnDistributedCacheClusterTopic](docs/MsgVpnDistributedCacheClusterTopic.md)
 - [MsgVpnDistributedCacheClusterTopicLinks](docs/MsgVpnDistributedCacheClusterTopicLinks.md)
 - [MsgVpnDistributedCacheClusterTopicResponse](docs/MsgVpnDistributedCacheClusterTopicResponse.md)
 - [MsgVpnDistributedCacheClusterTopicsResponse](docs/MsgVpnDistributedCacheClusterTopicsResponse.md)
 - [MsgVpnDistributedCacheClustersResponse](docs/MsgVpnDistributedCacheClustersResponse.md)
 - [MsgVpnDistributedCacheLinks](docs/MsgVpnDistributedCacheLinks.md)
 - [MsgVpnDistributedCacheResponse](docs/MsgVpnDistributedCacheResponse.md)
 - [MsgVpnDistributedCachesResponse](docs/MsgVpnDistributedCachesResponse.md)
 - [MsgVpnDmrBridge](docs/MsgVpnDmrBridge.md)
 - [MsgVpnDmrBridgeLinks](docs/MsgVpnDmrBridgeLinks.md)
 - [MsgVpnDmrBridgeResponse](docs/MsgVpnDmrBridgeResponse.md)
 - [MsgVpnDmrBridgesResponse](docs/MsgVpnDmrBridgesResponse.md)
 - [MsgVpnJndiConnectionFactoriesResponse](docs/MsgVpnJndiConnectionFactoriesResponse.md)
 - [MsgVpnJndiConnectionFactory](docs/MsgVpnJndiConnectionFactory.md)
 - [MsgVpnJndiConnectionFactoryLinks](docs/MsgVpnJndiConnectionFactoryLinks.md)
 - [MsgVpnJndiConnectionFactoryResponse](docs/MsgVpnJndiConnectionFactoryResponse.md)
 - [MsgVpnJndiQueue](docs/MsgVpnJndiQueue.md)
 - [MsgVpnJndiQueueLinks](docs/MsgVpnJndiQueueLinks.md)
 - [MsgVpnJndiQueueResponse](docs/MsgVpnJndiQueueResponse.md)
 - [MsgVpnJndiQueuesResponse](docs/MsgVpnJndiQueuesResponse.md)
 - [MsgVpnJndiTopic](docs/MsgVpnJndiTopic.md)
 - [MsgVpnJndiTopicLinks](docs/MsgVpnJndiTopicLinks.md)
 - [MsgVpnJndiTopicResponse](docs/MsgVpnJndiTopicResponse.md)
 - [MsgVpnJndiTopicsResponse](docs/MsgVpnJndiTopicsResponse.md)
 - [MsgVpnLinks](docs/MsgVpnLinks.md)
 - [MsgVpnMqttRetainCache](docs/MsgVpnMqttRetainCache.md)
 - [MsgVpnMqttRetainCacheLinks](docs/MsgVpnMqttRetainCacheLinks.md)
 - [MsgVpnMqttRetainCacheResponse](docs/MsgVpnMqttRetainCacheResponse.md)
 - [MsgVpnMqttRetainCachesResponse](docs/MsgVpnMqttRetainCachesResponse.md)
 - [MsgVpnMqttSession](docs/MsgVpnMqttSession.md)
 - [MsgVpnMqttSessionLinks](docs/MsgVpnMqttSessionLinks.md)
 - [MsgVpnMqttSessionResponse](docs/MsgVpnMqttSessionResponse.md)
 - [MsgVpnMqttSessionSubscription](docs/MsgVpnMqttSessionSubscription.md)
 - [MsgVpnMqttSessionSubscriptionLinks](docs/MsgVpnMqttSessionSubscriptionLinks.md)
 - [MsgVpnMqttSessionSubscriptionResponse](docs/MsgVpnMqttSessionSubscriptionResponse.md)
 - [MsgVpnMqttSessionSubscriptionsResponse](docs/MsgVpnMqttSessionSubscriptionsResponse.md)
 - [MsgVpnMqttSessionsResponse](docs/MsgVpnMqttSessionsResponse.md)
 - [MsgVpnQueue](docs/MsgVpnQueue.md)
 - [MsgVpnQueueLinks](docs/MsgVpnQueueLinks.md)
 - [MsgVpnQueueResponse](docs/MsgVpnQueueResponse.md)
 - [MsgVpnQueueSubscription](docs/MsgVpnQueueSubscription.md)
 - [MsgVpnQueueSubscriptionLinks](docs/MsgVpnQueueSubscriptionLinks.md)
 - [MsgVpnQueueSubscriptionResponse](docs/MsgVpnQueueSubscriptionResponse.md)
 - [MsgVpnQueueSubscriptionsResponse](docs/MsgVpnQueueSubscriptionsResponse.md)
 - [MsgVpnQueueTemplate](docs/MsgVpnQueueTemplate.md)
 - [MsgVpnQueueTemplateLinks](docs/MsgVpnQueueTemplateLinks.md)
 - [MsgVpnQueueTemplateResponse](docs/MsgVpnQueueTemplateResponse.md)
 - [MsgVpnQueueTemplatesResponse](docs/MsgVpnQueueTemplatesResponse.md)
 - [MsgVpnQueuesResponse](docs/MsgVpnQueuesResponse.md)
 - [MsgVpnReplayLog](docs/MsgVpnReplayLog.md)
 - [MsgVpnReplayLogLinks](docs/MsgVpnReplayLogLinks.md)
 - [MsgVpnReplayLogResponse](docs/MsgVpnReplayLogResponse.md)
 - [MsgVpnReplayLogTopicFilterSubscription](docs/MsgVpnReplayLogTopicFilterSubscription.md)
 - [MsgVpnReplayLogTopicFilterSubscriptionLinks](docs/MsgVpnReplayLogTopicFilterSubscriptionLinks.md)
 - [MsgVpnReplayLogTopicFilterSubscriptionResponse](docs/MsgVpnReplayLogTopicFilterSubscriptionResponse.md)
 - [MsgVpnReplayLogTopicFilterSubscriptionsResponse](docs/MsgVpnReplayLogTopicFilterSubscriptionsResponse.md)
 - [MsgVpnReplayLogsResponse](docs/MsgVpnReplayLogsResponse.md)
 - [MsgVpnReplicatedTopic](docs/MsgVpnReplicatedTopic.md)
 - [MsgVpnReplicatedTopicLinks](docs/MsgVpnReplicatedTopicLinks.md)
 - [MsgVpnReplicatedTopicResponse](docs/MsgVpnReplicatedTopicResponse.md)
 - [MsgVpnReplicatedTopicsResponse](docs/MsgVpnReplicatedTopicsResponse.md)
 - [MsgVpnResponse](docs/MsgVpnResponse.md)
 - [MsgVpnRestDeliveryPoint](docs/MsgVpnRestDeliveryPoint.md)
 - [MsgVpnRestDeliveryPointLinks](docs/MsgVpnRestDeliveryPointLinks.md)
 - [MsgVpnRestDeliveryPointQueueBinding](docs/MsgVpnRestDeliveryPointQueueBinding.md)
 - [MsgVpnRestDeliveryPointQueueBindingLinks](docs/MsgVpnRestDeliveryPointQueueBindingLinks.md)
 - [MsgVpnRestDeliveryPointQueueBindingRequestHeader](docs/MsgVpnRestDeliveryPointQueueBindingRequestHeader.md)
 - [MsgVpnRestDeliveryPointQueueBindingRequestHeaderLinks](docs/MsgVpnRestDeliveryPointQueueBindingRequestHeaderLinks.md)
 - [MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse](docs/MsgVpnRestDeliveryPointQueueBindingRequestHeaderResponse.md)
 - [MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse](docs/MsgVpnRestDeliveryPointQueueBindingRequestHeadersResponse.md)
 - [MsgVpnRestDeliveryPointQueueBindingResponse](docs/MsgVpnRestDeliveryPointQueueBindingResponse.md)
 - [MsgVpnRestDeliveryPointQueueBindingsResponse](docs/MsgVpnRestDeliveryPointQueueBindingsResponse.md)
 - [MsgVpnRestDeliveryPointResponse](docs/MsgVpnRestDeliveryPointResponse.md)
 - [MsgVpnRestDeliveryPointRestConsumer](docs/MsgVpnRestDeliveryPointRestConsumer.md)
 - [MsgVpnRestDeliveryPointRestConsumerLinks](docs/MsgVpnRestDeliveryPointRestConsumerLinks.md)
 - [MsgVpnRestDeliveryPointRestConsumerOauthJwtClaim](docs/MsgVpnRestDeliveryPointRestConsumerOauthJwtClaim.md)
 - [MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimLinks](docs/MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimLinks.md)
 - [MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse](docs/MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimResponse.md)
 - [MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse](docs/MsgVpnRestDeliveryPointRestConsumerOauthJwtClaimsResponse.md)
 - [MsgVpnRestDeliveryPointRestConsumerResponse](docs/MsgVpnRestDeliveryPointRestConsumerResponse.md)
 - [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName](docs/MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName.md)
 - [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameLinks](docs/MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameLinks.md)
 - [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse](docs/MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse.md)
 - [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse](docs/MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse.md)
 - [MsgVpnRestDeliveryPointRestConsumersResponse](docs/MsgVpnRestDeliveryPointRestConsumersResponse.md)
 - [MsgVpnRestDeliveryPointsResponse](docs/MsgVpnRestDeliveryPointsResponse.md)
 - [MsgVpnSequencedTopic](docs/MsgVpnSequencedTopic.md)
 - [MsgVpnSequencedTopicLinks](docs/MsgVpnSequencedTopicLinks.md)
 - [MsgVpnSequencedTopicResponse](docs/MsgVpnSequencedTopicResponse.md)
 - [MsgVpnSequencedTopicsResponse](docs/MsgVpnSequencedTopicsResponse.md)
 - [MsgVpnTopicEndpoint](docs/MsgVpnTopicEndpoint.md)
 - [MsgVpnTopicEndpointLinks](docs/MsgVpnTopicEndpointLinks.md)
 - [MsgVpnTopicEndpointResponse](docs/MsgVpnTopicEndpointResponse.md)
 - [MsgVpnTopicEndpointTemplate](docs/MsgVpnTopicEndpointTemplate.md)
 - [MsgVpnTopicEndpointTemplateLinks](docs/MsgVpnTopicEndpointTemplateLinks.md)
 - [MsgVpnTopicEndpointTemplateResponse](docs/MsgVpnTopicEndpointTemplateResponse.md)
 - [MsgVpnTopicEndpointTemplatesResponse](docs/MsgVpnTopicEndpointTemplatesResponse.md)
 - [MsgVpnTopicEndpointsResponse](docs/MsgVpnTopicEndpointsResponse.md)
 - [MsgVpnsResponse](docs/MsgVpnsResponse.md)
 - [OauthProfile](docs/OauthProfile.md)
 - [OauthProfileAccessLevelGroup](docs/OauthProfileAccessLevelGroup.md)
 - [OauthProfileAccessLevelGroupLinks](docs/OauthProfileAccessLevelGroupLinks.md)
 - [OauthProfileAccessLevelGroupMsgVpnAccessLevelException](docs/OauthProfileAccessLevelGroupMsgVpnAccessLevelException.md)
 - [OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionLinks](docs/OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionLinks.md)
 - [OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse](docs/OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse.md)
 - [OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse](docs/OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse.md)
 - [OauthProfileAccessLevelGroupResponse](docs/OauthProfileAccessLevelGroupResponse.md)
 - [OauthProfileAccessLevelGroupsResponse](docs/OauthProfileAccessLevelGroupsResponse.md)
 - [OauthProfileClientAllowedHost](docs/OauthProfileClientAllowedHost.md)
 - [OauthProfileClientAllowedHostLinks](docs/OauthProfileClientAllowedHostLinks.md)
 - [OauthProfileClientAllowedHostResponse](docs/OauthProfileClientAllowedHostResponse.md)
 - [OauthProfileClientAllowedHostsResponse](docs/OauthProfileClientAllowedHostsResponse.md)
 - [OauthProfileClientAuthorizationParameter](docs/OauthProfileClientAuthorizationParameter.md)
 - [OauthProfileClientAuthorizationParameterLinks](docs/OauthProfileClientAuthorizationParameterLinks.md)
 - [OauthProfileClientAuthorizationParameterResponse](docs/OauthProfileClientAuthorizationParameterResponse.md)
 - [OauthProfileClientAuthorizationParametersResponse](docs/OauthProfileClientAuthorizationParametersResponse.md)
 - [OauthProfileClientRequiredClaim](docs/OauthProfileClientRequiredClaim.md)
 - [OauthProfileClientRequiredClaimLinks](docs/OauthProfileClientRequiredClaimLinks.md)
 - [OauthProfileClientRequiredClaimResponse](docs/OauthProfileClientRequiredClaimResponse.md)
 - [OauthProfileClientRequiredClaimsResponse](docs/OauthProfileClientRequiredClaimsResponse.md)
 - [OauthProfileDefaultMsgVpnAccessLevelException](docs/OauthProfileDefaultMsgVpnAccessLevelException.md)
 - [OauthProfileDefaultMsgVpnAccessLevelExceptionLinks](docs/OauthProfileDefaultMsgVpnAccessLevelExceptionLinks.md)
 - [OauthProfileDefaultMsgVpnAccessLevelExceptionResponse](docs/OauthProfileDefaultMsgVpnAccessLevelExceptionResponse.md)
 - [OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse](docs/OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse.md)
 - [OauthProfileLinks](docs/OauthProfileLinks.md)
 - [OauthProfileResourceServerRequiredClaim](docs/OauthProfileResourceServerRequiredClaim.md)
 - [OauthProfileResourceServerRequiredClaimLinks](docs/OauthProfileResourceServerRequiredClaimLinks.md)
 - [OauthProfileResourceServerRequiredClaimResponse](docs/OauthProfileResourceServerRequiredClaimResponse.md)
 - [OauthProfileResourceServerRequiredClaimsResponse](docs/OauthProfileResourceServerRequiredClaimsResponse.md)
 - [OauthProfileResponse](docs/OauthProfileResponse.md)
 - [OauthProfilesResponse](docs/OauthProfilesResponse.md)
 - [SempError](docs/SempError.md)
 - [SempMeta](docs/SempMeta.md)
 - [SempMetaOnlyResponse](docs/SempMetaOnlyResponse.md)
 - [SempPaging](docs/SempPaging.md)
 - [SempRequest](docs/SempRequest.md)
 - [SystemInformation](docs/SystemInformation.md)
 - [SystemInformationLinks](docs/SystemInformationLinks.md)
 - [SystemInformationResponse](docs/SystemInformationResponse.md)
 - [VirtualHostname](docs/VirtualHostname.md)
 - [VirtualHostnameLinks](docs/VirtualHostnameLinks.md)
 - [VirtualHostnameResponse](docs/VirtualHostnameResponse.md)
 - [VirtualHostnamesResponse](docs/VirtualHostnamesResponse.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### basicAuth


- **Type**: HTTP basic authentication


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

support@solace.com

