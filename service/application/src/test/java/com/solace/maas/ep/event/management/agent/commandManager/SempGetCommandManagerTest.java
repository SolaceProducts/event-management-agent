package com.solace.maas.ep.event.management.agent.commandManager;

import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.ClientProfileApi;
import com.solace.client.sempv2.model.MsgVpnClientProfileResponse;
import com.solace.maas.ep.common.model.SempClientProfileValidationRequest;
import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.command.SempGetCommandManager;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandType;
import com.solace.maas.ep.event.management.agent.plugin.command.model.JobStatus;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.solace.maas.ep.common.model.SempEntityType.solaceClientProfileName;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants.SEMP_COMMAND_DATA;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE;
import static com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants.SEMP_GET_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class SempGetCommandManagerTest {

    private static final String DIR_SEMP_RESOURCES = "src/test/resources/sempResponses/";
    private static final String SEMP_RESPONSE_MISSING_RESOURCE = "sempResponseMissingResource.json";

    @MockitoSpyBean
    private SempGetCommandManager sempGetCommandManager;

    private SempApiProvider sempApiProvider;

    @BeforeEach
    void reset() {
        sempApiProvider = Mockito.mock(SempApiProvider.class);
    }

    @Test
    void happyPath() throws ApiException {
        ClientProfileApi clientProfileApi = Mockito.mock(ClientProfileApi.class);
        when(sempApiProvider.getClientProfileApi()).thenReturn(clientProfileApi);
        when(clientProfileApi.getMsgVpnClientProfile(any(), any(), any(), any()))
                .thenReturn(new MsgVpnClientProfileResponse());

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(createClientProfileGetParameters(true))
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        verify(clientProfileApi).getMsgVpnClientProfile(eq("default"), eq("testClientProfile"), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.success);
    }

    @Test
    void withValidationException() {
        ClientProfileApi clientProfileApi = Mockito.mock(ClientProfileApi.class);
        when(sempApiProvider.getClientProfileApi()).thenReturn(clientProfileApi);

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(createClientProfileGetParameters(false))
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        verifyNoInteractions(clientProfileApi);
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void withNotFoundException() throws ApiException {
        ClientProfileApi clientProfileApi = Mockito.mock(ClientProfileApi.class);
        when(sempApiProvider.getClientProfileApi()).thenReturn(clientProfileApi);
        when(clientProfileApi.getMsgVpnClientProfile(any(), any(), any(), any()))
                .thenThrow(createNotFoundApiException(SEMP_RESPONSE_MISSING_RESOURCE));

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(createClientProfileGetParameters(true))
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        verify(clientProfileApi).getMsgVpnClientProfile(eq("default"), eq("testClientProfile"), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.validation_error);
        assertThat(cmd.getResult().getResult()).containsKey(SempCommandConstants.VALIDATION_ERROR_MESSAGE);
    }

    @Test
    void withServerErrorException() throws ApiException {
        ClientProfileApi clientProfileApi = Mockito.mock(ClientProfileApi.class);
        when(sempApiProvider.getClientProfileApi()).thenReturn(clientProfileApi);
        when(clientProfileApi.getMsgVpnClientProfile(any(), any(), any(), any()))
                .thenThrow(createServerErrorApiException());

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(createClientProfileGetParameters(true))
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        verify(clientProfileApi).getMsgVpnClientProfile(eq("default"), eq("testClientProfile"), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testExtractResourceNameWithException() {
        // Create a Command with parameters that will cause extractResourceName to throw an exception
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceClientProfileName.name());
        // Put an object that will cause an exception when trying to extract resource name
        parameters.put(SEMP_COMMAND_DATA, new Object()); // Not a valid request object

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(parameters)
                .build();

        // Execute the command
        sempGetCommandManager.execute(cmd, sempApiProvider);

        // Verify the command failed with error
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
    }

    @Test
    void testExecuteSempCommandWithNullEntityType() {
        // Create a Command with null entity type
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_DATA, new HashMap<>());
        // Explicitly set entity type to null
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, null);

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(parameters)
                .build();

        // Execute the command
        sempGetCommandManager.execute(cmd, sempApiProvider);

        // Verify the command failed with error
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        // Check the logs for the error message
        assertThat(cmd.getResult().getLogs()).isNotEmpty();
        assertThat(cmd.getResult().getLogs().get(0).get("message"))
                .isEqualTo("Semp request must be against a specific semp entity type");
    }

    @Test
    void testExecuteSempCommandWithInvalidEntityType() {
        // Create a Command with invalid entity type
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_DATA, new HashMap<>());
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, "nonExistentEntityType");

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(parameters)
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        // Verify the command failed with error
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        // Check the logs for the error message
        assertThat(cmd.getResult().getLogs()).isNotEmpty();
        assertThat(cmd.getResult().getLogs().get(0).get("message"))
                .isEqualTo("Unsupported SEMP get entity type: nonExistentEntityType");
    }

    @Test
    void testClientProfileValidationErrorEmptyMsgVpn() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceClientProfileName.name());

        SempClientProfileValidationRequest request = SempClientProfileValidationRequest.builder()
                .msgVpn("") // Empty msgVpn
                .clientProfileName("testProfile")
                .build();

        parameters.put(SEMP_COMMAND_DATA, request);

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(parameters)
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.error);
        assertThat(cmd.getResult().getLogs().get(0).get("message").toString())
                .contains("Msg VPN must not be empty");
    }

    @Test
    void with400Exception() throws ApiException {
        ClientProfileApi clientProfileApi = Mockito.mock(ClientProfileApi.class);
        when(sempApiProvider.getClientProfileApi()).thenReturn(clientProfileApi);
        when(clientProfileApi.getMsgVpnClientProfile(any(), any(), any(), any()))
                .thenThrow(new ApiException(400, "Bad Request", new HashMap<>(),
                        "{\"meta\":{\"error\":{\"status\":\"NOT_FOUND\",\"code\":400}}}"));

        Command cmd = Command.builder()
                .commandType(CommandType.semp)
                .command(SEMP_GET_OPERATION)
                .parameters(createClientProfileGetParameters(true))
                .build();

        sempGetCommandManager.execute(cmd, sempApiProvider);

        verify(clientProfileApi).getMsgVpnClientProfile(eq("default"), eq("testClientProfile"), any(), any());
        assertThat(cmd.getResult().getStatus()).isEqualTo(JobStatus.validation_error);
    }

    private Map<String, Object> createClientProfileGetParameters(boolean valid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SEMP_COMMAND_ENTITY_TYPE, solaceClientProfileName.name());

        SempClientProfileValidationRequest request = SempClientProfileValidationRequest.builder()
                .msgVpn("default")
                .clientProfileName(valid ? "testClientProfile" : null)
                .build();

        parameters.put(SEMP_COMMAND_DATA, request);
        return parameters;
    }

    private ApiException createServerErrorApiException() {
        return new ApiException(500, "", new HashMap<>(), "{\"some\":\"error_message\"}");
    }

    private ApiException createNotFoundApiException(String resourceName) {
        return new ApiException(404, "", new HashMap<>(), readSempResponseResource(resourceName));
    }

    private String readSempResponseResource(String resourceName) {
        try {
            return Files.readString(Path.of(DIR_SEMP_RESOURCES + resourceName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}