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
 * DmrClusterLink
 */
@JsonPropertyOrder({
  DmrClusterLink.JSON_PROPERTY_AUTHENTICATION_BASIC_PASSWORD,
  DmrClusterLink.JSON_PROPERTY_AUTHENTICATION_SCHEME,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MAX_DEPTH,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MIN_MSG_BURST,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MAX_DEPTH,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MIN_MSG_BURST,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MAX_DEPTH,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MIN_MSG_BURST,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MAX_DEPTH,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MIN_MSG_BURST,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MAX_DEPTH,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MIN_MSG_BURST,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_TCP_CONGESTION_WINDOW_SIZE,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE_TIME,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_SEGMENT_SIZE,
  DmrClusterLink.JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_WINDOW_SIZE,
  DmrClusterLink.JSON_PROPERTY_DMR_CLUSTER_NAME,
  DmrClusterLink.JSON_PROPERTY_EGRESS_FLOW_WINDOW_SIZE,
  DmrClusterLink.JSON_PROPERTY_ENABLED,
  DmrClusterLink.JSON_PROPERTY_INITIATOR,
  DmrClusterLink.JSON_PROPERTY_QUEUE_DEAD_MSG_QUEUE,
  DmrClusterLink.JSON_PROPERTY_QUEUE_EVENT_SPOOL_USAGE_THRESHOLD,
  DmrClusterLink.JSON_PROPERTY_QUEUE_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW,
  DmrClusterLink.JSON_PROPERTY_QUEUE_MAX_MSG_SPOOL_USAGE,
  DmrClusterLink.JSON_PROPERTY_QUEUE_MAX_REDELIVERY_COUNT,
  DmrClusterLink.JSON_PROPERTY_QUEUE_MAX_TTL,
  DmrClusterLink.JSON_PROPERTY_QUEUE_REJECT_MSG_TO_SENDER_ON_DISCARD_BEHAVIOR,
  DmrClusterLink.JSON_PROPERTY_QUEUE_RESPECT_TTL_ENABLED,
  DmrClusterLink.JSON_PROPERTY_REMOTE_NODE_NAME,
  DmrClusterLink.JSON_PROPERTY_SPAN,
  DmrClusterLink.JSON_PROPERTY_TRANSPORT_COMPRESSED_ENABLED,
  DmrClusterLink.JSON_PROPERTY_TRANSPORT_TLS_ENABLED
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-17T23:49:01.929728+01:00[Europe/London]")
public class DmrClusterLink {
  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_PASSWORD = "authenticationBasicPassword";
  private String authenticationBasicPassword;

  /**
   * The authentication scheme to be used by the Link which initiates connections to the remote node. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt; 
   */
  public enum AuthenticationSchemeEnum {
    BASIC("basic"),
    
    CLIENT_CERTIFICATE("client-certificate");

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

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MAX_DEPTH = "clientProfileQueueControl1MaxDepth";
  private Integer clientProfileQueueControl1MaxDepth;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MIN_MSG_BURST = "clientProfileQueueControl1MinMsgBurst";
  private Integer clientProfileQueueControl1MinMsgBurst;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MAX_DEPTH = "clientProfileQueueDirect1MaxDepth";
  private Integer clientProfileQueueDirect1MaxDepth;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MIN_MSG_BURST = "clientProfileQueueDirect1MinMsgBurst";
  private Integer clientProfileQueueDirect1MinMsgBurst;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MAX_DEPTH = "clientProfileQueueDirect2MaxDepth";
  private Integer clientProfileQueueDirect2MaxDepth;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MIN_MSG_BURST = "clientProfileQueueDirect2MinMsgBurst";
  private Integer clientProfileQueueDirect2MinMsgBurst;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MAX_DEPTH = "clientProfileQueueDirect3MaxDepth";
  private Integer clientProfileQueueDirect3MaxDepth;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MIN_MSG_BURST = "clientProfileQueueDirect3MinMsgBurst";
  private Integer clientProfileQueueDirect3MinMsgBurst;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MAX_DEPTH = "clientProfileQueueGuaranteed1MaxDepth";
  private Integer clientProfileQueueGuaranteed1MaxDepth;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MIN_MSG_BURST = "clientProfileQueueGuaranteed1MinMsgBurst";
  private Integer clientProfileQueueGuaranteed1MinMsgBurst;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_TCP_CONGESTION_WINDOW_SIZE = "clientProfileTcpCongestionWindowSize";
  private Long clientProfileTcpCongestionWindowSize;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT = "clientProfileTcpKeepaliveCount";
  private Long clientProfileTcpKeepaliveCount;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE_TIME = "clientProfileTcpKeepaliveIdleTime";
  private Long clientProfileTcpKeepaliveIdleTime;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL = "clientProfileTcpKeepaliveInterval";
  private Long clientProfileTcpKeepaliveInterval;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_SEGMENT_SIZE = "clientProfileTcpMaxSegmentSize";
  private Long clientProfileTcpMaxSegmentSize;

  public static final String JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_WINDOW_SIZE = "clientProfileTcpMaxWindowSize";
  private Long clientProfileTcpMaxWindowSize;

  public static final String JSON_PROPERTY_DMR_CLUSTER_NAME = "dmrClusterName";
  private String dmrClusterName;

  public static final String JSON_PROPERTY_EGRESS_FLOW_WINDOW_SIZE = "egressFlowWindowSize";
  private Long egressFlowWindowSize;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  /**
   * The initiator of the Link&#39;s TCP connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;lexical\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;lexical\&quot; - The \&quot;higher\&quot; node-name initiates. \&quot;local\&quot; - The local node initiates. \&quot;remote\&quot; - The remote node initiates. &lt;/pre&gt; 
   */
  public enum InitiatorEnum {
    LEXICAL("lexical"),
    
    LOCAL("local"),
    
    REMOTE("remote");

    private String value;

    InitiatorEnum(String value) {
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
    public static InitiatorEnum fromValue(String value) {
      for (InitiatorEnum b : InitiatorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_INITIATOR = "initiator";
  private InitiatorEnum initiator;

  public static final String JSON_PROPERTY_QUEUE_DEAD_MSG_QUEUE = "queueDeadMsgQueue";
  private String queueDeadMsgQueue;

  public static final String JSON_PROPERTY_QUEUE_EVENT_SPOOL_USAGE_THRESHOLD = "queueEventSpoolUsageThreshold";
  private EventThreshold queueEventSpoolUsageThreshold;

  public static final String JSON_PROPERTY_QUEUE_MAX_DELIVERED_UNACKED_MSGS_PER_FLOW = "queueMaxDeliveredUnackedMsgsPerFlow";
  private Long queueMaxDeliveredUnackedMsgsPerFlow;

  public static final String JSON_PROPERTY_QUEUE_MAX_MSG_SPOOL_USAGE = "queueMaxMsgSpoolUsage";
  private Long queueMaxMsgSpoolUsage;

  public static final String JSON_PROPERTY_QUEUE_MAX_REDELIVERY_COUNT = "queueMaxRedeliveryCount";
  private Long queueMaxRedeliveryCount;

  public static final String JSON_PROPERTY_QUEUE_MAX_TTL = "queueMaxTtl";
  private Long queueMaxTtl;

  /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;always\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt; 
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

  public static final String JSON_PROPERTY_REMOTE_NODE_NAME = "remoteNodeName";
  private String remoteNodeName;

  /**
   * The span of the Link, either internal or external. Internal Links connect nodes within the same Cluster. External Links connect nodes within different Clusters. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;external\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Link to same cluster. \&quot;external\&quot; - Link to other cluster. &lt;/pre&gt; 
   */
  public enum SpanEnum {
    INTERNAL("internal"),
    
    EXTERNAL("external");

    private String value;

    SpanEnum(String value) {
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
    public static SpanEnum fromValue(String value) {
      for (SpanEnum b : SpanEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_SPAN = "span";
  private SpanEnum span;

  public static final String JSON_PROPERTY_TRANSPORT_COMPRESSED_ENABLED = "transportCompressedEnabled";
  private Boolean transportCompressedEnabled;

  public static final String JSON_PROPERTY_TRANSPORT_TLS_ENABLED = "transportTlsEnabled";
  private Boolean transportTlsEnabled;

  public DmrClusterLink() {
  }

  public DmrClusterLink authenticationBasicPassword(String authenticationBasicPassword) {
    
    this.authenticationBasicPassword = authenticationBasicPassword;
    return this;
  }

   /**
   * The password used to authenticate with the remote node when using basic internal authentication. If this per-Link password is not configured, the Cluster&#39;s password is used instead. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return authenticationBasicPassword
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationBasicPassword() {
    return authenticationBasicPassword;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_BASIC_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationBasicPassword(String authenticationBasicPassword) {
    this.authenticationBasicPassword = authenticationBasicPassword;
  }


  public DmrClusterLink authenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    
    this.authenticationScheme = authenticationScheme;
    return this;
  }

   /**
   * The authentication scheme to be used by the Link which initiates connections to the remote node. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt; 
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


  public DmrClusterLink clientProfileQueueControl1MaxDepth(Integer clientProfileQueueControl1MaxDepth) {
    
    this.clientProfileQueueControl1MaxDepth = clientProfileQueueControl1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Control 1\&quot; (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;20000&#x60;.
   * @return clientProfileQueueControl1MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueControl1MaxDepth() {
    return clientProfileQueueControl1MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueControl1MaxDepth(Integer clientProfileQueueControl1MaxDepth) {
    this.clientProfileQueueControl1MaxDepth = clientProfileQueueControl1MaxDepth;
  }


  public DmrClusterLink clientProfileQueueControl1MinMsgBurst(Integer clientProfileQueueControl1MinMsgBurst) {
    
    this.clientProfileQueueControl1MinMsgBurst = clientProfileQueueControl1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Control 1\&quot; (C-1) priority queue, regardless of the &#x60;clientProfileQueueControl1MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;4&#x60;.
   * @return clientProfileQueueControl1MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueControl1MinMsgBurst() {
    return clientProfileQueueControl1MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_CONTROL1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueControl1MinMsgBurst(Integer clientProfileQueueControl1MinMsgBurst) {
    this.clientProfileQueueControl1MinMsgBurst = clientProfileQueueControl1MinMsgBurst;
  }


  public DmrClusterLink clientProfileQueueDirect1MaxDepth(Integer clientProfileQueueDirect1MaxDepth) {
    
    this.clientProfileQueueDirect1MaxDepth = clientProfileQueueDirect1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Direct 1\&quot; (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;20000&#x60;.
   * @return clientProfileQueueDirect1MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueDirect1MaxDepth() {
    return clientProfileQueueDirect1MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueDirect1MaxDepth(Integer clientProfileQueueDirect1MaxDepth) {
    this.clientProfileQueueDirect1MaxDepth = clientProfileQueueDirect1MaxDepth;
  }


  public DmrClusterLink clientProfileQueueDirect1MinMsgBurst(Integer clientProfileQueueDirect1MinMsgBurst) {
    
    this.clientProfileQueueDirect1MinMsgBurst = clientProfileQueueDirect1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Direct 1\&quot; (D-1) priority queue, regardless of the &#x60;clientProfileQueueDirect1MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;4&#x60;.
   * @return clientProfileQueueDirect1MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueDirect1MinMsgBurst() {
    return clientProfileQueueDirect1MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueDirect1MinMsgBurst(Integer clientProfileQueueDirect1MinMsgBurst) {
    this.clientProfileQueueDirect1MinMsgBurst = clientProfileQueueDirect1MinMsgBurst;
  }


  public DmrClusterLink clientProfileQueueDirect2MaxDepth(Integer clientProfileQueueDirect2MaxDepth) {
    
    this.clientProfileQueueDirect2MaxDepth = clientProfileQueueDirect2MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Direct 2\&quot; (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;20000&#x60;.
   * @return clientProfileQueueDirect2MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueDirect2MaxDepth() {
    return clientProfileQueueDirect2MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueDirect2MaxDepth(Integer clientProfileQueueDirect2MaxDepth) {
    this.clientProfileQueueDirect2MaxDepth = clientProfileQueueDirect2MaxDepth;
  }


  public DmrClusterLink clientProfileQueueDirect2MinMsgBurst(Integer clientProfileQueueDirect2MinMsgBurst) {
    
    this.clientProfileQueueDirect2MinMsgBurst = clientProfileQueueDirect2MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Direct 2\&quot; (D-2) priority queue, regardless of the &#x60;clientProfileQueueDirect2MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;4&#x60;.
   * @return clientProfileQueueDirect2MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueDirect2MinMsgBurst() {
    return clientProfileQueueDirect2MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT2_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueDirect2MinMsgBurst(Integer clientProfileQueueDirect2MinMsgBurst) {
    this.clientProfileQueueDirect2MinMsgBurst = clientProfileQueueDirect2MinMsgBurst;
  }


  public DmrClusterLink clientProfileQueueDirect3MaxDepth(Integer clientProfileQueueDirect3MaxDepth) {
    
    this.clientProfileQueueDirect3MaxDepth = clientProfileQueueDirect3MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Direct 3\&quot; (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;20000&#x60;.
   * @return clientProfileQueueDirect3MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueDirect3MaxDepth() {
    return clientProfileQueueDirect3MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueDirect3MaxDepth(Integer clientProfileQueueDirect3MaxDepth) {
    this.clientProfileQueueDirect3MaxDepth = clientProfileQueueDirect3MaxDepth;
  }


  public DmrClusterLink clientProfileQueueDirect3MinMsgBurst(Integer clientProfileQueueDirect3MinMsgBurst) {
    
    this.clientProfileQueueDirect3MinMsgBurst = clientProfileQueueDirect3MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Direct 3\&quot; (D-3) priority queue, regardless of the &#x60;clientProfileQueueDirect3MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;4&#x60;.
   * @return clientProfileQueueDirect3MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueDirect3MinMsgBurst() {
    return clientProfileQueueDirect3MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_DIRECT3_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueDirect3MinMsgBurst(Integer clientProfileQueueDirect3MinMsgBurst) {
    this.clientProfileQueueDirect3MinMsgBurst = clientProfileQueueDirect3MinMsgBurst;
  }


  public DmrClusterLink clientProfileQueueGuaranteed1MaxDepth(Integer clientProfileQueueGuaranteed1MaxDepth) {
    
    this.clientProfileQueueGuaranteed1MaxDepth = clientProfileQueueGuaranteed1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \&quot;Guaranteed 1\&quot; (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;20000&#x60;.
   * @return clientProfileQueueGuaranteed1MaxDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueGuaranteed1MaxDepth() {
    return clientProfileQueueGuaranteed1MaxDepth;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MAX_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueGuaranteed1MaxDepth(Integer clientProfileQueueGuaranteed1MaxDepth) {
    this.clientProfileQueueGuaranteed1MaxDepth = clientProfileQueueGuaranteed1MaxDepth;
  }


  public DmrClusterLink clientProfileQueueGuaranteed1MinMsgBurst(Integer clientProfileQueueGuaranteed1MinMsgBurst) {
    
    this.clientProfileQueueGuaranteed1MinMsgBurst = clientProfileQueueGuaranteed1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \&quot;Guaranteed 1\&quot; (G-3) priority queue, regardless of the &#x60;clientProfileQueueGuaranteed1MaxDepth&#x60; value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;255&#x60;.
   * @return clientProfileQueueGuaranteed1MinMsgBurst
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getClientProfileQueueGuaranteed1MinMsgBurst() {
    return clientProfileQueueGuaranteed1MinMsgBurst;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_QUEUE_GUARANTEED1_MIN_MSG_BURST)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileQueueGuaranteed1MinMsgBurst(Integer clientProfileQueueGuaranteed1MinMsgBurst) {
    this.clientProfileQueueGuaranteed1MinMsgBurst = clientProfileQueueGuaranteed1MinMsgBurst;
  }


  public DmrClusterLink clientProfileTcpCongestionWindowSize(Long clientProfileTcpCongestionWindowSize) {
    
    this.clientProfileTcpCongestionWindowSize = clientProfileTcpCongestionWindowSize;
    return this;
  }

   /**
   * The TCP initial congestion window size, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact support before changing this value. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;2&#x60;.
   * @return clientProfileTcpCongestionWindowSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_CONGESTION_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getClientProfileTcpCongestionWindowSize() {
    return clientProfileTcpCongestionWindowSize;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_CONGESTION_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileTcpCongestionWindowSize(Long clientProfileTcpCongestionWindowSize) {
    this.clientProfileTcpCongestionWindowSize = clientProfileTcpCongestionWindowSize;
  }


  public DmrClusterLink clientProfileTcpKeepaliveCount(Long clientProfileTcpKeepaliveCount) {
    
    this.clientProfileTcpKeepaliveCount = clientProfileTcpKeepaliveCount;
    return this;
  }

   /**
   * The number of TCP keepalive retransmissions to be carried out before declaring that the remote end is not available. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;5&#x60;.
   * @return clientProfileTcpKeepaliveCount
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getClientProfileTcpKeepaliveCount() {
    return clientProfileTcpKeepaliveCount;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_COUNT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileTcpKeepaliveCount(Long clientProfileTcpKeepaliveCount) {
    this.clientProfileTcpKeepaliveCount = clientProfileTcpKeepaliveCount;
  }


  public DmrClusterLink clientProfileTcpKeepaliveIdleTime(Long clientProfileTcpKeepaliveIdleTime) {
    
    this.clientProfileTcpKeepaliveIdleTime = clientProfileTcpKeepaliveIdleTime;
    return this;
  }

   /**
   * The amount of time a connection must remain idle before TCP begins sending keepalive probes, in seconds. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;3&#x60;.
   * @return clientProfileTcpKeepaliveIdleTime
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE_TIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getClientProfileTcpKeepaliveIdleTime() {
    return clientProfileTcpKeepaliveIdleTime;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_IDLE_TIME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileTcpKeepaliveIdleTime(Long clientProfileTcpKeepaliveIdleTime) {
    this.clientProfileTcpKeepaliveIdleTime = clientProfileTcpKeepaliveIdleTime;
  }


  public DmrClusterLink clientProfileTcpKeepaliveInterval(Long clientProfileTcpKeepaliveInterval) {
    
    this.clientProfileTcpKeepaliveInterval = clientProfileTcpKeepaliveInterval;
    return this;
  }

   /**
   * The amount of time between TCP keepalive retransmissions when no acknowledgement is received, in seconds. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1&#x60;.
   * @return clientProfileTcpKeepaliveInterval
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getClientProfileTcpKeepaliveInterval() {
    return clientProfileTcpKeepaliveInterval;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_KEEPALIVE_INTERVAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileTcpKeepaliveInterval(Long clientProfileTcpKeepaliveInterval) {
    this.clientProfileTcpKeepaliveInterval = clientProfileTcpKeepaliveInterval;
  }


  public DmrClusterLink clientProfileTcpMaxSegmentSize(Long clientProfileTcpMaxSegmentSize) {
    
    this.clientProfileTcpMaxSegmentSize = clientProfileTcpMaxSegmentSize;
    return this;
  }

   /**
   * The TCP maximum segment size, in bytes. Changes are applied to all existing connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1460&#x60;.
   * @return clientProfileTcpMaxSegmentSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_SEGMENT_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getClientProfileTcpMaxSegmentSize() {
    return clientProfileTcpMaxSegmentSize;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_SEGMENT_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileTcpMaxSegmentSize(Long clientProfileTcpMaxSegmentSize) {
    this.clientProfileTcpMaxSegmentSize = clientProfileTcpMaxSegmentSize;
  }


  public DmrClusterLink clientProfileTcpMaxWindowSize(Long clientProfileTcpMaxWindowSize) {
    
    this.clientProfileTcpMaxWindowSize = clientProfileTcpMaxWindowSize;
    return this;
  }

   /**
   * The TCP maximum window size, in kilobytes. Changes are applied to all existing connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;256&#x60;.
   * @return clientProfileTcpMaxWindowSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getClientProfileTcpMaxWindowSize() {
    return clientProfileTcpMaxWindowSize;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILE_TCP_MAX_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfileTcpMaxWindowSize(Long clientProfileTcpMaxWindowSize) {
    this.clientProfileTcpMaxWindowSize = clientProfileTcpMaxWindowSize;
  }


  public DmrClusterLink dmrClusterName(String dmrClusterName) {
    
    this.dmrClusterName = dmrClusterName;
    return this;
  }

   /**
   * The name of the Cluster.
   * @return dmrClusterName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DMR_CLUSTER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDmrClusterName() {
    return dmrClusterName;
  }


  @JsonProperty(JSON_PROPERTY_DMR_CLUSTER_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDmrClusterName(String dmrClusterName) {
    this.dmrClusterName = dmrClusterName;
  }


  public DmrClusterLink egressFlowWindowSize(Long egressFlowWindowSize) {
    
    this.egressFlowWindowSize = egressFlowWindowSize;
    return this;
  }

   /**
   * The number of outstanding guaranteed messages that can be sent over the Link before acknowledgement is received by the sender. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;255&#x60;.
   * @return egressFlowWindowSize
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EGRESS_FLOW_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getEgressFlowWindowSize() {
    return egressFlowWindowSize;
  }


  @JsonProperty(JSON_PROPERTY_EGRESS_FLOW_WINDOW_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEgressFlowWindowSize(Long egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
  }


  public DmrClusterLink enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Link. When disabled, subscription sets of this and the remote node are not kept up-to-date, and messages are not exchanged with the remote node. Published guaranteed messages will be queued up for future delivery based on current subscription sets. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
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


  public DmrClusterLink initiator(InitiatorEnum initiator) {
    
    this.initiator = initiator;
    return this;
  }

   /**
   * The initiator of the Link&#39;s TCP connections. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;lexical\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;lexical\&quot; - The \&quot;higher\&quot; node-name initiates. \&quot;local\&quot; - The local node initiates. \&quot;remote\&quot; - The remote node initiates. &lt;/pre&gt; 
   * @return initiator
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INITIATOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public InitiatorEnum getInitiator() {
    return initiator;
  }


  @JsonProperty(JSON_PROPERTY_INITIATOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInitiator(InitiatorEnum initiator) {
    this.initiator = initiator;
  }


  public DmrClusterLink queueDeadMsgQueue(String queueDeadMsgQueue) {
    
    this.queueDeadMsgQueue = queueDeadMsgQueue;
    return this;
  }

   /**
   * The name of the Dead Message Queue (DMQ) used by the Queue for discarded messages. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;.
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


  public DmrClusterLink queueEventSpoolUsageThreshold(EventThreshold queueEventSpoolUsageThreshold) {
    
    this.queueEventSpoolUsageThreshold = queueEventSpoolUsageThreshold;
    return this;
  }

   /**
   * Get queueEventSpoolUsageThreshold
   * @return queueEventSpoolUsageThreshold
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public EventThreshold getQueueEventSpoolUsageThreshold() {
    return queueEventSpoolUsageThreshold;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_EVENT_SPOOL_USAGE_THRESHOLD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueEventSpoolUsageThreshold(EventThreshold queueEventSpoolUsageThreshold) {
    this.queueEventSpoolUsageThreshold = queueEventSpoolUsageThreshold;
  }


  public DmrClusterLink queueMaxDeliveredUnackedMsgsPerFlow(Long queueMaxDeliveredUnackedMsgsPerFlow) {
    
    this.queueMaxDeliveredUnackedMsgsPerFlow = queueMaxDeliveredUnackedMsgsPerFlow;
    return this;
  }

   /**
   * The maximum number of messages delivered but not acknowledged per flow for the Queue. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;1000000&#x60;.
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


  public DmrClusterLink queueMaxMsgSpoolUsage(Long queueMaxMsgSpoolUsage) {
    
    this.queueMaxMsgSpoolUsage = queueMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage by the Queue (quota), in megabytes (MB). Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;800000&#x60;.
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


  public DmrClusterLink queueMaxRedeliveryCount(Long queueMaxRedeliveryCount) {
    
    this.queueMaxRedeliveryCount = queueMaxRedeliveryCount;
    return this;
  }

   /**
   * The maximum number of times the Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;0&#x60;.
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


  public DmrClusterLink queueMaxTtl(Long queueMaxTtl) {
    
    this.queueMaxTtl = queueMaxTtl;
    return this;
  }

   /**
   * The maximum time in seconds a message can stay in the Queue when &#x60;queueRespectTtlEnabled&#x60; is &#x60;true&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;queueMaxTtl&#x60; configured for the Queue, is exceeded. A value of 0 disables expiry. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;0&#x60;.
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


  public DmrClusterLink queueRejectMsgToSenderOnDiscardBehavior(QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior) {
    
    this.queueRejectMsgToSenderOnDiscardBehavior = queueRejectMsgToSenderOnDiscardBehavior;
    return this;
  }

   /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;always\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt; 
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


  public DmrClusterLink queueRespectTtlEnabled(Boolean queueRespectTtlEnabled) {
    
    this.queueRespectTtlEnabled = queueRespectTtlEnabled;
    return this;
  }

   /**
   * Enable or disable the respecting of the time-to-live (TTL) for messages in the Queue. When enabled, expired messages are discarded or moved to the DMQ. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
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


  public DmrClusterLink remoteNodeName(String remoteNodeName) {
    
    this.remoteNodeName = remoteNodeName;
    return this;
  }

   /**
   * The name of the node at the remote end of the Link.
   * @return remoteNodeName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REMOTE_NODE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRemoteNodeName() {
    return remoteNodeName;
  }


  @JsonProperty(JSON_PROPERTY_REMOTE_NODE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRemoteNodeName(String remoteNodeName) {
    this.remoteNodeName = remoteNodeName;
  }


  public DmrClusterLink span(SpanEnum span) {
    
    this.span = span;
    return this;
  }

   /**
   * The span of the Link, either internal or external. Internal Links connect nodes within the same Cluster. External Links connect nodes within different Clusters. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;external\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Link to same cluster. \&quot;external\&quot; - Link to other cluster. &lt;/pre&gt; 
   * @return span
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SPAN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public SpanEnum getSpan() {
    return span;
  }


  @JsonProperty(JSON_PROPERTY_SPAN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSpan(SpanEnum span) {
    this.span = span;
  }


  public DmrClusterLink transportCompressedEnabled(Boolean transportCompressedEnabled) {
    
    this.transportCompressedEnabled = transportCompressedEnabled;
    return this;
  }

   /**
   * Enable or disable compression on the Link. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
   * @return transportCompressedEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TRANSPORT_COMPRESSED_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTransportCompressedEnabled() {
    return transportCompressedEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TRANSPORT_COMPRESSED_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTransportCompressedEnabled(Boolean transportCompressedEnabled) {
    this.transportCompressedEnabled = transportCompressedEnabled;
  }


  public DmrClusterLink transportTlsEnabled(Boolean transportTlsEnabled) {
    
    this.transportTlsEnabled = transportTlsEnabled;
    return this;
  }

   /**
   * Enable or disable encryption (TLS) on the Link. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
   * @return transportTlsEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TRANSPORT_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTransportTlsEnabled() {
    return transportTlsEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TRANSPORT_TLS_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTransportTlsEnabled(Boolean transportTlsEnabled) {
    this.transportTlsEnabled = transportTlsEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DmrClusterLink dmrClusterLink = (DmrClusterLink) o;
    return Objects.equals(this.authenticationBasicPassword, dmrClusterLink.authenticationBasicPassword) &&
        Objects.equals(this.authenticationScheme, dmrClusterLink.authenticationScheme) &&
        Objects.equals(this.clientProfileQueueControl1MaxDepth, dmrClusterLink.clientProfileQueueControl1MaxDepth) &&
        Objects.equals(this.clientProfileQueueControl1MinMsgBurst, dmrClusterLink.clientProfileQueueControl1MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueDirect1MaxDepth, dmrClusterLink.clientProfileQueueDirect1MaxDepth) &&
        Objects.equals(this.clientProfileQueueDirect1MinMsgBurst, dmrClusterLink.clientProfileQueueDirect1MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueDirect2MaxDepth, dmrClusterLink.clientProfileQueueDirect2MaxDepth) &&
        Objects.equals(this.clientProfileQueueDirect2MinMsgBurst, dmrClusterLink.clientProfileQueueDirect2MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueDirect3MaxDepth, dmrClusterLink.clientProfileQueueDirect3MaxDepth) &&
        Objects.equals(this.clientProfileQueueDirect3MinMsgBurst, dmrClusterLink.clientProfileQueueDirect3MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueGuaranteed1MaxDepth, dmrClusterLink.clientProfileQueueGuaranteed1MaxDepth) &&
        Objects.equals(this.clientProfileQueueGuaranteed1MinMsgBurst, dmrClusterLink.clientProfileQueueGuaranteed1MinMsgBurst) &&
        Objects.equals(this.clientProfileTcpCongestionWindowSize, dmrClusterLink.clientProfileTcpCongestionWindowSize) &&
        Objects.equals(this.clientProfileTcpKeepaliveCount, dmrClusterLink.clientProfileTcpKeepaliveCount) &&
        Objects.equals(this.clientProfileTcpKeepaliveIdleTime, dmrClusterLink.clientProfileTcpKeepaliveIdleTime) &&
        Objects.equals(this.clientProfileTcpKeepaliveInterval, dmrClusterLink.clientProfileTcpKeepaliveInterval) &&
        Objects.equals(this.clientProfileTcpMaxSegmentSize, dmrClusterLink.clientProfileTcpMaxSegmentSize) &&
        Objects.equals(this.clientProfileTcpMaxWindowSize, dmrClusterLink.clientProfileTcpMaxWindowSize) &&
        Objects.equals(this.dmrClusterName, dmrClusterLink.dmrClusterName) &&
        Objects.equals(this.egressFlowWindowSize, dmrClusterLink.egressFlowWindowSize) &&
        Objects.equals(this.enabled, dmrClusterLink.enabled) &&
        Objects.equals(this.initiator, dmrClusterLink.initiator) &&
        Objects.equals(this.queueDeadMsgQueue, dmrClusterLink.queueDeadMsgQueue) &&
        Objects.equals(this.queueEventSpoolUsageThreshold, dmrClusterLink.queueEventSpoolUsageThreshold) &&
        Objects.equals(this.queueMaxDeliveredUnackedMsgsPerFlow, dmrClusterLink.queueMaxDeliveredUnackedMsgsPerFlow) &&
        Objects.equals(this.queueMaxMsgSpoolUsage, dmrClusterLink.queueMaxMsgSpoolUsage) &&
        Objects.equals(this.queueMaxRedeliveryCount, dmrClusterLink.queueMaxRedeliveryCount) &&
        Objects.equals(this.queueMaxTtl, dmrClusterLink.queueMaxTtl) &&
        Objects.equals(this.queueRejectMsgToSenderOnDiscardBehavior, dmrClusterLink.queueRejectMsgToSenderOnDiscardBehavior) &&
        Objects.equals(this.queueRespectTtlEnabled, dmrClusterLink.queueRespectTtlEnabled) &&
        Objects.equals(this.remoteNodeName, dmrClusterLink.remoteNodeName) &&
        Objects.equals(this.span, dmrClusterLink.span) &&
        Objects.equals(this.transportCompressedEnabled, dmrClusterLink.transportCompressedEnabled) &&
        Objects.equals(this.transportTlsEnabled, dmrClusterLink.transportTlsEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationBasicPassword, authenticationScheme, clientProfileQueueControl1MaxDepth, clientProfileQueueControl1MinMsgBurst, clientProfileQueueDirect1MaxDepth, clientProfileQueueDirect1MinMsgBurst, clientProfileQueueDirect2MaxDepth, clientProfileQueueDirect2MinMsgBurst, clientProfileQueueDirect3MaxDepth, clientProfileQueueDirect3MinMsgBurst, clientProfileQueueGuaranteed1MaxDepth, clientProfileQueueGuaranteed1MinMsgBurst, clientProfileTcpCongestionWindowSize, clientProfileTcpKeepaliveCount, clientProfileTcpKeepaliveIdleTime, clientProfileTcpKeepaliveInterval, clientProfileTcpMaxSegmentSize, clientProfileTcpMaxWindowSize, dmrClusterName, egressFlowWindowSize, enabled, initiator, queueDeadMsgQueue, queueEventSpoolUsageThreshold, queueMaxDeliveredUnackedMsgsPerFlow, queueMaxMsgSpoolUsage, queueMaxRedeliveryCount, queueMaxTtl, queueRejectMsgToSenderOnDiscardBehavior, queueRespectTtlEnabled, remoteNodeName, span, transportCompressedEnabled, transportTlsEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DmrClusterLink {\n");
    sb.append("    authenticationBasicPassword: ").append(toIndentedString(authenticationBasicPassword)).append("\n");
    sb.append("    authenticationScheme: ").append(toIndentedString(authenticationScheme)).append("\n");
    sb.append("    clientProfileQueueControl1MaxDepth: ").append(toIndentedString(clientProfileQueueControl1MaxDepth)).append("\n");
    sb.append("    clientProfileQueueControl1MinMsgBurst: ").append(toIndentedString(clientProfileQueueControl1MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueDirect1MaxDepth: ").append(toIndentedString(clientProfileQueueDirect1MaxDepth)).append("\n");
    sb.append("    clientProfileQueueDirect1MinMsgBurst: ").append(toIndentedString(clientProfileQueueDirect1MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueDirect2MaxDepth: ").append(toIndentedString(clientProfileQueueDirect2MaxDepth)).append("\n");
    sb.append("    clientProfileQueueDirect2MinMsgBurst: ").append(toIndentedString(clientProfileQueueDirect2MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueDirect3MaxDepth: ").append(toIndentedString(clientProfileQueueDirect3MaxDepth)).append("\n");
    sb.append("    clientProfileQueueDirect3MinMsgBurst: ").append(toIndentedString(clientProfileQueueDirect3MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueGuaranteed1MaxDepth: ").append(toIndentedString(clientProfileQueueGuaranteed1MaxDepth)).append("\n");
    sb.append("    clientProfileQueueGuaranteed1MinMsgBurst: ").append(toIndentedString(clientProfileQueueGuaranteed1MinMsgBurst)).append("\n");
    sb.append("    clientProfileTcpCongestionWindowSize: ").append(toIndentedString(clientProfileTcpCongestionWindowSize)).append("\n");
    sb.append("    clientProfileTcpKeepaliveCount: ").append(toIndentedString(clientProfileTcpKeepaliveCount)).append("\n");
    sb.append("    clientProfileTcpKeepaliveIdleTime: ").append(toIndentedString(clientProfileTcpKeepaliveIdleTime)).append("\n");
    sb.append("    clientProfileTcpKeepaliveInterval: ").append(toIndentedString(clientProfileTcpKeepaliveInterval)).append("\n");
    sb.append("    clientProfileTcpMaxSegmentSize: ").append(toIndentedString(clientProfileTcpMaxSegmentSize)).append("\n");
    sb.append("    clientProfileTcpMaxWindowSize: ").append(toIndentedString(clientProfileTcpMaxWindowSize)).append("\n");
    sb.append("    dmrClusterName: ").append(toIndentedString(dmrClusterName)).append("\n");
    sb.append("    egressFlowWindowSize: ").append(toIndentedString(egressFlowWindowSize)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
    sb.append("    queueDeadMsgQueue: ").append(toIndentedString(queueDeadMsgQueue)).append("\n");
    sb.append("    queueEventSpoolUsageThreshold: ").append(toIndentedString(queueEventSpoolUsageThreshold)).append("\n");
    sb.append("    queueMaxDeliveredUnackedMsgsPerFlow: ").append(toIndentedString(queueMaxDeliveredUnackedMsgsPerFlow)).append("\n");
    sb.append("    queueMaxMsgSpoolUsage: ").append(toIndentedString(queueMaxMsgSpoolUsage)).append("\n");
    sb.append("    queueMaxRedeliveryCount: ").append(toIndentedString(queueMaxRedeliveryCount)).append("\n");
    sb.append("    queueMaxTtl: ").append(toIndentedString(queueMaxTtl)).append("\n");
    sb.append("    queueRejectMsgToSenderOnDiscardBehavior: ").append(toIndentedString(queueRejectMsgToSenderOnDiscardBehavior)).append("\n");
    sb.append("    queueRespectTtlEnabled: ").append(toIndentedString(queueRespectTtlEnabled)).append("\n");
    sb.append("    remoteNodeName: ").append(toIndentedString(remoteNodeName)).append("\n");
    sb.append("    span: ").append(toIndentedString(span)).append("\n");
    sb.append("    transportCompressedEnabled: ").append(toIndentedString(transportCompressedEnabled)).append("\n");
    sb.append("    transportTlsEnabled: ").append(toIndentedString(transportTlsEnabled)).append("\n");
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

