package com.solace.maas.ep.event.management.agent.plugin.contentstorage.processor.content.storage.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentStorageEvent implements Serializable {
    private String fileId;
}
