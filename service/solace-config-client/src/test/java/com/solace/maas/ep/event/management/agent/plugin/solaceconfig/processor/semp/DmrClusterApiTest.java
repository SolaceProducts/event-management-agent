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


package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp;

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrCluster;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRule;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleAttributeFilter;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleAttributeFilterResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleAttributeFiltersResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleCondition;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleConditionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleConditionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRuleResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterCertMatchingRulesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLink;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkAttribute;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkAttributeResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkAttributesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkRemoteAddress;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkRemoteAddressResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkRemoteAddressesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkTlsTrustedCommonName;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkTlsTrustedCommonNameResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinkTlsTrustedCommonNamesResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterLinksResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClusterResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.DmrClustersResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.SempMetaOnlyResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DmrClusterApi
 */
@Ignore
public class DmrClusterApiTest {

    private final DmrClusterApi api = new DmrClusterApi();

    
    /**
     * Create a Cluster object.
     *
     * Create a Cluster object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x||x authenticationClientCertContent||||x||x authenticationClientCertPassword||||x|| dmrClusterName|x|x|||| nodeName|||x||| tlsServerCertEnforceTrustedCommonNameEnabled|||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterTest() {
        DmrCluster body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterResponse response = api.createDmrCluster(body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Certificate Matching Rule object.
     *
     * Create a Certificate Matching Rule object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterCertMatchingRuleTest() {
        String dmrClusterName = null;
        DmrClusterCertMatchingRule body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleResponse response = api.createDmrClusterCertMatchingRule(dmrClusterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Certificate Matching Rule Attribute Filter object.
     *
     * Create a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| filterName|x|x|||| ruleName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterCertMatchingRuleAttributeFilterTest() {
        String dmrClusterName = null;
        String ruleName = null;
        DmrClusterCertMatchingRuleAttributeFilter body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleAttributeFilterResponse response = api.createDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Certificate Matching Rule Condition object.
     *
     * Create a Certificate Matching Rule Condition object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| ruleName|x||x||| source|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterCertMatchingRuleConditionTest() {
        String dmrClusterName = null;
        String ruleName = null;
        DmrClusterCertMatchingRuleCondition body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleConditionResponse response = api.createDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Link object.
     *
     * Create a Link object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x||x dmrClusterName|x||x||| remoteNodeName|x|x||||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterLinkTest() {
        String dmrClusterName = null;
        DmrClusterLink body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkResponse response = api.createDmrClusterLink(dmrClusterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Link Attribute object.
     *
     * Create a Link Attribute object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: attributeName|x|x|||| attributeValue|x|x|||| dmrClusterName|x||x||| remoteNodeName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterLinkAttributeTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        DmrClusterLinkAttribute body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkAttributeResponse response = api.createDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Remote Address object.
     *
     * Create a Remote Address object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||| remoteAddress|x|x|||| remoteNodeName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterLinkRemoteAddressTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        DmrClusterLinkRemoteAddress body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkRemoteAddressResponse response = api.createDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Trusted Common Name object.
     *
     * Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x||x| remoteNodeName|x||x||x| tlsTrustedCommonName|x|x|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDmrClusterLinkTlsTrustedCommonNameTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        DmrClusterLinkTlsTrustedCommonName body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkTlsTrustedCommonNameResponse response = api.createDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Delete a Cluster object.
     *
     * Delete a Cluster object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterTest() {
        String dmrClusterName = null;
        SempMetaOnlyResponse response = api.deleteDmrCluster(dmrClusterName);

        // TODO: test validations
    }
    
    /**
     * Delete a Certificate Matching Rule object.
     *
     * Delete a Certificate Matching Rule object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterCertMatchingRuleTest() {
        String dmrClusterName = null;
        String ruleName = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterCertMatchingRule(dmrClusterName, ruleName);

        // TODO: test validations
    }
    
    /**
     * Delete a Certificate Matching Rule Attribute Filter object.
     *
     * Delete a Certificate Matching Rule Attribute Filter object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterCertMatchingRuleAttributeFilterTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String filterName = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName);

        // TODO: test validations
    }
    
    /**
     * Delete a Certificate Matching Rule Condition object.
     *
     * Delete a Certificate Matching Rule Condition object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterCertMatchingRuleConditionTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String source = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, source);

        // TODO: test validations
    }
    
    /**
     * Delete a Link object.
     *
     * Delete a Link object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterLinkTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterLink(dmrClusterName, remoteNodeName);

        // TODO: test validations
    }
    
    /**
     * Delete a Link Attribute object.
     *
     * Delete a Link Attribute object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterLinkAttributeTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String attributeName = null;
        String attributeValue = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, attributeName, attributeValue);

        // TODO: test validations
    }
    
    /**
     * Delete a Remote Address object.
     *
     * Delete a Remote Address object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterLinkRemoteAddressTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String remoteAddress = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress);

        // TODO: test validations
    }
    
    /**
     * Delete a Trusted Common Name object.
     *
     * Delete a Trusted Common Name object. The deletion of instances of this object are synchronized to HA mates via config-sync.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDmrClusterLinkTlsTrustedCommonNameTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String tlsTrustedCommonName = null;
        SempMetaOnlyResponse response = api.deleteDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName);

        // TODO: test validations
    }
    
    /**
     * Get a Cluster object.
     *
     * Get a Cluster object.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| dmrClusterName|x||| tlsServerCertEnforceTrustedCommonNameEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterTest() {
        String dmrClusterName = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterResponse response = api.getDmrCluster(dmrClusterName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a Certificate Matching Rule object.
     *
     * Get a Certificate Matching Rule object.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterCertMatchingRuleTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleResponse response = api.getDmrClusterCertMatchingRule(dmrClusterName, ruleName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a Certificate Matching Rule Attribute Filter object.
     *
     * Get a Certificate Matching Rule Attribute Filter object.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| filterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterCertMatchingRuleAttributeFilterTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String filterName = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleAttributeFilterResponse response = api.getDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Certificate Matching Rule Attribute Filter objects.
     *
     * Get a list of Certificate Matching Rule Attribute Filter objects.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| filterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterCertMatchingRuleAttributeFiltersTest() {
        String dmrClusterName = null;
        String ruleName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleAttributeFiltersResponse response = api.getDmrClusterCertMatchingRuleAttributeFilters(dmrClusterName, ruleName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Certificate Matching Rule Condition object.
     *
     * Get a Certificate Matching Rule Condition object.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x||| source|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterCertMatchingRuleConditionTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String source = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleConditionResponse response = api.getDmrClusterCertMatchingRuleCondition(dmrClusterName, ruleName, source, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Certificate Matching Rule Condition objects.
     *
     * Get a list of Certificate Matching Rule Condition objects.  A Cert Matching Rule Condition compares data extracted from a certificate to a link attribute or an expression.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x||| source|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterCertMatchingRuleConditionsTest() {
        String dmrClusterName = null;
        String ruleName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleConditionsResponse response = api.getDmrClusterCertMatchingRuleConditions(dmrClusterName, ruleName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Certificate Matching Rule objects.
     *
     * Get a list of Certificate Matching Rule objects.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| ruleName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterCertMatchingRulesTest() {
        String dmrClusterName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterCertMatchingRulesResponse response = api.getDmrClusterCertMatchingRules(dmrClusterName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Link object.
     *
     * Get a Link object.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkResponse response = api.getDmrClusterLink(dmrClusterName, remoteNodeName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a Link Attribute object.
     *
     * Get a Link Attribute object.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: attributeName|x||| attributeValue|x||| dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkAttributeTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String attributeName = null;
        String attributeValue = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkAttributeResponse response = api.getDmrClusterLinkAttribute(dmrClusterName, remoteNodeName, attributeName, attributeValue, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Link Attribute objects.
     *
     * Get a list of Link Attribute objects.  A Link Attribute is a key+value pair that can be used to locate a DMR Cluster Link, for example when using client certificate mapping.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: attributeName|x||| attributeValue|x||| dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkAttributesTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterLinkAttributesResponse response = api.getDmrClusterLinkAttributes(dmrClusterName, remoteNodeName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Remote Address object.
     *
     * Get a Remote Address object.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| remoteAddress|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkRemoteAddressTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String remoteAddress = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkRemoteAddressResponse response = api.getDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Remote Address objects.
     *
     * Get a list of Remote Address objects.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||| remoteAddress|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkRemoteAddressesTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterLinkRemoteAddressesResponse response = api.getDmrClusterLinkRemoteAddresses(dmrClusterName, remoteNodeName, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Trusted Common Name object.
     *
     * Get a Trusted Common Name object.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||x| remoteNodeName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkTlsTrustedCommonNameTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String tlsTrustedCommonName = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkTlsTrustedCommonNameResponse response = api.getDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Trusted Common Name objects.
     *
     * Get a list of Trusted Common Name objects.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: dmrClusterName|x||x| remoteNodeName|x||x| tlsTrustedCommonName|x||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinkTlsTrustedCommonNamesTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterLinkTlsTrustedCommonNamesResponse response = api.getDmrClusterLinkTlsTrustedCommonNames(dmrClusterName, remoteNodeName, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Link objects.
     *
     * Get a list of Link objects.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x dmrClusterName|x||| remoteNodeName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClusterLinksTest() {
        String dmrClusterName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClusterLinksResponse response = api.getDmrClusterLinks(dmrClusterName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Cluster objects.
     *
     * Get a list of Cluster objects.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authenticationBasicPassword||x||x authenticationClientCertContent||x||x authenticationClientCertPassword||x|| dmrClusterName|x||| tlsServerCertEnforceTrustedCommonNameEnabled|||x|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDmrClustersTest() {
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        DmrClustersResponse response = api.getDmrClusters(count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Cluster object.
     *
     * Replace a Cluster object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword||||x|x||x authenticationClientCertContent||||x|x||x authenticationClientCertPassword||||x|x|| directOnlyEnabled||x||||| dmrClusterName|x||x|||| nodeName|||x|||| tlsServerCertEnforceTrustedCommonNameEnabled||||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceDmrClusterTest() {
        String dmrClusterName = null;
        DmrCluster body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterResponse response = api.replaceDmrCluster(dmrClusterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Certificate Matching Rule object.
     *
     * Replace a Certificate Matching Rule object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- dmrClusterName|x||x|||| ruleName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceDmrClusterCertMatchingRuleTest() {
        String dmrClusterName = null;
        String ruleName = null;
        DmrClusterCertMatchingRule body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleResponse response = api.replaceDmrClusterCertMatchingRule(dmrClusterName, ruleName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Certificate Matching Rule Attribute Filter object.
     *
     * Replace a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- dmrClusterName|x||x|||| filterName|x||x|||| ruleName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceDmrClusterCertMatchingRuleAttributeFilterTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String filterName = null;
        DmrClusterCertMatchingRuleAttributeFilter body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleAttributeFilterResponse response = api.replaceDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Link object.
     *
     * Replace a Link object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword||||x|x||x authenticationScheme|||||x|| dmrClusterName|x||x|||| egressFlowWindowSize|||||x|| initiator|||||x|| remoteNodeName|x||x|||| span|||||x|| transportCompressedEnabled|||||x|| transportTlsEnabled|||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceDmrClusterLinkTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        DmrClusterLink body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkResponse response = api.replaceDmrClusterLink(dmrClusterName, remoteNodeName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Cluster object.
     *
     * Update a Cluster object. Any attribute missing from the request will be left unchanged.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword|||x|x||x authenticationClientCertContent|||x|x||x authenticationClientCertPassword|||x|x|| directOnlyEnabled||x|||| dmrClusterName|x|x|||| nodeName||x|||| tlsServerCertEnforceTrustedCommonNameEnabled|||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDmrClusterTest() {
        String dmrClusterName = null;
        DmrCluster body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterResponse response = api.updateDmrCluster(dmrClusterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Certificate Matching Rule object.
     *
     * Update a Certificate Matching Rule object. Any attribute missing from the request will be left unchanged.  A Cert Matching Rule is a collection of conditions and attribute filters that all have to be satisfied for certificate to be acceptable as authentication for a given link.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- dmrClusterName|x|x|||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDmrClusterCertMatchingRuleTest() {
        String dmrClusterName = null;
        String ruleName = null;
        DmrClusterCertMatchingRule body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleResponse response = api.updateDmrClusterCertMatchingRule(dmrClusterName, ruleName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Certificate Matching Rule Attribute Filter object.
     *
     * Update a Certificate Matching Rule Attribute Filter object. Any attribute missing from the request will be left unchanged.  A Cert Matching Rule Attribute Filter compares a link attribute to a string.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- dmrClusterName|x|x|||| filterName|x|x|||| ruleName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.28.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDmrClusterCertMatchingRuleAttributeFilterTest() {
        String dmrClusterName = null;
        String ruleName = null;
        String filterName = null;
        DmrClusterCertMatchingRuleAttributeFilter body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterCertMatchingRuleAttributeFilterResponse response = api.updateDmrClusterCertMatchingRuleAttributeFilter(dmrClusterName, ruleName, filterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Link object.
     *
     * Update a Link object. Any attribute missing from the request will be left unchanged.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authenticationBasicPassword|||x|x||x authenticationScheme||||x|| dmrClusterName|x|x|||| egressFlowWindowSize||||x|| initiator||||x|| remoteNodeName|x|x|||| span||||x|| transportCompressedEnabled||||x|| transportTlsEnabled||||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDmrClusterLinkTest() {
        String dmrClusterName = null;
        String remoteNodeName = null;
        DmrClusterLink body = null;
        String opaquePassword = null;
        List<String> select = null;
        DmrClusterLinkResponse response = api.updateDmrClusterLink(dmrClusterName, remoteNodeName, body, opaquePassword, select);

        // TODO: test validations
    }
    
}
