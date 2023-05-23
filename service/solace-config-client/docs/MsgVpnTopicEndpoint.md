

# MsgVpnTopicEndpoint


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**accessType** | [**AccessTypeEnum**](#AccessTypeEnum) | The access type for delivering messages to consumer flows bound to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;exclusive\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;exclusive\&quot; - Exclusive delivery of messages to the first bound consumer flow. \&quot;non-exclusive\&quot; - Non-exclusive delivery of messages to all bound consumer flows in a round-robin fashion. &lt;/pre&gt;  Available since 2.4. |  [optional] |
|**consumerAckPropagationEnabled** | **Boolean** | Enable or disable the propagation of consumer acknowledgements (ACKs) received on the active replication Message VPN to the standby replication Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**deadMsgQueue** | **String** | The name of the Dead Message Queue (DMQ) used by the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. Available since 2.2. |  [optional] |
|**deliveryCountEnabled** | **Boolean** | Enable or disable the ability for client applications to query the message delivery count of messages received from the Topic Endpoint. This is a controlled availability feature. Please contact support to find out if this feature is supported for your use case. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.19. |  [optional] |
|**deliveryDelay** | **Long** | The delay, in seconds, to apply to messages arriving on the Topic Endpoint before the messages are eligible for delivery. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.22. |  [optional] |
|**egressEnabled** | **Boolean** | Enable or disable the transmission of messages from the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**eventBindCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional] |
|**eventRejectLowPriorityMsgLimitThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional] |
|**eventSpoolUsageThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional] |
|**ingressEnabled** | **Boolean** | Enable or disable the reception of messages to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**maxBindCount** | **Long** | The maximum number of consumer flows that can bind to the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1&#x60;. Available since 2.4. |  [optional] |
|**maxDeliveredUnackedMsgsPerFlow** | **Long** | The maximum number of messages delivered but not acknowledged per flow for the Topic Endpoint. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000&#x60;. |  [optional] |
|**maxMsgSize** | **Integer** | The maximum message size allowed in the Topic Endpoint, in bytes (B). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000000&#x60;. |  [optional] |
|**maxRedeliveryCount** | **Long** | The maximum number of times the Topic Endpoint will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. |  [optional] |
|**maxSpoolUsage** | **Long** | The maximum message spool usage allowed by the Topic Endpoint, in megabytes (MB). A value of 0 only allows spooling of the last message received and disables quota checking. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;5000&#x60;. |  [optional] |
|**maxTtl** | **Long** | The maximum time in seconds a message can stay in the Topic Endpoint when &#x60;respectTtlEnabled&#x60; is &#x60;\&quot;true\&quot;&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;maxTtl&#x60; configured for the Topic Endpoint, is exceeded. A value of 0 disables expiry. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**owner** | **String** | The Client Username that owns the Topic Endpoint and has permission equivalent to &#x60;\&quot;delete\&quot;&#x60;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**permission** | [**PermissionEnum**](#PermissionEnum) | The permission level for all consumers of the Topic Endpoint, excluding the owner. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;no-access\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;no-access\&quot; - Disallows all access. \&quot;read-only\&quot; - Read-only access to the messages. \&quot;consume\&quot; - Consume (read and remove) messages. \&quot;modify-topic\&quot; - Consume messages or modify the topic/selector. \&quot;delete\&quot; - Consume messages, modify the topic/selector or delete the Client created endpoint altogether. &lt;/pre&gt;  |  [optional] |
|**redeliveryEnabled** | **Boolean** | Enable or disable message redelivery. When enabled, the number of redelivery attempts is controlled by maxRedeliveryCount. When disabled, the message will never be delivered from the topic-endpoint more than once. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.18. |  [optional] |
|**rejectLowPriorityMsgEnabled** | **Boolean** | Enable or disable the checking of low priority messages against the &#x60;rejectLowPriorityMsgLimit&#x60;. This may only be enabled if &#x60;rejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**rejectLowPriorityMsgLimit** | **Long** | The number of messages of any priority in the Topic Endpoint above which low priority messages are not admitted but higher priority messages are allowed. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. |  [optional] |
|**rejectMsgToSenderOnDiscardBehavior** | [**RejectMsgToSenderOnDiscardBehaviorEnum**](#RejectMsgToSenderOnDiscardBehaviorEnum) | Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;never\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-topic-endpoint-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Topic Endpoint is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt;  |  [optional] |
|**respectMsgPriorityEnabled** | **Boolean** | Enable or disable the respecting of message priority. When enabled, messages contained in the Topic Endpoint are delivered in priority order, from 9 (highest) to 0 (lowest). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.8. |  [optional] |
|**respectTtlEnabled** | **Boolean** | Enable or disable the respecting of the time-to-live (TTL) for messages in the Topic Endpoint. When enabled, expired messages are discarded or moved to the DMQ. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**topicEndpointName** | **String** | The name of the Topic Endpoint. |  [optional] |



## Enum: AccessTypeEnum

| Name | Value |
|---- | -----|
| EXCLUSIVE | &quot;exclusive&quot; |
| NON_EXCLUSIVE | &quot;non-exclusive&quot; |



## Enum: PermissionEnum

| Name | Value |
|---- | -----|
| NO_ACCESS | &quot;no-access&quot; |
| READ_ONLY | &quot;read-only&quot; |
| CONSUME | &quot;consume&quot; |
| MODIFY_TOPIC | &quot;modify-topic&quot; |
| DELETE | &quot;delete&quot; |



## Enum: RejectMsgToSenderOnDiscardBehaviorEnum

| Name | Value |
|---- | -----|
| ALWAYS | &quot;always&quot; |
| WHEN_TOPIC_ENDPOINT_ENABLED | &quot;when-topic-endpoint-enabled&quot; |
| NEVER | &quot;never&quot; |



