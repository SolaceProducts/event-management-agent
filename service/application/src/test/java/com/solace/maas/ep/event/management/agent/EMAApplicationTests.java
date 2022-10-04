package com.solace.maas.ep.event.management.agent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class EMAApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testGetDefaults() {
        EMAApplication.getDefault();

        assertThatNoException();
    }
}
