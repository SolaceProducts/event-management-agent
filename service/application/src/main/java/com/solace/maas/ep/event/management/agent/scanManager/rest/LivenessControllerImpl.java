package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.constants.RestEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping(RestEndpoint.LIVENESS_URL)
public class LivenessControllerImpl implements LivenessController {

    @Override
    @GetMapping
    public ResponseEntity<String> liveness() {
        return ResponseEntity.ok().body("EMA is alive");
    }
}
