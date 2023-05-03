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
 * DmrCluster
 */
@JsonPropertyOrder({
  DmrCluster.JSON_PROPERTY_AUTHENTICATION_BASIC_ENABLED,
  DmrCluster.JSON_PROPERTY_AUTHENTICATION_BASIC_PASSWORD,
  DmrCluster.JSON_PROPERTY_AUTHENTICATION_BASIC_TYPE,
  DmrCluster.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CONTENT,
  DmrCluster.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ENABLED,
  DmrCluster.JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_PASSWORD,
  DmrCluster.JSON_PROPERTY_DIRECT_ONLY_ENABLED,
  DmrCluster.JSON_PROPERTY_DMR_CLUSTER_NAME,
  DmrCluster.JSON_PROPERTY_ENABLED,
  DmrCluster.JSON_PROPERTY_NODE_NAME,
  DmrCluster.JSON_PROPERTY_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED,
  DmrCluster.JSON_PROPERTY_TLS_SERVER_CERT_MAX_CHAIN_DEPTH,
  DmrCluster.JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED,
  DmrCluster.JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-04-25T11:27:30.946889+01:00[Europe/London]")
public class DmrCluster {
  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_ENABLED = "authenticationBasicEnabled";
  private Boolean authenticationBasicEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_BASIC_PASSWORD = "authenticationBasicPassword";
  private String authenticationBasicPassword;

  /**
   * The type of basic authentication to use for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;internal\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Use locally configured password. \&quot;none\&quot; - No authentication. &lt;/pre&gt; 
   */
  public enum AuthenticationBasicTypeEnum {
    INTERNAL("internal"),
    
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

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_CONTENT = "authenticationClientCertContent";
  private String authenticationClientCertContent;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_ENABLED = "authenticationClientCertEnabled";
  private Boolean authenticationClientCertEnabled;

  public static final String JSON_PROPERTY_AUTHENTICATION_CLIENT_CERT_PASSWORD = "authenticationClientCertPassword";
  private String authenticationClientCertPassword;

  public static final String JSON_PROPERTY_DIRECT_ONLY_ENABLED = "directOnlyEnabled";
  private Boolean directOnlyEnabled;

  public static final String JSON_PROPERTY_DMR_CLUSTER_NAME = "dmrClusterName";
  private String dmrClusterName;

  public static final String JSON_PROPERTY_ENABLED = "enabled";
  private Boolean enabled;

  public static final String JSON_PROPERTY_NODE_NAME = "nodeName";
  private String nodeName;

  public static final String JSON_PROPERTY_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED = "tlsServerCertEnforceTrustedCommonNameEnabled";
  private Boolean tlsServerCertEnforceTrustedCommonNameEnabled;

  public static final String JSON_PROPERTY_TLS_SERVER_CERT_MAX_CHAIN_DEPTH = "tlsServerCertMaxChainDepth";
  private Long tlsServerCertMaxChainDepth;

  public static final String JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED = "tlsServerCertValidateDateEnabled";
  private Boolean tlsServerCertValidateDateEnabled;

  public static final String JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED = "tlsServerCertValidateNameEnabled";
  private Boolean tlsServerCertValidateNameEnabled;

  public DmrCluster() {
  }

  public DmrCluster authenticationBasicEnabled(Boolean authenticationBasicEnabled) {
    
    this.authenticationBasicEnabled = authenticationBasicEnabled;
    return this;
  }

   /**
   * Enable or disable basic authentication for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public DmrCluster authenticationBasicPassword(String authenticationBasicPassword) {
    
    this.authenticationBasicPassword = authenticationBasicPassword;
    return this;
  }

   /**
   * The password used to authenticate incoming Cluster Links when using basic internal authentication. The same password is also used by outgoing Cluster Links if a per-Link password is not configured. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public DmrCluster authenticationBasicType(AuthenticationBasicTypeEnum authenticationBasicType) {
    
    this.authenticationBasicType = authenticationBasicType;
    return this;
  }

   /**
   * The type of basic authentication to use for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;internal\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Use locally configured password. \&quot;none\&quot; - No authentication. &lt;/pre&gt; 
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


  public DmrCluster authenticationClientCertContent(String authenticationClientCertContent) {
    
    this.authenticationClientCertContent = authenticationClientCertContent;
    return this;
  }

   /**
   * The PEM formatted content for the client certificate used to login to the remote node. It must consist of a private key and between one and three certificates comprising the certificate trust chain. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public DmrCluster authenticationClientCertEnabled(Boolean authenticationClientCertEnabled) {
    
    this.authenticationClientCertEnabled = authenticationClientCertEnabled;
    return this;
  }

   /**
   * Enable or disable client certificate authentication for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
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


  public DmrCluster authenticationClientCertPassword(String authenticationClientCertPassword) {
    
    this.authenticationClientCertPassword = authenticationClientCertPassword;
    return this;
  }

   /**
   * The password for the client certificate. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;.
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


  public DmrCluster directOnlyEnabled(Boolean directOnlyEnabled) {
    
    this.directOnlyEnabled = directOnlyEnabled;
    return this;
  }

   /**
   * Enable or disable direct messaging only. Guaranteed messages will not be transmitted through the cluster. The default value is &#x60;false&#x60;.
   * @return directOnlyEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DIRECT_ONLY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getDirectOnlyEnabled() {
    return directOnlyEnabled;
  }


  @JsonProperty(JSON_PROPERTY_DIRECT_ONLY_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDirectOnlyEnabled(Boolean directOnlyEnabled) {
    this.directOnlyEnabled = directOnlyEnabled;
  }


  public DmrCluster dmrClusterName(String dmrClusterName) {
    
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


  public DmrCluster enabled(Boolean enabled) {
    
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Cluster. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;.
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


  public DmrCluster nodeName(String nodeName) {
    
    this.nodeName = nodeName;
    return this;
  }

   /**
   * The name of this node in the Cluster. This is the name that this broker (or redundant group of brokers) is know by to other nodes in the Cluster. The name is chosen automatically to be either this broker&#39;s Router Name or Mate Router Name, depending on which Active Standby Role (primary or backup) this broker plays in its redundancy group.
   * @return nodeName
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NODE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getNodeName() {
    return nodeName;
  }


  @JsonProperty(JSON_PROPERTY_NODE_NAME)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }


  public DmrCluster tlsServerCertEnforceTrustedCommonNameEnabled(Boolean tlsServerCertEnforceTrustedCommonNameEnabled) {
    
    this.tlsServerCertEnforceTrustedCommonNameEnabled = tlsServerCertEnforceTrustedCommonNameEnabled;
    return this;
  }

   /**
   * Enable or disable the enforcing of the common name provided by the remote broker against the list of trusted common names configured for the Link. If enabled, the certificate&#39;s common name must match one of the trusted common names for the Link to be accepted. Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is enabled. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation.
   * @return tlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsServerCertEnforceTrustedCommonNameEnabled() {
    return tlsServerCertEnforceTrustedCommonNameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_ENFORCE_TRUSTED_COMMON_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsServerCertEnforceTrustedCommonNameEnabled(Boolean tlsServerCertEnforceTrustedCommonNameEnabled) {
    this.tlsServerCertEnforceTrustedCommonNameEnabled = tlsServerCertEnforceTrustedCommonNameEnabled;
  }


  public DmrCluster tlsServerCertMaxChainDepth(Long tlsServerCertMaxChainDepth) {
    
    this.tlsServerCertMaxChainDepth = tlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum allowed depth of a certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;3&#x60;.
   * @return tlsServerCertMaxChainDepth
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getTlsServerCertMaxChainDepth() {
    return tlsServerCertMaxChainDepth;
  }


  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_MAX_CHAIN_DEPTH)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsServerCertMaxChainDepth(Long tlsServerCertMaxChainDepth) {
    this.tlsServerCertMaxChainDepth = tlsServerCertMaxChainDepth;
  }


  public DmrCluster tlsServerCertValidateDateEnabled(Boolean tlsServerCertValidateDateEnabled) {
    
    this.tlsServerCertValidateDateEnabled = tlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable the validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the certificate. When disabled, the certificate is accepted even if the certificate is not valid based on these dates. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;.
   * @return tlsServerCertValidateDateEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsServerCertValidateDateEnabled() {
    return tlsServerCertValidateDateEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_DATE_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsServerCertValidateDateEnabled(Boolean tlsServerCertValidateDateEnabled) {
    this.tlsServerCertValidateDateEnabled = tlsServerCertValidateDateEnabled;
  }


  public DmrCluster tlsServerCertValidateNameEnabled(Boolean tlsServerCertValidateNameEnabled) {
    
    this.tlsServerCertValidateNameEnabled = tlsServerCertValidateNameEnabled;
    return this;
  }

   /**
   * Enable or disable the standard TLS authentication mechanism of verifying the name used to connect to the bridge. If enabled, the name used to connect to the bridge is checked against the names specified in the certificate returned by the remote router. Legacy Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is also enabled. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.18.
   * @return tlsServerCertValidateNameEnabled
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getTlsServerCertValidateNameEnabled() {
    return tlsServerCertValidateNameEnabled;
  }


  @JsonProperty(JSON_PROPERTY_TLS_SERVER_CERT_VALIDATE_NAME_ENABLED)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTlsServerCertValidateNameEnabled(Boolean tlsServerCertValidateNameEnabled) {
    this.tlsServerCertValidateNameEnabled = tlsServerCertValidateNameEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DmrCluster dmrCluster = (DmrCluster) o;
    return Objects.equals(this.authenticationBasicEnabled, dmrCluster.authenticationBasicEnabled) &&
        Objects.equals(this.authenticationBasicPassword, dmrCluster.authenticationBasicPassword) &&
        Objects.equals(this.authenticationBasicType, dmrCluster.authenticationBasicType) &&
        Objects.equals(this.authenticationClientCertContent, dmrCluster.authenticationClientCertContent) &&
        Objects.equals(this.authenticationClientCertEnabled, dmrCluster.authenticationClientCertEnabled) &&
        Objects.equals(this.authenticationClientCertPassword, dmrCluster.authenticationClientCertPassword) &&
        Objects.equals(this.directOnlyEnabled, dmrCluster.directOnlyEnabled) &&
        Objects.equals(this.dmrClusterName, dmrCluster.dmrClusterName) &&
        Objects.equals(this.enabled, dmrCluster.enabled) &&
        Objects.equals(this.nodeName, dmrCluster.nodeName) &&
        Objects.equals(this.tlsServerCertEnforceTrustedCommonNameEnabled, dmrCluster.tlsServerCertEnforceTrustedCommonNameEnabled) &&
        Objects.equals(this.tlsServerCertMaxChainDepth, dmrCluster.tlsServerCertMaxChainDepth) &&
        Objects.equals(this.tlsServerCertValidateDateEnabled, dmrCluster.tlsServerCertValidateDateEnabled) &&
        Objects.equals(this.tlsServerCertValidateNameEnabled, dmrCluster.tlsServerCertValidateNameEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationBasicEnabled, authenticationBasicPassword, authenticationBasicType, authenticationClientCertContent, authenticationClientCertEnabled, authenticationClientCertPassword, directOnlyEnabled, dmrClusterName, enabled, nodeName, tlsServerCertEnforceTrustedCommonNameEnabled, tlsServerCertMaxChainDepth, tlsServerCertValidateDateEnabled, tlsServerCertValidateNameEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DmrCluster {\n");
    sb.append("    authenticationBasicEnabled: ").append(toIndentedString(authenticationBasicEnabled)).append("\n");
    sb.append("    authenticationBasicPassword: ").append(toIndentedString(authenticationBasicPassword)).append("\n");
    sb.append("    authenticationBasicType: ").append(toIndentedString(authenticationBasicType)).append("\n");
    sb.append("    authenticationClientCertContent: ").append(toIndentedString(authenticationClientCertContent)).append("\n");
    sb.append("    authenticationClientCertEnabled: ").append(toIndentedString(authenticationClientCertEnabled)).append("\n");
    sb.append("    authenticationClientCertPassword: ").append(toIndentedString(authenticationClientCertPassword)).append("\n");
    sb.append("    directOnlyEnabled: ").append(toIndentedString(directOnlyEnabled)).append("\n");
    sb.append("    dmrClusterName: ").append(toIndentedString(dmrClusterName)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    nodeName: ").append(toIndentedString(nodeName)).append("\n");
    sb.append("    tlsServerCertEnforceTrustedCommonNameEnabled: ").append(toIndentedString(tlsServerCertEnforceTrustedCommonNameEnabled)).append("\n");
    sb.append("    tlsServerCertMaxChainDepth: ").append(toIndentedString(tlsServerCertMaxChainDepth)).append("\n");
    sb.append("    tlsServerCertValidateDateEnabled: ").append(toIndentedString(tlsServerCertValidateDateEnabled)).append("\n");
    sb.append("    tlsServerCertValidateNameEnabled: ").append(toIndentedString(tlsServerCertValidateNameEnabled)).append("\n");
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

