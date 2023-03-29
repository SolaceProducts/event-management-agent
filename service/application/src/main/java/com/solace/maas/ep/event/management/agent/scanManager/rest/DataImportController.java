package com.solace.maas.ep.event.management.agent.scanManager.rest;


import com.solace.maas.ep.common.model.EventErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DataImportController {

    @Operation(
            summary = "Imports data collection files",
            description = "Use this API to trigger manual import of data collection for the specified resource.",
            parameters = {
                    @Parameter(name = "file", description = "The scan data zip file to be imported.", in = ParameterIn.QUERY)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Scan data is imported successfully."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = EventErrorDTO.class), mediaType = "application/json"),
                            description = "Bad request."
                    )
            }
    )
    ResponseEntity<String> read(MultipartFile file);

    @Operation(
            summary = "Zips data collection files",
            description = "Use this API to zip the data collection files for the specified resource.",
            parameters = {
                    @Parameter(name = "scanId", description = "The ID of the scan request.", required = true),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Scan data is zipped successfully."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = EventErrorDTO.class), mediaType = "application/json"),
                            description = "Bad request."
                    )
            }
    )
    ResponseEntity<InputStreamResource> zip(String scanId);
}
