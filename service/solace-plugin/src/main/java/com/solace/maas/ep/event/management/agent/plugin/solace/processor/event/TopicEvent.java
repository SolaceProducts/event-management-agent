package com.solace.maas.ep.event.management.agent.plugin.solace.processor.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TopicEvent implements Serializable {
    private String data;
    private String subscription;
}
