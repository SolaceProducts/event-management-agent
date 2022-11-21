package com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DataCollectionFileEvent implements Serializable {
    private String id;

    private String path;

    private String name;

    private String scanId;

    private boolean purged;
}
