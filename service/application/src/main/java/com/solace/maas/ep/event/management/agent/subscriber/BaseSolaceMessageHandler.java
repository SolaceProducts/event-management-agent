package com.solace.maas.ep.event.management.agent.subscriber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.solace.maas.ep.event.management.agent.constants.Command;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.EnumDeserializer;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPSvcType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseSolaceMessageHandler {

    private static final SimpleModule module = new SimpleModule();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Register all the enums we expect to cross versions and allow them to be mapped to null instead of throwing an exception if unknown
        module.addDeserializer(MOPMessageType.class, new EnumDeserializer<>(MOPMessageType.class));
        module.addDeserializer(MOPProtocol.class, new EnumDeserializer<>(MOPProtocol.class));
        module.addDeserializer(MOPSvcType.class, new EnumDeserializer<>(MOPSvcType.class));
        module.addDeserializer(MOPUHFlag.class, new EnumDeserializer<>(MOPUHFlag.class));

        objectMapper.registerModule(module);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    protected static void setupMDC(String messageAsString, String receivedClassName) throws JsonProcessingException {
        List<String> scanClassNames = List.of("ScanCommandMessage", "ScanDataImportMessage");
        List<String> commandClassNames = List.of("CommandMessage");

        Map<String, String> map = objectMapper.readValue(messageAsString, Map.class);

        MDC.clear();
        MDC.put(RouteConstants.TRACE_ID, map.get("traceId"));
        MDC.put(RouteConstants.X_B_3_TRACE_ID, map.get("traceId"));
        MDC.put(RouteConstants.ACTOR_ID, map.get("actorId"));

        if (scanClassNames.contains(receivedClassName)) {
            MDC.put(RouteConstants.SCAN_ID, map.get("scanId"));
            MDC.put(RouteConstants.MESSAGING_SERVICE_ID, map.get("messagingServiceId"));
        }

        if (commandClassNames.contains(receivedClassName)) {
            MDC.put(RouteConstants.COMMAND_CORRELATION_ID, map.get(Command.COMMAND_CORRELATION_ID));
            MDC.put(RouteConstants.MESSAGING_SERVICE_ID, map.get("serviceId"));
        }
    }

    protected Object toMessage(String messageAsString,
                               Class messageClass) throws JsonProcessingException {
        return objectMapper.readValue(messageAsString, messageClass);
    }
}
