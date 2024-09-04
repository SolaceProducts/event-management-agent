package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SempTopicSubscriptionDeletionRequest {
    private String topicName;
    private String queueName;
    private String msgVpn;
}
