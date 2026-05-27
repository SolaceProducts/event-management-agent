package com.solace.maas.ep.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SempAclClientConnectExceptionDeletionRequest {

    private String msgVpn;
    private String aclProfileName;
    /**
     * The IP address/netmask of the client connect exception in canonical CIDR form.
     */
    private String clientConnectExceptionAddress;
}
