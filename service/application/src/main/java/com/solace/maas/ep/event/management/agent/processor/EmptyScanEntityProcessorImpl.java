package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.EmptyScanEntityProcessor;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanRecipientsPathEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.ScanRecipientStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EmptyScanEntityProcessorImpl extends EmptyScanEntityProcessor {

    private final ScanRecipientStoreRepository scanRecipientStoreRepository;

    public EmptyScanEntityProcessorImpl(ScanRecipientStoreRepository scanRecipientStoreRepository) {
        this.scanRecipientStoreRepository = scanRecipientStoreRepository;
    }

    @Transactional
    @Override
    public void process(Exchange exchange) throws Exception {
        List<String> emptyScanTypes = new ArrayList<>();
        Map<String, Object> properties = exchange.getIn().getHeaders();

        String scanId = (String) properties.get(RouteConstants.SCAN_ID);
        String scanType = (String) properties.get(RouteConstants.SCAN_TYPE);

        log.info("Scan request [{}]: Encountered an empty scan type [{}].", scanId, scanType);

        emptyScanTypes.add(scanType);

        List<String> emptyDescendents = checkScanTypeDescendents(scanId, scanType);
        emptyScanTypes.addAll(emptyDescendents);

        exchange.getIn().setHeader(RouteConstants.SCAN_TYPE, emptyScanTypes);
    }

    private List<String> checkScanTypeDescendents(String scanId, String scanType) {
        List<String> emptyDescendentsForScanType = new ArrayList<>();
        List<ScanRecipientsPathEntity> scanRecipientPathEntities =
                scanRecipientStoreRepository.findScanRecipientPathEntitiesByScanId(scanId);

        List<String> emptyScanTypesPaths = scanRecipientPathEntities.stream()
                .map(ScanRecipientsPathEntity::getPath)
                .collect(Collectors.toUnmodifiableList());

        emptyScanTypesPaths.forEach(emptyScanTypesPath -> {
            List<String> emptyScanTypes = Arrays.asList(emptyScanTypesPath.split(","));

            if (emptyScanTypes.contains(scanType)) {
                int parentScanTypeIndex = emptyScanTypes.indexOf(scanType);
                List<String> childScanTypes = emptyScanTypes.subList(parentScanTypeIndex + 1, emptyScanTypes.size());

                childScanTypes.forEach(childScanType -> {
                    log.info("Scan request [{}]: Encountered an empty scan type [{}].", scanId, childScanType);
                    emptyDescendentsForScanType.add(childScanType);
                });
            }
        });
        return emptyDescendentsForScanType;
    }
}
