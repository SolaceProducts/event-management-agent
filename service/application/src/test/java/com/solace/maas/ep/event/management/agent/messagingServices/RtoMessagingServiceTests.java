package com.solace.maas.ep.event.management.agent.messagingServices;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.RtoMessageBuilder;
import com.solace.messaging.resources.Topic;
import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class RtoMessagingServiceTests {

    @Mock
    SessionHandle sessionHandle;

    @Mock
    MessageCallback messageCallback;

    @Mock
    SessionEventCallback sessionEventCallback;

    @DisabledIf(
            expression = "#{systemProperties['os.name'].toLowerCase().contains('mac')}",
            reason = "Disabled on Mac OS"
    )
    @SneakyThrows
    @Test
    public void RtoMessagingService_Create_Context_And_Session_Then_Connect() {
        RtoMessagingService.createRtoMessagingServiceBuilder()
                .fromProperties(new ArrayList<>())
                .createContext()
                .createSession();

        assertThatNoException();

        RtoMessageBuilder.RtoMessageBuilderImpl rtoMessageBuilderImpl =
                new RtoMessageBuilder.RtoMessageBuilderImpl(messageCallback, sessionEventCallback);

        rtoMessageBuilderImpl.setSessionHandle(sessionHandle);
        when(sessionHandle.connect()).thenReturn(SolEnum.ReturnCode.OK);

        rtoMessageBuilderImpl.connect();
        rtoMessageBuilderImpl.publish(Mockito.anyString(), Topic.of("test"));
        rtoMessageBuilderImpl.destroy();

        assertThatNoException();
    }
}
