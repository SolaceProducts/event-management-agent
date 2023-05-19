package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.config.eventPortal.GatewayMessagingProperties;
import com.solace.maas.ep.event.management.agent.config.eventPortal.GatewayProperties;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanItemMapper;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class EMAControllerTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private ScanRequestMapper scanRequestMapper;

    @Autowired
    private ScanItemMapper scanItemMapper;


    @Test
    public void testEMAControllerInConnectedMode() {
        ScanManager scanManager = mock(ScanManager.class);
        IDGenerator idGenerator = mock(IDGenerator.class);
        EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);

        ScanRequestDTO scanRequestDTO = new ScanRequestDTO(List.of("topics"), List.of());
        ScanRequestBO scanRequestBO = new ScanRequestBO(
                "id",
                "scanId",
                "traceId",
                List.of("TEST_SCAN"),
                List.of());

        when(eventPortalProperties.getGateway())
                .thenReturn(GatewayProperties.builder()
                        .messaging(GatewayMessagingProperties.builder().standalone(false).build())
                        .build());

        when(scanManager.scan(scanRequestBO))
                .thenReturn("scanId");

        EMAController controller =
                new EMAControllerImpl(scanRequestMapper, scanManager, idGenerator, eventPortalProperties);

        ResponseEntity<String> reply = controller.scan("id", scanRequestDTO);

        // Scan requests via REST endpoints are prevented in connected mode, e.g., standalone=false
        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reply.getBody()).contains("Scan requests via REST endpoint could not be initiated in connected mode.");

        exception.expect(Exception.class);
    }

    @Test
    public void testEMAControllerInStandAloneModeWithEventPortalDestination() {
        ScanManager scanManager = mock(ScanManager.class);
        IDGenerator idGenerator = mock(IDGenerator.class);
        EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);

        ScanRequestDTO scanRequestDTO = new ScanRequestDTO(List.of("topics"), List.of("EVENT_PORTAL"));
        ScanRequestBO scanRequestBO = new ScanRequestBO(
                "id",
                "scanId",
                "traceId",
                List.of("TEST_SCAN"),
                List.of("EVENT_PORTAL"));

        when(eventPortalProperties.getGateway())
                .thenReturn(GatewayProperties.builder()
                        .messaging(GatewayMessagingProperties.builder().standalone(true).build())
                        .build());

        when(scanManager.scan(scanRequestBO))
                .thenReturn("scanId");

        EMAController controller = new EMAControllerImpl(scanRequestMapper, scanManager, idGenerator, eventPortalProperties);

        ResponseEntity<String> reply =
                controller.scan("id", scanRequestDTO);

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reply.getBody()).contains("Scan data could not be streamed to EP in standalone mode.");

        exception.expect(Exception.class);
    }

    @Test
    public void testEMAControllerInStandAloneMode() {
        ScanManager scanManager = mock(ScanManager.class);
        IDGenerator idGenerator = mock(IDGenerator.class);
        EventPortalProperties eventPortalProperties = mock(EventPortalProperties.class);

        ScanRequestDTO scanRequestDTO = new ScanRequestDTO(List.of("topics"), List.of());
        ScanRequestBO scanRequestBO = new ScanRequestBO(
                "id",
                "scanId",
                "traceId",
                List.of("TEST_SCAN"),
                List.of());

        when(eventPortalProperties.getGateway())
                .thenReturn(GatewayProperties.builder()
                        .messaging(GatewayMessagingProperties.builder().standalone(true).build())
                        .build());

        when(scanManager.scan(scanRequestBO))
                .thenReturn("scanId");

        EMAController controller = new EMAControllerImpl(scanRequestMapper, scanManager, idGenerator, eventPortalProperties);

        ResponseEntity<String> reply =
                controller.scan("id", scanRequestDTO);

        assertThat(reply.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reply.getBody()).contains("Scan started.");

        assertThatNoException();
    }
}
