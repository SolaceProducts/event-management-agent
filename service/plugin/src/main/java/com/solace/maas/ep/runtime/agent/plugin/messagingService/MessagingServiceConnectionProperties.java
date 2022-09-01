package com.solace.maas.ep.runtime.agent.plugin.messagingService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagingServiceConnectionProperties {
    private String name;

    private String authenticationType;

    private String url;

    private String msgVpn;

    private String trustStoreDir;

    private String bootstrapServer;

    private List<MessagingServiceUsersProperties> users;
}