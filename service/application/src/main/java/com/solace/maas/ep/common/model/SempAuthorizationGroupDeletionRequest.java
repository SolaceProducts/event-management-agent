package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SempAuthorizationGroupDeletionRequest {
    private String msgVpn;
    private String authorizationGroupName;
}
