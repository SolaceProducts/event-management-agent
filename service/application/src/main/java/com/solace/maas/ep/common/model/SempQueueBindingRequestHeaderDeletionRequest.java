package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SempQueueBindingRequestHeaderDeletionRequest {
    private String rdpName;
    private String msgVpn;
    private String queueBindingName;
    private String headerName;
    boolean isProtected;
}