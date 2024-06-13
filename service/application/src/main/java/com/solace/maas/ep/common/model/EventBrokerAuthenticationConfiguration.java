package com.solace.maas.ep.common.model;


import lombok.Data;

@Data
public class EventBrokerAuthenticationConfiguration {
    private String type;
    private String protocol;
    private EventBrokerCredentialConfiguration credential;
}