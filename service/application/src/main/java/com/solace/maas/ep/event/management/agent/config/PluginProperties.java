package com.solace.maas.ep.event.management.agent.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginProperties implements Serializable {
    private String name;
    private String value;
}
