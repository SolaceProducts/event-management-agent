package com.solace.maas.ep.common.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventBrokerAuthenticationConfiguration {
    private String type;
    private String protocol;
    @NotNull
    @Valid
    private EventBrokerCredentialConfiguration credential;
}