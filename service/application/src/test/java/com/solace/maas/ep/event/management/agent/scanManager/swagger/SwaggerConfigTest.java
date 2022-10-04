package com.solace.maas.ep.event.management.agent.scanManager.swagger;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.SwaggerConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class SwaggerConfigTest {

    @InjectMocks
    SwaggerConfig swaggerConfig;

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    @SneakyThrows
    public void SwaggerConfigTest() {
        swaggerConfig.swagger(resourceLoader);

        assertThatNoException();
    }
}
