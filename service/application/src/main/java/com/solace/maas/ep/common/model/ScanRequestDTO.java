package com.solace.maas.ep.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.solace.maas.ep.runtime.agent.model.rest.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
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
    @Schema(enumAsRef = true)
    private ScanType scanType;

    @NotNull(message = "Entity type must not be null")
    private List<String> entityTypes;

    @NotNull(message = "Destinations must not be null")
    private List<String> destinations;
}
