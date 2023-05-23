/*
 * SEMP (Solace Element Management Protocol)
 * SEMP (starting in `v2`, see note 1) is a RESTful API for configuring, monitoring, and administering a Solace PubSub+ broker.  SEMP uses URIs to address manageable **resources** of the Solace PubSub+ broker. Resources are individual **objects**, **collections** of objects, or (exclusively in the action API) **actions**. This document applies to the following API:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See note 2    The following APIs are also available:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Action|/SEMP/v2/action|Performing actions|See note 2 Monitoring|/SEMP/v2/monitor|Querying operational parameters|See note 2    Resources are always nouns, with individual objects being singular and collections being plural.  Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`.  Actions within an object are identified by an `action-id`, which follows the object name with the form `obj-id/action-id`.  Some examples:  ``` /SEMP/v2/config/msgVpns                        ; MsgVpn collection /SEMP/v2/config/msgVpns/a                      ; MsgVpn object named \"a\" /SEMP/v2/config/msgVpns/a/queues               ; Queue collection in MsgVpn \"a\" /SEMP/v2/config/msgVpns/a/queues/b             ; Queue object named \"b\" in MsgVpn \"a\" /SEMP/v2/action/msgVpns/a/queues/b/startReplay ; Action that starts a replay on Queue \"b\" in MsgVpn \"a\" /SEMP/v2/monitor/msgVpns/a/clients             ; Client collection in MsgVpn \"a\" /SEMP/v2/monitor/msgVpns/a/clients/c           ; Client object named \"c\" in MsgVpn \"a\" ```  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. In the configuration API, the creation of a new object is done through its collection resource.  ## Object and Action Resources  Objects are composed of attributes, actions, collections, and other objects. They are described by JSON objects as name/value pairs. The collections and actions of an object are not contained directly in the object's JSON content; rather the content includes an attribute containing a URI which points to the collections and actions. These contained resources must be managed through this URI. At a minimum, every object has one or more identifying attributes, and its own `uri` attribute which contains the URI pointing to itself.  Actions are also composed of attributes, and are described by JSON objects as name/value pairs. Unlike objects, however, they are not members of a collection and cannot be retrieved, only performed. Actions only exist in the action API.  Attributes in an object or action may have any combination of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Const|Attribute value can only be chosen during object creation| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written.|See note 3 Write-Only|Attribute can only be written, not read, unless the attribute is also opaque|See the documentation for the opaque property Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version| Opaque|Attribute can be set or retrieved in opaque form when the `opaquePassword` query parameter is present|See the `opaquePassword` query parameter documentation    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    In the monitoring API, any non-identifying attribute may not be returned in a GET.  ## HTTP Methods  The following HTTP methods manipulate resources in accordance with these general principles. Note that some methods are only used in certain APIs:   Method|Resource|Meaning|Request Body|Response Body|Notes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Absent attributes are set to default. If object already exists, a 400 error is returned PUT|Object|Update object|New attribute values|Object attributes and metadata|If does not exist, the object is first created. Absent attributes are set to default, with certain exceptions (see note 4) PUT|Action|Performs action|Action arguments|Action metadata| PATCH|Object|Update object|New attribute values|Object attributes and metadata|Absent attributes are left unchanged. If the object does not exist, a 404 error is returned DELETE|Object|Delete object|Empty|Object metadata|If the object does not exist, a 404 is returned GET|Object|Get object|Empty|Object attributes and metadata|If the object does not exist, a 404 is returned GET|Collection|Get collection|Empty|Object attributes and collection metadata|If the collection is empty, then an empty collection is returned with a 200 code    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  ``` ; Request for the MsgVpns collection using two hypothetical query parameters ; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 ```  ### select  Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, return only those fields that are desired, or exclude fields that are not desired.  The value of `select` is a comma-separated list of attribute names. If the list contains attribute names that are not prefaced by `-`, only those attributes are included in the response. If the list contains attribute names that are prefaced by `-`, those attributes are excluded from the response. If the list contains both types, then the difference of the first set of attributes and the second set of attributes is returned. If the list is empty (i.e. `select=`), it is treated the same as if no `select` was provided: all attribute are returned.  All attributes that are prefaced by `-` must follow all attributes that are not prefaced by `-`. In addition, each attribute name in the list must match at least one attribute in the object.  Names may include the `*` wildcard (zero or more characters). Nested attribute names are supported using periods (e.g. `parentName.childName`).  Some examples:  ``` ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName ; List of all MsgVpn and their attributes except for their names /SEMP/v2/config/msgVpns?select=-msgVpnName ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication* ; All attributes of MsgVpn \"finance\" except for authentication attributes /SEMP/v2/config/msgVpns/finance?select=-authentication* ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission ```  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  ``` expression  = attribute-name OP value OP          = '==' | '!=' | '&lt;' | '&gt;' | '&lt;=' | '&gt;=' ```  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard (zero or more characters). Some examples:  ``` ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 ; Only MsgVpns with msgVpnName starting with \"B\": /SEMP/v2/config/msgVpns?where=msgVpnName==B* ```  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. There is also a per-collection maximum value to limit request handling time.  `count` does not guarantee that a minimum number of objects will be returned. A page may contain fewer than `count` objects or even be empty. Additional objects may nonetheless be available for retrieval on subsequent pages. See the `cursor` query parameter documentation for more information on paging.  For example: ``` ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 ```  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects.  Applications must continue to use the `cursorQuery` if one is provided in order to retrieve the full set of objects associated with the request, even if a page contains fewer than the requested number of objects (see the `count` query parameter documentation) or is empty.  ### opaquePassword  Attributes with the opaque property are also write-only and so cannot normally be retrieved in a GET. However, when a password is provided in the `opaquePassword` query parameter, attributes with the opaque property are retrieved in a GET in opaque form, encrypted with this password. The query parameter can also be used on a POST, PATCH, or PUT to set opaque attributes using opaque attribute values retrieved in a GET, so long as:  1. the same password that was used to retrieve the opaque attribute values is provided; and  2. the broker to which the request is being sent has the same major and minor SEMP version as the broker that produced the opaque attribute values.  The password provided in the query parameter must be a minimum of 8 characters and a maximum of 128 characters.  The query parameter can only be used in the configuration API, and only over HTTPS.  ## Authentication  When a client makes its first SEMPv2 request, it must supply a username and password using HTTP Basic authentication, or an OAuth token or tokens using HTTP Bearer authentication.  When HTTP Basic authentication is used, the broker returns a cookie containing a session key. The client can omit the username and password from subsequent requests, because the broker can use the session cookie for authentication instead. When the session expires or is deleted, the client must provide the username and password again, and the broker creates a new session.  There are a limited number of session slots available on the broker. The broker returns 529 No SEMP Session Available if it is not able to allocate a session.  If certain attributes—such as a user's password—are changed, the broker automatically deletes the affected sessions. These attributes are documented below. However, changes in external user configuration data stored on a RADIUS or LDAP server do not trigger the broker to delete the associated session(s), therefore you must do this manually, if required.  A client can retrieve its current session information using the /about/user endpoint and delete its own session using the /about/user/logout endpoint. A client with appropriate permissions can also manage all sessions using the /sessions endpoint.  Sessions are not created when authenticating with an OAuth token or tokens using HTTP Bearer authentication. If a session cookie is provided, it is ignored.  ## Help  Visit [our website](https://solace.com) to learn more about Solace.  You can also download the SEMP API specifications by clicking [here](https://solace.com/downloads/).  If you need additional support, please contact us at [support@solace.com](mailto:support@solace.com).  ## Notes  Note|Description :---:|:--- 1|This specification defines SEMP starting in \"v2\", and not the original SEMP \"v1\" interface. Request and response formats between \"v1\" and \"v2\" are entirely incompatible, although both protocols share a common port configuration on the Solace PubSub+ broker. They are differentiated by the initial portion of the URI path, one of either \"/SEMP/\" or \"/SEMP/v2/\" 2|This API is partially implemented. Only a subset of all objects are available. 3|Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4|On a PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT, except in the following two cases: there is a mutual requires relationship with another non-write-only attribute, both attributes are absent from the request, and the non-write-only attribute is not currently set to its default value; or the attribute is also opaque and the `opaquePassword` query parameter is provided in the request.  
 *
 * The version of the OpenAPI document: 2.28
 * Contact: support@solace.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.EventThreshold;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.EventThresholdByValue;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Model tests for MsgVpn
 */
