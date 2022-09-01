package com.solace.maas.ep.runtime.agent.scanManager.rest;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public interface RuntimeController {

    @Operation(
            summary = "Triggers data collections",
            description = "Use this API to trigger a data collection for the specified messaging service.",
            parameters = {@Parameter(description = "The ID of the messaging service.", name = "messagingServiceId", required = true)},
            requestBody = @RequestBody(description = "The scan request object.",
                    content = @Content(schema = @Schema(implementation = ScanRequestDTO.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The scan has been triggered."
                    )
            }
    )
    ResponseEntity<String> scan(String messagingServiceId, @Valid ScanRequestDTO body);
}
