package com.solace.maas.ep.runtime.agent.plugin.solace.processor.event;

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
public class SolaceSubscriptionEvent implements Serializable {
    private String queueName;
    private List<String> subscriptionList;
}
