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
 * MsgVpnClientProfile
 */
@JsonPropertyOrder({
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_BRIDGE_CONNECTIONS_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_CUT_THROUGH_FORWARDING_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_DURABILITY,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_GUARANTEED_MSG_RECEIVE_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_GUARANTEED_MSG_SEND_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_SHARED_SUBSCRIPTIONS_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ALLOW_TRANSACTED_SESSIONS_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_NAME,
  MsgVpnClientProfile.JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME,
  MsgVpnClientProfile.JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_NAME,
  MsgVpnClientProfile.JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME,
  MsgVpnClientProfile.JSON_PROPERTY_CLIENT_PROFILE_NAME,
  MsgVpnClientProfile.JSON_PROPERTY_COMPRESSION_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ELIDING_DELAY,
  MsgVpnClientProfile.JSON_PROPERTY_ELIDING_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_ELIDING_MAX_TOPIC_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_CLIENT_PROVISIONED_ENDPOINT_SPOOL_USAGE_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_EGRESS_FLOW_COUNT_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_ENDPOINT_COUNT_PER_CLIENT_USERNAME_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_INGRESS_FLOW_COUNT_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_SUBSCRIPTION_COUNT_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_EVENT_TRANSACTION_COUNT_THRESHOLD,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_EGRESS_FLOW_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_ENDPOINT_COUNT_PER_CLIENT_USERNAME,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_INGRESS_FLOW_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_MSGS_PER_TRANSACTION,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_SUBSCRIPTION_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_TRANSACTED_SESSION_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_MAX_TRANSACTION_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_CONTROL1_MAX_DEPTH,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_CONTROL1_MIN_MSG_BURST,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_DIRECT1_MAX_DEPTH,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_DIRECT1_MIN_MSG_BURST,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_DIRECT2_MAX_DEPTH,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_DIRECT2_MIN_MSG_BURST,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_DIRECT3_MAX_DEPTH,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_DIRECT3_MIN_MSG_BURST,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_GUARANTEED1_MAX_DEPTH,
  MsgVpnClientProfile.JSON_PROPERTY_QUEUE_GUARANTEED1_MIN_MSG_BURST,
  MsgVpnClientProfile.JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_NO_SUBSCRIPTION_MATCH_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_REPLICATION_ALLOW_CLIENT_CONNECT_WHEN_STANDBY_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_SERVICE_MIN_KEEPALIVE_TIMEOUT,
  MsgVpnClientProfile.JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME,
  MsgVpnClientProfile.JSON_PROPERTY_SERVICE_SMF_MIN_KEEPALIVE_ENABLED,
  MsgVpnClientProfile.JSON_PROPERTY_SERVICE_WEB_INACTIVE_TIMEOUT,
  MsgVpnClientProfile.JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME,
  MsgVpnClientProfile.JSON_PROPERTY_SERVICE_WEB_MAX_PAYLOAD,
  MsgVpnClientProfile.JSON_PROPERTY_TCP_CONGESTION_WINDOW_SIZE,
  MsgVpnClientProfile.JSON_PROPERTY_TCP_KEEPALIVE_COUNT,
  MsgVpnClientProfile.JSON_PROPERTY_TCP_KEEPALIVE_IDLE_TIME,
  MsgVpnClientProfile.JSON_PROPERTY_TCP_KEEPALIVE_INTERVAL,
  MsgVpnClientProfile.JSON_PROPERTY_TCP_MAX_SEGMENT_SIZE,
  MsgVpnClientProfile.JSON_PROPERTY_TCP_MAX_WINDOW_SIZE,
  MsgVpnClientProfile.JSON_PROPERTY_TLS_ALLOW_DOWNGRADE_TO_PLAIN_TEXT_ENABLED
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class MsgVpnClientProfile {
  public static final String JSON_PROPERTY_ALLOW_BRIDGE_CONNECTIONS_ENABLED = "allowBridgeConnectionsEnabled";
  private Boolean allowBridgeConnectionsEnabled;

  public static final String JSON_PROPERTY_ALLOW_CUT_THROUGH_FORWARDING_ENABLED = "allowCutThroughForwardingEnabled";
  private Boolean allowCutThroughForwardingEnabled;

  /**
   * The types of Queues and Topic Endpoints that clients using the client-profile can create. Changing this value does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;all\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;all\&quot; - Client can create any type of endpoint. \&quot;durable\&quot; - Client can create only durable endpoints. \&quot;non-durable\&quot; - Client can create only non-durable endpoints. &lt;/pre&gt;  Available since 2.14.
   */
  public enum AllowGuaranteedEndpointCreateDurabilityEnum {
    ALL("all"),
    
    DURABLE("durable"),
    
    NON_DURABLE("non-durable");

    private String value;

    AllowGuaranteedEndpointCreateDurabilityEnum(String value) {
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
    public static AllowGuaranteedEndpointCreateDurabilityEnum fromValue(String value) {
      for (AllowGuaranteedEndpointCreateDurabilityEnum b : AllowGuaranteedEndpointCreateDurabilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_DURABILITY = "allowGuaranteedEndpointCreateDurability";
  private AllowGuaranteedEndpointCreateDurabilityEnum allowGuaranteedEndpointCreateDurability;

  public static final String JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_ENABLED = "allowGuaranteedEndpointCreateEnabled";
  private Boolean allowGuaranteedEndpointCreateEnabled;

  public static final String JSON_PROPERTY_ALLOW_GUARANTEED_MSG_RECEIVE_ENABLED = "allowGuaranteedMsgReceiveEnabled";
  private Boolean allowGuaranteedMsgReceiveEnabled;

  public static final String JSON_PROPERTY_ALLOW_GUARANTEED_MSG_SEND_ENABLED = "allowGuaranteedMsgSendEnabled";
  private Boolean allowGuaranteedMsgSendEnabled;

  public static final String JSON_PROPERTY_ALLOW_SHARED_SUBSCRIPTIONS_ENABLED = "allowSharedSubscriptionsEnabled";
  private Boolean allowSharedSubscriptionsEnabled;

  public static final String JSON_PROPERTY_ALLOW_TRANSACTED_SESSIONS_ENABLED = "allowTransactedSessionsEnabled";
  private Boolean allowTransactedSessionsEnabled;

  public static final String JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_NAME = "apiQueueManagementCopyFromOnCreateName";
  private String apiQueueManagementCopyFromOnCreateName;

  public static final String JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME = "apiQueueManagementCopyFromOnCreateTemplateName";
  private String apiQueueManagementCopyFromOnCreateTemplateName;

  public static final String JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_NAME = "apiTopicEndpointManagementCopyFromOnCreateName";
  private String apiTopicEndpointManagementCopyFromOnCreateName;

  public static final String JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME = "apiTopicEndpointManagementCopyFromOnCreateTemplateName";
  private String apiTopicEndpointManagementCopyFromOnCreateTemplateName;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_NAME = "clientProfileName";
  private String clientProfileName;

  public static final String JSON_PROPERTY_COMPRESSION_ENABLED = "compressionEnabled";
  private Boolean compressionEnabled;

  public static final String JSON_PROPERTY_ELIDING_DELAY = "elidingDelay";
  private Long elidingDelay;

  public static final String JSON_PROPERTY_ELIDING_ENABLED = "elidingEnabled";
  private Boolean elidingEnabled;

  public static final String JSON_PROPERTY_ELIDING_MAX_TOPIC_COUNT = "elidingMaxTopicCount";
  private Long elidingMaxTopicCount;

  public static final String JSON_PROPERTY_EVENT_CLIENT_PROVISIONED_ENDPOINT_SPOOL_USAGE_THRESHOLD = "eventClientProvisionedEndpointSpoolUsageThreshold";
  private EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold;

  public static final String JSON_PROPERTY_EVENT_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD = "eventConnectionCountPerClientUsernameThreshold";
  private EventThreshold eventConnectionCountPerClientUsernameThreshold;

  public static final String JSON_PROPERTY_EVENT_EGRESS_FLOW_COUNT_THRESHOLD = "eventEgressFlowCountThreshold";
  private EventThreshold eventEgressFlowCountThreshold;

  public static final String JSON_PROPERTY_EVENT_ENDPOINT_COUNT_PER_CLIENT_USERNAME_THRESHOLD = "eventEndpointCountPerClientUsernameThreshold";
  private EventThreshold eventEndpointCountPerClientUsernameThreshold;

  public static final String JSON_PROPERTY_EVENT_INGRESS_FLOW_COUNT_THRESHOLD = "eventIngressFlowCountThreshold";
  private EventThreshold eventIngressFlowCountThreshold;

  public static final String JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD = "eventServiceSmfConnectionCountPerClientUsernameThreshold";
  private EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold;

  public static final String JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD = "eventServiceWebConnectionCountPerClientUsernameThreshold";
  private EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold;

  public static final String JSON_PROPERTY_EVENT_SUBSCRIPTION_COUNT_THRESHOLD = "eventSubscriptionCountThreshold";
  private EventThreshold eventSubscriptionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_TRANSACTED_SESSION_COUNT_THRESHOLD = "eventTransactedSessionCountThreshold";
  private EventThreshold eventTransactedSessionCountThreshold;

  public static final String JSON_PROPERTY_EVENT_TRANSACTION_COUNT_THRESHOLD = "eventTransactionCountThreshold";
  private EventThreshold eventTransactionCountThreshold;

  public static final String JSON_PROPERTY_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME = "maxConnectionCountPerClientUsername";
  private Long maxConnectionCountPerClientUsername;

  public static final String JSON_PROPERTY_MAX_EGRESS_FLOW_COUNT = "maxEgressFlowCount";
  private Long maxEgressFlowCount;

  public static final String JSON_PROPERTY_MAX_ENDPOINT_COUNT_PER_CLIENT_USERNAME = "maxEndpointCountPerClientUsername";
  private Long maxEndpointCountPerClientUsername;

  public static final String JSON_PROPERTY_MAX_INGRESS_FLOW_COUNT = "maxIngressFlowCount";
  private Long maxIngressFlowCount;

  public static final String JSON_PROPERTY_MAX_MSGS_PER_TRANSACTION = "maxMsgsPerTransaction";
  private Integer maxMsgsPerTransaction;

  public static final String JSON_PROPERTY_MAX_SUBSCRIPTION_COUNT = "maxSubscriptionCount";
  private Long maxSubscriptionCount;

  public static final String JSON_PROPERTY_MAX_TRANSACTED_SESSION_COUNT = "maxTransactedSessionCount";
  private Long maxTransactedSessionCount;

  public static final String JSON_PROPERTY_MAX_TRANSACTION_COUNT = "maxTransactionCount";
  private Long maxTransactionCount;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_QUEUE_CONTROL1_MAX_DEPTH = "queueControl1MaxDepth";
  private Integer queueControl1MaxDepth;

  public static final String JSON_PROPERTY_QUEUE_CONTROL1_MIN_MSG_BURST = "queueControl1MinMsgBurst";
  private Integer queueControl1MinMsgBurst;

  public static final String JSON_PROPERTY_QUEUE_DIRECT1_MAX_DEPTH = "queueDirect1MaxDepth";
  private Integer queueDirect1MaxDepth;

  public static final String JSON_PROPERTY_QUEUE_DIRECT1_MIN_MSG_BURST = "queueDirect1MinMsgBurst";
  private Integer queueDirect1MinMsgBurst;

  public static final String JSON_PROPERTY_QUEUE_DIRECT2_MAX_DEPTH = "queueDirect2MaxDepth";
  private Integer queueDirect2MaxDepth;

  public static final String JSON_PROPERTY_QUEUE_DIRECT2_MIN_MSG_BURST = "queueDirect2MinMsgBurst";
  private Integer queueDirect2MinMsgBurst;

  public static final String JSON_PROPERTY_QUEUE_DIRECT3_MAX_DEPTH = "queueDirect3MaxDepth";
  private Integer queueDirect3MaxDepth;

  public static final String JSON_PROPERTY_QUEUE_DIRECT3_MIN_MSG_BURST = "queueDirect3MinMsgBurst";
  private Integer queueDirect3MinMsgBurst;

  public static final String JSON_PROPERTY_QUEUE_GUARANTEED1_MAX_DEPTH = "queueGuaranteed1MaxDepth";
  private Integer queueGuaranteed1MaxDepth;

  public static final String JSON_PROPERTY_QUEUE_GUARANTEED1_MIN_MSG_BURST = "queueGuaranteed1MinMsgBurst";
  private Integer queueGuaranteed1MinMsgBurst;

  public static final String JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_NO_SUBSCRIPTION_MATCH_ENABLED = "rejectMsgToSenderOnNoSubscriptionMatchEnabled";
  private Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled;

  public static final String JSON_PROPERTY_REPLICATION_ALLOW_CLIENT_CONNECT_WHEN_STANDBY_ENABLED = "replicationAllowClientConnectWhenStandbyEnabled";
  private Boolean replicationAllowClientConnectWhenStandbyEnabled;

  public static final String JSON_PROPERTY_SERVICE_MIN_KEEPALIVE_TIMEOUT = "serviceMinKeepaliveTimeout";
  private Integer serviceMinKeepaliveTimeout;

  public static final String JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME = "serviceSmfMaxConnectionCountPerClientUsername";
  private Long serviceSmfMaxConnectionCountPerClientUsername;

  public static final String JSON_PROPERTY_SERVICE_SMF_MIN_KEEPALIVE_ENABLED = "serviceSmfMinKeepaliveEnabled";
  private Boolean serviceSmfMinKeepaliveEnabled;

  public static final String JSON_PROPERTY_SERVICE_WEB_INACTIVE_TIMEOUT = "serviceWebInactiveTimeout";
  private Long serviceWebInactiveTimeout;

  public static final String JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME = "serviceWebMaxConnectionCountPerClientUsername";
  private Long serviceWebMaxConnectionCountPerClientUsername;

  public static final String JSON_PROPERTY_SERVICE_WEB_MAX_PAYLOAD = "serviceWebMaxPayload";
  private Long serviceWebMaxPayload;

  public static final String JSON_PROPERTY_TCP_CONGESTION_WINDOW_SIZE = "tcpCongestionWindowSize";
  private Long tcpCongestionWindowSize;

  public static final String JSON_PROPERTY_TCP_KEEPALIVE_COUNT = "tcpKeepaliveCount";
  private Long tcpKeepaliveCount;

  public static final String JSON_PROPERTY_TCP_KEEPALIVE_IDLE_TIME = "tcpKeepaliveIdleTime";
  private Long tcpKeepaliveIdleTime;

  public static final String JSON_PROPERTY_TCP_KEEPALIVE_INTERVAL = "tcpKeepaliveInterval";
  private Long tcpKeepaliveInterval;

  public static final String JSON_PROPERTY_TCP_MAX_SEGMENT_SIZE = "tcpMaxSegmentSize";
  private Long tcpMaxSegmentSize;

  public static final String JSON_PROPERTY_TCP_MAX_WINDOW_SIZE = "tcpMaxWindowSize";
  private Long tcpMaxWindowSize;

  public static final String JSON_PROPERTY_TLS_ALLOW_DOWNGRADE_TO_PLAIN_TEXT_ENABLED = "tlsAllowDowngradeToPlainTextEnabled";
  private Boolean tlsAllowDowngradeToPlainTextEnabled;

  public MsgVpnClientProfile() {
  }

  public MsgVpnClientProfile allowBridgeConnectionsEnabled(Boolean allowBridgeConnectionsEnabled) {
    
    this.allowBridgeConnectionsEnabled = allowBridgeConnectionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing Bridge clients using the Client Profile to connect. Changing this setting does not affect existing Bridge client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return allowBridgeConnectionsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_BRIDGE_CONNECTIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowBridgeConnectionsEnabled() {
    return allowBridgeConnectionsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_BRIDGE_CONNECTIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowBridgeConnectionsEnabled(Boolean allowBridgeConnectionsEnabled) {
    this.allowBridgeConnectionsEnabled = allowBridgeConnectionsEnabled;
  }


  public MsgVpnClientProfile allowCutThroughForwardingEnabled(Boolean allowCutThroughForwardingEnabled) {
    
    this.allowCutThroughForwardingEnabled = allowCutThroughForwardingEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to bind to endpoints with the cut-through forwarding delivery mode. Changing this value does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.22. This attribute has been deprecated. Please visit the Solace Product Lifecycle Policy web page for details on deprecated features.
   * @return allowCutThroughForwardingEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_CUT_THROUGH_FORWARDING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowCutThroughForwardingEnabled() {
    return allowCutThroughForwardingEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_CUT_THROUGH_FORWARDING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowCutThroughForwardingEnabled(Boolean allowCutThroughForwardingEnabled) {
    this.allowCutThroughForwardingEnabled = allowCutThroughForwardingEnabled;
  }


  public MsgVpnClientProfile allowGuaranteedEndpointCreateDurability(AllowGuaranteedEndpointCreateDurabilityEnum allowGuaranteedEndpointCreateDurability) {
    
    this.allowGuaranteedEndpointCreateDurability = allowGuaranteedEndpointCreateDurability;
    return this;
  }

   /**
   * The types of Queues and Topic Endpoints that clients using the client-profile can create. Changing this value does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;all\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;all\&quot; - Client can create any type of endpoint. \&quot;durable\&quot; - Client can create only durable endpoints. \&quot;non-durable\&quot; - Client can create only non-durable endpoints. &lt;/pre&gt;  Available since 2.14.
   * @return allowGuaranteedEndpointCreateDurability
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_DURABILITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AllowGuaranteedEndpointCreateDurabilityEnum getAllowGuaranteedEndpointCreateDurability() {
    return allowGuaranteedEndpointCreateDurability;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_DURABILITY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowGuaranteedEndpointCreateDurability(AllowGuaranteedEndpointCreateDurabilityEnum allowGuaranteedEndpointCreateDurability) {
    this.allowGuaranteedEndpointCreateDurability = allowGuaranteedEndpointCreateDurability;
  }


  public MsgVpnClientProfile allowGuaranteedEndpointCreateEnabled(Boolean allowGuaranteedEndpointCreateEnabled) {
    
    this.allowGuaranteedEndpointCreateEnabled = allowGuaranteedEndpointCreateEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to create topic endponts or queues. Changing this value does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return allowGuaranteedEndpointCreateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowGuaranteedEndpointCreateEnabled() {
    return allowGuaranteedEndpointCreateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_ENDPOINT_CREATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowGuaranteedEndpointCreateEnabled(Boolean allowGuaranteedEndpointCreateEnabled) {
    this.allowGuaranteedEndpointCreateEnabled = allowGuaranteedEndpointCreateEnabled;
  }


  public MsgVpnClientProfile allowGuaranteedMsgReceiveEnabled(Boolean allowGuaranteedMsgReceiveEnabled) {
    
    this.allowGuaranteedMsgReceiveEnabled = allowGuaranteedMsgReceiveEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to receive guaranteed messages. Changing this setting does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return allowGuaranteedMsgReceiveEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_MSG_RECEIVE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowGuaranteedMsgReceiveEnabled() {
    return allowGuaranteedMsgReceiveEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_MSG_RECEIVE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowGuaranteedMsgReceiveEnabled(Boolean allowGuaranteedMsgReceiveEnabled) {
    this.allowGuaranteedMsgReceiveEnabled = allowGuaranteedMsgReceiveEnabled;
  }


  public MsgVpnClientProfile allowGuaranteedMsgSendEnabled(Boolean allowGuaranteedMsgSendEnabled) {
    
    this.allowGuaranteedMsgSendEnabled = allowGuaranteedMsgSendEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to send guaranteed messages. Changing this setting does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return allowGuaranteedMsgSendEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_MSG_SEND_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowGuaranteedMsgSendEnabled() {
    return allowGuaranteedMsgSendEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_GUARANTEED_MSG_SEND_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowGuaranteedMsgSendEnabled(Boolean allowGuaranteedMsgSendEnabled) {
    this.allowGuaranteedMsgSendEnabled = allowGuaranteedMsgSendEnabled;
  }


  public MsgVpnClientProfile allowSharedSubscriptionsEnabled(Boolean allowSharedSubscriptionsEnabled) {
    
    this.allowSharedSubscriptionsEnabled = allowSharedSubscriptionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing shared subscriptions. Changing this setting does not affect existing subscriptions. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.11.
   * @return allowSharedSubscriptionsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_SHARED_SUBSCRIPTIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowSharedSubscriptionsEnabled() {
    return allowSharedSubscriptionsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_SHARED_SUBSCRIPTIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowSharedSubscriptionsEnabled(Boolean allowSharedSubscriptionsEnabled) {
    this.allowSharedSubscriptionsEnabled = allowSharedSubscriptionsEnabled;
  }


  public MsgVpnClientProfile allowTransactedSessionsEnabled(Boolean allowTransactedSessionsEnabled) {
    
    this.allowTransactedSessionsEnabled = allowTransactedSessionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to establish transacted sessions. Changing this setting does not affect existing client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return allowTransactedSessionsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALLOW_TRANSACTED_SESSIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAllowTransactedSessionsEnabled() {
    return allowTransactedSessionsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ALLOW_TRANSACTED_SESSIONS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAllowTransactedSessionsEnabled(Boolean allowTransactedSessionsEnabled) {
    this.allowTransactedSessionsEnabled = allowTransactedSessionsEnabled;
  }


  public MsgVpnClientProfile apiQueueManagementCopyFromOnCreateName(String apiQueueManagementCopyFromOnCreateName) {
    
    this.apiQueueManagementCopyFromOnCreateName = apiQueueManagementCopyFromOnCreateName;
    return this;
  }

   /**
   * The name of a queue to copy settings from when a new queue is created by a client using the Client Profile. The referenced queue must exist in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.14. This attribute has been replaced with &#x60;apiQueueManagementCopyFromOnCreateTemplateName&#x60;.
   * @return apiQueueManagementCopyFromOnCreateName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getApiQueueManagementCopyFromOnCreateName() {
    return apiQueueManagementCopyFromOnCreateName;
  }


  @JsonProperty(JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setApiQueueManagementCopyFromOnCreateName(String apiQueueManagementCopyFromOnCreateName) {
    this.apiQueueManagementCopyFromOnCreateName = apiQueueManagementCopyFromOnCreateName;
  }


  public MsgVpnClientProfile apiQueueManagementCopyFromOnCreateTemplateName(String apiQueueManagementCopyFromOnCreateTemplateName) {
    
    this.apiQueueManagementCopyFromOnCreateTemplateName = apiQueueManagementCopyFromOnCreateTemplateName;
    return this;
  }

   /**
   * The name of a queue template to copy settings from when a new queue is created by a client using the Client Profile. If the referenced queue template does not exist, queue creation will fail when it tries to resolve this template. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.14.
   * @return apiQueueManagementCopyFromOnCreateTemplateName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getApiQueueManagementCopyFromOnCreateTemplateName() {
    return apiQueueManagementCopyFromOnCreateTemplateName;
  }


  @JsonProperty(JSON_PROPERTY_API_QUEUE_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setApiQueueManagementCopyFromOnCreateTemplateName(String apiQueueManagementCopyFromOnCreateTemplateName) {
    this.apiQueueManagementCopyFromOnCreateTemplateName = apiQueueManagementCopyFromOnCreateTemplateName;
  }


  public MsgVpnClientProfile apiTopicEndpointManagementCopyFromOnCreateName(String apiTopicEndpointManagementCopyFromOnCreateName) {
    
    this.apiTopicEndpointManagementCopyFromOnCreateName = apiTopicEndpointManagementCopyFromOnCreateName;
    return this;
  }

   /**
   * The name of a topic endpoint to copy settings from when a new topic endpoint is created by a client using the Client Profile. The referenced topic endpoint must exist in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.14. This attribute has been replaced with &#x60;apiTopicEndpointManagementCopyFromOnCreateTemplateName&#x60;.
   * @return apiTopicEndpointManagementCopyFromOnCreateName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getApiTopicEndpointManagementCopyFromOnCreateName() {
    return apiTopicEndpointManagementCopyFromOnCreateName;
  }


  @JsonProperty(JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setApiTopicEndpointManagementCopyFromOnCreateName(String apiTopicEndpointManagementCopyFromOnCreateName) {
    this.apiTopicEndpointManagementCopyFromOnCreateName = apiTopicEndpointManagementCopyFromOnCreateName;
  }


  public MsgVpnClientProfile apiTopicEndpointManagementCopyFromOnCreateTemplateName(String apiTopicEndpointManagementCopyFromOnCreateTemplateName) {
    
    this.apiTopicEndpointManagementCopyFromOnCreateTemplateName = apiTopicEndpointManagementCopyFromOnCreateTemplateName;
    return this;
  }

   /**
   * The name of a topic endpoint template to copy settings from when a new topic endpoint is created by a client using the Client Profile. If the referenced topic endpoint template does not exist, topic endpoint creation will fail when it tries to resolve this template. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.14.
   * @return apiTopicEndpointManagementCopyFromOnCreateTemplateName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getApiTopicEndpointManagementCopyFromOnCreateTemplateName() {
    return apiTopicEndpointManagementCopyFromOnCreateTemplateName;
  }


  @JsonProperty(JSON_PROPERTY_API_TOPIC_ENDPOINT_MANAGEMENT_COPY_FROM_ON_CREATE_TEMPLATE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setApiTopicEndpointManagementCopyFromOnCreateTemplateName(String apiTopicEndpointManagementCopyFromOnCreateTemplateName) {
    this.apiTopicEndpointManagementCopyFromOnCreateTemplateName = apiTopicEndpointManagementCopyFromOnCreateTemplateName;
  }


  public MsgVpnClientProfile clientProfileName(String clientProfileName) {
    
    this.clientProfileName = clientProfileName;
    return this;
  }

   /**
   * The name of the Client Profile.
   * @return clientProfileName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientProfileName() {
    return clientProfileName;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileName(String clientProfileName) {
    this.clientProfileName = clientProfileName;
  }


  public MsgVpnClientProfile compressionEnabled(Boolean compressionEnabled) {
    
    this.compressionEnabled = compressionEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to use compression. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.10.
   * @return compressionEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_COMPRESSION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getCompressionEnabled() {
    return compressionEnabled;
  }


  @JsonProperty(JSON_PROPERTY_COMPRESSION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCompressionEnabled(Boolean compressionEnabled) {
    this.compressionEnabled = compressionEnabled;
  }


  public MsgVpnClientProfile elidingDelay(Long elidingDelay) {
    
    this.elidingDelay = elidingDelay;
    return this;
  }

   /**
   * The amount of time to delay the delivery of messages to clients using the Client Profile after the initial message has been delivered (the eliding delay interval), in milliseconds. A value of 0 means there is no delay in delivering messages to clients. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return elidingDelay
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ELIDING_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getElidingDelay() {
    return elidingDelay;
  }


  @JsonProperty(JSON_PROPERTY_ELIDING_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setElidingDelay(Long elidingDelay) {
    this.elidingDelay = elidingDelay;
  }


  public MsgVpnClientProfile elidingEnabled(Boolean elidingEnabled) {
    
    this.elidingEnabled = elidingEnabled;
    return this;
  }

   /**
   * Enable or disable message eliding for clients using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return elidingEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ELIDING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getElidingEnabled() {
    return elidingEnabled;
  }


  @JsonProperty(JSON_PROPERTY_ELIDING_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setElidingEnabled(Boolean elidingEnabled) {
    this.elidingEnabled = elidingEnabled;
  }


  public MsgVpnClientProfile elidingMaxTopicCount(Long elidingMaxTopicCount) {
    
    this.elidingMaxTopicCount = elidingMaxTopicCount;
    return this;
  }

   /**
   * The maximum number of topics tracked for message eliding per client connection using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;256&#x60;.
   * @return elidingMaxTopicCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ELIDING_MAX_TOPIC_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getElidingMaxTopicCount() {
    return elidingMaxTopicCount;
  }


  @JsonProperty(JSON_PROPERTY_ELIDING_MAX_TOPIC_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setElidingMaxTopicCount(Long elidingMaxTopicCount) {
    this.elidingMaxTopicCount = elidingMaxTopicCount;
  }


  public MsgVpnClientProfile eventClientProvisionedEndpointSpoolUsageThreshold(EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold) {
    
    this.eventClientProvisionedEndpointSpoolUsageThreshold = eventClientProvisionedEndpointSpoolUsageThreshold;
    return this;
  }

   /**
   * Get eventClientProvisionedEndpointSpoolUsageThreshold
   * @return eventClientProvisionedEndpointSpoolUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_CLIENT_PROVISIONED_ENDPOINT_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThresholdByPercent getEventClientProvisionedEndpointSpoolUsageThreshold() {
    return eventClientProvisionedEndpointSpoolUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_CLIENT_PROVISIONED_ENDPOINT_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventClientProvisionedEndpointSpoolUsageThreshold(EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold) {
    this.eventClientProvisionedEndpointSpoolUsageThreshold = eventClientProvisionedEndpointSpoolUsageThreshold;
  }


  public MsgVpnClientProfile eventConnectionCountPerClientUsernameThreshold(EventThreshold eventConnectionCountPerClientUsernameThreshold) {
    
    this.eventConnectionCountPerClientUsernameThreshold = eventConnectionCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventConnectionCountPerClientUsernameThreshold
   * @return eventConnectionCountPerClientUsernameThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventConnectionCountPerClientUsernameThreshold() {
    return eventConnectionCountPerClientUsernameThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventConnectionCountPerClientUsernameThreshold(EventThreshold eventConnectionCountPerClientUsernameThreshold) {
    this.eventConnectionCountPerClientUsernameThreshold = eventConnectionCountPerClientUsernameThreshold;
  }


  public MsgVpnClientProfile eventEgressFlowCountThreshold(EventThreshold eventEgressFlowCountThreshold) {
    
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


  public MsgVpnClientProfile eventEndpointCountPerClientUsernameThreshold(EventThreshold eventEndpointCountPerClientUsernameThreshold) {
    
    this.eventEndpointCountPerClientUsernameThreshold = eventEndpointCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventEndpointCountPerClientUsernameThreshold
   * @return eventEndpointCountPerClientUsernameThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_ENDPOINT_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventEndpointCountPerClientUsernameThreshold() {
    return eventEndpointCountPerClientUsernameThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_ENDPOINT_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventEndpointCountPerClientUsernameThreshold(EventThreshold eventEndpointCountPerClientUsernameThreshold) {
    this.eventEndpointCountPerClientUsernameThreshold = eventEndpointCountPerClientUsernameThreshold;
  }


  public MsgVpnClientProfile eventIngressFlowCountThreshold(EventThreshold eventIngressFlowCountThreshold) {
    
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


  public MsgVpnClientProfile eventServiceSmfConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold) {
    
    this.eventServiceSmfConnectionCountPerClientUsernameThreshold = eventServiceSmfConnectionCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventServiceSmfConnectionCountPerClientUsernameThreshold
   * @return eventServiceSmfConnectionCountPerClientUsernameThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceSmfConnectionCountPerClientUsernameThreshold() {
    return eventServiceSmfConnectionCountPerClientUsernameThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_SMF_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceSmfConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold) {
    this.eventServiceSmfConnectionCountPerClientUsernameThreshold = eventServiceSmfConnectionCountPerClientUsernameThreshold;
  }


  public MsgVpnClientProfile eventServiceWebConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold) {
    
    this.eventServiceWebConnectionCountPerClientUsernameThreshold = eventServiceWebConnectionCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventServiceWebConnectionCountPerClientUsernameThreshold
   * @return eventServiceWebConnectionCountPerClientUsernameThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventServiceWebConnectionCountPerClientUsernameThreshold() {
    return eventServiceWebConnectionCountPerClientUsernameThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SERVICE_WEB_CONNECTION_COUNT_PER_CLIENT_USERNAME_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventServiceWebConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold) {
    this.eventServiceWebConnectionCountPerClientUsernameThreshold = eventServiceWebConnectionCountPerClientUsernameThreshold;
  }


  public MsgVpnClientProfile eventSubscriptionCountThreshold(EventThreshold eventSubscriptionCountThreshold) {
    
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


  public MsgVpnClientProfile eventTransactedSessionCountThreshold(EventThreshold eventTransactedSessionCountThreshold) {
    
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


  public MsgVpnClientProfile eventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
    
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


  public MsgVpnClientProfile maxConnectionCountPerClientUsername(Long maxConnectionCountPerClientUsername) {
    
    this.maxConnectionCountPerClientUsername = maxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of client connections per Client Username using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform.
   * @return maxConnectionCountPerClientUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxConnectionCountPerClientUsername() {
    return maxConnectionCountPerClientUsername;
  }


  @JsonProperty(JSON_PROPERTY_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxConnectionCountPerClientUsername(Long maxConnectionCountPerClientUsername) {
    this.maxConnectionCountPerClientUsername = maxConnectionCountPerClientUsername;
  }


  public MsgVpnClientProfile maxEgressFlowCount(Long maxEgressFlowCount) {
    
    this.maxEgressFlowCount = maxEgressFlowCount;
    return this;
  }

   /**
   * The maximum number of transmit flows that can be created by one client using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;.
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


  public MsgVpnClientProfile maxEndpointCountPerClientUsername(Long maxEndpointCountPerClientUsername) {
    
    this.maxEndpointCountPerClientUsername = maxEndpointCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of queues and topic endpoints that can be created by clients with the same Client Username using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;.
   * @return maxEndpointCountPerClientUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_ENDPOINT_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxEndpointCountPerClientUsername() {
    return maxEndpointCountPerClientUsername;
  }


  @JsonProperty(JSON_PROPERTY_MAX_ENDPOINT_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxEndpointCountPerClientUsername(Long maxEndpointCountPerClientUsername) {
    this.maxEndpointCountPerClientUsername = maxEndpointCountPerClientUsername;
  }


  public MsgVpnClientProfile maxIngressFlowCount(Long maxIngressFlowCount) {
    
    this.maxIngressFlowCount = maxIngressFlowCount;
    return this;
  }

   /**
   * The maximum number of receive flows that can be created by one client using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;.
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


  public MsgVpnClientProfile maxMsgsPerTransaction(Integer maxMsgsPerTransaction) {
    
    this.maxMsgsPerTransaction = maxMsgsPerTransaction;
    return this;
  }

   /**
   * The maximum number of publisher and consumer messages combined that is allowed within a transaction for each client associated with this client-profile. Exceeding this limit will result in a transaction prepare or commit failure. Changing this value during operation will not affect existing sessions. It is only validated at transaction creation time. Large transactions consume more resources and are more likely to require retrieving messages from the ADB or from disk to process the transaction prepare or commit requests. The transaction processing rate may diminish if a large number of messages must be retrieved from the ADB or from disk. Care should be taken to not use excessively large transactions needlessly to avoid exceeding resource limits and to avoid reducing the overall broker performance. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;256&#x60;. Available since 2.20.
   * @return maxMsgsPerTransaction
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_MSGS_PER_TRANSACTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getMaxMsgsPerTransaction() {
    return maxMsgsPerTransaction;
  }


  @JsonProperty(JSON_PROPERTY_MAX_MSGS_PER_TRANSACTION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxMsgsPerTransaction(Integer maxMsgsPerTransaction) {
    this.maxMsgsPerTransaction = maxMsgsPerTransaction;
  }


  public MsgVpnClientProfile maxSubscriptionCount(Long maxSubscriptionCount) {
    
    this.maxSubscriptionCount = maxSubscriptionCount;
    return this;
  }

   /**
   * The maximum number of subscriptions per client using the Client Profile. This limit is not enforced when a client adds a subscription to an endpoint, except for MQTT QoS 1 subscriptions. In addition, this limit is not enforced when a subscription is added using a management interface, such as CLI or SEMP. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
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


  public MsgVpnClientProfile maxTransactedSessionCount(Long maxTransactedSessionCount) {
    
    this.maxTransactedSessionCount = maxTransactedSessionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions that can be created by one client using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10&#x60;.
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


  public MsgVpnClientProfile maxTransactionCount(Long maxTransactionCount) {
    
    this.maxTransactionCount = maxTransactionCount;
    return this;
  }

   /**
   * The maximum number of transactions that can be created by one client using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default varies by platform.
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


  public MsgVpnClientProfile msgVpnName(String msgVpnName) {
    
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


  public MsgVpnClientProfile queueControl1MaxDepth(Integer queueControl1MaxDepth) {
    
    this.queueControl1MaxDepth = queueControl1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Control 1\&quot; (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;20000&#x60;.
   * @return queueControl1MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_CONTROL1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueControl1MaxDepth() {
    return queueControl1MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_CONTROL1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueControl1MaxDepth(Integer queueControl1MaxDepth) {
    this.queueControl1MaxDepth = queueControl1MaxDepth;
  }


  public MsgVpnClientProfile queueControl1MinMsgBurst(Integer queueControl1MinMsgBurst) {
    
    this.queueControl1MinMsgBurst = queueControl1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Control 1\&quot; (C-1) priority queue, regardless of the &#x60;queueControl1MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;4&#x60;.
   * @return queueControl1MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_CONTROL1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueControl1MinMsgBurst() {
    return queueControl1MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_CONTROL1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueControl1MinMsgBurst(Integer queueControl1MinMsgBurst) {
    this.queueControl1MinMsgBurst = queueControl1MinMsgBurst;
  }


  public MsgVpnClientProfile queueDirect1MaxDepth(Integer queueDirect1MaxDepth) {
    
    this.queueDirect1MaxDepth = queueDirect1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Direct 1\&quot; (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;20000&#x60;.
   * @return queueDirect1MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueDirect1MaxDepth() {
    return queueDirect1MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDirect1MaxDepth(Integer queueDirect1MaxDepth) {
    this.queueDirect1MaxDepth = queueDirect1MaxDepth;
  }


  public MsgVpnClientProfile queueDirect1MinMsgBurst(Integer queueDirect1MinMsgBurst) {
    
    this.queueDirect1MinMsgBurst = queueDirect1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Direct 1\&quot; (D-1) priority queue, regardless of the &#x60;queueDirect1MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;4&#x60;.
   * @return queueDirect1MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueDirect1MinMsgBurst() {
    return queueDirect1MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDirect1MinMsgBurst(Integer queueDirect1MinMsgBurst) {
    this.queueDirect1MinMsgBurst = queueDirect1MinMsgBurst;
  }


  public MsgVpnClientProfile queueDirect2MaxDepth(Integer queueDirect2MaxDepth) {
    
    this.queueDirect2MaxDepth = queueDirect2MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Direct 2\&quot; (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;20000&#x60;.
   * @return queueDirect2MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT2_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueDirect2MaxDepth() {
    return queueDirect2MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT2_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDirect2MaxDepth(Integer queueDirect2MaxDepth) {
    this.queueDirect2MaxDepth = queueDirect2MaxDepth;
  }


  public MsgVpnClientProfile queueDirect2MinMsgBurst(Integer queueDirect2MinMsgBurst) {
    
    this.queueDirect2MinMsgBurst = queueDirect2MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Direct 2\&quot; (D-2) priority queue, regardless of the &#x60;queueDirect2MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;4&#x60;.
   * @return queueDirect2MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT2_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueDirect2MinMsgBurst() {
    return queueDirect2MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT2_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDirect2MinMsgBurst(Integer queueDirect2MinMsgBurst) {
    this.queueDirect2MinMsgBurst = queueDirect2MinMsgBurst;
  }


  public MsgVpnClientProfile queueDirect3MaxDepth(Integer queueDirect3MaxDepth) {
    
    this.queueDirect3MaxDepth = queueDirect3MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Direct 3\&quot; (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;20000&#x60;.
   * @return queueDirect3MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT3_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueDirect3MaxDepth() {
    return queueDirect3MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT3_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDirect3MaxDepth(Integer queueDirect3MaxDepth) {
    this.queueDirect3MaxDepth = queueDirect3MaxDepth;
  }


  public MsgVpnClientProfile queueDirect3MinMsgBurst(Integer queueDirect3MinMsgBurst) {
    
    this.queueDirect3MinMsgBurst = queueDirect3MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Direct 3\&quot; (D-3) priority queue, regardless of the &#x60;queueDirect3MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;4&#x60;.
   * @return queueDirect3MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT3_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueDirect3MinMsgBurst() {
    return queueDirect3MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DIRECT3_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDirect3MinMsgBurst(Integer queueDirect3MinMsgBurst) {
    this.queueDirect3MinMsgBurst = queueDirect3MinMsgBurst;
  }


  public MsgVpnClientProfile queueGuaranteed1MaxDepth(Integer queueGuaranteed1MaxDepth) {
    
    this.queueGuaranteed1MaxDepth = queueGuaranteed1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Guaranteed 1\&quot; (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;20000&#x60;.
   * @return queueGuaranteed1MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_GUARANTEED1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueGuaranteed1MaxDepth() {
    return queueGuaranteed1MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_GUARANTEED1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueGuaranteed1MaxDepth(Integer queueGuaranteed1MaxDepth) {
    this.queueGuaranteed1MaxDepth = queueGuaranteed1MaxDepth;
  }


  public MsgVpnClientProfile queueGuaranteed1MinMsgBurst(Integer queueGuaranteed1MinMsgBurst) {
    
    this.queueGuaranteed1MinMsgBurst = queueGuaranteed1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Guaranteed 1\&quot; (G-3) priority queue, regardless of the &#x60;queueGuaranteed1MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;255&#x60;.
   * @return queueGuaranteed1MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_GUARANTEED1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueGuaranteed1MinMsgBurst() {
    return queueGuaranteed1MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_GUARANTEED1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueGuaranteed1MinMsgBurst(Integer queueGuaranteed1MinMsgBurst) {
    this.queueGuaranteed1MinMsgBurst = queueGuaranteed1MinMsgBurst;
  }


  public MsgVpnClientProfile rejectMsgToSenderOnNoSubscriptionMatchEnabled(Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled) {
    
    this.rejectMsgToSenderOnNoSubscriptionMatchEnabled = rejectMsgToSenderOnNoSubscriptionMatchEnabled;
    return this;
  }

   /**
   * Enable or disable the sending of a negative acknowledgement (NACK) to a client using the Client Profile when discarding a guaranteed message due to no matching subscription found. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.2.
   * @return rejectMsgToSenderOnNoSubscriptionMatchEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_NO_SUBSCRIPTION_MATCH_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRejectMsgToSenderOnNoSubscriptionMatchEnabled() {
    return rejectMsgToSenderOnNoSubscriptionMatchEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_NO_SUBSCRIPTION_MATCH_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRejectMsgToSenderOnNoSubscriptionMatchEnabled(Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled) {
    this.rejectMsgToSenderOnNoSubscriptionMatchEnabled = rejectMsgToSenderOnNoSubscriptionMatchEnabled;
  }


  public MsgVpnClientProfile replicationAllowClientConnectWhenStandbyEnabled(Boolean replicationAllowClientConnectWhenStandbyEnabled) {
    
    this.replicationAllowClientConnectWhenStandbyEnabled = replicationAllowClientConnectWhenStandbyEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to connect to the Message VPN when its replication state is standby. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return replicationAllowClientConnectWhenStandbyEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATION_ALLOW_CLIENT_CONNECT_WHEN_STANDBY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getReplicationAllowClientConnectWhenStandbyEnabled() {
    return replicationAllowClientConnectWhenStandbyEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATION_ALLOW_CLIENT_CONNECT_WHEN_STANDBY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicationAllowClientConnectWhenStandbyEnabled(Boolean replicationAllowClientConnectWhenStandbyEnabled) {
    this.replicationAllowClientConnectWhenStandbyEnabled = replicationAllowClientConnectWhenStandbyEnabled;
  }


  public MsgVpnClientProfile serviceMinKeepaliveTimeout(Integer serviceMinKeepaliveTimeout) {
    
    this.serviceMinKeepaliveTimeout = serviceMinKeepaliveTimeout;
    return this;
  }

   /**
   * The minimum client keepalive timeout which will be enforced for client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;30&#x60;. Available since 2.19.
   * @return serviceMinKeepaliveTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_MIN_KEEPALIVE_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getServiceMinKeepaliveTimeout() {
    return serviceMinKeepaliveTimeout;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_MIN_KEEPALIVE_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceMinKeepaliveTimeout(Integer serviceMinKeepaliveTimeout) {
    this.serviceMinKeepaliveTimeout = serviceMinKeepaliveTimeout;
  }


  public MsgVpnClientProfile serviceSmfMaxConnectionCountPerClientUsername(Long serviceSmfMaxConnectionCountPerClientUsername) {
    
    this.serviceSmfMaxConnectionCountPerClientUsername = serviceSmfMaxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of SMF client connections per Client Username using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform.
   * @return serviceSmfMaxConnectionCountPerClientUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceSmfMaxConnectionCountPerClientUsername() {
    return serviceSmfMaxConnectionCountPerClientUsername;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfMaxConnectionCountPerClientUsername(Long serviceSmfMaxConnectionCountPerClientUsername) {
    this.serviceSmfMaxConnectionCountPerClientUsername = serviceSmfMaxConnectionCountPerClientUsername;
  }


  public MsgVpnClientProfile serviceSmfMinKeepaliveEnabled(Boolean serviceSmfMinKeepaliveEnabled) {
    
    this.serviceSmfMinKeepaliveEnabled = serviceSmfMinKeepaliveEnabled;
    return this;
  }

   /**
   * Enable or disable the enforcement of a minimum keepalive timeout for SMF clients. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.19.
   * @return serviceSmfMinKeepaliveEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_MIN_KEEPALIVE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getServiceSmfMinKeepaliveEnabled() {
    return serviceSmfMinKeepaliveEnabled;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_SMF_MIN_KEEPALIVE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceSmfMinKeepaliveEnabled(Boolean serviceSmfMinKeepaliveEnabled) {
    this.serviceSmfMinKeepaliveEnabled = serviceSmfMinKeepaliveEnabled;
  }


  public MsgVpnClientProfile serviceWebInactiveTimeout(Long serviceWebInactiveTimeout) {
    
    this.serviceWebInactiveTimeout = serviceWebInactiveTimeout;
    return this;
  }

   /**
   * The timeout for inactive Web Transport client sessions using the Client Profile, in seconds. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;30&#x60;.
   * @return serviceWebInactiveTimeout
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_INACTIVE_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceWebInactiveTimeout() {
    return serviceWebInactiveTimeout;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_INACTIVE_TIMEOUT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebInactiveTimeout(Long serviceWebInactiveTimeout) {
    this.serviceWebInactiveTimeout = serviceWebInactiveTimeout;
  }


  public MsgVpnClientProfile serviceWebMaxConnectionCountPerClientUsername(Long serviceWebMaxConnectionCountPerClientUsername) {
    
    this.serviceWebMaxConnectionCountPerClientUsername = serviceWebMaxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of Web Transport client connections per Client Username using the Client Profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is the maximum value supported by the platform.
   * @return serviceWebMaxConnectionCountPerClientUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceWebMaxConnectionCountPerClientUsername() {
    return serviceWebMaxConnectionCountPerClientUsername;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_MAX_CONNECTION_COUNT_PER_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebMaxConnectionCountPerClientUsername(Long serviceWebMaxConnectionCountPerClientUsername) {
    this.serviceWebMaxConnectionCountPerClientUsername = serviceWebMaxConnectionCountPerClientUsername;
  }


  public MsgVpnClientProfile serviceWebMaxPayload(Long serviceWebMaxPayload) {
    
    this.serviceWebMaxPayload = serviceWebMaxPayload;
    return this;
  }

   /**
   * The maximum Web Transport payload size before fragmentation occurs for clients using the Client Profile, in bytes. The size of the header is not included. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000000&#x60;.
   * @return serviceWebMaxPayload
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_MAX_PAYLOAD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getServiceWebMaxPayload() {
    return serviceWebMaxPayload;
  }


  @JsonProperty(JSON_PROPERTY_SERVICE_WEB_MAX_PAYLOAD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setServiceWebMaxPayload(Long serviceWebMaxPayload) {
    this.serviceWebMaxPayload = serviceWebMaxPayload;
  }


  public MsgVpnClientProfile tcpCongestionWindowSize(Long tcpCongestionWindowSize) {
    
    this.tcpCongestionWindowSize = tcpCongestionWindowSize;
    return this;
  }

   /**
   * The TCP initial congestion window size for clients using the Client Profile, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact support before changing this value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;2&#x60;.
   * @return tcpCongestionWindowSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TCP_CONGESTION_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTcpCongestionWindowSize() {
    return tcpCongestionWindowSize;
  }


  @JsonProperty(JSON_PROPERTY_TCP_CONGESTION_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTcpCongestionWindowSize(Long tcpCongestionWindowSize) {
    this.tcpCongestionWindowSize = tcpCongestionWindowSize;
  }


  public MsgVpnClientProfile tcpKeepaliveCount(Long tcpKeepaliveCount) {
    
    this.tcpKeepaliveCount = tcpKeepaliveCount;
    return this;
  }

   /**
   * The number of TCP keepalive retransmissions to a client using the Client Profile before declaring that it is not available. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;5&#x60;.
   * @return tcpKeepaliveCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TCP_KEEPALIVE_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTcpKeepaliveCount() {
    return tcpKeepaliveCount;
  }


  @JsonProperty(JSON_PROPERTY_TCP_KEEPALIVE_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTcpKeepaliveCount(Long tcpKeepaliveCount) {
    this.tcpKeepaliveCount = tcpKeepaliveCount;
  }


  public MsgVpnClientProfile tcpKeepaliveIdleTime(Long tcpKeepaliveIdleTime) {
    
    this.tcpKeepaliveIdleTime = tcpKeepaliveIdleTime;
    return this;
  }

   /**
   * The amount of time a client connection using the Client Profile must remain idle before TCP begins sending keepalive probes, in seconds. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;.
   * @return tcpKeepaliveIdleTime
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TCP_KEEPALIVE_IDLE_TIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTcpKeepaliveIdleTime() {
    return tcpKeepaliveIdleTime;
  }


  @JsonProperty(JSON_PROPERTY_TCP_KEEPALIVE_IDLE_TIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTcpKeepaliveIdleTime(Long tcpKeepaliveIdleTime) {
    this.tcpKeepaliveIdleTime = tcpKeepaliveIdleTime;
  }


  public MsgVpnClientProfile tcpKeepaliveInterval(Long tcpKeepaliveInterval) {
    
    this.tcpKeepaliveInterval = tcpKeepaliveInterval;
    return this;
  }

   /**
   * The amount of time between TCP keepalive retransmissions to a client using the Client Profile when no acknowledgement is received, in seconds. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;.
   * @return tcpKeepaliveInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TCP_KEEPALIVE_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTcpKeepaliveInterval() {
    return tcpKeepaliveInterval;
  }


  @JsonProperty(JSON_PROPERTY_TCP_KEEPALIVE_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTcpKeepaliveInterval(Long tcpKeepaliveInterval) {
    this.tcpKeepaliveInterval = tcpKeepaliveInterval;
  }


  public MsgVpnClientProfile tcpMaxSegmentSize(Long tcpMaxSegmentSize) {
    
    this.tcpMaxSegmentSize = tcpMaxSegmentSize;
    return this;
  }

   /**
   * The TCP maximum segment size for clients using the Client Profile, in bytes. Changes are applied to all existing connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1460&#x60;.
   * @return tcpMaxSegmentSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TCP_MAX_SEGMENT_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTcpMaxSegmentSize() {
    return tcpMaxSegmentSize;
  }


  @JsonProperty(JSON_PROPERTY_TCP_MAX_SEGMENT_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTcpMaxSegmentSize(Long tcpMaxSegmentSize) {
    this.tcpMaxSegmentSize = tcpMaxSegmentSize;
  }


  public MsgVpnClientProfile tcpMaxWindowSize(Long tcpMaxWindowSize) {
    
    this.tcpMaxWindowSize = tcpMaxWindowSize;
    return this;
  }

   /**
   * The TCP maximum window size for clients using the Client Profile, in kilobytes. Changes are applied to all existing connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;256&#x60;.
   * @return tcpMaxWindowSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TCP_MAX_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTcpMaxWindowSize() {
    return tcpMaxWindowSize;
  }


  @JsonProperty(JSON_PROPERTY_TCP_MAX_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTcpMaxWindowSize(Long tcpMaxWindowSize) {
    this.tcpMaxWindowSize = tcpMaxWindowSize;
  }


  public MsgVpnClientProfile tlsAllowDowngradeToPlainTextEnabled(Boolean tlsAllowDowngradeToPlainTextEnabled) {
    
    this.tlsAllowDowngradeToPlainTextEnabled = tlsAllowDowngradeToPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable allowing a client using the Client Profile to downgrade an encrypted connection to plain text. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.8.
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
    MsgVpnClientProfile msgVpnClientProfile = (MsgVpnClientProfile) o;
    return Objects.equals(this.allowBridgeConnectionsEnabled, msgVpnClientProfile.allowBridgeConnectionsEnabled) &&
        Objects.equals(this.allowCutThroughForwardingEnabled, msgVpnClientProfile.allowCutThroughForwardingEnabled) &&
        Objects.equals(this.allowGuaranteedEndpointCreateDurability, msgVpnClientProfile.allowGuaranteedEndpointCreateDurability) &&
        Objects.equals(this.allowGuaranteedEndpointCreateEnabled, msgVpnClientProfile.allowGuaranteedEndpointCreateEnabled) &&
        Objects.equals(this.allowGuaranteedMsgReceiveEnabled, msgVpnClientProfile.allowGuaranteedMsgReceiveEnabled) &&
        Objects.equals(this.allowGuaranteedMsgSendEnabled, msgVpnClientProfile.allowGuaranteedMsgSendEnabled) &&
        Objects.equals(this.allowSharedSubscriptionsEnabled, msgVpnClientProfile.allowSharedSubscriptionsEnabled) &&
        Objects.equals(this.allowTransactedSessionsEnabled, msgVpnClientProfile.allowTransactedSessionsEnabled) &&
        Objects.equals(this.apiQueueManagementCopyFromOnCreateName, msgVpnClientProfile.apiQueueManagementCopyFromOnCreateName) &&
        Objects.equals(this.apiQueueManagementCopyFromOnCreateTemplateName, msgVpnClientProfile.apiQueueManagementCopyFromOnCreateTemplateName) &&
        Objects.equals(this.apiTopicEndpointManagementCopyFromOnCreateName, msgVpnClientProfile.apiTopicEndpointManagementCopyFromOnCreateName) &&
        Objects.equals(this.apiTopicEndpointManagementCopyFromOnCreateTemplateName, msgVpnClientProfile.apiTopicEndpointManagementCopyFromOnCreateTemplateName) &&
        Objects.equals(this.clientProfileName, msgVpnClientProfile.clientProfileName) &&
        Objects.equals(this.compressionEnabled, msgVpnClientProfile.compressionEnabled) &&
        Objects.equals(this.elidingDelay, msgVpnClientProfile.elidingDelay) &&
        Objects.equals(this.elidingEnabled, msgVpnClientProfile.elidingEnabled) &&
        Objects.equals(this.elidingMaxTopicCount, msgVpnClientProfile.elidingMaxTopicCount) &&
        Objects.equals(this.eventClientProvisionedEndpointSpoolUsageThreshold, msgVpnClientProfile.eventClientProvisionedEndpointSpoolUsageThreshold) &&
        Objects.equals(this.eventConnectionCountPerClientUsernameThreshold, msgVpnClientProfile.eventConnectionCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventEgressFlowCountThreshold, msgVpnClientProfile.eventEgressFlowCountThreshold) &&
        Objects.equals(this.eventEndpointCountPerClientUsernameThreshold, msgVpnClientProfile.eventEndpointCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventIngressFlowCountThreshold, msgVpnClientProfile.eventIngressFlowCountThreshold) &&
        Objects.equals(this.eventServiceSmfConnectionCountPerClientUsernameThreshold, msgVpnClientProfile.eventServiceSmfConnectionCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventServiceWebConnectionCountPerClientUsernameThreshold, msgVpnClientProfile.eventServiceWebConnectionCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventSubscriptionCountThreshold, msgVpnClientProfile.eventSubscriptionCountThreshold) &&
        Objects.equals(this.eventTransactedSessionCountThreshold, msgVpnClientProfile.eventTransactedSessionCountThreshold) &&
        Objects.equals(this.eventTransactionCountThreshold, msgVpnClientProfile.eventTransactionCountThreshold) &&
        Objects.equals(this.maxConnectionCountPerClientUsername, msgVpnClientProfile.maxConnectionCountPerClientUsername) &&
        Objects.equals(this.maxEgressFlowCount, msgVpnClientProfile.maxEgressFlowCount) &&
        Objects.equals(this.maxEndpointCountPerClientUsername, msgVpnClientProfile.maxEndpointCountPerClientUsername) &&
        Objects.equals(this.maxIngressFlowCount, msgVpnClientProfile.maxIngressFlowCount) &&
        Objects.equals(this.maxMsgsPerTransaction, msgVpnClientProfile.maxMsgsPerTransaction) &&
        Objects.equals(this.maxSubscriptionCount, msgVpnClientProfile.maxSubscriptionCount) &&
        Objects.equals(this.maxTransactedSessionCount, msgVpnClientProfile.maxTransactedSessionCount) &&
        Objects.equals(this.maxTransactionCount, msgVpnClientProfile.maxTransactionCount) &&
        Objects.equals(this.msgVpnName, msgVpnClientProfile.msgVpnName) &&
        Objects.equals(this.queueControl1MaxDepth, msgVpnClientProfile.queueControl1MaxDepth) &&
        Objects.equals(this.queueControl1MinMsgBurst, msgVpnClientProfile.queueControl1MinMsgBurst) &&
        Objects.equals(this.queueDirect1MaxDepth, msgVpnClientProfile.queueDirect1MaxDepth) &&
        Objects.equals(this.queueDirect1MinMsgBurst, msgVpnClientProfile.queueDirect1MinMsgBurst) &&
        Objects.equals(this.queueDirect2MaxDepth, msgVpnClientProfile.queueDirect2MaxDepth) &&
        Objects.equals(this.queueDirect2MinMsgBurst, msgVpnClientProfile.queueDirect2MinMsgBurst) &&
        Objects.equals(this.queueDirect3MaxDepth, msgVpnClientProfile.queueDirect3MaxDepth) &&
        Objects.equals(this.queueDirect3MinMsgBurst, msgVpnClientProfile.queueDirect3MinMsgBurst) &&
        Objects.equals(this.queueGuaranteed1MaxDepth, msgVpnClientProfile.queueGuaranteed1MaxDepth) &&
        Objects.equals(this.queueGuaranteed1MinMsgBurst, msgVpnClientProfile.queueGuaranteed1MinMsgBurst) &&
        Objects.equals(this.rejectMsgToSenderOnNoSubscriptionMatchEnabled, msgVpnClientProfile.rejectMsgToSenderOnNoSubscriptionMatchEnabled) &&
        Objects.equals(this.replicationAllowClientConnectWhenStandbyEnabled, msgVpnClientProfile.replicationAllowClientConnectWhenStandbyEnabled) &&
        Objects.equals(this.serviceMinKeepaliveTimeout, msgVpnClientProfile.serviceMinKeepaliveTimeout) &&
        Objects.equals(this.serviceSmfMaxConnectionCountPerClientUsername, msgVpnClientProfile.serviceSmfMaxConnectionCountPerClientUsername) &&
        Objects.equals(this.serviceSmfMinKeepaliveEnabled, msgVpnClientProfile.serviceSmfMinKeepaliveEnabled) &&
        Objects.equals(this.serviceWebInactiveTimeout, msgVpnClientProfile.serviceWebInactiveTimeout) &&
        Objects.equals(this.serviceWebMaxConnectionCountPerClientUsername, msgVpnClientProfile.serviceWebMaxConnectionCountPerClientUsername) &&
        Objects.equals(this.serviceWebMaxPayload, msgVpnClientProfile.serviceWebMaxPayload) &&
        Objects.equals(this.tcpCongestionWindowSize, msgVpnClientProfile.tcpCongestionWindowSize) &&
        Objects.equals(this.tcpKeepaliveCount, msgVpnClientProfile.tcpKeepaliveCount) &&
        Objects.equals(this.tcpKeepaliveIdleTime, msgVpnClientProfile.tcpKeepaliveIdleTime) &&
        Objects.equals(this.tcpKeepaliveInterval, msgVpnClientProfile.tcpKeepaliveInterval) &&
        Objects.equals(this.tcpMaxSegmentSize, msgVpnClientProfile.tcpMaxSegmentSize) &&
        Objects.equals(this.tcpMaxWindowSize, msgVpnClientProfile.tcpMaxWindowSize) &&
        Objects.equals(this.tlsAllowDowngradeToPlainTextEnabled, msgVpnClientProfile.tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowBridgeConnectionsEnabled, allowCutThroughForwardingEnabled, allowGuaranteedEndpointCreateDurability, allowGuaranteedEndpointCreateEnabled, allowGuaranteedMsgReceiveEnabled, allowGuaranteedMsgSendEnabled, allowSharedSubscriptionsEnabled, allowTransactedSessionsEnabled, apiQueueManagementCopyFromOnCreateName, apiQueueManagementCopyFromOnCreateTemplateName, apiTopicEndpointManagementCopyFromOnCreateName, apiTopicEndpointManagementCopyFromOnCreateTemplateName, clientProfileName, compressionEnabled, elidingDelay, elidingEnabled, elidingMaxTopicCount, eventClientProvisionedEndpointSpoolUsageThreshold, eventConnectionCountPerClientUsernameThreshold, eventEgressFlowCountThreshold, eventEndpointCountPerClientUsernameThreshold, eventIngressFlowCountThreshold, eventServiceSmfConnectionCountPerClientUsernameThreshold, eventServiceWebConnectionCountPerClientUsernameThreshold, eventSubscriptionCountThreshold, eventTransactedSessionCountThreshold, eventTransactionCountThreshold, maxConnectionCountPerClientUsername, maxEgressFlowCount, maxEndpointCountPerClientUsername, maxIngressFlowCount, maxMsgsPerTransaction, maxSubscriptionCount, maxTransactedSessionCount, maxTransactionCount, msgVpnName, queueControl1MaxDepth, queueControl1MinMsgBurst, queueDirect1MaxDepth, queueDirect1MinMsgBurst, queueDirect2MaxDepth, queueDirect2MinMsgBurst, queueDirect3MaxDepth, queueDirect3MinMsgBurst, queueGuaranteed1MaxDepth, queueGuaranteed1MinMsgBurst, rejectMsgToSenderOnNoSubscriptionMatchEnabled, replicationAllowClientConnectWhenStandbyEnabled, serviceMinKeepaliveTimeout, serviceSmfMaxConnectionCountPerClientUsername, serviceSmfMinKeepaliveEnabled, serviceWebInactiveTimeout, serviceWebMaxConnectionCountPerClientUsername, serviceWebMaxPayload, tcpCongestionWindowSize, tcpKeepaliveCount, tcpKeepaliveIdleTime, tcpKeepaliveInterval, tcpMaxSegmentSize, tcpMaxWindowSize, tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnClientProfile {\n");
    sb.append("    allowBridgeConnectionsEnabled: ").append(toIndentedString(allowBridgeConnectionsEnabled)).append("\n");
    sb.append("    allowCutThroughForwardingEnabled: ").append(toIndentedString(allowCutThroughForwardingEnabled)).append("\n");
    sb.append("    allowGuaranteedEndpointCreateDurability: ").append(toIndentedString(allowGuaranteedEndpointCreateDurability)).append("\n");
    sb.append("    allowGuaranteedEndpointCreateEnabled: ").append(toIndentedString(allowGuaranteedEndpointCreateEnabled)).append("\n");
    sb.append("    allowGuaranteedMsgReceiveEnabled: ").append(toIndentedString(allowGuaranteedMsgReceiveEnabled)).append("\n");
    sb.append("    allowGuaranteedMsgSendEnabled: ").append(toIndentedString(allowGuaranteedMsgSendEnabled)).append("\n");
    sb.append("    allowSharedSubscriptionsEnabled: ").append(toIndentedString(allowSharedSubscriptionsEnabled)).append("\n");
    sb.append("    allowTransactedSessionsEnabled: ").append(toIndentedString(allowTransactedSessionsEnabled)).append("\n");
    sb.append("    apiQueueManagementCopyFromOnCreateName: ").append(toIndentedString(apiQueueManagementCopyFromOnCreateName)).append("\n");
    sb.append("    apiQueueManagementCopyFromOnCreateTemplateName: ").append(toIndentedString(apiQueueManagementCopyFromOnCreateTemplateName)).append("\n");
    sb.append("    apiTopicEndpointManagementCopyFromOnCreateName: ").append(toIndentedString(apiTopicEndpointManagementCopyFromOnCreateName)).append("\n");
    sb.append("    apiTopicEndpointManagementCopyFromOnCreateTemplateName: ").append(toIndentedString(apiTopicEndpointManagementCopyFromOnCreateTemplateName)).append("\n");
    sb.append("    clientProfileName: ").append(toIndentedString(clientProfileName)).append("\n");
    sb.append("    compressionEnabled: ").append(toIndentedString(compressionEnabled)).append("\n");
    sb.append("    elidingDelay: ").append(toIndentedString(elidingDelay)).append("\n");
    sb.append("    elidingEnabled: ").append(toIndentedString(elidingEnabled)).append("\n");
    sb.append("    elidingMaxTopicCount: ").append(toIndentedString(elidingMaxTopicCount)).append("\n");
    sb.append("    eventClientProvisionedEndpointSpoolUsageThreshold: ").append(toIndentedString(eventClientProvisionedEndpointSpoolUsageThreshold)).append("\n");
    sb.append("    eventConnectionCountPerClientUsernameThreshold: ").append(toIndentedString(eventConnectionCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventEgressFlowCountThreshold: ").append(toIndentedString(eventEgressFlowCountThreshold)).append("\n");
    sb.append("    eventEndpointCountPerClientUsernameThreshold: ").append(toIndentedString(eventEndpointCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventIngressFlowCountThreshold: ").append(toIndentedString(eventIngressFlowCountThreshold)).append("\n");
    sb.append("    eventServiceSmfConnectionCountPerClientUsernameThreshold: ").append(toIndentedString(eventServiceSmfConnectionCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventServiceWebConnectionCountPerClientUsernameThreshold: ").append(toIndentedString(eventServiceWebConnectionCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventSubscriptionCountThreshold: ").append(toIndentedString(eventSubscriptionCountThreshold)).append("\n");
    sb.append("    eventTransactedSessionCountThreshold: ").append(toIndentedString(eventTransactedSessionCountThreshold)).append("\n");
    sb.append("    eventTransactionCountThreshold: ").append(toIndentedString(eventTransactionCountThreshold)).append("\n");
    sb.append("    maxConnectionCountPerClientUsername: ").append(toIndentedString(maxConnectionCountPerClientUsername)).append("\n");
    sb.append("    maxEgressFlowCount: ").append(toIndentedString(maxEgressFlowCount)).append("\n");
    sb.append("    maxEndpointCountPerClientUsername: ").append(toIndentedString(maxEndpointCountPerClientUsername)).append("\n");
    sb.append("    maxIngressFlowCount: ").append(toIndentedString(maxIngressFlowCount)).append("\n");
    sb.append("    maxMsgsPerTransaction: ").append(toIndentedString(maxMsgsPerTransaction)).append("\n");
    sb.append("    maxSubscriptionCount: ").append(toIndentedString(maxSubscriptionCount)).append("\n");
    sb.append("    maxTransactedSessionCount: ").append(toIndentedString(maxTransactedSessionCount)).append("\n");
    sb.append("    maxTransactionCount: ").append(toIndentedString(maxTransactionCount)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    queueControl1MaxDepth: ").append(toIndentedString(queueControl1MaxDepth)).append("\n");
    sb.append("    queueControl1MinMsgBurst: ").append(toIndentedString(queueControl1MinMsgBurst)).append("\n");
    sb.append("    queueDirect1MaxDepth: ").append(toIndentedString(queueDirect1MaxDepth)).append("\n");
    sb.append("    queueDirect1MinMsgBurst: ").append(toIndentedString(queueDirect1MinMsgBurst)).append("\n");
    sb.append("    queueDirect2MaxDepth: ").append(toIndentedString(queueDirect2MaxDepth)).append("\n");
    sb.append("    queueDirect2MinMsgBurst: ").append(toIndentedString(queueDirect2MinMsgBurst)).append("\n");
    sb.append("    queueDirect3MaxDepth: ").append(toIndentedString(queueDirect3MaxDepth)).append("\n");
    sb.append("    queueDirect3MinMsgBurst: ").append(toIndentedString(queueDirect3MinMsgBurst)).append("\n");
    sb.append("    queueGuaranteed1MaxDepth: ").append(toIndentedString(queueGuaranteed1MaxDepth)).append("\n");
    sb.append("    queueGuaranteed1MinMsgBurst: ").append(toIndentedString(queueGuaranteed1MinMsgBurst)).append("\n");
    sb.append("    rejectMsgToSenderOnNoSubscriptionMatchEnabled: ").append(toIndentedString(rejectMsgToSenderOnNoSubscriptionMatchEnabled)).append("\n");
    sb.append("    replicationAllowClientConnectWhenStandbyEnabled: ").append(toIndentedString(replicationAllowClientConnectWhenStandbyEnabled)).append("\n");
    sb.append("    serviceMinKeepaliveTimeout: ").append(toIndentedString(serviceMinKeepaliveTimeout)).append("\n");
    sb.append("    serviceSmfMaxConnectionCountPerClientUsername: ").append(toIndentedString(serviceSmfMaxConnectionCountPerClientUsername)).append("\n");
    sb.append("    serviceSmfMinKeepaliveEnabled: ").append(toIndentedString(serviceSmfMinKeepaliveEnabled)).append("\n");
    sb.append("    serviceWebInactiveTimeout: ").append(toIndentedString(serviceWebInactiveTimeout)).append("\n");
    sb.append("    serviceWebMaxConnectionCountPerClientUsername: ").append(toIndentedString(serviceWebMaxConnectionCountPerClientUsername)).append("\n");
    sb.append("    serviceWebMaxPayload: ").append(toIndentedString(serviceWebMaxPayload)).append("\n");
    sb.append("    tcpCongestionWindowSize: ").append(toIndentedString(tcpCongestionWindowSize)).append("\n");
    sb.append("    tcpKeepaliveCount: ").append(toIndentedString(tcpKeepaliveCount)).append("\n");
    sb.append("    tcpKeepaliveIdleTime: ").append(toIndentedString(tcpKeepaliveIdleTime)).append("\n");
    sb.append("    tcpKeepaliveInterval: ").append(toIndentedString(tcpKeepaliveInterval)).append("\n");
    sb.append("    tcpMaxSegmentSize: ").append(toIndentedString(tcpMaxSegmentSize)).append("\n");
    sb.append("    tcpMaxWindowSize: ").append(toIndentedString(tcpMaxWindowSize)).append("\n");
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

