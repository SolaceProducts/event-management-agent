

# MsgVpnDistributedCache


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cacheName** | **String** | The name of the Distributed Cache. |  [optional] |
|**cacheVirtualRouter** | [**CacheVirtualRouterEnum**](#CacheVirtualRouterEnum) | The virtual router of the Distributed Cache. The default value is &#x60;\&quot;auto\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;auto\&quot; - The Distributed Cache is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt;  Available since 2.28. |  [optional] |
|**enabled** | **Boolean** | Enable or disable the Distributed Cache. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**heartbeat** | **Long** | The heartbeat interval, in seconds, used by the Cache Instances to monitor connectivity with the message broker. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;10&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**scheduledDeleteMsgDayList** | **String** | The scheduled delete message day(s), specified as \&quot;daily\&quot; or a comma-separated list of days. Days must be specified as \&quot;Sun\&quot;, \&quot;Mon\&quot;, \&quot;Tue\&quot;, \&quot;Wed\&quot;, \&quot;Thu\&quot;, \&quot;Fri\&quot;, or \&quot;Sat\&quot;, with no spaces, and in sorted order from Sunday to Saturday. The empty-string (\&quot;\&quot;) can also be specified, indicating no schedule is configured (\&quot;scheduledDeleteMsgTimeList\&quot; must also be configured to the empty-string). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**scheduledDeleteMsgTimeList** | **String** | The scheduled delete message time(s), specified as \&quot;hourly\&quot; or a comma-separated list of 24-hour times in the form hh:mm, or h:mm. There must be no spaces, and times (up to 4) must be in sorted order from 0:00 to 23:59. The empty-string (\&quot;\&quot;) can also be specified, indicating no schedule is configured (\&quot;scheduledDeleteMsgDayList\&quot; must also be configured to the empty-string). Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |



## Enum: CacheVirtualRouterEnum

| Name | Value |
|---- | -----|
| AUTO | &quot;auto&quot; |



