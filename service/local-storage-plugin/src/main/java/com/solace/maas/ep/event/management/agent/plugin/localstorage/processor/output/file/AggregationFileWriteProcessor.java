package com.solace.maas.ep.event.management.agent.plugin.localstorage.processor.output.file;

import com.solace.maas.ep.event.management.agent.plugin.constants.AggregationConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.AggregatedFileEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.FileDetailsAggregationEvent;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.FileStoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AggregationFileWriteProcessor extends ResultProcessorImpl<AggregatedFileEvent, Object> {
    private final FileStoreManager fileStoreManager;

    @Autowired
    public AggregationFileWriteProcessor(FileStoreManager fileStoreManager) {
        super();
        this.fileStoreManager = fileStoreManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AggregatedFileEvent handleEvent(Map<String, Object> properties, Object body) throws Exception {
        String aggregatedFilePath = (String) properties.get(AggregationConstants.AGGREGATED_FILE_PATH);
        List<FileDetailsAggregationEvent> events = (List<FileDetailsAggregationEvent>) properties.get("FILES");
        List<String> fileIds = events.stream()
                .map(FileDetailsAggregationEvent::getId)
                .collect(Collectors.toUnmodifiableList());

        AggregatedFileEvent aggregatedFileEvent = AggregatedFileEvent.builder()
                .path(aggregatedFilePath)
                .fileIds(fileIds)
                .purged(false)
                .build();

        fileStoreManager.storeRecord(aggregatedFileEvent);

        return aggregatedFileEvent;
    }
}
