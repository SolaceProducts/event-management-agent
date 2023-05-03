

# MsgVpnAclProfilePublishTopicException


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**aclProfileName** | **String** | The name of the ACL Profile. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**publishTopicException** | **String** | The topic for the exception to the default action taken. May include wildcard characters. |  [optional] |
|**publishTopicExceptionSyntax** | [**PublishTopicExceptionSyntaxEnum**](#PublishTopicExceptionSyntaxEnum) | The syntax of the topic for the exception to the default action taken. The allowed values and their meaning are:  &lt;pre&gt; \&quot;smf\&quot; - Topic uses SMF syntax. \&quot;mqtt\&quot; - Topic uses MQTT syntax. &lt;/pre&gt;  |  [optional] |



## Enum: PublishTopicExceptionSyntaxEnum

| Name | Value |
|---- | -----|
| SMF | &quot;smf&quot; |
| MQTT | &quot;mqtt&quot; |



