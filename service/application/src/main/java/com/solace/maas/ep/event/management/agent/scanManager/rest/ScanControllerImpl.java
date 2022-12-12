package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.constants.RestEndpoint;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanItemMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping(RestEndpoint.SCAN_URL)
public class ScanControllerImpl implements ScanController {

    private final ScanManager scanManager;
    private final ScanItemMapper scanItemMapper;

    @Autowired
    public ScanControllerImpl(ScanManager scanManager,
                              ScanItemMapper scanItemMapper) {
        this.scanManager = scanManager;
        this.scanItemMapper = scanItemMapper;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ScanItemDTO>> list() {

        return ResponseEntity.ok().body(scanManager.listScans().stream()
                .map(scanItemMapper::map)
                .collect(Collectors.toUnmodifiableList()));
    }
}
