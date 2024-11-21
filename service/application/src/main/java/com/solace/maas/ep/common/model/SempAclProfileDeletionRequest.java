package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SempAclProfileDeletionRequest {
    private String msgVpn;
    private String aclProfileName;
}
