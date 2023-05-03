

# MsgVpnAuthorizationGroup


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**aclProfileName** | **String** | The ACL Profile of the Authorization Group. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional] |
|**authorizationGroupName** | **String** | The name of the Authorization Group. For LDAP groups, special care is needed if the group name contains special characters such as &#39;#&#39;, &#39;+&#39;, &#39;;&#39;, &#39;&#x3D;&#39; as the value of the group name returned from the LDAP server might prepend those characters with &#39;\\&#39;. For example a group name called &#39;test#,lab,com&#39; will be returned from the LDAP server as &#39;test\\#,lab,com&#39;. |  [optional] |
|**clientProfileName** | **String** | The Client Profile of the Authorization Group. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional] |
|**enabled** | **Boolean** | Enable or disable the Authorization Group in the Message VPN. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default value is &#x60;false&#x60;. |  [optional] |
|**msgVpnName** | **String** | The name of the Message VPN. |  [optional] |
|**orderAfterAuthorizationGroupName** | **String** | Lower the priority to be less than this group. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is not applicable. |  [optional] |
|**orderBeforeAuthorizationGroupName** | **String** | Raise the priority to be greater than this group. This attribute is absent from a GET and not updated when absent in a PUT, subject to the exceptions in note 4. Changes to this attribute are synchronized to HA mates and replication sites via config-sync. The default is not applicable. |  [optional] |



