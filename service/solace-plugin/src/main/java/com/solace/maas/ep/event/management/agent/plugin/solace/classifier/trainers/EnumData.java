package com.solace.maas.ep.event.management.agent.plugin.solace.classifier.trainers;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EnumData {
    private String name;
    private List<String> enumValues;

}
