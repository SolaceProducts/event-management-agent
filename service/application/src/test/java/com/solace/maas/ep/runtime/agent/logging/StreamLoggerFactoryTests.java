package com.solace.maas.ep.runtime.agent.logging;

import com.solace.maas.ep.runtime.agent.TestConfig;
import lombok.SneakyThrows;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class StreamLoggerFactoryTests {

    @Mock
    ProducerTemplate producerTemplate;

    @SneakyThrows
    @Test
    public void testStreamLoggerFactory() {
        StreamLoggerFactory factory = new StreamLoggerFactory(producerTemplate);
        factory.getStreamingAppender();

        assertThatNoException();
    }
}
