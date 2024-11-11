package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SempAclPublishTopicExceptionDeletionRequest {
    private String msgVpn;
    private String aclProfileName;
    /**
     * The topic string to be deleted from the ACL exception list - may include wild cards
     */
    private String publishTopic;
}
