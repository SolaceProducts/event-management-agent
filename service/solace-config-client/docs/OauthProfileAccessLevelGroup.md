

# OauthProfileAccessLevelGroup


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | A description for the group. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**globalAccessLevel** | [**GlobalAccessLevelEnum**](#GlobalAccessLevelEnum) | The global access level for this group. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to global data. \&quot;read-only\&quot; - User has read-only access to global data. \&quot;read-write\&quot; - User has read-write access to most global data. \&quot;admin\&quot; - User has read-write access to all global data. &lt;/pre&gt;  |  [optional] |
|**groupName** | **String** | The name of the group. |  [optional] |
|**msgVpnAccessLevel** | [**MsgVpnAccessLevelEnum**](#MsgVpnAccessLevelEnum) | The default message VPN access level for this group. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to a Message VPN. \&quot;read-only\&quot; - User has read-only access to a Message VPN. \&quot;read-write\&quot; - User has read-write access to most Message VPN settings. &lt;/pre&gt;  |  [optional] |
|**oauthProfileName** | **String** | The name of the OAuth profile. |  [optional] |



## Enum: GlobalAccessLevelEnum

| Name | Value |
|---- | -----|
| NONE | &quot;none&quot; |
| READ_ONLY | &quot;read-only&quot; |
| READ_WRITE | &quot;read-write&quot; |
| ADMIN | &quot;admin&quot; |



## Enum: MsgVpnAccessLevelEnum

| Name | Value |
|---- | -----|
| NONE | &quot;none&quot; |
| READ_ONLY | &quot;read-only&quot; |
| READ_WRITE | &quot;read-write&quot; |



