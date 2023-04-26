package com.solace.maas.ep.event.management.agent.plugin.contentstorage.processor.content.storage;

import com.solace.maas.ep.event.management.agent.plugin.contentstorage.processor.content.storage.event.ContentStorageEvent;
import com.solace.maas.ep.event.management.agent.plugin.contentstorage.service.ContentStorageService;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class ContentStorageProcessor extends ResultProcessorImpl<ContentStorageEvent, String> {
    private final ContentStorageService contentStorageService;

    public ContentStorageProcessor(ContentStorageService contentStorageService) {
        super();

        this.contentStorageService = contentStorageService;
    }

    @Override
    public ContentStorageEvent handleEvent(Map<String, Object> properties, String body) throws Exception {
        String mimeType = (String) properties.get("MIME_TYPE");
        String path = (String) properties.get("FILE_PATH");

        FileInputStream fis = new FileInputStream(body);

        String fileUuid = contentStorageService.uploadFile(fis, Paths.get(path), mimeType);

        fis.close();

        Path filePath = Paths.get(body);

        Files.delete(filePath);

        return ContentStorageEvent.builder()
                .fileId(fileUuid)
                .build();
    }
}
