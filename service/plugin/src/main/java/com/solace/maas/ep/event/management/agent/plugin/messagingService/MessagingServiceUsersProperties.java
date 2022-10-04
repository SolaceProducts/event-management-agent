package com.solace.maas.ep.event.management.agent.plugin.messagingService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagingServiceUsersProperties implements Serializable {
    private String name;

    private String username;

    private String password;

    private String clientName;
}
