package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.event.management.agent.constants.RestEndpoint;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.DataCollectionFileMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.DataCollectionFileDTO;
import com.solace.maas.ep.event.management.agent.service.DataCollectionFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping(RestEndpoint.DATA_COLLECTION_FILE_URL)
public class DataCollectionFileControllerImpl implements DataCollectionFileController {
    private final DataCollectionFileService dataCollectionFileService;
    private final DataCollectionFileMapper dataCollectionFileMapper;

    public DataCollectionFileControllerImpl(DataCollectionFileService dataCollectionFileService,
                                            DataCollectionFileMapper dataCollectionFileMapper) {
        this.dataCollectionFileService = dataCollectionFileService;
        this.dataCollectionFileMapper = dataCollectionFileMapper;
    }

    @Override
    @GetMapping("/query")
    public ResponseEntity<Page<DataCollectionFileDTO>> list(@RequestParam("scanId") String scanId, Pageable pageable) {
        return ResponseEntity.ok().body(dataCollectionFileService.findByScanId(scanId, pageable)
                .map(dataCollectionFileMapper::map));
    }
}