public class MsgVpnTest {
    private final MsgVpn model = new MsgVpn();

    /**
     * Model tests for MsgVpn
     */
    @Test
    public void testMsgVpn() {
        // TODO: test MsgVpn
    }

    /**
     * Test the property 'alias'
     */
    @Test
    public void aliasTest() {
        // TODO: test alias
    }

    /**
     * Test the property 'authenticationBasicEnabled'
     */
    @Test
    public void authenticationBasicEnabledTest() {
        // TODO: test authenticationBasicEnabled
    }

    /**
     * Test the property 'authenticationBasicProfileName'
     */
    @Test
    public void authenticationBasicProfileNameTest() {
        // TODO: test authenticationBasicProfileName
    }

    /**
     * Test the property 'authenticationBasicRadiusDomain'
     */
    @Test
    public void authenticationBasicRadiusDomainTest() {
        // TODO: test authenticationBasicRadiusDomain
    }

    /**
     * Test the property 'authenticationBasicType'
     */
    @Test
    public void authenticationBasicTypeTest() {
        // TODO: test authenticationBasicType
    }

    /**
     * Test the property 'authenticationClientCertAllowApiProvidedUsernameEnabled'
     */
    @Test
    public void authenticationClientCertAllowApiProvidedUsernameEnabledTest() {
        // TODO: test authenticationClientCertAllowApiProvidedUsernameEnabled
    }

