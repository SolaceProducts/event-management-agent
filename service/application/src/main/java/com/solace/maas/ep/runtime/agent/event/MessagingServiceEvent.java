package com.solace.maas.ep.runtime.agent.event;

import com.solace.maas.ep.runtime.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessagingServiceEvent implements Serializable {
    private String id;

    private String name;

    private String messagingServiceType;

    private List<ConnectionDetailsEvent> connectionDetails;
}
