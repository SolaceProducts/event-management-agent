package com.solace.maas.ep.event.management.agent.scanManager.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface LivenessController {
    @Operation(
            summary = "An endpoint to check the liveness of the Event Management Agent",
            description = "Use this API to determine if the Event Management Agent process is running and capable of handling REST requests.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The Event Management Agent is available."
                    )
            }
    )
    ResponseEntity<String> liveness();
}