    /**
     * Test the property 'authenticationClientCertCertificateMatchingRulesEnabled'
     */
    @Test
    public void authenticationClientCertCertificateMatchingRulesEnabledTest() {
        // TODO: test authenticationClientCertCertificateMatchingRulesEnabled
    }

    /**
     * Test the property 'authenticationClientCertEnabled'
     */
    @Test
    public void authenticationClientCertEnabledTest() {
        // TODO: test authenticationClientCertEnabled
    }

    /**
     * Test the property 'authenticationClientCertMaxChainDepth'
     */
    @Test
    public void authenticationClientCertMaxChainDepthTest() {
        // TODO: test authenticationClientCertMaxChainDepth
    }

    /**
     * Test the property 'authenticationClientCertRevocationCheckMode'
     */
    @Test
    public void authenticationClientCertRevocationCheckModeTest() {
        // TODO: test authenticationClientCertRevocationCheckMode
    }

    /**
     * Test the property 'authenticationClientCertUsernameSource'
     */
    @Test
    public void authenticationClientCertUsernameSourceTest() {
        // TODO: test authenticationClientCertUsernameSource
    }

    /**
     * Test the property 'authenticationClientCertValidateDateEnabled'
     */
    @Test
    public void authenticationClientCertValidateDateEnabledTest() {
        // TODO: test authenticationClientCertValidateDateEnabled
    }

    /**
     * Test the property 'authenticationKerberosAllowApiProvidedUsernameEnabled'
     */
    @Test
    public void authenticationKerberosAllowApiProvidedUsernameEnabledTest() {
        // TODO: test authenticationKerberosAllowApiProvidedUsernameEnabled
    }

    /**
     * Test the property 'authenticationKerberosEnabled'
     */
    @Test
    public void authenticationKerberosEnabledTest() {
        // TODO: test authenticationKerberosEnabled
    }

    /**
     * Test the property 'authenticationOauthDefaultProfileName'
     */
    @Test
    public void authenticationOauthDefaultProfileNameTest() {
        // TODO: test authenticationOauthDefaultProfileName
    }

    /**
     * Test the property 'authenticationOauthDefaultProviderName'
     */
    @Test
    public void authenticationOauthDefaultProviderNameTest() {
        // TODO: test authenticationOauthDefaultProviderName
    }

    /**
     * Test the property 'authenticationOauthEnabled'
     */
    @Test
    public void authenticationOauthEnabledTest() {
        // TODO: test authenticationOauthEnabled
    }

    /**
     * Test the property 'authorizationLdapGroupMembershipAttributeName'
     */
    @Test
    public void authorizationLdapGroupMembershipAttributeNameTest() {
        // TODO: test authorizationLdapGroupMembershipAttributeName
    }

    /**
     * Test the property 'authorizationLdapTrimClientUsernameDomainEnabled'
     */
    @Test
    public void authorizationLdapTrimClientUsernameDomainEnabledTest() {
        // TODO: test authorizationLdapTrimClientUsernameDomainEnabled
    }

    /**
     * Test the property 'authorizationProfileName'
     */
    @Test
    public void authorizationProfileNameTest() {
        // TODO: test authorizationProfileName
    }

