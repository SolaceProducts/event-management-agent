package com.solace.maas.ep.event.management.agent.scanManager.rest;


import com.solace.maas.ep.common.model.EventErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DataImportController {

    @Operation(
            summary = "Imports data collection files",
            description = "Use this API to trigger manual import of data collection for the specified messaging service.",
            parameters = {
                    @Parameter(name = "messagingServiceId", description = "The ID of the messaging service.", required = true),
                    @Parameter(name = "file", description = "The scan data zip file to be imported.", in = ParameterIn.QUERY),
                    @Parameter(name = "scheduleId", description = "The ID of the scan request group.", in = ParameterIn.QUERY),
                    @Parameter(name = "scanId", description = "The ID of the scan request.", in = ParameterIn.QUERY)
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
    ResponseEntity<String> read(String messagingServiceId, MultipartFile file, String scheduleId, String scanId);

    @Operation(
            summary = "Zips data collection files",
            description = "Use this API to zip the data collection files for the specified messaging service.",
            parameters = {
                    @Parameter(name = "messagingServiceId", description = "The ID of the messaging service.", required = true),
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
    ResponseEntity<String> zip(String messagingServiceId, String scanId);
}
