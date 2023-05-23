

# MsgVpnBridgeTlsTrustedCommonName


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**bridgeName** | **String** | The name of the Bridge. Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation. |  [optional] |
|**bridgeVirtualRouter** | [**BridgeVirtualRouterEnum**](#BridgeVirtualRouterEnum) | The virtual router of the Bridge. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The Bridge is used for the primary virtual router. \&quot;backup\&quot; - The Bridge is used for the backup virtual router. \&quot;auto\&quot; - The Bridge is automatically assigned a virtual router at creation, depending on the broker&#39;s active-standby role. &lt;/pre&gt;  Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation. |  [optional] |
|**tlsTrustedCommonName** | **String** | The expected trusted common name of the remote certificate. Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation. |  [optional] |



## Enum: BridgeVirtualRouterEnum

| Name | Value |
|---- | -----|
| PRIMARY | &quot;primary&quot; |
| BACKUP | &quot;backup&quot; |
| AUTO | &quot;auto&quot; |



