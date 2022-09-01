package com.solace.maas.ep.runtime.agent.plugin.route.delegate;

import com.solace.maas.ep.runtime.agent.TestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class LoggingDelegateImplTests {
    @InjectMocks
    private LoggingDelegateImpl loggingDelegate;

    @Test
    public void testGenerateSolaceQueueListingRouteList() {
        loggingDelegate.generateRouteList(List.of(), List.of(), "testScanType",
                "service1");

        assertThatNoException();
    }
}
