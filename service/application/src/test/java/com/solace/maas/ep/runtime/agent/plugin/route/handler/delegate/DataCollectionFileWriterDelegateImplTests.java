package com.solace.maas.ep.runtime.agent.plugin.route.handler.delegate;

import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.route.delegate.DataCollectionFileWriterDelegateImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class DataCollectionFileWriterDelegateImplTests {
    @InjectMocks
    private DataCollectionFileWriterDelegateImpl dataCollectionFileWriterDelegate;

    @Test
    public void testGenerateSolaceQueueListingRouteList() {
        dataCollectionFileWriterDelegate.generateRouteList(List.of(), List.of(), "testScanType",
                "service1");

        assertThatNoException();
    }
}
