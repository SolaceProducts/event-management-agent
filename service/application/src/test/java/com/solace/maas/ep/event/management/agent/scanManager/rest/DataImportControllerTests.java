package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class DataImportControllerTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    ImportService importService;

    @SneakyThrows
    @Test
    public void testDataImportController() {

        DataImportController controller = new DataImportControllerImpl(importService);

        ResponseEntity<String> reply =
                controller.read("messagingServiceId", null, "scheduleId", "scanId");

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reply.getBody()).contains("Import complete");

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testDataImportControllerException() {
        DataImportController controller = new DataImportControllerImpl(importService);

        doThrow(new IOException("Exception occurred"))
                .when(importService)
                .importData(any(ImportRequestBO.class));

        ResponseEntity<String> reply =
                controller.read("messagingServiceId", null, "scheduleId", "scanId");

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reply.getBody()).contains("Exception occurred");

        exception.expect(IOException.class);
    }
}