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
 * MsgVpnAuthenticationOauthProvider
 */
@JsonPropertyOrder({
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUDIENCE_CLAIM_NAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUDIENCE_CLAIM_SOURCE,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUDIENCE_CLAIM_VALUE,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUDIENCE_VALIDATION_ENABLED,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_NAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_SOURCE,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_AUTHORIZATION_GROUP_ENABLED,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_DISCONNECT_ON_TOKEN_EXPIRATION_ENABLED,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_ENABLED,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_JWKS_REFRESH_INTERVAL,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_JWKS_URI,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_OAUTH_PROVIDER_NAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_TOKEN_IGNORE_TIME_LIMITS_ENABLED,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_TOKEN_INTROSPECTION_PARAMETER_NAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_TOKEN_INTROSPECTION_PASSWORD,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_TOKEN_INTROSPECTION_TIMEOUT,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_TOKEN_INTROSPECTION_URI,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_TOKEN_INTROSPECTION_USERNAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_USERNAME_CLAIM_NAME,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_USERNAME_CLAIM_SOURCE,
  MsgVpnAuthenticationOauthProvider.JSON_PROPERTY_USERNAME_VALIDATE_ENABLED
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-17T23:49:01.929728+01:00[Europe/London]")
public class MsgVpnAuthenticationOauthProvider {
  public static final String JSON_PROPERTY_AUDIENCE_CLAIM_NAME = "audienceClaimName";
  private String audienceClaimName;

  /**
   * The audience claim source, indicating where to search for the audience value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   */
  public enum AudienceClaimSourceEnum {
    ACCESS_TOKEN("access-token"),
    
    ID_TOKEN("id-token"),
    
    INTROSPECTION("introspection");

    private String value;

    AudienceClaimSourceEnum(String value) {
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
    public static AudienceClaimSourceEnum fromValue(String value) {
      for (AudienceClaimSourceEnum b : AudienceClaimSourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUDIENCE_CLAIM_SOURCE = "audienceClaimSource";
  private AudienceClaimSourceEnum audienceClaimSource;

  public static final String JSON_PROPERTY_AUDIENCE_CLAIM_VALUE = "audienceClaimValue";
  private String audienceClaimValue;

  public static final String JSON_PROPERTY_AUDIENCE_VALIDATION_ENABLED = "audienceValidationEnabled";
  private Boolean audienceValidationEnabled;

  public static final String JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_NAME = "authorizationGroupClaimName";
  private String authorizationGroupClaimName;

  /**
   * The authorization group claim source, indicating where to search for the authorization group name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   */
  public enum AuthorizationGroupClaimSourceEnum {
    ACCESS_TOKEN("access-token"),
    
    ID_TOKEN("id-token"),
    
    INTROSPECTION("introspection");

    private String value;

    AuthorizationGroupClaimSourceEnum(String value) {
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
    public static AuthorizationGroupClaimSourceEnum fromValue(String value) {
      for (AuthorizationGroupClaimSourceEnum b : AuthorizationGroupClaimSourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_SOURCE = "authorizationGroupClaimSource";
  private AuthorizationGroupClaimSourceEnum authorizationGroupClaimSource;

  public static final String JSON_PROPERTY_AUTHORIZATION_GROUP_ENABLED = "authorizationGroupEnabled";
  private Boolean authorizationGroupEnabled;

  public static final String JSON_PROPERTY_DISCONNECT_ON_TOKEN_EXPIRATION_ENABLED = "disconnectOnTokenExpirationEnabled";
  private Boolean disconnectOnTokenExpirationEnabled;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_JWKS_REFRESH_INTERVAL = "jwksRefreshInterval";
  private Integer jwksRefreshInterval;

  public static final String JSON_PROPERTY_JWKS_URI = "jwksUri";
  private String jwksUri;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_OAUTH_PROVIDER_NAME = "oauthProviderName";
  private String oauthProviderName;

  public static final String JSON_PROPERTY_TOKEN_IGNORE_TIME_LIMITS_ENABLED = "tokenIgnoreTimeLimitsEnabled";
  private Boolean tokenIgnoreTimeLimitsEnabled;

  public static final String JSON_PROPERTY_TOKEN_INTROSPECTION_PARAMETER_NAME = "tokenIntrospectionParameterName";
  private String tokenIntrospectionParameterName;

  public static final String JSON_PROPERTY_TOKEN_INTROSPECTION_PASSWORD = "tokenIntrospectionPassword";
  private String tokenIntrospectionPassword;

  public static final String JSON_PROPERTY_TOKEN_INTROSPECTION_TIMEOUT = "tokenIntrospectionTimeout";
  private Integer tokenIntrospectionTimeout;

  public static final String JSON_PROPERTY_TOKEN_INTROSPECTION_URI = "tokenIntrospectionUri";
  private String tokenIntrospectionUri;

  public static final String JSON_PROPERTY_TOKEN_INTROSPECTION_USERNAME = "tokenIntrospectionUsername";
  private String tokenIntrospectionUsername;

  public static final String JSON_PROPERTY_USERNAME_CLAIM_NAME = "usernameClaimName";
  private String usernameClaimName;

  /**
   * The username claim source, indicating where to search for the username value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   */
  public enum UsernameClaimSourceEnum {
    ACCESS_TOKEN("access-token"),
    
    ID_TOKEN("id-token"),
    
    INTROSPECTION("introspection");

    private String value;

    UsernameClaimSourceEnum(String value) {
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
    public static UsernameClaimSourceEnum fromValue(String value) {
      for (UsernameClaimSourceEnum b : UsernameClaimSourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_USERNAME_CLAIM_SOURCE = "usernameClaimSource";
  private UsernameClaimSourceEnum usernameClaimSource;

  public static final String JSON_PROPERTY_USERNAME_VALIDATE_ENABLED = "usernameValidateEnabled";
  private Boolean usernameValidateEnabled;

  public MsgVpnAuthenticationOauthProvider() {
  }

  public MsgVpnAuthenticationOauthProvider audienceClaimName(String audienceClaimName) {
    
    this.audienceClaimName = audienceClaimName;
    return this;
  }

   /**
   * The audience claim name, indicating which part of the object to use for determining the audience. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;aud\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return audienceClaimName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUDIENCE_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAudienceClaimName() {
    return audienceClaimName;
  }


  @JsonProperty(JSON_PROPERTY_AUDIENCE_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAudienceClaimName(String audienceClaimName) {
    this.audienceClaimName = audienceClaimName;
  }


  public MsgVpnAuthenticationOauthProvider audienceClaimSource(AudienceClaimSourceEnum audienceClaimSource) {
    
    this.audienceClaimSource = audienceClaimSource;
    return this;
  }

   /**
   * The audience claim source, indicating where to search for the audience value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return audienceClaimSource
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUDIENCE_CLAIM_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AudienceClaimSourceEnum getAudienceClaimSource() {
    return audienceClaimSource;
  }


  @JsonProperty(JSON_PROPERTY_AUDIENCE_CLAIM_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAudienceClaimSource(AudienceClaimSourceEnum audienceClaimSource) {
    this.audienceClaimSource = audienceClaimSource;
  }


  public MsgVpnAuthenticationOauthProvider audienceClaimValue(String audienceClaimValue) {
    
    this.audienceClaimValue = audienceClaimValue;
    return this;
  }

   /**
   * The required audience value for a token to be considered valid. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return audienceClaimValue
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUDIENCE_CLAIM_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAudienceClaimValue() {
    return audienceClaimValue;
  }


  @JsonProperty(JSON_PROPERTY_AUDIENCE_CLAIM_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAudienceClaimValue(String audienceClaimValue) {
    this.audienceClaimValue = audienceClaimValue;
  }


  public MsgVpnAuthenticationOauthProvider audienceValidationEnabled(Boolean audienceValidationEnabled) {
    
    this.audienceValidationEnabled = audienceValidationEnabled;
    return this;
  }

   /**
   * Enable or disable audience validation. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return audienceValidationEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUDIENCE_VALIDATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAudienceValidationEnabled() {
    return audienceValidationEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUDIENCE_VALIDATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAudienceValidationEnabled(Boolean audienceValidationEnabled) {
    this.audienceValidationEnabled = audienceValidationEnabled;
  }


  public MsgVpnAuthenticationOauthProvider authorizationGroupClaimName(String authorizationGroupClaimName) {
    
    this.authorizationGroupClaimName = authorizationGroupClaimName;
    return this;
  }

   /**
   * The authorization group claim name, indicating which part of the object to use for determining the authorization group. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;scope\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return authorizationGroupClaimName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthorizationGroupClaimName() {
    return authorizationGroupClaimName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationGroupClaimName(String authorizationGroupClaimName) {
    this.authorizationGroupClaimName = authorizationGroupClaimName;
  }


  public MsgVpnAuthenticationOauthProvider authorizationGroupClaimSource(AuthorizationGroupClaimSourceEnum authorizationGroupClaimSource) {
    
    this.authorizationGroupClaimSource = authorizationGroupClaimSource;
    return this;
  }

   /**
   * The authorization group claim source, indicating where to search for the authorization group name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return authorizationGroupClaimSource
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthorizationGroupClaimSourceEnum getAuthorizationGroupClaimSource() {
    return authorizationGroupClaimSource;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUP_CLAIM_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationGroupClaimSource(AuthorizationGroupClaimSourceEnum authorizationGroupClaimSource) {
    this.authorizationGroupClaimSource = authorizationGroupClaimSource;
  }


  public MsgVpnAuthenticationOauthProvider authorizationGroupEnabled(Boolean authorizationGroupEnabled) {
    
    this.authorizationGroupEnabled = authorizationGroupEnabled;
    return this;
  }

   /**
   * Enable or disable OAuth based authorization. When enabled, the configured authorization type for OAuth clients is overridden. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return authorizationGroupEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthorizationGroupEnabled() {
    return authorizationGroupEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationGroupEnabled(Boolean authorizationGroupEnabled) {
    this.authorizationGroupEnabled = authorizationGroupEnabled;
  }


  public MsgVpnAuthenticationOauthProvider disconnectOnTokenExpirationEnabled(Boolean disconnectOnTokenExpirationEnabled) {
    
    this.disconnectOnTokenExpirationEnabled = disconnectOnTokenExpirationEnabled;
    return this;
  }

   /**
   * Enable or disable the disconnection of clients when their tokens expire. Changing this value does not affect existing clients, only new client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
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


  public MsgVpnAuthenticationOauthProvider enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable OAuth Provider client authentication. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
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


  public MsgVpnAuthenticationOauthProvider jwksRefreshInterval(Integer jwksRefreshInterval) {
    
    this.jwksRefreshInterval = jwksRefreshInterval;
    return this;
  }

   /**
   * The number of seconds between forced JWKS public key refreshing. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;86400&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return jwksRefreshInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_JWKS_REFRESH_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getJwksRefreshInterval() {
    return jwksRefreshInterval;
  }


  @JsonProperty(JSON_PROPERTY_JWKS_REFRESH_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJwksRefreshInterval(Integer jwksRefreshInterval) {
    this.jwksRefreshInterval = jwksRefreshInterval;
  }


  public MsgVpnAuthenticationOauthProvider jwksUri(String jwksUri) {
    
    this.jwksUri = jwksUri;
    return this;
  }

   /**
   * The URI where the OAuth provider publishes its JWKS public keys. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return jwksUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_JWKS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getJwksUri() {
    return jwksUri;
  }


  @JsonProperty(JSON_PROPERTY_JWKS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJwksUri(String jwksUri) {
    this.jwksUri = jwksUri;
  }


  public MsgVpnAuthenticationOauthProvider msgVpnName(String msgVpnName) {
    
    this.msgVpnName = msgVpnName;
    return this;
  }

   /**
   * The name of the Message VPN. Deprecated since 2.25. Replaced by authenticationOauthProfiles.
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


  public MsgVpnAuthenticationOauthProvider oauthProviderName(String oauthProviderName) {
    
    this.oauthProviderName = oauthProviderName;
    return this;
  }

   /**
   * The name of the OAuth Provider. Deprecated since 2.25. Replaced by authenticationOauthProfiles.
   * @return oauthProviderName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OAUTH_PROVIDER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOauthProviderName() {
    return oauthProviderName;
  }


  @JsonProperty(JSON_PROPERTY_OAUTH_PROVIDER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOauthProviderName(String oauthProviderName) {
    this.oauthProviderName = oauthProviderName;
  }


  public MsgVpnAuthenticationOauthProvider tokenIgnoreTimeLimitsEnabled(Boolean tokenIgnoreTimeLimitsEnabled) {
    
    this.tokenIgnoreTimeLimitsEnabled = tokenIgnoreTimeLimitsEnabled;
    return this;
  }

   /**
   * Enable or disable whether to ignore time limits and accept tokens that are not yet valid or are no longer valid. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return tokenIgnoreTimeLimitsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOKEN_IGNORE_TIME_LIMITS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTokenIgnoreTimeLimitsEnabled() {
    return tokenIgnoreTimeLimitsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TOKEN_IGNORE_TIME_LIMITS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTokenIgnoreTimeLimitsEnabled(Boolean tokenIgnoreTimeLimitsEnabled) {
    this.tokenIgnoreTimeLimitsEnabled = tokenIgnoreTimeLimitsEnabled;
  }


  public MsgVpnAuthenticationOauthProvider tokenIntrospectionParameterName(String tokenIntrospectionParameterName) {
    
    this.tokenIntrospectionParameterName = tokenIntrospectionParameterName;
    return this;
  }

   /**
   * The parameter name used to identify the token during access token introspection. A standards compliant OAuth introspection server expects \&quot;token\&quot;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;token\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return tokenIntrospectionParameterName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_PARAMETER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTokenIntrospectionParameterName() {
    return tokenIntrospectionParameterName;
  }


  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_PARAMETER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTokenIntrospectionParameterName(String tokenIntrospectionParameterName) {
    this.tokenIntrospectionParameterName = tokenIntrospectionParameterName;
  }


  public MsgVpnAuthenticationOauthProvider tokenIntrospectionPassword(String tokenIntrospectionPassword) {
    
    this.tokenIntrospectionPassword = tokenIntrospectionPassword;
    return this;
  }

   /**
   * The password to use when logging into the token introspection URI. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return tokenIntrospectionPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTokenIntrospectionPassword() {
    return tokenIntrospectionPassword;
  }


  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTokenIntrospectionPassword(String tokenIntrospectionPassword) {
    this.tokenIntrospectionPassword = tokenIntrospectionPassword;
  }


  public MsgVpnAuthenticationOauthProvider tokenIntrospectionTimeout(Integer tokenIntrospectionTimeout) {
    
    this.tokenIntrospectionTimeout = tokenIntrospectionTimeout;
    return this;
  }

   /**
   * The maximum time in seconds a token introspection is allowed to take. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return tokenIntrospectionTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getTokenIntrospectionTimeout() {
    return tokenIntrospectionTimeout;
  }


  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTokenIntrospectionTimeout(Integer tokenIntrospectionTimeout) {
    this.tokenIntrospectionTimeout = tokenIntrospectionTimeout;
  }


  public MsgVpnAuthenticationOauthProvider tokenIntrospectionUri(String tokenIntrospectionUri) {
    
    this.tokenIntrospectionUri = tokenIntrospectionUri;
    return this;
  }

   /**
   * The token introspection URI of the OAuth authentication server. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return tokenIntrospectionUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTokenIntrospectionUri() {
    return tokenIntrospectionUri;
  }


  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTokenIntrospectionUri(String tokenIntrospectionUri) {
    this.tokenIntrospectionUri = tokenIntrospectionUri;
  }


  public MsgVpnAuthenticationOauthProvider tokenIntrospectionUsername(String tokenIntrospectionUsername) {
    
    this.tokenIntrospectionUsername = tokenIntrospectionUsername;
    return this;
  }

   /**
   * The username to use when logging into the token introspection URI. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return tokenIntrospectionUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTokenIntrospectionUsername() {
    return tokenIntrospectionUsername;
  }


  @JsonProperty(JSON_PROPERTY_TOKEN_INTROSPECTION_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTokenIntrospectionUsername(String tokenIntrospectionUsername) {
    this.tokenIntrospectionUsername = tokenIntrospectionUsername;
  }


  public MsgVpnAuthenticationOauthProvider usernameClaimName(String usernameClaimName) {
    
    this.usernameClaimName = usernameClaimName;
    return this;
  }

   /**
   * The username claim name, indicating which part of the object to use for determining the username. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;sub\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
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


  public MsgVpnAuthenticationOauthProvider usernameClaimSource(UsernameClaimSourceEnum usernameClaimSource) {
    
    this.usernameClaimSource = usernameClaimSource;
    return this;
  }

   /**
   * The username claim source, indicating where to search for the username value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return usernameClaimSource
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_USERNAME_CLAIM_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UsernameClaimSourceEnum getUsernameClaimSource() {
    return usernameClaimSource;
  }


  @JsonProperty(JSON_PROPERTY_USERNAME_CLAIM_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUsernameClaimSource(UsernameClaimSourceEnum usernameClaimSource) {
    this.usernameClaimSource = usernameClaimSource;
  }


  public MsgVpnAuthenticationOauthProvider usernameValidateEnabled(Boolean usernameValidateEnabled) {
    
    this.usernameValidateEnabled = usernameValidateEnabled;
    return this;
  }

   /**
   * Enable or disable whether the API provided username will be validated against the username calculated from the token(s); the connection attempt is rejected if they differ. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles.
   * @return usernameValidateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_USERNAME_VALIDATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getUsernameValidateEnabled() {
    return usernameValidateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_USERNAME_VALIDATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUsernameValidateEnabled(Boolean usernameValidateEnabled) {
    this.usernameValidateEnabled = usernameValidateEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnAuthenticationOauthProvider msgVpnAuthenticationOauthProvider = (MsgVpnAuthenticationOauthProvider) o;
    return Objects.equals(this.audienceClaimName, msgVpnAuthenticationOauthProvider.audienceClaimName) &&
        Objects.equals(this.audienceClaimSource, msgVpnAuthenticationOauthProvider.audienceClaimSource) &&
        Objects.equals(this.audienceClaimValue, msgVpnAuthenticationOauthProvider.audienceClaimValue) &&
        Objects.equals(this.audienceValidationEnabled, msgVpnAuthenticationOauthProvider.audienceValidationEnabled) &&
        Objects.equals(this.authorizationGroupClaimName, msgVpnAuthenticationOauthProvider.authorizationGroupClaimName) &&
        Objects.equals(this.authorizationGroupClaimSource, msgVpnAuthenticationOauthProvider.authorizationGroupClaimSource) &&
        Objects.equals(this.authorizationGroupEnabled, msgVpnAuthenticationOauthProvider.authorizationGroupEnabled) &&
        Objects.equals(this.disconnectOnTokenExpirationEnabled, msgVpnAuthenticationOauthProvider.disconnectOnTokenExpirationEnabled) &&
        Objects.equals(this.enabled, msgVpnAuthenticationOauthProvider.enabled) &&
        Objects.equals(this.jwksRefreshInterval, msgVpnAuthenticationOauthProvider.jwksRefreshInterval) &&
        Objects.equals(this.jwksUri, msgVpnAuthenticationOauthProvider.jwksUri) &&
        Objects.equals(this.msgVpnName, msgVpnAuthenticationOauthProvider.msgVpnName) &&
        Objects.equals(this.oauthProviderName, msgVpnAuthenticationOauthProvider.oauthProviderName) &&
        Objects.equals(this.tokenIgnoreTimeLimitsEnabled, msgVpnAuthenticationOauthProvider.tokenIgnoreTimeLimitsEnabled) &&
        Objects.equals(this.tokenIntrospectionParameterName, msgVpnAuthenticationOauthProvider.tokenIntrospectionParameterName) &&
        Objects.equals(this.tokenIntrospectionPassword, msgVpnAuthenticationOauthProvider.tokenIntrospectionPassword) &&
        Objects.equals(this.tokenIntrospectionTimeout, msgVpnAuthenticationOauthProvider.tokenIntrospectionTimeout) &&
        Objects.equals(this.tokenIntrospectionUri, msgVpnAuthenticationOauthProvider.tokenIntrospectionUri) &&
        Objects.equals(this.tokenIntrospectionUsername, msgVpnAuthenticationOauthProvider.tokenIntrospectionUsername) &&
        Objects.equals(this.usernameClaimName, msgVpnAuthenticationOauthProvider.usernameClaimName) &&
        Objects.equals(this.usernameClaimSource, msgVpnAuthenticationOauthProvider.usernameClaimSource) &&
        Objects.equals(this.usernameValidateEnabled, msgVpnAuthenticationOauthProvider.usernameValidateEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(audienceClaimName, audienceClaimSource, audienceClaimValue, audienceValidationEnabled, authorizationGroupClaimName, authorizationGroupClaimSource, authorizationGroupEnabled, disconnectOnTokenExpirationEnabled, enabled, jwksRefreshInterval, jwksUri, msgVpnName, oauthProviderName, tokenIgnoreTimeLimitsEnabled, tokenIntrospectionParameterName, tokenIntrospectionPassword, tokenIntrospectionTimeout, tokenIntrospectionUri, tokenIntrospectionUsername, usernameClaimName, usernameClaimSource, usernameValidateEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnAuthenticationOauthProvider {\n");
    sb.append("    audienceClaimName: ").append(toIndentedString(audienceClaimName)).append("\n");
    sb.append("    audienceClaimSource: ").append(toIndentedString(audienceClaimSource)).append("\n");
    sb.append("    audienceClaimValue: ").append(toIndentedString(audienceClaimValue)).append("\n");
    sb.append("    audienceValidationEnabled: ").append(toIndentedString(audienceValidationEnabled)).append("\n");
    sb.append("    authorizationGroupClaimName: ").append(toIndentedString(authorizationGroupClaimName)).append("\n");
    sb.append("    authorizationGroupClaimSource: ").append(toIndentedString(authorizationGroupClaimSource)).append("\n");
    sb.append("    authorizationGroupEnabled: ").append(toIndentedString(authorizationGroupEnabled)).append("\n");
    sb.append("    disconnectOnTokenExpirationEnabled: ").append(toIndentedString(disconnectOnTokenExpirationEnabled)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    jwksRefreshInterval: ").append(toIndentedString(jwksRefreshInterval)).append("\n");
    sb.append("    jwksUri: ").append(toIndentedString(jwksUri)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    oauthProviderName: ").append(toIndentedString(oauthProviderName)).append("\n");
    sb.append("    tokenIgnoreTimeLimitsEnabled: ").append(toIndentedString(tokenIgnoreTimeLimitsEnabled)).append("\n");
    sb.append("    tokenIntrospectionParameterName: ").append(toIndentedString(tokenIntrospectionParameterName)).append("\n");
    sb.append("    tokenIntrospectionPassword: ").append(toIndentedString(tokenIntrospectionPassword)).append("\n");
    sb.append("    tokenIntrospectionTimeout: ").append(toIndentedString(tokenIntrospectionTimeout)).append("\n");
    sb.append("    tokenIntrospectionUri: ").append(toIndentedString(tokenIntrospectionUri)).append("\n");
    sb.append("    tokenIntrospectionUsername: ").append(toIndentedString(tokenIntrospectionUsername)).append("\n");
    sb.append("    usernameClaimName: ").append(toIndentedString(usernameClaimName)).append("\n");
    sb.append("    usernameClaimSource: ").append(toIndentedString(usernameClaimSource)).append("\n");
    sb.append("    usernameValidateEnabled: ").append(toIndentedString(usernameValidateEnabled)).append("\n");
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

