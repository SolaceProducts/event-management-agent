

# MsgVpnCertMatchingRuleCondition


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**attribute** | **String** | Client Username Attribute to be compared with certificate content. Either an attribute or an expression must be provided on creation, but not both. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**expression** | **String** | Glob expression to be matched with certificate content. Either an expression or an attribute must be provided on creation, but not both. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**ruleName** | **String** | The name of the rule. |  [optional] |
|**source** | [**SourceEnum**](#SourceEnum) | Certificate field to be compared with the Attribute. The allowed values and their meaning are:  &lt;pre&gt; \&quot;certificate-thumbprint\&quot; - The attribute is computed as the SHA-1 hash over the entire DER-encoded contents of the client certificate. \&quot;common-name\&quot; - The attribute is extracted from the certificate&#39;s first instance of the Common Name attribute in the Subject DN. \&quot;common-name-last\&quot; - The attribute is extracted from the certificate&#39;s last instance of the Common Name attribute in the Subject DN. \&quot;subject-alternate-name-msupn\&quot; - The attribute is extracted from the certificate&#39;s Other Name type of the Subject Alternative Name and must have the msUPN signature. \&quot;uid\&quot; - The attribute is extracted from the certificate&#39;s first instance of the User Identifier attribute in the Subject DN. \&quot;uid-last\&quot; - The attribute is extracted from the certificate&#39;s last instance of the User Identifier attribute in the Subject DN. \&quot;org-unit\&quot; - The attribute is extracted from the certificate&#39;s first instance of the Org Unit attribute in the Subject DN. \&quot;org-unit-last\&quot; - The attribute is extracted from the certificate&#39;s last instance of the Org Unit attribute in the Subject DN. \&quot;issuer\&quot; - The attribute is extracted from the certificate&#39;s Issuer DN. \&quot;subject\&quot; - The attribute is extracted from the certificate&#39;s Subject DN. \&quot;serial-number\&quot; - The attribute is extracted from the certificate&#39;s Serial Number. \&quot;dns-name\&quot; - The attribute is extracted from the certificate&#39;s Subject Alt Name DNSName. \&quot;ip-address\&quot; - The attribute is extracted from the certificate&#39;s Subject Alt Name IPAddress. &lt;/pre&gt;  |  [optional] |



## Enum: SourceEnum

| Name | Value |
|---- | -----|
| CERTIFICATE_THUMBPRINT | &quot;certificate-thumbprint&quot; |
| COMMON_NAME | &quot;common-name&quot; |
| COMMON_NAME_LAST | &quot;common-name-last&quot; |
| SUBJECT_ALTERNATE_NAME_MSUPN | &quot;subject-alternate-name-msupn&quot; |
| UID | &quot;uid&quot; |
| UID_LAST | &quot;uid-last&quot; |
| ORG_UNIT | &quot;org-unit&quot; |
| ORG_UNIT_LAST | &quot;org-unit-last&quot; |
| ISSUER | &quot;issuer&quot; |
| SUBJECT | &quot;subject&quot; |
| SERIAL_NUMBER | &quot;serial-number&quot; |
| DNS_NAME | &quot;dns-name&quot; |
| IP_ADDRESS | &quot;ip-address&quot; |



