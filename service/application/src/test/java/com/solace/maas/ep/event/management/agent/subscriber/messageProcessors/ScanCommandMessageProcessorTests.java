package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.common.model.ScanDestination;
import com.solace.maas.ep.common.model.ScanType;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@ActiveProfiles("TEST")
class ScanCommandMessageProcessorTests {

    @MockBean
    private ScanManager scanManager;

    @SpyBean
    private ScanCommandMessageProcessor scanCommandMessageProcessor;

    @MockBean
    private DynamicResourceConfigurationHelper dynamicResourceConfigurationHelper;

    @Test
    void processMessageWithoutResourceConfiguration(){
        ScanCommandMessage message = buildScanCommandMessage(null);
        scanCommandMessageProcessor.processMessage(message);
        verifyNoInteractions(dynamicResourceConfigurationHelper);
        verify(scanManager,times(1)).scan(any());
    }

    @Test
    void processMessageWithResourceConfiguration(){
        ScanCommandMessage message = buildScanCommandMessage(List.of(
                EventBrokerResourceConfigTestHelper.buildResourceConfiguration(ResourceConfigurationType.SOLACE))
        );
        scanCommandMessageProcessor.processMessage(message);
        verify(dynamicResourceConfigurationHelper, times(1)).loadSolaceBrokerResourceConfigurations(any());
        verify(scanManager,times(1)).scan(any());
    }


    private ScanCommandMessage buildScanCommandMessage(List<EventBrokerResourceConfiguration> resources){
        return new ScanCommandMessage(
                "messageServiceId",
                "scanId",
                List.of(ScanType.SOLACE_ALL),
                List.of(ScanDestination.EVENT_PORTAL),
                resources);
    }
}
