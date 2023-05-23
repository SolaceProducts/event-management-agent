

# AboutUser


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**globalAccessLevel** | [**GlobalAccessLevelEnum**](#GlobalAccessLevelEnum) | The global access level of the User. The allowed values and their meaning are:  &lt;pre&gt; \&quot;admin\&quot; - Full administrative access. \&quot;none\&quot; - No access. \&quot;read-only\&quot; - Read only access. \&quot;read-write\&quot; - Read and write access. &lt;/pre&gt;  |  [optional] |
|**sessionActive** | **Boolean** | Indicates whether a session is active for this request. Available since 2.24. |  [optional] |
|**sessionCreateTime** | **Integer** | The timestamp of when the session was created. This attribute may not be returned in a GET. This value represents the number of seconds since 1970-01-01 00:00:00 UTC (Unix time). Available since 2.21. |  [optional] |
|**sessionCurrentTime** | **Integer** | The current server timestamp. This is provided as a reference point for the other timestamps provided. This attribute may not be returned in a GET. This value represents the number of seconds since 1970-01-01 00:00:00 UTC (Unix time). Available since 2.21. |  [optional] |
|**sessionHardExpiryTime** | **Integer** | The hard expiry time for the session. After this time the session will be invalid, regardless of activity. This attribute may not be returned in a GET. This value represents the number of seconds since 1970-01-01 00:00:00 UTC (Unix time). Available since 2.21. |  [optional] |
|**sessionId** | **String** | An identifier for the session to differentiate this session from other sessions for the same user. This value is not guaranteed to be unique between active sessions for different users. This attribute may not be returned in a GET. Available since 2.21. |  [optional] |
|**sessionIdleExpiryTime** | **Integer** | The session idle expiry time. After this time the session will be invalid if there has been no activity. This attribute may not be returned in a GET. This value represents the number of seconds since 1970-01-01 00:00:00 UTC (Unix time). Available since 2.21. |  [optional] |
|**username** | **String** | The username of the User. Available since 2.21. |  [optional] |



## Enum: GlobalAccessLevelEnum

| Name | Value |
|---- | -----|
| ADMIN | &quot;admin&quot; |
| NONE | &quot;none&quot; |
| READ_ONLY | &quot;read-only&quot; |
| READ_WRITE | &quot;read-write&quot; |



