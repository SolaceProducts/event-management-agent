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
 * MsgVpnBridgeRemoteMsgVpn
 */
@JsonPropertyOrder({
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_BRIDGE_NAME,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_BRIDGE_VIRTUAL_ROUTER,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_CLIENT_USERNAME,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_COMPRESSED_DATA_ENABLED,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_CONNECT_ORDER,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_EGRESS_FLOW_WINDOW_SIZE,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_ENABLED,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_MSG_VPN_NAME,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_PASSWORD,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_QUEUE_BINDING,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_REMOTE_MSG_VPN_INTERFACE,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_REMOTE_MSG_VPN_LOCATION,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_REMOTE_MSG_VPN_NAME,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_TLS_ENABLED,
  MsgVpnBridgeRemoteMsgVpn.JSON_PROPERTY_UNIDIRECTIONAL_CLIENT_PROFILE
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-17T23:49:01.929728+01:00[Europe/London]")
public class MsgVpnBridgeRemoteMsgVpn {
  public static final String JSON_PROPERTY_BRIDGE_NAME = "bridgeName";
  private String bridgeName;

  /**
   * The virtual router of the Bridge. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The Bridge is used for the primary virtual router. \&quot;backup\&quot; - The Bridge is used for the backup virtual router. \&quot;auto\&quot; - The Bridge is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt; 
   */
  public enum BridgeVirtualRouterEnum {
    PRIMARY("primary"),
    
    BACKUP("backup"),
    
    AUTO("auto");

    private String value;

    BridgeVirtualRouterEnum(String value) {
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
    public static BridgeVirtualRouterEnum fromValue(String value) {
      for (BridgeVirtualRouterEnum b : BridgeVirtualRouterEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_BRIDGE_VIRTUAL_ROUTER = "bridgeVirtualRouter";
  private BridgeVirtualRouterEnum bridgeVirtualRouter;

  public static final String JSON_PROPERTY_CLIENT_USERNAME = "clientUsername";
  private String clientUsername;

  public static final String JSON_PROPERTY_COMPRESSED_DATA_ENABLED = "compressedDataEnabled";
  private Boolean compressedDataEnabled;

  public static final String JSON_PROPERTY_CONNECT_ORDER = "connectOrder";
  private Integer connectOrder;

  public static final String JSON_PROPERTY_EGRESS_FLOW_WINDOW_SIZE = "egressFlowWindowSize";
  private Long egressFlowWindowSize;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_MSG_VPN_NAME = "msgVpnName";
  private String msgVpnName;

  public static final String JSON_PROPERTY_PASSWORD = "password";
  private String password;

  public static final String JSON_PROPERTY_QUEUE_BINDING = "queueBinding";
  private String queueBinding;

  public static final String JSON_PROPERTY_REMOTE_MSG_VPN_INTERFACE = "remoteMsgVpnInterface";
  private String remoteMsgVpnInterface;

  public static final String JSON_PROPERTY_REMOTE_MSG_VPN_LOCATION = "remoteMsgVpnLocation";
  private String remoteMsgVpnLocation;

  public static final String JSON_PROPERTY_REMOTE_MSG_VPN_NAME = "remoteMsgVpnName";
  private String remoteMsgVpnName;

  public static final String JSON_PROPERTY_TLS_ENABLED = "tlsEnabled";
  private Boolean tlsEnabled;

  public static final String JSON_PROPERTY_UNIDIRECTIONAL_CLIENT_PROFILE = "unidirectionalClientProfile";
  private String unidirectionalClientProfile;

  public MsgVpnBridgeRemoteMsgVpn() {
  }

  public MsgVpnBridgeRemoteMsgVpn bridgeName(String bridgeName) {
    
    this.bridgeName = bridgeName;
    return this;
  }

   /**
   * The name of the Bridge.
   * @return bridgeName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getBridgeName() {
    return bridgeName;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgeName(String bridgeName) {
    this.bridgeName = bridgeName;
  }


  public MsgVpnBridgeRemoteMsgVpn bridgeVirtualRouter(BridgeVirtualRouterEnum bridgeVirtualRouter) {
    
    this.bridgeVirtualRouter = bridgeVirtualRouter;
    return this;
  }

   /**
   * The virtual router of the Bridge. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The Bridge is used for the primary virtual router. \&quot;backup\&quot; - The Bridge is used for the backup virtual router. \&quot;auto\&quot; - The Bridge is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt; 
   * @return bridgeVirtualRouter
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGE_VIRTUAL_ROUTER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public BridgeVirtualRouterEnum getBridgeVirtualRouter() {
    return bridgeVirtualRouter;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGE_VIRTUAL_ROUTER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgeVirtualRouter(BridgeVirtualRouterEnum bridgeVirtualRouter) {
    this.bridgeVirtualRouter = bridgeVirtualRouter;
  }


  public MsgVpnBridgeRemoteMsgVpn clientUsername(String clientUsername) {
    
    this.clientUsername = clientUsername;
    return this;
  }

   /**
   * The Client Username the Bridge uses to login to the remote Message VPN. This per remote Message VPN value overrides the value provided for the Bridge overall. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return clientUsername
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientUsername() {
    return clientUsername;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_USERNAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientUsername(String clientUsername) {
    this.clientUsername = clientUsername;
  }


  public MsgVpnBridgeRemoteMsgVpn compressedDataEnabled(Boolean compressedDataEnabled) {
    
    this.compressedDataEnabled = compressedDataEnabled;
    return this;
  }

   /**
   * Enable or disable data compression for the remote Message VPN connection. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
   * @return compressedDataEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_COMPRESSED_DATA_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getCompressedDataEnabled() {
    return compressedDataEnabled;
  }


  @JsonProperty(JSON_PROPERTY_COMPRESSED_DATA_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCompressedDataEnabled(Boolean compressedDataEnabled) {
    this.compressedDataEnabled = compressedDataEnabled;
  }


  public MsgVpnBridgeRemoteMsgVpn connectOrder(Integer connectOrder) {
    
    this.connectOrder = connectOrder;
    return this;
  }

   /**
   * The preference given to incoming connections from remote Message VPN hosts, from 1 (highest priority) to 4 (lowest priority). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;4&#x60;.
   * @return connectOrder
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CONNECT_ORDER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getConnectOrder() {
    return connectOrder;
  }


  @JsonProperty(JSON_PROPERTY_CONNECT_ORDER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setConnectOrder(Integer connectOrder) {
    this.connectOrder = connectOrder;
  }


  public MsgVpnBridgeRemoteMsgVpn egressFlowWindowSize(Long egressFlowWindowSize) {
    
    this.egressFlowWindowSize = egressFlowWindowSize;
    return this;
  }

   /**
   * The number of outstanding guaranteed messages that can be transmitted over the remote Message VPN connection before an acknowledgement is received. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;255&#x60;.
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


  public MsgVpnBridgeRemoteMsgVpn enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the remote Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
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


  public MsgVpnBridgeRemoteMsgVpn msgVpnName(String msgVpnName) {
    
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


  public MsgVpnBridgeRemoteMsgVpn password(String password) {
    
    this.password = password;
    return this;
  }

   /**
   * The password for the Client Username. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return password
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getPassword() {
    return password;
  }


  @JsonProperty(JSON_PROPERTY_PASSWORD)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPassword(String password) {
    this.password = password;
  }


  public MsgVpnBridgeRemoteMsgVpn queueBinding(String queueBinding) {
    
    this.queueBinding = queueBinding;
    return this;
  }

   /**
   * The queue binding of the Bridge in the remote Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
   * @return queueBinding
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_BINDING)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getQueueBinding() {
    return queueBinding;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_BINDING)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueBinding(String queueBinding) {
    this.queueBinding = queueBinding;
  }


  public MsgVpnBridgeRemoteMsgVpn remoteMsgVpnInterface(String remoteMsgVpnInterface) {
    
    this.remoteMsgVpnInterface = remoteMsgVpnInterface;
    return this;
  }

   /**
   * The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, &#x60;remoteMsgVpnLocation&#x60; must not be a virtual router name.
   * @return remoteMsgVpnInterface
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REMOTE_MSG_VPN_INTERFACE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRemoteMsgVpnInterface() {
    return remoteMsgVpnInterface;
  }


  @JsonProperty(JSON_PROPERTY_REMOTE_MSG_VPN_INTERFACE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRemoteMsgVpnInterface(String remoteMsgVpnInterface) {
    this.remoteMsgVpnInterface = remoteMsgVpnInterface;
  }


  public MsgVpnBridgeRemoteMsgVpn remoteMsgVpnLocation(String remoteMsgVpnLocation) {
    
    this.remoteMsgVpnLocation = remoteMsgVpnLocation;
    return this;
  }

   /**
   * The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \&quot;v:\&quot;).
   * @return remoteMsgVpnLocation
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REMOTE_MSG_VPN_LOCATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRemoteMsgVpnLocation() {
    return remoteMsgVpnLocation;
  }


  @JsonProperty(JSON_PROPERTY_REMOTE_MSG_VPN_LOCATION)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRemoteMsgVpnLocation(String remoteMsgVpnLocation) {
    this.remoteMsgVpnLocation = remoteMsgVpnLocation;
  }


  public MsgVpnBridgeRemoteMsgVpn remoteMsgVpnName(String remoteMsgVpnName) {
    
    this.remoteMsgVpnName = remoteMsgVpnName;
    return this;
  }

   /**
   * The name of the remote Message VPN.
   * @return remoteMsgVpnName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REMOTE_MSG_VPN_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRemoteMsgVpnName() {
    return remoteMsgVpnName;
  }


  @JsonProperty(JSON_PROPERTY_REMOTE_MSG_VPN_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRemoteMsgVpnName(String remoteMsgVpnName) {
    this.remoteMsgVpnName = remoteMsgVpnName;
  }


  public MsgVpnBridgeRemoteMsgVpn tlsEnabled(Boolean tlsEnabled) {
    
    this.tlsEnabled = tlsEnabled;
    return this;
  }

   /**
   * Enable or disable encryption (TLS) for the remote Message VPN connection. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;.
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


  public MsgVpnBridgeRemoteMsgVpn unidirectionalClientProfile(String unidirectionalClientProfile) {
    
    this.unidirectionalClientProfile = unidirectionalClientProfile;
    return this;
  }

   /**
   * The Client Profile for the unidirectional Bridge of the remote Message VPN. The Client Profile must exist in the local Message VPN, and it is used only for the TCP parameters. Note that the default client profile has a TCP maximum window size of 2MB. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;#client-profile\&quot;&#x60;.
   * @return unidirectionalClientProfile
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_UNIDIRECTIONAL_CLIENT_PROFILE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getUnidirectionalClientProfile() {
    return unidirectionalClientProfile;
  }


  @JsonProperty(JSON_PROPERTY_UNIDIRECTIONAL_CLIENT_PROFILE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUnidirectionalClientProfile(String unidirectionalClientProfile) {
    this.unidirectionalClientProfile = unidirectionalClientProfile;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnBridgeRemoteMsgVpn msgVpnBridgeRemoteMsgVpn = (MsgVpnBridgeRemoteMsgVpn) o;
    return Objects.equals(this.bridgeName, msgVpnBridgeRemoteMsgVpn.bridgeName) &&
        Objects.equals(this.bridgeVirtualRouter, msgVpnBridgeRemoteMsgVpn.bridgeVirtualRouter) &&
        Objects.equals(this.clientUsername, msgVpnBridgeRemoteMsgVpn.clientUsername) &&
        Objects.equals(this.compressedDataEnabled, msgVpnBridgeRemoteMsgVpn.compressedDataEnabled) &&
        Objects.equals(this.connectOrder, msgVpnBridgeRemoteMsgVpn.connectOrder) &&
        Objects.equals(this.egressFlowWindowSize, msgVpnBridgeRemoteMsgVpn.egressFlowWindowSize) &&
        Objects.equals(this.enabled, msgVpnBridgeRemoteMsgVpn.enabled) &&
        Objects.equals(this.msgVpnName, msgVpnBridgeRemoteMsgVpn.msgVpnName) &&
        Objects.equals(this.password, msgVpnBridgeRemoteMsgVpn.password) &&
        Objects.equals(this.queueBinding, msgVpnBridgeRemoteMsgVpn.queueBinding) &&
        Objects.equals(this.remoteMsgVpnInterface, msgVpnBridgeRemoteMsgVpn.remoteMsgVpnInterface) &&
        Objects.equals(this.remoteMsgVpnLocation, msgVpnBridgeRemoteMsgVpn.remoteMsgVpnLocation) &&
        Objects.equals(this.remoteMsgVpnName, msgVpnBridgeRemoteMsgVpn.remoteMsgVpnName) &&
        Objects.equals(this.tlsEnabled, msgVpnBridgeRemoteMsgVpn.tlsEnabled) &&
        Objects.equals(this.unidirectionalClientProfile, msgVpnBridgeRemoteMsgVpn.unidirectionalClientProfile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bridgeName, bridgeVirtualRouter, clientUsername, compressedDataEnabled, connectOrder, egressFlowWindowSize, enabled, msgVpnName, password, queueBinding, remoteMsgVpnInterface, remoteMsgVpnLocation, remoteMsgVpnName, tlsEnabled, unidirectionalClientProfile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnBridgeRemoteMsgVpn {\n");
    sb.append("    bridgeName: ").append(toIndentedString(bridgeName)).append("\n");
    sb.append("    bridgeVirtualRouter: ").append(toIndentedString(bridgeVirtualRouter)).append("\n");
    sb.append("    clientUsername: ").append(toIndentedString(clientUsername)).append("\n");
    sb.append("    compressedDataEnabled: ").append(toIndentedString(compressedDataEnabled)).append("\n");
    sb.append("    connectOrder: ").append(toIndentedString(connectOrder)).append("\n");
    sb.append("    egressFlowWindowSize: ").append(toIndentedString(egressFlowWindowSize)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    queueBinding: ").append(toIndentedString(queueBinding)).append("\n");
    sb.append("    remoteMsgVpnInterface: ").append(toIndentedString(remoteMsgVpnInterface)).append("\n");
    sb.append("    remoteMsgVpnLocation: ").append(toIndentedString(remoteMsgVpnLocation)).append("\n");
    sb.append("    remoteMsgVpnName: ").append(toIndentedString(remoteMsgVpnName)).append("\n");
    sb.append("    tlsEnabled: ").append(toIndentedString(tlsEnabled)).append("\n");
    sb.append("    unidirectionalClientProfile: ").append(toIndentedString(unidirectionalClientProfile)).append("\n");
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

