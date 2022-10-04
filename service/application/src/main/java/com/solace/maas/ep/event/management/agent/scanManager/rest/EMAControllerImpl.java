package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
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
@RequestMapping("/api/v2/ema/messagingServices")
public class EMAControllerImpl implements EMAController {

    private final ScanRequestMapper scanRequestMapper;
    private final ScanManager scanManager;

    @Autowired
    public EMAControllerImpl(ScanRequestMapper scanRequestMapper, ScanManager scanManager) {
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

            String result = scanManager.scan(scanRequestBO);

            log.info("Successfully started the scan {}", result);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
