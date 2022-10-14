package com.solace.maas.ep.event.management.agent.plugin.localstorage.processor.output.file;

import com.solace.maas.ep.event.management.agent.plugin.route.manager.FileStoreManager;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("PMD")
public class DataCollectionFileWriteProcessorTests {
    @Mock
    private FileStoreManager fileStoreManager;

    @InjectMocks
    private DataCollectionFileWriteProcessor dataCollectionFileWriteProcessor;

    @SneakyThrows
    @Test
    public void testHandleEvent() {
        Map<String, Object> properties = Map.of(
                RouteConstants.SCAN_ID, "scan1",
                Exchange.FILE_NAME_PRODUCED, "/test/file"
        );

        dataCollectionFileWriteProcessor.handleEvent(properties, null);

        assertThatNoException();
    }
}
