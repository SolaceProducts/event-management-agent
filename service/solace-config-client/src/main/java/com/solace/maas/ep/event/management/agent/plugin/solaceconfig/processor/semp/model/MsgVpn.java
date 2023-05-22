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
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.EventThreshold;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.EventThresholdByValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * MsgVpn
 */
@JsonPropertyOrder({
  MsgVpn.JSON_PROPERTY_ALIAS,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_BASIC_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_BASIC_PROFILE_NAME,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_BASIC_RADIUS_DOMAIN,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_BASIC_TYPE,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ALLOW_API_PROVIDED_USERNAME_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CERTIFICATE_MATCHING_RULES_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_REVOCATION_CHECK_MODE,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_USERNAME_SOURCE,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_KERBEROS_ALLOW_API_PROVIDED_USERNAME_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_KERBEROS_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROFILE_NAME,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROVIDER_NAME,
  MsgVpn.JSON_PROPERTY_AUTHENTICATION_OAUTH_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHORIZATION_LDAP_GROUP_MEMBERSHIP_ATTRIBUTE_NAME,
  MsgVpn.JSON_PROPERTY_AUTHORIZATION_LDAP_TRIM_CLIENT_USERNAME_DOMAIN_ENABLED,
  MsgVpn.JSON_PROPERTY_AUTHORIZATION_PROFILE_NAME,
  MsgVpn.JSON_PROPERTY_AUTHORIZATION_TYPE,
  MsgVpn.JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED,
  MsgVpn.JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_MAX_CHAIN_DEPTH,
  MsgVpn.JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED,
  MsgVpn.JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED,
  MsgVpn.JSON_PROPERTY_DISTRIBUTED_CACHE_MANAGEMENT_ENABLED,
  MsgVpn.JSON_PROPERTY_DMR_ENABLED,
  MsgVpn.JSON_PROPERTY_ENABLED,
  MsgVpn.JSON_PROPERTY_EVENT_CONNECTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_EGRESS_FLOW_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_EGRESS_MSG_RATE_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_ENDPOINT_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_INGRESS_FLOW_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_INGRESS_MSG_RATE_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_LARGE_MSG_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_LOG_TAG,
  MsgVpn.JSON_PROPERTY_EVENT_MSG_SPOOL_USAGE_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_PUBLISH_CLIENT_ENABLED,
  MsgVpn.JSON_PROPERTY_EVENT_PUBLISH_MSG_VPN_ENABLED,
  MsgVpn.JSON_PROPERTY_EVENT_PUBLISH_SUBSCRIPTION_MODE,
  MsgVpn.JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_MQTT_ENABLED,
  MsgVpn.JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_SMF_ENABLED,
  MsgVpn.JSON_PROPERTY_EVENT_SERVICE_AMQP_CONNECTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_SERVICE_MQTT_CONNECTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_SERVICE_REST_INCOMING_CONNECTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_SUBSCRIPTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EVENT_TRANSACTION_COUNT_THRESHOLD,
  MsgVpn.JSON_PROPERTY_EXPORT_SUBSCRIPTIONS_ENABLED,
  MsgVpn.JSON_PROPERTY_JNDI_ENABLED,
  MsgVpn.JSON_PROPERTY_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_MAX_EGRESS_FLOW_COUNT,
  MsgVpn.JSON_PROPERTY_MAX_ENDPOINT_COUNT,
  MsgVpn.JSON_PROPERTY_MAX_INGRESS_FLOW_COUNT,
  MsgVpn.JSON_PROPERTY_MAX_MSG_SPOOL_USAGE,
  MsgVpn.JSON_PROPERTY_MAX_SUBSCRIPTION_COUNT,
  MsgVpn.JSON_PROPERTY_MAX_TRANSACTED_SESSION_COUNT,
  MsgVpn.JSON_PROPERTY_MAX_TRANSACTION_COUNT,
  MsgVpn.JSON_PROPERTY_MQTT_RETAIN_MAX_MEMORY,
  MsgVpn.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpn.JSON_PROPERTY_REPLICATION_ACK_PROPAGATION_INTERVAL_MSG_COUNT,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_CLIENT_USERNAME,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_PASSWORD,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_CONTENT,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_PASSWORD,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_SCHEME,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_COMPRESSED_DATA_ENABLED,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_EGRESS_FLOW_WINDOW_SIZE,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_RETRY_DELAY,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_TLS_ENABLED,
  MsgVpn.JSON_PROPERTY_REPLICATION_BRIDGE_UNIDIRECTIONAL_CLIENT_PROFILE_NAME,
  MsgVpn.JSON_PROPERTY_REPLICATION_ENABLED,
  MsgVpn.JSON_PROPERTY_REPLICATION_ENABLED_QUEUE_BEHAVIOR,
  MsgVpn.JSON_PROPERTY_REPLICATION_QUEUE_MAX_MSG_SPOOL_USAGE,
  MsgVpn.JSON_PROPERTY_REPLICATION_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_ENABLED,
  MsgVpn.JSON_PROPERTY_REPLICATION_REJECT_MSG_WHEN_SYNC_INELIGIBLE_ENABLED,
  MsgVpn.JSON_PROPERTY_REPLICATION_ROLE,
  MsgVpn.JSON_PROPERTY_REPLICATION_TRANSACTION_MODE,
  MsgVpn.JSON_PROPERTY_REST_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED,
  MsgVpn.JSON_PROPERTY_REST_TLS_SERVER_CERT_MAX_CHAIN_DEPTH,
  MsgVpn.JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED,
  MsgVpn.JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED,
  MsgVpn.JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_CLIENT_ENABLED,
  MsgVpn.JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_DISTRIBUTED_CACHE_ENABLED,
  MsgVpn.JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_ENABLED,
  MsgVpn.JSON_PROPERTY_SEMP_OVER_MSG_BUS_ENABLED,
  MsgVpn.JSON_PROPERTY_SEMP_OVER_MSG_BUS_SHOW_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_AMQP_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_AMQP_TLS_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_AMQP_TLS_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_AUTHENTICATION_CLIENT_CERT_REQUEST,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_TLS_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_TLS_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHENTICATION_CLIENT_CERT_REQUEST,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHORIZATION_HEADER_HANDLING,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_LISTEN_PORT,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_MODE,
  MsgVpn.JSON_PROPERTY_SERVICE_REST_OUTGOING_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_SMF_TLS_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_WEB_AUTHENTICATION_CLIENT_CERT_REQUEST,
  MsgVpn.JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT,
  MsgVpn.JSON_PROPERTY_SERVICE_WEB_PLAIN_TEXT_ENABLED,
  MsgVpn.JSON_PROPERTY_SERVICE_WEB_TLS_ENABLED,
  MsgVpn.JSON_PROPERTY_TLS_ALLOW_DOWNGRADE_TO_PLAIN_TEXT_ENABLED
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-17T23:49:01.929728+01:00[Europe/London]")
public class MsgVpn {
  public static final String JSON_PROPERTY_ALIAS = "alias";
  private String alias;

  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_ENABLED = "authenticationBasicEnabled";
  private Boolean authenticationBasicEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_PROFILE_NAME = "authenticationBasicProfileName";
  private String authenticationBasicProfileName;

  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_RADIUS_DOMAIN = "authenticationBasicRadiusDomain";
  private String authenticationBasicRadiusDomain;

  /**
   * The type of basic authentication to use for clients connecting to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;radius\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Internal database. Authentication is against Client Usernames. \&quot;ldap\&quot; - LDAP authentication. An LDAP profile name must be provided. \&quot;radius\&quot; - RADIUS authentication. A RADIUS profile name must be provided. \&quot;none\&quot; - No authentication. Anonymous login allowed. &lt;/pre&gt; 
   */
  public enum AuthenticationBasicTypeEnum {
    INTERNAL("internal"),
    
    LDAP("ldap"),
    
    RADIUS("radius"),
    
    NONE("none");

    private String value;

    AuthenticationBasicTypeEnum(String value) {
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
    public static AuthenticationBasicTypeEnum fromValue(String value) {
      for (AuthenticationBasicTypeEnum b : AuthenticationBasicTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_TYPE = "authenticationBasicType";
  private AuthenticationBasicTypeEnum authenticationBasicType;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ALLOW_API_PROVIDED_USERNAME_ENABLED = "authenticationClientCertAllowApiProvidedUsernameEnabled";
  private Boolean authenticationClientCertAllowApiProvidedUsernameEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CERTIFICATE_MATCHING_RULES_ENABLED = "authenticationClientCertCertificateMatchingRulesEnabled";
  private Boolean authenticationClientCertCertificateMatchingRulesEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ENABLED = "authenticationClientCertEnabled";
  private Boolean authenticationClientCertEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH = "authenticationClientCertMaxChainDepth";
  private Long authenticationClientCertMaxChainDepth;

  /**
   * The desired behavior for client certificate revocation checking. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;allow-valid\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow-all\&quot; - Allow the client to authenticate, the result of client certificate revocation check is ignored. \&quot;allow-unknown\&quot; - Allow the client to authenticate even if the revocation status of his certificate cannot be determined. \&quot;allow-valid\&quot; - Allow the client to authenticate only when the revocation check returned an explicit positive response. &lt;/pre&gt;  Available since 2.6.
   */
  public enum AuthenticationClientCertRevocationCheckModeEnum {
    ALL("allow-all"),
    
    UNKNOWN("allow-unknown"),
    
    VALID("allow-valid");

    private String value;

    AuthenticationClientCertRevocationCheckModeEnum(String value) {
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
    public static AuthenticationClientCertRevocationCheckModeEnum fromValue(String value) {
      for (AuthenticationClientCertRevocationCheckModeEnum b : AuthenticationClientCertRevocationCheckModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_REVOCATION_CHECK_MODE = "authenticationClientCertRevocationCheckMode";
  private AuthenticationClientCertRevocationCheckModeEnum authenticationClientCertRevocationCheckMode;

  /**
   * The field from the client certificate to use as the client username. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;common-name\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;certificate-thumbprint\&quot; - The username is computed as the SHA-1 hash over the entire DER-encoded contents of the client certificate. \&quot;common-name\&quot; - The username is extracted from the certificate&#39;s first instance of the Common Name attribute in the Subject DN. \&quot;common-name-last\&quot; - The username is extracted from the certificate&#39;s last instance of the Common Name attribute in the Subject DN. \&quot;subject-alternate-name-msupn\&quot; - The username is extracted from the certificate&#39;s Other Name type of the Subject Alternative Name and must have the msUPN signature. \&quot;uid\&quot; - The username is extracted from the certificate&#39;s first instance of the User Identifier attribute in the Subject DN. \&quot;uid-last\&quot; - The username is extracted from the certificate&#39;s last instance of the User Identifier attribute in the Subject DN. &lt;/pre&gt;  Available since 2.6.
   */
  public enum AuthenticationClientCertUsernameSourceEnum {
    CERTIFICATE_THUMBPRINT("certificate-thumbprint"),
    
    COMMON_NAME("common-name"),
    
    COMMON_NAME_LAST("common-name-last"),
    
    SUBJECT_ALTERNATE_NAME_MSUPN("subject-alternate-name-msupn"),
    
    UID("uid"),
    
    UID_LAST("uid-last");

    private String value;

    AuthenticationClientCertUsernameSourceEnum(String value) {
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
    public static AuthenticationClientCertUsernameSourceEnum fromValue(String value) {
      for (AuthenticationClientCertUsernameSourceEnum b : AuthenticationClientCertUsernameSourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_USERNAME_SOURCE = "authenticationClientCertUsernameSource";
  private AuthenticationClientCertUsernameSourceEnum authenticationClientCertUsernameSource;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED = "authenticationClientCertValidateDateEnabled";
  private Boolean authenticationClientCertValidateDateEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_KERBEROS_ALLOW_API_PROVIDED_USERNAME_ENABLED = "authenticationKerberosAllowApiProvidedUsernameEnabled";
  private Boolean authenticationKerberosAllowApiProvidedUsernameEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_KERBEROS_ENABLED = "authenticationKerberosEnabled";
  private Boolean authenticationKerberosEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROFILE_NAME = "authenticationOauthDefaultProfileName";
  private String authenticationOauthDefaultProfileName;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROVIDER_NAME = "authenticationOauthDefaultProviderName";
  private String authenticationOauthDefaultProviderName;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_ENABLED = "authenticationOauthEnabled";
  private Boolean authenticationOauthEnabled;

  public static final String JSON_PROPERTY_AUTHORIZATION_LDAP_GROUP_MEMBERSHIP_ATTRIBUTE_NAME = "authorizationLdapGroupMembershipAttributeName";
  private String authorizationLdapGroupMembershipAttributeName;

  public static final String JSON_PROPERTY_AUTHORIZATION_LDAP_TRIM_CLIENT_USERNAME_DOMAIN_ENABLED = "authorizationLdapTrimClientUsernameDomainEnabled";
  private Boolean authorizationLdapTrimClientUsernameDomainEnabled;

  public static final String JSON_PROPERTY_AUTHORIZATION_PROFILE_NAME = "authorizationProfileName";
  private String authorizationProfileName;

  /**
   * The type of authorization to use for clients connecting to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;internal\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;ldap\&quot; - LDAP authorization. \&quot;internal\&quot; - Internal authorization. &lt;/pre&gt; 
   */
  public enum AuthorizationTypeEnum {
    LDAP("ldap"),
    
    INTERNAL("internal");

    private String value;

    AuthorizationTypeEnum(String value) {
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
    public static AuthorizationTypeEnum fromValue(String value) {
      for (AuthorizationTypeEnum b : AuthorizationTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTHORIZATION_TYPE = "authorizationType";
  private AuthorizationTypeEnum authorizationType;

  public static final String JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED = "bridgingTlsServerCertEnforceTrustedCommonNameEnabled";
  private Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled;

  public static final String JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_MAX_CHAIN_DEPTH = "bridgingTlsServerCertMaxChainDepth";
  private Long bridgingTlsServerCertMaxChainDepth;

  public static final String JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED = "bridgingTlsServerCertValidateDateEnabled";
  private Boolean bridgingTlsServerCertValidateDateEnabled;

  public static final String JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED = "bridgingTlsServerCertValidateNameEnabled";
  private Boolean bridgingTlsServerCertValidateNameEnabled;

  public static final String JSON_PROPERTY_DISTRIBUTED_CACHE_MANAGEMENT_ENABLED = "distributedCacheManagementEnabled";
  private Boolean distributedCacheManagementEnabled;

  public static final String JSON_PROPERTY_DMR_ENABLED = "dmrEnabled";
  private Boolean dmrEnabled;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_EVENT_CONNECTION_COUNT_THRESHOLD = "eventConnectionCountThreshold";
  private EventThreshold eventConnectionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_EGRESS_FLOW_COUNT_THRESHOLD = "eventEgressFlowCountThreshold";
  private EventThreshold eventEgressFlowCountThreshold;

  public static final String JSON_PROPERTY_EVENT_EGRESS_MSG_RATE_THRESHOLD = "eventEgressMsgRateThreshold";
  private EventThresholdByValue eventEgressMsgRateThreshold;

  public static final String JSON_PROPERTY_EVENT_ENDPOINT_COUNT_THRESHOLD = "eventEndpointCountThreshold";
  private EventThreshold eventEndpointCountThreshold;

  public static final String JSON_PROPERTY_EVENT_INGRESS_FLOW_COUNT_THRESHOLD = "eventIngressFlowCountThreshold";
  private EventThreshold eventIngressFlowCountThreshold;

  public static final String JSON_PROPERTY_EVENT_INGRESS_MSG_RATE_THRESHOLD = "eventIngressMsgRateThreshold";
  private EventThresholdByValue eventIngressMsgRateThreshold;

  public static final String JSON_PROPERTY_EVENT_LARGE_MSG_THRESHOLD = "eventLargeMsgThreshold";
  private Long eventLargeMsgThreshold;

  public static final String JSON_PROPERTY_EVENT_LOG_TAG = "eventLogTag";
  private String eventLogTag;

  public static final String JSON_PROPERTY_EVENT_MSG_SPOOL_USAGE_THRESHOLD = "eventMsgSpoolUsageThreshold";
  private EventThreshold eventMsgSpoolUsageThreshold;

  public static final String JSON_PROPERTY_EVENT_PUBLISH_CLIENT_ENABLED = "eventPublishClientEnabled";
  private Boolean eventPublishClientEnabled;

  public static final String JSON_PROPERTY_EVENT_PUBLISH_MSG_VPN_ENABLED = "eventPublishMsgVpnEnabled";
  private Boolean eventPublishMsgVpnEnabled;

  /**
   * Subscription level Event message publishing mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;off\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;off\&quot; - Disable client level event message publishing. \&quot;on-with-format-v1\&quot; - Enable client level event message publishing with format v1. \&quot;on-with-no-unsubscribe-events-on-disconnect-format-v1\&quot; - As \&quot;on-with-format-v1\&quot;, but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. \&quot;on-with-format-v2\&quot; - Enable client level event message publishing with format v2. \&quot;on-with-no-unsubscribe-events-on-disconnect-format-v2\&quot; - As \&quot;on-with-format-v2\&quot;, but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. &lt;/pre&gt; 
   */
  public enum EventPublishSubscriptionModeEnum {
    OFF("off"),
    
    ON_WITH_FORMAT_V1("on-with-format-v1"),
    
    ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V1("on-with-no-unsubscribe-events-on-disconnect-format-v1"),
    
    ON_WITH_FORMAT_V2("on-with-format-v2"),
    
    ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V2("on-with-no-unsubscribe-events-on-disconnect-format-v2");

    private String value;

    EventPublishSubscriptionModeEnum(String value) {
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
    public static EventPublishSubscriptionModeEnum fromValue(String value) {
      for (EventPublishSubscriptionModeEnum b : EventPublishSubscriptionModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_EVENT_PUBLISH_SUBSCRIPTION_MODE = "eventPublishSubscriptionMode";
  private EventPublishSubscriptionModeEnum eventPublishSubscriptionMode;

  public static final String JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_MQTT_ENABLED = "eventPublishTopicFormatMqttEnabled";
  private Boolean eventPublishTopicFormatMqttEnabled;

  public static final String JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_SMF_ENABLED = "eventPublishTopicFormatSmfEnabled";
  private Boolean eventPublishTopicFormatSmfEnabled;

  public static final String JSON_PROPERTY_EVENT_SERVICE_AMQP_CONNECTION_COUNT_THRESHOLD = "eventServiceAmqpConnectionCountThreshold";
  private EventThreshold eventServiceAmqpConnectionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_SERVICE_MQTT_CONNECTION_COUNT_THRESHOLD = "eventServiceMqttConnectionCountThreshold";
  private EventThreshold eventServiceMqttConnectionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_SERVICE_REST_INCOMING_CONNECTION_COUNT_THRESHOLD = "eventServiceRestIncomingConnectionCountThreshold";
  private EventThreshold eventServiceRestIncomingConnectionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_THRESHOLD = "eventServiceSmfConnectionCountThreshold";
  private EventThreshold eventServiceSmfConnectionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_THRESHOLD = "eventServiceWebConnectionCountThreshold";
  private EventThreshold eventServiceWebConnectionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_SUBSCRIPTION_COUNT_THRESHOLD = "eventSubscriptionCountThreshold";
  private EventThreshold eventSubscriptionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD = "eventTransactedSessionCountThreshold";
  private EventThreshold eventTransactedSessionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_TRANSACTION_COUNT_THRESHOLD = "eventTransactionCountThreshold";
  private EventThreshold eventTransactionCountThreshold;

  public static final String JSON_PROPERTY_EXPORT_SUBSCRIPTIONS_ENABLED = "exportSubscriptionsEnabled";
  private Boolean exportSubscriptionsEnabled;

  public static final String JSON_PROPERTY_JNDI_ENABLED = "jndiEnabled";
  private Boolean jndiEnabled;

  public static final String JSON_PROPERTY_MAX_CONNECTION_COUNT = "maxConnectionCount";
  private Long maxConnectionCount;

  public static final String JSON_PROPERTY_MAX_EGRESS_FLOW_COUNT = "maxEgressFlowCount";
  private Long maxEgressFlowCount;

  public static final String JSON_PROPERTY_MAX_ENDPOINT_COUNT = "maxEndpointCount";
  private Long maxEndpointCount;

  public static final String JSON_PROPERTY_MAX_INGRESS_FLOW_COUNT = "maxIngressFlowCount";
  private Long maxIngressFlowCount;

  public static final String JSON_PROPERTY_MAX_MSG_SPOOL_USAGE = "maxMsgSpoolUsage";
  private Long maxMsgSpoolUsage;

  public static final String JSON_PROPERTY_MAX_SUBSCRIPTION_COUNT = "maxSubscriptionCount";
  private Long maxSubscriptionCount;

  public static final String JSON_PROPERTY_MAX_TRANSACTED_SESSION_COUNT = "maxTransactedSessionCount";
  private Long maxTransactedSessionCount;

  public static final String JSON_PROPERTY_MAX_TRANSACTION_COUNT = "maxTransactionCount";
  private Long maxTransactionCount;

  public static final String JSON_PROPERTY_MQTT_RETAIN_MAX_MEMORY = "mqttRetainMaxMemory";
  private Integer mqttRetainMaxMemory;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_REPLICATION_ACK_PROPAGATION_INTERVAL_MSG_COUNT = "replicationAckPropagationIntervalMsgCount";
  private Long replicationAckPropagationIntervalMsgCount;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_CLIENT_USERNAME = "replicationBridgeAuthenticationBasicClientUsername";
  private String replicationBridgeAuthenticationBasicClientUsername;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_PASSWORD = "replicationBridgeAuthenticationBasicPassword";
  private String replicationBridgeAuthenticationBasicPassword;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_CONTENT = "replicationBridgeAuthenticationClientCertContent";
  private String replicationBridgeAuthenticationClientCertContent;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_PASSWORD = "replicationBridgeAuthenticationClientCertPassword";
  private String replicationBridgeAuthenticationClientCertPassword;

  /**
   * The authentication scheme for the replication Bridge in the Message VPN. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt; 
   */
  public enum ReplicationBridgeAuthenticationSchemeEnum {
    BASIC("basic"),
    
    CLIENT_CERTIFICATE("client-certificate");

    private String value;

    ReplicationBridgeAuthenticationSchemeEnum(String value) {
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
    public static ReplicationBridgeAuthenticationSchemeEnum fromValue(String value) {
      for (ReplicationBridgeAuthenticationSchemeEnum b : ReplicationBridgeAuthenticationSchemeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_SCHEME = "replicationBridgeAuthenticationScheme";
  private ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_COMPRESSED_DATA_ENABLED = "replicationBridgeCompressedDataEnabled";
  private Boolean replicationBridgeCompressedDataEnabled;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_EGRESS_FLOW_WINDOW_SIZE = "replicationBridgeEgressFlowWindowSize";
  private Long replicationBridgeEgressFlowWindowSize;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_RETRY_DELAY = "replicationBridgeRetryDelay";
  private Long replicationBridgeRetryDelay;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_TLS_ENABLED = "replicationBridgeTlsEnabled";
  private Boolean replicationBridgeTlsEnabled;

  public static final String JSON_PROPERTY_REPLICATION_BRIDGE_UNIDIRECTIONAL_CLIENT_PROFILE_NAME = "replicationBridgeUnidirectionalClientProfileName";
  private String replicationBridgeUnidirectionalClientProfileName;

  public static final String JSON_PROPERTY_REPLICATION_ENABLED = "replicationEnabled";
  private Boolean replicationEnabled;

  /**
   * The behavior to take when enabling replication for the Message VPN, depending on the existence of the replication Queue. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;fail-on-existing-queue\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;fail-on-existing-queue\&quot; - The data replication queue must not already exist. \&quot;force-use-existing-queue\&quot; - The data replication queue must already exist. Any data messages on the Queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail. \&quot;force-recreate-queue\&quot; - The data replication queue must already exist. Any data messages on the Queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. &lt;/pre&gt; 
   */
  public enum ReplicationEnabledQueueBehaviorEnum {
    FAIL_ON_EXISTING_QUEUE("fail-on-existing-queue"),
    
    FORCE_USE_EXISTING_QUEUE("force-use-existing-queue"),
    
    FORCE_RECREATE_QUEUE("force-recreate-queue");

    private String value;

    ReplicationEnabledQueueBehaviorEnum(String value) {
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
    public static ReplicationEnabledQueueBehaviorEnum fromValue(String value) {
      for (ReplicationEnabledQueueBehaviorEnum b : ReplicationEnabledQueueBehaviorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_REPLICATION_ENABLED_QUEUE_BEHAVIOR = "replicationEnabledQueueBehavior";
  private ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior;

  public static final String JSON_PROPERTY_REPLICATION_QUEUE_MAX_MSG_SPOOL_USAGE = "replicationQueueMaxMsgSpoolUsage";
  private Long replicationQueueMaxMsgSpoolUsage;

  public static final String JSON_PROPERTY_REPLICATION_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_ENABLED = "replicationQueueRejectMsgToSenderOnDiscardEnabled";
  private Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled;

  public static final String JSON_PROPERTY_REPLICATION_REJECT_MSG_WHEN_SYNC_INELIGIBLE_ENABLED = "replicationRejectMsgWhenSyncIneligibleEnabled";
  private Boolean replicationRejectMsgWhenSyncIneligibleEnabled;

  /**
   * The replication role for the Message VPN. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;standby\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;active\&quot; - Assume the Active role in replication for the Message VPN. \&quot;standby\&quot; - Assume the Standby role in replication for the Message VPN. &lt;/pre&gt; 
   */
  public enum ReplicationRoleEnum {
    ACTIVE("active"),
    
    STANDBY("standby");

    private String value;

    ReplicationRoleEnum(String value) {
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
    public static ReplicationRoleEnum fromValue(String value) {
      for (ReplicationRoleEnum b : ReplicationRoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_REPLICATION_ROLE = "replicationRole";
  private ReplicationRoleEnum replicationRole;

  /**
   * The transaction replication mode for all transactions within the Message VPN. Changing this value during operation will not affect existing transactions; it is only used upon starting a transaction. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;async\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;sync\&quot; - Messages are acknowledged when replicated (spooled remotely). \&quot;async\&quot; - Messages are acknowledged when pending replication (spooled locally). &lt;/pre&gt; 
   */
  public enum ReplicationTransactionModeEnum {
    SYNC("sync"),
    
    ASYNC("async");

    private String value;

    ReplicationTransactionModeEnum(String value) {
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
    public static ReplicationTransactionModeEnum fromValue(String value) {
      for (ReplicationTransactionModeEnum b : ReplicationTransactionModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_REPLICATION_TRANSACTION_MODE = "replicationTransactionMode";
  private ReplicationTransactionModeEnum replicationTransactionMode;

  public static final String JSON_PROPERTY_REST_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED = "restTlsServerCertEnforceTrustedCommonNameEnabled";
  private Boolean restTlsServerCertEnforceTrustedCommonNameEnabled;

  public static final String JSON_PROPERTY_REST_TLS_SERVER_CERT_MAX_CHAIN_DEPTH = "restTlsServerCertMaxChainDepth";
  private Long restTlsServerCertMaxChainDepth;

  public static final String JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED = "restTlsServerCertValidateDateEnabled";
  private Boolean restTlsServerCertValidateDateEnabled;

  public static final String JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED = "restTlsServerCertValidateNameEnabled";
  private Boolean restTlsServerCertValidateNameEnabled;

  public static final String JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_CLIENT_ENABLED = "sempOverMsgBusAdminClientEnabled";
  private Boolean sempOverMsgBusAdminClientEnabled;

  public static final String JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_DISTRIBUTED_CACHE_ENABLED = "sempOverMsgBusAdminDistributedCacheEnabled";
  private Boolean sempOverMsgBusAdminDistributedCacheEnabled;

  public static final String JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_ENABLED = "sempOverMsgBusAdminEnabled";
  private Boolean sempOverMsgBusAdminEnabled;

  public static final String JSON_PROPERTY_SEMP_OVER_MSG_BUS_ENABLED = "sempOverMsgBusEnabled";
  private Boolean sempOverMsgBusEnabled;

  public static final String JSON_PROPERTY_SEMP_OVER_MSG_BUS_SHOW_ENABLED = "sempOverMsgBusShowEnabled";
  private Boolean sempOverMsgBusShowEnabled;

  public static final String JSON_PROPERTY_SERVICE_AMQP_MAX_CONNECTION_COUNT = "serviceAmqpMaxConnectionCount";
  private Long serviceAmqpMaxConnectionCount;

  public static final String JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_ENABLED = "serviceAmqpPlainTextEnabled";
  private Boolean serviceAmqpPlainTextEnabled;

  public static final String JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_LISTEN_PORT = "serviceAmqpPlainTextListenPort";
  private Long serviceAmqpPlainTextListenPort;

  public static final String JSON_PROPERTY_SERVICE_AMQP_TLS_ENABLED = "serviceAmqpTlsEnabled";
  private Boolean serviceAmqpTlsEnabled;

  public static final String JSON_PROPERTY_SERVICE_AMQP_TLS_LISTEN_PORT = "serviceAmqpTlsListenPort";
  private Long serviceAmqpTlsListenPort;

  /**
   * Determines when to request a client certificate from an incoming MQTT client connecting via a TLS port. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-enabled-in-message-vpn\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;never\&quot; - Never ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;when-enabled-in-message-vpn\&quot; - Only ask for a client-certificate if client certificate authentication is enabled under \&quot;message-vpn &gt;  authentication &gt; client-certificate &gt; shutdown\&quot;. &lt;/pre&gt;  Available since 2.21.
   */
  public enum ServiceMqttAuthenticationClientCertRequestEnum {
    ALWAYS("always"),
    
    NEVER("never"),
    
    WHEN_ENABLED_IN_MESSAGE_VPN("when-enabled-in-message-vpn");

    private String value;

    ServiceMqttAuthenticationClientCertRequestEnum(String value) {
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
    public static ServiceMqttAuthenticationClientCertRequestEnum fromValue(String value) {
      for (ServiceMqttAuthenticationClientCertRequestEnum b : ServiceMqttAuthenticationClientCertRequestEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SERVICE_MQTT_AUTHENTICATION_CLIENT_CERT_REQUEST = "serviceMqttAuthenticationClientCertRequest";
  private ServiceMqttAuthenticationClientCertRequestEnum serviceMqttAuthenticationClientCertRequest;

  public static final String JSON_PROPERTY_SERVICE_MQTT_MAX_CONNECTION_COUNT = "serviceMqttMaxConnectionCount";
  private Long serviceMqttMaxConnectionCount;

  public static final String JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_ENABLED = "serviceMqttPlainTextEnabled";
  private Boolean serviceMqttPlainTextEnabled;

  public static final String JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_LISTEN_PORT = "serviceMqttPlainTextListenPort";
  private Long serviceMqttPlainTextListenPort;

  public static final String JSON_PROPERTY_SERVICE_MQTT_TLS_ENABLED = "serviceMqttTlsEnabled";
  private Boolean serviceMqttTlsEnabled;

  public static final String JSON_PROPERTY_SERVICE_MQTT_TLS_LISTEN_PORT = "serviceMqttTlsListenPort";
  private Long serviceMqttTlsListenPort;

  public static final String JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_ENABLED = "serviceMqttTlsWebSocketEnabled";
  private Boolean serviceMqttTlsWebSocketEnabled;

  public static final String JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_LISTEN_PORT = "serviceMqttTlsWebSocketListenPort";
  private Long serviceMqttTlsWebSocketListenPort;

  public static final String JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_ENABLED = "serviceMqttWebSocketEnabled";
  private Boolean serviceMqttWebSocketEnabled;

  public static final String JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_LISTEN_PORT = "serviceMqttWebSocketListenPort";
  private Long serviceMqttWebSocketListenPort;

  /**
   * Determines when to request a client certificate from an incoming REST Producer connecting via a TLS port. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-enabled-in-message-vpn\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;never\&quot; - Never ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;when-enabled-in-message-vpn\&quot; - Only ask for a client-certificate if client certificate authentication is enabled under \&quot;message-vpn &gt;  authentication &gt; client-certificate &gt; shutdown\&quot;. &lt;/pre&gt;  Available since 2.21.
   */
  public enum ServiceRestIncomingAuthenticationClientCertRequestEnum {
    ALWAYS("always"),
    
    NEVER("never"),
    
    WHEN_ENABLED_IN_MESSAGE_VPN("when-enabled-in-message-vpn");

    private String value;

    ServiceRestIncomingAuthenticationClientCertRequestEnum(String value) {
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
    public static ServiceRestIncomingAuthenticationClientCertRequestEnum fromValue(String value) {
      for (ServiceRestIncomingAuthenticationClientCertRequestEnum b : ServiceRestIncomingAuthenticationClientCertRequestEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHENTICATION_CLIENT_CERT_REQUEST = "serviceRestIncomingAuthenticationClientCertRequest";
  private ServiceRestIncomingAuthenticationClientCertRequestEnum serviceRestIncomingAuthenticationClientCertRequest;

  /**
   * The handling of Authorization headers for incoming REST connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;drop\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;drop\&quot; - Do not attach the Authorization header to the message as a user property. This configuration is most secure. \&quot;forward\&quot; - Forward the Authorization header, attaching it to the message as a user property in the same way as other headers. For best security, use the drop setting. \&quot;legacy\&quot; - If the Authorization header was used for authentication to the broker, do not attach it to the message. If the Authorization header was not used for authentication to the broker, attach it to the message as a user property in the same way as other headers. For best security, use the drop setting. &lt;/pre&gt;  Available since 2.19.
   */
  public enum ServiceRestIncomingAuthorizationHeaderHandlingEnum {
    DROP("drop"),
    
    FORWARD("forward"),
    
    LEGACY("legacy");

    private String value;

    ServiceRestIncomingAuthorizationHeaderHandlingEnum(String value) {
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
    public static ServiceRestIncomingAuthorizationHeaderHandlingEnum fromValue(String value) {
      for (ServiceRestIncomingAuthorizationHeaderHandlingEnum b : ServiceRestIncomingAuthorizationHeaderHandlingEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHORIZATION_HEADER_HANDLING = "serviceRestIncomingAuthorizationHeaderHandling";
  private ServiceRestIncomingAuthorizationHeaderHandlingEnum serviceRestIncomingAuthorizationHeaderHandling;

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_MAX_CONNECTION_COUNT = "serviceRestIncomingMaxConnectionCount";
  private Long serviceRestIncomingMaxConnectionCount;

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_ENABLED = "serviceRestIncomingPlainTextEnabled";
  private Boolean serviceRestIncomingPlainTextEnabled;

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_LISTEN_PORT = "serviceRestIncomingPlainTextListenPort";
  private Long serviceRestIncomingPlainTextListenPort;

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_ENABLED = "serviceRestIncomingTlsEnabled";
  private Boolean serviceRestIncomingTlsEnabled;

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_LISTEN_PORT = "serviceRestIncomingTlsListenPort";
  private Long serviceRestIncomingTlsListenPort;

  /**
   * The REST service mode for incoming REST clients that connect to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;messaging\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;gateway\&quot; - Act as a message gateway through which REST messages are propagated. \&quot;messaging\&quot; - Act as a message broker on which REST messages are queued. &lt;/pre&gt;  Available since 2.6.
   */
  public enum ServiceRestModeEnum {
    GATEWAY("gateway"),
    
    MESSAGING("messaging");

    private String value;

    ServiceRestModeEnum(String value) {
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
    public static ServiceRestModeEnum fromValue(String value) {
      for (ServiceRestModeEnum b : ServiceRestModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SERVICE_REST_MODE = "serviceRestMode";
  private ServiceRestModeEnum serviceRestMode;

  public static final String JSON_PROPERTY_SERVICE_REST_OUTGOING_MAX_CONNECTION_COUNT = "serviceRestOutgoingMaxConnectionCount";
  private Long serviceRestOutgoingMaxConnectionCount;

  public static final String JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT = "serviceSmfMaxConnectionCount";
  private Long serviceSmfMaxConnectionCount;

  public static final String JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_ENABLED = "serviceSmfPlainTextEnabled";
  private Boolean serviceSmfPlainTextEnabled;

  public static final String JSON_PROPERTY_SERVICE_SMF_TLS_ENABLED = "serviceSmfTlsEnabled";
  private Boolean serviceSmfTlsEnabled;

  /**
   * Determines when to request a client certificate from a Web Transport client connecting via a TLS port. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-enabled-in-message-vpn\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;never\&quot; - Never ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;when-enabled-in-message-vpn\&quot; - Only ask for a client-certificate if client certificate authentication is enabled under \&quot;message-vpn &gt;  authentication &gt; client-certificate &gt; shutdown\&quot;. &lt;/pre&gt;  Available since 2.21.
   */
  public enum ServiceWebAuthenticationClientCertRequestEnum {
    ALWAYS("always"),
    
    NEVER("never"),
    
    WHEN_ENABLED_IN_MESSAGE_VPN("when-enabled-in-message-vpn");

    private String value;

    ServiceWebAuthenticationClientCertRequestEnum(String value) {
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
    public static ServiceWebAuthenticationClientCertRequestEnum fromValue(String value) {
      for (ServiceWebAuthenticationClientCertRequestEnum b : ServiceWebAuthenticationClientCertRequestEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SERVICE_WEB_AUTHENTICATION_CLIENT_CERT_REQUEST = "serviceWebAuthenticationClientCertRequest";
  private ServiceWebAuthenticationClientCertRequestEnum serviceWebAuthenticationClientCertRequest;

  public static final String JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT = "serviceWebMaxConnectionCount";
  private Long serviceWebMaxConnectionCount;

  public static final String JSON_PROPERTY_SERVICE_WEB_PLAIN_TEXT_ENABLED = "serviceWebPlainTextEnabled";
  private Boolean serviceWebPlainTextEnabled;

  public static final String JSON_PROPERTY_SERVICE_WEB_TLS_ENABLED = "serviceWebTlsEnabled";
  private Boolean serviceWebTlsEnabled;

  public static final String JSON_PROPERTY_TLS_ALLOW_DOWNGRADE_TO_PLAIN_TEXT_ENABLED = "tlsAllowDowngradeToPlainTextEnabled";
  private Boolean tlsAllowDowngradeToPlainTextEnabled;

  public MsgVpn() {
  }

  public MsgVpn alias(String alias) {
    
    this.alias = alias;
    return this;
  }

   /**
   * The name of another Message VPN which this Message VPN is an alias for. When this Message VPN is enabled, the alias has no effect. When this Message VPN is disabled, Clients (but not Bridges and routing Links) logging into this Message VPN are automatically logged in to the other Message VPN, and authentication and authorization take place in the context of the other Message VPN.  Aliases may form a non-circular chain, cascading one to the next. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.14.
   * @return alias
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALIAS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAlias() {
    return alias;
  }


  @JsonProperty(JSON_PROPERTY_ALIAS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAlias(String alias) {
    this.alias = alias;
  }


  public MsgVpn authenticationBasicEnabled(Boolean authenticationBasicEnabled) {
    
    this.authenticationBasicEnabled = authenticationBasicEnabled;
    return this;
  }

   /**
   * Enable or disable basic authentication for clients connecting to the Message VPN. Basic authentication is authentication that involves the use of a username and password to prove identity. If a user provides credentials for a different authentication scheme, this setting is not applicable. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return authenticationBasicEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationBasicEnabled() {
    return authenticationBasicEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationBasicEnabled(Boolean authenticationBasicEnabled) {
    this.authenticationBasicEnabled = authenticationBasicEnabled;
  }


  public MsgVpn authenticationBasicProfileName(String authenticationBasicProfileName) {
    
    this.authenticationBasicProfileName = authenticationBasicProfileName;
    return this;
  }

   /**
   * The name of the RADIUS or LDAP Profile to use for basic authentication. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;.
   * @return authenticationBasicProfileName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationBasicProfileName() {
    return authenticationBasicProfileName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationBasicProfileName(String authenticationBasicProfileName) {
    this.authenticationBasicProfileName = authenticationBasicProfileName;
  }


  public MsgVpn authenticationBasicRadiusDomain(String authenticationBasicRadiusDomain) {
    
    this.authenticationBasicRadiusDomain = authenticationBasicRadiusDomain;
    return this;
  }

   /**
   * The RADIUS domain to use for basic authentication. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return authenticationBasicRadiusDomain
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_RADIUS_DOMAIN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationBasicRadiusDomain() {
    return authenticationBasicRadiusDomain;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_RADIUS_DOMAIN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationBasicRadiusDomain(String authenticationBasicRadiusDomain) {
    this.authenticationBasicRadiusDomain = authenticationBasicRadiusDomain;
  }


  public MsgVpn authenticationBasicType(AuthenticationBasicTypeEnum authenticationBasicType) {
    
    this.authenticationBasicType = authenticationBasicType;
    return this;
  }

   /**
   * The type of basic authentication to use for clients connecting to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;radius\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Internal database. Authentication is against Client Usernames. \&quot;ldap\&quot; - LDAP authentication. An LDAP profile name must be provided. \&quot;radius\&quot; - RADIUS authentication. A RADIUS profile name must be provided. \&quot;none\&quot; - No authentication. Anonymous login allowed. &lt;/pre&gt; 
   * @return authenticationBasicType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthenticationBasicTypeEnum getAuthenticationBasicType() {
    return authenticationBasicType;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationBasicType(AuthenticationBasicTypeEnum authenticationBasicType) {
    this.authenticationBasicType = authenticationBasicType;
  }


  public MsgVpn authenticationClientCertAllowApiProvidedUsernameEnabled(Boolean authenticationClientCertAllowApiProvidedUsernameEnabled) {
    
    this.authenticationClientCertAllowApiProvidedUsernameEnabled = authenticationClientCertAllowApiProvidedUsernameEnabled;
    return this;
  }

   /**
   * Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the certificate CN (Common Name) is always used. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return authenticationClientCertAllowApiProvidedUsernameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ALLOW_API_PROVIDED_USERNAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationClientCertAllowApiProvidedUsernameEnabled() {
    return authenticationClientCertAllowApiProvidedUsernameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ALLOW_API_PROVIDED_USERNAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertAllowApiProvidedUsernameEnabled(Boolean authenticationClientCertAllowApiProvidedUsernameEnabled) {
    this.authenticationClientCertAllowApiProvidedUsernameEnabled = authenticationClientCertAllowApiProvidedUsernameEnabled;
  }


  public MsgVpn authenticationClientCertCertificateMatchingRulesEnabled(Boolean authenticationClientCertCertificateMatchingRulesEnabled) {
    
    this.authenticationClientCertCertificateMatchingRulesEnabled = authenticationClientCertCertificateMatchingRulesEnabled;
    return this;
  }

   /**
   * Enable or disable certificate matching rules. When disabled, any valid certificate is accepted. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.27.
   * @return authenticationClientCertCertificateMatchingRulesEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CERTIFICATE_MATCHING_RULES_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationClientCertCertificateMatchingRulesEnabled() {
    return authenticationClientCertCertificateMatchingRulesEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CERTIFICATE_MATCHING_RULES_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertCertificateMatchingRulesEnabled(Boolean authenticationClientCertCertificateMatchingRulesEnabled) {
    this.authenticationClientCertCertificateMatchingRulesEnabled = authenticationClientCertCertificateMatchingRulesEnabled;
  }


  public MsgVpn authenticationClientCertEnabled(Boolean authenticationClientCertEnabled) {
    
    this.authenticationClientCertEnabled = authenticationClientCertEnabled;
    return this;
  }

   /**
   * Enable or disable client certificate authentication in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return authenticationClientCertEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationClientCertEnabled() {
    return authenticationClientCertEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertEnabled(Boolean authenticationClientCertEnabled) {
    this.authenticationClientCertEnabled = authenticationClientCertEnabled;
  }


  public MsgVpn authenticationClientCertMaxChainDepth(Long authenticationClientCertMaxChainDepth) {
    
    this.authenticationClientCertMaxChainDepth = authenticationClientCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;.
   * @return authenticationClientCertMaxChainDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getAuthenticationClientCertMaxChainDepth() {
    return authenticationClientCertMaxChainDepth;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertMaxChainDepth(Long authenticationClientCertMaxChainDepth) {
    this.authenticationClientCertMaxChainDepth = authenticationClientCertMaxChainDepth;
  }


  public MsgVpn authenticationClientCertRevocationCheckMode(AuthenticationClientCertRevocationCheckModeEnum authenticationClientCertRevocationCheckMode) {
    
    this.authenticationClientCertRevocationCheckMode = authenticationClientCertRevocationCheckMode;
    return this;
  }

   /**
   * The desired behavior for client certificate revocation checking. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;allow-valid\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow-all\&quot; - Allow the client to authenticate, the result of client certificate revocation check is ignored. \&quot;allow-unknown\&quot; - Allow the client to authenticate even if the revocation status of his certificate cannot be determined. \&quot;allow-valid\&quot; - Allow the client to authenticate only when the revocation check returned an explicit positive response. &lt;/pre&gt;  Available since 2.6.
   * @return authenticationClientCertRevocationCheckMode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_REVOCATION_CHECK_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthenticationClientCertRevocationCheckModeEnum getAuthenticationClientCertRevocationCheckMode() {
    return authenticationClientCertRevocationCheckMode;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_REVOCATION_CHECK_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertRevocationCheckMode(AuthenticationClientCertRevocationCheckModeEnum authenticationClientCertRevocationCheckMode) {
    this.authenticationClientCertRevocationCheckMode = authenticationClientCertRevocationCheckMode;
  }


  public MsgVpn authenticationClientCertUsernameSource(AuthenticationClientCertUsernameSourceEnum authenticationClientCertUsernameSource) {
    
    this.authenticationClientCertUsernameSource = authenticationClientCertUsernameSource;
    return this;
  }

   /**
   * The field from the client certificate to use as the client username. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;common-name\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;certificate-thumbprint\&quot; - The username is computed as the SHA-1 hash over the entire DER-encoded contents of the client certificate. \&quot;common-name\&quot; - The username is extracted from the certificate&#39;s first instance of the Common Name attribute in the Subject DN. \&quot;common-name-last\&quot; - The username is extracted from the certificate&#39;s last instance of the Common Name attribute in the Subject DN. \&quot;subject-alternate-name-msupn\&quot; - The username is extracted from the certificate&#39;s Other Name type of the Subject Alternative Name and must have the msUPN signature. \&quot;uid\&quot; - The username is extracted from the certificate&#39;s first instance of the User Identifier attribute in the Subject DN. \&quot;uid-last\&quot; - The username is extracted from the certificate&#39;s last instance of the User Identifier attribute in the Subject DN. &lt;/pre&gt;  Available since 2.6.
   * @return authenticationClientCertUsernameSource
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_USERNAME_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthenticationClientCertUsernameSourceEnum getAuthenticationClientCertUsernameSource() {
    return authenticationClientCertUsernameSource;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_USERNAME_SOURCE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertUsernameSource(AuthenticationClientCertUsernameSourceEnum authenticationClientCertUsernameSource) {
    this.authenticationClientCertUsernameSource = authenticationClientCertUsernameSource;
  }


  public MsgVpn authenticationClientCertValidateDateEnabled(Boolean authenticationClientCertValidateDateEnabled) {
    
    this.authenticationClientCertValidateDateEnabled = authenticationClientCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the client certificate. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return authenticationClientCertValidateDateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationClientCertValidateDateEnabled() {
    return authenticationClientCertValidateDateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationClientCertValidateDateEnabled(Boolean authenticationClientCertValidateDateEnabled) {
    this.authenticationClientCertValidateDateEnabled = authenticationClientCertValidateDateEnabled;
  }


  public MsgVpn authenticationKerberosAllowApiProvidedUsernameEnabled(Boolean authenticationKerberosAllowApiProvidedUsernameEnabled) {
    
    this.authenticationKerberosAllowApiProvidedUsernameEnabled = authenticationKerberosAllowApiProvidedUsernameEnabled;
    return this;
  }

   /**
   * Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the Kerberos Principal name is always used. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return authenticationKerberosAllowApiProvidedUsernameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_KERBEROS_ALLOW_API_PROVIDED_USERNAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationKerberosAllowApiProvidedUsernameEnabled() {
    return authenticationKerberosAllowApiProvidedUsernameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_KERBEROS_ALLOW_API_PROVIDED_USERNAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationKerberosAllowApiProvidedUsernameEnabled(Boolean authenticationKerberosAllowApiProvidedUsernameEnabled) {
    this.authenticationKerberosAllowApiProvidedUsernameEnabled = authenticationKerberosAllowApiProvidedUsernameEnabled;
  }


  public MsgVpn authenticationKerberosEnabled(Boolean authenticationKerberosEnabled) {
    
    this.authenticationKerberosEnabled = authenticationKerberosEnabled;
    return this;
  }

   /**
   * Enable or disable Kerberos authentication in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return authenticationKerberosEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_KERBEROS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationKerberosEnabled() {
    return authenticationKerberosEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_KERBEROS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationKerberosEnabled(Boolean authenticationKerberosEnabled) {
    this.authenticationKerberosEnabled = authenticationKerberosEnabled;
  }


  public MsgVpn authenticationOauthDefaultProfileName(String authenticationOauthDefaultProfileName) {
    
    this.authenticationOauthDefaultProfileName = authenticationOauthDefaultProfileName;
    return this;
  }

   /**
   * The name of the profile to use when the client does not supply a profile name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.25.
   * @return authenticationOauthDefaultProfileName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthDefaultProfileName() {
    return authenticationOauthDefaultProfileName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthDefaultProfileName(String authenticationOauthDefaultProfileName) {
    this.authenticationOauthDefaultProfileName = authenticationOauthDefaultProfileName;
  }


  public MsgVpn authenticationOauthDefaultProviderName(String authenticationOauthDefaultProviderName) {
    
    this.authenticationOauthDefaultProviderName = authenticationOauthDefaultProviderName;
    return this;
  }

   /**
   * The name of the provider to use when the client does not supply a provider name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthDefaultProviderName and authenticationOauthProviders replaced by authenticationOauthDefaultProfileName and authenticationOauthProfiles.
   * @return authenticationOauthDefaultProviderName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROVIDER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthDefaultProviderName() {
    return authenticationOauthDefaultProviderName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_DEFAULT_PROVIDER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthDefaultProviderName(String authenticationOauthDefaultProviderName) {
    this.authenticationOauthDefaultProviderName = authenticationOauthDefaultProviderName;
  }


  public MsgVpn authenticationOauthEnabled(Boolean authenticationOauthEnabled) {
    
    this.authenticationOauthEnabled = authenticationOauthEnabled;
    return this;
  }

   /**
   * Enable or disable OAuth authentication. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.13.
   * @return authenticationOauthEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthenticationOauthEnabled() {
    return authenticationOauthEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthEnabled(Boolean authenticationOauthEnabled) {
    this.authenticationOauthEnabled = authenticationOauthEnabled;
  }


  public MsgVpn authorizationLdapGroupMembershipAttributeName(String authorizationLdapGroupMembershipAttributeName) {
    
    this.authorizationLdapGroupMembershipAttributeName = authorizationLdapGroupMembershipAttributeName;
    return this;
  }

   /**
   * The name of the attribute that is retrieved from the LDAP server as part of the LDAP search when authorizing a client connecting to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;memberOf\&quot;&#x60;.
   * @return authorizationLdapGroupMembershipAttributeName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_LDAP_GROUP_MEMBERSHIP_ATTRIBUTE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthorizationLdapGroupMembershipAttributeName() {
    return authorizationLdapGroupMembershipAttributeName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_LDAP_GROUP_MEMBERSHIP_ATTRIBUTE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationLdapGroupMembershipAttributeName(String authorizationLdapGroupMembershipAttributeName) {
    this.authorizationLdapGroupMembershipAttributeName = authorizationLdapGroupMembershipAttributeName;
  }


  public MsgVpn authorizationLdapTrimClientUsernameDomainEnabled(Boolean authorizationLdapTrimClientUsernameDomainEnabled) {
    
    this.authorizationLdapTrimClientUsernameDomainEnabled = authorizationLdapTrimClientUsernameDomainEnabled;
    return this;
  }

   /**
   * Enable or disable client-username domain trimming for LDAP lookups of client connections. When enabled, the value of $CLIENT_USERNAME (when used for searching) will be truncated at the first occurance of the @ character. For example, if the client-username is in the form of an email address, then the domain portion will be removed. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.13.
   * @return authorizationLdapTrimClientUsernameDomainEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_LDAP_TRIM_CLIENT_USERNAME_DOMAIN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAuthorizationLdapTrimClientUsernameDomainEnabled() {
    return authorizationLdapTrimClientUsernameDomainEnabled;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_LDAP_TRIM_CLIENT_USERNAME_DOMAIN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationLdapTrimClientUsernameDomainEnabled(Boolean authorizationLdapTrimClientUsernameDomainEnabled) {
    this.authorizationLdapTrimClientUsernameDomainEnabled = authorizationLdapTrimClientUsernameDomainEnabled;
  }


  public MsgVpn authorizationProfileName(String authorizationProfileName) {
    
    this.authorizationProfileName = authorizationProfileName;
    return this;
  }

   /**
   * The name of the LDAP Profile to use for client authorization. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return authorizationProfileName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthorizationProfileName() {
    return authorizationProfileName;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationProfileName(String authorizationProfileName) {
    this.authorizationProfileName = authorizationProfileName;
  }


  public MsgVpn authorizationType(AuthorizationTypeEnum authorizationType) {
    
    this.authorizationType = authorizationType;
    return this;
  }

   /**
   * The type of authorization to use for clients connecting to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;internal\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;ldap\&quot; - LDAP authorization. \&quot;internal\&quot; - Internal authorization. &lt;/pre&gt; 
   * @return authorizationType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthorizationTypeEnum getAuthorizationType() {
    return authorizationType;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationType(AuthorizationTypeEnum authorizationType) {
    this.authorizationType = authorizationType;
  }


  public MsgVpn bridgingTlsServerCertEnforceTrustedCommonNameEnabled(Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled) {
    
    this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled = bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the Common Name (CN) in the server certificate from the remote broker. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the Bridge. Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
   * @return bridgingTlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getBridgingTlsServerCertEnforceTrustedCommonNameEnabled() {
    return bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgingTlsServerCertEnforceTrustedCommonNameEnabled(Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled = bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
  }


  public MsgVpn bridgingTlsServerCertMaxChainDepth(Long bridgingTlsServerCertMaxChainDepth) {
    
    this.bridgingTlsServerCertMaxChainDepth = bridgingTlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;.
   * @return bridgingTlsServerCertMaxChainDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getBridgingTlsServerCertMaxChainDepth() {
    return bridgingTlsServerCertMaxChainDepth;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgingTlsServerCertMaxChainDepth(Long bridgingTlsServerCertMaxChainDepth) {
    this.bridgingTlsServerCertMaxChainDepth = bridgingTlsServerCertMaxChainDepth;
  }


  public MsgVpn bridgingTlsServerCertValidateDateEnabled(Boolean bridgingTlsServerCertValidateDateEnabled) {
    
    this.bridgingTlsServerCertValidateDateEnabled = bridgingTlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid based on these dates. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return bridgingTlsServerCertValidateDateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getBridgingTlsServerCertValidateDateEnabled() {
    return bridgingTlsServerCertValidateDateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgingTlsServerCertValidateDateEnabled(Boolean bridgingTlsServerCertValidateDateEnabled) {
    this.bridgingTlsServerCertValidateDateEnabled = bridgingTlsServerCertValidateDateEnabled;
  }


  public MsgVpn bridgingTlsServerCertValidateNameEnabled(Boolean bridgingTlsServerCertValidateNameEnabled) {
    
    this.bridgingTlsServerCertValidateNameEnabled = bridgingTlsServerCertValidateNameEnabled;
    return this;
  }

   /**
   * Enable or disable the standard TLS authentication mechanism of verifying the name used to connect to the bridge. If enabled, the name used to connect to the bridge is checked against the names specified in the certificate returned by the remote router. Legacy Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is also enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.18.
   * @return bridgingTlsServerCertValidateNameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getBridgingTlsServerCertValidateNameEnabled() {
    return bridgingTlsServerCertValidateNameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGING_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgingTlsServerCertValidateNameEnabled(Boolean bridgingTlsServerCertValidateNameEnabled) {
    this.bridgingTlsServerCertValidateNameEnabled = bridgingTlsServerCertValidateNameEnabled;
  }


  public MsgVpn distributedCacheManagementEnabled(Boolean distributedCacheManagementEnabled) {
    
    this.distributedCacheManagementEnabled = distributedCacheManagementEnabled;
    return this;
  }

   /**
   * Enable or disable managing of cache instances over the message bus. The default value is &#x60;true&#x60;. Deprecated since 2.28. Distributed cache mangement is now redundancy aware and thus no longer requires administrative intervention for operational state.
   * @return distributedCacheManagementEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DISTRIBUTED_CACHE_MANAGEMENT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getDistributedCacheManagementEnabled() {
    return distributedCacheManagementEnabled;
  }


  @JsonProperty(JSON_PROPERTY_DISTRIBUTED_CACHE_MANAGEMENT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDistributedCacheManagementEnabled(Boolean distributedCacheManagementEnabled) {
    this.distributedCacheManagementEnabled = distributedCacheManagementEnabled;
  }


  public MsgVpn dmrEnabled(Boolean dmrEnabled) {
    
    this.dmrEnabled = dmrEnabled;
    return this;
  }

   /**
   * Enable or disable Dynamic Message Routing (DMR) for the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.11.
   * @return dmrEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DMR_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getDmrEnabled() {
    return dmrEnabled;
  }


  @JsonProperty(JSON_PROPERTY_DMR_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDmrEnabled(Boolean dmrEnabled) {
    this.dmrEnabled = dmrEnabled;
  }


  public MsgVpn enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
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


  public MsgVpn eventConnectionCountThreshold(EventThreshold eventConnectionCountThreshold) {
    
    this.eventConnectionCountThreshold = eventConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventConnectionCountThreshold
   * @return eventConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventConnectionCountThreshold() {
    return eventConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventConnectionCountThreshold(EventThreshold eventConnectionCountThreshold) {
    this.eventConnectionCountThreshold = eventConnectionCountThreshold;
  }


  public MsgVpn eventEgressFlowCountThreshold(EventThreshold eventEgressFlowCountThreshold) {
    
    this.eventEgressFlowCountThreshold = eventEgressFlowCountThreshold;
    return this;
  }

   /**
   * Get eventEgressFlowCountThreshold
   * @return eventEgressFlowCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_EGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventEgressFlowCountThreshold() {
    return eventEgressFlowCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_EGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventEgressFlowCountThreshold(EventThreshold eventEgressFlowCountThreshold) {
    this.eventEgressFlowCountThreshold = eventEgressFlowCountThreshold;
  }


  public MsgVpn eventEgressMsgRateThreshold(EventThresholdByValue eventEgressMsgRateThreshold) {
    
    this.eventEgressMsgRateThreshold = eventEgressMsgRateThreshold;
    return this;
  }

   /**
   * Get eventEgressMsgRateThreshold
   * @return eventEgressMsgRateThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_EGRESS_MSG_RATE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByValue getEventEgressMsgRateThreshold() {
    return eventEgressMsgRateThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_EGRESS_MSG_RATE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventEgressMsgRateThreshold(EventThresholdByValue eventEgressMsgRateThreshold) {
    this.eventEgressMsgRateThreshold = eventEgressMsgRateThreshold;
  }


  public MsgVpn eventEndpointCountThreshold(EventThreshold eventEndpointCountThreshold) {
    
    this.eventEndpointCountThreshold = eventEndpointCountThreshold;
    return this;
  }

   /**
   * Get eventEndpointCountThreshold
   * @return eventEndpointCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_ENDPOINT_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventEndpointCountThreshold() {
    return eventEndpointCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_ENDPOINT_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventEndpointCountThreshold(EventThreshold eventEndpointCountThreshold) {
    this.eventEndpointCountThreshold = eventEndpointCountThreshold;
  }


  public MsgVpn eventIngressFlowCountThreshold(EventThreshold eventIngressFlowCountThreshold) {
    
    this.eventIngressFlowCountThreshold = eventIngressFlowCountThreshold;
    return this;
  }

   /**
   * Get eventIngressFlowCountThreshold
   * @return eventIngressFlowCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_INGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventIngressFlowCountThreshold() {
    return eventIngressFlowCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_INGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventIngressFlowCountThreshold(EventThreshold eventIngressFlowCountThreshold) {
    this.eventIngressFlowCountThreshold = eventIngressFlowCountThreshold;
  }


  public MsgVpn eventIngressMsgRateThreshold(EventThresholdByValue eventIngressMsgRateThreshold) {
    
    this.eventIngressMsgRateThreshold = eventIngressMsgRateThreshold;
    return this;
  }

   /**
   * Get eventIngressMsgRateThreshold
   * @return eventIngressMsgRateThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_INGRESS_MSG_RATE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByValue getEventIngressMsgRateThreshold() {
    return eventIngressMsgRateThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_INGRESS_MSG_RATE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventIngressMsgRateThreshold(EventThresholdByValue eventIngressMsgRateThreshold) {
    this.eventIngressMsgRateThreshold = eventIngressMsgRateThreshold;
  }


  public MsgVpn eventLargeMsgThreshold(Long eventLargeMsgThreshold) {
    
    this.eventLargeMsgThreshold = eventLargeMsgThreshold;
    return this;
  }

   /**
   * The threshold, in kilobytes, after which a message is considered to be large for the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1024&#x60;.
   * @return eventLargeMsgThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_LARGE_MSG_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getEventLargeMsgThreshold() {
    return eventLargeMsgThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_LARGE_MSG_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventLargeMsgThreshold(Long eventLargeMsgThreshold) {
    this.eventLargeMsgThreshold = eventLargeMsgThreshold;
  }


  public MsgVpn eventLogTag(String eventLogTag) {
    
    this.eventLogTag = eventLogTag;
    return this;
  }

   /**
   * A prefix applied to all published Events in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return eventLogTag
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_LOG_TAG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getEventLogTag() {
    return eventLogTag;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_LOG_TAG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventLogTag(String eventLogTag) {
    this.eventLogTag = eventLogTag;
  }


  public MsgVpn eventMsgSpoolUsageThreshold(EventThreshold eventMsgSpoolUsageThreshold) {
    
    this.eventMsgSpoolUsageThreshold = eventMsgSpoolUsageThreshold;
    return this;
  }

   /**
   * Get eventMsgSpoolUsageThreshold
   * @return eventMsgSpoolUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_MSG_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventMsgSpoolUsageThreshold() {
    return eventMsgSpoolUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_MSG_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventMsgSpoolUsageThreshold(EventThreshold eventMsgSpoolUsageThreshold) {
    this.eventMsgSpoolUsageThreshold = eventMsgSpoolUsageThreshold;
  }


  public MsgVpn eventPublishClientEnabled(Boolean eventPublishClientEnabled) {
    
    this.eventPublishClientEnabled = eventPublishClientEnabled;
    return this;
  }

   /**
   * Enable or disable Client level Event message publishing. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return eventPublishClientEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_CLIENT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEventPublishClientEnabled() {
    return eventPublishClientEnabled;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_CLIENT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventPublishClientEnabled(Boolean eventPublishClientEnabled) {
    this.eventPublishClientEnabled = eventPublishClientEnabled;
  }


  public MsgVpn eventPublishMsgVpnEnabled(Boolean eventPublishMsgVpnEnabled) {
    
    this.eventPublishMsgVpnEnabled = eventPublishMsgVpnEnabled;
    return this;
  }

   /**
   * Enable or disable Message VPN level Event message publishing. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return eventPublishMsgVpnEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_MSG_VPN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEventPublishMsgVpnEnabled() {
    return eventPublishMsgVpnEnabled;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_MSG_VPN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventPublishMsgVpnEnabled(Boolean eventPublishMsgVpnEnabled) {
    this.eventPublishMsgVpnEnabled = eventPublishMsgVpnEnabled;
  }


  public MsgVpn eventPublishSubscriptionMode(EventPublishSubscriptionModeEnum eventPublishSubscriptionMode) {
    
    this.eventPublishSubscriptionMode = eventPublishSubscriptionMode;
    return this;
  }

   /**
   * Subscription level Event message publishing mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;off\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;off\&quot; - Disable client level event message publishing. \&quot;on-with-format-v1\&quot; - Enable client level event message publishing with format v1. \&quot;on-with-no-unsubscribe-events-on-disconnect-format-v1\&quot; - As \&quot;on-with-format-v1\&quot;, but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. \&quot;on-with-format-v2\&quot; - Enable client level event message publishing with format v2. \&quot;on-with-no-unsubscribe-events-on-disconnect-format-v2\&quot; - As \&quot;on-with-format-v2\&quot;, but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. &lt;/pre&gt; 
   * @return eventPublishSubscriptionMode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_SUBSCRIPTION_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventPublishSubscriptionModeEnum getEventPublishSubscriptionMode() {
    return eventPublishSubscriptionMode;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_SUBSCRIPTION_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventPublishSubscriptionMode(EventPublishSubscriptionModeEnum eventPublishSubscriptionMode) {
    this.eventPublishSubscriptionMode = eventPublishSubscriptionMode;
  }


  public MsgVpn eventPublishTopicFormatMqttEnabled(Boolean eventPublishTopicFormatMqttEnabled) {
    
    this.eventPublishTopicFormatMqttEnabled = eventPublishTopicFormatMqttEnabled;
    return this;
  }

   /**
   * Enable or disable Event publish topics in MQTT format. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return eventPublishTopicFormatMqttEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_MQTT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEventPublishTopicFormatMqttEnabled() {
    return eventPublishTopicFormatMqttEnabled;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_MQTT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventPublishTopicFormatMqttEnabled(Boolean eventPublishTopicFormatMqttEnabled) {
    this.eventPublishTopicFormatMqttEnabled = eventPublishTopicFormatMqttEnabled;
  }


  public MsgVpn eventPublishTopicFormatSmfEnabled(Boolean eventPublishTopicFormatSmfEnabled) {
    
    this.eventPublishTopicFormatSmfEnabled = eventPublishTopicFormatSmfEnabled;
    return this;
  }

   /**
   * Enable or disable Event publish topics in SMF format. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return eventPublishTopicFormatSmfEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_SMF_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEventPublishTopicFormatSmfEnabled() {
    return eventPublishTopicFormatSmfEnabled;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_PUBLISH_TOPIC_FORMAT_SMF_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventPublishTopicFormatSmfEnabled(Boolean eventPublishTopicFormatSmfEnabled) {
    this.eventPublishTopicFormatSmfEnabled = eventPublishTopicFormatSmfEnabled;
  }


  public MsgVpn eventServiceAmqpConnectionCountThreshold(EventThreshold eventServiceAmqpConnectionCountThreshold) {
    
    this.eventServiceAmqpConnectionCountThreshold = eventServiceAmqpConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceAmqpConnectionCountThreshold
   * @return eventServiceAmqpConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_AMQP_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceAmqpConnectionCountThreshold() {
    return eventServiceAmqpConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_AMQP_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceAmqpConnectionCountThreshold(EventThreshold eventServiceAmqpConnectionCountThreshold) {
    this.eventServiceAmqpConnectionCountThreshold = eventServiceAmqpConnectionCountThreshold;
  }


  public MsgVpn eventServiceMqttConnectionCountThreshold(EventThreshold eventServiceMqttConnectionCountThreshold) {
    
    this.eventServiceMqttConnectionCountThreshold = eventServiceMqttConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceMqttConnectionCountThreshold
   * @return eventServiceMqttConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_MQTT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceMqttConnectionCountThreshold() {
    return eventServiceMqttConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_MQTT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceMqttConnectionCountThreshold(EventThreshold eventServiceMqttConnectionCountThreshold) {
    this.eventServiceMqttConnectionCountThreshold = eventServiceMqttConnectionCountThreshold;
  }


  public MsgVpn eventServiceRestIncomingConnectionCountThreshold(EventThreshold eventServiceRestIncomingConnectionCountThreshold) {
    
    this.eventServiceRestIncomingConnectionCountThreshold = eventServiceRestIncomingConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceRestIncomingConnectionCountThreshold
   * @return eventServiceRestIncomingConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_REST_INCOMING_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceRestIncomingConnectionCountThreshold() {
    return eventServiceRestIncomingConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_REST_INCOMING_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceRestIncomingConnectionCountThreshold(EventThreshold eventServiceRestIncomingConnectionCountThreshold) {
    this.eventServiceRestIncomingConnectionCountThreshold = eventServiceRestIncomingConnectionCountThreshold;
  }


  public MsgVpn eventServiceSmfConnectionCountThreshold(EventThreshold eventServiceSmfConnectionCountThreshold) {
    
    this.eventServiceSmfConnectionCountThreshold = eventServiceSmfConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceSmfConnectionCountThreshold
   * @return eventServiceSmfConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceSmfConnectionCountThreshold() {
    return eventServiceSmfConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceSmfConnectionCountThreshold(EventThreshold eventServiceSmfConnectionCountThreshold) {
    this.eventServiceSmfConnectionCountThreshold = eventServiceSmfConnectionCountThreshold;
  }


  public MsgVpn eventServiceWebConnectionCountThreshold(EventThreshold eventServiceWebConnectionCountThreshold) {
    
    this.eventServiceWebConnectionCountThreshold = eventServiceWebConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceWebConnectionCountThreshold
   * @return eventServiceWebConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceWebConnectionCountThreshold() {
    return eventServiceWebConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceWebConnectionCountThreshold(EventThreshold eventServiceWebConnectionCountThreshold) {
    this.eventServiceWebConnectionCountThreshold = eventServiceWebConnectionCountThreshold;
  }


  public MsgVpn eventSubscriptionCountThreshold(EventThreshold eventSubscriptionCountThreshold) {
    
    this.eventSubscriptionCountThreshold = eventSubscriptionCountThreshold;
    return this;
  }

   /**
   * Get eventSubscriptionCountThreshold
   * @return eventSubscriptionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SUBSCRIPTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventSubscriptionCountThreshold() {
    return eventSubscriptionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SUBSCRIPTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventSubscriptionCountThreshold(EventThreshold eventSubscriptionCountThreshold) {
    this.eventSubscriptionCountThreshold = eventSubscriptionCountThreshold;
  }


  public MsgVpn eventTransactedSessionCountThreshold(EventThreshold eventTransactedSessionCountThreshold) {
    
    this.eventTransactedSessionCountThreshold = eventTransactedSessionCountThreshold;
    return this;
  }

   /**
   * Get eventTransactedSessionCountThreshold
   * @return eventTransactedSessionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventTransactedSessionCountThreshold() {
    return eventTransactedSessionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventTransactedSessionCountThreshold(EventThreshold eventTransactedSessionCountThreshold) {
    this.eventTransactedSessionCountThreshold = eventTransactedSessionCountThreshold;
  }


  public MsgVpn eventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
    
    this.eventTransactionCountThreshold = eventTransactionCountThreshold;
    return this;
  }

   /**
   * Get eventTransactionCountThreshold
   * @return eventTransactionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_TRANSACTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventTransactionCountThreshold() {
    return eventTransactionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_TRANSACTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
    this.eventTransactionCountThreshold = eventTransactionCountThreshold;
  }


  public MsgVpn exportSubscriptionsEnabled(Boolean exportSubscriptionsEnabled) {
    
    this.exportSubscriptionsEnabled = exportSubscriptionsEnabled;
    return this;
  }

   /**
   * Enable or disable the export of subscriptions in the Message VPN to other routers in the network over Neighbor links. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return exportSubscriptionsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EXPORT_SUBSCRIPTIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getExportSubscriptionsEnabled() {
    return exportSubscriptionsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_EXPORT_SUBSCRIPTIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setExportSubscriptionsEnabled(Boolean exportSubscriptionsEnabled) {
    this.exportSubscriptionsEnabled = exportSubscriptionsEnabled;
  }


  public MsgVpn jndiEnabled(Boolean jndiEnabled) {
    
    this.jndiEnabled = jndiEnabled;
    return this;
  }

   /**
   * Enable or disable JNDI access for clients in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.2.
   * @return jndiEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_JNDI_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getJndiEnabled() {
    return jndiEnabled;
  }


  @JsonProperty(JSON_PROPERTY_JNDI_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJndiEnabled(Boolean jndiEnabled) {
    this.jndiEnabled = jndiEnabled;
  }


  public MsgVpn maxConnectionCount(Long maxConnectionCount) {
    
    this.maxConnectionCount = maxConnectionCount;
    return this;
  }

   /**
   * The maximum number of client connections to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform.
   * @return maxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxConnectionCount() {
    return maxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxConnectionCount(Long maxConnectionCount) {
    this.maxConnectionCount = maxConnectionCount;
  }


  public MsgVpn maxEgressFlowCount(Long maxEgressFlowCount) {
    
    this.maxEgressFlowCount = maxEgressFlowCount;
    return this;
  }

   /**
   * The maximum number of transmit flows that can be created in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;.
   * @return maxEgressFlowCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_EGRESS_FLOW_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxEgressFlowCount() {
    return maxEgressFlowCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_EGRESS_FLOW_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxEgressFlowCount(Long maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
  }


  public MsgVpn maxEndpointCount(Long maxEndpointCount) {
    
    this.maxEndpointCount = maxEndpointCount;
    return this;
  }

   /**
   * The maximum number of Queues and Topic Endpoints that can be created in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;.
   * @return maxEndpointCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_ENDPOINT_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxEndpointCount() {
    return maxEndpointCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_ENDPOINT_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxEndpointCount(Long maxEndpointCount) {
    this.maxEndpointCount = maxEndpointCount;
  }


  public MsgVpn maxIngressFlowCount(Long maxIngressFlowCount) {
    
    this.maxIngressFlowCount = maxIngressFlowCount;
    return this;
  }

   /**
   * The maximum number of receive flows that can be created in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;.
   * @return maxIngressFlowCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_INGRESS_FLOW_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxIngressFlowCount() {
    return maxIngressFlowCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_INGRESS_FLOW_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxIngressFlowCount(Long maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
  }


  public MsgVpn maxMsgSpoolUsage(Long maxMsgSpoolUsage) {
    
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage by the Message VPN, in megabytes. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return maxMsgSpoolUsage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxMsgSpoolUsage() {
    return maxMsgSpoolUsage;
  }


  @JsonProperty(JSON_PROPERTY_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxMsgSpoolUsage(Long maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
  }


  public MsgVpn maxSubscriptionCount(Long maxSubscriptionCount) {
    
    this.maxSubscriptionCount = maxSubscriptionCount;
    return this;
  }

   /**
   * The maximum number of local client subscriptions that can be added to the Message VPN. This limit is not enforced when a subscription is added using a management interface, such as CLI or SEMP. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
   * @return maxSubscriptionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_SUBSCRIPTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxSubscriptionCount() {
    return maxSubscriptionCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_SUBSCRIPTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxSubscriptionCount(Long maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
  }


  public MsgVpn maxTransactedSessionCount(Long maxTransactedSessionCount) {
    
    this.maxTransactedSessionCount = maxTransactedSessionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions that can be created in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
   * @return maxTransactedSessionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_TRANSACTED_SESSION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxTransactedSessionCount() {
    return maxTransactedSessionCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_TRANSACTED_SESSION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxTransactedSessionCount(Long maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
  }


  public MsgVpn maxTransactionCount(Long maxTransactionCount) {
    
    this.maxTransactionCount = maxTransactionCount;
    return this;
  }

   /**
   * The maximum number of transactions that can be created in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
   * @return maxTransactionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_TRANSACTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxTransactionCount() {
    return maxTransactionCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_TRANSACTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxTransactionCount(Long maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
  }


  public MsgVpn mqttRetainMaxMemory(Integer mqttRetainMaxMemory) {
    
    this.mqttRetainMaxMemory = mqttRetainMaxMemory;
    return this;
  }

   /**
   * The maximum total memory usage of the MQTT Retain feature for this Message VPN, in MB. If the maximum memory is reached, any arriving retain messages that require more memory are discarded. A value of -1 indicates that the memory is bounded only by the global max memory limit. A value of 0 prevents MQTT Retain from becoming operational. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;-1&#x60;. Available since 2.11.
   * @return mqttRetainMaxMemory
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MQTT_RETAIN_MAX_MEMORY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getMqttRetainMaxMemory() {
    return mqttRetainMaxMemory;
  }


  @JsonProperty(JSON_PROPERTY_MQTT_RETAIN_MAX_MEMORY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMqttRetainMaxMemory(Integer mqttRetainMaxMemory) {
    this.mqttRetainMaxMemory = mqttRetainMaxMemory;
  }


  public MsgVpn msgVpnName(String msgVpnName) {
    
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


  public MsgVpn replicationAckPropagationIntervalMsgCount(Long replicationAckPropagationIntervalMsgCount) {
    
    this.replicationAckPropagationIntervalMsgCount = replicationAckPropagationIntervalMsgCount;
    return this;
  }

   /**
   * The acknowledgement (ACK) propagation interval for the replication Bridge, in number of replicated messages. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;20&#x60;.
   * @return replicationAckPropagationIntervalMsgCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_ACK_PROPAGATION_INTERVAL_MSG_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getReplicationAckPropagationIntervalMsgCount() {
    return replicationAckPropagationIntervalMsgCount;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_ACK_PROPAGATION_INTERVAL_MSG_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationAckPropagationIntervalMsgCount(Long replicationAckPropagationIntervalMsgCount) {
    this.replicationAckPropagationIntervalMsgCount = replicationAckPropagationIntervalMsgCount;
  }


  public MsgVpn replicationBridgeAuthenticationBasicClientUsername(String replicationBridgeAuthenticationBasicClientUsername) {
    
    this.replicationBridgeAuthenticationBasicClientUsername = replicationBridgeAuthenticationBasicClientUsername;
    return this;
  }

   /**
   * The Client Username the replication Bridge uses to login to the remote Message VPN. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return replicationBridgeAuthenticationBasicClientUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplicationBridgeAuthenticationBasicClientUsername() {
    return replicationBridgeAuthenticationBasicClientUsername;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeAuthenticationBasicClientUsername(String replicationBridgeAuthenticationBasicClientUsername) {
    this.replicationBridgeAuthenticationBasicClientUsername = replicationBridgeAuthenticationBasicClientUsername;
  }


  public MsgVpn replicationBridgeAuthenticationBasicPassword(String replicationBridgeAuthenticationBasicPassword) {
    
    this.replicationBridgeAuthenticationBasicPassword = replicationBridgeAuthenticationBasicPassword;
    return this;
  }

   /**
   * The password for the Client Username. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return replicationBridgeAuthenticationBasicPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplicationBridgeAuthenticationBasicPassword() {
    return replicationBridgeAuthenticationBasicPassword;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_BASIC_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeAuthenticationBasicPassword(String replicationBridgeAuthenticationBasicPassword) {
    this.replicationBridgeAuthenticationBasicPassword = replicationBridgeAuthenticationBasicPassword;
  }


  public MsgVpn replicationBridgeAuthenticationClientCertContent(String replicationBridgeAuthenticationClientCertContent) {
    
    this.replicationBridgeAuthenticationClientCertContent = replicationBridgeAuthenticationClientCertContent;
    return this;
  }

   /**
   * The PEM formatted content for the client certificate used by this bridge to login to the Remote Message VPN. It must consist of a private key and between one and three certificates comprising the certificate trust chain. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9.
   * @return replicationBridgeAuthenticationClientCertContent
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplicationBridgeAuthenticationClientCertContent() {
    return replicationBridgeAuthenticationClientCertContent;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeAuthenticationClientCertContent(String replicationBridgeAuthenticationClientCertContent) {
    this.replicationBridgeAuthenticationClientCertContent = replicationBridgeAuthenticationClientCertContent;
  }


  public MsgVpn replicationBridgeAuthenticationClientCertPassword(String replicationBridgeAuthenticationClientCertPassword) {
    
    this.replicationBridgeAuthenticationClientCertPassword = replicationBridgeAuthenticationClientCertPassword;
    return this;
  }

   /**
   * The password for the client certificate. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9.
   * @return replicationBridgeAuthenticationClientCertPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplicationBridgeAuthenticationClientCertPassword() {
    return replicationBridgeAuthenticationClientCertPassword;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_CLIENT_CERT_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeAuthenticationClientCertPassword(String replicationBridgeAuthenticationClientCertPassword) {
    this.replicationBridgeAuthenticationClientCertPassword = replicationBridgeAuthenticationClientCertPassword;
  }


  public MsgVpn replicationBridgeAuthenticationScheme(ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme) {
    
    this.replicationBridgeAuthenticationScheme = replicationBridgeAuthenticationScheme;
    return this;
  }

   /**
   * The authentication scheme for the replication Bridge in the Message VPN. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt; 
   * @return replicationBridgeAuthenticationScheme
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_SCHEME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ReplicationBridgeAuthenticationSchemeEnum getReplicationBridgeAuthenticationScheme() {
    return replicationBridgeAuthenticationScheme;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_AUTHENTICATION_SCHEME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeAuthenticationScheme(ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme) {
    this.replicationBridgeAuthenticationScheme = replicationBridgeAuthenticationScheme;
  }


  public MsgVpn replicationBridgeCompressedDataEnabled(Boolean replicationBridgeCompressedDataEnabled) {
    
    this.replicationBridgeCompressedDataEnabled = replicationBridgeCompressedDataEnabled;
    return this;
  }

   /**
   * Enable or disable use of compression for the replication Bridge. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
   * @return replicationBridgeCompressedDataEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_COMPRESSED_DATA_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getReplicationBridgeCompressedDataEnabled() {
    return replicationBridgeCompressedDataEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_COMPRESSED_DATA_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeCompressedDataEnabled(Boolean replicationBridgeCompressedDataEnabled) {
    this.replicationBridgeCompressedDataEnabled = replicationBridgeCompressedDataEnabled;
  }


  public MsgVpn replicationBridgeEgressFlowWindowSize(Long replicationBridgeEgressFlowWindowSize) {
    
    this.replicationBridgeEgressFlowWindowSize = replicationBridgeEgressFlowWindowSize;
    return this;
  }

   /**
   * The size of the window used for guaranteed messages published to the replication Bridge, in messages. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;255&#x60;.
   * @return replicationBridgeEgressFlowWindowSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_EGRESS_FLOW_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getReplicationBridgeEgressFlowWindowSize() {
    return replicationBridgeEgressFlowWindowSize;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_EGRESS_FLOW_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeEgressFlowWindowSize(Long replicationBridgeEgressFlowWindowSize) {
    this.replicationBridgeEgressFlowWindowSize = replicationBridgeEgressFlowWindowSize;
  }


  public MsgVpn replicationBridgeRetryDelay(Long replicationBridgeRetryDelay) {
    
    this.replicationBridgeRetryDelay = replicationBridgeRetryDelay;
    return this;
  }

   /**
   * The number of seconds that must pass before retrying the replication Bridge connection. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;3&#x60;.
   * @return replicationBridgeRetryDelay
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_RETRY_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getReplicationBridgeRetryDelay() {
    return replicationBridgeRetryDelay;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_RETRY_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeRetryDelay(Long replicationBridgeRetryDelay) {
    this.replicationBridgeRetryDelay = replicationBridgeRetryDelay;
  }


  public MsgVpn replicationBridgeTlsEnabled(Boolean replicationBridgeTlsEnabled) {
    
    this.replicationBridgeTlsEnabled = replicationBridgeTlsEnabled;
    return this;
  }

   /**
   * Enable or disable use of encryption (TLS) for the replication Bridge connection. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
   * @return replicationBridgeTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getReplicationBridgeTlsEnabled() {
    return replicationBridgeTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeTlsEnabled(Boolean replicationBridgeTlsEnabled) {
    this.replicationBridgeTlsEnabled = replicationBridgeTlsEnabled;
  }


  public MsgVpn replicationBridgeUnidirectionalClientProfileName(String replicationBridgeUnidirectionalClientProfileName) {
    
    this.replicationBridgeUnidirectionalClientProfileName = replicationBridgeUnidirectionalClientProfileName;
    return this;
  }

   /**
   * The Client Profile for the unidirectional replication Bridge in the Message VPN. It is used only for the TCP parameters. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;#client-profile\&quot;&#x60;.
   * @return replicationBridgeUnidirectionalClientProfileName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_UNIDIRECTIONAL_CLIENT_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplicationBridgeUnidirectionalClientProfileName() {
    return replicationBridgeUnidirectionalClientProfileName;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_BRIDGE_UNIDIRECTIONAL_CLIENT_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationBridgeUnidirectionalClientProfileName(String replicationBridgeUnidirectionalClientProfileName) {
    this.replicationBridgeUnidirectionalClientProfileName = replicationBridgeUnidirectionalClientProfileName;
  }


  public MsgVpn replicationEnabled(Boolean replicationEnabled) {
    
    this.replicationEnabled = replicationEnabled;
    return this;
  }

   /**
   * Enable or disable replication for the Message VPN. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
   * @return replicationEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getReplicationEnabled() {
    return replicationEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationEnabled(Boolean replicationEnabled) {
    this.replicationEnabled = replicationEnabled;
  }


  public MsgVpn replicationEnabledQueueBehavior(ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior) {
    
    this.replicationEnabledQueueBehavior = replicationEnabledQueueBehavior;
    return this;
  }

   /**
   * The behavior to take when enabling replication for the Message VPN, depending on the existence of the replication Queue. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;fail-on-existing-queue\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;fail-on-existing-queue\&quot; - The data replication queue must not already exist. \&quot;force-use-existing-queue\&quot; - The data replication queue must already exist. Any data messages on the Queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail. \&quot;force-recreate-queue\&quot; - The data replication queue must already exist. Any data messages on the Queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. &lt;/pre&gt; 
   * @return replicationEnabledQueueBehavior
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_ENABLED_QUEUE_BEHAVIOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ReplicationEnabledQueueBehaviorEnum getReplicationEnabledQueueBehavior() {
    return replicationEnabledQueueBehavior;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_ENABLED_QUEUE_BEHAVIOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationEnabledQueueBehavior(ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior) {
    this.replicationEnabledQueueBehavior = replicationEnabledQueueBehavior;
  }


  public MsgVpn replicationQueueMaxMsgSpoolUsage(Long replicationQueueMaxMsgSpoolUsage) {
    
    this.replicationQueueMaxMsgSpoolUsage = replicationQueueMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage by the replication Bridge local Queue (quota), in megabytes. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;60000&#x60;.
   * @return replicationQueueMaxMsgSpoolUsage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_QUEUE_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getReplicationQueueMaxMsgSpoolUsage() {
    return replicationQueueMaxMsgSpoolUsage;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_QUEUE_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationQueueMaxMsgSpoolUsage(Long replicationQueueMaxMsgSpoolUsage) {
    this.replicationQueueMaxMsgSpoolUsage = replicationQueueMaxMsgSpoolUsage;
  }


  public MsgVpn replicationQueueRejectMsgToSenderOnDiscardEnabled(Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled) {
    
    this.replicationQueueRejectMsgToSenderOnDiscardEnabled = replicationQueueRejectMsgToSenderOnDiscardEnabled;
    return this;
  }

   /**
   * Enable or disable whether messages discarded on the replication Bridge local Queue are rejected back to the sender. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return replicationQueueRejectMsgToSenderOnDiscardEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getReplicationQueueRejectMsgToSenderOnDiscardEnabled() {
    return replicationQueueRejectMsgToSenderOnDiscardEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationQueueRejectMsgToSenderOnDiscardEnabled(Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled) {
    this.replicationQueueRejectMsgToSenderOnDiscardEnabled = replicationQueueRejectMsgToSenderOnDiscardEnabled;
  }


  public MsgVpn replicationRejectMsgWhenSyncIneligibleEnabled(Boolean replicationRejectMsgWhenSyncIneligibleEnabled) {
    
    this.replicationRejectMsgWhenSyncIneligibleEnabled = replicationRejectMsgWhenSyncIneligibleEnabled;
    return this;
  }

   /**
   * Enable or disable whether guaranteed messages published to synchronously replicated Topics are rejected back to the sender when synchronous replication becomes ineligible. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return replicationRejectMsgWhenSyncIneligibleEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_REJECT_MSG_WHEN_SYNC_INELIGIBLE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getReplicationRejectMsgWhenSyncIneligibleEnabled() {
    return replicationRejectMsgWhenSyncIneligibleEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_REJECT_MSG_WHEN_SYNC_INELIGIBLE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationRejectMsgWhenSyncIneligibleEnabled(Boolean replicationRejectMsgWhenSyncIneligibleEnabled) {
    this.replicationRejectMsgWhenSyncIneligibleEnabled = replicationRejectMsgWhenSyncIneligibleEnabled;
  }


  public MsgVpn replicationRole(ReplicationRoleEnum replicationRole) {
    
    this.replicationRole = replicationRole;
    return this;
  }

   /**
   * The replication role for the Message VPN. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;standby\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;active\&quot; - Assume the Active role in replication for the Message VPN. \&quot;standby\&quot; - Assume the Standby role in replication for the Message VPN. &lt;/pre&gt; 
   * @return replicationRole
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_ROLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ReplicationRoleEnum getReplicationRole() {
    return replicationRole;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_ROLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationRole(ReplicationRoleEnum replicationRole) {
    this.replicationRole = replicationRole;
  }


  public MsgVpn replicationTransactionMode(ReplicationTransactionModeEnum replicationTransactionMode) {
    
    this.replicationTransactionMode = replicationTransactionMode;
    return this;
  }

   /**
   * The transaction replication mode for all transactions within the Message VPN. Changing this value during operation will not affect existing transactions; it is only used upon starting a transaction. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;async\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;sync\&quot; - Messages are acknowledged when replicated (spooled remotely). \&quot;async\&quot; - Messages are acknowledged when pending replication (spooled locally). &lt;/pre&gt; 
   * @return replicationTransactionMode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_TRANSACTION_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ReplicationTransactionModeEnum getReplicationTransactionMode() {
    return replicationTransactionMode;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_TRANSACTION_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationTransactionMode(ReplicationTransactionModeEnum replicationTransactionMode) {
    this.replicationTransactionMode = replicationTransactionMode;
  }


  public MsgVpn restTlsServerCertEnforceTrustedCommonNameEnabled(Boolean restTlsServerCertEnforceTrustedCommonNameEnabled) {
    
    this.restTlsServerCertEnforceTrustedCommonNameEnabled = restTlsServerCertEnforceTrustedCommonNameEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the Common Name (CN) in the server certificate from the remote REST Consumer. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the REST Consumer. Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.17. Common Name validation has been replaced by Server Certificate Name validation.
   * @return restTlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRestTlsServerCertEnforceTrustedCommonNameEnabled() {
    return restTlsServerCertEnforceTrustedCommonNameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestTlsServerCertEnforceTrustedCommonNameEnabled(Boolean restTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.restTlsServerCertEnforceTrustedCommonNameEnabled = restTlsServerCertEnforceTrustedCommonNameEnabled;
  }


  public MsgVpn restTlsServerCertMaxChainDepth(Long restTlsServerCertMaxChainDepth) {
    
    this.restTlsServerCertMaxChainDepth = restTlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a REST Consumer server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;.
   * @return restTlsServerCertMaxChainDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getRestTlsServerCertMaxChainDepth() {
    return restTlsServerCertMaxChainDepth;
  }


  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestTlsServerCertMaxChainDepth(Long restTlsServerCertMaxChainDepth) {
    this.restTlsServerCertMaxChainDepth = restTlsServerCertMaxChainDepth;
  }


  public MsgVpn restTlsServerCertValidateDateEnabled(Boolean restTlsServerCertValidateDateEnabled) {
    
    this.restTlsServerCertValidateDateEnabled = restTlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the REST Consumer server certificate. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return restTlsServerCertValidateDateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRestTlsServerCertValidateDateEnabled() {
    return restTlsServerCertValidateDateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestTlsServerCertValidateDateEnabled(Boolean restTlsServerCertValidateDateEnabled) {
    this.restTlsServerCertValidateDateEnabled = restTlsServerCertValidateDateEnabled;
  }


  public MsgVpn restTlsServerCertValidateNameEnabled(Boolean restTlsServerCertValidateNameEnabled) {
    
    this.restTlsServerCertValidateNameEnabled = restTlsServerCertValidateNameEnabled;
    return this;
  }

   /**
   * Enable or disable the standard TLS authentication mechanism of verifying the name used to connect to the remote REST Consumer. If enabled, the name used to connect to the remote REST Consumer is checked against the names specified in the certificate returned by the remote router. Legacy Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is also enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return restTlsServerCertValidateNameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRestTlsServerCertValidateNameEnabled() {
    return restTlsServerCertValidateNameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REST_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestTlsServerCertValidateNameEnabled(Boolean restTlsServerCertValidateNameEnabled) {
    this.restTlsServerCertValidateNameEnabled = restTlsServerCertValidateNameEnabled;
  }


  public MsgVpn sempOverMsgBusAdminClientEnabled(Boolean sempOverMsgBusAdminClientEnabled) {
    
    this.sempOverMsgBusAdminClientEnabled = sempOverMsgBusAdminClientEnabled;
    return this;
  }

   /**
   * Enable or disable \&quot;admin client\&quot; SEMP over the message bus commands for the current Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return sempOverMsgBusAdminClientEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_CLIENT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSempOverMsgBusAdminClientEnabled() {
    return sempOverMsgBusAdminClientEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_CLIENT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSempOverMsgBusAdminClientEnabled(Boolean sempOverMsgBusAdminClientEnabled) {
    this.sempOverMsgBusAdminClientEnabled = sempOverMsgBusAdminClientEnabled;
  }


  public MsgVpn sempOverMsgBusAdminDistributedCacheEnabled(Boolean sempOverMsgBusAdminDistributedCacheEnabled) {
    
    this.sempOverMsgBusAdminDistributedCacheEnabled = sempOverMsgBusAdminDistributedCacheEnabled;
    return this;
  }

   /**
   * Enable or disable \&quot;admin distributed-cache\&quot; SEMP over the message bus commands for the current Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return sempOverMsgBusAdminDistributedCacheEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_DISTRIBUTED_CACHE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSempOverMsgBusAdminDistributedCacheEnabled() {
    return sempOverMsgBusAdminDistributedCacheEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_DISTRIBUTED_CACHE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSempOverMsgBusAdminDistributedCacheEnabled(Boolean sempOverMsgBusAdminDistributedCacheEnabled) {
    this.sempOverMsgBusAdminDistributedCacheEnabled = sempOverMsgBusAdminDistributedCacheEnabled;
  }


  public MsgVpn sempOverMsgBusAdminEnabled(Boolean sempOverMsgBusAdminEnabled) {
    
    this.sempOverMsgBusAdminEnabled = sempOverMsgBusAdminEnabled;
    return this;
  }

   /**
   * Enable or disable \&quot;admin\&quot; SEMP over the message bus commands for the current Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return sempOverMsgBusAdminEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSempOverMsgBusAdminEnabled() {
    return sempOverMsgBusAdminEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ADMIN_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSempOverMsgBusAdminEnabled(Boolean sempOverMsgBusAdminEnabled) {
    this.sempOverMsgBusAdminEnabled = sempOverMsgBusAdminEnabled;
  }


  public MsgVpn sempOverMsgBusEnabled(Boolean sempOverMsgBusEnabled) {
    
    this.sempOverMsgBusEnabled = sempOverMsgBusEnabled;
    return this;
  }

   /**
   * Enable or disable SEMP over the message bus for the current Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return sempOverMsgBusEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSempOverMsgBusEnabled() {
    return sempOverMsgBusEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSempOverMsgBusEnabled(Boolean sempOverMsgBusEnabled) {
    this.sempOverMsgBusEnabled = sempOverMsgBusEnabled;
  }


  public MsgVpn sempOverMsgBusShowEnabled(Boolean sempOverMsgBusShowEnabled) {
    
    this.sempOverMsgBusShowEnabled = sempOverMsgBusShowEnabled;
    return this;
  }

   /**
   * Enable or disable \&quot;show\&quot; SEMP over the message bus commands for the current Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return sempOverMsgBusShowEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_SHOW_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getSempOverMsgBusShowEnabled() {
    return sempOverMsgBusShowEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SEMP_OVER_MSG_BUS_SHOW_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSempOverMsgBusShowEnabled(Boolean sempOverMsgBusShowEnabled) {
    this.sempOverMsgBusShowEnabled = sempOverMsgBusShowEnabled;
  }


  public MsgVpn serviceAmqpMaxConnectionCount(Long serviceAmqpMaxConnectionCount) {
    
    this.serviceAmqpMaxConnectionCount = serviceAmqpMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of AMQP client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform. Available since 2.7.
   * @return serviceAmqpMaxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceAmqpMaxConnectionCount() {
    return serviceAmqpMaxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceAmqpMaxConnectionCount(Long serviceAmqpMaxConnectionCount) {
    this.serviceAmqpMaxConnectionCount = serviceAmqpMaxConnectionCount;
  }


  public MsgVpn serviceAmqpPlainTextEnabled(Boolean serviceAmqpPlainTextEnabled) {
    
    this.serviceAmqpPlainTextEnabled = serviceAmqpPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text AMQP service in the Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.7.
   * @return serviceAmqpPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceAmqpPlainTextEnabled() {
    return serviceAmqpPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceAmqpPlainTextEnabled(Boolean serviceAmqpPlainTextEnabled) {
    this.serviceAmqpPlainTextEnabled = serviceAmqpPlainTextEnabled;
  }


  public MsgVpn serviceAmqpPlainTextListenPort(Long serviceAmqpPlainTextListenPort) {
    
    this.serviceAmqpPlainTextListenPort = serviceAmqpPlainTextListenPort;
    return this;
  }

   /**
   * The port number for plain-text AMQP clients that connect to the Message VPN. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.7.
   * @return serviceAmqpPlainTextListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceAmqpPlainTextListenPort() {
    return serviceAmqpPlainTextListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceAmqpPlainTextListenPort(Long serviceAmqpPlainTextListenPort) {
    this.serviceAmqpPlainTextListenPort = serviceAmqpPlainTextListenPort;
  }


  public MsgVpn serviceAmqpTlsEnabled(Boolean serviceAmqpTlsEnabled) {
    
    this.serviceAmqpTlsEnabled = serviceAmqpTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of encryption (TLS) for the AMQP service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.7.
   * @return serviceAmqpTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceAmqpTlsEnabled() {
    return serviceAmqpTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceAmqpTlsEnabled(Boolean serviceAmqpTlsEnabled) {
    this.serviceAmqpTlsEnabled = serviceAmqpTlsEnabled;
  }


  public MsgVpn serviceAmqpTlsListenPort(Long serviceAmqpTlsListenPort) {
    
    this.serviceAmqpTlsListenPort = serviceAmqpTlsListenPort;
    return this;
  }

   /**
   * The port number for AMQP clients that connect to the Message VPN over TLS. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.7.
   * @return serviceAmqpTlsListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceAmqpTlsListenPort() {
    return serviceAmqpTlsListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceAmqpTlsListenPort(Long serviceAmqpTlsListenPort) {
    this.serviceAmqpTlsListenPort = serviceAmqpTlsListenPort;
  }


  public MsgVpn serviceMqttAuthenticationClientCertRequest(ServiceMqttAuthenticationClientCertRequestEnum serviceMqttAuthenticationClientCertRequest) {
    
    this.serviceMqttAuthenticationClientCertRequest = serviceMqttAuthenticationClientCertRequest;
    return this;
  }

   /**
   * Determines when to request a client certificate from an incoming MQTT client connecting via a TLS port. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-enabled-in-message-vpn\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;never\&quot; - Never ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;when-enabled-in-message-vpn\&quot; - Only ask for a client-certificate if client certificate authentication is enabled under \&quot;message-vpn &gt;  authentication &gt; client-certificate &gt; shutdown\&quot;. &lt;/pre&gt;  Available since 2.21.
   * @return serviceMqttAuthenticationClientCertRequest
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_AUTHENTICATION_CLIENT_CERT_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ServiceMqttAuthenticationClientCertRequestEnum getServiceMqttAuthenticationClientCertRequest() {
    return serviceMqttAuthenticationClientCertRequest;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_AUTHENTICATION_CLIENT_CERT_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttAuthenticationClientCertRequest(ServiceMqttAuthenticationClientCertRequestEnum serviceMqttAuthenticationClientCertRequest) {
    this.serviceMqttAuthenticationClientCertRequest = serviceMqttAuthenticationClientCertRequest;
  }


  public MsgVpn serviceMqttMaxConnectionCount(Long serviceMqttMaxConnectionCount) {
    
    this.serviceMqttMaxConnectionCount = serviceMqttMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of MQTT client connections that can be simultaneously connected to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform. Available since 2.1.
   * @return serviceMqttMaxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceMqttMaxConnectionCount() {
    return serviceMqttMaxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttMaxConnectionCount(Long serviceMqttMaxConnectionCount) {
    this.serviceMqttMaxConnectionCount = serviceMqttMaxConnectionCount;
  }


  public MsgVpn serviceMqttPlainTextEnabled(Boolean serviceMqttPlainTextEnabled) {
    
    this.serviceMqttPlainTextEnabled = serviceMqttPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text MQTT service in the Message VPN. Disabling causes clients currently connected to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.1.
   * @return serviceMqttPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMqttPlainTextEnabled() {
    return serviceMqttPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttPlainTextEnabled(Boolean serviceMqttPlainTextEnabled) {
    this.serviceMqttPlainTextEnabled = serviceMqttPlainTextEnabled;
  }


  public MsgVpn serviceMqttPlainTextListenPort(Long serviceMqttPlainTextListenPort) {
    
    this.serviceMqttPlainTextListenPort = serviceMqttPlainTextListenPort;
    return this;
  }

   /**
   * The port number for plain-text MQTT clients that connect to the Message VPN. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.1.
   * @return serviceMqttPlainTextListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceMqttPlainTextListenPort() {
    return serviceMqttPlainTextListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttPlainTextListenPort(Long serviceMqttPlainTextListenPort) {
    this.serviceMqttPlainTextListenPort = serviceMqttPlainTextListenPort;
  }


  public MsgVpn serviceMqttTlsEnabled(Boolean serviceMqttTlsEnabled) {
    
    this.serviceMqttTlsEnabled = serviceMqttTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of encryption (TLS) for the MQTT service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.1.
   * @return serviceMqttTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMqttTlsEnabled() {
    return serviceMqttTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttTlsEnabled(Boolean serviceMqttTlsEnabled) {
    this.serviceMqttTlsEnabled = serviceMqttTlsEnabled;
  }


  public MsgVpn serviceMqttTlsListenPort(Long serviceMqttTlsListenPort) {
    
    this.serviceMqttTlsListenPort = serviceMqttTlsListenPort;
    return this;
  }

   /**
   * The port number for MQTT clients that connect to the Message VPN over TLS. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.1.
   * @return serviceMqttTlsListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceMqttTlsListenPort() {
    return serviceMqttTlsListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttTlsListenPort(Long serviceMqttTlsListenPort) {
    this.serviceMqttTlsListenPort = serviceMqttTlsListenPort;
  }


  public MsgVpn serviceMqttTlsWebSocketEnabled(Boolean serviceMqttTlsWebSocketEnabled) {
    
    this.serviceMqttTlsWebSocketEnabled = serviceMqttTlsWebSocketEnabled;
    return this;
  }

   /**
   * Enable or disable the use of encrypted WebSocket (WebSocket over TLS) for the MQTT service in the Message VPN. Disabling causes clients currently connected by encrypted WebSocket to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.1.
   * @return serviceMqttTlsWebSocketEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMqttTlsWebSocketEnabled() {
    return serviceMqttTlsWebSocketEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttTlsWebSocketEnabled(Boolean serviceMqttTlsWebSocketEnabled) {
    this.serviceMqttTlsWebSocketEnabled = serviceMqttTlsWebSocketEnabled;
  }


  public MsgVpn serviceMqttTlsWebSocketListenPort(Long serviceMqttTlsWebSocketListenPort) {
    
    this.serviceMqttTlsWebSocketListenPort = serviceMqttTlsWebSocketListenPort;
    return this;
  }

   /**
   * The port number for MQTT clients that connect to the Message VPN using WebSocket over TLS. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.1.
   * @return serviceMqttTlsWebSocketListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceMqttTlsWebSocketListenPort() {
    return serviceMqttTlsWebSocketListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_TLS_WEB_SOCKET_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttTlsWebSocketListenPort(Long serviceMqttTlsWebSocketListenPort) {
    this.serviceMqttTlsWebSocketListenPort = serviceMqttTlsWebSocketListenPort;
  }


  public MsgVpn serviceMqttWebSocketEnabled(Boolean serviceMqttWebSocketEnabled) {
    
    this.serviceMqttWebSocketEnabled = serviceMqttWebSocketEnabled;
    return this;
  }

   /**
   * Enable or disable the use of WebSocket for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.1.
   * @return serviceMqttWebSocketEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMqttWebSocketEnabled() {
    return serviceMqttWebSocketEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttWebSocketEnabled(Boolean serviceMqttWebSocketEnabled) {
    this.serviceMqttWebSocketEnabled = serviceMqttWebSocketEnabled;
  }


  public MsgVpn serviceMqttWebSocketListenPort(Long serviceMqttWebSocketListenPort) {
    
    this.serviceMqttWebSocketListenPort = serviceMqttWebSocketListenPort;
    return this;
  }

   /**
   * The port number for plain-text MQTT clients that connect to the Message VPN using WebSocket. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.1.
   * @return serviceMqttWebSocketListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceMqttWebSocketListenPort() {
    return serviceMqttWebSocketListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_WEB_SOCKET_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttWebSocketListenPort(Long serviceMqttWebSocketListenPort) {
    this.serviceMqttWebSocketListenPort = serviceMqttWebSocketListenPort;
  }


  public MsgVpn serviceRestIncomingAuthenticationClientCertRequest(ServiceRestIncomingAuthenticationClientCertRequestEnum serviceRestIncomingAuthenticationClientCertRequest) {
    
    this.serviceRestIncomingAuthenticationClientCertRequest = serviceRestIncomingAuthenticationClientCertRequest;
    return this;
  }

   /**
   * Determines when to request a client certificate from an incoming REST Producer connecting via a TLS port. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-enabled-in-message-vpn\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;never\&quot; - Never ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;when-enabled-in-message-vpn\&quot; - Only ask for a client-certificate if client certificate authentication is enabled under \&quot;message-vpn &gt;  authentication &gt; client-certificate &gt; shutdown\&quot;. &lt;/pre&gt;  Available since 2.21.
   * @return serviceRestIncomingAuthenticationClientCertRequest
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHENTICATION_CLIENT_CERT_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ServiceRestIncomingAuthenticationClientCertRequestEnum getServiceRestIncomingAuthenticationClientCertRequest() {
    return serviceRestIncomingAuthenticationClientCertRequest;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHENTICATION_CLIENT_CERT_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingAuthenticationClientCertRequest(ServiceRestIncomingAuthenticationClientCertRequestEnum serviceRestIncomingAuthenticationClientCertRequest) {
    this.serviceRestIncomingAuthenticationClientCertRequest = serviceRestIncomingAuthenticationClientCertRequest;
  }


  public MsgVpn serviceRestIncomingAuthorizationHeaderHandling(ServiceRestIncomingAuthorizationHeaderHandlingEnum serviceRestIncomingAuthorizationHeaderHandling) {
    
    this.serviceRestIncomingAuthorizationHeaderHandling = serviceRestIncomingAuthorizationHeaderHandling;
    return this;
  }

   /**
   * The handling of Authorization headers for incoming REST connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;drop\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;drop\&quot; - Do not attach the Authorization header to the message as a user property. This configuration is most secure. \&quot;forward\&quot; - Forward the Authorization header, attaching it to the message as a user property in the same way as other headers. For best security, use the drop setting. \&quot;legacy\&quot; - If the Authorization header was used for authentication to the broker, do not attach it to the message. If the Authorization header was not used for authentication to the broker, attach it to the message as a user property in the same way as other headers. For best security, use the drop setting. &lt;/pre&gt;  Available since 2.19.
   * @return serviceRestIncomingAuthorizationHeaderHandling
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHORIZATION_HEADER_HANDLING)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ServiceRestIncomingAuthorizationHeaderHandlingEnum getServiceRestIncomingAuthorizationHeaderHandling() {
    return serviceRestIncomingAuthorizationHeaderHandling;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_AUTHORIZATION_HEADER_HANDLING)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingAuthorizationHeaderHandling(ServiceRestIncomingAuthorizationHeaderHandlingEnum serviceRestIncomingAuthorizationHeaderHandling) {
    this.serviceRestIncomingAuthorizationHeaderHandling = serviceRestIncomingAuthorizationHeaderHandling;
  }


  public MsgVpn serviceRestIncomingMaxConnectionCount(Long serviceRestIncomingMaxConnectionCount) {
    
    this.serviceRestIncomingMaxConnectionCount = serviceRestIncomingMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform.
   * @return serviceRestIncomingMaxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceRestIncomingMaxConnectionCount() {
    return serviceRestIncomingMaxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingMaxConnectionCount(Long serviceRestIncomingMaxConnectionCount) {
    this.serviceRestIncomingMaxConnectionCount = serviceRestIncomingMaxConnectionCount;
  }


  public MsgVpn serviceRestIncomingPlainTextEnabled(Boolean serviceRestIncomingPlainTextEnabled) {
    
    this.serviceRestIncomingPlainTextEnabled = serviceRestIncomingPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text REST service for incoming clients in the Message VPN. Disabling causes clients currently connected to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return serviceRestIncomingPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceRestIncomingPlainTextEnabled() {
    return serviceRestIncomingPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingPlainTextEnabled(Boolean serviceRestIncomingPlainTextEnabled) {
    this.serviceRestIncomingPlainTextEnabled = serviceRestIncomingPlainTextEnabled;
  }


  public MsgVpn serviceRestIncomingPlainTextListenPort(Long serviceRestIncomingPlainTextListenPort) {
    
    this.serviceRestIncomingPlainTextListenPort = serviceRestIncomingPlainTextListenPort;
    return this;
  }

   /**
   * The port number for incoming plain-text REST clients that connect to the Message VPN. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return serviceRestIncomingPlainTextListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceRestIncomingPlainTextListenPort() {
    return serviceRestIncomingPlainTextListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingPlainTextListenPort(Long serviceRestIncomingPlainTextListenPort) {
    this.serviceRestIncomingPlainTextListenPort = serviceRestIncomingPlainTextListenPort;
  }


  public MsgVpn serviceRestIncomingTlsEnabled(Boolean serviceRestIncomingTlsEnabled) {
    
    this.serviceRestIncomingTlsEnabled = serviceRestIncomingTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of encryption (TLS) for the REST service for incoming clients in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return serviceRestIncomingTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceRestIncomingTlsEnabled() {
    return serviceRestIncomingTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingTlsEnabled(Boolean serviceRestIncomingTlsEnabled) {
    this.serviceRestIncomingTlsEnabled = serviceRestIncomingTlsEnabled;
  }


  public MsgVpn serviceRestIncomingTlsListenPort(Long serviceRestIncomingTlsListenPort) {
    
    this.serviceRestIncomingTlsListenPort = serviceRestIncomingTlsListenPort;
    return this;
  }

   /**
   * The port number for incoming REST clients that connect to the Message VPN over TLS. The port must be unique across the message backbone. A value of 0 means that the listen-port is unassigned and cannot be enabled. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return serviceRestIncomingTlsListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceRestIncomingTlsListenPort() {
    return serviceRestIncomingTlsListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingTlsListenPort(Long serviceRestIncomingTlsListenPort) {
    this.serviceRestIncomingTlsListenPort = serviceRestIncomingTlsListenPort;
  }


  public MsgVpn serviceRestMode(ServiceRestModeEnum serviceRestMode) {
    
    this.serviceRestMode = serviceRestMode;
    return this;
  }

   /**
   * The REST service mode for incoming REST clients that connect to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;messaging\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;gateway\&quot; - Act as a message gateway through which REST messages are propagated. \&quot;messaging\&quot; - Act as a message broker on which REST messages are queued. &lt;/pre&gt;  Available since 2.6.
   * @return serviceRestMode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ServiceRestModeEnum getServiceRestMode() {
    return serviceRestMode;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestMode(ServiceRestModeEnum serviceRestMode) {
    this.serviceRestMode = serviceRestMode;
  }


  public MsgVpn serviceRestOutgoingMaxConnectionCount(Long serviceRestOutgoingMaxConnectionCount) {
    
    this.serviceRestOutgoingMaxConnectionCount = serviceRestOutgoingMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of REST Consumer (outgoing) client connections that can be simultaneously connected to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
   * @return serviceRestOutgoingMaxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_OUTGOING_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceRestOutgoingMaxConnectionCount() {
    return serviceRestOutgoingMaxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_OUTGOING_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestOutgoingMaxConnectionCount(Long serviceRestOutgoingMaxConnectionCount) {
    this.serviceRestOutgoingMaxConnectionCount = serviceRestOutgoingMaxConnectionCount;
  }


  public MsgVpn serviceSmfMaxConnectionCount(Long serviceSmfMaxConnectionCount) {
    
    this.serviceSmfMaxConnectionCount = serviceSmfMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
   * @return serviceSmfMaxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSmfMaxConnectionCount() {
    return serviceSmfMaxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfMaxConnectionCount(Long serviceSmfMaxConnectionCount) {
    this.serviceSmfMaxConnectionCount = serviceSmfMaxConnectionCount;
  }


  public MsgVpn serviceSmfPlainTextEnabled(Boolean serviceSmfPlainTextEnabled) {
    
    this.serviceSmfPlainTextEnabled = serviceSmfPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text SMF service in the Message VPN. Disabling causes clients currently connected to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return serviceSmfPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSmfPlainTextEnabled() {
    return serviceSmfPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfPlainTextEnabled(Boolean serviceSmfPlainTextEnabled) {
    this.serviceSmfPlainTextEnabled = serviceSmfPlainTextEnabled;
  }


  public MsgVpn serviceSmfTlsEnabled(Boolean serviceSmfTlsEnabled) {
    
    this.serviceSmfTlsEnabled = serviceSmfTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of encryption (TLS) for the SMF service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return serviceSmfTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSmfTlsEnabled() {
    return serviceSmfTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfTlsEnabled(Boolean serviceSmfTlsEnabled) {
    this.serviceSmfTlsEnabled = serviceSmfTlsEnabled;
  }


  public MsgVpn serviceWebAuthenticationClientCertRequest(ServiceWebAuthenticationClientCertRequestEnum serviceWebAuthenticationClientCertRequest) {
    
    this.serviceWebAuthenticationClientCertRequest = serviceWebAuthenticationClientCertRequest;
    return this;
  }

   /**
   * Determines when to request a client certificate from a Web Transport client connecting via a TLS port. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-enabled-in-message-vpn\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;never\&quot; - Never ask for a client certificate regardless of the \&quot;message-vpn &gt; authentication &gt; client-certificate &gt; shutdown\&quot; configuration. \&quot;when-enabled-in-message-vpn\&quot; - Only ask for a client-certificate if client certificate authentication is enabled under \&quot;message-vpn &gt;  authentication &gt; client-certificate &gt; shutdown\&quot;. &lt;/pre&gt;  Available since 2.21.
   * @return serviceWebAuthenticationClientCertRequest
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_AUTHENTICATION_CLIENT_CERT_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public ServiceWebAuthenticationClientCertRequestEnum getServiceWebAuthenticationClientCertRequest() {
    return serviceWebAuthenticationClientCertRequest;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_AUTHENTICATION_CLIENT_CERT_REQUEST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebAuthenticationClientCertRequest(ServiceWebAuthenticationClientCertRequestEnum serviceWebAuthenticationClientCertRequest) {
    this.serviceWebAuthenticationClientCertRequest = serviceWebAuthenticationClientCertRequest;
  }


  public MsgVpn serviceWebMaxConnectionCount(Long serviceWebMaxConnectionCount) {
    
    this.serviceWebMaxConnectionCount = serviceWebMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of Web Transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform.
   * @return serviceWebMaxConnectionCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceWebMaxConnectionCount() {
    return serviceWebMaxConnectionCount;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebMaxConnectionCount(Long serviceWebMaxConnectionCount) {
    this.serviceWebMaxConnectionCount = serviceWebMaxConnectionCount;
  }


  public MsgVpn serviceWebPlainTextEnabled(Boolean serviceWebPlainTextEnabled) {
    
    this.serviceWebPlainTextEnabled = serviceWebPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text Web Transport service in the Message VPN. Disabling causes clients currently connected to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return serviceWebPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceWebPlainTextEnabled() {
    return serviceWebPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebPlainTextEnabled(Boolean serviceWebPlainTextEnabled) {
    this.serviceWebPlainTextEnabled = serviceWebPlainTextEnabled;
  }


  public MsgVpn serviceWebTlsEnabled(Boolean serviceWebTlsEnabled) {
    
    this.serviceWebTlsEnabled = serviceWebTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of TLS for the Web Transport service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return serviceWebTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceWebTlsEnabled() {
    return serviceWebTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebTlsEnabled(Boolean serviceWebTlsEnabled) {
    this.serviceWebTlsEnabled = serviceWebTlsEnabled;
  }


  public MsgVpn tlsAllowDowngradeToPlainTextEnabled(Boolean tlsAllowDowngradeToPlainTextEnabled) {
    
    this.tlsAllowDowngradeToPlainTextEnabled = tlsAllowDowngradeToPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return tlsAllowDowngradeToPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_ALLOW_DOWNGRADE_TO_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsAllowDowngradeToPlainTextEnabled() {
    return tlsAllowDowngradeToPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_ALLOW_DOWNGRADE_TO_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsAllowDowngradeToPlainTextEnabled(Boolean tlsAllowDowngradeToPlainTextEnabled) {
    this.tlsAllowDowngradeToPlainTextEnabled = tlsAllowDowngradeToPlainTextEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpn msgVpn = (MsgVpn) o;
    return Objects.equals(this.alias, msgVpn.alias) &&
        Objects.equals(this.authenticationBasicEnabled, msgVpn.authenticationBasicEnabled) &&
        Objects.equals(this.authenticationBasicProfileName, msgVpn.authenticationBasicProfileName) &&
        Objects.equals(this.authenticationBasicRadiusDomain, msgVpn.authenticationBasicRadiusDomain) &&
        Objects.equals(this.authenticationBasicType, msgVpn.authenticationBasicType) &&
        Objects.equals(this.authenticationClientCertAllowApiProvidedUsernameEnabled, msgVpn.authenticationClientCertAllowApiProvidedUsernameEnabled) &&
        Objects.equals(this.authenticationClientCertCertificateMatchingRulesEnabled, msgVpn.authenticationClientCertCertificateMatchingRulesEnabled) &&
        Objects.equals(this.authenticationClientCertEnabled, msgVpn.authenticationClientCertEnabled) &&
        Objects.equals(this.authenticationClientCertMaxChainDepth, msgVpn.authenticationClientCertMaxChainDepth) &&
        Objects.equals(this.authenticationClientCertRevocationCheckMode, msgVpn.authenticationClientCertRevocationCheckMode) &&
        Objects.equals(this.authenticationClientCertUsernameSource, msgVpn.authenticationClientCertUsernameSource) &&
        Objects.equals(this.authenticationClientCertValidateDateEnabled, msgVpn.authenticationClientCertValidateDateEnabled) &&
        Objects.equals(this.authenticationKerberosAllowApiProvidedUsernameEnabled, msgVpn.authenticationKerberosAllowApiProvidedUsernameEnabled) &&
        Objects.equals(this.authenticationKerberosEnabled, msgVpn.authenticationKerberosEnabled) &&
        Objects.equals(this.authenticationOauthDefaultProfileName, msgVpn.authenticationOauthDefaultProfileName) &&
        Objects.equals(this.authenticationOauthDefaultProviderName, msgVpn.authenticationOauthDefaultProviderName) &&
        Objects.equals(this.authenticationOauthEnabled, msgVpn.authenticationOauthEnabled) &&
        Objects.equals(this.authorizationLdapGroupMembershipAttributeName, msgVpn.authorizationLdapGroupMembershipAttributeName) &&
        Objects.equals(this.authorizationLdapTrimClientUsernameDomainEnabled, msgVpn.authorizationLdapTrimClientUsernameDomainEnabled) &&
        Objects.equals(this.authorizationProfileName, msgVpn.authorizationProfileName) &&
        Objects.equals(this.authorizationType, msgVpn.authorizationType) &&
        Objects.equals(this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled, msgVpn.bridgingTlsServerCertEnforceTrustedCommonNameEnabled) &&
        Objects.equals(this.bridgingTlsServerCertMaxChainDepth, msgVpn.bridgingTlsServerCertMaxChainDepth) &&
        Objects.equals(this.bridgingTlsServerCertValidateDateEnabled, msgVpn.bridgingTlsServerCertValidateDateEnabled) &&
        Objects.equals(this.bridgingTlsServerCertValidateNameEnabled, msgVpn.bridgingTlsServerCertValidateNameEnabled) &&
        Objects.equals(this.distributedCacheManagementEnabled, msgVpn.distributedCacheManagementEnabled) &&
        Objects.equals(this.dmrEnabled, msgVpn.dmrEnabled) &&
        Objects.equals(this.enabled, msgVpn.enabled) &&
        Objects.equals(this.eventConnectionCountThreshold, msgVpn.eventConnectionCountThreshold) &&
        Objects.equals(this.eventEgressFlowCountThreshold, msgVpn.eventEgressFlowCountThreshold) &&
        Objects.equals(this.eventEgressMsgRateThreshold, msgVpn.eventEgressMsgRateThreshold) &&
        Objects.equals(this.eventEndpointCountThreshold, msgVpn.eventEndpointCountThreshold) &&
        Objects.equals(this.eventIngressFlowCountThreshold, msgVpn.eventIngressFlowCountThreshold) &&
        Objects.equals(this.eventIngressMsgRateThreshold, msgVpn.eventIngressMsgRateThreshold) &&
        Objects.equals(this.eventLargeMsgThreshold, msgVpn.eventLargeMsgThreshold) &&
        Objects.equals(this.eventLogTag, msgVpn.eventLogTag) &&
        Objects.equals(this.eventMsgSpoolUsageThreshold, msgVpn.eventMsgSpoolUsageThreshold) &&
        Objects.equals(this.eventPublishClientEnabled, msgVpn.eventPublishClientEnabled) &&
        Objects.equals(this.eventPublishMsgVpnEnabled, msgVpn.eventPublishMsgVpnEnabled) &&
        Objects.equals(this.eventPublishSubscriptionMode, msgVpn.eventPublishSubscriptionMode) &&
        Objects.equals(this.eventPublishTopicFormatMqttEnabled, msgVpn.eventPublishTopicFormatMqttEnabled) &&
        Objects.equals(this.eventPublishTopicFormatSmfEnabled, msgVpn.eventPublishTopicFormatSmfEnabled) &&
        Objects.equals(this.eventServiceAmqpConnectionCountThreshold, msgVpn.eventServiceAmqpConnectionCountThreshold) &&
        Objects.equals(this.eventServiceMqttConnectionCountThreshold, msgVpn.eventServiceMqttConnectionCountThreshold) &&
        Objects.equals(this.eventServiceRestIncomingConnectionCountThreshold, msgVpn.eventServiceRestIncomingConnectionCountThreshold) &&
        Objects.equals(this.eventServiceSmfConnectionCountThreshold, msgVpn.eventServiceSmfConnectionCountThreshold) &&
        Objects.equals(this.eventServiceWebConnectionCountThreshold, msgVpn.eventServiceWebConnectionCountThreshold) &&
        Objects.equals(this.eventSubscriptionCountThreshold, msgVpn.eventSubscriptionCountThreshold) &&
        Objects.equals(this.eventTransactedSessionCountThreshold, msgVpn.eventTransactedSessionCountThreshold) &&
        Objects.equals(this.eventTransactionCountThreshold, msgVpn.eventTransactionCountThreshold) &&
        Objects.equals(this.exportSubscriptionsEnabled, msgVpn.exportSubscriptionsEnabled) &&
        Objects.equals(this.jndiEnabled, msgVpn.jndiEnabled) &&
        Objects.equals(this.maxConnectionCount, msgVpn.maxConnectionCount) &&
        Objects.equals(this.maxEgressFlowCount, msgVpn.maxEgressFlowCount) &&
        Objects.equals(this.maxEndpointCount, msgVpn.maxEndpointCount) &&
        Objects.equals(this.maxIngressFlowCount, msgVpn.maxIngressFlowCount) &&
        Objects.equals(this.maxMsgSpoolUsage, msgVpn.maxMsgSpoolUsage) &&
        Objects.equals(this.maxSubscriptionCount, msgVpn.maxSubscriptionCount) &&
        Objects.equals(this.maxTransactedSessionCount, msgVpn.maxTransactedSessionCount) &&
        Objects.equals(this.maxTransactionCount, msgVpn.maxTransactionCount) &&
        Objects.equals(this.mqttRetainMaxMemory, msgVpn.mqttRetainMaxMemory) &&
        Objects.equals(this.msgVpnName, msgVpn.msgVpnName) &&
        Objects.equals(this.replicationAckPropagationIntervalMsgCount, msgVpn.replicationAckPropagationIntervalMsgCount) &&
        Objects.equals(this.replicationBridgeAuthenticationBasicClientUsername, msgVpn.replicationBridgeAuthenticationBasicClientUsername) &&
        Objects.equals(this.replicationBridgeAuthenticationBasicPassword, msgVpn.replicationBridgeAuthenticationBasicPassword) &&
        Objects.equals(this.replicationBridgeAuthenticationClientCertContent, msgVpn.replicationBridgeAuthenticationClientCertContent) &&
        Objects.equals(this.replicationBridgeAuthenticationClientCertPassword, msgVpn.replicationBridgeAuthenticationClientCertPassword) &&
        Objects.equals(this.replicationBridgeAuthenticationScheme, msgVpn.replicationBridgeAuthenticationScheme) &&
        Objects.equals(this.replicationBridgeCompressedDataEnabled, msgVpn.replicationBridgeCompressedDataEnabled) &&
        Objects.equals(this.replicationBridgeEgressFlowWindowSize, msgVpn.replicationBridgeEgressFlowWindowSize) &&
        Objects.equals(this.replicationBridgeRetryDelay, msgVpn.replicationBridgeRetryDelay) &&
        Objects.equals(this.replicationBridgeTlsEnabled, msgVpn.replicationBridgeTlsEnabled) &&
        Objects.equals(this.replicationBridgeUnidirectionalClientProfileName, msgVpn.replicationBridgeUnidirectionalClientProfileName) &&
        Objects.equals(this.replicationEnabled, msgVpn.replicationEnabled) &&
        Objects.equals(this.replicationEnabledQueueBehavior, msgVpn.replicationEnabledQueueBehavior) &&
        Objects.equals(this.replicationQueueMaxMsgSpoolUsage, msgVpn.replicationQueueMaxMsgSpoolUsage) &&
        Objects.equals(this.replicationQueueRejectMsgToSenderOnDiscardEnabled, msgVpn.replicationQueueRejectMsgToSenderOnDiscardEnabled) &&
        Objects.equals(this.replicationRejectMsgWhenSyncIneligibleEnabled, msgVpn.replicationRejectMsgWhenSyncIneligibleEnabled) &&
        Objects.equals(this.replicationRole, msgVpn.replicationRole) &&
        Objects.equals(this.replicationTransactionMode, msgVpn.replicationTransactionMode) &&
        Objects.equals(this.restTlsServerCertEnforceTrustedCommonNameEnabled, msgVpn.restTlsServerCertEnforceTrustedCommonNameEnabled) &&
        Objects.equals(this.restTlsServerCertMaxChainDepth, msgVpn.restTlsServerCertMaxChainDepth) &&
        Objects.equals(this.restTlsServerCertValidateDateEnabled, msgVpn.restTlsServerCertValidateDateEnabled) &&
        Objects.equals(this.restTlsServerCertValidateNameEnabled, msgVpn.restTlsServerCertValidateNameEnabled) &&
        Objects.equals(this.sempOverMsgBusAdminClientEnabled, msgVpn.sempOverMsgBusAdminClientEnabled) &&
        Objects.equals(this.sempOverMsgBusAdminDistributedCacheEnabled, msgVpn.sempOverMsgBusAdminDistributedCacheEnabled) &&
        Objects.equals(this.sempOverMsgBusAdminEnabled, msgVpn.sempOverMsgBusAdminEnabled) &&
        Objects.equals(this.sempOverMsgBusEnabled, msgVpn.sempOverMsgBusEnabled) &&
        Objects.equals(this.sempOverMsgBusShowEnabled, msgVpn.sempOverMsgBusShowEnabled) &&
        Objects.equals(this.serviceAmqpMaxConnectionCount, msgVpn.serviceAmqpMaxConnectionCount) &&
        Objects.equals(this.serviceAmqpPlainTextEnabled, msgVpn.serviceAmqpPlainTextEnabled) &&
        Objects.equals(this.serviceAmqpPlainTextListenPort, msgVpn.serviceAmqpPlainTextListenPort) &&
        Objects.equals(this.serviceAmqpTlsEnabled, msgVpn.serviceAmqpTlsEnabled) &&
        Objects.equals(this.serviceAmqpTlsListenPort, msgVpn.serviceAmqpTlsListenPort) &&
        Objects.equals(this.serviceMqttAuthenticationClientCertRequest, msgVpn.serviceMqttAuthenticationClientCertRequest) &&
        Objects.equals(this.serviceMqttMaxConnectionCount, msgVpn.serviceMqttMaxConnectionCount) &&
        Objects.equals(this.serviceMqttPlainTextEnabled, msgVpn.serviceMqttPlainTextEnabled) &&
        Objects.equals(this.serviceMqttPlainTextListenPort, msgVpn.serviceMqttPlainTextListenPort) &&
        Objects.equals(this.serviceMqttTlsEnabled, msgVpn.serviceMqttTlsEnabled) &&
        Objects.equals(this.serviceMqttTlsListenPort, msgVpn.serviceMqttTlsListenPort) &&
        Objects.equals(this.serviceMqttTlsWebSocketEnabled, msgVpn.serviceMqttTlsWebSocketEnabled) &&
        Objects.equals(this.serviceMqttTlsWebSocketListenPort, msgVpn.serviceMqttTlsWebSocketListenPort) &&
        Objects.equals(this.serviceMqttWebSocketEnabled, msgVpn.serviceMqttWebSocketEnabled) &&
        Objects.equals(this.serviceMqttWebSocketListenPort, msgVpn.serviceMqttWebSocketListenPort) &&
        Objects.equals(this.serviceRestIncomingAuthenticationClientCertRequest, msgVpn.serviceRestIncomingAuthenticationClientCertRequest) &&
        Objects.equals(this.serviceRestIncomingAuthorizationHeaderHandling, msgVpn.serviceRestIncomingAuthorizationHeaderHandling) &&
        Objects.equals(this.serviceRestIncomingMaxConnectionCount, msgVpn.serviceRestIncomingMaxConnectionCount) &&
        Objects.equals(this.serviceRestIncomingPlainTextEnabled, msgVpn.serviceRestIncomingPlainTextEnabled) &&
        Objects.equals(this.serviceRestIncomingPlainTextListenPort, msgVpn.serviceRestIncomingPlainTextListenPort) &&
        Objects.equals(this.serviceRestIncomingTlsEnabled, msgVpn.serviceRestIncomingTlsEnabled) &&
        Objects.equals(this.serviceRestIncomingTlsListenPort, msgVpn.serviceRestIncomingTlsListenPort) &&
        Objects.equals(this.serviceRestMode, msgVpn.serviceRestMode) &&
        Objects.equals(this.serviceRestOutgoingMaxConnectionCount, msgVpn.serviceRestOutgoingMaxConnectionCount) &&
        Objects.equals(this.serviceSmfMaxConnectionCount, msgVpn.serviceSmfMaxConnectionCount) &&
        Objects.equals(this.serviceSmfPlainTextEnabled, msgVpn.serviceSmfPlainTextEnabled) &&
        Objects.equals(this.serviceSmfTlsEnabled, msgVpn.serviceSmfTlsEnabled) &&
        Objects.equals(this.serviceWebAuthenticationClientCertRequest, msgVpn.serviceWebAuthenticationClientCertRequest) &&
        Objects.equals(this.serviceWebMaxConnectionCount, msgVpn.serviceWebMaxConnectionCount) &&
        Objects.equals(this.serviceWebPlainTextEnabled, msgVpn.serviceWebPlainTextEnabled) &&
        Objects.equals(this.serviceWebTlsEnabled, msgVpn.serviceWebTlsEnabled) &&
        Objects.equals(this.tlsAllowDowngradeToPlainTextEnabled, msgVpn.tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias, authenticationBasicEnabled, authenticationBasicProfileName, authenticationBasicRadiusDomain, authenticationBasicType, authenticationClientCertAllowApiProvidedUsernameEnabled, authenticationClientCertCertificateMatchingRulesEnabled, authenticationClientCertEnabled, authenticationClientCertMaxChainDepth, authenticationClientCertRevocationCheckMode, authenticationClientCertUsernameSource, authenticationClientCertValidateDateEnabled, authenticationKerberosAllowApiProvidedUsernameEnabled, authenticationKerberosEnabled, authenticationOauthDefaultProfileName, authenticationOauthDefaultProviderName, authenticationOauthEnabled, authorizationLdapGroupMembershipAttributeName, authorizationLdapTrimClientUsernameDomainEnabled, authorizationProfileName, authorizationType, bridgingTlsServerCertEnforceTrustedCommonNameEnabled, bridgingTlsServerCertMaxChainDepth, bridgingTlsServerCertValidateDateEnabled, bridgingTlsServerCertValidateNameEnabled, distributedCacheManagementEnabled, dmrEnabled, enabled, eventConnectionCountThreshold, eventEgressFlowCountThreshold, eventEgressMsgRateThreshold, eventEndpointCountThreshold, eventIngressFlowCountThreshold, eventIngressMsgRateThreshold, eventLargeMsgThreshold, eventLogTag, eventMsgSpoolUsageThreshold, eventPublishClientEnabled, eventPublishMsgVpnEnabled, eventPublishSubscriptionMode, eventPublishTopicFormatMqttEnabled, eventPublishTopicFormatSmfEnabled, eventServiceAmqpConnectionCountThreshold, eventServiceMqttConnectionCountThreshold, eventServiceRestIncomingConnectionCountThreshold, eventServiceSmfConnectionCountThreshold, eventServiceWebConnectionCountThreshold, eventSubscriptionCountThreshold, eventTransactedSessionCountThreshold, eventTransactionCountThreshold, exportSubscriptionsEnabled, jndiEnabled, maxConnectionCount, maxEgressFlowCount, maxEndpointCount, maxIngressFlowCount, maxMsgSpoolUsage, maxSubscriptionCount, maxTransactedSessionCount, maxTransactionCount, mqttRetainMaxMemory, msgVpnName, replicationAckPropagationIntervalMsgCount, replicationBridgeAuthenticationBasicClientUsername, replicationBridgeAuthenticationBasicPassword, replicationBridgeAuthenticationClientCertContent, replicationBridgeAuthenticationClientCertPassword, replicationBridgeAuthenticationScheme, replicationBridgeCompressedDataEnabled, replicationBridgeEgressFlowWindowSize, replicationBridgeRetryDelay, replicationBridgeTlsEnabled, replicationBridgeUnidirectionalClientProfileName, replicationEnabled, replicationEnabledQueueBehavior, replicationQueueMaxMsgSpoolUsage, replicationQueueRejectMsgToSenderOnDiscardEnabled, replicationRejectMsgWhenSyncIneligibleEnabled, replicationRole, replicationTransactionMode, restTlsServerCertEnforceTrustedCommonNameEnabled, restTlsServerCertMaxChainDepth, restTlsServerCertValidateDateEnabled, restTlsServerCertValidateNameEnabled, sempOverMsgBusAdminClientEnabled, sempOverMsgBusAdminDistributedCacheEnabled, sempOverMsgBusAdminEnabled, sempOverMsgBusEnabled, sempOverMsgBusShowEnabled, serviceAmqpMaxConnectionCount, serviceAmqpPlainTextEnabled, serviceAmqpPlainTextListenPort, serviceAmqpTlsEnabled, serviceAmqpTlsListenPort, serviceMqttAuthenticationClientCertRequest, serviceMqttMaxConnectionCount, serviceMqttPlainTextEnabled, serviceMqttPlainTextListenPort, serviceMqttTlsEnabled, serviceMqttTlsListenPort, serviceMqttTlsWebSocketEnabled, serviceMqttTlsWebSocketListenPort, serviceMqttWebSocketEnabled, serviceMqttWebSocketListenPort, serviceRestIncomingAuthenticationClientCertRequest, serviceRestIncomingAuthorizationHeaderHandling, serviceRestIncomingMaxConnectionCount, serviceRestIncomingPlainTextEnabled, serviceRestIncomingPlainTextListenPort, serviceRestIncomingTlsEnabled, serviceRestIncomingTlsListenPort, serviceRestMode, serviceRestOutgoingMaxConnectionCount, serviceSmfMaxConnectionCount, serviceSmfPlainTextEnabled, serviceSmfTlsEnabled, serviceWebAuthenticationClientCertRequest, serviceWebMaxConnectionCount, serviceWebPlainTextEnabled, serviceWebTlsEnabled, tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpn {\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    authenticationBasicEnabled: ").append(toIndentedString(authenticationBasicEnabled)).append("\n");
    sb.append("    authenticationBasicProfileName: ").append(toIndentedString(authenticationBasicProfileName)).append("\n");
    sb.append("    authenticationBasicRadiusDomain: ").append(toIndentedString(authenticationBasicRadiusDomain)).append("\n");
    sb.append("    authenticationBasicType: ").append(toIndentedString(authenticationBasicType)).append("\n");
    sb.append("    authenticationClientCertAllowApiProvidedUsernameEnabled: ").append(toIndentedString(authenticationClientCertAllowApiProvidedUsernameEnabled)).append("\n");
    sb.append("    authenticationClientCertCertificateMatchingRulesEnabled: ").append(toIndentedString(authenticationClientCertCertificateMatchingRulesEnabled)).append("\n");
    sb.append("    authenticationClientCertEnabled: ").append(toIndentedString(authenticationClientCertEnabled)).append("\n");
    sb.append("    authenticationClientCertMaxChainDepth: ").append(toIndentedString(authenticationClientCertMaxChainDepth)).append("\n");
    sb.append("    authenticationClientCertRevocationCheckMode: ").append(toIndentedString(authenticationClientCertRevocationCheckMode)).append("\n");
    sb.append("    authenticationClientCertUsernameSource: ").append(toIndentedString(authenticationClientCertUsernameSource)).append("\n");
    sb.append("    authenticationClientCertValidateDateEnabled: ").append(toIndentedString(authenticationClientCertValidateDateEnabled)).append("\n");
    sb.append("    authenticationKerberosAllowApiProvidedUsernameEnabled: ").append(toIndentedString(authenticationKerberosAllowApiProvidedUsernameEnabled)).append("\n");
    sb.append("    authenticationKerberosEnabled: ").append(toIndentedString(authenticationKerberosEnabled)).append("\n");
    sb.append("    authenticationOauthDefaultProfileName: ").append(toIndentedString(authenticationOauthDefaultProfileName)).append("\n");
    sb.append("    authenticationOauthDefaultProviderName: ").append(toIndentedString(authenticationOauthDefaultProviderName)).append("\n");
    sb.append("    authenticationOauthEnabled: ").append(toIndentedString(authenticationOauthEnabled)).append("\n");
    sb.append("    authorizationLdapGroupMembershipAttributeName: ").append(toIndentedString(authorizationLdapGroupMembershipAttributeName)).append("\n");
    sb.append("    authorizationLdapTrimClientUsernameDomainEnabled: ").append(toIndentedString(authorizationLdapTrimClientUsernameDomainEnabled)).append("\n");
    sb.append("    authorizationProfileName: ").append(toIndentedString(authorizationProfileName)).append("\n");
    sb.append("    authorizationType: ").append(toIndentedString(authorizationType)).append("\n");
    sb.append("    bridgingTlsServerCertEnforceTrustedCommonNameEnabled: ").append(toIndentedString(bridgingTlsServerCertEnforceTrustedCommonNameEnabled)).append("\n");
    sb.append("    bridgingTlsServerCertMaxChainDepth: ").append(toIndentedString(bridgingTlsServerCertMaxChainDepth)).append("\n");
    sb.append("    bridgingTlsServerCertValidateDateEnabled: ").append(toIndentedString(bridgingTlsServerCertValidateDateEnabled)).append("\n");
    sb.append("    bridgingTlsServerCertValidateNameEnabled: ").append(toIndentedString(bridgingTlsServerCertValidateNameEnabled)).append("\n");
    sb.append("    distributedCacheManagementEnabled: ").append(toIndentedString(distributedCacheManagementEnabled)).append("\n");
    sb.append("    dmrEnabled: ").append(toIndentedString(dmrEnabled)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    eventConnectionCountThreshold: ").append(toIndentedString(eventConnectionCountThreshold)).append("\n");
    sb.append("    eventEgressFlowCountThreshold: ").append(toIndentedString(eventEgressFlowCountThreshold)).append("\n");
    sb.append("    eventEgressMsgRateThreshold: ").append(toIndentedString(eventEgressMsgRateThreshold)).append("\n");
    sb.append("    eventEndpointCountThreshold: ").append(toIndentedString(eventEndpointCountThreshold)).append("\n");
    sb.append("    eventIngressFlowCountThreshold: ").append(toIndentedString(eventIngressFlowCountThreshold)).append("\n");
    sb.append("    eventIngressMsgRateThreshold: ").append(toIndentedString(eventIngressMsgRateThreshold)).append("\n");
    sb.append("    eventLargeMsgThreshold: ").append(toIndentedString(eventLargeMsgThreshold)).append("\n");
    sb.append("    eventLogTag: ").append(toIndentedString(eventLogTag)).append("\n");
    sb.append("    eventMsgSpoolUsageThreshold: ").append(toIndentedString(eventMsgSpoolUsageThreshold)).append("\n");
    sb.append("    eventPublishClientEnabled: ").append(toIndentedString(eventPublishClientEnabled)).append("\n");
    sb.append("    eventPublishMsgVpnEnabled: ").append(toIndentedString(eventPublishMsgVpnEnabled)).append("\n");
    sb.append("    eventPublishSubscriptionMode: ").append(toIndentedString(eventPublishSubscriptionMode)).append("\n");
    sb.append("    eventPublishTopicFormatMqttEnabled: ").append(toIndentedString(eventPublishTopicFormatMqttEnabled)).append("\n");
    sb.append("    eventPublishTopicFormatSmfEnabled: ").append(toIndentedString(eventPublishTopicFormatSmfEnabled)).append("\n");
    sb.append("    eventServiceAmqpConnectionCountThreshold: ").append(toIndentedString(eventServiceAmqpConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceMqttConnectionCountThreshold: ").append(toIndentedString(eventServiceMqttConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceRestIncomingConnectionCountThreshold: ").append(toIndentedString(eventServiceRestIncomingConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceSmfConnectionCountThreshold: ").append(toIndentedString(eventServiceSmfConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceWebConnectionCountThreshold: ").append(toIndentedString(eventServiceWebConnectionCountThreshold)).append("\n");
    sb.append("    eventSubscriptionCountThreshold: ").append(toIndentedString(eventSubscriptionCountThreshold)).append("\n");
    sb.append("    eventTransactedSessionCountThreshold: ").append(toIndentedString(eventTransactedSessionCountThreshold)).append("\n");
    sb.append("    eventTransactionCountThreshold: ").append(toIndentedString(eventTransactionCountThreshold)).append("\n");
    sb.append("    exportSubscriptionsEnabled: ").append(toIndentedString(exportSubscriptionsEnabled)).append("\n");
    sb.append("    jndiEnabled: ").append(toIndentedString(jndiEnabled)).append("\n");
    sb.append("    maxConnectionCount: ").append(toIndentedString(maxConnectionCount)).append("\n");
    sb.append("    maxEgressFlowCount: ").append(toIndentedString(maxEgressFlowCount)).append("\n");
    sb.append("    maxEndpointCount: ").append(toIndentedString(maxEndpointCount)).append("\n");
    sb.append("    maxIngressFlowCount: ").append(toIndentedString(maxIngressFlowCount)).append("\n");
    sb.append("    maxMsgSpoolUsage: ").append(toIndentedString(maxMsgSpoolUsage)).append("\n");
    sb.append("    maxSubscriptionCount: ").append(toIndentedString(maxSubscriptionCount)).append("\n");
    sb.append("    maxTransactedSessionCount: ").append(toIndentedString(maxTransactedSessionCount)).append("\n");
    sb.append("    maxTransactionCount: ").append(toIndentedString(maxTransactionCount)).append("\n");
    sb.append("    mqttRetainMaxMemory: ").append(toIndentedString(mqttRetainMaxMemory)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    replicationAckPropagationIntervalMsgCount: ").append(toIndentedString(replicationAckPropagationIntervalMsgCount)).append("\n");
    sb.append("    replicationBridgeAuthenticationBasicClientUsername: ").append(toIndentedString(replicationBridgeAuthenticationBasicClientUsername)).append("\n");
    sb.append("    replicationBridgeAuthenticationBasicPassword: ").append(toIndentedString(replicationBridgeAuthenticationBasicPassword)).append("\n");
    sb.append("    replicationBridgeAuthenticationClientCertContent: ").append(toIndentedString(replicationBridgeAuthenticationClientCertContent)).append("\n");
    sb.append("    replicationBridgeAuthenticationClientCertPassword: ").append(toIndentedString(replicationBridgeAuthenticationClientCertPassword)).append("\n");
    sb.append("    replicationBridgeAuthenticationScheme: ").append(toIndentedString(replicationBridgeAuthenticationScheme)).append("\n");
    sb.append("    replicationBridgeCompressedDataEnabled: ").append(toIndentedString(replicationBridgeCompressedDataEnabled)).append("\n");
    sb.append("    replicationBridgeEgressFlowWindowSize: ").append(toIndentedString(replicationBridgeEgressFlowWindowSize)).append("\n");
    sb.append("    replicationBridgeRetryDelay: ").append(toIndentedString(replicationBridgeRetryDelay)).append("\n");
    sb.append("    replicationBridgeTlsEnabled: ").append(toIndentedString(replicationBridgeTlsEnabled)).append("\n");
    sb.append("    replicationBridgeUnidirectionalClientProfileName: ").append(toIndentedString(replicationBridgeUnidirectionalClientProfileName)).append("\n");
    sb.append("    replicationEnabled: ").append(toIndentedString(replicationEnabled)).append("\n");
    sb.append("    replicationEnabledQueueBehavior: ").append(toIndentedString(replicationEnabledQueueBehavior)).append("\n");
    sb.append("    replicationQueueMaxMsgSpoolUsage: ").append(toIndentedString(replicationQueueMaxMsgSpoolUsage)).append("\n");
    sb.append("    replicationQueueRejectMsgToSenderOnDiscardEnabled: ").append(toIndentedString(replicationQueueRejectMsgToSenderOnDiscardEnabled)).append("\n");
    sb.append("    replicationRejectMsgWhenSyncIneligibleEnabled: ").append(toIndentedString(replicationRejectMsgWhenSyncIneligibleEnabled)).append("\n");
    sb.append("    replicationRole: ").append(toIndentedString(replicationRole)).append("\n");
    sb.append("    replicationTransactionMode: ").append(toIndentedString(replicationTransactionMode)).append("\n");
    sb.append("    restTlsServerCertEnforceTrustedCommonNameEnabled: ").append(toIndentedString(restTlsServerCertEnforceTrustedCommonNameEnabled)).append("\n");
    sb.append("    restTlsServerCertMaxChainDepth: ").append(toIndentedString(restTlsServerCertMaxChainDepth)).append("\n");
    sb.append("    restTlsServerCertValidateDateEnabled: ").append(toIndentedString(restTlsServerCertValidateDateEnabled)).append("\n");
    sb.append("    restTlsServerCertValidateNameEnabled: ").append(toIndentedString(restTlsServerCertValidateNameEnabled)).append("\n");
    sb.append("    sempOverMsgBusAdminClientEnabled: ").append(toIndentedString(sempOverMsgBusAdminClientEnabled)).append("\n");
    sb.append("    sempOverMsgBusAdminDistributedCacheEnabled: ").append(toIndentedString(sempOverMsgBusAdminDistributedCacheEnabled)).append("\n");
    sb.append("    sempOverMsgBusAdminEnabled: ").append(toIndentedString(sempOverMsgBusAdminEnabled)).append("\n");
    sb.append("    sempOverMsgBusEnabled: ").append(toIndentedString(sempOverMsgBusEnabled)).append("\n");
    sb.append("    sempOverMsgBusShowEnabled: ").append(toIndentedString(sempOverMsgBusShowEnabled)).append("\n");
    sb.append("    serviceAmqpMaxConnectionCount: ").append(toIndentedString(serviceAmqpMaxConnectionCount)).append("\n");
    sb.append("    serviceAmqpPlainTextEnabled: ").append(toIndentedString(serviceAmqpPlainTextEnabled)).append("\n");
    sb.append("    serviceAmqpPlainTextListenPort: ").append(toIndentedString(serviceAmqpPlainTextListenPort)).append("\n");
    sb.append("    serviceAmqpTlsEnabled: ").append(toIndentedString(serviceAmqpTlsEnabled)).append("\n");
    sb.append("    serviceAmqpTlsListenPort: ").append(toIndentedString(serviceAmqpTlsListenPort)).append("\n");
    sb.append("    serviceMqttAuthenticationClientCertRequest: ").append(toIndentedString(serviceMqttAuthenticationClientCertRequest)).append("\n");
    sb.append("    serviceMqttMaxConnectionCount: ").append(toIndentedString(serviceMqttMaxConnectionCount)).append("\n");
    sb.append("    serviceMqttPlainTextEnabled: ").append(toIndentedString(serviceMqttPlainTextEnabled)).append("\n");
    sb.append("    serviceMqttPlainTextListenPort: ").append(toIndentedString(serviceMqttPlainTextListenPort)).append("\n");
    sb.append("    serviceMqttTlsEnabled: ").append(toIndentedString(serviceMqttTlsEnabled)).append("\n");
    sb.append("    serviceMqttTlsListenPort: ").append(toIndentedString(serviceMqttTlsListenPort)).append("\n");
    sb.append("    serviceMqttTlsWebSocketEnabled: ").append(toIndentedString(serviceMqttTlsWebSocketEnabled)).append("\n");
    sb.append("    serviceMqttTlsWebSocketListenPort: ").append(toIndentedString(serviceMqttTlsWebSocketListenPort)).append("\n");
    sb.append("    serviceMqttWebSocketEnabled: ").append(toIndentedString(serviceMqttWebSocketEnabled)).append("\n");
    sb.append("    serviceMqttWebSocketListenPort: ").append(toIndentedString(serviceMqttWebSocketListenPort)).append("\n");
    sb.append("    serviceRestIncomingAuthenticationClientCertRequest: ").append(toIndentedString(serviceRestIncomingAuthenticationClientCertRequest)).append("\n");
    sb.append("    serviceRestIncomingAuthorizationHeaderHandling: ").append(toIndentedString(serviceRestIncomingAuthorizationHeaderHandling)).append("\n");
    sb.append("    serviceRestIncomingMaxConnectionCount: ").append(toIndentedString(serviceRestIncomingMaxConnectionCount)).append("\n");
    sb.append("    serviceRestIncomingPlainTextEnabled: ").append(toIndentedString(serviceRestIncomingPlainTextEnabled)).append("\n");
    sb.append("    serviceRestIncomingPlainTextListenPort: ").append(toIndentedString(serviceRestIncomingPlainTextListenPort)).append("\n");
    sb.append("    serviceRestIncomingTlsEnabled: ").append(toIndentedString(serviceRestIncomingTlsEnabled)).append("\n");
    sb.append("    serviceRestIncomingTlsListenPort: ").append(toIndentedString(serviceRestIncomingTlsListenPort)).append("\n");
    sb.append("    serviceRestMode: ").append(toIndentedString(serviceRestMode)).append("\n");
    sb.append("    serviceRestOutgoingMaxConnectionCount: ").append(toIndentedString(serviceRestOutgoingMaxConnectionCount)).append("\n");
    sb.append("    serviceSmfMaxConnectionCount: ").append(toIndentedString(serviceSmfMaxConnectionCount)).append("\n");
    sb.append("    serviceSmfPlainTextEnabled: ").append(toIndentedString(serviceSmfPlainTextEnabled)).append("\n");
    sb.append("    serviceSmfTlsEnabled: ").append(toIndentedString(serviceSmfTlsEnabled)).append("\n");
    sb.append("    serviceWebAuthenticationClientCertRequest: ").append(toIndentedString(serviceWebAuthenticationClientCertRequest)).append("\n");
    sb.append("    serviceWebMaxConnectionCount: ").append(toIndentedString(serviceWebMaxConnectionCount)).append("\n");
    sb.append("    serviceWebPlainTextEnabled: ").append(toIndentedString(serviceWebPlainTextEnabled)).append("\n");
    sb.append("    serviceWebTlsEnabled: ").append(toIndentedString(serviceWebTlsEnabled)).append("\n");
    sb.append("    tlsAllowDowngradeToPlainTextEnabled: ").append(toIndentedString(tlsAllowDowngradeToPlainTextEnabled)).append("\n");
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

