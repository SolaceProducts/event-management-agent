package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientHierarchyEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientHierarchyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ScanTypeDescendentsProcessorImpl implements ScanTypeDescendentsProcessor {

    private final ScanRecipientHierarchyRepository scanRecipientHierarchyRepository;

    public ScanTypeDescendentsProcessorImpl(ScanRecipientHierarchyRepository scanRecipientHierarchyRepository) {
        super();
        this.scanRecipientHierarchyRepository = scanRecipientHierarchyRepository;
    }

    @Transactional
    @Override
    public void process(Exchange exchange) throws Exception {
        List<String> allDescendentScanTypes = new ArrayList<>();
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String traceId = (String) properties.get(RouteConstants.TRACE_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);
        boolean isEmptyScanTypes = (boolean) properties.get(RouteConstants.IS_EMPTY_SCAN_TYPES);

        allDescendentScanTypes.add(scanType);

        List<String> scanTypeDescendents = checkScanTypeDescendents(scanId, scanType);
        allDescendentScanTypes.addAll(scanTypeDescendents);

        if (isEmptyScanTypes) {
            allDescendentScanTypes.forEach(type ->
                    log.info("Scan request [{}], trace ID [{}]: Encountered an empty scan type [{}].", scanId, traceId, type));
        }

        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, allDescendentScanTypes);
    }

    private List<String> checkScanTypeDescendents(String scanId, String scanType) {
        List<String> descendentsForScanType = new ArrayList<>();
        List<ScanRecipientHierarchyEntity> scanRecipientHierarchyEntities =
                scanRecipientHierarchyRepository.findScanRecipientHierarchyEntitiesByScanId(scanId);

        Map<String, String> store = scanRecipientHierarchyEntities.stream()
                .map(ScanRecipientHierarchyEntity::getStore)
                .findFirst().orElseThrow();

        List<String> descendentScanTypePaths = new ArrayList<>(store.values());

        descendentScanTypePaths.forEach(descendentScanTypePath -> {
            List<String> descendentScanTypes = Arrays.asList(descendentScanTypePath.split(","));

            if (descendentScanTypes.contains(scanType)) {
                int parentScanTypeIndex = descendentScanTypes.indexOf(scanType);
                List<String> childScanTypes = descendentScanTypes.subList(parentScanTypeIndex + 1, descendentScanTypes.size());

                descendentsForScanType.addAll(childScanTypes);
            }
        });

        return descendentsForScanType;
    }
}
