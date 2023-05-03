

# MsgVpnAclProfileSubscribeException


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**aclProfileName** | **String** | The name of the ACL Profile. Deprecated since 2.14. Replaced by subscribeTopicExceptions. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. Deprecated since 2.14. Replaced by subscribeTopicExceptions. |  [optional] |
|**subscribeExceptionTopic** | **String** | The topic for the exception to the default action taken. May include wildcard characters. Deprecated since 2.14. Replaced by subscribeTopicExceptions. |  [optional] |
|**topicSyntax** | [**TopicSyntaxEnum**](#TopicSyntaxEnum) | The syntax of the topic for the exception to the default action taken. The allowed values and their meaning are:  &lt;pre&gt; \&quot;smf\&quot; - Topic uses SMF syntax. \&quot;mqtt\&quot; - Topic uses MQTT syntax. &lt;/pre&gt;  Deprecated since 2.14. Replaced by subscribeTopicExceptions. |  [optional] |



## Enum: TopicSyntaxEnum

| Name | Value |
|---- | -----|
| SMF | &quot;smf&quot; |
| MQTT | &quot;mqtt&quot; |



