package com.solace.maas.ep.event.management.agent.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDetailsPluginProperties {
    private String protocol;

    private List<CredentialDetailsProperties> credentials;

    private List<PluginProperties> properties;
}
