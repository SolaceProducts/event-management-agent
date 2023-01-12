package com.solace.maas.ep.event.management.agent.async.manager;

import com.solace.maas.ep.event.management.agent.TestConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Slf4j
public class AsyncProcessManagerTests {
    @Autowired
    AsyncProcessManager asyncProcessManager;

    @Test
    @SneakyThrows
    public void testTerminate() {
        asyncProcessManager.terminate("key");
    }
}
