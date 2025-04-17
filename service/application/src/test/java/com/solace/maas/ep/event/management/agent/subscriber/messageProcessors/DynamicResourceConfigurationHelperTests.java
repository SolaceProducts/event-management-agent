package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.common.model.EventBrokerResourceConfiguration;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import com.solace.maas.ep.event.management.agent.service.SolaceResourceConfigurationToEventConverter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@ActiveProfiles("TEST")
class DynamicResourceConfigurationHelperTests {

    @MockitoSpyBean
    private SolaceResourceConfigurationToEventConverter solaceResourceConfigurationToEventConverter;

    @MockitoSpyBean
    private MessagingServiceDelegateServiceImpl messagingServiceDelegateServiceImpl;

    @MockitoSpyBean
    private DynamicResourceConfigurationHelper helper;


    @Test
    void emptyResourceListResultsInNoOperation() {
        helper.loadSolaceBrokerResourceConfigurations(List.of());
        Mockito.verifyNoInteractions(messagingServiceDelegateServiceImpl);
        Mockito.verifyNoInteractions(solaceResourceConfigurationToEventConverter);
    }


    @Test
    void testLoadSolaceBrokerResourceConfigurations() {

        List<EventBrokerResourceConfiguration> resources = List.of(
                EventBrokerResourceConfigTestHelper.buildResourceConfiguration(ResourceConfigurationType.SOLACE)
        );
        helper.loadSolaceBrokerResourceConfigurations(resources);
        verify(messagingServiceDelegateServiceImpl, times(1)).upsertMessagingServiceEvents(any());
    }


}
