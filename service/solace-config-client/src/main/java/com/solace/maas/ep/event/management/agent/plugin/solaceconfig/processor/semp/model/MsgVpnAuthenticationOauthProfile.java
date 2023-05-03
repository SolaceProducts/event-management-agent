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

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * MsgVpnAuthenticationOauthProfile
 */
@JsonPropertyOrder({
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_AUTHORIZATION_GROUPS_CLAIM_NAME,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_CLIENT_ID,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_CLIENT_REQUIRED_TYPE,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_CLIENT_SECRET,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_CLIENT_VALIDATE_TYPE_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_DISCONNECT_ON_TOKEN_EXPIRATION_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_DISCOVERY,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_DISCOVERY_REFRESH_INTERVAL,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_INTROSPECTION,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_INTROSPECTION_TIMEOUT,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_JWKS,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_JWKS_REFRESH_INTERVAL,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_USERINFO,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ENDPOINT_USERINFO_TIMEOUT,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_ISSUER,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_MQTT_USERNAME_VALIDATE_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_OAUTH_PROFILE_NAME,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_OAUTH_ROLE,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_PARSE_ACCESS_TOKEN_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_AUDIENCE,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_ISSUER,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_SCOPE,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_TYPE,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_AUDIENCE_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_ISSUER_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_SCOPE_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_TYPE_ENABLED,
  MsgVpnAuthenticationOauthProfile.JSON_PROPERTY_USERNAME_CLAIM_NAME
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class MsgVpnAuthenticationOauthProfile {
  public static final String JSON_PROPERTY_AUTHORIZATION_GROUPS_CLAIM_NAME = "authorizationGroupsClaimName";
  private String authorizationGroupsClaimName;

  public static final String JSON_PROPERTY_CLIENT_ID = "clientId";
  private String clientId;

  public static final String JSON_PROPERTY_CLIENT_REQUIRED_TYPE = "clientRequiredType";
  private String clientRequiredType;

  public static final String JSON_PROPERTY_CLIENT_SECRET = "clientSecret";
  private String clientSecret;

  public static final String JSON_PROPERTY_CLIENT_VALIDATE_TYPE_ENABLED = "clientValidateTypeEnabled";
  private Boolean clientValidateTypeEnabled;

  public static final String JSON_PROPERTY_DISCONNECT_ON_TOKEN_EXPIRATION_ENABLED = "disconnectOnTokenExpirationEnabled";
  private Boolean disconnectOnTokenExpirationEnabled;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_ENDPOINT_DISCOVERY = "endpointDiscovery";
  private String endpointDiscovery;

  public static final String JSON_PROPERTY_ENDPOINT_DISCOVERY_REFRESH_INTERVAL = "endpointDiscoveryRefreshInterval";
  private Integer endpointDiscoveryRefreshInterval;

  public static final String JSON_PROPERTY_ENDPOINT_INTROSPECTION = "endpointIntrospection";
  private String endpointIntrospection;

  public static final String JSON_PROPERTY_ENDPOINT_INTROSPECTION_TIMEOUT = "endpointIntrospectionTimeout";
  private Integer endpointIntrospectionTimeout;

  public static final String JSON_PROPERTY_ENDPOINT_JWKS = "endpointJwks";
  private String endpointJwks;

  public static final String JSON_PROPERTY_ENDPOINT_JWKS_REFRESH_INTERVAL = "endpointJwksRefreshInterval";
  private Integer endpointJwksRefreshInterval;

  public static final String JSON_PROPERTY_ENDPOINT_USERINFO = "endpointUserinfo";
  private String endpointUserinfo;

  public static final String JSON_PROPERTY_ENDPOINT_USERINFO_TIMEOUT = "endpointUserinfoTimeout";
  private Integer endpointUserinfoTimeout;

  public static final String JSON_PROPERTY_ISSUER = "issuer";
  private String issuer;

  public static final String JSON_PROPERTY_MQTT_USERNAME_VALIDATE_ENABLED = "mqttUsernameValidateEnabled";
  private Boolean mqttUsernameValidateEnabled;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_OAUTH_PROFILE_NAME = "oauthProfileName";
  private String oauthProfileName;

  /**
   * The OAuth role of the broker. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;client\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;client\&quot; - The broker is in the OAuth client role. \&quot;resource-server\&quot; - The broker is in the OAuth resource server role. &lt;/pre&gt; 
   */
  public enum OauthRoleEnum {
    CLIENT("client"),
    
    RESOURCE_SERVER("resource-server");

    private String value;

    OauthRoleEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OauthRoleEnum fromValue(String value) {
      for (OauthRoleEnum b : OauthRoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_OAUTH_ROLE = "oauthRole";
  private OauthRoleEnum oauthRole;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_PARSE_ACCESS_TOKEN_ENABLED = "resourceServerParseAccessTokenEnabled";
  private Boolean resourceServerParseAccessTokenEnabled;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_AUDIENCE = "resourceServerRequiredAudience";
  private String resourceServerRequiredAudience;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_ISSUER = "resourceServerRequiredIssuer";
  private String resourceServerRequiredIssuer;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_SCOPE = "resourceServerRequiredScope";
  private String resourceServerRequiredScope;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_TYPE = "resourceServerRequiredType";
  private String resourceServerRequiredType;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_AUDIENCE_ENABLED = "resourceServerValidateAudienceEnabled";
  private Boolean resourceServerValidateAudienceEnabled;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_ISSUER_ENABLED = "resourceServerValidateIssuerEnabled";
  private Boolean resourceServerValidateIssuerEnabled;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_SCOPE_ENABLED = "resourceServerValidateScopeEnabled";
  private Boolean resourceServerValidateScopeEnabled;

  public static final String JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_TYPE_ENABLED = "resourceServerValidateTypeEnabled";
  private Boolean resourceServerValidateTypeEnabled;

  public static final String JSON_PROPERTY_USERNAME_CLAIM_NAME = "usernameClaimName";
  private String usernameClaimName;

  public MsgVpnAuthenticationOauthProfile() {
  }

  public MsgVpnAuthenticationOauthProfile authorizationGroupsClaimName(String authorizationGroupsClaimName) {
    
    this.authorizationGroupsClaimName = authorizationGroupsClaimName;
    return this;
  }

   /**
   * The name of the groups claim. If non-empty, the specified claim will be used to determine groups for authorization. If empty, the authorizationType attribute of the Message VPN will be used to determine authorization. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;groups\&quot;&#x60;.
   * @return authorizationGroupsClaimName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUPS_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthorizationGroupsClaimName() {
    return authorizationGroupsClaimName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUPS_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationGroupsClaimName(String authorizationGroupsClaimName) {
    this.authorizationGroupsClaimName = authorizationGroupsClaimName;
  }


  public MsgVpnAuthenticationOauthProfile clientId(String clientId) {
    
    this.clientId = clientId;
    return this;
  }

   /**
   * The OAuth client id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return clientId
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientId() {
    return clientId;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }


  public MsgVpnAuthenticationOauthProfile clientRequiredType(String clientRequiredType) {
    
    this.clientRequiredType = clientRequiredType;
    return this;
  }

   /**
   * The required value for the TYP field in the ID token header. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;JWT\&quot;&#x60;.
   * @return clientRequiredType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_REQUIRED_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientRequiredType() {
    return clientRequiredType;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_REQUIRED_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientRequiredType(String clientRequiredType) {
    this.clientRequiredType = clientRequiredType;
  }


  public MsgVpnAuthenticationOauthProfile clientSecret(String clientSecret) {
    
    this.clientSecret = clientSecret;
    return this;
  }

   /**
   * The OAuth client secret. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return clientSecret
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_SECRET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientSecret() {
    return clientSecret;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_SECRET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }


  public MsgVpnAuthenticationOauthProfile clientValidateTypeEnabled(Boolean clientValidateTypeEnabled) {
    
    this.clientValidateTypeEnabled = clientValidateTypeEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the TYP field in the ID token header. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return clientValidateTypeEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_VALIDATE_TYPE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getClientValidateTypeEnabled() {
    return clientValidateTypeEnabled;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_VALIDATE_TYPE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientValidateTypeEnabled(Boolean clientValidateTypeEnabled) {
    this.clientValidateTypeEnabled = clientValidateTypeEnabled;
  }


  public MsgVpnAuthenticationOauthProfile disconnectOnTokenExpirationEnabled(Boolean disconnectOnTokenExpirationEnabled) {
    
    this.disconnectOnTokenExpirationEnabled = disconnectOnTokenExpirationEnabled;
    return this;
  }

   /**
   * Enable or disable the disconnection of clients when their tokens expire. Changing this value does not affect existing clients, only new client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return disconnectOnTokenExpirationEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DISCONNECT_ON_TOKEN_EXPIRATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getDisconnectOnTokenExpirationEnabled() {
    return disconnectOnTokenExpirationEnabled;
  }


  @JsonProperty(JSON_PROPERTY_DISCONNECT_ON_TOKEN_EXPIRATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDisconnectOnTokenExpirationEnabled(Boolean disconnectOnTokenExpirationEnabled) {
    this.disconnectOnTokenExpirationEnabled = disconnectOnTokenExpirationEnabled;
  }


  public MsgVpnAuthenticationOauthProfile enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the OAuth profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return enabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEnabled() {
    return enabled;
  }


  @JsonProperty(JSON_PROPERTY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }


  public MsgVpnAuthenticationOauthProfile endpointDiscovery(String endpointDiscovery) {
    
    this.endpointDiscovery = endpointDiscovery;
    return this;
  }

   /**
   * The OpenID Connect discovery endpoint or OAuth Authorization Server Metadata endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return endpointDiscovery
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_DISCOVERY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEndpointDiscovery() {
    return endpointDiscovery;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_DISCOVERY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointDiscovery(String endpointDiscovery) {
    this.endpointDiscovery = endpointDiscovery;
  }


  public MsgVpnAuthenticationOauthProfile endpointDiscoveryRefreshInterval(Integer endpointDiscoveryRefreshInterval) {
    
    this.endpointDiscoveryRefreshInterval = endpointDiscoveryRefreshInterval;
    return this;
  }

   /**
   * The number of seconds between discovery endpoint requests. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;86400&#x60;.
   * @return endpointDiscoveryRefreshInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_DISCOVERY_REFRESH_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getEndpointDiscoveryRefreshInterval() {
    return endpointDiscoveryRefreshInterval;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_DISCOVERY_REFRESH_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointDiscoveryRefreshInterval(Integer endpointDiscoveryRefreshInterval) {
    this.endpointDiscoveryRefreshInterval = endpointDiscoveryRefreshInterval;
  }


  public MsgVpnAuthenticationOauthProfile endpointIntrospection(String endpointIntrospection) {
    
    this.endpointIntrospection = endpointIntrospection;
    return this;
  }

   /**
   * The OAuth introspection endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return endpointIntrospection
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_INTROSPECTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEndpointIntrospection() {
    return endpointIntrospection;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_INTROSPECTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointIntrospection(String endpointIntrospection) {
    this.endpointIntrospection = endpointIntrospection;
  }


  public MsgVpnAuthenticationOauthProfile endpointIntrospectionTimeout(Integer endpointIntrospectionTimeout) {
    
    this.endpointIntrospectionTimeout = endpointIntrospectionTimeout;
    return this;
  }

   /**
   * The maximum time in seconds a token introspection request is allowed to take. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;.
   * @return endpointIntrospectionTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_INTROSPECTION_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getEndpointIntrospectionTimeout() {
    return endpointIntrospectionTimeout;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_INTROSPECTION_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointIntrospectionTimeout(Integer endpointIntrospectionTimeout) {
    this.endpointIntrospectionTimeout = endpointIntrospectionTimeout;
  }


  public MsgVpnAuthenticationOauthProfile endpointJwks(String endpointJwks) {
    
    this.endpointJwks = endpointJwks;
    return this;
  }

   /**
   * The OAuth JWKS endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return endpointJwks
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_JWKS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEndpointJwks() {
    return endpointJwks;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_JWKS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointJwks(String endpointJwks) {
    this.endpointJwks = endpointJwks;
  }


  public MsgVpnAuthenticationOauthProfile endpointJwksRefreshInterval(Integer endpointJwksRefreshInterval) {
    
    this.endpointJwksRefreshInterval = endpointJwksRefreshInterval;
    return this;
  }

   /**
   * The number of seconds between JWKS endpoint requests. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;86400&#x60;.
   * @return endpointJwksRefreshInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_JWKS_REFRESH_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getEndpointJwksRefreshInterval() {
    return endpointJwksRefreshInterval;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_JWKS_REFRESH_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointJwksRefreshInterval(Integer endpointJwksRefreshInterval) {
    this.endpointJwksRefreshInterval = endpointJwksRefreshInterval;
  }


  public MsgVpnAuthenticationOauthProfile endpointUserinfo(String endpointUserinfo) {
    
    this.endpointUserinfo = endpointUserinfo;
    return this;
  }

   /**
   * The OpenID Connect Userinfo endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return endpointUserinfo
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_USERINFO)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEndpointUserinfo() {
    return endpointUserinfo;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_USERINFO)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointUserinfo(String endpointUserinfo) {
    this.endpointUserinfo = endpointUserinfo;
  }


  public MsgVpnAuthenticationOauthProfile endpointUserinfoTimeout(Integer endpointUserinfoTimeout) {
    
    this.endpointUserinfoTimeout = endpointUserinfoTimeout;
    return this;
  }

   /**
   * The maximum time in seconds a userinfo request is allowed to take. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;.
   * @return endpointUserinfoTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_USERINFO_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getEndpointUserinfoTimeout() {
    return endpointUserinfoTimeout;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_USERINFO_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointUserinfoTimeout(Integer endpointUserinfoTimeout) {
    this.endpointUserinfoTimeout = endpointUserinfoTimeout;
  }


  public MsgVpnAuthenticationOauthProfile issuer(String issuer) {
    
    this.issuer = issuer;
    return this;
  }

   /**
   * The Issuer Identifier for the OAuth provider. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return issuer
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ISSUER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getIssuer() {
    return issuer;
  }


  @JsonProperty(JSON_PROPERTY_ISSUER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }


  public MsgVpnAuthenticationOauthProfile mqttUsernameValidateEnabled(Boolean mqttUsernameValidateEnabled) {
    
    this.mqttUsernameValidateEnabled = mqttUsernameValidateEnabled;
    return this;
  }

   /**
   * Enable or disable whether the API provided MQTT client username will be validated against the username calculated from the token(s). When enabled, connection attempts by MQTT clients are rejected if they differ. Note that this value only applies to MQTT clients; SMF client usernames will not be validated. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return mqttUsernameValidateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MQTT_USERNAME_VALIDATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getMqttUsernameValidateEnabled() {
    return mqttUsernameValidateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_MQTT_USERNAME_VALIDATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMqttUsernameValidateEnabled(Boolean mqttUsernameValidateEnabled) {
    this.mqttUsernameValidateEnabled = mqttUsernameValidateEnabled;
  }


  public MsgVpnAuthenticationOauthProfile msgVpnName(String msgVpnName) {
    
    this.msgVpnName = msgVpnName;
    return this;
  }

   /**
   * The name of the Message VPN.
   * @return msgVpnName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MSG_VPN_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMsgVpnName() {
    return msgVpnName;
  }


  @JsonProperty(JSON_PROPERTY_MSG_VPN_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMsgVpnName(String msgVpnName) {
    this.msgVpnName = msgVpnName;
  }


  public MsgVpnAuthenticationOauthProfile oauthProfileName(String oauthProfileName) {
    
    this.oauthProfileName = oauthProfileName;
    return this;
  }

   /**
   * The name of the OAuth profile.
   * @return oauthProfileName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OAUTH_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOauthProfileName() {
    return oauthProfileName;
  }


  @JsonProperty(JSON_PROPERTY_OAUTH_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOauthProfileName(String oauthProfileName) {
    this.oauthProfileName = oauthProfileName;
  }


  public MsgVpnAuthenticationOauthProfile oauthRole(OauthRoleEnum oauthRole) {
    
    this.oauthRole = oauthRole;
    return this;
  }

   /**
   * The OAuth role of the broker. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;client\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;client\&quot; - The broker is in the OAuth client role. \&quot;resource-server\&quot; - The broker is in the OAuth resource server role. &lt;/pre&gt; 
   * @return oauthRole
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OAUTH_ROLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public OauthRoleEnum getOauthRole() {
    return oauthRole;
  }


  @JsonProperty(JSON_PROPERTY_OAUTH_ROLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOauthRole(OauthRoleEnum oauthRole) {
    this.oauthRole = oauthRole;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerParseAccessTokenEnabled(Boolean resourceServerParseAccessTokenEnabled) {
    
    this.resourceServerParseAccessTokenEnabled = resourceServerParseAccessTokenEnabled;
    return this;
  }

   /**
   * Enable or disable parsing of the access token as a JWT. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return resourceServerParseAccessTokenEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_PARSE_ACCESS_TOKEN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getResourceServerParseAccessTokenEnabled() {
    return resourceServerParseAccessTokenEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_PARSE_ACCESS_TOKEN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerParseAccessTokenEnabled(Boolean resourceServerParseAccessTokenEnabled) {
    this.resourceServerParseAccessTokenEnabled = resourceServerParseAccessTokenEnabled;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerRequiredAudience(String resourceServerRequiredAudience) {
    
    this.resourceServerRequiredAudience = resourceServerRequiredAudience;
    return this;
  }

   /**
   * The required audience value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return resourceServerRequiredAudience
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_AUDIENCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getResourceServerRequiredAudience() {
    return resourceServerRequiredAudience;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_AUDIENCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerRequiredAudience(String resourceServerRequiredAudience) {
    this.resourceServerRequiredAudience = resourceServerRequiredAudience;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerRequiredIssuer(String resourceServerRequiredIssuer) {
    
    this.resourceServerRequiredIssuer = resourceServerRequiredIssuer;
    return this;
  }

   /**
   * The required issuer value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return resourceServerRequiredIssuer
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_ISSUER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getResourceServerRequiredIssuer() {
    return resourceServerRequiredIssuer;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_ISSUER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerRequiredIssuer(String resourceServerRequiredIssuer) {
    this.resourceServerRequiredIssuer = resourceServerRequiredIssuer;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerRequiredScope(String resourceServerRequiredScope) {
    
    this.resourceServerRequiredScope = resourceServerRequiredScope;
    return this;
  }

   /**
   * A space-separated list of scopes that must be present in the scope claim. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return resourceServerRequiredScope
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_SCOPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getResourceServerRequiredScope() {
    return resourceServerRequiredScope;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_SCOPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerRequiredScope(String resourceServerRequiredScope) {
    this.resourceServerRequiredScope = resourceServerRequiredScope;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerRequiredType(String resourceServerRequiredType) {
    
    this.resourceServerRequiredType = resourceServerRequiredType;
    return this;
  }

   /**
   * The required TYP value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;at+jwt\&quot;&#x60;.
   * @return resourceServerRequiredType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getResourceServerRequiredType() {
    return resourceServerRequiredType;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerRequiredType(String resourceServerRequiredType) {
    this.resourceServerRequiredType = resourceServerRequiredType;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerValidateAudienceEnabled(Boolean resourceServerValidateAudienceEnabled) {
    
    this.resourceServerValidateAudienceEnabled = resourceServerValidateAudienceEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the audience claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return resourceServerValidateAudienceEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_AUDIENCE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getResourceServerValidateAudienceEnabled() {
    return resourceServerValidateAudienceEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_AUDIENCE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerValidateAudienceEnabled(Boolean resourceServerValidateAudienceEnabled) {
    this.resourceServerValidateAudienceEnabled = resourceServerValidateAudienceEnabled;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerValidateIssuerEnabled(Boolean resourceServerValidateIssuerEnabled) {
    
    this.resourceServerValidateIssuerEnabled = resourceServerValidateIssuerEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the issuer claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return resourceServerValidateIssuerEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_ISSUER_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getResourceServerValidateIssuerEnabled() {
    return resourceServerValidateIssuerEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_ISSUER_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerValidateIssuerEnabled(Boolean resourceServerValidateIssuerEnabled) {
    this.resourceServerValidateIssuerEnabled = resourceServerValidateIssuerEnabled;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerValidateScopeEnabled(Boolean resourceServerValidateScopeEnabled) {
    
    this.resourceServerValidateScopeEnabled = resourceServerValidateScopeEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the scope claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return resourceServerValidateScopeEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_SCOPE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getResourceServerValidateScopeEnabled() {
    return resourceServerValidateScopeEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_SCOPE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerValidateScopeEnabled(Boolean resourceServerValidateScopeEnabled) {
    this.resourceServerValidateScopeEnabled = resourceServerValidateScopeEnabled;
  }


  public MsgVpnAuthenticationOauthProfile resourceServerValidateTypeEnabled(Boolean resourceServerValidateTypeEnabled) {
    
    this.resourceServerValidateTypeEnabled = resourceServerValidateTypeEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the TYP field in the access token header. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return resourceServerValidateTypeEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_TYPE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getResourceServerValidateTypeEnabled() {
    return resourceServerValidateTypeEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_TYPE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setResourceServerValidateTypeEnabled(Boolean resourceServerValidateTypeEnabled) {
    this.resourceServerValidateTypeEnabled = resourceServerValidateTypeEnabled;
  }


  public MsgVpnAuthenticationOauthProfile usernameClaimName(String usernameClaimName) {
    
    this.usernameClaimName = usernameClaimName;
    return this;
  }

   /**
   * The name of the username claim. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;sub\&quot;&#x60;.
   * @return usernameClaimName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_USERNAME_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getUsernameClaimName() {
    return usernameClaimName;
  }


  @JsonProperty(JSON_PROPERTY_USERNAME_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUsernameClaimName(String usernameClaimName) {
    this.usernameClaimName = usernameClaimName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnAuthenticationOauthProfile msgVpnAuthenticationOauthProfile = (MsgVpnAuthenticationOauthProfile) o;
    return Objects.equals(this.authorizationGroupsClaimName, msgVpnAuthenticationOauthProfile.authorizationGroupsClaimName) &&
        Objects.equals(this.clientId, msgVpnAuthenticationOauthProfile.clientId) &&
        Objects.equals(this.clientRequiredType, msgVpnAuthenticationOauthProfile.clientRequiredType) &&
        Objects.equals(this.clientSecret, msgVpnAuthenticationOauthProfile.clientSecret) &&
        Objects.equals(this.clientValidateTypeEnabled, msgVpnAuthenticationOauthProfile.clientValidateTypeEnabled) &&
        Objects.equals(this.disconnectOnTokenExpirationEnabled, msgVpnAuthenticationOauthProfile.disconnectOnTokenExpirationEnabled) &&
        Objects.equals(this.enabled, msgVpnAuthenticationOauthProfile.enabled) &&
        Objects.equals(this.endpointDiscovery, msgVpnAuthenticationOauthProfile.endpointDiscovery) &&
        Objects.equals(this.endpointDiscoveryRefreshInterval, msgVpnAuthenticationOauthProfile.endpointDiscoveryRefreshInterval) &&
        Objects.equals(this.endpointIntrospection, msgVpnAuthenticationOauthProfile.endpointIntrospection) &&
        Objects.equals(this.endpointIntrospectionTimeout, msgVpnAuthenticationOauthProfile.endpointIntrospectionTimeout) &&
        Objects.equals(this.endpointJwks, msgVpnAuthenticationOauthProfile.endpointJwks) &&
        Objects.equals(this.endpointJwksRefreshInterval, msgVpnAuthenticationOauthProfile.endpointJwksRefreshInterval) &&
        Objects.equals(this.endpointUserinfo, msgVpnAuthenticationOauthProfile.endpointUserinfo) &&
        Objects.equals(this.endpointUserinfoTimeout, msgVpnAuthenticationOauthProfile.endpointUserinfoTimeout) &&
        Objects.equals(this.issuer, msgVpnAuthenticationOauthProfile.issuer) &&
        Objects.equals(this.mqttUsernameValidateEnabled, msgVpnAuthenticationOauthProfile.mqttUsernameValidateEnabled) &&
        Objects.equals(this.msgVpnName, msgVpnAuthenticationOauthProfile.msgVpnName) &&
        Objects.equals(this.oauthProfileName, msgVpnAuthenticationOauthProfile.oauthProfileName) &&
        Objects.equals(this.oauthRole, msgVpnAuthenticationOauthProfile.oauthRole) &&
        Objects.equals(this.resourceServerParseAccessTokenEnabled, msgVpnAuthenticationOauthProfile.resourceServerParseAccessTokenEnabled) &&
        Objects.equals(this.resourceServerRequiredAudience, msgVpnAuthenticationOauthProfile.resourceServerRequiredAudience) &&
        Objects.equals(this.resourceServerRequiredIssuer, msgVpnAuthenticationOauthProfile.resourceServerRequiredIssuer) &&
        Objects.equals(this.resourceServerRequiredScope, msgVpnAuthenticationOauthProfile.resourceServerRequiredScope) &&
        Objects.equals(this.resourceServerRequiredType, msgVpnAuthenticationOauthProfile.resourceServerRequiredType) &&
        Objects.equals(this.resourceServerValidateAudienceEnabled, msgVpnAuthenticationOauthProfile.resourceServerValidateAudienceEnabled) &&
        Objects.equals(this.resourceServerValidateIssuerEnabled, msgVpnAuthenticationOauthProfile.resourceServerValidateIssuerEnabled) &&
        Objects.equals(this.resourceServerValidateScopeEnabled, msgVpnAuthenticationOauthProfile.resourceServerValidateScopeEnabled) &&
        Objects.equals(this.resourceServerValidateTypeEnabled, msgVpnAuthenticationOauthProfile.resourceServerValidateTypeEnabled) &&
        Objects.equals(this.usernameClaimName, msgVpnAuthenticationOauthProfile.usernameClaimName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorizationGroupsClaimName, clientId, clientRequiredType, clientSecret, clientValidateTypeEnabled, disconnectOnTokenExpirationEnabled, enabled, endpointDiscovery, endpointDiscoveryRefreshInterval, endpointIntrospection, endpointIntrospectionTimeout, endpointJwks, endpointJwksRefreshInterval, endpointUserinfo, endpointUserinfoTimeout, issuer, mqttUsernameValidateEnabled, msgVpnName, oauthProfileName, oauthRole, resourceServerParseAccessTokenEnabled, resourceServerRequiredAudience, resourceServerRequiredIssuer, resourceServerRequiredScope, resourceServerRequiredType, resourceServerValidateAudienceEnabled, resourceServerValidateIssuerEnabled, resourceServerValidateScopeEnabled, resourceServerValidateTypeEnabled, usernameClaimName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnAuthenticationOauthProfile {\n");
    sb.append("    authorizationGroupsClaimName: ").append(toIndentedString(authorizationGroupsClaimName)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    clientRequiredType: ").append(toIndentedString(clientRequiredType)).append("\n");
    sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
    sb.append("    clientValidateTypeEnabled: ").append(toIndentedString(clientValidateTypeEnabled)).append("\n");
    sb.append("    disconnectOnTokenExpirationEnabled: ").append(toIndentedString(disconnectOnTokenExpirationEnabled)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    endpointDiscovery: ").append(toIndentedString(endpointDiscovery)).append("\n");
    sb.append("    endpointDiscoveryRefreshInterval: ").append(toIndentedString(endpointDiscoveryRefreshInterval)).append("\n");
    sb.append("    endpointIntrospection: ").append(toIndentedString(endpointIntrospection)).append("\n");
    sb.append("    endpointIntrospectionTimeout: ").append(toIndentedString(endpointIntrospectionTimeout)).append("\n");
    sb.append("    endpointJwks: ").append(toIndentedString(endpointJwks)).append("\n");
    sb.append("    endpointJwksRefreshInterval: ").append(toIndentedString(endpointJwksRefreshInterval)).append("\n");
    sb.append("    endpointUserinfo: ").append(toIndentedString(endpointUserinfo)).append("\n");
    sb.append("    endpointUserinfoTimeout: ").append(toIndentedString(endpointUserinfoTimeout)).append("\n");
    sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
    sb.append("    mqttUsernameValidateEnabled: ").append(toIndentedString(mqttUsernameValidateEnabled)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    oauthProfileName: ").append(toIndentedString(oauthProfileName)).append("\n");
    sb.append("    oauthRole: ").append(toIndentedString(oauthRole)).append("\n");
    sb.append("    resourceServerParseAccessTokenEnabled: ").append(toIndentedString(resourceServerParseAccessTokenEnabled)).append("\n");
    sb.append("    resourceServerRequiredAudience: ").append(toIndentedString(resourceServerRequiredAudience)).append("\n");
    sb.append("    resourceServerRequiredIssuer: ").append(toIndentedString(resourceServerRequiredIssuer)).append("\n");
    sb.append("    resourceServerRequiredScope: ").append(toIndentedString(resourceServerRequiredScope)).append("\n");
    sb.append("    resourceServerRequiredType: ").append(toIndentedString(resourceServerRequiredType)).append("\n");
    sb.append("    resourceServerValidateAudienceEnabled: ").append(toIndentedString(resourceServerValidateAudienceEnabled)).append("\n");
    sb.append("    resourceServerValidateIssuerEnabled: ").append(toIndentedString(resourceServerValidateIssuerEnabled)).append("\n");
    sb.append("    resourceServerValidateScopeEnabled: ").append(toIndentedString(resourceServerValidateScopeEnabled)).append("\n");
    sb.append("    resourceServerValidateTypeEnabled: ").append(toIndentedString(resourceServerValidateTypeEnabled)).append("\n");
    sb.append("    usernameClaimName: ").append(toIndentedString(usernameClaimName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

