package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SempRdpQueueBindingDeletionRequest {
    private String rdpName;
    private String msgVpn;
    private String queueBindingName;
}