    /**
     * Test the property 'authorizationType'
     */
    @Test
    public void authorizationTypeTest() {
        // TODO: test authorizationType
    }

    /**
     * Test the property 'bridgingTlsServerCertEnforceTrustedCommonNameEnabled'
     */
    @Test
    public void bridgingTlsServerCertEnforceTrustedCommonNameEnabledTest() {
        // TODO: test bridgingTlsServerCertEnforceTrustedCommonNameEnabled
    }

    /**
     * Test the property 'bridgingTlsServerCertMaxChainDepth'
     */
    @Test
    public void bridgingTlsServerCertMaxChainDepthTest() {
        // TODO: test bridgingTlsServerCertMaxChainDepth
    }

    /**
     * Test the property 'bridgingTlsServerCertValidateDateEnabled'
     */
    @Test
    public void bridgingTlsServerCertValidateDateEnabledTest() {
        // TODO: test bridgingTlsServerCertValidateDateEnabled
    }

    /**
     * Test the property 'bridgingTlsServerCertValidateNameEnabled'
     */
    @Test
    public void bridgingTlsServerCertValidateNameEnabledTest() {
        // TODO: test bridgingTlsServerCertValidateNameEnabled
    }

    /**
     * Test the property 'distributedCacheManagementEnabled'
     */
    @Test
    public void distributedCacheManagementEnabledTest() {
        // TODO: test distributedCacheManagementEnabled
    }

    /**
     * Test the property 'dmrEnabled'
     */
    @Test
    public void dmrEnabledTest() {
        // TODO: test dmrEnabled
    }

    /**
     * Test the property 'enabled'
     */
    @Test
    public void enabledTest() {
        // TODO: test enabled
    }

    /**
     * Test the property 'eventConnectionCountThreshold'
     */
    @Test
    public void eventConnectionCountThresholdTest() {
        // TODO: test eventConnectionCountThreshold
    }

    /**
     * Test the property 'eventEgressFlowCountThreshold'
     */
    @Test
    public void eventEgressFlowCountThresholdTest() {
        // TODO: test eventEgressFlowCountThreshold
    }

    /**
     * Test the property 'eventEgressMsgRateThreshold'
     */
    @Test
    public void eventEgressMsgRateThresholdTest() {
        // TODO: test eventEgressMsgRateThreshold
    }

    /**
     * Test the property 'eventEndpointCountThreshold'
     */
    @Test
    public void eventEndpointCountThresholdTest() {
        // TODO: test eventEndpointCountThreshold
    }

    /**
     * Test the property 'eventIngressFlowCountThreshold'
     */
    @Test
    public void eventIngressFlowCountThresholdTest() {
        // TODO: test eventIngressFlowCountThreshold
    }

    /**
     * Test the property 'eventIngressMsgRateThreshold'
     */
    @Test
    public void eventIngressMsgRateThresholdTest() {
        // TODO: test eventIngressMsgRateThreshold
    }

    /**
     * Test the property 'eventLargeMsgThreshold'
     */
    @Test
    public void eventLargeMsgThresholdTest() {
        // TODO: test eventLargeMsgThreshold
    }

    /**
     * Test the property 'eventLogTag'
     */
    @Test
    public void eventLogTagTest() {
        // TODO: test eventLogTag
    }

    /**
     * Test the property 'eventMsgSpoolUsageThreshold'
     */
    @Test
    public void eventMsgSpoolUsageThresholdTest() {
        // TODO: test eventMsgSpoolUsageThreshold
    }

    /**
     * Test the property 'eventPublishClientEnabled'
     */
    @Test
    public void eventPublishClientEnabledTest() {
        // TODO: test eventPublishClientEnabled
    }

    /**
     * Test the property 'eventPublishMsgVpnEnabled'
     */
    @Test
    public void eventPublishMsgVpnEnabledTest() {
        // TODO: test eventPublishMsgVpnEnabled
    }

    /**
     * Test the property 'eventPublishSubscriptionMode'
     */
    @Test
    public void eventPublishSubscriptionModeTest() {
        // TODO: test eventPublishSubscriptionMode
    }

    /**
     * Test the property 'eventPublishTopicFormatMqttEnabled'
     */
    @Test
    public void eventPublishTopicFormatMqttEnabledTest() {
        // TODO: test eventPublishTopicFormatMqttEnabled
    }

