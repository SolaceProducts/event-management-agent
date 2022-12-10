package com.solace.maas.ep.event.management.agent.plugin.messagingService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationOperationDetailsEvent implements Serializable {
    private String id;
    private String name;
}
