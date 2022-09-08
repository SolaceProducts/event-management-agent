package com.solace.maas.ep.runtime.agent.plugin.localstorage.processor.output.file;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.runtime.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import com.solace.maas.ep.runtime.agent.plugin.route.manager.FileStoreManager;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataCollectionFileWriteProcessor extends ResultProcessorImpl<DataCollectionFileEvent, String> {
    private final FileStoreManager fileStoreManager;

    @Autowired
    public DataCollectionFileWriteProcessor(FileStoreManager fileStoreManager) {
        super();
        this.fileStoreManager = fileStoreManager;
    }

    @Override
    public DataCollectionFileEvent handleEvent(Map<String, Object> properties, String body) throws Exception {
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String path = (String) properties.get(Exchange.FILE_NAME_PRODUCED);

        DataCollectionFileEvent event = DataCollectionFileEvent.builder()
                .scanId(scanId)
                .path(path)
                .purged(false)
                .build();

        fileStoreManager.storeRecord(event);

        return event;
    }
}
