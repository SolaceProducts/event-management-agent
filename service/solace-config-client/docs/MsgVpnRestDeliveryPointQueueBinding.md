

# MsgVpnRestDeliveryPointQueueBinding


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**gatewayReplaceTargetAuthorityEnabled** | **Boolean** | Enable or disable whether the authority for the request-target is replaced with that configured for the REST Consumer remote. When enabled, the broker sends HTTP requests in absolute-form, with the request-target&#39;s authority taken from the REST Consumer&#39;s remote host and port configuration. When disabled, the broker sends HTTP requests whose request-target matches that of the original request message, including whether to use absolute-form or origin-form. This configuration is applicable only when the Message VPN is in REST gateway mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. Available since 2.6. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**postRequestTarget** | **String** | The request-target string to use when sending requests. It identifies the target resource on the far-end REST Consumer upon which to apply the request. There are generally two common forms for the request-target. The origin-form is most often used in practice and contains the path and query components of the target URI. If the path component is empty then the client must generally send a \&quot;/\&quot; as the path. When making a request to a proxy, most often the absolute-form is required. This configuration is only applicable when the Message VPN is in REST messaging mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**queueBindingName** | **String** | The name of a queue in the Message VPN. |  [optional] |
|**requestTargetEvaluation** | [**RequestTargetEvaluationEnum**](#RequestTargetEvaluationEnum) | The type of evaluation to perform on the request target. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Do not evaluate substitution expressions on the request target. \&quot;substitution-expressions\&quot; - Evaluate substitution expressions on the request target. &lt;/pre&gt;  Available since 2.23. |  [optional] |
|**restDeliveryPointName** | **String** | The name of the REST Delivery Point. |  [optional] |



## Enum: RequestTargetEvaluationEnum

| Name | Value |
|---- | -----|
| NONE | &quot;none&quot; |
| SUBSTITUTION_EXPRESSIONS | &quot;substitution-expressions&quot; |



