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
 * OauthProfile
 */
@JsonPropertyOrder({
  OauthProfile.JSON_PROPERTY_ACCESS_LEVEL_GROUPS_CLAIM_NAME,
  OauthProfile.JSON_PROPERTY_CLIENT_ID,
  OauthProfile.JSON_PROPERTY_CLIENT_REDIRECT_URI,
  OauthProfile.JSON_PROPERTY_CLIENT_REQUIRED_TYPE,
  OauthProfile.JSON_PROPERTY_CLIENT_SCOPE,
  OauthProfile.JSON_PROPERTY_CLIENT_SECRET,
  OauthProfile.JSON_PROPERTY_CLIENT_VALIDATE_TYPE_ENABLED,
  OauthProfile.JSON_PROPERTY_DEFAULT_GLOBAL_ACCESS_LEVEL,
  OauthProfile.JSON_PROPERTY_DEFAULT_MSG_VPN_ACCESS_LEVEL,
  OauthProfile.JSON_PROPERTY_DISPLAY_NAME,
  OauthProfile.JSON_PROPERTY_ENABLED,
  OauthProfile.JSON_PROPERTY_ENDPOINT_AUTHORIZATION,
  OauthProfile.JSON_PROPERTY_ENDPOINT_DISCOVERY,
  OauthProfile.JSON_PROPERTY_ENDPOINT_DISCOVERY_REFRESH_INTERVAL,
  OauthProfile.JSON_PROPERTY_ENDPOINT_INTROSPECTION,
  OauthProfile.JSON_PROPERTY_ENDPOINT_INTROSPECTION_TIMEOUT,
  OauthProfile.JSON_PROPERTY_ENDPOINT_JWKS,
  OauthProfile.JSON_PROPERTY_ENDPOINT_JWKS_REFRESH_INTERVAL,
  OauthProfile.JSON_PROPERTY_ENDPOINT_TOKEN,
  OauthProfile.JSON_PROPERTY_ENDPOINT_TOKEN_TIMEOUT,
  OauthProfile.JSON_PROPERTY_ENDPOINT_USERINFO,
  OauthProfile.JSON_PROPERTY_ENDPOINT_USERINFO_TIMEOUT,
  OauthProfile.JSON_PROPERTY_INTERACTIVE_ENABLED,
  OauthProfile.JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_EXPIRED_SESSION,
  OauthProfile.JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_NEW_SESSION,
  OauthProfile.JSON_PROPERTY_ISSUER,
  OauthProfile.JSON_PROPERTY_OAUTH_PROFILE_NAME,
  OauthProfile.JSON_PROPERTY_OAUTH_ROLE,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_PARSE_ACCESS_TOKEN_ENABLED,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_AUDIENCE,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_ISSUER,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_SCOPE,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_REQUIRED_TYPE,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_AUDIENCE_ENABLED,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_ISSUER_ENABLED,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_SCOPE_ENABLED,
  OauthProfile.JSON_PROPERTY_RESOURCE_SERVER_VALIDATE_TYPE_ENABLED,
  OauthProfile.JSON_PROPERTY_SEMP_ENABLED,
  OauthProfile.JSON_PROPERTY_USERNAME_CLAIM_NAME
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class OauthProfile {
  public static final String JSON_PROPERTY_ACCESS_LEVEL_GROUPS_CLAIM_NAME = "accessLevelGroupsClaimName";
  private String accessLevelGroupsClaimName;

  public static final String JSON_PROPERTY_CLIENT_ID = "clientId";
  private String clientId;

  public static final String JSON_PROPERTY_CLIENT_REDIRECT_URI = "clientRedirectUri";
  private String clientRedirectUri;

  public static final String JSON_PROPERTY_CLIENT_REQUIRED_TYPE = "clientRequiredType";
  private String clientRequiredType;

  public static final String JSON_PROPERTY_CLIENT_SCOPE = "clientScope";
  private String clientScope;

  public static final String JSON_PROPERTY_CLIENT_SECRET = "clientSecret";
  private String clientSecret;

  public static final String JSON_PROPERTY_CLIENT_VALIDATE_TYPE_ENABLED = "clientValidateTypeEnabled";
  private Boolean clientValidateTypeEnabled;

  /**
   * The default global access level for this OAuth profile. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to global data. \&quot;read-only\&quot; - User has read-only access to global data. \&quot;read-write\&quot; - User has read-write access to most global data. \&quot;admin\&quot; - User has read-write access to all global data. &lt;/pre&gt; 
   */
  public enum DefaultGlobalAccessLevelEnum {
    NONE("none"),
    
    READ_ONLY("read-only"),
    
    READ_WRITE("read-write"),
    
    ADMIN("admin");

    private String value;

    DefaultGlobalAccessLevelEnum(String value) {
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
    public static DefaultGlobalAccessLevelEnum fromValue(String value) {
      for (DefaultGlobalAccessLevelEnum b : DefaultGlobalAccessLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_DEFAULT_GLOBAL_ACCESS_LEVEL = "defaultGlobalAccessLevel";
  private DefaultGlobalAccessLevelEnum defaultGlobalAccessLevel;

  /**
   * The default message VPN access level for the OAuth profile. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to a Message VPN. \&quot;read-only\&quot; - User has read-only access to a Message VPN. \&quot;read-write\&quot; - User has read-write access to most Message VPN settings. &lt;/pre&gt; 
   */
  public enum DefaultMsgVpnAccessLevelEnum {
    NONE("none"),
    
    READ_ONLY("read-only"),
    
    READ_WRITE("read-write");

    private String value;

    DefaultMsgVpnAccessLevelEnum(String value) {
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
    public static DefaultMsgVpnAccessLevelEnum fromValue(String value) {
      for (DefaultMsgVpnAccessLevelEnum b : DefaultMsgVpnAccessLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_DEFAULT_MSG_VPN_ACCESS_LEVEL = "defaultMsgVpnAccessLevel";
  private DefaultMsgVpnAccessLevelEnum defaultMsgVpnAccessLevel;

  public static final String JSON_PROPERTY_DISPLAY_NAME = "displayName";
  private String displayName;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_ENDPOINT_AUTHORIZATION = "endpointAuthorization";
  private String endpointAuthorization;

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

  public static final String JSON_PROPERTY_ENDPOINT_TOKEN = "endpointToken";
  private String endpointToken;

  public static final String JSON_PROPERTY_ENDPOINT_TOKEN_TIMEOUT = "endpointTokenTimeout";
  private Integer endpointTokenTimeout;

  public static final String JSON_PROPERTY_ENDPOINT_USERINFO = "endpointUserinfo";
  private String endpointUserinfo;

  public static final String JSON_PROPERTY_ENDPOINT_USERINFO_TIMEOUT = "endpointUserinfoTimeout";
  private Integer endpointUserinfoTimeout;

  public static final String JSON_PROPERTY_INTERACTIVE_ENABLED = "interactiveEnabled";
  private Boolean interactiveEnabled;

  public static final String JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_EXPIRED_SESSION = "interactivePromptForExpiredSession";
  private String interactivePromptForExpiredSession;

  public static final String JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_NEW_SESSION = "interactivePromptForNewSession";
  private String interactivePromptForNewSession;

  public static final String JSON_PROPERTY_ISSUER = "issuer";
  private String issuer;

  public static final String JSON_PROPERTY_OAUTH_PROFILE_NAME = "oauthProfileName";
  private String oauthProfileName;

  /**
   * The OAuth role of the broker. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;client\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;client\&quot; - The broker is in the OAuth client role. \&quot;resource-server\&quot; - The broker is in the OAuth resource server role. &lt;/pre&gt; 
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

  public static final String JSON_PROPERTY_SEMP_ENABLED = "sempEnabled";
  private Boolean sempEnabled;

  public static final String JSON_PROPERTY_USERNAME_CLAIM_NAME = "usernameClaimName";
  private String usernameClaimName;

  public OauthProfile() {
  }

  public OauthProfile accessLevelGroupsClaimName(String accessLevelGroupsClaimName) {
    
    this.accessLevelGroupsClaimName = accessLevelGroupsClaimName;
    return this;
  }

   /**
   * The name of the groups claim. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;groups\&quot;&#x60;.
   * @return accessLevelGroupsClaimName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACCESS_LEVEL_GROUPS_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAccessLevelGroupsClaimName() {
    return accessLevelGroupsClaimName;
  }


  @JsonProperty(JSON_PROPERTY_ACCESS_LEVEL_GROUPS_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAccessLevelGroupsClaimName(String accessLevelGroupsClaimName) {
    this.accessLevelGroupsClaimName = accessLevelGroupsClaimName;
  }


  public OauthProfile clientId(String clientId) {
    
    this.clientId = clientId;
    return this;
  }

   /**
   * The OAuth client id. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile clientRedirectUri(String clientRedirectUri) {
    
    this.clientRedirectUri = clientRedirectUri;
    return this;
  }

   /**
   * The OAuth redirect URI. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return clientRedirectUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_REDIRECT_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientRedirectUri() {
    return clientRedirectUri;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_REDIRECT_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientRedirectUri(String clientRedirectUri) {
    this.clientRedirectUri = clientRedirectUri;
  }


  public OauthProfile clientRequiredType(String clientRequiredType) {
    
    this.clientRequiredType = clientRequiredType;
    return this;
  }

   /**
   * The required value for the TYP field in the ID token header. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;JWT\&quot;&#x60;.
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


  public OauthProfile clientScope(String clientScope) {
    
    this.clientScope = clientScope;
    return this;
  }

   /**
   * The OAuth scope. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;openid email\&quot;&#x60;.
   * @return clientScope
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_SCOPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientScope() {
    return clientScope;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_SCOPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientScope(String clientScope) {
    this.clientScope = clientScope;
  }


  public OauthProfile clientSecret(String clientSecret) {
    
    this.clientSecret = clientSecret;
    return this;
  }

   /**
   * The OAuth client secret. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile clientValidateTypeEnabled(Boolean clientValidateTypeEnabled) {
    
    this.clientValidateTypeEnabled = clientValidateTypeEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the TYP field in the ID token header. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public OauthProfile defaultGlobalAccessLevel(DefaultGlobalAccessLevelEnum defaultGlobalAccessLevel) {
    
    this.defaultGlobalAccessLevel = defaultGlobalAccessLevel;
    return this;
  }

   /**
   * The default global access level for this OAuth profile. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to global data. \&quot;read-only\&quot; - User has read-only access to global data. \&quot;read-write\&quot; - User has read-write access to most global data. \&quot;admin\&quot; - User has read-write access to all global data. &lt;/pre&gt; 
   * @return defaultGlobalAccessLevel
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DEFAULT_GLOBAL_ACCESS_LEVEL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DefaultGlobalAccessLevelEnum getDefaultGlobalAccessLevel() {
    return defaultGlobalAccessLevel;
  }


  @JsonProperty(JSON_PROPERTY_DEFAULT_GLOBAL_ACCESS_LEVEL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDefaultGlobalAccessLevel(DefaultGlobalAccessLevelEnum defaultGlobalAccessLevel) {
    this.defaultGlobalAccessLevel = defaultGlobalAccessLevel;
  }


  public OauthProfile defaultMsgVpnAccessLevel(DefaultMsgVpnAccessLevelEnum defaultMsgVpnAccessLevel) {
    
    this.defaultMsgVpnAccessLevel = defaultMsgVpnAccessLevel;
    return this;
  }

   /**
   * The default message VPN access level for the OAuth profile. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to a Message VPN. \&quot;read-only\&quot; - User has read-only access to a Message VPN. \&quot;read-write\&quot; - User has read-write access to most Message VPN settings. &lt;/pre&gt; 
   * @return defaultMsgVpnAccessLevel
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DEFAULT_MSG_VPN_ACCESS_LEVEL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DefaultMsgVpnAccessLevelEnum getDefaultMsgVpnAccessLevel() {
    return defaultMsgVpnAccessLevel;
  }


  @JsonProperty(JSON_PROPERTY_DEFAULT_MSG_VPN_ACCESS_LEVEL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDefaultMsgVpnAccessLevel(DefaultMsgVpnAccessLevelEnum defaultMsgVpnAccessLevel) {
    this.defaultMsgVpnAccessLevel = defaultMsgVpnAccessLevel;
  }


  public OauthProfile displayName(String displayName) {
    
    this.displayName = displayName;
    return this;
  }

   /**
   * The user friendly name for the OAuth profile. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return displayName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DISPLAY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDisplayName() {
    return displayName;
  }


  @JsonProperty(JSON_PROPERTY_DISPLAY_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }


  public OauthProfile enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the OAuth profile. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
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


  public OauthProfile endpointAuthorization(String endpointAuthorization) {
    
    this.endpointAuthorization = endpointAuthorization;
    return this;
  }

   /**
   * The OAuth authorization endpoint. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return endpointAuthorization
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_AUTHORIZATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEndpointAuthorization() {
    return endpointAuthorization;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_AUTHORIZATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointAuthorization(String endpointAuthorization) {
    this.endpointAuthorization = endpointAuthorization;
  }


  public OauthProfile endpointDiscovery(String endpointDiscovery) {
    
    this.endpointDiscovery = endpointDiscovery;
    return this;
  }

   /**
   * The OpenID Connect discovery endpoint or OAuth Authorization Server Metadata endpoint. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile endpointDiscoveryRefreshInterval(Integer endpointDiscoveryRefreshInterval) {
    
    this.endpointDiscoveryRefreshInterval = endpointDiscoveryRefreshInterval;
    return this;
  }

   /**
   * The number of seconds between discovery endpoint requests. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;86400&#x60;.
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


  public OauthProfile endpointIntrospection(String endpointIntrospection) {
    
    this.endpointIntrospection = endpointIntrospection;
    return this;
  }

   /**
   * The OAuth introspection endpoint. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile endpointIntrospectionTimeout(Integer endpointIntrospectionTimeout) {
    
    this.endpointIntrospectionTimeout = endpointIntrospectionTimeout;
    return this;
  }

   /**
   * The maximum time in seconds a token introspection request is allowed to take. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1&#x60;.
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


  public OauthProfile endpointJwks(String endpointJwks) {
    
    this.endpointJwks = endpointJwks;
    return this;
  }

   /**
   * The OAuth JWKS endpoint. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile endpointJwksRefreshInterval(Integer endpointJwksRefreshInterval) {
    
    this.endpointJwksRefreshInterval = endpointJwksRefreshInterval;
    return this;
  }

   /**
   * The number of seconds between JWKS endpoint requests. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;86400&#x60;.
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


  public OauthProfile endpointToken(String endpointToken) {
    
    this.endpointToken = endpointToken;
    return this;
  }

   /**
   * The OAuth token endpoint. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return endpointToken
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_TOKEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEndpointToken() {
    return endpointToken;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_TOKEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointToken(String endpointToken) {
    this.endpointToken = endpointToken;
  }


  public OauthProfile endpointTokenTimeout(Integer endpointTokenTimeout) {
    
    this.endpointTokenTimeout = endpointTokenTimeout;
    return this;
  }

   /**
   * The maximum time in seconds a token request is allowed to take. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1&#x60;.
   * @return endpointTokenTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENDPOINT_TOKEN_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getEndpointTokenTimeout() {
    return endpointTokenTimeout;
  }


  @JsonProperty(JSON_PROPERTY_ENDPOINT_TOKEN_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEndpointTokenTimeout(Integer endpointTokenTimeout) {
    this.endpointTokenTimeout = endpointTokenTimeout;
  }


  public OauthProfile endpointUserinfo(String endpointUserinfo) {
    
    this.endpointUserinfo = endpointUserinfo;
    return this;
  }

   /**
   * The OpenID Connect Userinfo endpoint. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile endpointUserinfoTimeout(Integer endpointUserinfoTimeout) {
    
    this.endpointUserinfoTimeout = endpointUserinfoTimeout;
    return this;
  }

   /**
   * The maximum time in seconds a userinfo request is allowed to take. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1&#x60;.
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


  public OauthProfile interactiveEnabled(Boolean interactiveEnabled) {
    
    this.interactiveEnabled = interactiveEnabled;
    return this;
  }

   /**
   * Enable or disable interactive logins via this OAuth provider. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
   * @return interactiveEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INTERACTIVE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getInteractiveEnabled() {
    return interactiveEnabled;
  }


  @JsonProperty(JSON_PROPERTY_INTERACTIVE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInteractiveEnabled(Boolean interactiveEnabled) {
    this.interactiveEnabled = interactiveEnabled;
  }


  public OauthProfile interactivePromptForExpiredSession(String interactivePromptForExpiredSession) {
    
    this.interactivePromptForExpiredSession = interactivePromptForExpiredSession;
    return this;
  }

   /**
   * The value of the prompt parameter provided to the OAuth authorization server for login requests where the session has expired. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return interactivePromptForExpiredSession
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_EXPIRED_SESSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getInteractivePromptForExpiredSession() {
    return interactivePromptForExpiredSession;
  }


  @JsonProperty(JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_EXPIRED_SESSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInteractivePromptForExpiredSession(String interactivePromptForExpiredSession) {
    this.interactivePromptForExpiredSession = interactivePromptForExpiredSession;
  }


  public OauthProfile interactivePromptForNewSession(String interactivePromptForNewSession) {
    
    this.interactivePromptForNewSession = interactivePromptForNewSession;
    return this;
  }

   /**
   * The value of the prompt parameter provided to the OAuth authorization server for login requests where the session is new or the user has explicitly logged out. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;select_account\&quot;&#x60;.
   * @return interactivePromptForNewSession
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_NEW_SESSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getInteractivePromptForNewSession() {
    return interactivePromptForNewSession;
  }


  @JsonProperty(JSON_PROPERTY_INTERACTIVE_PROMPT_FOR_NEW_SESSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInteractivePromptForNewSession(String interactivePromptForNewSession) {
    this.interactivePromptForNewSession = interactivePromptForNewSession;
  }


  public OauthProfile issuer(String issuer) {
    
    this.issuer = issuer;
    return this;
  }

   /**
   * The Issuer Identifier for the OAuth provider. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile oauthProfileName(String oauthProfileName) {
    
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


  public OauthProfile oauthRole(OauthRoleEnum oauthRole) {
    
    this.oauthRole = oauthRole;
    return this;
  }

   /**
   * The OAuth role of the broker. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;client\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;client\&quot; - The broker is in the OAuth client role. \&quot;resource-server\&quot; - The broker is in the OAuth resource server role. &lt;/pre&gt; 
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


  public OauthProfile resourceServerParseAccessTokenEnabled(Boolean resourceServerParseAccessTokenEnabled) {
    
    this.resourceServerParseAccessTokenEnabled = resourceServerParseAccessTokenEnabled;
    return this;
  }

   /**
   * Enable or disable parsing of the access token as a JWT. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public OauthProfile resourceServerRequiredAudience(String resourceServerRequiredAudience) {
    
    this.resourceServerRequiredAudience = resourceServerRequiredAudience;
    return this;
  }

   /**
   * The required audience value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile resourceServerRequiredIssuer(String resourceServerRequiredIssuer) {
    
    this.resourceServerRequiredIssuer = resourceServerRequiredIssuer;
    return this;
  }

   /**
   * The required issuer value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile resourceServerRequiredScope(String resourceServerRequiredScope) {
    
    this.resourceServerRequiredScope = resourceServerRequiredScope;
    return this;
  }

   /**
   * A space-separated list of scopes that must be present in the scope claim. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public OauthProfile resourceServerRequiredType(String resourceServerRequiredType) {
    
    this.resourceServerRequiredType = resourceServerRequiredType;
    return this;
  }

   /**
   * The required TYP value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;at+jwt\&quot;&#x60;.
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


  public OauthProfile resourceServerValidateAudienceEnabled(Boolean resourceServerValidateAudienceEnabled) {
    
    this.resourceServerValidateAudienceEnabled = resourceServerValidateAudienceEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the audience claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public OauthProfile resourceServerValidateIssuerEnabled(Boolean resourceServerValidateIssuerEnabled) {
    
    this.resourceServerValidateIssuerEnabled = resourceServerValidateIssuerEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the issuer claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public OauthProfile resourceServerValidateScopeEnabled(Boolean resourceServerValidateScopeEnabled) {
    
    this.resourceServerValidateScopeEnabled = resourceServerValidateScopeEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the scope claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public OauthProfile resourceServerValidateTypeEnabled(Boolean resourceServerValidateTypeEnabled) {
    
    this.resourceServerValidateTypeEnabled = resourceServerValidateTypeEnabled;
    return this;
  }

   /**
   * Enable or disable verification of the TYP field in the access token header. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public OauthProfile sempEnabled(Boolean sempEnabled) {
    
    this.sempEnabled = sempEnabled;
    return this;
  }

   /**
   * Enable or disable authentication of SEMP requests with OAuth tokens. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
   * @return sempEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEMP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSempEnabled() {
    return sempEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SEMP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSempEnabled(Boolean sempEnabled) {
    this.sempEnabled = sempEnabled;
  }


  public OauthProfile usernameClaimName(String usernameClaimName) {
    
    this.usernameClaimName = usernameClaimName;
    return this;
  }

   /**
   * The name of the username claim. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;sub\&quot;&#x60;.
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
    OauthProfile oauthProfile = (OauthProfile) o;
    return Objects.equals(this.accessLevelGroupsClaimName, oauthProfile.accessLevelGroupsClaimName) &&
        Objects.equals(this.clientId, oauthProfile.clientId) &&
        Objects.equals(this.clientRedirectUri, oauthProfile.clientRedirectUri) &&
        Objects.equals(this.clientRequiredType, oauthProfile.clientRequiredType) &&
        Objects.equals(this.clientScope, oauthProfile.clientScope) &&
        Objects.equals(this.clientSecret, oauthProfile.clientSecret) &&
        Objects.equals(this.clientValidateTypeEnabled, oauthProfile.clientValidateTypeEnabled) &&
        Objects.equals(this.defaultGlobalAccessLevel, oauthProfile.defaultGlobalAccessLevel) &&
        Objects.equals(this.defaultMsgVpnAccessLevel, oauthProfile.defaultMsgVpnAccessLevel) &&
        Objects.equals(this.displayName, oauthProfile.displayName) &&
        Objects.equals(this.enabled, oauthProfile.enabled) &&
        Objects.equals(this.endpointAuthorization, oauthProfile.endpointAuthorization) &&
        Objects.equals(this.endpointDiscovery, oauthProfile.endpointDiscovery) &&
        Objects.equals(this.endpointDiscoveryRefreshInterval, oauthProfile.endpointDiscoveryRefreshInterval) &&
        Objects.equals(this.endpointIntrospection, oauthProfile.endpointIntrospection) &&
        Objects.equals(this.endpointIntrospectionTimeout, oauthProfile.endpointIntrospectionTimeout) &&
        Objects.equals(this.endpointJwks, oauthProfile.endpointJwks) &&
        Objects.equals(this.endpointJwksRefreshInterval, oauthProfile.endpointJwksRefreshInterval) &&
        Objects.equals(this.endpointToken, oauthProfile.endpointToken) &&
        Objects.equals(this.endpointTokenTimeout, oauthProfile.endpointTokenTimeout) &&
        Objects.equals(this.endpointUserinfo, oauthProfile.endpointUserinfo) &&
        Objects.equals(this.endpointUserinfoTimeout, oauthProfile.endpointUserinfoTimeout) &&
        Objects.equals(this.interactiveEnabled, oauthProfile.interactiveEnabled) &&
        Objects.equals(this.interactivePromptForExpiredSession, oauthProfile.interactivePromptForExpiredSession) &&
        Objects.equals(this.interactivePromptForNewSession, oauthProfile.interactivePromptForNewSession) &&
        Objects.equals(this.issuer, oauthProfile.issuer) &&
        Objects.equals(this.oauthProfileName, oauthProfile.oauthProfileName) &&
        Objects.equals(this.oauthRole, oauthProfile.oauthRole) &&
        Objects.equals(this.resourceServerParseAccessTokenEnabled, oauthProfile.resourceServerParseAccessTokenEnabled) &&
        Objects.equals(this.resourceServerRequiredAudience, oauthProfile.resourceServerRequiredAudience) &&
        Objects.equals(this.resourceServerRequiredIssuer, oauthProfile.resourceServerRequiredIssuer) &&
        Objects.equals(this.resourceServerRequiredScope, oauthProfile.resourceServerRequiredScope) &&
        Objects.equals(this.resourceServerRequiredType, oauthProfile.resourceServerRequiredType) &&
        Objects.equals(this.resourceServerValidateAudienceEnabled, oauthProfile.resourceServerValidateAudienceEnabled) &&
        Objects.equals(this.resourceServerValidateIssuerEnabled, oauthProfile.resourceServerValidateIssuerEnabled) &&
        Objects.equals(this.resourceServerValidateScopeEnabled, oauthProfile.resourceServerValidateScopeEnabled) &&
        Objects.equals(this.resourceServerValidateTypeEnabled, oauthProfile.resourceServerValidateTypeEnabled) &&
        Objects.equals(this.sempEnabled, oauthProfile.sempEnabled) &&
        Objects.equals(this.usernameClaimName, oauthProfile.usernameClaimName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessLevelGroupsClaimName, clientId, clientRedirectUri, clientRequiredType, clientScope, clientSecret, clientValidateTypeEnabled, defaultGlobalAccessLevel, defaultMsgVpnAccessLevel, displayName, enabled, endpointAuthorization, endpointDiscovery, endpointDiscoveryRefreshInterval, endpointIntrospection, endpointIntrospectionTimeout, endpointJwks, endpointJwksRefreshInterval, endpointToken, endpointTokenTimeout, endpointUserinfo, endpointUserinfoTimeout, interactiveEnabled, interactivePromptForExpiredSession, interactivePromptForNewSession, issuer, oauthProfileName, oauthRole, resourceServerParseAccessTokenEnabled, resourceServerRequiredAudience, resourceServerRequiredIssuer, resourceServerRequiredScope, resourceServerRequiredType, resourceServerValidateAudienceEnabled, resourceServerValidateIssuerEnabled, resourceServerValidateScopeEnabled, resourceServerValidateTypeEnabled, sempEnabled, usernameClaimName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OauthProfile {\n");
    sb.append("    accessLevelGroupsClaimName: ").append(toIndentedString(accessLevelGroupsClaimName)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    clientRedirectUri: ").append(toIndentedString(clientRedirectUri)).append("\n");
    sb.append("    clientRequiredType: ").append(toIndentedString(clientRequiredType)).append("\n");
    sb.append("    clientScope: ").append(toIndentedString(clientScope)).append("\n");
    sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
    sb.append("    clientValidateTypeEnabled: ").append(toIndentedString(clientValidateTypeEnabled)).append("\n");
    sb.append("    defaultGlobalAccessLevel: ").append(toIndentedString(defaultGlobalAccessLevel)).append("\n");
    sb.append("    defaultMsgVpnAccessLevel: ").append(toIndentedString(defaultMsgVpnAccessLevel)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    endpointAuthorization: ").append(toIndentedString(endpointAuthorization)).append("\n");
    sb.append("    endpointDiscovery: ").append(toIndentedString(endpointDiscovery)).append("\n");
    sb.append("    endpointDiscoveryRefreshInterval: ").append(toIndentedString(endpointDiscoveryRefreshInterval)).append("\n");
    sb.append("    endpointIntrospection: ").append(toIndentedString(endpointIntrospection)).append("\n");
    sb.append("    endpointIntrospectionTimeout: ").append(toIndentedString(endpointIntrospectionTimeout)).append("\n");
    sb.append("    endpointJwks: ").append(toIndentedString(endpointJwks)).append("\n");
    sb.append("    endpointJwksRefreshInterval: ").append(toIndentedString(endpointJwksRefreshInterval)).append("\n");
    sb.append("    endpointToken: ").append(toIndentedString(endpointToken)).append("\n");
    sb.append("    endpointTokenTimeout: ").append(toIndentedString(endpointTokenTimeout)).append("\n");
    sb.append("    endpointUserinfo: ").append(toIndentedString(endpointUserinfo)).append("\n");
    sb.append("    endpointUserinfoTimeout: ").append(toIndentedString(endpointUserinfoTimeout)).append("\n");
    sb.append("    interactiveEnabled: ").append(toIndentedString(interactiveEnabled)).append("\n");
    sb.append("    interactivePromptForExpiredSession: ").append(toIndentedString(interactivePromptForExpiredSession)).append("\n");
    sb.append("    interactivePromptForNewSession: ").append(toIndentedString(interactivePromptForNewSession)).append("\n");
    sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
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
    sb.append("    sempEnabled: ").append(toIndentedString(sempEnabled)).append("\n");
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

