

# MsgVpnAclProfileSubscribeTopicException


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**aclProfileName** | **String** | The name of the ACL Profile. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**subscribeTopicException** | **String** | The topic for the exception to the default action taken. May include wildcard characters. |  [optional] |
|**subscribeTopicExceptionSyntax** | [**SubscribeTopicExceptionSyntaxEnum**](#SubscribeTopicExceptionSyntaxEnum) | The syntax of the topic for the exception to the default action taken. The allowed values and their meaning are:  &lt;pre&gt; \&quot;smf\&quot; - Topic uses SMF syntax. \&quot;mqtt\&quot; - Topic uses MQTT syntax. &lt;/pre&gt;  |  [optional] |



## Enum: SubscribeTopicExceptionSyntaxEnum

| Name | Value |
|---- | -----|
| SMF | &quot;smf&quot; |
| MQTT | &quot;mqtt&quot; |



