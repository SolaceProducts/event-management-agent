package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.maas.ep.common.messages.CommandMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandMessageErrorFieldsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void commandMessage_serializesUserMessage() throws Exception {
        CommandMessage msg = new CommandMessage();
        msg.setUserMessage("Broker rejected: ACL Profile limit reached.");

        String json = objectMapper.writeValueAsString(msg);
        assertThat(json).contains("\"userMessage\"");
        assertThat(json).contains("ACL Profile limit reached");
    }

    @Test
    void commandMessage_serializesInternalPayload() throws Exception {
        CommandMessage msg = new CommandMessage();
        EmaInternalPayload internal = EmaInternalPayload.builder()
                .serviceId("ema")
                .code("TERRAFORM_APPLY_ERROR")
                .result("error")
                .errorMessage("exit status 1")
                .build();
        msg.setInternal(internal);

        String json = objectMapper.writeValueAsString(msg);
        assertThat(json).contains("\"_internal\"");
        assertThat(json).contains("TERRAFORM_APPLY_ERROR");
    }

    @Test
    void commandMessage_nullUserMessageOmittedFromJson() throws Exception {
        CommandMessage msg = new CommandMessage();
        msg.setUserMessage(null);

        String json = objectMapper.writeValueAsString(msg);
        assertThat(json).doesNotContain("userMessage");
    }

    @Test
    void commandMessage_deserializesUserMessage() throws Exception {
        String json = "{\"userMessage\":\"Broker rejected: MAX_NUM_EXCEEDED\"}";
        CommandMessage msg = objectMapper.readValue(json, CommandMessage.class);
        assertThat(msg.getUserMessage()).isEqualTo("Broker rejected: MAX_NUM_EXCEEDED");
    }
}
