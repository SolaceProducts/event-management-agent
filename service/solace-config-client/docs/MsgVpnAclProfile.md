

# MsgVpnAclProfile


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**aclProfileName** | **String** | The name of the ACL Profile. |  [optional] |
|**clientConnectDefaultAction** | [**ClientConnectDefaultActionEnum**](#ClientConnectDefaultActionEnum) | The default action to take when a client using the ACL Profile connects to the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;disallow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow client connection unless an exception is found for it. \&quot;disallow\&quot; - Disallow client connection unless an exception is found for it. &lt;/pre&gt;  |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**publishTopicDefaultAction** | [**PublishTopicDefaultActionEnum**](#PublishTopicDefaultActionEnum) | The default action to take when a client using the ACL Profile publishes to a topic in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;disallow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow topic unless an exception is found for it. \&quot;disallow\&quot; - Disallow topic unless an exception is found for it. &lt;/pre&gt;  |  [optional] |
|**subscribeShareNameDefaultAction** | [**SubscribeShareNameDefaultActionEnum**](#SubscribeShareNameDefaultActionEnum) | The default action to take when a client using the ACL Profile subscribes to a share-name subscription in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;allow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow topic unless an exception is found for it. \&quot;disallow\&quot; - Disallow topic unless an exception is found for it. &lt;/pre&gt;  Available since 2.14. |  [optional] |
|**subscribeTopicDefaultAction** | [**SubscribeTopicDefaultActionEnum**](#SubscribeTopicDefaultActionEnum) | The default action to take when a client using the ACL Profile subscribes to a topic in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;disallow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow topic unless an exception is found for it. \&quot;disallow\&quot; - Disallow topic unless an exception is found for it. &lt;/pre&gt;  |  [optional] |



## Enum: ClientConnectDefaultActionEnum

| Name | Value |
|---- | -----|
| ALLOW | &quot;allow&quot; |
| DISALLOW | &quot;disallow&quot; |



## Enum: PublishTopicDefaultActionEnum

| Name | Value |
|---- | -----|
| ALLOW | &quot;allow&quot; |
| DISALLOW | &quot;disallow&quot; |



## Enum: SubscribeShareNameDefaultActionEnum

| Name | Value |
|---- | -----|
| ALLOW | &quot;allow&quot; |
| DISALLOW | &quot;disallow&quot; |



## Enum: SubscribeTopicDefaultActionEnum

| Name | Value |
|---- | -----|
| ALLOW | &quot;allow&quot; |
| DISALLOW | &quot;disallow&quot; |



