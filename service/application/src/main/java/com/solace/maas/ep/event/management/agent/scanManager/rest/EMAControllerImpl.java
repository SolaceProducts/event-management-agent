package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.constants.RestEndpoint;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping(RestEndpoint.MESSAGING_SERVICE_URL)
public class EMAControllerImpl implements EMAController {

    private final ScanRequestMapper scanRequestMapper;
    private final ScanManager scanManager;
    private final IDGenerator idGenerator;
    private final EventPortalProperties eventPortalProperties;

    @Autowired
    public EMAControllerImpl(ScanRequestMapper scanRequestMapper, ScanManager scanManager,
                             IDGenerator idGenerator, EventPortalProperties eventPortalProperties) {
        this.scanRequestMapper = scanRequestMapper;
        this.scanManager = scanManager;
        this.idGenerator = idGenerator;
        this.eventPortalProperties = eventPortalProperties;
    }

    @Override
    @PostMapping(value = "/{resourceId}/scan")
    public ResponseEntity<String> scan(@PathVariable(value = "resourceId") String messagingServiceId,
                                       @RequestBody @Valid ScanRequestDTO body) {
        try {
            ScanRequestBO scanRequestBO = scanRequestMapper.map(body);
            scanRequestBO.setMessagingServiceId(messagingServiceId);
            scanRequestBO.setScanId(idGenerator.generateRandomUniqueId());

            boolean isEMAStandalone = eventPortalProperties.getGateway().getMessaging().isStandalone();
            List<String> destinations = scanRequestBO.getDestinations();

            if (!isEMAStandalone) {
                throw new RestErrorHandler.RestException("Scan requests via REST endpoint could not be initiated in connected mode.");
            }

            if (destinations.contains("EVENT_PORTAL")) {
                throw new RestErrorHandler.RestException("Scan data could not be streamed to EP in standalone mode.");
            }

            log.info("Scan request [{}]: Received, request details: {}", scanRequestBO.getScanId(), scanRequestBO);

            String scanId = scanManager.scan(scanRequestBO);

            String message = String.format("Scan request [%s]: Scan started.", scanId);
            log.info(message);

            return ResponseEntity.ok().body(message);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
