package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Wire DTO for the _internal block on CommandMessage responses from EMA.
 * For service-to-service use only. The entire payload must not be exposed to end users;
 * strip before returning any public REST response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmaInternalPayload implements Serializable {
    private String correlationId;
    private String serviceId;
    private int code;
    private String result;
    private String errorMessage;
}