    /**
     * Test the property 'eventPublishTopicFormatSmfEnabled'
     */
    @Test
    public void eventPublishTopicFormatSmfEnabledTest() {
        // TODO: test eventPublishTopicFormatSmfEnabled
    }

    /**
     * Test the property 'eventServiceAmqpConnectionCountThreshold'
     */
    @Test
    public void eventServiceAmqpConnectionCountThresholdTest() {
        // TODO: test eventServiceAmqpConnectionCountThreshold
    }

    /**
     * Test the property 'eventServiceMqttConnectionCountThreshold'
     */
    @Test
    public void eventServiceMqttConnectionCountThresholdTest() {
        // TODO: test eventServiceMqttConnectionCountThreshold
    }

    /**
     * Test the property 'eventServiceRestIncomingConnectionCountThreshold'
     */
    @Test
    public void eventServiceRestIncomingConnectionCountThresholdTest() {
        // TODO: test eventServiceRestIncomingConnectionCountThreshold
    }

    /**
     * Test the property 'eventServiceSmfConnectionCountThreshold'
     */
    @Test
    public void eventServiceSmfConnectionCountThresholdTest() {
        // TODO: test eventServiceSmfConnectionCountThreshold
    }

    /**
     * Test the property 'eventServiceWebConnectionCountThreshold'
     */
    @Test
    public void eventServiceWebConnectionCountThresholdTest() {
        // TODO: test eventServiceWebConnectionCountThreshold
    }

    /**
     * Test the property 'eventSubscriptionCountThreshold'
     */
    @Test
    public void eventSubscriptionCountThresholdTest() {
        // TODO: test eventSubscriptionCountThreshold
    }

    /**
     * Test the property 'eventTransactedSessionCountThreshold'
     */
    @Test
    public void eventTransactedSessionCountThresholdTest() {
        // TODO: test eventTransactedSessionCountThreshold
    }

    /**
     * Test the property 'eventTransactionCountThreshold'
     */
    @Test
    public void eventTransactionCountThresholdTest() {
        // TODO: test eventTransactionCountThreshold
    }

    /**
     * Test the property 'exportSubscriptionsEnabled'
     */
    @Test
    public void exportSubscriptionsEnabledTest() {
        // TODO: test exportSubscriptionsEnabled
    }

    /**
     * Test the property 'jndiEnabled'
     */
    @Test
    public void jndiEnabledTest() {
        // TODO: test jndiEnabled
    }

    /**
     * Test the property 'maxConnectionCount'
     */
    @Test
    public void maxConnectionCountTest() {
        // TODO: test maxConnectionCount
    }

    /**
     * Test the property 'maxEgressFlowCount'
     */
    @Test
    public void maxEgressFlowCountTest() {
        // TODO: test maxEgressFlowCount
    }

    /**
     * Test the property 'maxEndpointCount'
     */
    @Test
    public void maxEndpointCountTest() {
        // TODO: test maxEndpointCount
    }

    /**
     * Test the property 'maxIngressFlowCount'
     */
    @Test
    public void maxIngressFlowCountTest() {
        // TODO: test maxIngressFlowCount
    }

    /**
     * Test the property 'maxMsgSpoolUsage'
     */
    @Test
    public void maxMsgSpoolUsageTest() {
        // TODO: test maxMsgSpoolUsage
    }

    /**
     * Test the property 'maxSubscriptionCount'
     */
    @Test
    public void maxSubscriptionCountTest() {
        // TODO: test maxSubscriptionCount
    }

    /**
     * Test the property 'maxTransactedSessionCount'
     */
    @Test
    public void maxTransactedSessionCountTest() {
        // TODO: test maxTransactedSessionCount
    }

    /**
     * Test the property 'maxTransactionCount'
     */
    @Test
    public void maxTransactionCountTest() {
        // TODO: test maxTransactionCount
    }

    /**
     * Test the property 'mqttRetainMaxMemory'
     */
    @Test
    public void mqttRetainMaxMemoryTest() {
        // TODO: test mqttRetainMaxMemory
    }

    /**
     * Test the property 'msgVpnName'
     */
    @Test
    public void msgVpnNameTest() {
        // TODO: test msgVpnName
    }

    /**
     * Test the property 'replicationAckPropagationIntervalMsgCount'
     */
    @Test
    public void replicationAckPropagationIntervalMsgCountTest() {
        // TODO: test replicationAckPropagationIntervalMsgCount
    }

