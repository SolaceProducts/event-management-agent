package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ScanController {
    @Operation(
            summary = "Retrieves a list of data collections",
            description = "Use this API to retrieve a list of data collections.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The data collection list has been successful."
                    )
            }
    )
    ResponseEntity<Page<ScanItemDTO>> list(Pageable pageable);
}
