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
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.model.EventThresholdByPercent;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Broker
 */
@JsonPropertyOrder({
  Broker.JSON_PROPERTY_AUTH_CLIENT_CERT_REVOCATION_CHECK_MODE,
  Broker.JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH,
  Broker.JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED,
  Broker.JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_INITIAL_CONGESTION_WINDOW,
  Broker.JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT,
  Broker.JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE,
  Broker.JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL,
  Broker.JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MAX_WINDOW,
  Broker.JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MSS,
  Broker.JSON_PROPERTY_CONFIG_SYNC_ENABLED,
  Broker.JSON_PROPERTY_CONFIG_SYNC_SYNCHRONIZE_USERNAME_ENABLED,
  Broker.JSON_PROPERTY_CONFIG_SYNC_TLS_ENABLED,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_DAY_LIST,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_ENABLED,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_TIME_LIST,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_ENABLED,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_FRAGMENTATION_PERCENTAGE,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_MIN_INTERVAL,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_USAGE_PERCENTAGE,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_ENABLED,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_CACHE_USAGE_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DELIVERED_UNACKED_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DISK_USAGE_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_EGRESS_FLOW_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_ENDPOINT_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_INGRESS_FLOW_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_FILE_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_USAGE_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_RESOURCE_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTION_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_MAX_CACHE_USAGE,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_MAX_MSG_SPOOL_USAGE,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_MSG_ACK_TIMEOUT,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_SPOOL_FILE_ACK_TIMEOUT,
  Broker.JSON_PROPERTY_GUARANTEED_MSGING_TRANSACTION_REPLICATION_COMPATIBILITY_MODE,
  Broker.JSON_PROPERTY_OAUTH_PROFILE_DEFAULT,
  Broker.JSON_PROPERTY_SERVICE_AMQP_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_AMQP_TLS_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_EVENT_CONNECTION_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_SERVICE_HEALTH_CHECK_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_HEALTH_CHECK_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_MATE_LINK_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_MATE_LINK_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_MQTT_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_MSG_BACKBONE_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_REDUNDANCY_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_REDUNDANCY_FIRST_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_REST_EVENT_OUTGOING_CONNECTION_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_SERVICE_REST_INCOMING_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_REST_OUTGOING_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_SEMP_CORS_ALLOW_ANY_HOST_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_SEMP_LEGACY_TIMEOUT_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_SEMP_SESSION_IDLE_TIMEOUT,
  Broker.JSON_PROPERTY_SERVICE_SEMP_SESSION_MAX_LIFETIME,
  Broker.JSON_PROPERTY_SERVICE_SEMP_TLS_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_SEMP_TLS_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_SMF_COMPRESSION_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_SMF_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_SMF_EVENT_CONNECTION_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_SMF_ROUTING_CONTROL_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_SMF_TLS_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_TLS_EVENT_CONNECTION_COUNT_THRESHOLD,
  Broker.JSON_PROPERTY_SERVICE_WEB_TRANSPORT_ENABLED,
  Broker.JSON_PROPERTY_SERVICE_WEB_TRANSPORT_PLAIN_TEXT_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_WEB_TRANSPORT_TLS_LISTEN_PORT,
  Broker.JSON_PROPERTY_SERVICE_WEB_TRANSPORT_WEB_URL_SUFFIX,
  Broker.JSON_PROPERTY_TLS_BLOCK_VERSION11_ENABLED,
  Broker.JSON_PROPERTY_TLS_CIPHER_SUITE_MANAGEMENT_LIST,
  Broker.JSON_PROPERTY_TLS_CIPHER_SUITE_MSG_BACKBONE_LIST,
  Broker.JSON_PROPERTY_TLS_CIPHER_SUITE_SECURE_SHELL_LIST,
  Broker.JSON_PROPERTY_TLS_CRIME_EXPLOIT_PROTECTION_ENABLED,
  Broker.JSON_PROPERTY_TLS_SERVER_CERT_CONTENT,
  Broker.JSON_PROPERTY_TLS_SERVER_CERT_PASSWORD,
  Broker.JSON_PROPERTY_TLS_STANDARD_DOMAIN_CERTIFICATE_AUTHORITIES_ENABLED,
  Broker.JSON_PROPERTY_TLS_TICKET_LIFETIME,
  Broker.JSON_PROPERTY_WEB_MANAGER_ALLOW_UNENCRYPTED_WIZARDS_ENABLED,
  Broker.JSON_PROPERTY_WEB_MANAGER_CUSTOMIZATION,
  Broker.JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_ENABLED,
  Broker.JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_OVERRIDE_TLS_PORT
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class Broker {
  /**
   * The client certificate revocation checking mode used when a client authenticates with a client certificate. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Do not perform any certificate revocation checking. \&quot;ocsp\&quot; - Use the Open Certificate Status Protcol (OCSP) for certificate revocation checking. \&quot;crl\&quot; - Use Certificate Revocation Lists (CRL) for certificate revocation checking. \&quot;ocsp-crl\&quot; - Use OCSP first, but if OCSP fails to return an unambiguous result, then check via CRL. &lt;/pre&gt; 
   */
  public enum AuthClientCertRevocationCheckModeEnum {
    NONE("none"),
    
    OCSP("ocsp"),
    
    CRL("crl"),
    
    OCSP_CRL("ocsp-crl");

    private String value;

    AuthClientCertRevocationCheckModeEnum(String value) {
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
    public static AuthClientCertRevocationCheckModeEnum fromValue(String value) {
      for (AuthClientCertRevocationCheckModeEnum b : AuthClientCertRevocationCheckModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_AUTH_CLIENT_CERT_REVOCATION_CHECK_MODE = "authClientCertRevocationCheckMode";
  private AuthClientCertRevocationCheckModeEnum authClientCertRevocationCheckMode;

  public static final String JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH = "configSyncAuthenticationClientCertMaxChainDepth";
  private Long configSyncAuthenticationClientCertMaxChainDepth;

  public static final String JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED = "configSyncAuthenticationClientCertValidateDateEnabled";
  private Boolean configSyncAuthenticationClientCertValidateDateEnabled;

  public static final String JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_INITIAL_CONGESTION_WINDOW = "configSyncClientProfileTcpInitialCongestionWindow";
  private Long configSyncClientProfileTcpInitialCongestionWindow;

  public static final String JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT = "configSyncClientProfileTcpKeepaliveCount";
  private Long configSyncClientProfileTcpKeepaliveCount;

  public static final String JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE = "configSyncClientProfileTcpKeepaliveIdle";
  private Long configSyncClientProfileTcpKeepaliveIdle;

  public static final String JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL = "configSyncClientProfileTcpKeepaliveInterval";
  private Long configSyncClientProfileTcpKeepaliveInterval;

  public static final String JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MAX_WINDOW = "configSyncClientProfileTcpMaxWindow";
  private Long configSyncClientProfileTcpMaxWindow;

  public static final String JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MSS = "configSyncClientProfileTcpMss";
  private Long configSyncClientProfileTcpMss;

  public static final String JSON_PROPERTY_CONFIG_SYNC_ENABLED = "configSyncEnabled";
  private Boolean configSyncEnabled;

  public static final String JSON_PROPERTY_CONFIG_SYNC_SYNCHRONIZE_USERNAME_ENABLED = "configSyncSynchronizeUsernameEnabled";
  private Boolean configSyncSynchronizeUsernameEnabled;

  public static final String JSON_PROPERTY_CONFIG_SYNC_TLS_ENABLED = "configSyncTlsEnabled";
  private Boolean configSyncTlsEnabled;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_DAY_LIST = "guaranteedMsgingDefragmentationScheduleDayList";
  private String guaranteedMsgingDefragmentationScheduleDayList;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_ENABLED = "guaranteedMsgingDefragmentationScheduleEnabled";
  private Boolean guaranteedMsgingDefragmentationScheduleEnabled;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_TIME_LIST = "guaranteedMsgingDefragmentationScheduleTimeList";
  private String guaranteedMsgingDefragmentationScheduleTimeList;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_ENABLED = "guaranteedMsgingDefragmentationThresholdEnabled";
  private Boolean guaranteedMsgingDefragmentationThresholdEnabled;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_FRAGMENTATION_PERCENTAGE = "guaranteedMsgingDefragmentationThresholdFragmentationPercentage";
  private Long guaranteedMsgingDefragmentationThresholdFragmentationPercentage;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_MIN_INTERVAL = "guaranteedMsgingDefragmentationThresholdMinInterval";
  private Long guaranteedMsgingDefragmentationThresholdMinInterval;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_USAGE_PERCENTAGE = "guaranteedMsgingDefragmentationThresholdUsagePercentage";
  private Long guaranteedMsgingDefragmentationThresholdUsagePercentage;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_ENABLED = "guaranteedMsgingEnabled";
  private Boolean guaranteedMsgingEnabled;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_CACHE_USAGE_THRESHOLD = "guaranteedMsgingEventCacheUsageThreshold";
  private EventThreshold guaranteedMsgingEventCacheUsageThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DELIVERED_UNACKED_THRESHOLD = "guaranteedMsgingEventDeliveredUnackedThreshold";
  private EventThresholdByPercent guaranteedMsgingEventDeliveredUnackedThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DISK_USAGE_THRESHOLD = "guaranteedMsgingEventDiskUsageThreshold";
  private EventThresholdByPercent guaranteedMsgingEventDiskUsageThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_EGRESS_FLOW_COUNT_THRESHOLD = "guaranteedMsgingEventEgressFlowCountThreshold";
  private EventThreshold guaranteedMsgingEventEgressFlowCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_ENDPOINT_COUNT_THRESHOLD = "guaranteedMsgingEventEndpointCountThreshold";
  private EventThreshold guaranteedMsgingEventEndpointCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_INGRESS_FLOW_COUNT_THRESHOLD = "guaranteedMsgingEventIngressFlowCountThreshold";
  private EventThreshold guaranteedMsgingEventIngressFlowCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_COUNT_THRESHOLD = "guaranteedMsgingEventMsgCountThreshold";
  private EventThresholdByPercent guaranteedMsgingEventMsgCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_FILE_COUNT_THRESHOLD = "guaranteedMsgingEventMsgSpoolFileCountThreshold";
  private EventThresholdByPercent guaranteedMsgingEventMsgSpoolFileCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_USAGE_THRESHOLD = "guaranteedMsgingEventMsgSpoolUsageThreshold";
  private EventThreshold guaranteedMsgingEventMsgSpoolUsageThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD = "guaranteedMsgingEventTransactedSessionCountThreshold";
  private EventThreshold guaranteedMsgingEventTransactedSessionCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_RESOURCE_COUNT_THRESHOLD = "guaranteedMsgingEventTransactedSessionResourceCountThreshold";
  private EventThresholdByPercent guaranteedMsgingEventTransactedSessionResourceCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTION_COUNT_THRESHOLD = "guaranteedMsgingEventTransactionCountThreshold";
  private EventThreshold guaranteedMsgingEventTransactionCountThreshold;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_MAX_CACHE_USAGE = "guaranteedMsgingMaxCacheUsage";
  private Integer guaranteedMsgingMaxCacheUsage;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_MAX_MSG_SPOOL_USAGE = "guaranteedMsgingMaxMsgSpoolUsage";
  private Long guaranteedMsgingMaxMsgSpoolUsage;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_MSG_ACK_TIMEOUT = "guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout";
  private Long guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout;

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_SPOOL_FILE_ACK_TIMEOUT = "guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout";
  private Long guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout;

  /**
   * The replication compatibility mode for the router. The default value is &#x60;\&quot;legacy\&quot;&#x60;. The allowed values and their meaning are:\&quot;legacy\&quot; - All transactions originated by clients are replicated to the standby site without using transactions.\&quot;transacted\&quot; - All transactions originated by clients are replicated to the standby site using transactions. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;legacy\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;legacy\&quot; - All transactions originated by clients are replicated to the standby site without using transactions. \&quot;transacted\&quot; - All transactions originated by clients are replicated to the standby site using transactions. &lt;/pre&gt;  Available since 2.18.
   */
  public enum GuaranteedMsgingTransactionReplicationCompatibilityModeEnum {
    LEGACY("legacy"),
    
    TRANSACTED("transacted");

    private String value;

    GuaranteedMsgingTransactionReplicationCompatibilityModeEnum(String value) {
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
    public static GuaranteedMsgingTransactionReplicationCompatibilityModeEnum fromValue(String value) {
      for (GuaranteedMsgingTransactionReplicationCompatibilityModeEnum b : GuaranteedMsgingTransactionReplicationCompatibilityModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_GUARANTEED_MSGING_TRANSACTION_REPLICATION_COMPATIBILITY_MODE = "guaranteedMsgingTransactionReplicationCompatibilityMode";
  private GuaranteedMsgingTransactionReplicationCompatibilityModeEnum guaranteedMsgingTransactionReplicationCompatibilityMode;

  public static final String JSON_PROPERTY_OAUTH_PROFILE_DEFAULT = "oauthProfileDefault";
  private String oauthProfileDefault;

  public static final String JSON_PROPERTY_SERVICE_AMQP_ENABLED = "serviceAmqpEnabled";
  private Boolean serviceAmqpEnabled;

  public static final String JSON_PROPERTY_SERVICE_AMQP_TLS_LISTEN_PORT = "serviceAmqpTlsListenPort";
  private Long serviceAmqpTlsListenPort;

  public static final String JSON_PROPERTY_SERVICE_EVENT_CONNECTION_COUNT_THRESHOLD = "serviceEventConnectionCountThreshold";
  private EventThreshold serviceEventConnectionCountThreshold;

  public static final String JSON_PROPERTY_SERVICE_HEALTH_CHECK_ENABLED = "serviceHealthCheckEnabled";
  private Boolean serviceHealthCheckEnabled;

  public static final String JSON_PROPERTY_SERVICE_HEALTH_CHECK_LISTEN_PORT = "serviceHealthCheckListenPort";
  private Long serviceHealthCheckListenPort;

  public static final String JSON_PROPERTY_SERVICE_MATE_LINK_ENABLED = "serviceMateLinkEnabled";
  private Boolean serviceMateLinkEnabled;

  public static final String JSON_PROPERTY_SERVICE_MATE_LINK_LISTEN_PORT = "serviceMateLinkListenPort";
  private Long serviceMateLinkListenPort;

  public static final String JSON_PROPERTY_SERVICE_MQTT_ENABLED = "serviceMqttEnabled";
  private Boolean serviceMqttEnabled;

  public static final String JSON_PROPERTY_SERVICE_MSG_BACKBONE_ENABLED = "serviceMsgBackboneEnabled";
  private Boolean serviceMsgBackboneEnabled;

  public static final String JSON_PROPERTY_SERVICE_REDUNDANCY_ENABLED = "serviceRedundancyEnabled";
  private Boolean serviceRedundancyEnabled;

  public static final String JSON_PROPERTY_SERVICE_REDUNDANCY_FIRST_LISTEN_PORT = "serviceRedundancyFirstListenPort";
  private Long serviceRedundancyFirstListenPort;

  public static final String JSON_PROPERTY_SERVICE_REST_EVENT_OUTGOING_CONNECTION_COUNT_THRESHOLD = "serviceRestEventOutgoingConnectionCountThreshold";
  private EventThreshold serviceRestEventOutgoingConnectionCountThreshold;

  public static final String JSON_PROPERTY_SERVICE_REST_INCOMING_ENABLED = "serviceRestIncomingEnabled";
  private Boolean serviceRestIncomingEnabled;

  public static final String JSON_PROPERTY_SERVICE_REST_OUTGOING_ENABLED = "serviceRestOutgoingEnabled";
  private Boolean serviceRestOutgoingEnabled;

  public static final String JSON_PROPERTY_SERVICE_SEMP_CORS_ALLOW_ANY_HOST_ENABLED = "serviceSempCorsAllowAnyHostEnabled";
  private Boolean serviceSempCorsAllowAnyHostEnabled;

  public static final String JSON_PROPERTY_SERVICE_SEMP_LEGACY_TIMEOUT_ENABLED = "serviceSempLegacyTimeoutEnabled";
  private Boolean serviceSempLegacyTimeoutEnabled;

  public static final String JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_ENABLED = "serviceSempPlainTextEnabled";
  private Boolean serviceSempPlainTextEnabled;

  public static final String JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_LISTEN_PORT = "serviceSempPlainTextListenPort";
  private Long serviceSempPlainTextListenPort;

  public static final String JSON_PROPERTY_SERVICE_SEMP_SESSION_IDLE_TIMEOUT = "serviceSempSessionIdleTimeout";
  private Integer serviceSempSessionIdleTimeout;

  public static final String JSON_PROPERTY_SERVICE_SEMP_SESSION_MAX_LIFETIME = "serviceSempSessionMaxLifetime";
  private Integer serviceSempSessionMaxLifetime;

  public static final String JSON_PROPERTY_SERVICE_SEMP_TLS_ENABLED = "serviceSempTlsEnabled";
  private Boolean serviceSempTlsEnabled;

  public static final String JSON_PROPERTY_SERVICE_SEMP_TLS_LISTEN_PORT = "serviceSempTlsListenPort";
  private Long serviceSempTlsListenPort;

  public static final String JSON_PROPERTY_SERVICE_SMF_COMPRESSION_LISTEN_PORT = "serviceSmfCompressionListenPort";
  private Long serviceSmfCompressionListenPort;

  public static final String JSON_PROPERTY_SERVICE_SMF_ENABLED = "serviceSmfEnabled";
  private Boolean serviceSmfEnabled;

  public static final String JSON_PROPERTY_SERVICE_SMF_EVENT_CONNECTION_COUNT_THRESHOLD = "serviceSmfEventConnectionCountThreshold";
  private EventThreshold serviceSmfEventConnectionCountThreshold;

  public static final String JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_LISTEN_PORT = "serviceSmfPlainTextListenPort";
  private Long serviceSmfPlainTextListenPort;

  public static final String JSON_PROPERTY_SERVICE_SMF_ROUTING_CONTROL_LISTEN_PORT = "serviceSmfRoutingControlListenPort";
  private Long serviceSmfRoutingControlListenPort;

  public static final String JSON_PROPERTY_SERVICE_SMF_TLS_LISTEN_PORT = "serviceSmfTlsListenPort";
  private Long serviceSmfTlsListenPort;

  public static final String JSON_PROPERTY_SERVICE_TLS_EVENT_CONNECTION_COUNT_THRESHOLD = "serviceTlsEventConnectionCountThreshold";
  private EventThreshold serviceTlsEventConnectionCountThreshold;

  public static final String JSON_PROPERTY_SERVICE_WEB_TRANSPORT_ENABLED = "serviceWebTransportEnabled";
  private Boolean serviceWebTransportEnabled;

  public static final String JSON_PROPERTY_SERVICE_WEB_TRANSPORT_PLAIN_TEXT_LISTEN_PORT = "serviceWebTransportPlainTextListenPort";
  private Long serviceWebTransportPlainTextListenPort;

  public static final String JSON_PROPERTY_SERVICE_WEB_TRANSPORT_TLS_LISTEN_PORT = "serviceWebTransportTlsListenPort";
  private Long serviceWebTransportTlsListenPort;

  public static final String JSON_PROPERTY_SERVICE_WEB_TRANSPORT_WEB_URL_SUFFIX = "serviceWebTransportWebUrlSuffix";
  private String serviceWebTransportWebUrlSuffix;

  public static final String JSON_PROPERTY_TLS_BLOCK_VERSION11_ENABLED = "tlsBlockVersion11Enabled";
  private Boolean tlsBlockVersion11Enabled;

  public static final String JSON_PROPERTY_TLS_CIPHER_SUITE_MANAGEMENT_LIST = "tlsCipherSuiteManagementList";
  private String tlsCipherSuiteManagementList;

  public static final String JSON_PROPERTY_TLS_CIPHER_SUITE_MSG_BACKBONE_LIST = "tlsCipherSuiteMsgBackboneList";
  private String tlsCipherSuiteMsgBackboneList;

  public static final String JSON_PROPERTY_TLS_CIPHER_SUITE_SECURE_SHELL_LIST = "tlsCipherSuiteSecureShellList";
  private String tlsCipherSuiteSecureShellList;

  public static final String JSON_PROPERTY_TLS_CRIME_EXPLOIT_PROTECTION_ENABLED = "tlsCrimeExploitProtectionEnabled";
  private Boolean tlsCrimeExploitProtectionEnabled;

  public static final String JSON_PROPERTY_TLS_SERVER_CERT_CONTENT = "tlsServerCertContent";
  private String tlsServerCertContent;

  public static final String JSON_PROPERTY_TLS_SERVER_CERT_PASSWORD = "tlsServerCertPassword";
  private String tlsServerCertPassword;

  public static final String JSON_PROPERTY_TLS_STANDARD_DOMAIN_CERTIFICATE_AUTHORITIES_ENABLED = "tlsStandardDomainCertificateAuthoritiesEnabled";
  private Boolean tlsStandardDomainCertificateAuthoritiesEnabled;

  public static final String JSON_PROPERTY_TLS_TICKET_LIFETIME = "tlsTicketLifetime";
  private Integer tlsTicketLifetime;

  public static final String JSON_PROPERTY_WEB_MANAGER_ALLOW_UNENCRYPTED_WIZARDS_ENABLED = "webManagerAllowUnencryptedWizardsEnabled";
  private Boolean webManagerAllowUnencryptedWizardsEnabled;

  public static final String JSON_PROPERTY_WEB_MANAGER_CUSTOMIZATION = "webManagerCustomization";
  private String webManagerCustomization;

  public static final String JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_ENABLED = "webManagerRedirectHttpEnabled";
  private Boolean webManagerRedirectHttpEnabled;

  public static final String JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_OVERRIDE_TLS_PORT = "webManagerRedirectHttpOverrideTlsPort";
  private Integer webManagerRedirectHttpOverrideTlsPort;

  public Broker() {
  }

  public Broker authClientCertRevocationCheckMode(AuthClientCertRevocationCheckModeEnum authClientCertRevocationCheckMode) {
    
    this.authClientCertRevocationCheckMode = authClientCertRevocationCheckMode;
    return this;
  }

   /**
   * The client certificate revocation checking mode used when a client authenticates with a client certificate. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Do not perform any certificate revocation checking. \&quot;ocsp\&quot; - Use the Open Certificate Status Protcol (OCSP) for certificate revocation checking. \&quot;crl\&quot; - Use Certificate Revocation Lists (CRL) for certificate revocation checking. \&quot;ocsp-crl\&quot; - Use OCSP first, but if OCSP fails to return an unambiguous result, then check via CRL. &lt;/pre&gt; 
   * @return authClientCertRevocationCheckMode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTH_CLIENT_CERT_REVOCATION_CHECK_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AuthClientCertRevocationCheckModeEnum getAuthClientCertRevocationCheckMode() {
    return authClientCertRevocationCheckMode;
  }


  @JsonProperty(JSON_PROPERTY_AUTH_CLIENT_CERT_REVOCATION_CHECK_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthClientCertRevocationCheckMode(AuthClientCertRevocationCheckModeEnum authClientCertRevocationCheckMode) {
    this.authClientCertRevocationCheckMode = authClientCertRevocationCheckMode;
  }


  public Broker configSyncAuthenticationClientCertMaxChainDepth(Long configSyncAuthenticationClientCertMaxChainDepth) {
    
    this.configSyncAuthenticationClientCertMaxChainDepth = configSyncAuthenticationClientCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is &#x60;3&#x60;. Available since 2.22.
   * @return configSyncAuthenticationClientCertMaxChainDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncAuthenticationClientCertMaxChainDepth() {
    return configSyncAuthenticationClientCertMaxChainDepth;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncAuthenticationClientCertMaxChainDepth(Long configSyncAuthenticationClientCertMaxChainDepth) {
    this.configSyncAuthenticationClientCertMaxChainDepth = configSyncAuthenticationClientCertMaxChainDepth;
  }


  public Broker configSyncAuthenticationClientCertValidateDateEnabled(Boolean configSyncAuthenticationClientCertValidateDateEnabled) {
    
    this.configSyncAuthenticationClientCertValidateDateEnabled = configSyncAuthenticationClientCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the authentication certificate(s). The default value is &#x60;true&#x60;. Available since 2.22.
   * @return configSyncAuthenticationClientCertValidateDateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getConfigSyncAuthenticationClientCertValidateDateEnabled() {
    return configSyncAuthenticationClientCertValidateDateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_AUTHENTICATION_CLIENT_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncAuthenticationClientCertValidateDateEnabled(Boolean configSyncAuthenticationClientCertValidateDateEnabled) {
    this.configSyncAuthenticationClientCertValidateDateEnabled = configSyncAuthenticationClientCertValidateDateEnabled;
  }


  public Broker configSyncClientProfileTcpInitialCongestionWindow(Long configSyncClientProfileTcpInitialCongestionWindow) {
    
    this.configSyncClientProfileTcpInitialCongestionWindow = configSyncClientProfileTcpInitialCongestionWindow;
    return this;
  }

   /**
   * The TCP initial congestion window size for Config Sync clients, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact support before changing this value. The default value is &#x60;2&#x60;. Available since 2.22.
   * @return configSyncClientProfileTcpInitialCongestionWindow
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_INITIAL_CONGESTION_WINDOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncClientProfileTcpInitialCongestionWindow() {
    return configSyncClientProfileTcpInitialCongestionWindow;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_INITIAL_CONGESTION_WINDOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncClientProfileTcpInitialCongestionWindow(Long configSyncClientProfileTcpInitialCongestionWindow) {
    this.configSyncClientProfileTcpInitialCongestionWindow = configSyncClientProfileTcpInitialCongestionWindow;
  }


  public Broker configSyncClientProfileTcpKeepaliveCount(Long configSyncClientProfileTcpKeepaliveCount) {
    
    this.configSyncClientProfileTcpKeepaliveCount = configSyncClientProfileTcpKeepaliveCount;
    return this;
  }

   /**
   * The number of TCP keepalive retransmissions to a client using the Client Profile before declaring that it is not available. The default value is &#x60;5&#x60;. Available since 2.22.
   * @return configSyncClientProfileTcpKeepaliveCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncClientProfileTcpKeepaliveCount() {
    return configSyncClientProfileTcpKeepaliveCount;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncClientProfileTcpKeepaliveCount(Long configSyncClientProfileTcpKeepaliveCount) {
    this.configSyncClientProfileTcpKeepaliveCount = configSyncClientProfileTcpKeepaliveCount;
  }


  public Broker configSyncClientProfileTcpKeepaliveIdle(Long configSyncClientProfileTcpKeepaliveIdle) {
    
    this.configSyncClientProfileTcpKeepaliveIdle = configSyncClientProfileTcpKeepaliveIdle;
    return this;
  }

   /**
   * The amount of time a client connection using the Client Profile must remain idle before TCP begins sending keepalive probes, in seconds. The default value is &#x60;3&#x60;. Available since 2.22.
   * @return configSyncClientProfileTcpKeepaliveIdle
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncClientProfileTcpKeepaliveIdle() {
    return configSyncClientProfileTcpKeepaliveIdle;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncClientProfileTcpKeepaliveIdle(Long configSyncClientProfileTcpKeepaliveIdle) {
    this.configSyncClientProfileTcpKeepaliveIdle = configSyncClientProfileTcpKeepaliveIdle;
  }


  public Broker configSyncClientProfileTcpKeepaliveInterval(Long configSyncClientProfileTcpKeepaliveInterval) {
    
    this.configSyncClientProfileTcpKeepaliveInterval = configSyncClientProfileTcpKeepaliveInterval;
    return this;
  }

   /**
   * The amount of time between TCP keepalive retransmissions to a client using the Client Profile when no acknowledgement is received, in seconds. The default value is &#x60;1&#x60;. Available since 2.22.
   * @return configSyncClientProfileTcpKeepaliveInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncClientProfileTcpKeepaliveInterval() {
    return configSyncClientProfileTcpKeepaliveInterval;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncClientProfileTcpKeepaliveInterval(Long configSyncClientProfileTcpKeepaliveInterval) {
    this.configSyncClientProfileTcpKeepaliveInterval = configSyncClientProfileTcpKeepaliveInterval;
  }


  public Broker configSyncClientProfileTcpMaxWindow(Long configSyncClientProfileTcpMaxWindow) {
    
    this.configSyncClientProfileTcpMaxWindow = configSyncClientProfileTcpMaxWindow;
    return this;
  }

   /**
   * The TCP maximum window size for clients using the Client Profile, in kilobytes. Changes are applied to all existing connections. The default value is &#x60;256&#x60;. Available since 2.22.
   * @return configSyncClientProfileTcpMaxWindow
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MAX_WINDOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncClientProfileTcpMaxWindow() {
    return configSyncClientProfileTcpMaxWindow;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MAX_WINDOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncClientProfileTcpMaxWindow(Long configSyncClientProfileTcpMaxWindow) {
    this.configSyncClientProfileTcpMaxWindow = configSyncClientProfileTcpMaxWindow;
  }


  public Broker configSyncClientProfileTcpMss(Long configSyncClientProfileTcpMss) {
    
    this.configSyncClientProfileTcpMss = configSyncClientProfileTcpMss;
    return this;
  }

   /**
   * The TCP maximum segment size for clients using the Client Profile, in bytes. Changes are applied to all existing connections. The default value is &#x60;1460&#x60;. Available since 2.22.
   * @return configSyncClientProfileTcpMss
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MSS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getConfigSyncClientProfileTcpMss() {
    return configSyncClientProfileTcpMss;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_CLIENT_PROFILE_TCP_MSS)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncClientProfileTcpMss(Long configSyncClientProfileTcpMss) {
    this.configSyncClientProfileTcpMss = configSyncClientProfileTcpMss;
  }


  public Broker configSyncEnabled(Boolean configSyncEnabled) {
    
    this.configSyncEnabled = configSyncEnabled;
    return this;
  }

   /**
   * Enable or disable configuration synchronization for High Availability or Disaster Recovery. The default value is &#x60;false&#x60;. Available since 2.22.
   * @return configSyncEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getConfigSyncEnabled() {
    return configSyncEnabled;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncEnabled(Boolean configSyncEnabled) {
    this.configSyncEnabled = configSyncEnabled;
  }


  public Broker configSyncSynchronizeUsernameEnabled(Boolean configSyncSynchronizeUsernameEnabled) {
    
    this.configSyncSynchronizeUsernameEnabled = configSyncSynchronizeUsernameEnabled;
    return this;
  }

   /**
   * Enable or disable the synchronizing of usernames within High Availability groups. The transition from not synchronizing to synchronizing will cause the High Availability mate to fall out of sync. Recommendation: leave this as enabled. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.22.
   * @return configSyncSynchronizeUsernameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_SYNCHRONIZE_USERNAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getConfigSyncSynchronizeUsernameEnabled() {
    return configSyncSynchronizeUsernameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_SYNCHRONIZE_USERNAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncSynchronizeUsernameEnabled(Boolean configSyncSynchronizeUsernameEnabled) {
    this.configSyncSynchronizeUsernameEnabled = configSyncSynchronizeUsernameEnabled;
  }


  public Broker configSyncTlsEnabled(Boolean configSyncTlsEnabled) {
    
    this.configSyncTlsEnabled = configSyncTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of TLS encryption of the configuration synchronization communications between brokers in High Availability groups and/or Disaster Recovery sites. The default value is &#x60;false&#x60;. Available since 2.22.
   * @return configSyncTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getConfigSyncTlsEnabled() {
    return configSyncTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_CONFIG_SYNC_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConfigSyncTlsEnabled(Boolean configSyncTlsEnabled) {
    this.configSyncTlsEnabled = configSyncTlsEnabled;
  }


  public Broker guaranteedMsgingDefragmentationScheduleDayList(String guaranteedMsgingDefragmentationScheduleDayList) {
    
    this.guaranteedMsgingDefragmentationScheduleDayList = guaranteedMsgingDefragmentationScheduleDayList;
    return this;
  }

   /**
   * The days of the week to schedule defragmentation runs, specified as \&quot;daily\&quot; or as a comma-separated list of days. Days must be specified as \&quot;Sun\&quot;, \&quot;Mon\&quot;, \&quot;Tue\&quot;, \&quot;Wed\&quot;, \&quot;Thu\&quot;, \&quot;Fri, or \&quot;Sat\&quot;, with no spaces, and in sorted order from Sunday to Saturday. Please note \&quot;Sun,Mon,Tue,Wed,Thu,Fri,Sat\&quot; is not allowed, use \&quot;daily\&quot; instead. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;daily\&quot;&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationScheduleDayList
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_DAY_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getGuaranteedMsgingDefragmentationScheduleDayList() {
    return guaranteedMsgingDefragmentationScheduleDayList;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_DAY_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationScheduleDayList(String guaranteedMsgingDefragmentationScheduleDayList) {
    this.guaranteedMsgingDefragmentationScheduleDayList = guaranteedMsgingDefragmentationScheduleDayList;
  }


  public Broker guaranteedMsgingDefragmentationScheduleEnabled(Boolean guaranteedMsgingDefragmentationScheduleEnabled) {
    
    this.guaranteedMsgingDefragmentationScheduleEnabled = guaranteedMsgingDefragmentationScheduleEnabled;
    return this;
  }

   /**
   * Enable or disable schedule-based defragmentation of Guaranteed Messaging spool files. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationScheduleEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getGuaranteedMsgingDefragmentationScheduleEnabled() {
    return guaranteedMsgingDefragmentationScheduleEnabled;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationScheduleEnabled(Boolean guaranteedMsgingDefragmentationScheduleEnabled) {
    this.guaranteedMsgingDefragmentationScheduleEnabled = guaranteedMsgingDefragmentationScheduleEnabled;
  }


  public Broker guaranteedMsgingDefragmentationScheduleTimeList(String guaranteedMsgingDefragmentationScheduleTimeList) {
    
    this.guaranteedMsgingDefragmentationScheduleTimeList = guaranteedMsgingDefragmentationScheduleTimeList;
    return this;
  }

   /**
   * The times of the day to schedule defragmentation runs, specified as \&quot;hourly\&quot; or as a comma-separated list of 24-hour times in the form hh:mm, or h:mm. There must be no spaces, and times (up to 4) must be in sorted order from 0:00 to 23:59. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;0:00\&quot;&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationScheduleTimeList
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_TIME_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getGuaranteedMsgingDefragmentationScheduleTimeList() {
    return guaranteedMsgingDefragmentationScheduleTimeList;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_SCHEDULE_TIME_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationScheduleTimeList(String guaranteedMsgingDefragmentationScheduleTimeList) {
    this.guaranteedMsgingDefragmentationScheduleTimeList = guaranteedMsgingDefragmentationScheduleTimeList;
  }


  public Broker guaranteedMsgingDefragmentationThresholdEnabled(Boolean guaranteedMsgingDefragmentationThresholdEnabled) {
    
    this.guaranteedMsgingDefragmentationThresholdEnabled = guaranteedMsgingDefragmentationThresholdEnabled;
    return this;
  }

   /**
   * Enable or disable threshold-based defragmentation of Guaranteed Messaging spool files. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationThresholdEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getGuaranteedMsgingDefragmentationThresholdEnabled() {
    return guaranteedMsgingDefragmentationThresholdEnabled;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationThresholdEnabled(Boolean guaranteedMsgingDefragmentationThresholdEnabled) {
    this.guaranteedMsgingDefragmentationThresholdEnabled = guaranteedMsgingDefragmentationThresholdEnabled;
  }


  public Broker guaranteedMsgingDefragmentationThresholdFragmentationPercentage(Long guaranteedMsgingDefragmentationThresholdFragmentationPercentage) {
    
    this.guaranteedMsgingDefragmentationThresholdFragmentationPercentage = guaranteedMsgingDefragmentationThresholdFragmentationPercentage;
    return this;
  }

   /**
   * Percentage of spool fragmentation needed to trigger defragmentation run. The minimum value allowed is 30%. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;50&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationThresholdFragmentationPercentage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_FRAGMENTATION_PERCENTAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getGuaranteedMsgingDefragmentationThresholdFragmentationPercentage() {
    return guaranteedMsgingDefragmentationThresholdFragmentationPercentage;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_FRAGMENTATION_PERCENTAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationThresholdFragmentationPercentage(Long guaranteedMsgingDefragmentationThresholdFragmentationPercentage) {
    this.guaranteedMsgingDefragmentationThresholdFragmentationPercentage = guaranteedMsgingDefragmentationThresholdFragmentationPercentage;
  }


  public Broker guaranteedMsgingDefragmentationThresholdMinInterval(Long guaranteedMsgingDefragmentationThresholdMinInterval) {
    
    this.guaranteedMsgingDefragmentationThresholdMinInterval = guaranteedMsgingDefragmentationThresholdMinInterval;
    return this;
  }

   /**
   * Minimum interval of time (in minutes) between defragmentation runs triggered by thresholds. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;15&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationThresholdMinInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_MIN_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getGuaranteedMsgingDefragmentationThresholdMinInterval() {
    return guaranteedMsgingDefragmentationThresholdMinInterval;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_MIN_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationThresholdMinInterval(Long guaranteedMsgingDefragmentationThresholdMinInterval) {
    this.guaranteedMsgingDefragmentationThresholdMinInterval = guaranteedMsgingDefragmentationThresholdMinInterval;
  }


  public Broker guaranteedMsgingDefragmentationThresholdUsagePercentage(Long guaranteedMsgingDefragmentationThresholdUsagePercentage) {
    
    this.guaranteedMsgingDefragmentationThresholdUsagePercentage = guaranteedMsgingDefragmentationThresholdUsagePercentage;
    return this;
  }

   /**
   * Percentage of spool usage needed to trigger defragmentation run. The minimum value allowed is 30%. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;50&#x60;. Available since 2.25.
   * @return guaranteedMsgingDefragmentationThresholdUsagePercentage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_USAGE_PERCENTAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getGuaranteedMsgingDefragmentationThresholdUsagePercentage() {
    return guaranteedMsgingDefragmentationThresholdUsagePercentage;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_DEFRAGMENTATION_THRESHOLD_USAGE_PERCENTAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingDefragmentationThresholdUsagePercentage(Long guaranteedMsgingDefragmentationThresholdUsagePercentage) {
    this.guaranteedMsgingDefragmentationThresholdUsagePercentage = guaranteedMsgingDefragmentationThresholdUsagePercentage;
  }


  public Broker guaranteedMsgingEnabled(Boolean guaranteedMsgingEnabled) {
    
    this.guaranteedMsgingEnabled = guaranteedMsgingEnabled;
    return this;
  }

   /**
   * Enable or disable Guaranteed Messaging. The default value is &#x60;false&#x60;. Available since 2.18.
   * @return guaranteedMsgingEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getGuaranteedMsgingEnabled() {
    return guaranteedMsgingEnabled;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEnabled(Boolean guaranteedMsgingEnabled) {
    this.guaranteedMsgingEnabled = guaranteedMsgingEnabled;
  }


  public Broker guaranteedMsgingEventCacheUsageThreshold(EventThreshold guaranteedMsgingEventCacheUsageThreshold) {
    
    this.guaranteedMsgingEventCacheUsageThreshold = guaranteedMsgingEventCacheUsageThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventCacheUsageThreshold
   * @return guaranteedMsgingEventCacheUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_CACHE_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventCacheUsageThreshold() {
    return guaranteedMsgingEventCacheUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_CACHE_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventCacheUsageThreshold(EventThreshold guaranteedMsgingEventCacheUsageThreshold) {
    this.guaranteedMsgingEventCacheUsageThreshold = guaranteedMsgingEventCacheUsageThreshold;
  }


  public Broker guaranteedMsgingEventDeliveredUnackedThreshold(EventThresholdByPercent guaranteedMsgingEventDeliveredUnackedThreshold) {
    
    this.guaranteedMsgingEventDeliveredUnackedThreshold = guaranteedMsgingEventDeliveredUnackedThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventDeliveredUnackedThreshold
   * @return guaranteedMsgingEventDeliveredUnackedThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DELIVERED_UNACKED_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByPercent getGuaranteedMsgingEventDeliveredUnackedThreshold() {
    return guaranteedMsgingEventDeliveredUnackedThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DELIVERED_UNACKED_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventDeliveredUnackedThreshold(EventThresholdByPercent guaranteedMsgingEventDeliveredUnackedThreshold) {
    this.guaranteedMsgingEventDeliveredUnackedThreshold = guaranteedMsgingEventDeliveredUnackedThreshold;
  }


  public Broker guaranteedMsgingEventDiskUsageThreshold(EventThresholdByPercent guaranteedMsgingEventDiskUsageThreshold) {
    
    this.guaranteedMsgingEventDiskUsageThreshold = guaranteedMsgingEventDiskUsageThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventDiskUsageThreshold
   * @return guaranteedMsgingEventDiskUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DISK_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByPercent getGuaranteedMsgingEventDiskUsageThreshold() {
    return guaranteedMsgingEventDiskUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_DISK_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventDiskUsageThreshold(EventThresholdByPercent guaranteedMsgingEventDiskUsageThreshold) {
    this.guaranteedMsgingEventDiskUsageThreshold = guaranteedMsgingEventDiskUsageThreshold;
  }


  public Broker guaranteedMsgingEventEgressFlowCountThreshold(EventThreshold guaranteedMsgingEventEgressFlowCountThreshold) {
    
    this.guaranteedMsgingEventEgressFlowCountThreshold = guaranteedMsgingEventEgressFlowCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventEgressFlowCountThreshold
   * @return guaranteedMsgingEventEgressFlowCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_EGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventEgressFlowCountThreshold() {
    return guaranteedMsgingEventEgressFlowCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_EGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventEgressFlowCountThreshold(EventThreshold guaranteedMsgingEventEgressFlowCountThreshold) {
    this.guaranteedMsgingEventEgressFlowCountThreshold = guaranteedMsgingEventEgressFlowCountThreshold;
  }


  public Broker guaranteedMsgingEventEndpointCountThreshold(EventThreshold guaranteedMsgingEventEndpointCountThreshold) {
    
    this.guaranteedMsgingEventEndpointCountThreshold = guaranteedMsgingEventEndpointCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventEndpointCountThreshold
   * @return guaranteedMsgingEventEndpointCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_ENDPOINT_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventEndpointCountThreshold() {
    return guaranteedMsgingEventEndpointCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_ENDPOINT_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventEndpointCountThreshold(EventThreshold guaranteedMsgingEventEndpointCountThreshold) {
    this.guaranteedMsgingEventEndpointCountThreshold = guaranteedMsgingEventEndpointCountThreshold;
  }


  public Broker guaranteedMsgingEventIngressFlowCountThreshold(EventThreshold guaranteedMsgingEventIngressFlowCountThreshold) {
    
    this.guaranteedMsgingEventIngressFlowCountThreshold = guaranteedMsgingEventIngressFlowCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventIngressFlowCountThreshold
   * @return guaranteedMsgingEventIngressFlowCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_INGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventIngressFlowCountThreshold() {
    return guaranteedMsgingEventIngressFlowCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_INGRESS_FLOW_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventIngressFlowCountThreshold(EventThreshold guaranteedMsgingEventIngressFlowCountThreshold) {
    this.guaranteedMsgingEventIngressFlowCountThreshold = guaranteedMsgingEventIngressFlowCountThreshold;
  }


  public Broker guaranteedMsgingEventMsgCountThreshold(EventThresholdByPercent guaranteedMsgingEventMsgCountThreshold) {
    
    this.guaranteedMsgingEventMsgCountThreshold = guaranteedMsgingEventMsgCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventMsgCountThreshold
   * @return guaranteedMsgingEventMsgCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByPercent getGuaranteedMsgingEventMsgCountThreshold() {
    return guaranteedMsgingEventMsgCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventMsgCountThreshold(EventThresholdByPercent guaranteedMsgingEventMsgCountThreshold) {
    this.guaranteedMsgingEventMsgCountThreshold = guaranteedMsgingEventMsgCountThreshold;
  }


  public Broker guaranteedMsgingEventMsgSpoolFileCountThreshold(EventThresholdByPercent guaranteedMsgingEventMsgSpoolFileCountThreshold) {
    
    this.guaranteedMsgingEventMsgSpoolFileCountThreshold = guaranteedMsgingEventMsgSpoolFileCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventMsgSpoolFileCountThreshold
   * @return guaranteedMsgingEventMsgSpoolFileCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_FILE_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByPercent getGuaranteedMsgingEventMsgSpoolFileCountThreshold() {
    return guaranteedMsgingEventMsgSpoolFileCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_FILE_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventMsgSpoolFileCountThreshold(EventThresholdByPercent guaranteedMsgingEventMsgSpoolFileCountThreshold) {
    this.guaranteedMsgingEventMsgSpoolFileCountThreshold = guaranteedMsgingEventMsgSpoolFileCountThreshold;
  }


  public Broker guaranteedMsgingEventMsgSpoolUsageThreshold(EventThreshold guaranteedMsgingEventMsgSpoolUsageThreshold) {
    
    this.guaranteedMsgingEventMsgSpoolUsageThreshold = guaranteedMsgingEventMsgSpoolUsageThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventMsgSpoolUsageThreshold
   * @return guaranteedMsgingEventMsgSpoolUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventMsgSpoolUsageThreshold() {
    return guaranteedMsgingEventMsgSpoolUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_MSG_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventMsgSpoolUsageThreshold(EventThreshold guaranteedMsgingEventMsgSpoolUsageThreshold) {
    this.guaranteedMsgingEventMsgSpoolUsageThreshold = guaranteedMsgingEventMsgSpoolUsageThreshold;
  }


  public Broker guaranteedMsgingEventTransactedSessionCountThreshold(EventThreshold guaranteedMsgingEventTransactedSessionCountThreshold) {
    
    this.guaranteedMsgingEventTransactedSessionCountThreshold = guaranteedMsgingEventTransactedSessionCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventTransactedSessionCountThreshold
   * @return guaranteedMsgingEventTransactedSessionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventTransactedSessionCountThreshold() {
    return guaranteedMsgingEventTransactedSessionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventTransactedSessionCountThreshold(EventThreshold guaranteedMsgingEventTransactedSessionCountThreshold) {
    this.guaranteedMsgingEventTransactedSessionCountThreshold = guaranteedMsgingEventTransactedSessionCountThreshold;
  }


  public Broker guaranteedMsgingEventTransactedSessionResourceCountThreshold(EventThresholdByPercent guaranteedMsgingEventTransactedSessionResourceCountThreshold) {
    
    this.guaranteedMsgingEventTransactedSessionResourceCountThreshold = guaranteedMsgingEventTransactedSessionResourceCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventTransactedSessionResourceCountThreshold
   * @return guaranteedMsgingEventTransactedSessionResourceCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_RESOURCE_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByPercent getGuaranteedMsgingEventTransactedSessionResourceCountThreshold() {
    return guaranteedMsgingEventTransactedSessionResourceCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTED_SESSION_RESOURCE_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventTransactedSessionResourceCountThreshold(EventThresholdByPercent guaranteedMsgingEventTransactedSessionResourceCountThreshold) {
    this.guaranteedMsgingEventTransactedSessionResourceCountThreshold = guaranteedMsgingEventTransactedSessionResourceCountThreshold;
  }


  public Broker guaranteedMsgingEventTransactionCountThreshold(EventThreshold guaranteedMsgingEventTransactionCountThreshold) {
    
    this.guaranteedMsgingEventTransactionCountThreshold = guaranteedMsgingEventTransactionCountThreshold;
    return this;
  }

   /**
   * Get guaranteedMsgingEventTransactionCountThreshold
   * @return guaranteedMsgingEventTransactionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getGuaranteedMsgingEventTransactionCountThreshold() {
    return guaranteedMsgingEventTransactionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_EVENT_TRANSACTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingEventTransactionCountThreshold(EventThreshold guaranteedMsgingEventTransactionCountThreshold) {
    this.guaranteedMsgingEventTransactionCountThreshold = guaranteedMsgingEventTransactionCountThreshold;
  }


  public Broker guaranteedMsgingMaxCacheUsage(Integer guaranteedMsgingMaxCacheUsage) {
    
    this.guaranteedMsgingMaxCacheUsage = guaranteedMsgingMaxCacheUsage;
    return this;
  }

   /**
   * Guaranteed messaging cache usage limit. Expressed as a maximum percentage of the NAB&#39;s egress queueing. resources that the guaranteed message cache is allowed to use. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;10&#x60;. Available since 2.18.
   * @return guaranteedMsgingMaxCacheUsage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MAX_CACHE_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getGuaranteedMsgingMaxCacheUsage() {
    return guaranteedMsgingMaxCacheUsage;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MAX_CACHE_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingMaxCacheUsage(Integer guaranteedMsgingMaxCacheUsage) {
    this.guaranteedMsgingMaxCacheUsage = guaranteedMsgingMaxCacheUsage;
  }


  public Broker guaranteedMsgingMaxMsgSpoolUsage(Long guaranteedMsgingMaxMsgSpoolUsage) {
    
    this.guaranteedMsgingMaxMsgSpoolUsage = guaranteedMsgingMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum total message spool usage allowed across all VPNs on this broker, in megabytes. Recommendation: the maximum value should be less than 90% of the disk space allocated for the guaranteed message spool. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1500&#x60;. Available since 2.18.
   * @return guaranteedMsgingMaxMsgSpoolUsage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getGuaranteedMsgingMaxMsgSpoolUsage() {
    return guaranteedMsgingMaxMsgSpoolUsage;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingMaxMsgSpoolUsage(Long guaranteedMsgingMaxMsgSpoolUsage) {
    this.guaranteedMsgingMaxMsgSpoolUsage = guaranteedMsgingMaxMsgSpoolUsage;
  }


  public Broker guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout(Long guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout) {
    
    this.guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout = guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout;
    return this;
  }

   /**
   * The maximum time, in milliseconds, that can be tolerated for remote acknowledgement of synchronization messages before which the remote system will be considered out of sync. The default value is &#x60;10000&#x60;. Available since 2.18.
   * @return guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_MSG_ACK_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getGuaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout() {
    return guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_MSG_ACK_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout(Long guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout) {
    this.guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout = guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout;
  }


  public Broker guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout(Long guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout) {
    
    this.guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout = guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout;
    return this;
  }

   /**
   * The maximum time, in milliseconds, that can be tolerated for remote disk writes before which the remote system will be considered out of sync. The default value is &#x60;10000&#x60;. Available since 2.18.
   * @return guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_SPOOL_FILE_ACK_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getGuaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout() {
    return guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_MSG_SPOOL_SYNC_MIRRORED_SPOOL_FILE_ACK_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout(Long guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout) {
    this.guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout = guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout;
  }


  public Broker guaranteedMsgingTransactionReplicationCompatibilityMode(GuaranteedMsgingTransactionReplicationCompatibilityModeEnum guaranteedMsgingTransactionReplicationCompatibilityMode) {
    
    this.guaranteedMsgingTransactionReplicationCompatibilityMode = guaranteedMsgingTransactionReplicationCompatibilityMode;
    return this;
  }

   /**
   * The replication compatibility mode for the router. The default value is &#x60;\&quot;legacy\&quot;&#x60;. The allowed values and their meaning are:\&quot;legacy\&quot; - All transactions originated by clients are replicated to the standby site without using transactions.\&quot;transacted\&quot; - All transactions originated by clients are replicated to the standby site using transactions. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;legacy\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;legacy\&quot; - All transactions originated by clients are replicated to the standby site without using transactions. \&quot;transacted\&quot; - All transactions originated by clients are replicated to the standby site using transactions. &lt;/pre&gt;  Available since 2.18.
   * @return guaranteedMsgingTransactionReplicationCompatibilityMode
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_TRANSACTION_REPLICATION_COMPATIBILITY_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public GuaranteedMsgingTransactionReplicationCompatibilityModeEnum getGuaranteedMsgingTransactionReplicationCompatibilityMode() {
    return guaranteedMsgingTransactionReplicationCompatibilityMode;
  }


  @JsonProperty(JSON_PROPERTY_GUARANTEED_MSGING_TRANSACTION_REPLICATION_COMPATIBILITY_MODE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGuaranteedMsgingTransactionReplicationCompatibilityMode(GuaranteedMsgingTransactionReplicationCompatibilityModeEnum guaranteedMsgingTransactionReplicationCompatibilityMode) {
    this.guaranteedMsgingTransactionReplicationCompatibilityMode = guaranteedMsgingTransactionReplicationCompatibilityMode;
  }


  public Broker oauthProfileDefault(String oauthProfileDefault) {
    
    this.oauthProfileDefault = oauthProfileDefault;
    return this;
  }

   /**
   * The default OAuth profile for OAuth authenticated SEMP requests. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.24.
   * @return oauthProfileDefault
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OAUTH_PROFILE_DEFAULT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOauthProfileDefault() {
    return oauthProfileDefault;
  }


  @JsonProperty(JSON_PROPERTY_OAUTH_PROFILE_DEFAULT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOauthProfileDefault(String oauthProfileDefault) {
    this.oauthProfileDefault = oauthProfileDefault;
  }


  public Broker serviceAmqpEnabled(Boolean serviceAmqpEnabled) {
    
    this.serviceAmqpEnabled = serviceAmqpEnabled;
    return this;
  }

   /**
   * Enable or disable the AMQP service. When disabled new AMQP Clients may not connect through the global or per-VPN AMQP listen-ports, and all currently connected AMQP Clients are immediately disconnected. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.17.
   * @return serviceAmqpEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceAmqpEnabled() {
    return serviceAmqpEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_AMQP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceAmqpEnabled(Boolean serviceAmqpEnabled) {
    this.serviceAmqpEnabled = serviceAmqpEnabled;
  }


  public Broker serviceAmqpTlsListenPort(Long serviceAmqpTlsListenPort) {
    
    this.serviceAmqpTlsListenPort = serviceAmqpTlsListenPort;
    return this;
  }

   /**
   * TCP port number that AMQP clients can use to connect to the broker using raw TCP over TLS. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;0&#x60;. Available since 2.17.
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


  public Broker serviceEventConnectionCountThreshold(EventThreshold serviceEventConnectionCountThreshold) {
    
    this.serviceEventConnectionCountThreshold = serviceEventConnectionCountThreshold;
    return this;
  }

   /**
   * Get serviceEventConnectionCountThreshold
   * @return serviceEventConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getServiceEventConnectionCountThreshold() {
    return serviceEventConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceEventConnectionCountThreshold(EventThreshold serviceEventConnectionCountThreshold) {
    this.serviceEventConnectionCountThreshold = serviceEventConnectionCountThreshold;
  }


  public Broker serviceHealthCheckEnabled(Boolean serviceHealthCheckEnabled) {
    
    this.serviceHealthCheckEnabled = serviceHealthCheckEnabled;
    return this;
  }

   /**
   * Enable or disable the health-check service. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.17.
   * @return serviceHealthCheckEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_HEALTH_CHECK_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceHealthCheckEnabled() {
    return serviceHealthCheckEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_HEALTH_CHECK_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceHealthCheckEnabled(Boolean serviceHealthCheckEnabled) {
    this.serviceHealthCheckEnabled = serviceHealthCheckEnabled;
  }


  public Broker serviceHealthCheckListenPort(Long serviceHealthCheckListenPort) {
    
    this.serviceHealthCheckListenPort = serviceHealthCheckListenPort;
    return this;
  }

   /**
   * The port number for the health-check service. The port must be unique across the message backbone. The health-check service must be disabled to change the port. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;5550&#x60;. Available since 2.17.
   * @return serviceHealthCheckListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_HEALTH_CHECK_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceHealthCheckListenPort() {
    return serviceHealthCheckListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_HEALTH_CHECK_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceHealthCheckListenPort(Long serviceHealthCheckListenPort) {
    this.serviceHealthCheckListenPort = serviceHealthCheckListenPort;
  }


  public Broker serviceMateLinkEnabled(Boolean serviceMateLinkEnabled) {
    
    this.serviceMateLinkEnabled = serviceMateLinkEnabled;
    return this;
  }

   /**
   * Enable or disable the mate-link service. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return serviceMateLinkEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MATE_LINK_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMateLinkEnabled() {
    return serviceMateLinkEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MATE_LINK_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMateLinkEnabled(Boolean serviceMateLinkEnabled) {
    this.serviceMateLinkEnabled = serviceMateLinkEnabled;
  }


  public Broker serviceMateLinkListenPort(Long serviceMateLinkListenPort) {
    
    this.serviceMateLinkListenPort = serviceMateLinkListenPort;
    return this;
  }

   /**
   * The port number for the mate-link service. The port must be unique across the message backbone. The mate-link service must be disabled to change the port. The default value is &#x60;8741&#x60;. Available since 2.17.
   * @return serviceMateLinkListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MATE_LINK_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceMateLinkListenPort() {
    return serviceMateLinkListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MATE_LINK_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMateLinkListenPort(Long serviceMateLinkListenPort) {
    this.serviceMateLinkListenPort = serviceMateLinkListenPort;
  }


  public Broker serviceMqttEnabled(Boolean serviceMqttEnabled) {
    
    this.serviceMqttEnabled = serviceMqttEnabled;
    return this;
  }

   /**
   * Enable or disable the MQTT service. When disabled new MQTT Clients may not connect through the per-VPN MQTT listen-ports, and all currently connected MQTT Clients are immediately disconnected. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.17.
   * @return serviceMqttEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMqttEnabled() {
    return serviceMqttEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MQTT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMqttEnabled(Boolean serviceMqttEnabled) {
    this.serviceMqttEnabled = serviceMqttEnabled;
  }


  public Broker serviceMsgBackboneEnabled(Boolean serviceMsgBackboneEnabled) {
    
    this.serviceMsgBackboneEnabled = serviceMsgBackboneEnabled;
    return this;
  }

   /**
   * Enable or disable the msg-backbone service. When disabled new Clients may not connect through global or per-VPN listen-ports, and all currently connected Clients are immediately disconnected. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return serviceMsgBackboneEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MSG_BACKBONE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceMsgBackboneEnabled() {
    return serviceMsgBackboneEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MSG_BACKBONE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMsgBackboneEnabled(Boolean serviceMsgBackboneEnabled) {
    this.serviceMsgBackboneEnabled = serviceMsgBackboneEnabled;
  }


  public Broker serviceRedundancyEnabled(Boolean serviceRedundancyEnabled) {
    
    this.serviceRedundancyEnabled = serviceRedundancyEnabled;
    return this;
  }

   /**
   * Enable or disable the redundancy service. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return serviceRedundancyEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REDUNDANCY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceRedundancyEnabled() {
    return serviceRedundancyEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REDUNDANCY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRedundancyEnabled(Boolean serviceRedundancyEnabled) {
    this.serviceRedundancyEnabled = serviceRedundancyEnabled;
  }


  public Broker serviceRedundancyFirstListenPort(Long serviceRedundancyFirstListenPort) {
    
    this.serviceRedundancyFirstListenPort = serviceRedundancyFirstListenPort;
    return this;
  }

   /**
   * The first listen-port used for the redundancy service. Redundancy uses this port and the subsequent 2 ports. These port must be unique across the message backbone. The redundancy service must be disabled to change this port. The default value is &#x60;8300&#x60;. Available since 2.17.
   * @return serviceRedundancyFirstListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REDUNDANCY_FIRST_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceRedundancyFirstListenPort() {
    return serviceRedundancyFirstListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REDUNDANCY_FIRST_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRedundancyFirstListenPort(Long serviceRedundancyFirstListenPort) {
    this.serviceRedundancyFirstListenPort = serviceRedundancyFirstListenPort;
  }


  public Broker serviceRestEventOutgoingConnectionCountThreshold(EventThreshold serviceRestEventOutgoingConnectionCountThreshold) {
    
    this.serviceRestEventOutgoingConnectionCountThreshold = serviceRestEventOutgoingConnectionCountThreshold;
    return this;
  }

   /**
   * Get serviceRestEventOutgoingConnectionCountThreshold
   * @return serviceRestEventOutgoingConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_EVENT_OUTGOING_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getServiceRestEventOutgoingConnectionCountThreshold() {
    return serviceRestEventOutgoingConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_EVENT_OUTGOING_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestEventOutgoingConnectionCountThreshold(EventThreshold serviceRestEventOutgoingConnectionCountThreshold) {
    this.serviceRestEventOutgoingConnectionCountThreshold = serviceRestEventOutgoingConnectionCountThreshold;
  }


  public Broker serviceRestIncomingEnabled(Boolean serviceRestIncomingEnabled) {
    
    this.serviceRestIncomingEnabled = serviceRestIncomingEnabled;
    return this;
  }

   /**
   * Enable or disable the REST service incoming connections on the router. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.17.
   * @return serviceRestIncomingEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceRestIncomingEnabled() {
    return serviceRestIncomingEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_INCOMING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestIncomingEnabled(Boolean serviceRestIncomingEnabled) {
    this.serviceRestIncomingEnabled = serviceRestIncomingEnabled;
  }


  public Broker serviceRestOutgoingEnabled(Boolean serviceRestOutgoingEnabled) {
    
    this.serviceRestOutgoingEnabled = serviceRestOutgoingEnabled;
    return this;
  }

   /**
   * Enable or disable the REST service outgoing connections on the router. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.17.
   * @return serviceRestOutgoingEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_REST_OUTGOING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceRestOutgoingEnabled() {
    return serviceRestOutgoingEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_REST_OUTGOING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceRestOutgoingEnabled(Boolean serviceRestOutgoingEnabled) {
    this.serviceRestOutgoingEnabled = serviceRestOutgoingEnabled;
  }


  public Broker serviceSempCorsAllowAnyHostEnabled(Boolean serviceSempCorsAllowAnyHostEnabled) {
    
    this.serviceSempCorsAllowAnyHostEnabled = serviceSempCorsAllowAnyHostEnabled;
    return this;
  }

   /**
   * Enable or disable cross origin resource requests for the SEMP service. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.24.
   * @return serviceSempCorsAllowAnyHostEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_CORS_ALLOW_ANY_HOST_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSempCorsAllowAnyHostEnabled() {
    return serviceSempCorsAllowAnyHostEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_CORS_ALLOW_ANY_HOST_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempCorsAllowAnyHostEnabled(Boolean serviceSempCorsAllowAnyHostEnabled) {
    this.serviceSempCorsAllowAnyHostEnabled = serviceSempCorsAllowAnyHostEnabled;
  }


  public Broker serviceSempLegacyTimeoutEnabled(Boolean serviceSempLegacyTimeoutEnabled) {
    
    this.serviceSempLegacyTimeoutEnabled = serviceSempLegacyTimeoutEnabled;
    return this;
  }

   /**
   * Enable or disable extended SEMP timeouts for paged GETs. When a request times out, it returns the current page of content, even if the page is not full.  When enabled, the timeout is 60 seconds. When disabled, the timeout is 5 seconds.  The recommended setting is disabled (no legacy-timeout).  This parameter is intended as a temporary workaround to be used until SEMP clients can handle short pages.  This setting will be removed in a future release. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.18.
   * @return serviceSempLegacyTimeoutEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_LEGACY_TIMEOUT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSempLegacyTimeoutEnabled() {
    return serviceSempLegacyTimeoutEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_LEGACY_TIMEOUT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempLegacyTimeoutEnabled(Boolean serviceSempLegacyTimeoutEnabled) {
    this.serviceSempLegacyTimeoutEnabled = serviceSempLegacyTimeoutEnabled;
  }


  public Broker serviceSempPlainTextEnabled(Boolean serviceSempPlainTextEnabled) {
    
    this.serviceSempPlainTextEnabled = serviceSempPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable plain-text SEMP service. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return serviceSempPlainTextEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSempPlainTextEnabled() {
    return serviceSempPlainTextEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempPlainTextEnabled(Boolean serviceSempPlainTextEnabled) {
    this.serviceSempPlainTextEnabled = serviceSempPlainTextEnabled;
  }


  public Broker serviceSempPlainTextListenPort(Long serviceSempPlainTextListenPort) {
    
    this.serviceSempPlainTextListenPort = serviceSempPlainTextListenPort;
    return this;
  }

   /**
   * The TCP port for plain-text SEMP client connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;80&#x60;. Available since 2.17.
   * @return serviceSempPlainTextListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSempPlainTextListenPort() {
    return serviceSempPlainTextListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempPlainTextListenPort(Long serviceSempPlainTextListenPort) {
    this.serviceSempPlainTextListenPort = serviceSempPlainTextListenPort;
  }


  public Broker serviceSempSessionIdleTimeout(Integer serviceSempSessionIdleTimeout) {
    
    this.serviceSempSessionIdleTimeout = serviceSempSessionIdleTimeout;
    return this;
  }

   /**
   * The session idle timeout, in minutes. Sessions will be invalidated if there is no activity in this period of time. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;15&#x60;. Available since 2.21.
   * @return serviceSempSessionIdleTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_SESSION_IDLE_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getServiceSempSessionIdleTimeout() {
    return serviceSempSessionIdleTimeout;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_SESSION_IDLE_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempSessionIdleTimeout(Integer serviceSempSessionIdleTimeout) {
    this.serviceSempSessionIdleTimeout = serviceSempSessionIdleTimeout;
  }


  public Broker serviceSempSessionMaxLifetime(Integer serviceSempSessionMaxLifetime) {
    
    this.serviceSempSessionMaxLifetime = serviceSempSessionMaxLifetime;
    return this;
  }

   /**
   * The maximum lifetime of a session, in minutes. Sessions will be invalidated after this period of time, regardless of activity. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;43200&#x60;. Available since 2.21.
   * @return serviceSempSessionMaxLifetime
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_SESSION_MAX_LIFETIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getServiceSempSessionMaxLifetime() {
    return serviceSempSessionMaxLifetime;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_SESSION_MAX_LIFETIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempSessionMaxLifetime(Integer serviceSempSessionMaxLifetime) {
    this.serviceSempSessionMaxLifetime = serviceSempSessionMaxLifetime;
  }


  public Broker serviceSempTlsEnabled(Boolean serviceSempTlsEnabled) {
    
    this.serviceSempTlsEnabled = serviceSempTlsEnabled;
    return this;
  }

   /**
   * Enable or disable TLS SEMP service. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return serviceSempTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSempTlsEnabled() {
    return serviceSempTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempTlsEnabled(Boolean serviceSempTlsEnabled) {
    this.serviceSempTlsEnabled = serviceSempTlsEnabled;
  }


  public Broker serviceSempTlsListenPort(Long serviceSempTlsListenPort) {
    
    this.serviceSempTlsListenPort = serviceSempTlsListenPort;
    return this;
  }

   /**
   * The TCP port for TLS SEMP client connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1943&#x60;. Available since 2.17.
   * @return serviceSempTlsListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSempTlsListenPort() {
    return serviceSempTlsListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SEMP_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSempTlsListenPort(Long serviceSempTlsListenPort) {
    this.serviceSempTlsListenPort = serviceSempTlsListenPort;
  }


  public Broker serviceSmfCompressionListenPort(Long serviceSmfCompressionListenPort) {
    
    this.serviceSmfCompressionListenPort = serviceSmfCompressionListenPort;
    return this;
  }

   /**
   * TCP port number that SMF clients can use to connect to the broker using raw compression TCP. The default value is &#x60;55003&#x60;. Available since 2.17.
   * @return serviceSmfCompressionListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_COMPRESSION_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSmfCompressionListenPort() {
    return serviceSmfCompressionListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_COMPRESSION_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfCompressionListenPort(Long serviceSmfCompressionListenPort) {
    this.serviceSmfCompressionListenPort = serviceSmfCompressionListenPort;
  }


  public Broker serviceSmfEnabled(Boolean serviceSmfEnabled) {
    
    this.serviceSmfEnabled = serviceSmfEnabled;
    return this;
  }

   /**
   * Enable or disable the SMF service. When disabled new SMF Clients may not connect through the global listen-ports, and all currently connected SMF Clients are immediately disconnected. The default value is &#x60;true&#x60;. Available since 2.17.
   * @return serviceSmfEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSmfEnabled() {
    return serviceSmfEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfEnabled(Boolean serviceSmfEnabled) {
    this.serviceSmfEnabled = serviceSmfEnabled;
  }


  public Broker serviceSmfEventConnectionCountThreshold(EventThreshold serviceSmfEventConnectionCountThreshold) {
    
    this.serviceSmfEventConnectionCountThreshold = serviceSmfEventConnectionCountThreshold;
    return this;
  }

   /**
   * Get serviceSmfEventConnectionCountThreshold
   * @return serviceSmfEventConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getServiceSmfEventConnectionCountThreshold() {
    return serviceSmfEventConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfEventConnectionCountThreshold(EventThreshold serviceSmfEventConnectionCountThreshold) {
    this.serviceSmfEventConnectionCountThreshold = serviceSmfEventConnectionCountThreshold;
  }


  public Broker serviceSmfPlainTextListenPort(Long serviceSmfPlainTextListenPort) {
    
    this.serviceSmfPlainTextListenPort = serviceSmfPlainTextListenPort;
    return this;
  }

   /**
   * TCP port number that SMF clients can use to connect to the broker using raw TCP. The default value is &#x60;55555&#x60;. Available since 2.17.
   * @return serviceSmfPlainTextListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSmfPlainTextListenPort() {
    return serviceSmfPlainTextListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfPlainTextListenPort(Long serviceSmfPlainTextListenPort) {
    this.serviceSmfPlainTextListenPort = serviceSmfPlainTextListenPort;
  }


  public Broker serviceSmfRoutingControlListenPort(Long serviceSmfRoutingControlListenPort) {
    
    this.serviceSmfRoutingControlListenPort = serviceSmfRoutingControlListenPort;
    return this;
  }

   /**
   * TCP port number that SMF clients can use to connect to the broker using raw routing control TCP. The default value is &#x60;55556&#x60;. Available since 2.17.
   * @return serviceSmfRoutingControlListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_ROUTING_CONTROL_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSmfRoutingControlListenPort() {
    return serviceSmfRoutingControlListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_ROUTING_CONTROL_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfRoutingControlListenPort(Long serviceSmfRoutingControlListenPort) {
    this.serviceSmfRoutingControlListenPort = serviceSmfRoutingControlListenPort;
  }


  public Broker serviceSmfTlsListenPort(Long serviceSmfTlsListenPort) {
    
    this.serviceSmfTlsListenPort = serviceSmfTlsListenPort;
    return this;
  }

   /**
   * TCP port number that SMF clients can use to connect to the broker using raw TCP over TLS. The default value is &#x60;55443&#x60;. Available since 2.17.
   * @return serviceSmfTlsListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSmfTlsListenPort() {
    return serviceSmfTlsListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfTlsListenPort(Long serviceSmfTlsListenPort) {
    this.serviceSmfTlsListenPort = serviceSmfTlsListenPort;
  }


  public Broker serviceTlsEventConnectionCountThreshold(EventThreshold serviceTlsEventConnectionCountThreshold) {
    
    this.serviceTlsEventConnectionCountThreshold = serviceTlsEventConnectionCountThreshold;
    return this;
  }

   /**
   * Get serviceTlsEventConnectionCountThreshold
   * @return serviceTlsEventConnectionCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_TLS_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getServiceTlsEventConnectionCountThreshold() {
    return serviceTlsEventConnectionCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_TLS_EVENT_CONNECTION_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceTlsEventConnectionCountThreshold(EventThreshold serviceTlsEventConnectionCountThreshold) {
    this.serviceTlsEventConnectionCountThreshold = serviceTlsEventConnectionCountThreshold;
  }


  public Broker serviceWebTransportEnabled(Boolean serviceWebTransportEnabled) {
    
    this.serviceWebTransportEnabled = serviceWebTransportEnabled;
    return this;
  }

   /**
   * Enable or disable the web-transport service. When disabled new web-transport Clients may not connect through the global listen-ports, and all currently connected web-transport Clients are immediately disconnected. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.17.
   * @return serviceWebTransportEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceWebTransportEnabled() {
    return serviceWebTransportEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebTransportEnabled(Boolean serviceWebTransportEnabled) {
    this.serviceWebTransportEnabled = serviceWebTransportEnabled;
  }


  public Broker serviceWebTransportPlainTextListenPort(Long serviceWebTransportPlainTextListenPort) {
    
    this.serviceWebTransportPlainTextListenPort = serviceWebTransportPlainTextListenPort;
    return this;
  }

   /**
   * The TCP port for plain-text WEB client connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;8008&#x60;. Available since 2.17.
   * @return serviceWebTransportPlainTextListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceWebTransportPlainTextListenPort() {
    return serviceWebTransportPlainTextListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_PLAIN_TEXT_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebTransportPlainTextListenPort(Long serviceWebTransportPlainTextListenPort) {
    this.serviceWebTransportPlainTextListenPort = serviceWebTransportPlainTextListenPort;
  }


  public Broker serviceWebTransportTlsListenPort(Long serviceWebTransportTlsListenPort) {
    
    this.serviceWebTransportTlsListenPort = serviceWebTransportTlsListenPort;
    return this;
  }

   /**
   * The TCP port for TLS WEB client connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1443&#x60;. Available since 2.17.
   * @return serviceWebTransportTlsListenPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceWebTransportTlsListenPort() {
    return serviceWebTransportTlsListenPort;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_TLS_LISTEN_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebTransportTlsListenPort(Long serviceWebTransportTlsListenPort) {
    this.serviceWebTransportTlsListenPort = serviceWebTransportTlsListenPort;
  }


  public Broker serviceWebTransportWebUrlSuffix(String serviceWebTransportWebUrlSuffix) {
    
    this.serviceWebTransportWebUrlSuffix = serviceWebTransportWebUrlSuffix;
    return this;
  }

   /**
   * Used to specify the Web URL suffix that will be used by Web clients when communicating with the broker. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.17.
   * @return serviceWebTransportWebUrlSuffix
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_WEB_URL_SUFFIX)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getServiceWebTransportWebUrlSuffix() {
    return serviceWebTransportWebUrlSuffix;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_TRANSPORT_WEB_URL_SUFFIX)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebTransportWebUrlSuffix(String serviceWebTransportWebUrlSuffix) {
    this.serviceWebTransportWebUrlSuffix = serviceWebTransportWebUrlSuffix;
  }


  public Broker tlsBlockVersion11Enabled(Boolean tlsBlockVersion11Enabled) {
    
    this.tlsBlockVersion11Enabled = tlsBlockVersion11Enabled;
    return this;
  }

   /**
   * Enable or disable the blocking of TLS version 1.1 connections. When blocked, all existing incoming and outgoing TLS 1.1 connections with Clients, SEMP users, and LDAP servers remain connected while new connections are blocked. Note that support for TLS 1.1 will eventually be discontinued, at which time TLS 1.1 connections will be blocked regardless of this setting. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
   * @return tlsBlockVersion11Enabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_BLOCK_VERSION11_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsBlockVersion11Enabled() {
    return tlsBlockVersion11Enabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_BLOCK_VERSION11_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsBlockVersion11Enabled(Boolean tlsBlockVersion11Enabled) {
    this.tlsBlockVersion11Enabled = tlsBlockVersion11Enabled;
  }


  public Broker tlsCipherSuiteManagementList(String tlsCipherSuiteManagementList) {
    
    this.tlsCipherSuiteManagementList = tlsCipherSuiteManagementList;
    return this;
  }

   /**
   * The colon-separated list of cipher suites used for TLS management connections (e.g. SEMP, LDAP). The value \&quot;default\&quot; implies all supported suites ordered from most secure to least secure. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;.
   * @return tlsCipherSuiteManagementList
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_MANAGEMENT_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTlsCipherSuiteManagementList() {
    return tlsCipherSuiteManagementList;
  }


  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_MANAGEMENT_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsCipherSuiteManagementList(String tlsCipherSuiteManagementList) {
    this.tlsCipherSuiteManagementList = tlsCipherSuiteManagementList;
  }


  public Broker tlsCipherSuiteMsgBackboneList(String tlsCipherSuiteMsgBackboneList) {
    
    this.tlsCipherSuiteMsgBackboneList = tlsCipherSuiteMsgBackboneList;
    return this;
  }

   /**
   * The colon-separated list of cipher suites used for TLS data connections (e.g. client pub/sub). The value \&quot;default\&quot; implies all supported suites ordered from most secure to least secure. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;.
   * @return tlsCipherSuiteMsgBackboneList
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_MSG_BACKBONE_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTlsCipherSuiteMsgBackboneList() {
    return tlsCipherSuiteMsgBackboneList;
  }


  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_MSG_BACKBONE_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsCipherSuiteMsgBackboneList(String tlsCipherSuiteMsgBackboneList) {
    this.tlsCipherSuiteMsgBackboneList = tlsCipherSuiteMsgBackboneList;
  }


  public Broker tlsCipherSuiteSecureShellList(String tlsCipherSuiteSecureShellList) {
    
    this.tlsCipherSuiteSecureShellList = tlsCipherSuiteSecureShellList;
    return this;
  }

   /**
   * The colon-separated list of cipher suites used for TLS secure shell connections (e.g. SSH, SFTP, SCP). The value \&quot;default\&quot; implies all supported suites ordered from most secure to least secure. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;.
   * @return tlsCipherSuiteSecureShellList
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_SECURE_SHELL_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTlsCipherSuiteSecureShellList() {
    return tlsCipherSuiteSecureShellList;
  }


  @JsonProperty(JSON_PROPERTY_TLS_CIPHER_SUITE_SECURE_SHELL_LIST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsCipherSuiteSecureShellList(String tlsCipherSuiteSecureShellList) {
    this.tlsCipherSuiteSecureShellList = tlsCipherSuiteSecureShellList;
  }


  public Broker tlsCrimeExploitProtectionEnabled(Boolean tlsCrimeExploitProtectionEnabled) {
    
    this.tlsCrimeExploitProtectionEnabled = tlsCrimeExploitProtectionEnabled;
    return this;
  }

   /**
   * Enable or disable protection against the CRIME exploit. When enabled, TLS+compressed messaging performance is degraded. This protection should only be disabled if sufficient ACL and authentication features are being employed such that a potential attacker does not have sufficient access to trigger the exploit. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
   * @return tlsCrimeExploitProtectionEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_CRIME_EXPLOIT_PROTECTION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsCrimeExploitProtectionEnabled() {
    return tlsCrimeExploitProtectionEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_CRIME_EXPLOIT_PROTECTION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsCrimeExploitProtectionEnabled(Boolean tlsCrimeExploitProtectionEnabled) {
    this.tlsCrimeExploitProtectionEnabled = tlsCrimeExploitProtectionEnabled;
  }


  public Broker tlsServerCertContent(String tlsServerCertContent) {
    
    this.tlsServerCertContent = tlsServerCertContent;
    return this;
  }

   /**
   * The PEM formatted content for the server certificate used for TLS connections. It must consist of a private key and between one and three certificates comprising the certificate trust chain. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return tlsServerCertContent
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTlsServerCertContent() {
    return tlsServerCertContent;
  }


  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_CONTENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsServerCertContent(String tlsServerCertContent) {
    this.tlsServerCertContent = tlsServerCertContent;
  }


  public Broker tlsServerCertPassword(String tlsServerCertPassword) {
    
    this.tlsServerCertPassword = tlsServerCertPassword;
    return this;
  }

   /**
   * The password for the server certificate used for TLS connections. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return tlsServerCertPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTlsServerCertPassword() {
    return tlsServerCertPassword;
  }


  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsServerCertPassword(String tlsServerCertPassword) {
    this.tlsServerCertPassword = tlsServerCertPassword;
  }


  public Broker tlsStandardDomainCertificateAuthoritiesEnabled(Boolean tlsStandardDomainCertificateAuthoritiesEnabled) {
    
    this.tlsStandardDomainCertificateAuthoritiesEnabled = tlsStandardDomainCertificateAuthoritiesEnabled;
    return this;
  }

   /**
   * Enable or disable the standard domain certificate authority list. The default value is &#x60;true&#x60;. Available since 2.19.
   * @return tlsStandardDomainCertificateAuthoritiesEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_STANDARD_DOMAIN_CERTIFICATE_AUTHORITIES_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsStandardDomainCertificateAuthoritiesEnabled() {
    return tlsStandardDomainCertificateAuthoritiesEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_STANDARD_DOMAIN_CERTIFICATE_AUTHORITIES_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsStandardDomainCertificateAuthoritiesEnabled(Boolean tlsStandardDomainCertificateAuthoritiesEnabled) {
    this.tlsStandardDomainCertificateAuthoritiesEnabled = tlsStandardDomainCertificateAuthoritiesEnabled;
  }


  public Broker tlsTicketLifetime(Integer tlsTicketLifetime) {
    
    this.tlsTicketLifetime = tlsTicketLifetime;
    return this;
  }

   /**
   * The TLS ticket lifetime in seconds. When a client connects with TLS, a session with a session ticket is created using the TLS ticket lifetime which determines how long the client has to resume the session. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;86400&#x60;.
   * @return tlsTicketLifetime
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_TICKET_LIFETIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getTlsTicketLifetime() {
    return tlsTicketLifetime;
  }


  @JsonProperty(JSON_PROPERTY_TLS_TICKET_LIFETIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsTicketLifetime(Integer tlsTicketLifetime) {
    this.tlsTicketLifetime = tlsTicketLifetime;
  }


  public Broker webManagerAllowUnencryptedWizardsEnabled(Boolean webManagerAllowUnencryptedWizardsEnabled) {
    
    this.webManagerAllowUnencryptedWizardsEnabled = webManagerAllowUnencryptedWizardsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of unencrypted wizards in the Web-based Manager UI. This setting should be left at its default on all production systems or other systems that need to be secure.  Enabling this option will permit the broker to forward plain-text data to other brokers, making important information or credentials available for snooping. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Available since 2.28.
   * @return webManagerAllowUnencryptedWizardsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_ALLOW_UNENCRYPTED_WIZARDS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getWebManagerAllowUnencryptedWizardsEnabled() {
    return webManagerAllowUnencryptedWizardsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_ALLOW_UNENCRYPTED_WIZARDS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWebManagerAllowUnencryptedWizardsEnabled(Boolean webManagerAllowUnencryptedWizardsEnabled) {
    this.webManagerAllowUnencryptedWizardsEnabled = webManagerAllowUnencryptedWizardsEnabled;
  }


  public Broker webManagerCustomization(String webManagerCustomization) {
    
    this.webManagerCustomization = webManagerCustomization;
    return this;
  }

   /**
   * Reserved for internal use by Solace. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.25.
   * @return webManagerCustomization
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_CUSTOMIZATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getWebManagerCustomization() {
    return webManagerCustomization;
  }


  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_CUSTOMIZATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWebManagerCustomization(String webManagerCustomization) {
    this.webManagerCustomization = webManagerCustomization;
  }


  public Broker webManagerRedirectHttpEnabled(Boolean webManagerRedirectHttpEnabled) {
    
    this.webManagerRedirectHttpEnabled = webManagerRedirectHttpEnabled;
    return this;
  }

   /**
   * Enable or disable redirection of HTTP requests for the broker manager to HTTPS. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.24.
   * @return webManagerRedirectHttpEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getWebManagerRedirectHttpEnabled() {
    return webManagerRedirectHttpEnabled;
  }


  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWebManagerRedirectHttpEnabled(Boolean webManagerRedirectHttpEnabled) {
    this.webManagerRedirectHttpEnabled = webManagerRedirectHttpEnabled;
  }


  public Broker webManagerRedirectHttpOverrideTlsPort(Integer webManagerRedirectHttpOverrideTlsPort) {
    
    this.webManagerRedirectHttpOverrideTlsPort = webManagerRedirectHttpOverrideTlsPort;
    return this;
  }

   /**
   * The HTTPS port that HTTP requests will be redirected towards in a HTTP 301 redirect response. Zero is a special value that means use the value specified for the SEMP TLS port value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;0&#x60;. Available since 2.24.
   * @return webManagerRedirectHttpOverrideTlsPort
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_OVERRIDE_TLS_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getWebManagerRedirectHttpOverrideTlsPort() {
    return webManagerRedirectHttpOverrideTlsPort;
  }


  @JsonProperty(JSON_PROPERTY_WEB_MANAGER_REDIRECT_HTTP_OVERRIDE_TLS_PORT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setWebManagerRedirectHttpOverrideTlsPort(Integer webManagerRedirectHttpOverrideTlsPort) {
    this.webManagerRedirectHttpOverrideTlsPort = webManagerRedirectHttpOverrideTlsPort;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Broker broker = (Broker) o;
    return Objects.equals(this.authClientCertRevocationCheckMode, broker.authClientCertRevocationCheckMode) &&
        Objects.equals(this.configSyncAuthenticationClientCertMaxChainDepth, broker.configSyncAuthenticationClientCertMaxChainDepth) &&
        Objects.equals(this.configSyncAuthenticationClientCertValidateDateEnabled, broker.configSyncAuthenticationClientCertValidateDateEnabled) &&
        Objects.equals(this.configSyncClientProfileTcpInitialCongestionWindow, broker.configSyncClientProfileTcpInitialCongestionWindow) &&
        Objects.equals(this.configSyncClientProfileTcpKeepaliveCount, broker.configSyncClientProfileTcpKeepaliveCount) &&
        Objects.equals(this.configSyncClientProfileTcpKeepaliveIdle, broker.configSyncClientProfileTcpKeepaliveIdle) &&
        Objects.equals(this.configSyncClientProfileTcpKeepaliveInterval, broker.configSyncClientProfileTcpKeepaliveInterval) &&
        Objects.equals(this.configSyncClientProfileTcpMaxWindow, broker.configSyncClientProfileTcpMaxWindow) &&
        Objects.equals(this.configSyncClientProfileTcpMss, broker.configSyncClientProfileTcpMss) &&
        Objects.equals(this.configSyncEnabled, broker.configSyncEnabled) &&
        Objects.equals(this.configSyncSynchronizeUsernameEnabled, broker.configSyncSynchronizeUsernameEnabled) &&
        Objects.equals(this.configSyncTlsEnabled, broker.configSyncTlsEnabled) &&
        Objects.equals(this.guaranteedMsgingDefragmentationScheduleDayList, broker.guaranteedMsgingDefragmentationScheduleDayList) &&
        Objects.equals(this.guaranteedMsgingDefragmentationScheduleEnabled, broker.guaranteedMsgingDefragmentationScheduleEnabled) &&
        Objects.equals(this.guaranteedMsgingDefragmentationScheduleTimeList, broker.guaranteedMsgingDefragmentationScheduleTimeList) &&
        Objects.equals(this.guaranteedMsgingDefragmentationThresholdEnabled, broker.guaranteedMsgingDefragmentationThresholdEnabled) &&
        Objects.equals(this.guaranteedMsgingDefragmentationThresholdFragmentationPercentage, broker.guaranteedMsgingDefragmentationThresholdFragmentationPercentage) &&
        Objects.equals(this.guaranteedMsgingDefragmentationThresholdMinInterval, broker.guaranteedMsgingDefragmentationThresholdMinInterval) &&
        Objects.equals(this.guaranteedMsgingDefragmentationThresholdUsagePercentage, broker.guaranteedMsgingDefragmentationThresholdUsagePercentage) &&
        Objects.equals(this.guaranteedMsgingEnabled, broker.guaranteedMsgingEnabled) &&
        Objects.equals(this.guaranteedMsgingEventCacheUsageThreshold, broker.guaranteedMsgingEventCacheUsageThreshold) &&
        Objects.equals(this.guaranteedMsgingEventDeliveredUnackedThreshold, broker.guaranteedMsgingEventDeliveredUnackedThreshold) &&
        Objects.equals(this.guaranteedMsgingEventDiskUsageThreshold, broker.guaranteedMsgingEventDiskUsageThreshold) &&
        Objects.equals(this.guaranteedMsgingEventEgressFlowCountThreshold, broker.guaranteedMsgingEventEgressFlowCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventEndpointCountThreshold, broker.guaranteedMsgingEventEndpointCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventIngressFlowCountThreshold, broker.guaranteedMsgingEventIngressFlowCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventMsgCountThreshold, broker.guaranteedMsgingEventMsgCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventMsgSpoolFileCountThreshold, broker.guaranteedMsgingEventMsgSpoolFileCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventMsgSpoolUsageThreshold, broker.guaranteedMsgingEventMsgSpoolUsageThreshold) &&
        Objects.equals(this.guaranteedMsgingEventTransactedSessionCountThreshold, broker.guaranteedMsgingEventTransactedSessionCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventTransactedSessionResourceCountThreshold, broker.guaranteedMsgingEventTransactedSessionResourceCountThreshold) &&
        Objects.equals(this.guaranteedMsgingEventTransactionCountThreshold, broker.guaranteedMsgingEventTransactionCountThreshold) &&
        Objects.equals(this.guaranteedMsgingMaxCacheUsage, broker.guaranteedMsgingMaxCacheUsage) &&
        Objects.equals(this.guaranteedMsgingMaxMsgSpoolUsage, broker.guaranteedMsgingMaxMsgSpoolUsage) &&
        Objects.equals(this.guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout, broker.guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout) &&
        Objects.equals(this.guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout, broker.guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout) &&
        Objects.equals(this.guaranteedMsgingTransactionReplicationCompatibilityMode, broker.guaranteedMsgingTransactionReplicationCompatibilityMode) &&
        Objects.equals(this.oauthProfileDefault, broker.oauthProfileDefault) &&
        Objects.equals(this.serviceAmqpEnabled, broker.serviceAmqpEnabled) &&
        Objects.equals(this.serviceAmqpTlsListenPort, broker.serviceAmqpTlsListenPort) &&
        Objects.equals(this.serviceEventConnectionCountThreshold, broker.serviceEventConnectionCountThreshold) &&
        Objects.equals(this.serviceHealthCheckEnabled, broker.serviceHealthCheckEnabled) &&
        Objects.equals(this.serviceHealthCheckListenPort, broker.serviceHealthCheckListenPort) &&
        Objects.equals(this.serviceMateLinkEnabled, broker.serviceMateLinkEnabled) &&
        Objects.equals(this.serviceMateLinkListenPort, broker.serviceMateLinkListenPort) &&
        Objects.equals(this.serviceMqttEnabled, broker.serviceMqttEnabled) &&
        Objects.equals(this.serviceMsgBackboneEnabled, broker.serviceMsgBackboneEnabled) &&
        Objects.equals(this.serviceRedundancyEnabled, broker.serviceRedundancyEnabled) &&
        Objects.equals(this.serviceRedundancyFirstListenPort, broker.serviceRedundancyFirstListenPort) &&
        Objects.equals(this.serviceRestEventOutgoingConnectionCountThreshold, broker.serviceRestEventOutgoingConnectionCountThreshold) &&
        Objects.equals(this.serviceRestIncomingEnabled, broker.serviceRestIncomingEnabled) &&
        Objects.equals(this.serviceRestOutgoingEnabled, broker.serviceRestOutgoingEnabled) &&
        Objects.equals(this.serviceSempCorsAllowAnyHostEnabled, broker.serviceSempCorsAllowAnyHostEnabled) &&
        Objects.equals(this.serviceSempLegacyTimeoutEnabled, broker.serviceSempLegacyTimeoutEnabled) &&
        Objects.equals(this.serviceSempPlainTextEnabled, broker.serviceSempPlainTextEnabled) &&
        Objects.equals(this.serviceSempPlainTextListenPort, broker.serviceSempPlainTextListenPort) &&
        Objects.equals(this.serviceSempSessionIdleTimeout, broker.serviceSempSessionIdleTimeout) &&
        Objects.equals(this.serviceSempSessionMaxLifetime, broker.serviceSempSessionMaxLifetime) &&
        Objects.equals(this.serviceSempTlsEnabled, broker.serviceSempTlsEnabled) &&
        Objects.equals(this.serviceSempTlsListenPort, broker.serviceSempTlsListenPort) &&
        Objects.equals(this.serviceSmfCompressionListenPort, broker.serviceSmfCompressionListenPort) &&
        Objects.equals(this.serviceSmfEnabled, broker.serviceSmfEnabled) &&
        Objects.equals(this.serviceSmfEventConnectionCountThreshold, broker.serviceSmfEventConnectionCountThreshold) &&
        Objects.equals(this.serviceSmfPlainTextListenPort, broker.serviceSmfPlainTextListenPort) &&
        Objects.equals(this.serviceSmfRoutingControlListenPort, broker.serviceSmfRoutingControlListenPort) &&
        Objects.equals(this.serviceSmfTlsListenPort, broker.serviceSmfTlsListenPort) &&
        Objects.equals(this.serviceTlsEventConnectionCountThreshold, broker.serviceTlsEventConnectionCountThreshold) &&
        Objects.equals(this.serviceWebTransportEnabled, broker.serviceWebTransportEnabled) &&
        Objects.equals(this.serviceWebTransportPlainTextListenPort, broker.serviceWebTransportPlainTextListenPort) &&
        Objects.equals(this.serviceWebTransportTlsListenPort, broker.serviceWebTransportTlsListenPort) &&
        Objects.equals(this.serviceWebTransportWebUrlSuffix, broker.serviceWebTransportWebUrlSuffix) &&
        Objects.equals(this.tlsBlockVersion11Enabled, broker.tlsBlockVersion11Enabled) &&
        Objects.equals(this.tlsCipherSuiteManagementList, broker.tlsCipherSuiteManagementList) &&
        Objects.equals(this.tlsCipherSuiteMsgBackboneList, broker.tlsCipherSuiteMsgBackboneList) &&
        Objects.equals(this.tlsCipherSuiteSecureShellList, broker.tlsCipherSuiteSecureShellList) &&
        Objects.equals(this.tlsCrimeExploitProtectionEnabled, broker.tlsCrimeExploitProtectionEnabled) &&
        Objects.equals(this.tlsServerCertContent, broker.tlsServerCertContent) &&
        Objects.equals(this.tlsServerCertPassword, broker.tlsServerCertPassword) &&
        Objects.equals(this.tlsStandardDomainCertificateAuthoritiesEnabled, broker.tlsStandardDomainCertificateAuthoritiesEnabled) &&
        Objects.equals(this.tlsTicketLifetime, broker.tlsTicketLifetime) &&
        Objects.equals(this.webManagerAllowUnencryptedWizardsEnabled, broker.webManagerAllowUnencryptedWizardsEnabled) &&
        Objects.equals(this.webManagerCustomization, broker.webManagerCustomization) &&
        Objects.equals(this.webManagerRedirectHttpEnabled, broker.webManagerRedirectHttpEnabled) &&
        Objects.equals(this.webManagerRedirectHttpOverrideTlsPort, broker.webManagerRedirectHttpOverrideTlsPort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authClientCertRevocationCheckMode, configSyncAuthenticationClientCertMaxChainDepth, configSyncAuthenticationClientCertValidateDateEnabled, configSyncClientProfileTcpInitialCongestionWindow, configSyncClientProfileTcpKeepaliveCount, configSyncClientProfileTcpKeepaliveIdle, configSyncClientProfileTcpKeepaliveInterval, configSyncClientProfileTcpMaxWindow, configSyncClientProfileTcpMss, configSyncEnabled, configSyncSynchronizeUsernameEnabled, configSyncTlsEnabled, guaranteedMsgingDefragmentationScheduleDayList, guaranteedMsgingDefragmentationScheduleEnabled, guaranteedMsgingDefragmentationScheduleTimeList, guaranteedMsgingDefragmentationThresholdEnabled, guaranteedMsgingDefragmentationThresholdFragmentationPercentage, guaranteedMsgingDefragmentationThresholdMinInterval, guaranteedMsgingDefragmentationThresholdUsagePercentage, guaranteedMsgingEnabled, guaranteedMsgingEventCacheUsageThreshold, guaranteedMsgingEventDeliveredUnackedThreshold, guaranteedMsgingEventDiskUsageThreshold, guaranteedMsgingEventEgressFlowCountThreshold, guaranteedMsgingEventEndpointCountThreshold, guaranteedMsgingEventIngressFlowCountThreshold, guaranteedMsgingEventMsgCountThreshold, guaranteedMsgingEventMsgSpoolFileCountThreshold, guaranteedMsgingEventMsgSpoolUsageThreshold, guaranteedMsgingEventTransactedSessionCountThreshold, guaranteedMsgingEventTransactedSessionResourceCountThreshold, guaranteedMsgingEventTransactionCountThreshold, guaranteedMsgingMaxCacheUsage, guaranteedMsgingMaxMsgSpoolUsage, guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout, guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout, guaranteedMsgingTransactionReplicationCompatibilityMode, oauthProfileDefault, serviceAmqpEnabled, serviceAmqpTlsListenPort, serviceEventConnectionCountThreshold, serviceHealthCheckEnabled, serviceHealthCheckListenPort, serviceMateLinkEnabled, serviceMateLinkListenPort, serviceMqttEnabled, serviceMsgBackboneEnabled, serviceRedundancyEnabled, serviceRedundancyFirstListenPort, serviceRestEventOutgoingConnectionCountThreshold, serviceRestIncomingEnabled, serviceRestOutgoingEnabled, serviceSempCorsAllowAnyHostEnabled, serviceSempLegacyTimeoutEnabled, serviceSempPlainTextEnabled, serviceSempPlainTextListenPort, serviceSempSessionIdleTimeout, serviceSempSessionMaxLifetime, serviceSempTlsEnabled, serviceSempTlsListenPort, serviceSmfCompressionListenPort, serviceSmfEnabled, serviceSmfEventConnectionCountThreshold, serviceSmfPlainTextListenPort, serviceSmfRoutingControlListenPort, serviceSmfTlsListenPort, serviceTlsEventConnectionCountThreshold, serviceWebTransportEnabled, serviceWebTransportPlainTextListenPort, serviceWebTransportTlsListenPort, serviceWebTransportWebUrlSuffix, tlsBlockVersion11Enabled, tlsCipherSuiteManagementList, tlsCipherSuiteMsgBackboneList, tlsCipherSuiteSecureShellList, tlsCrimeExploitProtectionEnabled, tlsServerCertContent, tlsServerCertPassword, tlsStandardDomainCertificateAuthoritiesEnabled, tlsTicketLifetime, webManagerAllowUnencryptedWizardsEnabled, webManagerCustomization, webManagerRedirectHttpEnabled, webManagerRedirectHttpOverrideTlsPort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Broker {\n");
    sb.append("    authClientCertRevocationCheckMode: ").append(toIndentedString(authClientCertRevocationCheckMode)).append("\n");
    sb.append("    configSyncAuthenticationClientCertMaxChainDepth: ").append(toIndentedString(configSyncAuthenticationClientCertMaxChainDepth)).append("\n");
    sb.append("    configSyncAuthenticationClientCertValidateDateEnabled: ").append(toIndentedString(configSyncAuthenticationClientCertValidateDateEnabled)).append("\n");
    sb.append("    configSyncClientProfileTcpInitialCongestionWindow: ").append(toIndentedString(configSyncClientProfileTcpInitialCongestionWindow)).append("\n");
    sb.append("    configSyncClientProfileTcpKeepaliveCount: ").append(toIndentedString(configSyncClientProfileTcpKeepaliveCount)).append("\n");
    sb.append("    configSyncClientProfileTcpKeepaliveIdle: ").append(toIndentedString(configSyncClientProfileTcpKeepaliveIdle)).append("\n");
    sb.append("    configSyncClientProfileTcpKeepaliveInterval: ").append(toIndentedString(configSyncClientProfileTcpKeepaliveInterval)).append("\n");
    sb.append("    configSyncClientProfileTcpMaxWindow: ").append(toIndentedString(configSyncClientProfileTcpMaxWindow)).append("\n");
    sb.append("    configSyncClientProfileTcpMss: ").append(toIndentedString(configSyncClientProfileTcpMss)).append("\n");
    sb.append("    configSyncEnabled: ").append(toIndentedString(configSyncEnabled)).append("\n");
    sb.append("    configSyncSynchronizeUsernameEnabled: ").append(toIndentedString(configSyncSynchronizeUsernameEnabled)).append("\n");
    sb.append("    configSyncTlsEnabled: ").append(toIndentedString(configSyncTlsEnabled)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationScheduleDayList: ").append(toIndentedString(guaranteedMsgingDefragmentationScheduleDayList)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationScheduleEnabled: ").append(toIndentedString(guaranteedMsgingDefragmentationScheduleEnabled)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationScheduleTimeList: ").append(toIndentedString(guaranteedMsgingDefragmentationScheduleTimeList)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationThresholdEnabled: ").append(toIndentedString(guaranteedMsgingDefragmentationThresholdEnabled)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationThresholdFragmentationPercentage: ").append(toIndentedString(guaranteedMsgingDefragmentationThresholdFragmentationPercentage)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationThresholdMinInterval: ").append(toIndentedString(guaranteedMsgingDefragmentationThresholdMinInterval)).append("\n");
    sb.append("    guaranteedMsgingDefragmentationThresholdUsagePercentage: ").append(toIndentedString(guaranteedMsgingDefragmentationThresholdUsagePercentage)).append("\n");
    sb.append("    guaranteedMsgingEnabled: ").append(toIndentedString(guaranteedMsgingEnabled)).append("\n");
    sb.append("    guaranteedMsgingEventCacheUsageThreshold: ").append(toIndentedString(guaranteedMsgingEventCacheUsageThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventDeliveredUnackedThreshold: ").append(toIndentedString(guaranteedMsgingEventDeliveredUnackedThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventDiskUsageThreshold: ").append(toIndentedString(guaranteedMsgingEventDiskUsageThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventEgressFlowCountThreshold: ").append(toIndentedString(guaranteedMsgingEventEgressFlowCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventEndpointCountThreshold: ").append(toIndentedString(guaranteedMsgingEventEndpointCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventIngressFlowCountThreshold: ").append(toIndentedString(guaranteedMsgingEventIngressFlowCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventMsgCountThreshold: ").append(toIndentedString(guaranteedMsgingEventMsgCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventMsgSpoolFileCountThreshold: ").append(toIndentedString(guaranteedMsgingEventMsgSpoolFileCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventMsgSpoolUsageThreshold: ").append(toIndentedString(guaranteedMsgingEventMsgSpoolUsageThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventTransactedSessionCountThreshold: ").append(toIndentedString(guaranteedMsgingEventTransactedSessionCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventTransactedSessionResourceCountThreshold: ").append(toIndentedString(guaranteedMsgingEventTransactedSessionResourceCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingEventTransactionCountThreshold: ").append(toIndentedString(guaranteedMsgingEventTransactionCountThreshold)).append("\n");
    sb.append("    guaranteedMsgingMaxCacheUsage: ").append(toIndentedString(guaranteedMsgingMaxCacheUsage)).append("\n");
    sb.append("    guaranteedMsgingMaxMsgSpoolUsage: ").append(toIndentedString(guaranteedMsgingMaxMsgSpoolUsage)).append("\n");
    sb.append("    guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout: ").append(toIndentedString(guaranteedMsgingMsgSpoolSyncMirroredMsgAckTimeout)).append("\n");
    sb.append("    guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout: ").append(toIndentedString(guaranteedMsgingMsgSpoolSyncMirroredSpoolFileAckTimeout)).append("\n");
    sb.append("    guaranteedMsgingTransactionReplicationCompatibilityMode: ").append(toIndentedString(guaranteedMsgingTransactionReplicationCompatibilityMode)).append("\n");
    sb.append("    oauthProfileDefault: ").append(toIndentedString(oauthProfileDefault)).append("\n");
    sb.append("    serviceAmqpEnabled: ").append(toIndentedString(serviceAmqpEnabled)).append("\n");
    sb.append("    serviceAmqpTlsListenPort: ").append(toIndentedString(serviceAmqpTlsListenPort)).append("\n");
    sb.append("    serviceEventConnectionCountThreshold: ").append(toIndentedString(serviceEventConnectionCountThreshold)).append("\n");
    sb.append("    serviceHealthCheckEnabled: ").append(toIndentedString(serviceHealthCheckEnabled)).append("\n");
    sb.append("    serviceHealthCheckListenPort: ").append(toIndentedString(serviceHealthCheckListenPort)).append("\n");
    sb.append("    serviceMateLinkEnabled: ").append(toIndentedString(serviceMateLinkEnabled)).append("\n");
    sb.append("    serviceMateLinkListenPort: ").append(toIndentedString(serviceMateLinkListenPort)).append("\n");
    sb.append("    serviceMqttEnabled: ").append(toIndentedString(serviceMqttEnabled)).append("\n");
    sb.append("    serviceMsgBackboneEnabled: ").append(toIndentedString(serviceMsgBackboneEnabled)).append("\n");
    sb.append("    serviceRedundancyEnabled: ").append(toIndentedString(serviceRedundancyEnabled)).append("\n");
    sb.append("    serviceRedundancyFirstListenPort: ").append(toIndentedString(serviceRedundancyFirstListenPort)).append("\n");
    sb.append("    serviceRestEventOutgoingConnectionCountThreshold: ").append(toIndentedString(serviceRestEventOutgoingConnectionCountThreshold)).append("\n");
    sb.append("    serviceRestIncomingEnabled: ").append(toIndentedString(serviceRestIncomingEnabled)).append("\n");
    sb.append("    serviceRestOutgoingEnabled: ").append(toIndentedString(serviceRestOutgoingEnabled)).append("\n");
    sb.append("    serviceSempCorsAllowAnyHostEnabled: ").append(toIndentedString(serviceSempCorsAllowAnyHostEnabled)).append("\n");
    sb.append("    serviceSempLegacyTimeoutEnabled: ").append(toIndentedString(serviceSempLegacyTimeoutEnabled)).append("\n");
    sb.append("    serviceSempPlainTextEnabled: ").append(toIndentedString(serviceSempPlainTextEnabled)).append("\n");
    sb.append("    serviceSempPlainTextListenPort: ").append(toIndentedString(serviceSempPlainTextListenPort)).append("\n");
    sb.append("    serviceSempSessionIdleTimeout: ").append(toIndentedString(serviceSempSessionIdleTimeout)).append("\n");
    sb.append("    serviceSempSessionMaxLifetime: ").append(toIndentedString(serviceSempSessionMaxLifetime)).append("\n");
    sb.append("    serviceSempTlsEnabled: ").append(toIndentedString(serviceSempTlsEnabled)).append("\n");
    sb.append("    serviceSempTlsListenPort: ").append(toIndentedString(serviceSempTlsListenPort)).append("\n");
    sb.append("    serviceSmfCompressionListenPort: ").append(toIndentedString(serviceSmfCompressionListenPort)).append("\n");
    sb.append("    serviceSmfEnabled: ").append(toIndentedString(serviceSmfEnabled)).append("\n");
    sb.append("    serviceSmfEventConnectionCountThreshold: ").append(toIndentedString(serviceSmfEventConnectionCountThreshold)).append("\n");
    sb.append("    serviceSmfPlainTextListenPort: ").append(toIndentedString(serviceSmfPlainTextListenPort)).append("\n");
    sb.append("    serviceSmfRoutingControlListenPort: ").append(toIndentedString(serviceSmfRoutingControlListenPort)).append("\n");
    sb.append("    serviceSmfTlsListenPort: ").append(toIndentedString(serviceSmfTlsListenPort)).append("\n");
    sb.append("    serviceTlsEventConnectionCountThreshold: ").append(toIndentedString(serviceTlsEventConnectionCountThreshold)).append("\n");
    sb.append("    serviceWebTransportEnabled: ").append(toIndentedString(serviceWebTransportEnabled)).append("\n");
    sb.append("    serviceWebTransportPlainTextListenPort: ").append(toIndentedString(serviceWebTransportPlainTextListenPort)).append("\n");
    sb.append("    serviceWebTransportTlsListenPort: ").append(toIndentedString(serviceWebTransportTlsListenPort)).append("\n");
    sb.append("    serviceWebTransportWebUrlSuffix: ").append(toIndentedString(serviceWebTransportWebUrlSuffix)).append("\n");
    sb.append("    tlsBlockVersion11Enabled: ").append(toIndentedString(tlsBlockVersion11Enabled)).append("\n");
    sb.append("    tlsCipherSuiteManagementList: ").append(toIndentedString(tlsCipherSuiteManagementList)).append("\n");
    sb.append("    tlsCipherSuiteMsgBackboneList: ").append(toIndentedString(tlsCipherSuiteMsgBackboneList)).append("\n");
    sb.append("    tlsCipherSuiteSecureShellList: ").append(toIndentedString(tlsCipherSuiteSecureShellList)).append("\n");
    sb.append("    tlsCrimeExploitProtectionEnabled: ").append(toIndentedString(tlsCrimeExploitProtectionEnabled)).append("\n");
    sb.append("    tlsServerCertContent: ").append(toIndentedString(tlsServerCertContent)).append("\n");
    sb.append("    tlsServerCertPassword: ").append(toIndentedString(tlsServerCertPassword)).append("\n");
    sb.append("    tlsStandardDomainCertificateAuthoritiesEnabled: ").append(toIndentedString(tlsStandardDomainCertificateAuthoritiesEnabled)).append("\n");
    sb.append("    tlsTicketLifetime: ").append(toIndentedString(tlsTicketLifetime)).append("\n");
    sb.append("    webManagerAllowUnencryptedWizardsEnabled: ").append(toIndentedString(webManagerAllowUnencryptedWizardsEnabled)).append("\n");
    sb.append("    webManagerCustomization: ").append(toIndentedString(webManagerCustomization)).append("\n");
    sb.append("    webManagerRedirectHttpEnabled: ").append(toIndentedString(webManagerRedirectHttpEnabled)).append("\n");
    sb.append("    webManagerRedirectHttpOverrideTlsPort: ").append(toIndentedString(webManagerRedirectHttpOverrideTlsPort)).append("\n");
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