    /**
     * Test the property 'replicationBridgeAuthenticationBasicClientUsername'
     */
    @Test
    public void replicationBridgeAuthenticationBasicClientUsernameTest() {
        // TODO: test replicationBridgeAuthenticationBasicClientUsername
    }

    /**
     * Test the property 'replicationBridgeAuthenticationBasicPassword'
     */
    @Test
    public void replicationBridgeAuthenticationBasicPasswordTest() {
        // TODO: test replicationBridgeAuthenticationBasicPassword
    }

    /**
     * Test the property 'replicationBridgeAuthenticationClientCertContent'
     */
    @Test
    public void replicationBridgeAuthenticationClientCertContentTest() {
        // TODO: test replicationBridgeAuthenticationClientCertContent
    }

    /**
     * Test the property 'replicationBridgeAuthenticationClientCertPassword'
     */
    @Test
    public void replicationBridgeAuthenticationClientCertPasswordTest() {
        // TODO: test replicationBridgeAuthenticationClientCertPassword
    }

    /**
     * Test the property 'replicationBridgeAuthenticationScheme'
     */
    @Test
    public void replicationBridgeAuthenticationSchemeTest() {
        // TODO: test replicationBridgeAuthenticationScheme
    }

    /**
     * Test the property 'replicationBridgeCompressedDataEnabled'
     */
    @Test
    public void replicationBridgeCompressedDataEnabledTest() {
        // TODO: test replicationBridgeCompressedDataEnabled
    }

    /**
     * Test the property 'replicationBridgeEgressFlowWindowSize'
     */
    @Test
    public void replicationBridgeEgressFlowWindowSizeTest() {
        // TODO: test replicationBridgeEgressFlowWindowSize
    }

    /**
     * Test the property 'replicationBridgeRetryDelay'
     */
    @Test
    public void replicationBridgeRetryDelayTest() {
        // TODO: test replicationBridgeRetryDelay
    }

    /**
     * Test the property 'replicationBridgeTlsEnabled'
     */
    @Test
    public void replicationBridgeTlsEnabledTest() {
        // TODO: test replicationBridgeTlsEnabled
    }

    /**
     * Test the property 'replicationBridgeUnidirectionalClientProfileName'
     */
    @Test
    public void replicationBridgeUnidirectionalClientProfileNameTest() {
        // TODO: test replicationBridgeUnidirectionalClientProfileName
    }

    /**
     * Test the property 'replicationEnabled'
     */
    @Test
    public void replicationEnabledTest() {
        // TODO: test replicationEnabled
    }

    /**
     * Test the property 'replicationEnabledQueueBehavior'
     */
    @Test
    public void replicationEnabledQueueBehaviorTest() {
        // TODO: test replicationEnabledQueueBehavior
    }

    /**
     * Test the property 'replicationQueueMaxMsgSpoolUsage'
     */
    @Test
    public void replicationQueueMaxMsgSpoolUsageTest() {
        // TODO: test replicationQueueMaxMsgSpoolUsage
    }

    /**
     * Test the property 'replicationQueueRejectMsgToSenderOnDiscardEnabled'
     */
    @Test
    public void replicationQueueRejectMsgToSenderOnDiscardEnabledTest() {
        // TODO: test replicationQueueRejectMsgToSenderOnDiscardEnabled
    }

    /**
     * Test the property 'replicationRejectMsgWhenSyncIneligibleEnabled'
     */
    @Test
    public void replicationRejectMsgWhenSyncIneligibleEnabledTest() {
        // TODO: test replicationRejectMsgWhenSyncIneligibleEnabled
    }

    /**
     * Test the property 'replicationRole'
     */
    @Test
    public void replicationRoleTest() {
        // TODO: test replicationRole
    }

    /**
     * Test the property 'replicationTransactionMode'
     */
    @Test
    public void replicationTransactionModeTest() {
        // TODO: test replicationTransactionMode
    }

    /**
     * Test the property 'restTlsServerCertEnforceTrustedCommonNameEnabled'
     */
    @Test
    public void restTlsServerCertEnforceTrustedCommonNameEnabledTest() {
        // TODO: test restTlsServerCertEnforceTrustedCommonNameEnabled
    }

    /**
     * Test the property 'restTlsServerCertMaxChainDepth'
     */
    @Test
    public void restTlsServerCertMaxChainDepthTest() {
        // TODO: test restTlsServerCertMaxChainDepth
    }

