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

import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfile;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroup;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupMsgVpnAccessLevelException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileAccessLevelGroupsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAllowedHost;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAllowedHostResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAllowedHostsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAuthorizationParameter;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAuthorizationParameterResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientAuthorizationParametersResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientRequiredClaim;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientRequiredClaimResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileClientRequiredClaimsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileDefaultMsgVpnAccessLevelException;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileDefaultMsgVpnAccessLevelExceptionResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResourceServerRequiredClaim;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResourceServerRequiredClaimResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResourceServerRequiredClaimsResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfileResponse;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.OauthProfilesResponse;
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
 * API tests for OauthProfileApi
 */
@Ignore
public class OauthProfileApiTest {

    private final OauthProfileApi api = new OauthProfileApi();

    
    /**
     * Create an OAuth Profile object.
     *
     * Create an OAuth Profile object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientSecret||||x||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileTest() {
        OauthProfile body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileResponse response = api.createOauthProfile(body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Group Access Level object.
     *
     * Create a Group Access Level object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileAccessLevelGroupTest() {
        String oauthProfileName = null;
        OauthProfileAccessLevelGroup body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupResponse response = api.createOauthProfileAccessLevelGroup(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Message VPN Access-Level Exception object.
     *
     * Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: groupName|x||x||| msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String groupName = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelException body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse response = api.createOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create an Allowed Host Value object.
     *
     * Create an Allowed Host Value object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: allowedHost|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileClientAllowedHostTest() {
        String oauthProfileName = null;
        OauthProfileClientAllowedHost body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientAllowedHostResponse response = api.createOauthProfileClientAllowedHost(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create an Authorization Parameter object.
     *
     * Create an Authorization Parameter object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: authorizationParameterName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileClientAuthorizationParameterTest() {
        String oauthProfileName = null;
        OauthProfileClientAuthorizationParameter body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientAuthorizationParameterResponse response = api.createOauthProfileClientAuthorizationParameter(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Required Claim object.
     *
     * Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: clientRequiredClaimName|x|x|||| clientRequiredClaimValue||x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileClientRequiredClaimTest() {
        String oauthProfileName = null;
        OauthProfileClientRequiredClaim body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientRequiredClaimResponse response = api.createOauthProfileClientRequiredClaim(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Message VPN Access-Level Exception object.
     *
     * Create a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x|||| oauthProfileName|x||x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileDefaultMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        OauthProfileDefaultMsgVpnAccessLevelException body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileDefaultMsgVpnAccessLevelExceptionResponse response = api.createOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Create a Required Claim object.
     *
     * Create a Required Claim object. Any attribute missing from the request will be set to its default value. The creation of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---:|:---:|:---: oauthProfileName|x||x||| resourceServerRequiredClaimName|x|x|||| resourceServerRequiredClaimValue||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOauthProfileResourceServerRequiredClaimTest() {
        String oauthProfileName = null;
        OauthProfileResourceServerRequiredClaim body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileResourceServerRequiredClaimResponse response = api.createOauthProfileResourceServerRequiredClaim(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Delete an OAuth Profile object.
     *
     * Delete an OAuth Profile object. The deletion of instances of this object are synchronized to HA mates via config-sync.  OAuth profiles specify how to securely authenticate to an OAuth provider.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileTest() {
        String oauthProfileName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfile(oauthProfileName);

        // TODO: test validations
    }
    
    /**
     * Delete a Group Access Level object.
     *
     * Delete a Group Access Level object. The deletion of instances of this object are synchronized to HA mates via config-sync.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileAccessLevelGroupTest() {
        String oauthProfileName = null;
        String groupName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileAccessLevelGroup(oauthProfileName, groupName);

        // TODO: test validations
    }
    
    /**
     * Delete a Message VPN Access-Level Exception object.
     *
     * Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Message VPN access-level exceptions for members of this group.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String groupName = null;
        String msgVpnName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName);

        // TODO: test validations
    }
    
    /**
     * Delete an Allowed Host Value object.
     *
     * Delete an Allowed Host Value object. The deletion of instances of this object are synchronized to HA mates via config-sync.  A valid hostname for this broker in OAuth redirects.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileClientAllowedHostTest() {
        String oauthProfileName = null;
        String allowedHost = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileClientAllowedHost(oauthProfileName, allowedHost);

        // TODO: test validations
    }
    
    /**
     * Delete an Authorization Parameter object.
     *
     * Delete an Authorization Parameter object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional parameters to be passed to the OAuth authorization endpoint.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileClientAuthorizationParameterTest() {
        String oauthProfileName = null;
        String authorizationParameterName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName);

        // TODO: test validations
    }
    
    /**
     * Delete a Required Claim object.
     *
     * Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the ID token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileClientRequiredClaimTest() {
        String oauthProfileName = null;
        String clientRequiredClaimName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileClientRequiredClaim(oauthProfileName, clientRequiredClaimName);

        // TODO: test validations
    }
    
    /**
     * Delete a Message VPN Access-Level Exception object.
     *
     * Delete a Message VPN Access-Level Exception object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Default message VPN access-level exceptions.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileDefaultMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String msgVpnName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName);

        // TODO: test validations
    }
    
    /**
     * Delete a Required Claim object.
     *
     * Delete a Required Claim object. The deletion of instances of this object are synchronized to HA mates via config-sync.  Additional claims to be verified in the access token.  A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOauthProfileResourceServerRequiredClaimTest() {
        String oauthProfileName = null;
        String resourceServerRequiredClaimName = null;
        SempMetaOnlyResponse response = api.deleteOauthProfileResourceServerRequiredClaim(oauthProfileName, resourceServerRequiredClaimName);

        // TODO: test validations
    }
    
    /**
     * Get an OAuth Profile object.
     *
     * Get an OAuth Profile object.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileTest() {
        String oauthProfileName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileResponse response = api.getOauthProfile(oauthProfileName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a Group Access Level object.
     *
     * Get a Group Access Level object.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileAccessLevelGroupTest() {
        String oauthProfileName = null;
        String groupName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupResponse response = api.getOauthProfileAccessLevelGroup(oauthProfileName, groupName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a Message VPN Access-Level Exception object.
     *
     * Get a Message VPN Access-Level Exception object.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String groupName = null;
        String msgVpnName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse response = api.getOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Message VPN Access-Level Exception objects.
     *
     * Get a list of Message VPN Access-Level Exception objects.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsTest() {
        String oauthProfileName = null;
        String groupName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionsResponse response = api.getOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptions(oauthProfileName, groupName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Group Access Level objects.
     *
     * Get a list of Group Access Level objects.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: groupName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileAccessLevelGroupsTest() {
        String oauthProfileName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupsResponse response = api.getOauthProfileAccessLevelGroups(oauthProfileName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get an Allowed Host Value object.
     *
     * Get an Allowed Host Value object.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileClientAllowedHostTest() {
        String oauthProfileName = null;
        String allowedHost = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientAllowedHostResponse response = api.getOauthProfileClientAllowedHost(oauthProfileName, allowedHost, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Allowed Host Value objects.
     *
     * Get a list of Allowed Host Value objects.  A valid hostname for this broker in OAuth redirects.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: allowedHost|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileClientAllowedHostsTest() {
        String oauthProfileName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileClientAllowedHostsResponse response = api.getOauthProfileClientAllowedHosts(oauthProfileName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get an Authorization Parameter object.
     *
     * Get an Authorization Parameter object.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileClientAuthorizationParameterTest() {
        String oauthProfileName = null;
        String authorizationParameterName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientAuthorizationParameterResponse response = api.getOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Authorization Parameter objects.
     *
     * Get a list of Authorization Parameter objects.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: authorizationParameterName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileClientAuthorizationParametersTest() {
        String oauthProfileName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileClientAuthorizationParametersResponse response = api.getOauthProfileClientAuthorizationParameters(oauthProfileName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Required Claim object.
     *
     * Get a Required Claim object.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileClientRequiredClaimTest() {
        String oauthProfileName = null;
        String clientRequiredClaimName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientRequiredClaimResponse response = api.getOauthProfileClientRequiredClaim(oauthProfileName, clientRequiredClaimName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Required Claim objects.
     *
     * Get a list of Required Claim objects.  Additional claims to be verified in the ID token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientRequiredClaimName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileClientRequiredClaimsTest() {
        String oauthProfileName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileClientRequiredClaimsResponse response = api.getOauthProfileClientRequiredClaims(oauthProfileName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Message VPN Access-Level Exception object.
     *
     * Get a Message VPN Access-Level Exception object.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileDefaultMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String msgVpnName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileDefaultMsgVpnAccessLevelExceptionResponse response = api.getOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Message VPN Access-Level Exception objects.
     *
     * Get a list of Message VPN Access-Level Exception objects.  Default message VPN access-level exceptions.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: msgVpnName|x||| oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileDefaultMsgVpnAccessLevelExceptionsTest() {
        String oauthProfileName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileDefaultMsgVpnAccessLevelExceptionsResponse response = api.getOauthProfileDefaultMsgVpnAccessLevelExceptions(oauthProfileName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a Required Claim object.
     *
     * Get a Required Claim object.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileResourceServerRequiredClaimTest() {
        String oauthProfileName = null;
        String resourceServerRequiredClaimName = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileResourceServerRequiredClaimResponse response = api.getOauthProfileResourceServerRequiredClaim(oauthProfileName, resourceServerRequiredClaimName, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of Required Claim objects.
     *
     * Get a list of Required Claim objects.  Additional claims to be verified in the access token.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: oauthProfileName|x||| resourceServerRequiredClaimName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfileResourceServerRequiredClaimsTest() {
        String oauthProfileName = null;
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfileResourceServerRequiredClaimsResponse response = api.getOauthProfileResourceServerRequiredClaims(oauthProfileName, count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Get a list of OAuth Profile objects.
     *
     * Get a list of OAuth Profile objects.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Write-Only|Deprecated|Opaque :---|:---:|:---:|:---:|:---: clientSecret||x||x oauthProfileName|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getOauthProfilesTest() {
        Integer count = null;
        String cursor = null;
        String opaquePassword = null;
        List<String> where = null;
        List<String> select = null;
        OauthProfilesResponse response = api.getOauthProfiles(count, cursor, opaquePassword, where, select);

        // TODO: test validations
    }
    
    /**
     * Replace an OAuth Profile object.
     *
     * Replace an OAuth Profile object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- clientSecret||||x|||x oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceOauthProfileTest() {
        String oauthProfileName = null;
        OauthProfile body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileResponse response = api.replaceOauthProfile(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Group Access Level object.
     *
     * Replace a Group Access Level object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceOauthProfileAccessLevelGroupTest() {
        String oauthProfileName = null;
        String groupName = null;
        OauthProfileAccessLevelGroup body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupResponse response = api.replaceOauthProfileAccessLevelGroup(oauthProfileName, groupName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Message VPN Access-Level Exception object.
     *
     * Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- groupName|x||x|||| msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String groupName = null;
        String msgVpnName = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelException body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse response = api.replaceOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace an Authorization Parameter object.
     *
     * Replace an Authorization Parameter object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceOauthProfileClientAuthorizationParameterTest() {
        String oauthProfileName = null;
        String authorizationParameterName = null;
        OauthProfileClientAuthorizationParameter body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientAuthorizationParameterResponse response = api.replaceOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Replace a Message VPN Access-Level Exception object.
     *
     * Replace a Message VPN Access-Level Exception object. Any attribute missing from the request will be set to its default value, subject to the exceptions in note 4.  Default message VPN access-level exceptions.   Attribute|Identifying|Const|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:---|:--- msgVpnName|x||x|||| oauthProfileName|x||x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void replaceOauthProfileDefaultMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String msgVpnName = null;
        OauthProfileDefaultMsgVpnAccessLevelException body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileDefaultMsgVpnAccessLevelExceptionResponse response = api.replaceOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update an OAuth Profile object.
     *
     * Update an OAuth Profile object. Any attribute missing from the request will be left unchanged.  OAuth profiles specify how to securely authenticate to an OAuth provider.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- clientSecret|||x|||x oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: accessLevelGroupsClaimName|global/admin clientId|global/admin clientRedirectUri|global/admin clientRequiredType|global/admin clientScope|global/admin clientSecret|global/admin clientValidateTypeEnabled|global/admin defaultGlobalAccessLevel|global/admin displayName|global/admin enabled|global/admin endpointAuthorization|global/admin endpointDiscovery|global/admin endpointDiscoveryRefreshInterval|global/admin endpointIntrospection|global/admin endpointIntrospectionTimeout|global/admin endpointJwks|global/admin endpointJwksRefreshInterval|global/admin endpointToken|global/admin endpointTokenTimeout|global/admin endpointUserinfo|global/admin endpointUserinfoTimeout|global/admin interactiveEnabled|global/admin interactivePromptForExpiredSession|global/admin interactivePromptForNewSession|global/admin issuer|global/admin oauthRole|global/admin resourceServerParseAccessTokenEnabled|global/admin resourceServerRequiredAudience|global/admin resourceServerRequiredIssuer|global/admin resourceServerRequiredScope|global/admin resourceServerRequiredType|global/admin resourceServerValidateAudienceEnabled|global/admin resourceServerValidateIssuerEnabled|global/admin resourceServerValidateScopeEnabled|global/admin resourceServerValidateTypeEnabled|global/admin sempEnabled|global/admin usernameClaimName|global/admin    This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOauthProfileTest() {
        String oauthProfileName = null;
        OauthProfile body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileResponse response = api.updateOauthProfile(oauthProfileName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Group Access Level object.
     *
     * Update a Group Access Level object. Any attribute missing from the request will be left unchanged.  The name of a group as it exists on the OAuth server being used to authenticate SEMP users.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin    This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOauthProfileAccessLevelGroupTest() {
        String oauthProfileName = null;
        String groupName = null;
        OauthProfileAccessLevelGroup body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupResponse response = api.updateOauthProfileAccessLevelGroup(oauthProfileName, groupName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Message VPN Access-Level Exception object.
     *
     * Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Message VPN access-level exceptions for members of this group.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- groupName|x|x|||| msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String groupName = null;
        String msgVpnName = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelException body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileAccessLevelGroupMsgVpnAccessLevelExceptionResponse response = api.updateOauthProfileAccessLevelGroupMsgVpnAccessLevelException(oauthProfileName, groupName, msgVpnName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update an Authorization Parameter object.
     *
     * Update an Authorization Parameter object. Any attribute missing from the request will be left unchanged.  Additional parameters to be passed to the OAuth authorization endpoint.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- authorizationParameterName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/admin\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOauthProfileClientAuthorizationParameterTest() {
        String oauthProfileName = null;
        String authorizationParameterName = null;
        OauthProfileClientAuthorizationParameter body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileClientAuthorizationParameterResponse response = api.updateOauthProfileClientAuthorizationParameter(oauthProfileName, authorizationParameterName, body, opaquePassword, select);

        // TODO: test validations
    }
    
    /**
     * Update a Message VPN Access-Level Exception object.
     *
     * Update a Message VPN Access-Level Exception object. Any attribute missing from the request will be left unchanged.  Default message VPN access-level exceptions.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated|Opaque :---|:---|:---|:---|:---|:---|:--- msgVpnName|x|x|||| oauthProfileName|x|x||||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.24.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOauthProfileDefaultMsgVpnAccessLevelExceptionTest() {
        String oauthProfileName = null;
        String msgVpnName = null;
        OauthProfileDefaultMsgVpnAccessLevelException body = null;
        String opaquePassword = null;
        List<String> select = null;
        OauthProfileDefaultMsgVpnAccessLevelExceptionResponse response = api.updateOauthProfileDefaultMsgVpnAccessLevelException(oauthProfileName, msgVpnName, body, opaquePassword, select);

        // TODO: test validations
    }
    
}
