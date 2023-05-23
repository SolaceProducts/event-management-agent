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
 * MsgVpnRestDeliveryPointRestConsumer
 */
@JsonPropertyOrder({
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_AWS_ACCESS_KEY_ID,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_AWS_REGION,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_AWS_SECRET_ACCESS_KEY,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_AWS_SERVICE,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CONTENT,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_PASSWORD,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_PASSWORD,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_USERNAME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_NAME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_VALUE,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_ID,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SCOPE,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SECRET,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_TOKEN_ENDPOINT,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_SECRET_KEY,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_TOKEN_ENDPOINT,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_AUTHENTICATION_SCHEME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_ENABLED,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_HTTP_METHOD,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_LOCAL_INTERFACE,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_MAX_POST_WAIT_TIME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_OUTGOING_CONNECTION_COUNT,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_REMOTE_HOST,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_REMOTE_PORT,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_REST_CONSUMER_NAME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_REST_DELIVERY_POINT_NAME,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_RETRY_DELAY,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_TLS_CIPHER_SUITE_LIST,
  MsgVpnRestDeliveryPointRestConsumer.JSON_PROPERTY_TLS_ENABLED
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-17T23:49:01.929728+01:00[Europe/London]")
public class MsgVpnRestDeliveryPointRestConsumer {
  public static final String JSON_PROPERTY_AUTHENTICATION_AWS_ACCESS_KEY_ID = "authenticationAwsAccessKeyId";
  private String authenticationAwsAccessKeyId;

  public static final String JSON_PROPERTY_AUTHENTICATION_AWS_REGION = "authenticationAwsRegion";
  private String authenticationAwsRegion;

  public static final String JSON_PROPERTY_AUTHENTICATION_AWS_SECRET_ACCESS_KEY = "authenticationAwsSecretAccessKey";
  private String authenticationAwsSecretAccessKey;

  public static final String JSON_PROPERTY_AUTHENTICATION_AWS_SERVICE = "authenticationAwsService";
  private String authenticationAwsService;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CONTENT = "authenticationClientCertContent";
  private String authenticationClientCertContent;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_PASSWORD = "authenticationClientCertPassword";
  private String authenticationClientCertPassword;

  public static final String JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_PASSWORD = "authenticationHttpBasicPassword";
  private String authenticationHttpBasicPassword;

  public static final String JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_USERNAME = "authenticationHttpBasicUsername";
  private String authenticationHttpBasicUsername;

  public static final String JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_NAME = "authenticationHttpHeaderName";
  private String authenticationHttpHeaderName;

  public static final String JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_VALUE = "authenticationHttpHeaderValue";
  private String authenticationHttpHeaderValue;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_ID = "authenticationOauthClientId";
  private String authenticationOauthClientId;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SCOPE = "authenticationOauthClientScope";
  private String authenticationOauthClientScope;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SECRET = "authenticationOauthClientSecret";
  private String authenticationOauthClientSecret;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_TOKEN_ENDPOINT = "authenticationOauthClientTokenEndpoint";
  private String authenticationOauthClientTokenEndpoint;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_SECRET_KEY = "authenticationOauthJwtSecretKey";
  private String authenticationOauthJwtSecretKey;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_TOKEN_ENDPOINT = "authenticationOauthJwtTokenEndpoint";
  private String authenticationOauthJwtTokenEndpoint;

  /**
   * The authentication scheme used by the REST Consumer to login to the REST host. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Login with no authentication. This may be useful for anonymous connections or when a REST Consumer does not require authentication. \&quot;http-basic\&quot; - Login with a username and optional password according to HTTP Basic authentication as per RFC2616. \&quot;client-certificate\&quot; - Login with a client TLS certificate as per RFC5246. Client certificate authentication is only available on TLS connections. \&quot;http-header\&quot; - Login with a specified HTTP header. \&quot;oauth-client\&quot; - Login with OAuth 2.0 client credentials. \&quot;oauth-jwt\&quot; - Login with OAuth (RFC 7523 JWT Profile). \&quot;transparent\&quot; - Login using the Authorization header from the message properties, if present. Transparent authentication passes along existing Authorization header metadata instead of discarding it. Note that if the message is coming from a REST producer, the REST service must be configured to forward the Authorization header. \&quot;aws\&quot; - Login using AWS Signature Version 4 authentication (AWS4-HMAC-SHA256). &lt;/pre&gt; 
   */
  public enum AuthenticationSchemeEnum {
    NONE("none"),
    
    HTTP_BASIC("http-basic"),
    
    CLIENT_CERTIFICATE("client-certificate"),
    
    HTTP_HEADER("http-header"),
    
    OAUTH_CLIENT("oauth-client"),
    
    OAUTH_JWT("oauth-jwt"),
    
    TRANSPARENT("transparent"),
    
    AWS("aws");

    private String value;

    AuthenticationSchemeEnum(String value) {
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
    public static AuthenticationSchemeEnum fromValue(String value) {
      for (AuthenticationSchemeEnum b : AuthenticationSchemeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTHENTICATION_SCHEME = "authenticationScheme";
  private AuthenticationSchemeEnum authenticationScheme;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  /**
   * The HTTP method to use (POST or PUT). This is used only when operating in the REST service \&quot;messaging\&quot; mode and is ignored in \&quot;gateway\&quot; mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;post\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;post\&quot; - Use the POST HTTP method. \&quot;put\&quot; - Use the PUT HTTP method. &lt;/pre&gt;  Available since 2.17.
   */
  public enum HttpMethodEnum {
    POST("post"),
    
    PUT("put");

    private String value;

    HttpMethodEnum(String value) {
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
    public static HttpMethodEnum fromValue(String value) {
      for (HttpMethodEnum b : HttpMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_HTTP_METHOD = "httpMethod";
  private HttpMethodEnum httpMethod;

  public static final String JSON_PROPERTY_LOCAL_INTERFACE = "localInterface";
  private String localInterface;

  public static final String JSON_PROPERTY_MAX_POST_WAIT_TIME = "maxPostWaitTime";
  private Integer maxPostWaitTime;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_OUTGOING_CONNECTION_COUNT = "outgoingConnectionCount";
  private Integer outgoingConnectionCount;

  public static final String JSON_PROPERTY_REMOTE_HOST = "remoteHost";
  private String remoteHost;

  public static final String JSON_PROPERTY_REMOTE_PORT = "remotePort";
  private Long remotePort;

  public static final String JSON_PROPERTY_REST_CONSUMER_NAME = "restConsumerName";
  private String restConsumerName;

  public static final String JSON_PROPERTY_REST_DELIVERY_POINT_NAME = "restDeliveryPointName";
  private String restDeliveryPointName;

  public static final String JSON_PROPERTY_RETRY_DELAY = "retryDelay";
  private Integer retryDelay;

  public static final String JSON_PROPERTY_TLS_CIPHER_SUITE_LIST = "tlsCipherSuiteList";
  private String tlsCipherSuiteList;

  public static final String JSON_PROPERTY_TLS_ENABLED = "tlsEnabled";
  private Boolean tlsEnabled;

  public MsgVpnRestDeliveryPointRestConsumer() {
  }

  public MsgVpnRestDeliveryPointRestConsumer authenticationAwsAccessKeyId(String authenticationAwsAccessKeyId) {
    
    this.authenticationAwsAccessKeyId = authenticationAwsAccessKeyId;
    return this;
  }

   /**
   * The AWS access key id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26.
   * @return authenticationAwsAccessKeyId
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_ACCESS_KEY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationAwsAccessKeyId() {
    return authenticationAwsAccessKeyId;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_ACCESS_KEY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationAwsAccessKeyId(String authenticationAwsAccessKeyId) {
    this.authenticationAwsAccessKeyId = authenticationAwsAccessKeyId;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationAwsRegion(String authenticationAwsRegion) {
    
    this.authenticationAwsRegion = authenticationAwsRegion;
    return this;
  }

   /**
   * The AWS region id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26.
   * @return authenticationAwsRegion
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_REGION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationAwsRegion() {
    return authenticationAwsRegion;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_REGION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationAwsRegion(String authenticationAwsRegion) {
    this.authenticationAwsRegion = authenticationAwsRegion;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationAwsSecretAccessKey(String authenticationAwsSecretAccessKey) {
    
    this.authenticationAwsSecretAccessKey = authenticationAwsSecretAccessKey;
    return this;
  }

   /**
   * The AWS secret access key. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26.
   * @return authenticationAwsSecretAccessKey
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_SECRET_ACCESS_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationAwsSecretAccessKey() {
    return authenticationAwsSecretAccessKey;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_SECRET_ACCESS_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationAwsSecretAccessKey(String authenticationAwsSecretAccessKey) {
    this.authenticationAwsSecretAccessKey = authenticationAwsSecretAccessKey;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationAwsService(String authenticationAwsService) {
    
    this.authenticationAwsService = authenticationAwsService;
    return this;
  }

   /**
   * The AWS service id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26.
   * @return authenticationAwsService
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_SERVICE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationAwsService() {
    return authenticationAwsService;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_AWS_SERVICE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationAwsService(String authenticationAwsService) {
    this.authenticationAwsService = authenticationAwsService;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationClientCertContent(String authenticationClientCertContent) {
    
    this.authenticationClientCertContent = authenticationClientCertContent;
    return this;
  }

   /**
   * The PEM formatted content for the client certificate that the REST Consumer will present to the REST host. It must consist of a private key and between one and three certificates comprising the certificate trust chain. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9.
   * @return authenticationClientCertContent
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationClientCertContent() {
    return authenticationClientCertContent;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertContent(String authenticationClientCertContent) {
    this.authenticationClientCertContent = authenticationClientCertContent;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationClientCertPassword(String authenticationClientCertPassword) {
    
    this.authenticationClientCertPassword = authenticationClientCertPassword;
    return this;
  }

   /**
   * The password for the client certificate. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9.
   * @return authenticationClientCertPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationClientCertPassword() {
    return authenticationClientCertPassword;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertPassword(String authenticationClientCertPassword) {
    this.authenticationClientCertPassword = authenticationClientCertPassword;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationHttpBasicPassword(String authenticationHttpBasicPassword) {
    
    this.authenticationHttpBasicPassword = authenticationHttpBasicPassword;
    return this;
  }

   /**
   * The password for the username. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return authenticationHttpBasicPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationHttpBasicPassword() {
    return authenticationHttpBasicPassword;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationHttpBasicPassword(String authenticationHttpBasicPassword) {
    this.authenticationHttpBasicPassword = authenticationHttpBasicPassword;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationHttpBasicUsername(String authenticationHttpBasicUsername) {
    
    this.authenticationHttpBasicUsername = authenticationHttpBasicUsername;
    return this;
  }

   /**
   * The username that the REST Consumer will use to login to the REST host. Normally a username is only configured when basic authentication is selected for the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return authenticationHttpBasicUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationHttpBasicUsername() {
    return authenticationHttpBasicUsername;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_BASIC_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationHttpBasicUsername(String authenticationHttpBasicUsername) {
    this.authenticationHttpBasicUsername = authenticationHttpBasicUsername;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationHttpHeaderName(String authenticationHttpHeaderName) {
    
    this.authenticationHttpHeaderName = authenticationHttpHeaderName;
    return this;
  }

   /**
   * The authentication header name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.15.
   * @return authenticationHttpHeaderName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationHttpHeaderName() {
    return authenticationHttpHeaderName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationHttpHeaderName(String authenticationHttpHeaderName) {
    this.authenticationHttpHeaderName = authenticationHttpHeaderName;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationHttpHeaderValue(String authenticationHttpHeaderValue) {
    
    this.authenticationHttpHeaderValue = authenticationHttpHeaderValue;
    return this;
  }

   /**
   * The authentication header value. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.15.
   * @return authenticationHttpHeaderValue
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationHttpHeaderValue() {
    return authenticationHttpHeaderValue;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_HTTP_HEADER_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationHttpHeaderValue(String authenticationHttpHeaderValue) {
    this.authenticationHttpHeaderValue = authenticationHttpHeaderValue;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationOauthClientId(String authenticationOauthClientId) {
    
    this.authenticationOauthClientId = authenticationOauthClientId;
    return this;
  }

   /**
   * The OAuth client ID. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19.
   * @return authenticationOauthClientId
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthClientId() {
    return authenticationOauthClientId;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthClientId(String authenticationOauthClientId) {
    this.authenticationOauthClientId = authenticationOauthClientId;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationOauthClientScope(String authenticationOauthClientScope) {
    
    this.authenticationOauthClientScope = authenticationOauthClientScope;
    return this;
  }

   /**
   * The OAuth scope. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19.
   * @return authenticationOauthClientScope
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SCOPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthClientScope() {
    return authenticationOauthClientScope;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SCOPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthClientScope(String authenticationOauthClientScope) {
    this.authenticationOauthClientScope = authenticationOauthClientScope;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationOauthClientSecret(String authenticationOauthClientSecret) {
    
    this.authenticationOauthClientSecret = authenticationOauthClientSecret;
    return this;
  }

   /**
   * The OAuth client secret. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19.
   * @return authenticationOauthClientSecret
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SECRET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthClientSecret() {
    return authenticationOauthClientSecret;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_SECRET)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthClientSecret(String authenticationOauthClientSecret) {
    this.authenticationOauthClientSecret = authenticationOauthClientSecret;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationOauthClientTokenEndpoint(String authenticationOauthClientTokenEndpoint) {
    
    this.authenticationOauthClientTokenEndpoint = authenticationOauthClientTokenEndpoint;
    return this;
  }

   /**
   * The OAuth token endpoint URL that the REST Consumer will use to request a token for login to the REST host. Must begin with \&quot;https\&quot;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19.
   * @return authenticationOauthClientTokenEndpoint
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_TOKEN_ENDPOINT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthClientTokenEndpoint() {
    return authenticationOauthClientTokenEndpoint;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_CLIENT_TOKEN_ENDPOINT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthClientTokenEndpoint(String authenticationOauthClientTokenEndpoint) {
    this.authenticationOauthClientTokenEndpoint = authenticationOauthClientTokenEndpoint;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationOauthJwtSecretKey(String authenticationOauthJwtSecretKey) {
    
    this.authenticationOauthJwtSecretKey = authenticationOauthJwtSecretKey;
    return this;
  }

   /**
   * The OAuth secret key used to sign the token request JWT. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.21.
   * @return authenticationOauthJwtSecretKey
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_SECRET_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthJwtSecretKey() {
    return authenticationOauthJwtSecretKey;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_SECRET_KEY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthJwtSecretKey(String authenticationOauthJwtSecretKey) {
    this.authenticationOauthJwtSecretKey = authenticationOauthJwtSecretKey;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationOauthJwtTokenEndpoint(String authenticationOauthJwtTokenEndpoint) {
    
    this.authenticationOauthJwtTokenEndpoint = authenticationOauthJwtTokenEndpoint;
    return this;
  }

   /**
   * The OAuth token endpoint URL that the REST Consumer will use to request a token for login to the REST host. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.21.
   * @return authenticationOauthJwtTokenEndpoint
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_TOKEN_ENDPOINT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthJwtTokenEndpoint() {
    return authenticationOauthJwtTokenEndpoint;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_JWT_TOKEN_ENDPOINT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthJwtTokenEndpoint(String authenticationOauthJwtTokenEndpoint) {
    this.authenticationOauthJwtTokenEndpoint = authenticationOauthJwtTokenEndpoint;
  }


  public MsgVpnRestDeliveryPointRestConsumer authenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    
    this.authenticationScheme = authenticationScheme;
    return this;
  }

   /**
   * The authentication scheme used by the REST Consumer to login to the REST host. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Login with no authentication. This may be useful for anonymous connections or when a REST Consumer does not require authentication. \&quot;http-basic\&quot; - Login with a username and optional password according to HTTP Basic authentication as per RFC2616. \&quot;client-certificate\&quot; - Login with a client TLS certificate as per RFC5246. Client certificate authentication is only available on TLS connections. \&quot;http-header\&quot; - Login with a specified HTTP header. \&quot;oauth-client\&quot; - Login with OAuth 2.0 client credentials. \&quot;oauth-jwt\&quot; - Login with OAuth (RFC 7523 JWT Profile). \&quot;transparent\&quot; - Login using the Authorization header from the message properties, if present. Transparent authentication passes along existing Authorization header metadata instead of discarding it. Note that if the message is coming from a REST producer, the REST service must be configured to forward the Authorization header. \&quot;aws\&quot; - Login using AWS Signature Version 4 authentication (AWS4-HMAC-SHA256). &lt;/pre&gt; 
   * @return authenticationScheme
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_SCHEME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthenticationSchemeEnum getAuthenticationScheme() {
    return authenticationScheme;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_SCHEME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
  }


  public MsgVpnRestDeliveryPointRestConsumer enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
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


  public MsgVpnRestDeliveryPointRestConsumer httpMethod(HttpMethodEnum httpMethod) {
    
    this.httpMethod = httpMethod;
    return this;
  }

   /**
   * The HTTP method to use (POST or PUT). This is used only when operating in the REST service \&quot;messaging\&quot; mode and is ignored in \&quot;gateway\&quot; mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;post\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;post\&quot; - Use the POST HTTP method. \&quot;put\&quot; - Use the PUT HTTP method. &lt;/pre&gt;  Available since 2.17.
   * @return httpMethod
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_HTTP_METHOD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public HttpMethodEnum getHttpMethod() {
    return httpMethod;
  }


  @JsonProperty(JSON_PROPERTY_HTTP_METHOD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setHttpMethod(HttpMethodEnum httpMethod) {
    this.httpMethod = httpMethod;
  }


  public MsgVpnRestDeliveryPointRestConsumer localInterface(String localInterface) {
    
    this.localInterface = localInterface;
    return this;
  }

   /**
   * The interface that will be used for all outgoing connections associated with the REST Consumer. When unspecified, an interface is automatically chosen. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return localInterface
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_LOCAL_INTERFACE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getLocalInterface() {
    return localInterface;
  }


  @JsonProperty(JSON_PROPERTY_LOCAL_INTERFACE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setLocalInterface(String localInterface) {
    this.localInterface = localInterface;
  }


  public MsgVpnRestDeliveryPointRestConsumer maxPostWaitTime(Integer maxPostWaitTime) {
    
    this.maxPostWaitTime = maxPostWaitTime;
    return this;
  }

   /**
   * The maximum amount of time (in seconds) to wait for an HTTP POST response from the REST Consumer. Once this time is exceeded, the TCP connection is reset. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;30&#x60;.
   * @return maxPostWaitTime
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_POST_WAIT_TIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getMaxPostWaitTime() {
    return maxPostWaitTime;
  }


  @JsonProperty(JSON_PROPERTY_MAX_POST_WAIT_TIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxPostWaitTime(Integer maxPostWaitTime) {
    this.maxPostWaitTime = maxPostWaitTime;
  }


  public MsgVpnRestDeliveryPointRestConsumer msgVpnName(String msgVpnName) {
    
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


  public MsgVpnRestDeliveryPointRestConsumer outgoingConnectionCount(Integer outgoingConnectionCount) {
    
    this.outgoingConnectionCount = outgoingConnectionCount;
    return this;
  }

   /**
   * The number of concurrent TCP connections open to the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;.
   * @return outgoingConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OUTGOING_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getOutgoingConnectionCount() {
    return outgoingConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_OUTGOING_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOutgoingConnectionCount(Integer outgoingConnectionCount) {
    this.outgoingConnectionCount = outgoingConnectionCount;
  }


  public MsgVpnRestDeliveryPointRestConsumer remoteHost(String remoteHost) {
    
    this.remoteHost = remoteHost;
    return this;
  }

   /**
   * The IP address or DNS name to which the broker is to connect to deliver messages for the REST Consumer. A host value must be configured for the REST Consumer to be operationally up. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return remoteHost
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REMOTE_HOST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRemoteHost() {
    return remoteHost;
  }


  @JsonProperty(JSON_PROPERTY_REMOTE_HOST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }


  public MsgVpnRestDeliveryPointRestConsumer remotePort(Long remotePort) {
    
    this.remotePort = remotePort;
    return this;
  }

   /**
   * The port associated with the host of the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;8080&#x60;.
   * @return remotePort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REMOTE_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getRemotePort() {
    return remotePort;
  }


  @JsonProperty(JSON_PROPERTY_REMOTE_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRemotePort(Long remotePort) {
    this.remotePort = remotePort;
  }


  public MsgVpnRestDeliveryPointRestConsumer restConsumerName(String restConsumerName) {
    
    this.restConsumerName = restConsumerName;
    return this;
  }

   /**
   * The name of the REST Consumer.
   * @return restConsumerName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_CONSUMER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRestConsumerName() {
    return restConsumerName;
  }


  @JsonProperty(JSON_PROPERTY_REST_CONSUMER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestConsumerName(String restConsumerName) {
    this.restConsumerName = restConsumerName;
  }


  public MsgVpnRestDeliveryPointRestConsumer restDeliveryPointName(String restDeliveryPointName) {
    
    this.restDeliveryPointName = restDeliveryPointName;
    return this;
  }

   /**
   * The name of the REST Delivery Point.
   * @return restDeliveryPointName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_DELIVERY_POINT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRestDeliveryPointName() {
    return restDeliveryPointName;
  }


  @JsonProperty(JSON_PROPERTY_REST_DELIVERY_POINT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestDeliveryPointName(String restDeliveryPointName) {
    this.restDeliveryPointName = restDeliveryPointName;
  }


  public MsgVpnRestDeliveryPointRestConsumer retryDelay(Integer retryDelay) {
    
    this.retryDelay = retryDelay;
    return this;
  }

   /**
   * The number of seconds that must pass before retrying the remote REST Consumer connection. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;.
   * @return retryDelay
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RETRY_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getRetryDelay() {
    return retryDelay;
  }


  @JsonProperty(JSON_PROPERTY_RETRY_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRetryDelay(Integer retryDelay) {
    this.retryDelay = retryDelay;
  }


  public MsgVpnRestDeliveryPointRestConsumer tlsCipherSuiteList(String tlsCipherSuiteList) {
    
    this.tlsCipherSuiteList = tlsCipherSuiteList;
    return this;
  }

   /**
   * The colon-separated list of cipher suites the REST Consumer uses in its encrypted connection. The value &#x60;\&quot;default\&quot;&#x60; implies all supported suites ordered from most secure to least secure. The list of default cipher suites is available in the &#x60;tlsCipherSuiteMsgBackboneDefaultList&#x60; attribute of the Broker object in the Monitoring API. The REST Consumer should choose the first suite from this list that it supports. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;.
   * @return tlsCipherSuiteList
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTlsCipherSuiteList() {
    return tlsCipherSuiteList;
  }


  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsCipherSuiteList(String tlsCipherSuiteList) {
    this.tlsCipherSuiteList = tlsCipherSuiteList;
  }


  public MsgVpnRestDeliveryPointRestConsumer tlsEnabled(Boolean tlsEnabled) {
    
    this.tlsEnabled = tlsEnabled;
    return this;
  }

   /**
   * Enable or disable encryption (TLS) for the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return tlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsEnabled() {
    return tlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsEnabled(Boolean tlsEnabled) {
    this.tlsEnabled = tlsEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnRestDeliveryPointRestConsumer msgVpnRestDeliveryPointRestConsumer = (MsgVpnRestDeliveryPointRestConsumer) o;
    return Objects.equals(this.authenticationAwsAccessKeyId, msgVpnRestDeliveryPointRestConsumer.authenticationAwsAccessKeyId) &&
        Objects.equals(this.authenticationAwsRegion, msgVpnRestDeliveryPointRestConsumer.authenticationAwsRegion) &&
        Objects.equals(this.authenticationAwsSecretAccessKey, msgVpnRestDeliveryPointRestConsumer.authenticationAwsSecretAccessKey) &&
        Objects.equals(this.authenticationAwsService, msgVpnRestDeliveryPointRestConsumer.authenticationAwsService) &&
        Objects.equals(this.authenticationClientCertContent, msgVpnRestDeliveryPointRestConsumer.authenticationClientCertContent) &&
        Objects.equals(this.authenticationClientCertPassword, msgVpnRestDeliveryPointRestConsumer.authenticationClientCertPassword) &&
        Objects.equals(this.authenticationHttpBasicPassword, msgVpnRestDeliveryPointRestConsumer.authenticationHttpBasicPassword) &&
        Objects.equals(this.authenticationHttpBasicUsername, msgVpnRestDeliveryPointRestConsumer.authenticationHttpBasicUsername) &&
        Objects.equals(this.authenticationHttpHeaderName, msgVpnRestDeliveryPointRestConsumer.authenticationHttpHeaderName) &&
        Objects.equals(this.authenticationHttpHeaderValue, msgVpnRestDeliveryPointRestConsumer.authenticationHttpHeaderValue) &&
        Objects.equals(this.authenticationOauthClientId, msgVpnRestDeliveryPointRestConsumer.authenticationOauthClientId) &&
        Objects.equals(this.authenticationOauthClientScope, msgVpnRestDeliveryPointRestConsumer.authenticationOauthClientScope) &&
        Objects.equals(this.authenticationOauthClientSecret, msgVpnRestDeliveryPointRestConsumer.authenticationOauthClientSecret) &&
        Objects.equals(this.authenticationOauthClientTokenEndpoint, msgVpnRestDeliveryPointRestConsumer.authenticationOauthClientTokenEndpoint) &&
        Objects.equals(this.authenticationOauthJwtSecretKey, msgVpnRestDeliveryPointRestConsumer.authenticationOauthJwtSecretKey) &&
        Objects.equals(this.authenticationOauthJwtTokenEndpoint, msgVpnRestDeliveryPointRestConsumer.authenticationOauthJwtTokenEndpoint) &&
        Objects.equals(this.authenticationScheme, msgVpnRestDeliveryPointRestConsumer.authenticationScheme) &&
        Objects.equals(this.enabled, msgVpnRestDeliveryPointRestConsumer.enabled) &&
        Objects.equals(this.httpMethod, msgVpnRestDeliveryPointRestConsumer.httpMethod) &&
        Objects.equals(this.localInterface, msgVpnRestDeliveryPointRestConsumer.localInterface) &&
        Objects.equals(this.maxPostWaitTime, msgVpnRestDeliveryPointRestConsumer.maxPostWaitTime) &&
        Objects.equals(this.msgVpnName, msgVpnRestDeliveryPointRestConsumer.msgVpnName) &&
        Objects.equals(this.outgoingConnectionCount, msgVpnRestDeliveryPointRestConsumer.outgoingConnectionCount) &&
        Objects.equals(this.remoteHost, msgVpnRestDeliveryPointRestConsumer.remoteHost) &&
        Objects.equals(this.remotePort, msgVpnRestDeliveryPointRestConsumer.remotePort) &&
        Objects.equals(this.restConsumerName, msgVpnRestDeliveryPointRestConsumer.restConsumerName) &&
        Objects.equals(this.restDeliveryPointName, msgVpnRestDeliveryPointRestConsumer.restDeliveryPointName) &&
        Objects.equals(this.retryDelay, msgVpnRestDeliveryPointRestConsumer.retryDelay) &&
        Objects.equals(this.tlsCipherSuiteList, msgVpnRestDeliveryPointRestConsumer.tlsCipherSuiteList) &&
        Objects.equals(this.tlsEnabled, msgVpnRestDeliveryPointRestConsumer.tlsEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationAwsAccessKeyId, authenticationAwsRegion, authenticationAwsSecretAccessKey, authenticationAwsService, authenticationClientCertContent, authenticationClientCertPassword, authenticationHttpBasicPassword, authenticationHttpBasicUsername, authenticationHttpHeaderName, authenticationHttpHeaderValue, authenticationOauthClientId, authenticationOauthClientScope, authenticationOauthClientSecret, authenticationOauthClientTokenEndpoint, authenticationOauthJwtSecretKey, authenticationOauthJwtTokenEndpoint, authenticationScheme, enabled, httpMethod, localInterface, maxPostWaitTime, msgVpnName, outgoingConnectionCount, remoteHost, remotePort, restConsumerName, restDeliveryPointName, retryDelay, tlsCipherSuiteList, tlsEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnRestDeliveryPointRestConsumer {\n");
    sb.append("    authenticationAwsAccessKeyId: ").append(toIndentedString(authenticationAwsAccessKeyId)).append("\n");
    sb.append("    authenticationAwsRegion: ").append(toIndentedString(authenticationAwsRegion)).append("\n");
    sb.append("    authenticationAwsSecretAccessKey: ").append(toIndentedString(authenticationAwsSecretAccessKey)).append("\n");
    sb.append("    authenticationAwsService: ").append(toIndentedString(authenticationAwsService)).append("\n");
    sb.append("    authenticationClientCertContent: ").append(toIndentedString(authenticationClientCertContent)).append("\n");
    sb.append("    authenticationClientCertPassword: ").append(toIndentedString(authenticationClientCertPassword)).append("\n");
    sb.append("    authenticationHttpBasicPassword: ").append(toIndentedString(authenticationHttpBasicPassword)).append("\n");
    sb.append("    authenticationHttpBasicUsername: ").append(toIndentedString(authenticationHttpBasicUsername)).append("\n");
    sb.append("    authenticationHttpHeaderName: ").append(toIndentedString(authenticationHttpHeaderName)).append("\n");
    sb.append("    authenticationHttpHeaderValue: ").append(toIndentedString(authenticationHttpHeaderValue)).append("\n");
    sb.append("    authenticationOauthClientId: ").append(toIndentedString(authenticationOauthClientId)).append("\n");
    sb.append("    authenticationOauthClientScope: ").append(toIndentedString(authenticationOauthClientScope)).append("\n");
    sb.append("    authenticationOauthClientSecret: ").append(toIndentedString(authenticationOauthClientSecret)).append("\n");
    sb.append("    authenticationOauthClientTokenEndpoint: ").append(toIndentedString(authenticationOauthClientTokenEndpoint)).append("\n");
    sb.append("    authenticationOauthJwtSecretKey: ").append(toIndentedString(authenticationOauthJwtSecretKey)).append("\n");
    sb.append("    authenticationOauthJwtTokenEndpoint: ").append(toIndentedString(authenticationOauthJwtTokenEndpoint)).append("\n");
    sb.append("    authenticationScheme: ").append(toIndentedString(authenticationScheme)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    httpMethod: ").append(toIndentedString(httpMethod)).append("\n");
    sb.append("    localInterface: ").append(toIndentedString(localInterface)).append("\n");
    sb.append("    maxPostWaitTime: ").append(toIndentedString(maxPostWaitTime)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    outgoingConnectionCount: ").append(toIndentedString(outgoingConnectionCount)).append("\n");
    sb.append("    remoteHost: ").append(toIndentedString(remoteHost)).append("\n");
    sb.append("    remotePort: ").append(toIndentedString(remotePort)).append("\n");
    sb.append("    restConsumerName: ").append(toIndentedString(restConsumerName)).append("\n");
    sb.append("    restDeliveryPointName: ").append(toIndentedString(restDeliveryPointName)).append("\n");
    sb.append("    retryDelay: ").append(toIndentedString(retryDelay)).append("\n");
    sb.append("    tlsCipherSuiteList: ").append(toIndentedString(tlsCipherSuiteList)).append("\n");
    sb.append("    tlsEnabled: ").append(toIndentedString(tlsEnabled)).append("\n");
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

