package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.TestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class LivenessControllerTest {
    @InjectMocks
    private LivenessControllerImpl controller;

    @Test
    public void liveness() {

        ResponseEntity<String> reply =
                controller.liveness();

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat("EMA is alive".equals(reply.getBody()));
        assertThatNoException();
    }
}
