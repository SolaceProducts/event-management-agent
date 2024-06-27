package com.solace.maas.ep.common.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventBrokerConnectionConfiguration {

    @NotBlank
    private String msgVpn;

    private String sempPageSize;
    @NotNull
    @Valid
    private EventBrokerAuthenticationConfiguration authentication;
    @NotBlank
    private String name;
    @NotBlank
    private String url;
}