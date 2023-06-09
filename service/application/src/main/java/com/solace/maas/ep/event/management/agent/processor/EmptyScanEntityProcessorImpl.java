package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientHierarchyEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientHierarchyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmptyScanEntityProcessorImpl extends EmptyScanEntityProcessor {

    private final ScanRecipientHierarchyRepository scanRecipientHierarchyRepository;

    public EmptyScanEntityProcessorImpl(ScanRecipientHierarchyRepository scanRecipientHierarchyRepository) {
        super();
        this.scanRecipientHierarchyRepository = scanRecipientHierarchyRepository;
    }

    @Transactional
    @Override
    public void process(Exchange exchange) throws Exception {
        List<String> emptyScanTypes = new ArrayList<>();
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String traceId = (String) properties.get(RouteConstants.TRACE_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);

        log.info("Scan request [{}], trace ID [{}]: Encountered an empty scan type [{}].", scanId, traceId, scanType);

        emptyScanTypes.add(scanType);

        List<String> emptyDescendents = checkScanTypeDescendents(scanId, traceId, scanType);
        emptyScanTypes.addAll(emptyDescendents);

        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, emptyScanTypes);
    }

    private List<String> checkScanTypeDescendents(String scanId, String traceId, String scanType) {
        List<String> emptyDescendentsForScanType = new ArrayList<>();
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
                    log.info("Scan request [{}], trace ID [{}]: Encountered an empty scan type [{}].", scanId, traceId, childScanType);
                    emptyDescendentsForScanType.add(childScanType);
                });
            }
        });

        return emptyDescendentsForScanType;
    }
}
