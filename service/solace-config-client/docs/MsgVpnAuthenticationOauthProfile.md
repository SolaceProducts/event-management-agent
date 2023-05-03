

# MsgVpnAuthenticationOauthProfile


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**authorizationGroupsClaimName** | **String** | The name of the groups claim. If non-empty, the specified claim will be used to determine groups for authorization. If empty, the authorizationType attribute of the Message VPN will be used to determine authorization. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;groups\&quot;&#x60;. |  [optional] |
|**clientId** | **String** | The OAuth client id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**clientRequiredType** | **String** | The required value for the TYP field in the ID token header. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;JWT\&quot;&#x60;. |  [optional] |
|**clientSecret** | **String** | The OAuth client secret. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**clientValidateTypeEnabled** | **Boolean** | Enable or disable verification of the TYP field in the ID token header. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**disconnectOnTokenExpirationEnabled** | **Boolean** | Enable or disable the disconnection of clients when their tokens expire. Changing this value does not affect existing clients, only new client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**enabled** | **Boolean** | Enable or disable the OAuth profile. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**endpointDiscovery** | **String** | The OpenID Connect discovery endpoint or OAuth Authorization Server Metadata endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**endpointDiscoveryRefreshInterval** | **Integer** | The number of seconds between discovery endpoint requests. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;86400&#x60;. |  [optional] |
|**endpointIntrospection** | **String** | The OAuth introspection endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**endpointIntrospectionTimeout** | **Integer** | The maximum time in seconds a token introspection request is allowed to take. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;. |  [optional] |
|**endpointJwks** | **String** | The OAuth JWKS endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**endpointJwksRefreshInterval** | **Integer** | The number of seconds between JWKS endpoint requests. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;86400&#x60;. |  [optional] |
|**endpointUserinfo** | **String** | The OpenID Connect Userinfo endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**endpointUserinfoTimeout** | **Integer** | The maximum time in seconds a userinfo request is allowed to take. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;. |  [optional] |
|**issuer** | **String** | The Issuer Identifier for the OAuth provider. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**mqttUsernameValidateEnabled** | **Boolean** | Enable or disable whether the API provided MQTT client username will be validated against the username calculated from the token(s). When enabled, connection attempts by MQTT clients are rejected if they differ. Note that this value only applies to MQTT clients; SMF client usernames will not be validated. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**oauthProfileName** | **String** | The name of the OAuth profile. |  [optional] |
|**oauthRole** | [**OauthRoleEnum**](#OauthRoleEnum) | The OAuth role of the broker. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;client\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;client\&quot; - The broker is in the OAuth client role. \&quot;resource-server\&quot; - The broker is in the OAuth resource server role. &lt;/pre&gt;  |  [optional] |
|**resourceServerParseAccessTokenEnabled** | **Boolean** | Enable or disable parsing of the access token as a JWT. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**resourceServerRequiredAudience** | **String** | The required audience value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**resourceServerRequiredIssuer** | **String** | The required issuer value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**resourceServerRequiredScope** | **String** | A space-separated list of scopes that must be present in the scope claim. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**resourceServerRequiredType** | **String** | The required TYP value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;at+jwt\&quot;&#x60;. |  [optional] |
|**resourceServerValidateAudienceEnabled** | **Boolean** | Enable or disable verification of the audience claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**resourceServerValidateIssuerEnabled** | **Boolean** | Enable or disable verification of the issuer claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**resourceServerValidateScopeEnabled** | **Boolean** | Enable or disable verification of the scope claim in the access token or introspection response. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**resourceServerValidateTypeEnabled** | **Boolean** | Enable or disable verification of the TYP field in the access token header. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**usernameClaimName** | **String** | The name of the username claim. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;sub\&quot;&#x60;. |  [optional] |



## Enum: OauthRoleEnum

| Name | Value |
|---- | -----|
| CLIENT | &quot;client&quot; |
| RESOURCE_SERVER | &quot;resource-server&quot; |



