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
public class MessagingServiceConnectionPluginProperties implements Serializable {
    private String name;

    private String url;

    private String messagingServiceId;

    private List<AuthenticationDetailsPluginProperties> authentication;

    private List<PluginProperties> properties;
}
