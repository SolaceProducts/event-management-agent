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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * MsgVpnTopicEndpoint
 */
@JsonPropertyOrder({
  MsgVpnTopicEndpoint.JSON_PROPERTY_ACCESS_TYPE,
  MsgVpnTopicEndpoint.JSON_PROPERTY_CONSUMER_ACK_PROPAGATION_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_DEAD_MSG_QUEUE,
  MsgVpnTopicEndpoint.JSON_PROPERTY_DELIVERY_COUNT_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_DELIVERY_DELAY,
  MsgVpnTopicEndpoint.JSON_PROPERTY_EGRESS_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_EVENT_BIND_COUNT_THRESHOLD,
  MsgVpnTopicEndpoint.JSON_PROPERTY_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD,
  MsgVpnTopicEndpoint.JSON_PROPERTY_EVENT_SPOOL_USAGE_THRESHOLD,
  MsgVpnTopicEndpoint.JSON_PROPERTY_INGRESS_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MAX_BIND_COUNT,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MAX_MSG_SIZE,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MAX_REDELIVERY_COUNT,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MAX_SPOOL_USAGE,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MAX_TTL,
  MsgVpnTopicEndpoint.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnTopicEndpoint.JSON_PROPERTY_OWNER,
  MsgVpnTopicEndpoint.JSON_PROPERTY_PERMISSION,
  MsgVpnTopicEndpoint.JSON_PROPERTY_REDELIVERY_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_LIMIT,
  MsgVpnTopicEndpoint.JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR,
  MsgVpnTopicEndpoint.JSON_PROPERTY_RESPECT_MSG_PRIORITY_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_RESPECT_TTL_ENABLED,
  MsgVpnTopicEndpoint.JSON_PROPERTY_TOPIC_ENDPOINT_NAME
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class MsgVpnTopicEndpoint {
  /**
   * The access type for delivering messages to consumer flows bound to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;exclusive\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;exclusive\&quot; - Exclusive delivery of messages to the first bound consumer flow. \&quot;non-exclusive\&quot; - Non-exclusive delivery of messages to all bound consumer flows in a round-robin fashion. &lt;/pre&gt;  Available since 2.4.
   */
  public enum AccessTypeEnum {
    EXCLUSIVE("exclusive"),
    
    NON_EXCLUSIVE("non-exclusive");

    private String value;

    AccessTypeEnum(String value) {
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
    public static AccessTypeEnum fromValue(String value) {
      for (AccessTypeEnum b : AccessTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_ACCESS_TYPE = "accessType";
  private AccessTypeEnum accessType;

  public static final String JSON_PROPERTY_CONSUMER_ACK_PROPAGATION_ENABLED = "consumerAckPropagationEnabled";
  private Boolean consumerAckPropagationEnabled;

  public static final String JSON_PROPERTY_DEAD_MSG_QUEUE = "deadMsgQueue";
  private String deadMsgQueue;

  public static final String JSON_PROPERTY_DELIVERY_COUNT_ENABLED = "deliveryCountEnabled";
  private Boolean deliveryCountEnabled;

  public static final String JSON_PROPERTY_DELIVERY_DELAY = "deliveryDelay";
  private Long deliveryDelay;

  public static final String JSON_PROPERTY_EGRESS_ENABLED = "egressEnabled";
  private Boolean egressEnabled;

  public static final String JSON_PROPERTY_EVENT_BIND_COUNT_THRESHOLD = "eventBindCountThreshold";
  private EventThreshold eventBindCountThreshold;

  public static final String JSON_PROPERTY_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD = "eventRejectLowPriorityMsgLimitThreshold";
  private EventThreshold eventRejectLowPriorityMsgLimitThreshold;

  public static final String JSON_PROPERTY_EVENT_SPOOL_USAGE_THRESHOLD = "eventSpoolUsageThreshold";
  private EventThreshold eventSpoolUsageThreshold;

  public static final String JSON_PROPERTY_INGRESS_ENABLED = "ingressEnabled";
  private Boolean ingressEnabled;

  public static final String JSON_PROPERTY_MAX_BIND_COUNT = "maxBindCount";
  private Long maxBindCount;

  public static final String JSON_PROPERTY_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW = "maxDeliveredUnackedMsgsPerFlow";
  private Long maxDeliveredUnackedMsgsPerFlow;

  public static final String JSON_PROPERTY_MAX_MSG_SIZE = "maxMsgSize";
  private Integer maxMsgSize;

  public static final String JSON_PROPERTY_MAX_REDELIVERY_COUNT = "maxRedeliveryCount";
  private Long maxRedeliveryCount;

  public static final String JSON_PROPERTY_MAX_SPOOL_USAGE = "maxSpoolUsage";
  private Long maxSpoolUsage;

  public static final String JSON_PROPERTY_MAX_TTL = "maxTtl";
  private Long maxTtl;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_OWNER = "owner";
  private String owner;

  /**
   * The permission level for all consumers of the Topic Endpoint, excluding the owner. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;no-access\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;no-access\&quot; - Disallows all access. \&quot;read-only\&quot; - Read-only access to the messages. \&quot;consume\&quot; - Consume (read and remove) messages. \&quot;modify-topic\&quot; - Consume messages or modify the topic/selector. \&quot;delete\&quot; - Consume messages, modify the topic/selector or delete the Client created endpoint altogether. &lt;/pre&gt; 
   */
  public enum PermissionEnum {
    NO_ACCESS("no-access"),
    
    READ_ONLY("read-only"),
    
    CONSUME("consume"),
    
    MODIFY_TOPIC("modify-topic"),
    
    DELETE("delete");

    private String value;

    PermissionEnum(String value) {
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
    public static PermissionEnum fromValue(String value) {
      for (PermissionEnum b : PermissionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_PERMISSION = "permission";
  private PermissionEnum permission;

  public static final String JSON_PROPERTY_REDELIVERY_ENABLED = "redeliveryEnabled";
  private Boolean redeliveryEnabled;

  public static final String JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_ENABLED = "rejectLowPriorityMsgEnabled";
  private Boolean rejectLowPriorityMsgEnabled;

  public static final String JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_LIMIT = "rejectLowPriorityMsgLimit";
  private Long rejectLowPriorityMsgLimit;

  /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;never\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-topic-endpoint-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Topic Endpoint is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt; 
   */
  public enum RejectMsgToSenderOnDiscardBehaviorEnum {
    ALWAYS("always"),
    
    WHEN_TOPIC_ENDPOINT_ENABLED("when-topic-endpoint-enabled"),
    
    NEVER("never");

    private String value;

    RejectMsgToSenderOnDiscardBehaviorEnum(String value) {
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
    public static RejectMsgToSenderOnDiscardBehaviorEnum fromValue(String value) {
      for (RejectMsgToSenderOnDiscardBehaviorEnum b : RejectMsgToSenderOnDiscardBehaviorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR = "rejectMsgToSenderOnDiscardBehavior";
  private RejectMsgToSenderOnDiscardBehaviorEnum rejectMsgToSenderOnDiscardBehavior;

  public static final String JSON_PROPERTY_RESPECT_MSG_PRIORITY_ENABLED = "respectMsgPriorityEnabled";
  private Boolean respectMsgPriorityEnabled;

  public static final String JSON_PROPERTY_RESPECT_TTL_ENABLED = "respectTtlEnabled";
  private Boolean respectTtlEnabled;

  public static final String JSON_PROPERTY_TOPIC_ENDPOINT_NAME = "topicEndpointName";
  private String topicEndpointName;

  public MsgVpnTopicEndpoint() {
  }

  public MsgVpnTopicEndpoint accessType(AccessTypeEnum accessType) {
    
    this.accessType = accessType;
    return this;
  }

   /**
   * The access type for delivering messages to consumer flows bound to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;exclusive\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;exclusive\&quot; - Exclusive delivery of messages to the first bound consumer flow. \&quot;non-exclusive\&quot; - Non-exclusive delivery of messages to all bound consumer flows in a round-robin fashion. &lt;/pre&gt;  Available since 2.4.
   * @return accessType
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACCESS_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public AccessTypeEnum getAccessType() {
    return accessType;
  }


  @JsonProperty(JSON_PROPERTY_ACCESS_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAccessType(AccessTypeEnum accessType) {
    this.accessType = accessType;
  }


  public MsgVpnTopicEndpoint consumerAckPropagationEnabled(Boolean consumerAckPropagationEnabled) {
    
    this.consumerAckPropagationEnabled = consumerAckPropagationEnabled;
    return this;
  }

   /**
   * Enable or disable the propagation of consumer acknowledgements (ACKs) received on the active replication Message VPN to the standby replication Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;.
   * @return consumerAckPropagationEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONSUMER_ACK_PROPAGATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getConsumerAckPropagationEnabled() {
    return consumerAckPropagationEnabled;
  }


  @JsonProperty(JSON_PROPERTY_CONSUMER_ACK_PROPAGATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConsumerAckPropagationEnabled(Boolean consumerAckPropagationEnabled) {
    this.consumerAckPropagationEnabled = consumerAckPropagationEnabled;
  }


  public MsgVpnTopicEndpoint deadMsgQueue(String deadMsgQueue) {
    
    this.deadMsgQueue = deadMsgQueue;
    return this;
  }

   /**
   * The name of the Dead Message Queue (DMQ) used by the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. Available since 2.2.
   * @return deadMsgQueue
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DEAD_MSG_QUEUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDeadMsgQueue() {
    return deadMsgQueue;
  }


  @JsonProperty(JSON_PROPERTY_DEAD_MSG_QUEUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDeadMsgQueue(String deadMsgQueue) {
    this.deadMsgQueue = deadMsgQueue;
  }


  public MsgVpnTopicEndpoint deliveryCountEnabled(Boolean deliveryCountEnabled) {
    
    this.deliveryCountEnabled = deliveryCountEnabled;
    return this;
  }

   /**
   * Enable or disable the ability for client applications to query the message delivery count of messages received from the Topic Endpoint. This is a controlled availability feature. Please contact support to find out if this feature is supported for your use case. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.19.
   * @return deliveryCountEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DELIVERY_COUNT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getDeliveryCountEnabled() {
    return deliveryCountEnabled;
  }


  @JsonProperty(JSON_PROPERTY_DELIVERY_COUNT_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDeliveryCountEnabled(Boolean deliveryCountEnabled) {
    this.deliveryCountEnabled = deliveryCountEnabled;
  }


  public MsgVpnTopicEndpoint deliveryDelay(Long deliveryDelay) {
    
    this.deliveryDelay = deliveryDelay;
    return this;
  }

   /**
   * The delay, in seconds, to apply to messages arriving on the Topic Endpoint before the messages are eligible for delivery. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.22.
   * @return deliveryDelay
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DELIVERY_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getDeliveryDelay() {
    return deliveryDelay;
  }


  @JsonProperty(JSON_PROPERTY_DELIVERY_DELAY)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDeliveryDelay(Long deliveryDelay) {
    this.deliveryDelay = deliveryDelay;
  }


  public MsgVpnTopicEndpoint egressEnabled(Boolean egressEnabled) {
    
    this.egressEnabled = egressEnabled;
    return this;
  }

   /**
   * Enable or disable the transmission of messages from the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return egressEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EGRESS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getEgressEnabled() {
    return egressEnabled;
  }


  @JsonProperty(JSON_PROPERTY_EGRESS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEgressEnabled(Boolean egressEnabled) {
    this.egressEnabled = egressEnabled;
  }


  public MsgVpnTopicEndpoint eventBindCountThreshold(EventThreshold eventBindCountThreshold) {
    
    this.eventBindCountThreshold = eventBindCountThreshold;
    return this;
  }

   /**
   * Get eventBindCountThreshold
   * @return eventBindCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_BIND_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventBindCountThreshold() {
    return eventBindCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_BIND_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventBindCountThreshold(EventThreshold eventBindCountThreshold) {
    this.eventBindCountThreshold = eventBindCountThreshold;
  }


  public MsgVpnTopicEndpoint eventRejectLowPriorityMsgLimitThreshold(EventThreshold eventRejectLowPriorityMsgLimitThreshold) {
    
    this.eventRejectLowPriorityMsgLimitThreshold = eventRejectLowPriorityMsgLimitThreshold;
    return this;
  }

   /**
   * Get eventRejectLowPriorityMsgLimitThreshold
   * @return eventRejectLowPriorityMsgLimitThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventRejectLowPriorityMsgLimitThreshold() {
    return eventRejectLowPriorityMsgLimitThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventRejectLowPriorityMsgLimitThreshold(EventThreshold eventRejectLowPriorityMsgLimitThreshold) {
    this.eventRejectLowPriorityMsgLimitThreshold = eventRejectLowPriorityMsgLimitThreshold;
  }


  public MsgVpnTopicEndpoint eventSpoolUsageThreshold(EventThreshold eventSpoolUsageThreshold) {
    
    this.eventSpoolUsageThreshold = eventSpoolUsageThreshold;
    return this;
  }

   /**
   * Get eventSpoolUsageThreshold
   * @return eventSpoolUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EVENT_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getEventSpoolUsageThreshold() {
    return eventSpoolUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_EVENT_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEventSpoolUsageThreshold(EventThreshold eventSpoolUsageThreshold) {
    this.eventSpoolUsageThreshold = eventSpoolUsageThreshold;
  }


  public MsgVpnTopicEndpoint ingressEnabled(Boolean ingressEnabled) {
    
    this.ingressEnabled = ingressEnabled;
    return this;
  }

   /**
   * Enable or disable the reception of messages to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return ingressEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INGRESS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getIngressEnabled() {
    return ingressEnabled;
  }


  @JsonProperty(JSON_PROPERTY_INGRESS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setIngressEnabled(Boolean ingressEnabled) {
    this.ingressEnabled = ingressEnabled;
  }


  public MsgVpnTopicEndpoint maxBindCount(Long maxBindCount) {
    
    this.maxBindCount = maxBindCount;
    return this;
  }

   /**
   * The maximum number of consumer flows that can bind to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;. Available since 2.4.
   * @return maxBindCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_BIND_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxBindCount() {
    return maxBindCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_BIND_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxBindCount(Long maxBindCount) {
    this.maxBindCount = maxBindCount;
  }


  public MsgVpnTopicEndpoint maxDeliveredUnackedMsgsPerFlow(Long maxDeliveredUnackedMsgsPerFlow) {
    
    this.maxDeliveredUnackedMsgsPerFlow = maxDeliveredUnackedMsgsPerFlow;
    return this;
  }

   /**
   * The maximum number of messages delivered but not acknowledged per flow for the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000&#x60;.
   * @return maxDeliveredUnackedMsgsPerFlow
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxDeliveredUnackedMsgsPerFlow() {
    return maxDeliveredUnackedMsgsPerFlow;
  }


  @JsonProperty(JSON_PROPERTY_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxDeliveredUnackedMsgsPerFlow(Long maxDeliveredUnackedMsgsPerFlow) {
    this.maxDeliveredUnackedMsgsPerFlow = maxDeliveredUnackedMsgsPerFlow;
  }


  public MsgVpnTopicEndpoint maxMsgSize(Integer maxMsgSize) {
    
    this.maxMsgSize = maxMsgSize;
    return this;
  }

   /**
   * The maximum message size allowed in the Topic Endpoint, in bytes (B). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000000&#x60;.
   * @return maxMsgSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_MSG_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getMaxMsgSize() {
    return maxMsgSize;
  }


  @JsonProperty(JSON_PROPERTY_MAX_MSG_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxMsgSize(Integer maxMsgSize) {
    this.maxMsgSize = maxMsgSize;
  }


  public MsgVpnTopicEndpoint maxRedeliveryCount(Long maxRedeliveryCount) {
    
    this.maxRedeliveryCount = maxRedeliveryCount;
    return this;
  }

   /**
   * The maximum number of times the Topic Endpoint will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return maxRedeliveryCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_REDELIVERY_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxRedeliveryCount() {
    return maxRedeliveryCount;
  }


  @JsonProperty(JSON_PROPERTY_MAX_REDELIVERY_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxRedeliveryCount(Long maxRedeliveryCount) {
    this.maxRedeliveryCount = maxRedeliveryCount;
  }


  public MsgVpnTopicEndpoint maxSpoolUsage(Long maxSpoolUsage) {
    
    this.maxSpoolUsage = maxSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage allowed by the Topic Endpoint, in megabytes (MB). A value of 0 only allows spooling of the last message received and disables quota checking. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;5000&#x60;.
   * @return maxSpoolUsage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxSpoolUsage() {
    return maxSpoolUsage;
  }


  @JsonProperty(JSON_PROPERTY_MAX_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxSpoolUsage(Long maxSpoolUsage) {
    this.maxSpoolUsage = maxSpoolUsage;
  }


  public MsgVpnTopicEndpoint maxTtl(Long maxTtl) {
    
    this.maxTtl = maxTtl;
    return this;
  }

   /**
   * The maximum time in seconds a message can stay in the Topic Endpoint when &#x60;respectTtlEnabled&#x60; is &#x60;\&quot;true\&quot;&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;maxTtl&#x60; configured for the Topic Endpoint, is exceeded. A value of 0 disables expiry. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return maxTtl
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MAX_TTL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getMaxTtl() {
    return maxTtl;
  }


  @JsonProperty(JSON_PROPERTY_MAX_TTL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMaxTtl(Long maxTtl) {
    this.maxTtl = maxTtl;
  }


  public MsgVpnTopicEndpoint msgVpnName(String msgVpnName) {
    
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


  public MsgVpnTopicEndpoint owner(String owner) {
    
    this.owner = owner;
    return this;
  }

   /**
   * The Client Username that owns the Topic Endpoint and has permission equivalent to &#x60;\&quot;delete\&quot;&#x60;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return owner
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_OWNER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getOwner() {
    return owner;
  }


  @JsonProperty(JSON_PROPERTY_OWNER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setOwner(String owner) {
    this.owner = owner;
  }


  public MsgVpnTopicEndpoint permission(PermissionEnum permission) {
    
    this.permission = permission;
    return this;
  }

   /**
   * The permission level for all consumers of the Topic Endpoint, excluding the owner. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;no-access\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;no-access\&quot; - Disallows all access. \&quot;read-only\&quot; - Read-only access to the messages. \&quot;consume\&quot; - Consume (read and remove) messages. \&quot;modify-topic\&quot; - Consume messages or modify the topic/selector. \&quot;delete\&quot; - Consume messages, modify the topic/selector or delete the Client created endpoint altogether. &lt;/pre&gt; 
   * @return permission
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PERMISSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public PermissionEnum getPermission() {
    return permission;
  }


  @JsonProperty(JSON_PROPERTY_PERMISSION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPermission(PermissionEnum permission) {
    this.permission = permission;
  }


  public MsgVpnTopicEndpoint redeliveryEnabled(Boolean redeliveryEnabled) {
    
    this.redeliveryEnabled = redeliveryEnabled;
    return this;
  }

   /**
   * Enable or disable message redelivery. When enabled, the number of redelivery attempts is controlled by maxRedeliveryCount. When disabled, the message will never be delivered from the topic-endpoint more than once. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.18.
   * @return redeliveryEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REDELIVERY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRedeliveryEnabled() {
    return redeliveryEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REDELIVERY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRedeliveryEnabled(Boolean redeliveryEnabled) {
    this.redeliveryEnabled = redeliveryEnabled;
  }


  public MsgVpnTopicEndpoint rejectLowPriorityMsgEnabled(Boolean rejectLowPriorityMsgEnabled) {
    
    this.rejectLowPriorityMsgEnabled = rejectLowPriorityMsgEnabled;
    return this;
  }

   /**
   * Enable or disable the checking of low priority messages against the &#x60;rejectLowPriorityMsgLimit&#x60;. This may only be enabled if &#x60;rejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return rejectLowPriorityMsgEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRejectLowPriorityMsgEnabled() {
    return rejectLowPriorityMsgEnabled;
  }


  @JsonProperty(JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRejectLowPriorityMsgEnabled(Boolean rejectLowPriorityMsgEnabled) {
    this.rejectLowPriorityMsgEnabled = rejectLowPriorityMsgEnabled;
  }


  public MsgVpnTopicEndpoint rejectLowPriorityMsgLimit(Long rejectLowPriorityMsgLimit) {
    
    this.rejectLowPriorityMsgLimit = rejectLowPriorityMsgLimit;
    return this;
  }

   /**
   * The number of messages of any priority in the Topic Endpoint above which low priority messages are not admitted but higher priority messages are allowed. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;.
   * @return rejectLowPriorityMsgLimit
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_LIMIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getRejectLowPriorityMsgLimit() {
    return rejectLowPriorityMsgLimit;
  }


  @JsonProperty(JSON_PROPERTY_REJECT_LOW_PRIORITY_MSG_LIMIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRejectLowPriorityMsgLimit(Long rejectLowPriorityMsgLimit) {
    this.rejectLowPriorityMsgLimit = rejectLowPriorityMsgLimit;
  }


  public MsgVpnTopicEndpoint rejectMsgToSenderOnDiscardBehavior(RejectMsgToSenderOnDiscardBehaviorEnum rejectMsgToSenderOnDiscardBehavior) {
    
    this.rejectMsgToSenderOnDiscardBehavior = rejectMsgToSenderOnDiscardBehavior;
    return this;
  }

   /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;never\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-topic-endpoint-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Topic Endpoint is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt; 
   * @return rejectMsgToSenderOnDiscardBehavior
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public RejectMsgToSenderOnDiscardBehaviorEnum getRejectMsgToSenderOnDiscardBehavior() {
    return rejectMsgToSenderOnDiscardBehavior;
  }


  @JsonProperty(JSON_PROPERTY_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRejectMsgToSenderOnDiscardBehavior(RejectMsgToSenderOnDiscardBehaviorEnum rejectMsgToSenderOnDiscardBehavior) {
    this.rejectMsgToSenderOnDiscardBehavior = rejectMsgToSenderOnDiscardBehavior;
  }


  public MsgVpnTopicEndpoint respectMsgPriorityEnabled(Boolean respectMsgPriorityEnabled) {
    
    this.respectMsgPriorityEnabled = respectMsgPriorityEnabled;
    return this;
  }

   /**
   * Enable or disable the respecting of message priority. When enabled, messages contained in the Topic Endpoint are delivered in priority order, from 9 (highest) to 0 (lowest). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.8.
   * @return respectMsgPriorityEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESPECT_MSG_PRIORITY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRespectMsgPriorityEnabled() {
    return respectMsgPriorityEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESPECT_MSG_PRIORITY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRespectMsgPriorityEnabled(Boolean respectMsgPriorityEnabled) {
    this.respectMsgPriorityEnabled = respectMsgPriorityEnabled;
  }


  public MsgVpnTopicEndpoint respectTtlEnabled(Boolean respectTtlEnabled) {
    
    this.respectTtlEnabled = respectTtlEnabled;
    return this;
  }

   /**
   * Enable or disable the respecting of the time-to-live (TTL) for messages in the Topic Endpoint. When enabled, expired messages are discarded or moved to the DMQ. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return respectTtlEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_RESPECT_TTL_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getRespectTtlEnabled() {
    return respectTtlEnabled;
  }


  @JsonProperty(JSON_PROPERTY_RESPECT_TTL_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRespectTtlEnabled(Boolean respectTtlEnabled) {
    this.respectTtlEnabled = respectTtlEnabled;
  }


  public MsgVpnTopicEndpoint topicEndpointName(String topicEndpointName) {
    
    this.topicEndpointName = topicEndpointName;
    return this;
  }

   /**
   * The name of the Topic Endpoint.
   * @return topicEndpointName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOPIC_ENDPOINT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTopicEndpointName() {
    return topicEndpointName;
  }


  @JsonProperty(JSON_PROPERTY_TOPIC_ENDPOINT_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTopicEndpointName(String topicEndpointName) {
    this.topicEndpointName = topicEndpointName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnTopicEndpoint msgVpnTopicEndpoint = (MsgVpnTopicEndpoint) o;
    return Objects.equals(this.accessType, msgVpnTopicEndpoint.accessType) &&
        Objects.equals(this.consumerAckPropagationEnabled, msgVpnTopicEndpoint.consumerAckPropagationEnabled) &&
        Objects.equals(this.deadMsgQueue, msgVpnTopicEndpoint.deadMsgQueue) &&
        Objects.equals(this.deliveryCountEnabled, msgVpnTopicEndpoint.deliveryCountEnabled) &&
        Objects.equals(this.deliveryDelay, msgVpnTopicEndpoint.deliveryDelay) &&
        Objects.equals(this.egressEnabled, msgVpnTopicEndpoint.egressEnabled) &&
        Objects.equals(this.eventBindCountThreshold, msgVpnTopicEndpoint.eventBindCountThreshold) &&
        Objects.equals(this.eventRejectLowPriorityMsgLimitThreshold, msgVpnTopicEndpoint.eventRejectLowPriorityMsgLimitThreshold) &&
        Objects.equals(this.eventSpoolUsageThreshold, msgVpnTopicEndpoint.eventSpoolUsageThreshold) &&
        Objects.equals(this.ingressEnabled, msgVpnTopicEndpoint.ingressEnabled) &&
        Objects.equals(this.maxBindCount, msgVpnTopicEndpoint.maxBindCount) &&
        Objects.equals(this.maxDeliveredUnackedMsgsPerFlow, msgVpnTopicEndpoint.maxDeliveredUnackedMsgsPerFlow) &&
        Objects.equals(this.maxMsgSize, msgVpnTopicEndpoint.maxMsgSize) &&
        Objects.equals(this.maxRedeliveryCount, msgVpnTopicEndpoint.maxRedeliveryCount) &&
        Objects.equals(this.maxSpoolUsage, msgVpnTopicEndpoint.maxSpoolUsage) &&
        Objects.equals(this.maxTtl, msgVpnTopicEndpoint.maxTtl) &&
        Objects.equals(this.msgVpnName, msgVpnTopicEndpoint.msgVpnName) &&
        Objects.equals(this.owner, msgVpnTopicEndpoint.owner) &&
        Objects.equals(this.permission, msgVpnTopicEndpoint.permission) &&
        Objects.equals(this.redeliveryEnabled, msgVpnTopicEndpoint.redeliveryEnabled) &&
        Objects.equals(this.rejectLowPriorityMsgEnabled, msgVpnTopicEndpoint.rejectLowPriorityMsgEnabled) &&
        Objects.equals(this.rejectLowPriorityMsgLimit, msgVpnTopicEndpoint.rejectLowPriorityMsgLimit) &&
        Objects.equals(this.rejectMsgToSenderOnDiscardBehavior, msgVpnTopicEndpoint.rejectMsgToSenderOnDiscardBehavior) &&
        Objects.equals(this.respectMsgPriorityEnabled, msgVpnTopicEndpoint.respectMsgPriorityEnabled) &&
        Objects.equals(this.respectTtlEnabled, msgVpnTopicEndpoint.respectTtlEnabled) &&
        Objects.equals(this.topicEndpointName, msgVpnTopicEndpoint.topicEndpointName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessType, consumerAckPropagationEnabled, deadMsgQueue, deliveryCountEnabled, deliveryDelay, egressEnabled, eventBindCountThreshold, eventRejectLowPriorityMsgLimitThreshold, eventSpoolUsageThreshold, ingressEnabled, maxBindCount, maxDeliveredUnackedMsgsPerFlow, maxMsgSize, maxRedeliveryCount, maxSpoolUsage, maxTtl, msgVpnName, owner, permission, redeliveryEnabled, rejectLowPriorityMsgEnabled, rejectLowPriorityMsgLimit, rejectMsgToSenderOnDiscardBehavior, respectMsgPriorityEnabled, respectTtlEnabled, topicEndpointName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnTopicEndpoint {\n");
    sb.append("    accessType: ").append(toIndentedString(accessType)).append("\n");
    sb.append("    consumerAckPropagationEnabled: ").append(toIndentedString(consumerAckPropagationEnabled)).append("\n");
    sb.append("    deadMsgQueue: ").append(toIndentedString(deadMsgQueue)).append("\n");
    sb.append("    deliveryCountEnabled: ").append(toIndentedString(deliveryCountEnabled)).append("\n");
    sb.append("    deliveryDelay: ").append(toIndentedString(deliveryDelay)).append("\n");
    sb.append("    egressEnabled: ").append(toIndentedString(egressEnabled)).append("\n");
    sb.append("    eventBindCountThreshold: ").append(toIndentedString(eventBindCountThreshold)).append("\n");
    sb.append("    eventRejectLowPriorityMsgLimitThreshold: ").append(toIndentedString(eventRejectLowPriorityMsgLimitThreshold)).append("\n");
    sb.append("    eventSpoolUsageThreshold: ").append(toIndentedString(eventSpoolUsageThreshold)).append("\n");
    sb.append("    ingressEnabled: ").append(toIndentedString(ingressEnabled)).append("\n");
    sb.append("    maxBindCount: ").append(toIndentedString(maxBindCount)).append("\n");
    sb.append("    maxDeliveredUnackedMsgsPerFlow: ").append(toIndentedString(maxDeliveredUnackedMsgsPerFlow)).append("\n");
    sb.append("    maxMsgSize: ").append(toIndentedString(maxMsgSize)).append("\n");
    sb.append("    maxRedeliveryCount: ").append(toIndentedString(maxRedeliveryCount)).append("\n");
    sb.append("    maxSpoolUsage: ").append(toIndentedString(maxSpoolUsage)).append("\n");
    sb.append("    maxTtl: ").append(toIndentedString(maxTtl)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
    sb.append("    redeliveryEnabled: ").append(toIndentedString(redeliveryEnabled)).append("\n");
    sb.append("    rejectLowPriorityMsgEnabled: ").append(toIndentedString(rejectLowPriorityMsgEnabled)).append("\n");
    sb.append("    rejectLowPriorityMsgLimit: ").append(toIndentedString(rejectLowPriorityMsgLimit)).append("\n");
    sb.append("    rejectMsgToSenderOnDiscardBehavior: ").append(toIndentedString(rejectMsgToSenderOnDiscardBehavior)).append("\n");
    sb.append("    respectMsgPriorityEnabled: ").append(toIndentedString(respectMsgPriorityEnabled)).append("\n");
    sb.append("    respectTtlEnabled: ").append(toIndentedString(respectTtlEnabled)).append("\n");
    sb.append("    topicEndpointName: ").append(toIndentedString(topicEndpointName)).append("\n");
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

