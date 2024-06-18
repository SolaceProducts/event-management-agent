package com.solace.maas.ep.common.model;


import lombok.Data;

@Data
public class EventBrokerConnectionConfiguration {
    private String msgVpn;
    private String sempPageSize;
    private EventBrokerAuthenticationConfiguration authentication;
    private String name;
    private String url;
}