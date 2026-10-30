package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Wire DTO for the _internal block on CommandMessage responses from EMA.
 * For service-to-service use only; strip before returning any public REST response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmaInternalPayload implements Serializable {
    private String serviceId;
    private String code;
    private String result;
    /** Internal diagnostic detail. Must not be exposed to end users. */
    private String errorMessage;
}
