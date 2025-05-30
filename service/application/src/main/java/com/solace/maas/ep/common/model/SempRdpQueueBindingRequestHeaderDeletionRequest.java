package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SempRdpQueueBindingRequestHeaderDeletionRequest {
    private String rdpName;
    private String msgVpn;
    private String queueBindingName;
    private String headerName;
    private boolean isProtected;
}
