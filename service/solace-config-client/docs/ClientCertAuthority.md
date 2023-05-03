

# ClientCertAuthority


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**certAuthorityName** | **String** | The name of the Certificate Authority. |  [optional] |
|**certContent** | **String** | The PEM formatted content for the trusted root certificate of a client Certificate Authority. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**crlDayList** | **String** | The scheduled CRL refresh day(s), specified as \&quot;daily\&quot; or a comma-separated list of days. Days must be specified as \&quot;Sun\&quot;, \&quot;Mon\&quot;, \&quot;Tue\&quot;, \&quot;Wed\&quot;, \&quot;Thu\&quot;, \&quot;Fri\&quot;, or \&quot;Sat\&quot;, with no spaces, and in sorted order from Sunday to Saturday. The empty-string (\&quot;\&quot;) can also be specified, indicating no schedule is configured (\&quot;crlTimeList\&quot; must also be configured to the empty-string). Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;daily\&quot;&#x60;. |  [optional] |
|**crlTimeList** | **String** | The scheduled CRL refresh time(s), specified as \&quot;hourly\&quot; or a comma-separated list of 24-hour times in the form hh:mm, or h:mm. There must be no spaces, and times (up to 4) must be in sorted order from 0:00 to 23:59. The empty-string (\&quot;\&quot;) can also be specified, indicating no schedule is configured (\&quot;crlDayList\&quot; must also be configured to the empty-string). Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;3:00\&quot;&#x60;. |  [optional] |
|**crlUrl** | **String** | The URL for the CRL source. This is a required attribute for CRL to be operational and the URL must be complete with http:// included. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**ocspNonResponderCertEnabled** | **Boolean** | Enable or disable allowing a non-responder certificate to sign an OCSP response. Typically used with an OCSP override URL in cases where a single certificate is used to sign client certificates and OCSP responses. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**ocspOverrideUrl** | **String** | The OCSP responder URL to use for overriding the one supplied in the client certificate. The URL must be complete with http:// included. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**ocspTimeout** | **Long** | The timeout in seconds to receive a response from the OCSP responder after sending a request or making the initial connection attempt. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;5&#x60;. |  [optional] |
|**revocationCheckEnabled** | **Boolean** | Enable or disable Certificate Authority revocation checking. Changes to this attribute are synchronized to HA mates via config-sync. The default value is &#x60;false&#x60;. |  [optional] |



