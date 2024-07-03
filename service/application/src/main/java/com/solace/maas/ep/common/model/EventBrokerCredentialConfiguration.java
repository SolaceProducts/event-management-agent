package com.solace.maas.ep.common.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventBrokerCredentialConfiguration {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}