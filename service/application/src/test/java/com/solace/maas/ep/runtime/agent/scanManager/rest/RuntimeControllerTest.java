package com.solace.maas.ep.runtime.agent.scanManager.rest;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.runtime.agent.TestConfig;
import com.solace.maas.ep.runtime.agent.plugin.route.enumeration.KafkaScanType;
import com.solace.maas.ep.runtime.agent.scanManager.ScanManager;
import com.solace.maas.ep.runtime.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.solace.maas.ep.common.model.ScanType.KAFKA_ALL;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class RuntimeControllerTest {

    @Autowired
    private ScanRequestMapper scanRequestMapper;


    @Test
    public void runtimeControllerTest() {
        ScanManager scanManager = mock(ScanManager.class);

        ScanRequestDTO scanRequestDTO = new ScanRequestDTO(KAFKA_ALL, List.of("topics"), List.of());
        ScanRequestBO scanRequestBO = new ScanRequestBO("id", KAFKA_ALL,
                List.of(KafkaScanType.KAFKA_TOPIC_LISTING.name()), List.of());

        when(scanManager.scan(scanRequestBO))
                .thenReturn(Mockito.anyString());

        RuntimeController controller = new RuntimeControllerImpl(scanRequestMapper, scanManager);

        controller.scan("id", scanRequestDTO);

        assertThatNoException();
    }
}
