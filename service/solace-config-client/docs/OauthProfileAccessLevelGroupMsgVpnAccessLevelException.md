

# OauthProfileAccessLevelGroupMsgVpnAccessLevelException


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**accessLevel** | [**AccessLevelEnum**](#AccessLevelEnum) | The message VPN access level. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to a Message VPN. \&quot;read-only\&quot; - User has read-only access to a Message VPN. \&quot;read-write\&quot; - User has read-write access to most Message VPN settings. &lt;/pre&gt;  |  [optional] |
|**groupName** | **String** | The name of the group. |  [optional] |
|**msgVpnName** | **String** | The name of the message VPN. |  [optional] |
|**oauthProfileName** | **String** | The name of the OAuth profile. |  [optional] |



## Enum: AccessLevelEnum

| Name | Value |
|---- | -----|
| NONE | &quot;none&quot; |
| READ_ONLY | &quot;read-only&quot; |
| READ_WRITE | &quot;read-write&quot; |



