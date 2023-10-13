package com.solace.maas.ep.event.management.agent.scanManager.rest;

import brave.Tracer;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.model.ImportRequestBO;
import com.solace.maas.ep.event.management.agent.scanManager.model.ZipRequestBO;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class DataImportControllerTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    public Tracer tracer;
    @Mock
    ImportService importService;

    @SneakyThrows
    @Test
    public void testDataImportControllerRead() {

        DataImportController controller = new DataImportControllerImpl(tracer, importService);

        MultipartFile multipartFile =
                new MockMultipartFile("file", "test.json", MediaType.APPLICATION_JSON_VALUE,
                        "test file contents".getBytes());

        ResponseEntity<String> reply = controller.read(multipartFile);

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reply.getBody()).contains("Manual import started.");

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testDataImportControllerZip() {

        DataImportController controller = new DataImportControllerImpl(tracer, importService);

        ZipRequestBO zipRequestBO = ZipRequestBO.builder()
                .scanId("scanId")
                .build();

        when(importService.zip(zipRequestBO))
                .thenReturn(mock(InputStream.class));

        ResponseEntity<InputStreamResource> reply =
                controller.zip("scanId");

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testDataImportControllerZipWithBadRequest() {

        DataImportController controller = new DataImportControllerImpl(tracer, importService);

        ResponseEntity<InputStreamResource> reply =
                controller.zip("scanId");

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void testDataImportControllerException() {
        DataImportController controller = new DataImportControllerImpl(tracer, importService);

        MultipartFile multipartFile =
                new MockMultipartFile("file", "test.json", MediaType.APPLICATION_JSON_VALUE,
                        "test file contents".getBytes());

        doThrow(new IOException("Exception occurred"))
                .when(importService)
                .importData(any(ImportRequestBO.class));

        ResponseEntity<String> reply =
                controller.read(multipartFile);

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reply.getBody()).contains("Exception occurred");

        exception.expect(IOException.class);
    }
}
