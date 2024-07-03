package com.solace.maas.ep.common.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EventBrokerResourceConfiguration extends ResourceConfiguration {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotEmpty
    private List<@Valid EventBrokerConnectionConfiguration> connections;
    @NotNull
    private ResourceConfigurationType resourceConfigurationType;
}