    /**
     * Test the property 'restTlsServerCertValidateDateEnabled'
     */
    @Test
    public void restTlsServerCertValidateDateEnabledTest() {
        // TODO: test restTlsServerCertValidateDateEnabled
    }

    /**
     * Test the property 'restTlsServerCertValidateNameEnabled'
     */
    @Test
    public void restTlsServerCertValidateNameEnabledTest() {
        // TODO: test restTlsServerCertValidateNameEnabled
    }

    /**
     * Test the property 'sempOverMsgBusAdminClientEnabled'
     */
    @Test
    public void sempOverMsgBusAdminClientEnabledTest() {
        // TODO: test sempOverMsgBusAdminClientEnabled
    }

    /**
     * Test the property 'sempOverMsgBusAdminDistributedCacheEnabled'
     */
    @Test
    public void sempOverMsgBusAdminDistributedCacheEnabledTest() {
        // TODO: test sempOverMsgBusAdminDistributedCacheEnabled
    }

    /**
     * Test the property 'sempOverMsgBusAdminEnabled'
     */
    @Test
    public void sempOverMsgBusAdminEnabledTest() {
        // TODO: test sempOverMsgBusAdminEnabled
    }

    /**
     * Test the property 'sempOverMsgBusEnabled'
     */
    @Test
    public void sempOverMsgBusEnabledTest() {
        // TODO: test sempOverMsgBusEnabled
    }

    /**
     * Test the property 'sempOverMsgBusShowEnabled'
     */
    @Test
    public void sempOverMsgBusShowEnabledTest() {
        // TODO: test sempOverMsgBusShowEnabled
    }

    /**
     * Test the property 'serviceAmqpMaxConnectionCount'
     */
    @Test
    public void serviceAmqpMaxConnectionCountTest() {
        // TODO: test serviceAmqpMaxConnectionCount
    }

    /**
     * Test the property 'serviceAmqpPlainTextEnabled'
     */
    @Test
    public void serviceAmqpPlainTextEnabledTest() {
        // TODO: test serviceAmqpPlainTextEnabled
    }

    /**
     * Test the property 'serviceAmqpPlainTextListenPort'
     */
    @Test
    public void serviceAmqpPlainTextListenPortTest() {
        // TODO: test serviceAmqpPlainTextListenPort
    }

    /**
     * Test the property 'serviceAmqpTlsEnabled'
     */
    @Test
    public void serviceAmqpTlsEnabledTest() {
        // TODO: test serviceAmqpTlsEnabled
    }

    /**
     * Test the property 'serviceAmqpTlsListenPort'
     */
    @Test
    public void serviceAmqpTlsListenPortTest() {
        // TODO: test serviceAmqpTlsListenPort
    }

    /**
     * Test the property 'serviceMqttAuthenticationClientCertRequest'
     */
    @Test
    public void serviceMqttAuthenticationClientCertRequestTest() {
        // TODO: test serviceMqttAuthenticationClientCertRequest
    }

    /**
     * Test the property 'serviceMqttMaxConnectionCount'
     */
    @Test
    public void serviceMqttMaxConnectionCountTest() {
        // TODO: test serviceMqttMaxConnectionCount
    }

    /**
     * Test the property 'serviceMqttPlainTextEnabled'
     */
    @Test
    public void serviceMqttPlainTextEnabledTest() {
        // TODO: test serviceMqttPlainTextEnabled
    }

    /**
     * Test the property 'serviceMqttPlainTextListenPort'
     */
    @Test
    public void serviceMqttPlainTextListenPortTest() {
        // TODO: test serviceMqttPlainTextListenPort
    }

    /**
     * Test the property 'serviceMqttTlsEnabled'
     */
    @Test
    public void serviceMqttTlsEnabledTest() {
        // TODO: test serviceMqttTlsEnabled
    }

    /**
     * Test the property 'serviceMqttTlsListenPort'
     */
    @Test
    public void serviceMqttTlsListenPortTest() {
        // TODO: test serviceMqttTlsListenPort
    }

    /**
     * Test the property 'serviceMqttTlsWebSocketEnabled'
     */
    @Test
    public void serviceMqttTlsWebSocketEnabledTest() {
        // TODO: test serviceMqttTlsWebSocketEnabled
    }

    /**
     * Test the property 'serviceMqttTlsWebSocketListenPort'
     */
    @Test
    public void serviceMqttTlsWebSocketListenPortTest() {
        // TODO: test serviceMqttTlsWebSocketListenPort
    }

