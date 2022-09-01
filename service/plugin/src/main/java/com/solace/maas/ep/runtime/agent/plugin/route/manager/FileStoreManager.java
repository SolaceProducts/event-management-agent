package com.solace.maas.ep.runtime.agent.plugin.route.manager;

import com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event.AggregatedFileEvent;
import com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event.DataCollectionFileEvent;

public interface FileStoreManager {
    void storeRecord(DataCollectionFileEvent dataCollectionFileEvent);

    void storeRecord(AggregatedFileEvent aggregatedFileEvent);
}
