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
 * MsgVpnLinks
 */
@JsonPropertyOrder({
  MsgVpnLinks.JSON_PROPERTY_ACL_PROFILES_URI,
  MsgVpnLinks.JSON_PROPERTY_AUTHENTICATION_OAUTH_PROFILES_URI,
  MsgVpnLinks.JSON_PROPERTY_AUTHENTICATION_OAUTH_PROVIDERS_URI,
  MsgVpnLinks.JSON_PROPERTY_AUTHORIZATION_GROUPS_URI,
  MsgVpnLinks.JSON_PROPERTY_BRIDGES_URI,
  MsgVpnLinks.JSON_PROPERTY_CERT_MATCHING_RULES_URI,
  MsgVpnLinks.JSON_PROPERTY_CLIENT_PROFILES_URI,
  MsgVpnLinks.JSON_PROPERTY_CLIENT_USERNAMES_URI,
  MsgVpnLinks.JSON_PROPERTY_DISTRIBUTED_CACHES_URI,
  MsgVpnLinks.JSON_PROPERTY_DMR_BRIDGES_URI,
  MsgVpnLinks.JSON_PROPERTY_JNDI_CONNECTION_FACTORIES_URI,
  MsgVpnLinks.JSON_PROPERTY_JNDI_QUEUES_URI,
  MsgVpnLinks.JSON_PROPERTY_JNDI_TOPICS_URI,
  MsgVpnLinks.JSON_PROPERTY_MQTT_RETAIN_CACHES_URI,
  MsgVpnLinks.JSON_PROPERTY_MQTT_SESSIONS_URI,
  MsgVpnLinks.JSON_PROPERTY_QUEUE_TEMPLATES_URI,
  MsgVpnLinks.JSON_PROPERTY_QUEUES_URI,
  MsgVpnLinks.JSON_PROPERTY_REPLAY_LOGS_URI,
  MsgVpnLinks.JSON_PROPERTY_REPLICATED_TOPICS_URI,
  MsgVpnLinks.JSON_PROPERTY_REST_DELIVERY_POINTS_URI,
  MsgVpnLinks.JSON_PROPERTY_SEQUENCED_TOPICS_URI,
  MsgVpnLinks.JSON_PROPERTY_TOPIC_ENDPOINT_TEMPLATES_URI,
  MsgVpnLinks.JSON_PROPERTY_TOPIC_ENDPOINTS_URI,
  MsgVpnLinks.JSON_PROPERTY_URI
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class MsgVpnLinks {
  public static final String JSON_PROPERTY_ACL_PROFILES_URI = "aclProfilesUri";
  private String aclProfilesUri;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_PROFILES_URI = "authenticationOauthProfilesUri";
  private String authenticationOauthProfilesUri;

  public static final String JSON_PROPERTY_AUTHENTICATION_OAUTH_PROVIDERS_URI = "authenticationOauthProvidersUri";
  private String authenticationOauthProvidersUri;

  public static final String JSON_PROPERTY_AUTHORIZATION_GROUPS_URI = "authorizationGroupsUri";
  private String authorizationGroupsUri;

  public static final String JSON_PROPERTY_BRIDGES_URI = "bridgesUri";
  private String bridgesUri;

  public static final String JSON_PROPERTY_CERT_MATCHING_RULES_URI = "certMatchingRulesUri";
  private String certMatchingRulesUri;

  public static final String JSON_PROPERTY_CLIENT_PROFILES_URI = "clientProfilesUri";
  private String clientProfilesUri;

  public static final String JSON_PROPERTY_CLIENT_USERNAMES_URI = "clientUsernamesUri";
  private String clientUsernamesUri;

  public static final String JSON_PROPERTY_DISTRIBUTED_CACHES_URI = "distributedCachesUri";
  private String distributedCachesUri;

  public static final String JSON_PROPERTY_DMR_BRIDGES_URI = "dmrBridgesUri";
  private String dmrBridgesUri;

  public static final String JSON_PROPERTY_JNDI_CONNECTION_FACTORIES_URI = "jndiConnectionFactoriesUri";
  private String jndiConnectionFactoriesUri;

  public static final String JSON_PROPERTY_JNDI_QUEUES_URI = "jndiQueuesUri";
  private String jndiQueuesUri;

  public static final String JSON_PROPERTY_JNDI_TOPICS_URI = "jndiTopicsUri";
  private String jndiTopicsUri;

  public static final String JSON_PROPERTY_MQTT_RETAIN_CACHES_URI = "mqttRetainCachesUri";
  private String mqttRetainCachesUri;

  public static final String JSON_PROPERTY_MQTT_SESSIONS_URI = "mqttSessionsUri";
  private String mqttSessionsUri;

  public static final String JSON_PROPERTY_QUEUE_TEMPLATES_URI = "queueTemplatesUri";
  private String queueTemplatesUri;

  public static final String JSON_PROPERTY_QUEUES_URI = "queuesUri";
  private String queuesUri;

  public static final String JSON_PROPERTY_REPLAY_LOGS_URI = "replayLogsUri";
  private String replayLogsUri;

  public static final String JSON_PROPERTY_REPLICATED_TOPICS_URI = "replicatedTopicsUri";
  private String replicatedTopicsUri;

  public static final String JSON_PROPERTY_REST_DELIVERY_POINTS_URI = "restDeliveryPointsUri";
  private String restDeliveryPointsUri;

  public static final String JSON_PROPERTY_SEQUENCED_TOPICS_URI = "sequencedTopicsUri";
  private String sequencedTopicsUri;

  public static final String JSON_PROPERTY_TOPIC_ENDPOINT_TEMPLATES_URI = "topicEndpointTemplatesUri";
  private String topicEndpointTemplatesUri;

  public static final String JSON_PROPERTY_TOPIC_ENDPOINTS_URI = "topicEndpointsUri";
  private String topicEndpointsUri;

  public static final String JSON_PROPERTY_URI = "uri";
  private String uri;

  public MsgVpnLinks() {
  }

  public MsgVpnLinks aclProfilesUri(String aclProfilesUri) {
    
    this.aclProfilesUri = aclProfilesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of ACL Profile objects.
   * @return aclProfilesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ACL_PROFILES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAclProfilesUri() {
    return aclProfilesUri;
  }


  @JsonProperty(JSON_PROPERTY_ACL_PROFILES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAclProfilesUri(String aclProfilesUri) {
    this.aclProfilesUri = aclProfilesUri;
  }


  public MsgVpnLinks authenticationOauthProfilesUri(String authenticationOauthProfilesUri) {
    
    this.authenticationOauthProfilesUri = authenticationOauthProfilesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of OAuth Profile objects. Available since 2.25.
   * @return authenticationOauthProfilesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_PROFILES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthProfilesUri() {
    return authenticationOauthProfilesUri;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_PROFILES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthProfilesUri(String authenticationOauthProfilesUri) {
    this.authenticationOauthProfilesUri = authenticationOauthProfilesUri;
  }


  public MsgVpnLinks authenticationOauthProvidersUri(String authenticationOauthProvidersUri) {
    
    this.authenticationOauthProvidersUri = authenticationOauthProvidersUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of OAuth Provider objects. Deprecated since 2.25. Replaced by authenticationOauthProfiles.
   * @return authenticationOauthProvidersUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_PROVIDERS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthenticationOauthProvidersUri() {
    return authenticationOauthProvidersUri;
  }


  @JsonProperty(JSON_PROPERTY_AUTHENTICATION_OAUTH_PROVIDERS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthenticationOauthProvidersUri(String authenticationOauthProvidersUri) {
    this.authenticationOauthProvidersUri = authenticationOauthProvidersUri;
  }


  public MsgVpnLinks authorizationGroupsUri(String authorizationGroupsUri) {
    
    this.authorizationGroupsUri = authorizationGroupsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Authorization Group objects.
   * @return authorizationGroupsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUPS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAuthorizationGroupsUri() {
    return authorizationGroupsUri;
  }


  @JsonProperty(JSON_PROPERTY_AUTHORIZATION_GROUPS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAuthorizationGroupsUri(String authorizationGroupsUri) {
    this.authorizationGroupsUri = authorizationGroupsUri;
  }


  public MsgVpnLinks bridgesUri(String bridgesUri) {
    
    this.bridgesUri = bridgesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Bridge objects.
   * @return bridgesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BRIDGES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getBridgesUri() {
    return bridgesUri;
  }


  @JsonProperty(JSON_PROPERTY_BRIDGES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBridgesUri(String bridgesUri) {
    this.bridgesUri = bridgesUri;
  }


  public MsgVpnLinks certMatchingRulesUri(String certMatchingRulesUri) {
    
    this.certMatchingRulesUri = certMatchingRulesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Certificate Matching Rule objects. Available since 2.27.
   * @return certMatchingRulesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CERT_MATCHING_RULES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getCertMatchingRulesUri() {
    return certMatchingRulesUri;
  }


  @JsonProperty(JSON_PROPERTY_CERT_MATCHING_RULES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setCertMatchingRulesUri(String certMatchingRulesUri) {
    this.certMatchingRulesUri = certMatchingRulesUri;
  }


  public MsgVpnLinks clientProfilesUri(String clientProfilesUri) {
    
    this.clientProfilesUri = clientProfilesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Client Profile objects.
   * @return clientProfilesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientProfilesUri() {
    return clientProfilesUri;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_PROFILES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientProfilesUri(String clientProfilesUri) {
    this.clientProfilesUri = clientProfilesUri;
  }


  public MsgVpnLinks clientUsernamesUri(String clientUsernamesUri) {
    
    this.clientUsernamesUri = clientUsernamesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Client Username objects.
   * @return clientUsernamesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_CLIENT_USERNAMES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getClientUsernamesUri() {
    return clientUsernamesUri;
  }


  @JsonProperty(JSON_PROPERTY_CLIENT_USERNAMES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setClientUsernamesUri(String clientUsernamesUri) {
    this.clientUsernamesUri = clientUsernamesUri;
  }


  public MsgVpnLinks distributedCachesUri(String distributedCachesUri) {
    
    this.distributedCachesUri = distributedCachesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Distributed Cache objects. Available since 2.11.
   * @return distributedCachesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DISTRIBUTED_CACHES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDistributedCachesUri() {
    return distributedCachesUri;
  }


  @JsonProperty(JSON_PROPERTY_DISTRIBUTED_CACHES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDistributedCachesUri(String distributedCachesUri) {
    this.distributedCachesUri = distributedCachesUri;
  }


  public MsgVpnLinks dmrBridgesUri(String dmrBridgesUri) {
    
    this.dmrBridgesUri = dmrBridgesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of DMR Bridge objects. Available since 2.11.
   * @return dmrBridgesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DMR_BRIDGES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getDmrBridgesUri() {
    return dmrBridgesUri;
  }


  @JsonProperty(JSON_PROPERTY_DMR_BRIDGES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDmrBridgesUri(String dmrBridgesUri) {
    this.dmrBridgesUri = dmrBridgesUri;
  }


  public MsgVpnLinks jndiConnectionFactoriesUri(String jndiConnectionFactoriesUri) {
    
    this.jndiConnectionFactoriesUri = jndiConnectionFactoriesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of JNDI Connection Factory objects. Available since 2.2.
   * @return jndiConnectionFactoriesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_JNDI_CONNECTION_FACTORIES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getJndiConnectionFactoriesUri() {
    return jndiConnectionFactoriesUri;
  }


  @JsonProperty(JSON_PROPERTY_JNDI_CONNECTION_FACTORIES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJndiConnectionFactoriesUri(String jndiConnectionFactoriesUri) {
    this.jndiConnectionFactoriesUri = jndiConnectionFactoriesUri;
  }


  public MsgVpnLinks jndiQueuesUri(String jndiQueuesUri) {
    
    this.jndiQueuesUri = jndiQueuesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of JNDI Queue objects. Available since 2.2.
   * @return jndiQueuesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_JNDI_QUEUES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getJndiQueuesUri() {
    return jndiQueuesUri;
  }


  @JsonProperty(JSON_PROPERTY_JNDI_QUEUES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJndiQueuesUri(String jndiQueuesUri) {
    this.jndiQueuesUri = jndiQueuesUri;
  }


  public MsgVpnLinks jndiTopicsUri(String jndiTopicsUri) {
    
    this.jndiTopicsUri = jndiTopicsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of JNDI Topic objects. Available since 2.2.
   * @return jndiTopicsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_JNDI_TOPICS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getJndiTopicsUri() {
    return jndiTopicsUri;
  }


  @JsonProperty(JSON_PROPERTY_JNDI_TOPICS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setJndiTopicsUri(String jndiTopicsUri) {
    this.jndiTopicsUri = jndiTopicsUri;
  }


  public MsgVpnLinks mqttRetainCachesUri(String mqttRetainCachesUri) {
    
    this.mqttRetainCachesUri = mqttRetainCachesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of MQTT Retain Cache objects. Available since 2.11.
   * @return mqttRetainCachesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MQTT_RETAIN_CACHES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMqttRetainCachesUri() {
    return mqttRetainCachesUri;
  }


  @JsonProperty(JSON_PROPERTY_MQTT_RETAIN_CACHES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMqttRetainCachesUri(String mqttRetainCachesUri) {
    this.mqttRetainCachesUri = mqttRetainCachesUri;
  }


  public MsgVpnLinks mqttSessionsUri(String mqttSessionsUri) {
    
    this.mqttSessionsUri = mqttSessionsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of MQTT Session objects. Available since 2.1.
   * @return mqttSessionsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_MQTT_SESSIONS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMqttSessionsUri() {
    return mqttSessionsUri;
  }


  @JsonProperty(JSON_PROPERTY_MQTT_SESSIONS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMqttSessionsUri(String mqttSessionsUri) {
    this.mqttSessionsUri = mqttSessionsUri;
  }


  public MsgVpnLinks queueTemplatesUri(String queueTemplatesUri) {
    
    this.queueTemplatesUri = queueTemplatesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Queue Template objects. Available since 2.14.
   * @return queueTemplatesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUE_TEMPLATES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getQueueTemplatesUri() {
    return queueTemplatesUri;
  }


  @JsonProperty(JSON_PROPERTY_QUEUE_TEMPLATES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueueTemplatesUri(String queueTemplatesUri) {
    this.queueTemplatesUri = queueTemplatesUri;
  }


  public MsgVpnLinks queuesUri(String queuesUri) {
    
    this.queuesUri = queuesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Queue objects.
   * @return queuesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_QUEUES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getQueuesUri() {
    return queuesUri;
  }


  @JsonProperty(JSON_PROPERTY_QUEUES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setQueuesUri(String queuesUri) {
    this.queuesUri = queuesUri;
  }


  public MsgVpnLinks replayLogsUri(String replayLogsUri) {
    
    this.replayLogsUri = replayLogsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Replay Log objects. Available since 2.10.
   * @return replayLogsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLAY_LOGS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplayLogsUri() {
    return replayLogsUri;
  }


  @JsonProperty(JSON_PROPERTY_REPLAY_LOGS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplayLogsUri(String replayLogsUri) {
    this.replayLogsUri = replayLogsUri;
  }


  public MsgVpnLinks replicatedTopicsUri(String replicatedTopicsUri) {
    
    this.replicatedTopicsUri = replicatedTopicsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Replicated Topic objects. Available since 2.1.
   * @return replicatedTopicsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REPLICATED_TOPICS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getReplicatedTopicsUri() {
    return replicatedTopicsUri;
  }


  @JsonProperty(JSON_PROPERTY_REPLICATED_TOPICS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setReplicatedTopicsUri(String replicatedTopicsUri) {
    this.replicatedTopicsUri = replicatedTopicsUri;
  }


  public MsgVpnLinks restDeliveryPointsUri(String restDeliveryPointsUri) {
    
    this.restDeliveryPointsUri = restDeliveryPointsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of REST Delivery Point objects.
   * @return restDeliveryPointsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_REST_DELIVERY_POINTS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getRestDeliveryPointsUri() {
    return restDeliveryPointsUri;
  }


  @JsonProperty(JSON_PROPERTY_REST_DELIVERY_POINTS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setRestDeliveryPointsUri(String restDeliveryPointsUri) {
    this.restDeliveryPointsUri = restDeliveryPointsUri;
  }


  public MsgVpnLinks sequencedTopicsUri(String sequencedTopicsUri) {
    
    this.sequencedTopicsUri = sequencedTopicsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Sequenced Topic objects.
   * @return sequencedTopicsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_SEQUENCED_TOPICS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getSequencedTopicsUri() {
    return sequencedTopicsUri;
  }


  @JsonProperty(JSON_PROPERTY_SEQUENCED_TOPICS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSequencedTopicsUri(String sequencedTopicsUri) {
    this.sequencedTopicsUri = sequencedTopicsUri;
  }


  public MsgVpnLinks topicEndpointTemplatesUri(String topicEndpointTemplatesUri) {
    
    this.topicEndpointTemplatesUri = topicEndpointTemplatesUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Topic Endpoint Template objects. Available since 2.14.
   * @return topicEndpointTemplatesUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOPIC_ENDPOINT_TEMPLATES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTopicEndpointTemplatesUri() {
    return topicEndpointTemplatesUri;
  }


  @JsonProperty(JSON_PROPERTY_TOPIC_ENDPOINT_TEMPLATES_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTopicEndpointTemplatesUri(String topicEndpointTemplatesUri) {
    this.topicEndpointTemplatesUri = topicEndpointTemplatesUri;
  }


  public MsgVpnLinks topicEndpointsUri(String topicEndpointsUri) {
    
    this.topicEndpointsUri = topicEndpointsUri;
    return this;
  }

   /**
   * The URI of this Message VPN&#39;s collection of Topic Endpoint objects. Available since 2.1.
   * @return topicEndpointsUri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOPIC_ENDPOINTS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getTopicEndpointsUri() {
    return topicEndpointsUri;
  }


  @JsonProperty(JSON_PROPERTY_TOPIC_ENDPOINTS_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTopicEndpointsUri(String topicEndpointsUri) {
    this.topicEndpointsUri = topicEndpointsUri;
  }


  public MsgVpnLinks uri(String uri) {
    
    this.uri = uri;
    return this;
  }

   /**
   * The URI of this Message VPN object.
   * @return uri
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getUri() {
    return uri;
  }


  @JsonProperty(JSON_PROPERTY_URI)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUri(String uri) {
    this.uri = uri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnLinks msgVpnLinks = (MsgVpnLinks) o;
    return Objects.equals(this.aclProfilesUri, msgVpnLinks.aclProfilesUri) &&
        Objects.equals(this.authenticationOauthProfilesUri, msgVpnLinks.authenticationOauthProfilesUri) &&
        Objects.equals(this.authenticationOauthProvidersUri, msgVpnLinks.authenticationOauthProvidersUri) &&
        Objects.equals(this.authorizationGroupsUri, msgVpnLinks.authorizationGroupsUri) &&
        Objects.equals(this.bridgesUri, msgVpnLinks.bridgesUri) &&
        Objects.equals(this.certMatchingRulesUri, msgVpnLinks.certMatchingRulesUri) &&
        Objects.equals(this.clientProfilesUri, msgVpnLinks.clientProfilesUri) &&
        Objects.equals(this.clientUsernamesUri, msgVpnLinks.clientUsernamesUri) &&
        Objects.equals(this.distributedCachesUri, msgVpnLinks.distributedCachesUri) &&
        Objects.equals(this.dmrBridgesUri, msgVpnLinks.dmrBridgesUri) &&
        Objects.equals(this.jndiConnectionFactoriesUri, msgVpnLinks.jndiConnectionFactoriesUri) &&
        Objects.equals(this.jndiQueuesUri, msgVpnLinks.jndiQueuesUri) &&
        Objects.equals(this.jndiTopicsUri, msgVpnLinks.jndiTopicsUri) &&
        Objects.equals(this.mqttRetainCachesUri, msgVpnLinks.mqttRetainCachesUri) &&
        Objects.equals(this.mqttSessionsUri, msgVpnLinks.mqttSessionsUri) &&
        Objects.equals(this.queueTemplatesUri, msgVpnLinks.queueTemplatesUri) &&
        Objects.equals(this.queuesUri, msgVpnLinks.queuesUri) &&
        Objects.equals(this.replayLogsUri, msgVpnLinks.replayLogsUri) &&
        Objects.equals(this.replicatedTopicsUri, msgVpnLinks.replicatedTopicsUri) &&
        Objects.equals(this.restDeliveryPointsUri, msgVpnLinks.restDeliveryPointsUri) &&
        Objects.equals(this.sequencedTopicsUri, msgVpnLinks.sequencedTopicsUri) &&
        Objects.equals(this.topicEndpointTemplatesUri, msgVpnLinks.topicEndpointTemplatesUri) &&
        Objects.equals(this.topicEndpointsUri, msgVpnLinks.topicEndpointsUri) &&
        Objects.equals(this.uri, msgVpnLinks.uri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aclProfilesUri, authenticationOauthProfilesUri, authenticationOauthProvidersUri, authorizationGroupsUri, bridgesUri, certMatchingRulesUri, clientProfilesUri, clientUsernamesUri, distributedCachesUri, dmrBridgesUri, jndiConnectionFactoriesUri, jndiQueuesUri, jndiTopicsUri, mqttRetainCachesUri, mqttSessionsUri, queueTemplatesUri, queuesUri, replayLogsUri, replicatedTopicsUri, restDeliveryPointsUri, sequencedTopicsUri, topicEndpointTemplatesUri, topicEndpointsUri, uri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnLinks {\n");
    sb.append("    aclProfilesUri: ").append(toIndentedString(aclProfilesUri)).append("\n");
    sb.append("    authenticationOauthProfilesUri: ").append(toIndentedString(authenticationOauthProfilesUri)).append("\n");
    sb.append("    authenticationOauthProvidersUri: ").append(toIndentedString(authenticationOauthProvidersUri)).append("\n");
    sb.append("    authorizationGroupsUri: ").append(toIndentedString(authorizationGroupsUri)).append("\n");
    sb.append("    bridgesUri: ").append(toIndentedString(bridgesUri)).append("\n");
    sb.append("    certMatchingRulesUri: ").append(toIndentedString(certMatchingRulesUri)).append("\n");
    sb.append("    clientProfilesUri: ").append(toIndentedString(clientProfilesUri)).append("\n");
    sb.append("    clientUsernamesUri: ").append(toIndentedString(clientUsernamesUri)).append("\n");
    sb.append("    distributedCachesUri: ").append(toIndentedString(distributedCachesUri)).append("\n");
    sb.append("    dmrBridgesUri: ").append(toIndentedString(dmrBridgesUri)).append("\n");
    sb.append("    jndiConnectionFactoriesUri: ").append(toIndentedString(jndiConnectionFactoriesUri)).append("\n");
    sb.append("    jndiQueuesUri: ").append(toIndentedString(jndiQueuesUri)).append("\n");
    sb.append("    jndiTopicsUri: ").append(toIndentedString(jndiTopicsUri)).append("\n");
    sb.append("    mqttRetainCachesUri: ").append(toIndentedString(mqttRetainCachesUri)).append("\n");
    sb.append("    mqttSessionsUri: ").append(toIndentedString(mqttSessionsUri)).append("\n");
    sb.append("    queueTemplatesUri: ").append(toIndentedString(queueTemplatesUri)).append("\n");
    sb.append("    queuesUri: ").append(toIndentedString(queuesUri)).append("\n");
    sb.append("    replayLogsUri: ").append(toIndentedString(replayLogsUri)).append("\n");
    sb.append("    replicatedTopicsUri: ").append(toIndentedString(replicatedTopicsUri)).append("\n");
    sb.append("    restDeliveryPointsUri: ").append(toIndentedString(restDeliveryPointsUri)).append("\n");
    sb.append("    sequencedTopicsUri: ").append(toIndentedString(sequencedTopicsUri)).append("\n");
    sb.append("    topicEndpointTemplatesUri: ").append(toIndentedString(topicEndpointTemplatesUri)).append("\n");
    sb.append("    topicEndpointsUri: ").append(toIndentedString(topicEndpointsUri)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
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

