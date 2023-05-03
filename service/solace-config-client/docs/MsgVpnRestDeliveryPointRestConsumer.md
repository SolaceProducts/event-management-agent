

# MsgVpnRestDeliveryPointRestConsumer


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**authenticationAwsAccessKeyId** | **String** | The AWS access key id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26. |  [optional] |
|**authenticationAwsRegion** | **String** | The AWS region id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26. |  [optional] |
|**authenticationAwsSecretAccessKey** | **String** | The AWS secret access key. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26. |  [optional] |
|**authenticationAwsService** | **String** | The AWS service id. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.26. |  [optional] |
|**authenticationClientCertContent** | **String** | The PEM formatted content for the client certificate that the REST Consumer will present to the REST host. It must consist of a private key and between one and three certificates comprising the certificate trust chain. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional] |
|**authenticationClientCertPassword** | **String** | The password for the client certificate. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional] |
|**authenticationHttpBasicPassword** | **String** | The password for the username. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**authenticationHttpBasicUsername** | **String** | The username that the REST Consumer will use to login to the REST host. Normally a username is only configured when basic authentication is selected for the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**authenticationHttpHeaderName** | **String** | The authentication header name. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.15. |  [optional] |
|**authenticationHttpHeaderValue** | **String** | The authentication header value. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.15. |  [optional] |
|**authenticationOauthClientId** | **String** | The OAuth client ID. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19. |  [optional] |
|**authenticationOauthClientScope** | **String** | The OAuth scope. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19. |  [optional] |
|**authenticationOauthClientSecret** | **String** | The OAuth client secret. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19. |  [optional] |
|**authenticationOauthClientTokenEndpoint** | **String** | The OAuth token endpoint URL that the REST Consumer will use to request a token for login to the REST host. Must begin with \&quot;https\&quot;. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.19. |  [optional] |
|**authenticationOauthJwtSecretKey** | **String** | The OAuth secret key used to sign the token request JWT. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.21. |  [optional] |
|**authenticationOauthJwtTokenEndpoint** | **String** | The OAuth token endpoint URL that the REST Consumer will use to request a token for login to the REST host. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.21. |  [optional] |
|**authenticationScheme** | [**AuthenticationSchemeEnum**](#AuthenticationSchemeEnum) | The authentication scheme used by the REST Consumer to login to the REST host. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Login with no authentication. This may be useful for anonymous connections or when a REST Consumer does not require authentication. \&quot;http-basic\&quot; - Login with a username and optional password according to HTTP Basic authentication as per RFC2616. \&quot;client-certificate\&quot; - Login with a client TLS certificate as per RFC5246. Client certificate authentication is only available on TLS connections. \&quot;http-header\&quot; - Login with a specified HTTP header. \&quot;oauth-client\&quot; - Login with OAuth 2.0 client credentials. \&quot;oauth-jwt\&quot; - Login with OAuth (RFC 7523 JWT Profile). \&quot;transparent\&quot; - Login using the Authorization header from the message properties, if present. Transparent authentication passes along existing Authorization header metadata instead of discarding it. Note that if the message is coming from a REST producer, the REST service must be configured to forward the Authorization header. \&quot;aws\&quot; - Login using AWS Signature Version 4 authentication (AWS4-HMAC-SHA256). &lt;/pre&gt;  |  [optional] |
|**enabled** | **Boolean** | Enable or disable the REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**httpMethod** | [**HttpMethodEnum**](#HttpMethodEnum) | The HTTP method to use (POST or PUT). This is used only when operating in the REST service \&quot;messaging\&quot; mode and is ignored in \&quot;gateway\&quot; mode. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;post\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;post\&quot; - Use the POST HTTP method. \&quot;put\&quot; - Use the PUT HTTP method. &lt;/pre&gt;  Available since 2.17. |  [optional] |
|**localInterface** | **String** | The interface that will be used for all outgoing connections associated with the REST Consumer. When unspecified, an interface is automatically chosen. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**maxPostWaitTime** | **Integer** | The maximum amount of time (in seconds) to wait for an HTTP POST response from the REST Consumer. Once this time is exceeded, the TCP connection is reset. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;30&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**outgoingConnectionCount** | **Integer** | The number of concurrent TCP connections open to the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;. |  [optional] |
|**remoteHost** | **String** | The IP address or DNS name to which the broker is to connect to deliver messages for the REST Consumer. A host value must be configured for the REST Consumer to be operationally up. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional] |
|**remotePort** | **Long** | The port associated with the host of the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;8080&#x60;. |  [optional] |
|**restConsumerName** | **String** | The name of the REST Consumer. |  [optional] |
|**restDeliveryPointName** | **String** | The name of the REST Delivery Point. |  [optional] |
|**retryDelay** | **Integer** | The number of seconds that must pass before retrying the remote REST Consumer connection. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;3&#x60;. |  [optional] |
|**tlsCipherSuiteList** | **String** | The colon-separated list of cipher suites the REST Consumer uses in its encrypted connection. The value &#x60;\&quot;default\&quot;&#x60; implies all supported suites ordered from most secure to least secure. The list of default cipher suites is available in the &#x60;tlsCipherSuiteMsgBackboneDefaultList&#x60; attribute of the Broker object in the Monitoring API. The REST Consumer should choose the first suite from this list that it supports. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional] |
|**tlsEnabled** | **Boolean** | Enable or disable encryption (TLS) for the REST Consumer. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |



## Enum: AuthenticationSchemeEnum

| Name | Value |
|---- | -----|
| NONE | &quot;none&quot; |
| HTTP_BASIC | &quot;http-basic&quot; |
| CLIENT_CERTIFICATE | &quot;client-certificate&quot; |
| HTTP_HEADER | &quot;http-header&quot; |
| OAUTH_CLIENT | &quot;oauth-client&quot; |
| OAUTH_JWT | &quot;oauth-jwt&quot; |
| TRANSPARENT | &quot;transparent&quot; |
| AWS | &quot;aws&quot; |



## Enum: HttpMethodEnum

| Name | Value |
|---- | -----|
| POST | &quot;post&quot; |
| PUT | &quot;put&quot; |



