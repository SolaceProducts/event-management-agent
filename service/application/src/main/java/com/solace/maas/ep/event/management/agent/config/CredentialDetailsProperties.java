package com.solace.maas.ep.event.management.agent.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDetailsProperties implements Serializable {
    private String source;
    private List<AuthenticationOperationDetailsProperties> operations;
    private List<PluginProperties> properties;
}
