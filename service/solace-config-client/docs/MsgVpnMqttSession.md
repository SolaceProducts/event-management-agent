

# MsgVpnMqttSession


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**enabled** | **Boolean** | Enable or disable the MQTT Session. When disabled, the client is disconnected, new messages matching QoS 0 subscriptions are discarded, and new messages matching QoS 1 subscriptions are stored for future delivery. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**mqttSessionClientId** | **String** | The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. |  [optional] |
|**mqttSessionVirtualRouter** | [**MqttSessionVirtualRouterEnum**](#MqttSessionVirtualRouterEnum) | The virtual router of the MQTT Session. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The MQTT Session belongs to the primary virtual router. \&quot;backup\&quot; - The MQTT Session belongs to the backup virtual router. \&quot;auto\&quot; - The MQTT Session is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt;  |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**owner** | **String** | The owner of the MQTT Session. For externally-created sessions this defaults to the Client Username of the connecting client. For management-created sessions this defaults to empty. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**queueConsumerAckPropagationEnabled** | **Boolean** | Enable or disable the propagation of consumer acknowledgements (ACKs) received on the active replication Message VPN to the standby replication Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;true&#x60;. Available since 2.14. |  [optional] |
|**queueDeadMsgQueue** | **String** | The name of the Dead Message Queue (DMQ) used by the MQTT Session Queue. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. Available since 2.14. |  [optional] |
|**queueEventBindCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional] |
|**queueEventMsgSpoolUsageThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional] |
|**queueEventRejectLowPriorityMsgLimitThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional] |
|**queueMaxBindCount** | **Long** | The maximum number of consumer flows that can bind to the MQTT Session Queue. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;1000&#x60;. Available since 2.14. |  [optional] |
|**queueMaxDeliveredUnackedMsgsPerFlow** | **Long** | The maximum number of messages delivered but not acknowledged per flow for the MQTT Session Queue. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000&#x60;. Available since 2.14. |  [optional] |
|**queueMaxMsgSize** | **Integer** | The maximum message size allowed in the MQTT Session Queue, in bytes (B). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10000000&#x60;. Available since 2.14. |  [optional] |
|**queueMaxMsgSpoolUsage** | **Long** | The maximum message spool usage allowed by the MQTT Session Queue, in megabytes (MB). A value of 0 only allows spooling of the last message received and disables quota checking. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;5000&#x60;. Available since 2.14. |  [optional] |
|**queueMaxRedeliveryCount** | **Long** | The maximum number of times the MQTT Session Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.14. |  [optional] |
|**queueMaxTtl** | **Long** | The maximum time in seconds a message can stay in the MQTT Session Queue when &#x60;queueRespectTtlEnabled&#x60; is &#x60;\&quot;true\&quot;&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;queueMaxTtl&#x60; configured for the MQTT Session Queue, is exceeded. A value of 0 disables expiry. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.14. |  [optional] |
|**queueRejectLowPriorityMsgEnabled** | **Boolean** | Enable or disable the checking of low priority messages against the &#x60;queueRejectLowPriorityMsgLimit&#x60;. This may only be enabled if &#x60;queueRejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.14. |  [optional] |
|**queueRejectLowPriorityMsgLimit** | **Long** | The number of messages of any priority in the MQTT Session Queue above which low priority messages are not admitted but higher priority messages are allowed. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. Available since 2.14. |  [optional] |
|**queueRejectMsgToSenderOnDiscardBehavior** | [**QueueRejectMsgToSenderOnDiscardBehaviorEnum**](#QueueRejectMsgToSenderOnDiscardBehaviorEnum) | Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;when-queue-enabled\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt;  Available since 2.14. |  [optional] |
|**queueRespectTtlEnabled** | **Boolean** | Enable or disable the respecting of the time-to-live (TTL) for messages in the MQTT Session Queue. When enabled, expired messages are discarded or moved to the DMQ. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.14. |  [optional] |



## Enum: MqttSessionVirtualRouterEnum

| Name | Value |
|---- | -----|
| PRIMARY | &quot;primary&quot; |
| BACKUP | &quot;backup&quot; |
| AUTO | &quot;auto&quot; |



## Enum: QueueRejectMsgToSenderOnDiscardBehaviorEnum

| Name | Value |
|---- | -----|
| ALWAYS | &quot;always&quot; |
| WHEN_QUEUE_ENABLED | &quot;when-queue-enabled&quot; |
| NEVER | &quot;never&quot; |



