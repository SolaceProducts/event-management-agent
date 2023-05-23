

# MsgVpnAclProfileSubscribeShareNameException


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**aclProfileName** | **String** | The name of the ACL Profile. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**subscribeShareNameException** | **String** | The subscribe share name exception to the default action taken. May include wildcard characters. |  [optional] |
|**subscribeShareNameExceptionSyntax** | [**SubscribeShareNameExceptionSyntaxEnum**](#SubscribeShareNameExceptionSyntaxEnum) | The syntax of the subscribe share name for the exception to the default action taken. The allowed values and their meaning are:  &lt;pre&gt; \&quot;smf\&quot; - Topic uses SMF syntax. \&quot;mqtt\&quot; - Topic uses MQTT syntax. &lt;/pre&gt;  |  [optional] |



## Enum: SubscribeShareNameExceptionSyntaxEnum

| Name | Value |
|---- | -----|
| SMF | &quot;smf&quot; |
| MQTT | &quot;mqtt&quot; |



