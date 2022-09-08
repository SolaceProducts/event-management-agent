package com.solace.maas.ep.runtime.agent.plugin.messagingService;

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
public class MessagingServiceManagementProperties implements Serializable {
    private List<MessagingServiceConnectionProperties> connections;
}
