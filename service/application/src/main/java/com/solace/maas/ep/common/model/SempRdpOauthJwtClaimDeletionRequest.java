package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SempRdpOauthJwtClaimDeletionRequest {
    private String rdpName;
    private String msgVpn;
    private String restConsumerName;
    private String oauthJwtClaimName;
}