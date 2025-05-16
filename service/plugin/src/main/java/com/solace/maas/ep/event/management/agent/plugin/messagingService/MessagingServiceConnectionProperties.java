package com.solace.maas.ep.event.management.agent.plugin.messagingService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagingServiceConnectionProperties implements Serializable {
    private String name;

    private String authenticationType;

    private String url;

    private String msgVpn;

    private String trustStoreDir;

    private String bootstrapServer;

    private List<MessagingServiceUsersProperties> users;

    private Boolean proxyEnabled;
    private String proxyType;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;
}