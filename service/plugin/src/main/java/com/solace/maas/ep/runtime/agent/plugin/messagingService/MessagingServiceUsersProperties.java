package com.solace.maas.ep.runtime.agent.plugin.messagingService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagingServiceUsersProperties {
    private String name;

    private String username;

    private String password;

    private String clientName;
}
