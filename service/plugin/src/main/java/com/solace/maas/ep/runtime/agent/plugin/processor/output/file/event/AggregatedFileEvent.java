package com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event;

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
public class AggregatedFileEvent implements Serializable {
    private String id;

    private String path;

    private boolean purged;

    private List<String> fileIds;
}