    /**
     * Test the property 'serviceMqttWebSocketEnabled'
     */
    @Test
    public void serviceMqttWebSocketEnabledTest() {
        // TODO: test serviceMqttWebSocketEnabled
    }

    /**
     * Test the property 'serviceMqttWebSocketListenPort'
     */
    @Test
    public void serviceMqttWebSocketListenPortTest() {
        // TODO: test serviceMqttWebSocketListenPort
    }

    /**
     * Test the property 'serviceRestIncomingAuthenticationClientCertRequest'
     */
    @Test
    public void serviceRestIncomingAuthenticationClientCertRequestTest() {
        // TODO: test serviceRestIncomingAuthenticationClientCertRequest
    }

    /**
     * Test the property 'serviceRestIncomingAuthorizationHeaderHandling'
     */
    @Test
    public void serviceRestIncomingAuthorizationHeaderHandlingTest() {
        // TODO: test serviceRestIncomingAuthorizationHeaderHandling
    }

    /**
     * Test the property 'serviceRestIncomingMaxConnectionCount'
     */
    @Test
    public void serviceRestIncomingMaxConnectionCountTest() {
        // TODO: test serviceRestIncomingMaxConnectionCount
    }

    /**
     * Test the property 'serviceRestIncomingPlainTextEnabled'
     */
    @Test
    public void serviceRestIncomingPlainTextEnabledTest() {
        // TODO: test serviceRestIncomingPlainTextEnabled
    }

    /**
     * Test the property 'serviceRestIncomingPlainTextListenPort'
     */
    @Test
    public void serviceRestIncomingPlainTextListenPortTest() {
        // TODO: test serviceRestIncomingPlainTextListenPort
    }

    /**
     * Test the property 'serviceRestIncomingTlsEnabled'
     */
    @Test
    public void serviceRestIncomingTlsEnabledTest() {
        // TODO: test serviceRestIncomingTlsEnabled
    }

    /**
     * Test the property 'serviceRestIncomingTlsListenPort'
     */
    @Test
    public void serviceRestIncomingTlsListenPortTest() {
        // TODO: test serviceRestIncomingTlsListenPort
    }

    /**
     * Test the property 'serviceRestMode'
     */
    @Test
    public void serviceRestModeTest() {
        // TODO: test serviceRestMode
    }

    /**
     * Test the property 'serviceRestOutgoingMaxConnectionCount'
     */
    @Test
    public void serviceRestOutgoingMaxConnectionCountTest() {
        // TODO: test serviceRestOutgoingMaxConnectionCount
    }

    /**
     * Test the property 'serviceSmfMaxConnectionCount'
     */
    @Test
    public void serviceSmfMaxConnectionCountTest() {
        // TODO: test serviceSmfMaxConnectionCount
    }

    /**
     * Test the property 'serviceSmfPlainTextEnabled'
     */
    @Test
    public void serviceSmfPlainTextEnabledTest() {
        // TODO: test serviceSmfPlainTextEnabled
    }

    /**
     * Test the property 'serviceSmfTlsEnabled'
     */
    @Test
    public void serviceSmfTlsEnabledTest() {
        // TODO: test serviceSmfTlsEnabled
    }

    /**
     * Test the property 'serviceWebAuthenticationClientCertRequest'
     */
    @Test
    public void serviceWebAuthenticationClientCertRequestTest() {
        // TODO: test serviceWebAuthenticationClientCertRequest
    }

    /**
     * Test the property 'serviceWebMaxConnectionCount'
     */
    @Test
    public void serviceWebMaxConnectionCountTest() {
        // TODO: test serviceWebMaxConnectionCount
    }

    /**
     * Test the property 'serviceWebPlainTextEnabled'
     */
    @Test
    public void serviceWebPlainTextEnabledTest() {
        // TODO: test serviceWebPlainTextEnabled
    }

    /**
     * Test the property 'serviceWebTlsEnabled'
     */
    @Test
    public void serviceWebTlsEnabledTest() {
        // TODO: test serviceWebTlsEnabled
    }

    /**
     * Test the property 'tlsAllowDowngradeToPlainTextEnabled'
     */
    @Test
    public void tlsAllowDowngradeToPlainTextEnabledTest() {
        // TODO: test tlsAllowDowngradeToPlainTextEnabled
    }

}
