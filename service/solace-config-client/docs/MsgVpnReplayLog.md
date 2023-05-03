

# MsgVpnReplayLog


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**egressEnabled** | **Boolean** | Enable or disable the transmission of messages from the Replay Log. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**ingressEnabled** | **Boolean** | Enable or disable the reception of messages to the Replay Log. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**maxSpoolUsage** | **Long** | The maximum spool usage allowed by the Replay Log, in megabytes (MB). If this limit is exceeded, old messages will be trimmed. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;0&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**replayLogName** | **String** | The name of the Replay Log. |  [optional] |
|**topicFilterEnabled** | **Boolean** | Enable or disable topic filtering for the Replay Log. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.27. |  [optional] |



