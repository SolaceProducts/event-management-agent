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
 * MsgVpnMqttSession
 */
@JsonPropertyOrder({
  MsgVpnMqttSession.JSON_PROPERTY_ENABLED,
  MsgVpnMqttSession.JSON_PROPERTY_MQTT_SESSION_CLIENT_ID,
  MsgVpnMqttSession.JSON_PROPERTY_MQTT_SESSION_VIRTUAL_ROUTER,
  MsgVpnMqttSession.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnMqttSession.JSON_PROPERTY_OWNER,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_CONSUMER_ACK_PROPAGATION_ENABLED,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_DEAD_MSG_QUEUE,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_EVENT_BIND_COUNT_THRESHOLD,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_EVENT_MSG_SPOOL_USAGE_THRESHOLD,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_MAX_BIND_COUNT,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_MAX_MSG_SIZE,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_MAX_MSG_SPOOL_USAGE,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_MAX_REDELIVERY_COUNT,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_MAX_TTL,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_ENABLED,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_LIMIT,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR,
  MsgVpnMqttSession.JSON_PROPERTY_QUEUE_RESPECT_TTL_ENABLED
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class MsgVpnMqttSession {
  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_MQTT_SESSION_CLIENT_ID = "mqttSessionClientId";
  private String mqttSessionClientId;

  /**
   * The virtual router of the MQTT Session. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The MQTT Session belongs to the primary virtual router. \&quot;backup\&quot; - The MQTT Session belongs to the backup virtual router. \&quot;auto\&quot; - The MQTT Session is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt; 
   */
  public enum MqttSessionVirtualRouterEnum {
    PRIMARY("primary"),
    
    BACKUP("backup"),
    
    AUTO("auto");

    private String value;

    MqttSessionVirtualRouterEnum(String value) {
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
    public static MqttSessionVirtualRouterEnum fromValue(String value) {
      for (MqttSessionVirtualRouterEnum b : MqttSessionVirtualRouterEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_MQTT_SESSION_VIRTUAL_ROUTER = "mqttSessionVirtualRouter";
  private MqttSessionVirtualRouterEnum mqttSessionVirtualRouter;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_OWNER = "owner";
  private String owner;

  public static final String JSON_PROPERTY_QUEUE_CONSUMER_ACK_PROPAGATION_ENABLED = "queueConsumerAckPropagationEnabled";
  private Boolean queueConsumerAckPropagationEnabled;

  public static final String JSON_PROPERTY_QUEUE_DEAD_MSG_QUEUE = "queueDeadMsgQueue";
  private String queueDeadMsgQueue;

  public static final String JSON_PROPERTY_QUEUE_EVENT_BIND_COUNT_THRESHOLD = "queueEventBindCountThreshold";
  private EventThreshold queueEventBindCountThreshold;

  public static final String JSON_PROPERTY_QUEUE_EVENT_MSG_SPOOL_USAGE_THRESHOLD = "queueEventMsgSpoolUsageThreshold";
  private EventThreshold queueEventMsgSpoolUsageThreshold;

  public static final String JSON_PROPERTY_QUEUE_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD = "queueEventRejectLowPriorityMsgLimitThreshold";
  private EventThreshold queueEventRejectLowPriorityMsgLimitThreshold;

  public static final String JSON_PROPERTY_QUEUE_MAX_BIND_COUNT = "queueMaxBindCount";
  private Long queueMaxBindCount;

  public static final String JSON_PROPERTY_QUEUE_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW = "queueMaxDeliveredUnackedMsgsPerFlow";
  private Long queueMaxDeliveredUnackedMsgsPerFlow;

  public static final String JSON_PROPERTY_QUEUE_MAX_MSG_SIZE = "queueMaxMsgSize";
  private Integer queueMaxMsgSize;

  public static final String JSON_PROPERTY_QUEUE_MAX_MSG_SPOOL_USAGE = "queueMaxMsgSpoolUsage";
  private Long queueMaxMsgSpoolUsage;

  public static final String JSON_PROPERTY_QUEUE_MAX_REDELIVERY_COUNT = "queueMaxRedeliveryCount";
  private Long queueMaxRedeliveryCount;

  public static final String JSON_PROPERTY_QUEUE_MAX_TTL = "queueMaxTtl";
  private Long queueMaxTtl;

  public static final String JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_ENABLED = "queueRejectLowPriorityMsgEnabled";
  private Boolean queueRejectLowPriorityMsgEnabled;

  public static final String JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_LIMIT = "queueRejectLowPriorityMsgLimit";
  private Long queueRejectLowPriorityMsgLimit;

  /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-queue-enabled\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt;  Available since 2.14.
   */
  public enum QueueRejectMsgToSenderOnDiscardBehaviorEnum {
    ALWAYS("always"),
    
    WHEN_QUEUE_ENABLED("when-queue-enabled"),
    
    NEVER("never");

    private String value;

    QueueRejectMsgToSenderOnDiscardBehaviorEnum(String value) {
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
    public static QueueRejectMsgToSenderOnDiscardBehaviorEnum fromValue(String value) {
      for (QueueRejectMsgToSenderOnDiscardBehaviorEnum b : QueueRejectMsgToSenderOnDiscardBehaviorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR = "queueRejectMsgToSenderOnDiscardBehavior";
  private QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior;

  public static final String JSON_PROPERTY_QUEUE_RESPECT_TTL_ENABLED = "queueRespectTtlEnabled";
  private Boolean queueRespectTtlEnabled;

  public MsgVpnMqttSession() {
  }

  public MsgVpnMqttSession enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the MQTT Session. When disabled, the client is disconnected, new messages matching QoS 0 subscriptions are discarded, and new messages matching QoS 1 subscriptions are stored for future delivery. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
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


  public MsgVpnMqttSession mqttSessionClientId(String mqttSessionClientId) {
    
    this.mqttSessionClientId = mqttSessionClientId;
    return this;
  }

   /**
   * The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet.
   * @return mqttSessionClientId
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MQTT_SESSION_CLIENT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMqttSessionClientId() {
    return mqttSessionClientId;
  }


  @JsonProperty(JSON_PROPERTY_MQTT_SESSION_CLIENT_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMqttSessionClientId(String mqttSessionClientId) {
    this.mqttSessionClientId = mqttSessionClientId;
  }


  public MsgVpnMqttSession mqttSessionVirtualRouter(MqttSessionVirtualRouterEnum mqttSessionVirtualRouter) {
    
    this.mqttSessionVirtualRouter = mqttSessionVirtualRouter;
    return this;
  }

   /**
   * The virtual router of the MQTT Session. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The MQTT Session belongs to the primary virtual router. \&quot;backup\&quot; - The MQTT Session belongs to the backup virtual router. \&quot;auto\&quot; - The MQTT Session is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt; 
   * @return mqttSessionVirtualRouter
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MQTT_SESSION_VIRTUAL_ROUTER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public MqttSessionVirtualRouterEnum getMqttSessionVirtualRouter() {
    return mqttSessionVirtualRouter;
  }


  @JsonProperty(JSON_PROPERTY_MQTT_SESSION_VIRTUAL_ROUTER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMqttSessionVirtualRouter(MqttSessionVirtualRouterEnum mqttSessionVirtualRouter) {
    this.mqttSessionVirtualRouter = mqttSessionVirtualRouter;
  }


  public MsgVpnMqttSession msgVpnName(String msgVpnName) {
    
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


  public MsgVpnMqttSession owner(String owner) {
    
    this.owner = owner;
    return this;
  }

   /**
   * The owner of the MQTT Session. For externally-created sessions this defaults to the Client Username of the connecting client. For management-created sessions this defaults to empty. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public MsgVpnMqttSession queueConsumerAckPropagationEnabled(Boolean queueConsumerAckPropagationEnabled) {
    
    this.queueConsumerAckPropagationEnabled = queueConsumerAckPropagationEnabled;
    return this;
  }

   /**
   * Enable or disable the propagation of consumer acknowledgements (ACKs) received on the active replication Message VPN to the standby replication Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.14.
   * @return queueConsumerAckPropagationEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_CONSUMER_ACK_PROPAGATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getQueueConsumerAckPropagationEnabled() {
    return queueConsumerAckPropagationEnabled;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_CONSUMER_ACK_PROPAGATION_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueConsumerAckPropagationEnabled(Boolean queueConsumerAckPropagationEnabled) {
    this.queueConsumerAckPropagationEnabled = queueConsumerAckPropagationEnabled;
  }


  public MsgVpnMqttSession queueDeadMsgQueue(String queueDeadMsgQueue) {
    
    this.queueDeadMsgQueue = queueDeadMsgQueue;
    return this;
  }

   /**
   * The name of the Dead Message Queue (DMQ) used by the MQTT Session Queue. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. Available since 2.14.
   * @return queueDeadMsgQueue
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_DEAD_MSG_QUEUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getQueueDeadMsgQueue() {
    return queueDeadMsgQueue;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_DEAD_MSG_QUEUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueDeadMsgQueue(String queueDeadMsgQueue) {
    this.queueDeadMsgQueue = queueDeadMsgQueue;
  }


  public MsgVpnMqttSession queueEventBindCountThreshold(EventThreshold queueEventBindCountThreshold) {
    
    this.queueEventBindCountThreshold = queueEventBindCountThreshold;
    return this;
  }

   /**
   * Get queueEventBindCountThreshold
   * @return queueEventBindCountThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_BIND_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getQueueEventBindCountThreshold() {
    return queueEventBindCountThreshold;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_BIND_COUNT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueEventBindCountThreshold(EventThreshold queueEventBindCountThreshold) {
    this.queueEventBindCountThreshold = queueEventBindCountThreshold;
  }


  public MsgVpnMqttSession queueEventMsgSpoolUsageThreshold(EventThreshold queueEventMsgSpoolUsageThreshold) {
    
    this.queueEventMsgSpoolUsageThreshold = queueEventMsgSpoolUsageThreshold;
    return this;
  }

   /**
   * Get queueEventMsgSpoolUsageThreshold
   * @return queueEventMsgSpoolUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_MSG_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getQueueEventMsgSpoolUsageThreshold() {
    return queueEventMsgSpoolUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_MSG_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueEventMsgSpoolUsageThreshold(EventThreshold queueEventMsgSpoolUsageThreshold) {
    this.queueEventMsgSpoolUsageThreshold = queueEventMsgSpoolUsageThreshold;
  }


  public MsgVpnMqttSession queueEventRejectLowPriorityMsgLimitThreshold(EventThreshold queueEventRejectLowPriorityMsgLimitThreshold) {
    
    this.queueEventRejectLowPriorityMsgLimitThreshold = queueEventRejectLowPriorityMsgLimitThreshold;
    return this;
  }

   /**
   * Get queueEventRejectLowPriorityMsgLimitThreshold
   * @return queueEventRejectLowPriorityMsgLimitThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getQueueEventRejectLowPriorityMsgLimitThreshold() {
    return queueEventRejectLowPriorityMsgLimitThreshold;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_REJECT_LOW_PRIORITY_MSG_LIMIT_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueEventRejectLowPriorityMsgLimitThreshold(EventThreshold queueEventRejectLowPriorityMsgLimitThreshold) {
    this.queueEventRejectLowPriorityMsgLimitThreshold = queueEventRejectLowPriorityMsgLimitThreshold;
  }


  public MsgVpnMqttSession queueMaxBindCount(Long queueMaxBindCount) {
    
    this.queueMaxBindCount = queueMaxBindCount;
    return this;
  }

   /**
   * The maximum number of consumer flows that can bind to the MQTT Session Queue. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;. Available since 2.14.
   * @return queueMaxBindCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_BIND_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getQueueMaxBindCount() {
    return queueMaxBindCount;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_BIND_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueMaxBindCount(Long queueMaxBindCount) {
    this.queueMaxBindCount = queueMaxBindCount;
  }


  public MsgVpnMqttSession queueMaxDeliveredUnackedMsgsPerFlow(Long queueMaxDeliveredUnackedMsgsPerFlow) {
    
    this.queueMaxDeliveredUnackedMsgsPerFlow = queueMaxDeliveredUnackedMsgsPerFlow;
    return this;
  }

   /**
   * The maximum number of messages delivered but not acknowledged per flow for the MQTT Session Queue. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000&#x60;. Available since 2.14.
   * @return queueMaxDeliveredUnackedMsgsPerFlow
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getQueueMaxDeliveredUnackedMsgsPerFlow() {
    return queueMaxDeliveredUnackedMsgsPerFlow;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueMaxDeliveredUnackedMsgsPerFlow(Long queueMaxDeliveredUnackedMsgsPerFlow) {
    this.queueMaxDeliveredUnackedMsgsPerFlow = queueMaxDeliveredUnackedMsgsPerFlow;
  }


  public MsgVpnMqttSession queueMaxMsgSize(Integer queueMaxMsgSize) {
    
    this.queueMaxMsgSize = queueMaxMsgSize;
    return this;
  }

   /**
   * The maximum message size allowed in the MQTT Session Queue, in bytes (B). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000000&#x60;. Available since 2.14.
   * @return queueMaxMsgSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_MSG_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getQueueMaxMsgSize() {
    return queueMaxMsgSize;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_MSG_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueMaxMsgSize(Integer queueMaxMsgSize) {
    this.queueMaxMsgSize = queueMaxMsgSize;
  }


  public MsgVpnMqttSession queueMaxMsgSpoolUsage(Long queueMaxMsgSpoolUsage) {
    
    this.queueMaxMsgSpoolUsage = queueMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage allowed by the MQTT Session Queue, in megabytes (MB). A value of 0 only allows spooling of the last message received and disables quota checking. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;5000&#x60;. Available since 2.14.
   * @return queueMaxMsgSpoolUsage
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getQueueMaxMsgSpoolUsage() {
    return queueMaxMsgSpoolUsage;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_MSG_SPOOL_USAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueMaxMsgSpoolUsage(Long queueMaxMsgSpoolUsage) {
    this.queueMaxMsgSpoolUsage = queueMaxMsgSpoolUsage;
  }


  public MsgVpnMqttSession queueMaxRedeliveryCount(Long queueMaxRedeliveryCount) {
    
    this.queueMaxRedeliveryCount = queueMaxRedeliveryCount;
    return this;
  }

   /**
   * The maximum number of times the MQTT Session Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.14.
   * @return queueMaxRedeliveryCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_REDELIVERY_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getQueueMaxRedeliveryCount() {
    return queueMaxRedeliveryCount;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_REDELIVERY_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueMaxRedeliveryCount(Long queueMaxRedeliveryCount) {
    this.queueMaxRedeliveryCount = queueMaxRedeliveryCount;
  }


  public MsgVpnMqttSession queueMaxTtl(Long queueMaxTtl) {
    
    this.queueMaxTtl = queueMaxTtl;
    return this;
  }

   /**
   * The maximum time in seconds a message can stay in the MQTT Session Queue when &#x60;queueRespectTtlEnabled&#x60; is &#x60;\&quot;true\&quot;&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;queueMaxTtl&#x60; configured for the MQTT Session Queue, is exceeded. A value of 0 disables expiry. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.14.
   * @return queueMaxTtl
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_TTL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getQueueMaxTtl() {
    return queueMaxTtl;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_MAX_TTL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueMaxTtl(Long queueMaxTtl) {
    this.queueMaxTtl = queueMaxTtl;
  }


  public MsgVpnMqttSession queueRejectLowPriorityMsgEnabled(Boolean queueRejectLowPriorityMsgEnabled) {
    
    this.queueRejectLowPriorityMsgEnabled = queueRejectLowPriorityMsgEnabled;
    return this;
  }

   /**
   * Enable or disable the checking of low priority messages against the &#x60;queueRejectLowPriorityMsgLimit&#x60;. This may only be enabled if &#x60;queueRejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.14.
   * @return queueRejectLowPriorityMsgEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getQueueRejectLowPriorityMsgEnabled() {
    return queueRejectLowPriorityMsgEnabled;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueRejectLowPriorityMsgEnabled(Boolean queueRejectLowPriorityMsgEnabled) {
    this.queueRejectLowPriorityMsgEnabled = queueRejectLowPriorityMsgEnabled;
  }


  public MsgVpnMqttSession queueRejectLowPriorityMsgLimit(Long queueRejectLowPriorityMsgLimit) {
    
    this.queueRejectLowPriorityMsgLimit = queueRejectLowPriorityMsgLimit;
    return this;
  }

   /**
   * The number of messages of any priority in the MQTT Session Queue above which low priority messages are not admitted but higher priority messages are allowed. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.14.
   * @return queueRejectLowPriorityMsgLimit
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_LIMIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getQueueRejectLowPriorityMsgLimit() {
    return queueRejectLowPriorityMsgLimit;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_REJECT_LOW_PRIORITY_MSG_LIMIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueRejectLowPriorityMsgLimit(Long queueRejectLowPriorityMsgLimit) {
    this.queueRejectLowPriorityMsgLimit = queueRejectLowPriorityMsgLimit;
  }


  public MsgVpnMqttSession queueRejectMsgToSenderOnDiscardBehavior(QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior) {
    
    this.queueRejectMsgToSenderOnDiscardBehavior = queueRejectMsgToSenderOnDiscardBehavior;
    return this;
  }

   /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-queue-enabled\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt;  Available since 2.14.
   * @return queueRejectMsgToSenderOnDiscardBehavior
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public QueueRejectMsgToSenderOnDiscardBehaviorEnum getQueueRejectMsgToSenderOnDiscardBehavior() {
    return queueRejectMsgToSenderOnDiscardBehavior;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueRejectMsgToSenderOnDiscardBehavior(QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior) {
    this.queueRejectMsgToSenderOnDiscardBehavior = queueRejectMsgToSenderOnDiscardBehavior;
  }


  public MsgVpnMqttSession queueRespectTtlEnabled(Boolean queueRespectTtlEnabled) {
    
    this.queueRespectTtlEnabled = queueRespectTtlEnabled;
    return this;
  }

   /**
   * Enable or disable the respecting of the time-to-live (TTL) for messages in the MQTT Session Queue. When enabled, expired messages are discarded or moved to the DMQ. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.14.
   * @return queueRespectTtlEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_RESPECT_TTL_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getQueueRespectTtlEnabled() {
    return queueRespectTtlEnabled;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_RESPECT_TTL_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueRespectTtlEnabled(Boolean queueRespectTtlEnabled) {
    this.queueRespectTtlEnabled = queueRespectTtlEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnMqttSession msgVpnMqttSession = (MsgVpnMqttSession) o;
    return Objects.equals(this.enabled, msgVpnMqttSession.enabled) &&
        Objects.equals(this.mqttSessionClientId, msgVpnMqttSession.mqttSessionClientId) &&
        Objects.equals(this.mqttSessionVirtualRouter, msgVpnMqttSession.mqttSessionVirtualRouter) &&
        Objects.equals(this.msgVpnName, msgVpnMqttSession.msgVpnName) &&
        Objects.equals(this.owner, msgVpnMqttSession.owner) &&
        Objects.equals(this.queueConsumerAckPropagationEnabled, msgVpnMqttSession.queueConsumerAckPropagationEnabled) &&
        Objects.equals(this.queueDeadMsgQueue, msgVpnMqttSession.queueDeadMsgQueue) &&
        Objects.equals(this.queueEventBindCountThreshold, msgVpnMqttSession.queueEventBindCountThreshold) &&
        Objects.equals(this.queueEventMsgSpoolUsageThreshold, msgVpnMqttSession.queueEventMsgSpoolUsageThreshold) &&
        Objects.equals(this.queueEventRejectLowPriorityMsgLimitThreshold, msgVpnMqttSession.queueEventRejectLowPriorityMsgLimitThreshold) &&
        Objects.equals(this.queueMaxBindCount, msgVpnMqttSession.queueMaxBindCount) &&
        Objects.equals(this.queueMaxDeliveredUnackedMsgsPerFlow, msgVpnMqttSession.queueMaxDeliveredUnackedMsgsPerFlow) &&
        Objects.equals(this.queueMaxMsgSize, msgVpnMqttSession.queueMaxMsgSize) &&
        Objects.equals(this.queueMaxMsgSpoolUsage, msgVpnMqttSession.queueMaxMsgSpoolUsage) &&
        Objects.equals(this.queueMaxRedeliveryCount, msgVpnMqttSession.queueMaxRedeliveryCount) &&
        Objects.equals(this.queueMaxTtl, msgVpnMqttSession.queueMaxTtl) &&
        Objects.equals(this.queueRejectLowPriorityMsgEnabled, msgVpnMqttSession.queueRejectLowPriorityMsgEnabled) &&
        Objects.equals(this.queueRejectLowPriorityMsgLimit, msgVpnMqttSession.queueRejectLowPriorityMsgLimit) &&
        Objects.equals(this.queueRejectMsgToSenderOnDiscardBehavior, msgVpnMqttSession.queueRejectMsgToSenderOnDiscardBehavior) &&
        Objects.equals(this.queueRespectTtlEnabled, msgVpnMqttSession.queueRespectTtlEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, mqttSessionClientId, mqttSessionVirtualRouter, msgVpnName, owner, queueConsumerAckPropagationEnabled, queueDeadMsgQueue, queueEventBindCountThreshold, queueEventMsgSpoolUsageThreshold, queueEventRejectLowPriorityMsgLimitThreshold, queueMaxBindCount, queueMaxDeliveredUnackedMsgsPerFlow, queueMaxMsgSize, queueMaxMsgSpoolUsage, queueMaxRedeliveryCount, queueMaxTtl, queueRejectLowPriorityMsgEnabled, queueRejectLowPriorityMsgLimit, queueRejectMsgToSenderOnDiscardBehavior, queueRespectTtlEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnMqttSession {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    mqttSessionClientId: ").append(toIndentedString(mqttSessionClientId)).append("\n");
    sb.append("    mqttSessionVirtualRouter: ").append(toIndentedString(mqttSessionVirtualRouter)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    queueConsumerAckPropagationEnabled: ").append(toIndentedString(queueConsumerAckPropagationEnabled)).append("\n");
    sb.append("    queueDeadMsgQueue: ").append(toIndentedString(queueDeadMsgQueue)).append("\n");
    sb.append("    queueEventBindCountThreshold: ").append(toIndentedString(queueEventBindCountThreshold)).append("\n");
    sb.append("    queueEventMsgSpoolUsageThreshold: ").append(toIndentedString(queueEventMsgSpoolUsageThreshold)).append("\n");
    sb.append("    queueEventRejectLowPriorityMsgLimitThreshold: ").append(toIndentedString(queueEventRejectLowPriorityMsgLimitThreshold)).append("\n");
    sb.append("    queueMaxBindCount: ").append(toIndentedString(queueMaxBindCount)).append("\n");
    sb.append("    queueMaxDeliveredUnackedMsgsPerFlow: ").append(toIndentedString(queueMaxDeliveredUnackedMsgsPerFlow)).append("\n");
    sb.append("    queueMaxMsgSize: ").append(toIndentedString(queueMaxMsgSize)).append("\n");
    sb.append("    queueMaxMsgSpoolUsage: ").append(toIndentedString(queueMaxMsgSpoolUsage)).append("\n");
    sb.append("    queueMaxRedeliveryCount: ").append(toIndentedString(queueMaxRedeliveryCount)).append("\n");
    sb.append("    queueMaxTtl: ").append(toIndentedString(queueMaxTtl)).append("\n");
    sb.append("    queueRejectLowPriorityMsgEnabled: ").append(toIndentedString(queueRejectLowPriorityMsgEnabled)).append("\n");
    sb.append("    queueRejectLowPriorityMsgLimit: ").append(toIndentedString(queueRejectLowPriorityMsgLimit)).append("\n");
    sb.append("    queueRejectMsgToSenderOnDiscardBehavior: ").append(toIndentedString(queueRejectMsgToSenderOnDiscardBehavior)).append("\n");
    sb.append("    queueRespectTtlEnabled: ").append(toIndentedString(queueRespectTtlEnabled)).append("\n");
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

