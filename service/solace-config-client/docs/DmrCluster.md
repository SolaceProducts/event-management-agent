

# DmrCluster


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**authenticationBasicEnabled** | **Boolean** | Enable or disable basic authentication for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**authenticationBasicPassword** | **String** | The password used to authenticate incoming Cluster Links when using basic internal authentication. The same password is also used by outgoing Cluster Links if a per-Link password is not configured. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**authenticationBasicType** | [**AuthenticationBasicTypeEnum**](#AuthenticationBasicTypeEnum) | The type of basic authentication to use for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;internal\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Use locally configured password. \&quot;none\&quot; - No authentication. &lt;/pre&gt;  |  [optional] |
|**authenticationClientCertContent** | **String** | The PEM formatted content for the client certificate used to login to the remote node. It must consist of a private key and between one and three certificates comprising the certificate trust chain. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**authenticationClientCertEnabled** | **Boolean** | Enable or disable client certificate authentication for Cluster Links. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**authenticationClientCertPassword** | **String** | The password for the client certificate. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**directOnlyEnabled** | **Boolean** | Enable or disable direct messaging only. Guaranteed messages will not be transmitted through the cluster. The default value is &#x60;false&#x60;. |  [optional] |
|**dmrClusterName** | **String** | The name of the Cluster. |  [optional] |
|**enabled** | **Boolean** | Enable or disable the Cluster. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**nodeName** | **String** | The name of this node in the Cluster. This is the name that this broker (or redundant group of brokers) is know by to other nodes in the Cluster. The name is chosen automatically to be either this broker&#39;s Router Name or Mate Router Name, depending on which Active Standby Role (primary or backup) this broker plays in its redundancy group. |  [optional] |
|**tlsServerCertEnforceTrustedCommonNameEnabled** | **Boolean** | Enable or disable the enforcing of the common name provided by the remote broker against the list of trusted common names configured for the Link. If enabled, the certificate&#39;s common name must match one of the trusted common names for the Link to be accepted. Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is enabled. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. Deprecated since 2.18. Common Name validation has been replaced by Server Certificate Name validation. |  [optional] |
|**tlsServerCertMaxChainDepth** | **Long** | The maximum allowed depth of a certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;3&#x60;. |  [optional] |
|**tlsServerCertValidateDateEnabled** | **Boolean** | Enable or disable the validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the certificate. When disabled, the certificate is accepted even if the certificate is not valid based on these dates. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. |  [optional] |
|**tlsServerCertValidateNameEnabled** | **Boolean** | Enable or disable the standard TLS authentication mechanism of verifying the name used to connect to the bridge. If enabled, the name used to connect to the bridge is checked against the names specified in the certificate returned by the remote router. Legacy Common Name validation is not performed if Server Certificate Name Validation is enabled, even if Common Name validation is also enabled. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;true&#x60;. Available since 2.18. |  [optional] |



## Enum: AuthenticationBasicTypeEnum

| Name | Value |
|---- | -----|
| INTERNAL | &quot;internal&quot; |
| NONE | &quot;none&quot; |



