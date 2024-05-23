package com.solace.maas.ep.common.messages;

import lombok.Data;

@Data
public class ConnectionDetail {
    private String msgVpn;
    private String sempPageSize;
    private Authentication authentication;
    private String name;
    private String url;
}
