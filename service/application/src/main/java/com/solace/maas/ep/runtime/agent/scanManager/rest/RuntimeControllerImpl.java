package com.solace.maas.ep.runtime.agent.scanManager.rest;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.runtime.agent.scanManager.ScanManager;
import com.solace.maas.ep.runtime.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v2/runtime/messagingServices")
public class RuntimeControllerImpl implements RuntimeController {

    private final ScanRequestMapper scanRequestMapper;
    private final ScanManager scanManager;

    @Autowired
    public RuntimeControllerImpl(ScanRequestMapper scanRequestMapper, ScanManager scanManager) {
        this.scanRequestMapper = scanRequestMapper;
        this.scanManager = scanManager;
    }

    @Override
    @PostMapping(value = "{messagingServiceId}/scan")
    public ResponseEntity<String> scan(@PathVariable(value = "messagingServiceId") String messagingServiceId,
                                       @RequestBody @Valid ScanRequestDTO body) {
        try {
            ScanRequestBO scanRequestBO = scanRequestMapper.map(body);
            scanRequestBO.setMessagingServiceId(messagingServiceId);
            scanRequestBO.setScanId(UUID.randomUUID().toString());

            log.info("Received scan request {}. Request details: {}", scanRequestBO.getScanId(), scanRequestBO);

            String scanResult = scanManager.scan(scanRequestBO);

            String message = String.format("started scan request %s.", scanResult);
            log.info(message);

            return ResponseEntity.ok().body(message);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
