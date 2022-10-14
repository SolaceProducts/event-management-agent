package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.common.model.ScanRequestDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class EMAControllerTest {

    @Autowired
    private ScanRequestMapper scanRequestMapper;


    @Test
    public void EMAControllerTest() {
        ScanManager scanManager = mock(ScanManager.class);

        ScanRequestDTO scanRequestDTO = new ScanRequestDTO(List.of("topics"), List.of());
        ScanRequestBO scanRequestBO = new ScanRequestBO("id", "scanId",
                List.of("TEST_SCAN"), List.of());

        when(scanManager.scan(scanRequestBO))
                .thenReturn(Mockito.anyString());

        EMAController controller = new EMAControllerImpl(scanRequestMapper, scanManager);

        controller.scan("id", scanRequestDTO);

        assertThatNoException();
    }
}
