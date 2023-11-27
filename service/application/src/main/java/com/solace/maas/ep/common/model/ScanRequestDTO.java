package com.solace.maas.ep.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.solace.maas.ep.event.management.agent.model.rest.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "ScanRequest")
@SuppressWarnings("PMD")
public class ScanRequestDTO extends BaseDTO {

    @NotNull(message = "Scan type must not be null")
    private List<String> scanTypes;

    @NotNull(message = "Destinations must not be null")
    private List<String> destinations;
}
