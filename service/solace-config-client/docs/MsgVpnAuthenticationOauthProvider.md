

# MsgVpnAuthenticationOauthProvider


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**audienceClaimName** | **String** | The audience claim name, indicating which part of the object to use for determining the audience. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;aud\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**audienceClaimSource** | [**AudienceClaimSourceEnum**](#AudienceClaimSourceEnum) | The audience claim source, indicating where to search for the audience value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**audienceClaimValue** | **String** | The required audience value for a token to be considered valid. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**audienceValidationEnabled** | **Boolean** | Enable or disable audience validation. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**authorizationGroupClaimName** | **String** | The authorization group claim name, indicating which part of the object to use for determining the authorization group. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;scope\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**authorizationGroupClaimSource** | [**AuthorizationGroupClaimSourceEnum**](#AuthorizationGroupClaimSourceEnum) | The authorization group claim source, indicating where to search for the authorization group name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**authorizationGroupEnabled** | **Boolean** | Enable or disable OAuth based authorization. When enabled, the configured authorization type for OAuth clients is overridden. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**disconnectOnTokenExpirationEnabled** | **Boolean** | Enable or disable the disconnection of clients when their tokens expire. Changing this value does not affect existing clients, only new client connections. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**enabled** | **Boolean** | Enable or disable OAuth Provider client authentication. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**jwksRefreshInterval** | **Integer** | The number of seconds between forced JWKS public key refreshing. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;86400&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**jwksUri** | **String** | The URI where the OAuth provider publishes its JWKS public keys. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. Deprecated since 2.25. Replaced by authenticationOauthProfiles. |  [optional] |
|**oauthProviderName** | **String** | The name of the OAuth Provider. Deprecated since 2.25. Replaced by authenticationOauthProfiles. |  [optional] |
|**tokenIgnoreTimeLimitsEnabled** | **Boolean** | Enable or disable whether to ignore time limits and accept tokens that are not yet valid or are no longer valid. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**tokenIntrospectionParameterName** | **String** | The parameter name used to identify the token during access token introspection. A standards compliant OAuth introspection server expects \&quot;token\&quot;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;token\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**tokenIntrospectionPassword** | **String** | The password to use when logging into the token introspection URI. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**tokenIntrospectionTimeout** | **Integer** | The maximum time in seconds a token introspection is allowed to take. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**tokenIntrospectionUri** | **String** | The token introspection URI of the OAuth authentication server. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**tokenIntrospectionUsername** | **String** | The username to use when logging into the token introspection URI. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**usernameClaimName** | **String** | The username claim name, indicating which part of the object to use for determining the username. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;sub\&quot;&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**usernameClaimSource** | [**UsernameClaimSourceEnum**](#UsernameClaimSourceEnum) | The username claim source, indicating where to search for the username value. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;id-token\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;access-token\&quot; - The OAuth v2 access_token. \&quot;id-token\&quot; - The OpenID Connect id_token. \&quot;introspection\&quot; - The result of introspecting the OAuth v2 access_token. &lt;/pre&gt;  Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |
|**usernameValidateEnabled** | **Boolean** | Enable or disable whether the API provided username will be validated against the username calculated from the token(s); the connection attempt is rejected if they differ. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.25. authenticationOauthProviders replaced by authenticationOauthProfiles. |  [optional] |



## Enum: AudienceClaimSourceEnum

| Name | Value |
|---- | -----|
| ACCESS_TOKEN | &quot;access-token&quot; |
| ID_TOKEN | &quot;id-token&quot; |
| INTROSPECTION | &quot;introspection&quot; |



## Enum: AuthorizationGroupClaimSourceEnum

| Name | Value |
|---- | -----|
| ACCESS_TOKEN | &quot;access-token&quot; |
| ID_TOKEN | &quot;id-token&quot; |
| INTROSPECTION | &quot;introspection&quot; |



## Enum: UsernameClaimSourceEnum

| Name | Value |
|---- | -----|
| ACCESS_TOKEN | &quot;access-token&quot; |
| ID_TOKEN | &quot;id-token&quot; |
| INTROSPECTION | &quot;introspection&quot; |



