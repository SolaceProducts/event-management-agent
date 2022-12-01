package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientHierarchyEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientHierarchyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmptyScanEntityProcessorImpl extends EmptyScanEntityProcessor {
    private final ProducerTemplate producerTemplate;

    private final ScanRecipientHierarchyRepository scanRecipientHierarchyRepository;

    public EmptyScanEntityProcessorImpl(ProducerTemplate producerTemplate,
                                        ScanRecipientHierarchyRepository scanRecipientHierarchyRepository) {
        this.producerTemplate = producerTemplate;
        this.scanRecipientHierarchyRepository = scanRecipientHierarchyRepository;
    }

    @Transactional
    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);

        log.info("Scan request [{}]: Encountered an empty scan type [{}].", scanId, scanType);

        sendCompleteStatusForScanType(scanId, messagingServiceId, scanType);

        log.info("Scan request [{}]: The status of [{}]" + " is: [{}].", scanId, scanType, ScanStatus.COMPLETE);

        checkScanTypeDescendent(scanId, messagingServiceId, scanType);
    }

    private void checkScanTypeDescendent(String scanId, String messagingServiceId, String scanType) {
        List<ScanRecipientHierarchyEntity> scanRecipientHierarchyEntities =
                scanRecipientHierarchyRepository.findScanRecipientHierarchyEntitiesByScanId(scanId);

        Map<String, String> store = scanRecipientHierarchyEntities.stream()
                .map(ScanRecipientHierarchyEntity::getStore)
                .findFirst().orElseThrow();

        List<String> emptyScanTypesHierarchyPaths = new ArrayList<>(store.values());

        emptyScanTypesHierarchyPaths.forEach(emptyScanTypesPath -> {
            List<String> emptyScanTypes = Arrays.asList(emptyScanTypesPath.split(","));

            if (emptyScanTypes.contains(scanType)) {
                int parentScanTypeIndex = emptyScanTypes.indexOf(scanType);
                List<String> childScanTypes = emptyScanTypes.subList(parentScanTypeIndex + 1, emptyScanTypes.size());

                childScanTypes.forEach(childScanType -> {
                    log.info("Scan request [{}]: Encountered an empty scan type [{}].", scanId, childScanType);
                    sendCompleteStatusForScanType(scanId, messagingServiceId, childScanType);
                    log.info("Scan request [{}]: The status of [{}]" + " is: [{}].", scanId, childScanType, ScanStatus.COMPLETE);
                });
            }
        });
    }

    public void sendCompleteStatusForScanType(String scanId, String messagingServiceId, String scanType) {
        producerTemplate.send("direct:processScanStatusAsComplete?block=false&failIfNoConsumers=false", exchange -> {
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, scanType);
        });
    }
}
