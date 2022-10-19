package com.solace.maas.ep.event.management.agent.plugin.async.processor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestEvent implements Serializable {
    private String data;

    private Long count;
}
