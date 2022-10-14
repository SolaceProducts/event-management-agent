package com.solace.maas.ep.event.management.agent.plugin.localstorage.processor.output.file;

import com.solace.maas.ep.event.management.agent.plugin.route.manager.FileStoreManager;
import com.solace.maas.ep.event.management.agent.plugin.constants.AggregationConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.FileDetailsAggregationEvent;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;


@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("PMD")
public class AggregationFileWriteProcessorTests {
    @Mock
    private FileStoreManager fileStoreManager;

    @InjectMocks
    private AggregationFileWriteProcessor aggregationFileWriteProcessor;

    @Test
    public void testHandleEvent() throws Exception {
        List<FileDetailsAggregationEvent> files = List.of(FileDetailsAggregationEvent.builder()
                .path("/test/file/path/test.json")
                .name("testFile")
                .id(UUID.randomUUID().toString())
                .build());

        Map<String, Object> properties = new HashMap<>();
        properties.put(AggregationConstants.AGGREGATED_FILE_PATH, "/test/file/path");
        properties.put("FILES", files);

        aggregationFileWriteProcessor.handleEvent(properties, null);

        assertThatNoException();
    }
}
