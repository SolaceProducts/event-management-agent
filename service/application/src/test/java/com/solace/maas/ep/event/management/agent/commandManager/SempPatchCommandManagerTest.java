package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.RestDeliveryPointApi;
import com.solace.client.sempv2.model.MsgVpnRestDeliveryPointRestConsumer;
import com.solace.client.sempv2.model.MsgVpnRestDeliveryPointRestConsumerResponse;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.SempPatchCommandManager;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.solace.maas.ep.common.model.SempEntityType.solaceRdpRestConsumer;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempPatchCommandConstants.SEMP_PATCH_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempPatchCommandConstants.SEMP_PATCH_ENTITY_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempPatchCommandConstants.SEMP_PATCH_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class SempPatchCommandManagerTest {

    private final static String DIR_SEMP_RESOURCES = "src/test/resources/sempResponses/";
    private final static String SEMP_RESPONSE_MISSING_RESOURCE = "sempResponseMissingResource.json";

    @SpyBean
    private SempPatchCommandManager sempPatchCommandManager;

    private SempApiProvider sempApiProvider;


    @BeforeEach
    void reset() throws ApiException {
        sempApiProvider = Mockito.mock(SempApiProvider.class);
    }

    @Test
    void testPatchRdpRestConsumer() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(any(), any(), any(), any(), any(), any()))
                .thenReturn(new MsgVpnRestDeliveryPointRestConsumerResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_PATCH_OPERATION)
                .parameters(createPatchRequestRestConsumer(true, true))
                .build();
        sempPatchCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(eq("default"), eq("someRdp"), eq("someRdp REST Consumer"), any(), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void testPatchRdpRestConsumerWithValidationException() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(any(), any(), any(), any(), any(), any()))
                .thenReturn(new MsgVpnRestDeliveryPointRestConsumerResponse());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_PATCH_OPERATION)
                .parameters(createPatchRequestRestConsumer(false, true))
                .build();
        sempPatchCommandManager.execute(cmd, sempApiProvider);
        verifyNoInteractions(rdpApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testPatchRdpRestConsumerWithException() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(any(), any(), any(), any(), any(), any()))
                .thenThrow(createaServerErrorApiException());
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_PATCH_OPERATION)
                .parameters(createPatchRequestRestConsumer(true, true))
                .build();
        sempPatchCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(eq("default"), eq("someRdp"), eq("someRdp REST Consumer"), any(), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testNotFoundDeleteRdpRestConsumer() throws ApiException {
        RestDeliveryPointApi rdpApi = Mockito.mock(RestDeliveryPointApi.class);
        when(sempApiProvider.getRestDeliveryPointApi()).thenReturn(rdpApi);
        when((rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(any(), any(), any(), any(), any(), any()))
                .thenThrow(createaNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));
        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_PATCH_OPERATION)
                .parameters(createPatchRequestRestConsumer(true, true))
                .build();
        sempPatchCommandManager.execute(cmd, sempApiProvider);
        verify(rdpApi).updateMsgVpnRestDeliveryPointRestConsumer(eq("default"), eq("someRdp"), eq("someRdp REST Consumer"), any(), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    // helpers

    //CPD-OFF
    private Map<String, Object> createPatchRequestRestConsumer(boolean valid, boolean enabled) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_PATCH_ENTITY_TYPE, solaceRdpRestConsumer.name());

        Map<String, Object> data = new HashMap<>();
        data.put("msgVpn", "default");
        data.put("rdpName", "someRdp");
        data.put("restConsumerName", "someRdp REST Consumer");
        if (valid) {
            MsgVpnRestDeliveryPointRestConsumer restConsumer = new MsgVpnRestDeliveryPointRestConsumer();
            restConsumer.setEnabled(enabled);
            data.put(SEMP_PATCH_DATA, restConsumer);
        }
        parameters.put(SEMP_PATCH_DATA, data);
        return parameters;
    }

    private ApiException createaServerErrorApiException() {
        return new ApiException(500, "", new HashMap<>(), "{\"some\":\"error_message\"}");
    }

    private ApiException createaNotFoundApiException(String resourceName) {
        return new ApiException(400, "", new HashMap<>(), readSempResponseResource(resourceName));
    }

    private String readSempResponseResource(String resourceName) {
        try {
            return Files.readString(Path.of(DIR_SEMP_RESOURCES + resourceName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //CPD-ON
}
