package com.solace.maas.ep.runtime.agent.plugin.processor.solace.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolaceQueueNameEvent implements Serializable {
    private String name;
}
