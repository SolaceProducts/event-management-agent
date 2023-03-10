package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanDataCollectionTypesMessage;
import com.solace.maas.ep.common.messages.ScanTypesMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.MessagingServiceRouteDelegate;
import com.solace.maas.ep.event.management.agent.publisher.ScanDataCollectionTypesPublisher;
import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class GetScanDataCollectionTypesMessageHandler extends SolaceMessageHandler<ScanDataCollectionTypesMessage> {

    private final ScanManager scanManager;
    private final ScanDataCollectionTypesPublisher scanDataCollectionTypesPublisher;

    public GetScanDataCollectionTypesMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber,
            ScanManager scanManager,
            ScanDataCollectionTypesPublisher scanDataCollectionTypesPublisher) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/v1/getScanDataCollectionTypes/>", solaceSubscriber);
        this.scanManager = scanManager;
        this.scanDataCollectionTypesPublisher = scanDataCollectionTypesPublisher;
    }

    @Override
    public void receiveMessage(String destinationName, ScanDataCollectionTypesMessage message) {

        List<String> entityTypes = new ArrayList<>();
        List<RouteBundle> routeBundles;
        List<String> extractedScanTypes = new ArrayList<>();
        Map<String, String> topicDetails = new HashMap<>();
        String orgId = message.getOrgId();
        String messagingServiceId = message.getMessagingServiceId();

        log.debug("Received getScanDataCollectionTypes command message: {} for messaging service: {}",
                message, messagingServiceId);

        message.getScanTypes().forEach(scanType -> entityTypes.add(scanType.name()));

        MessagingServiceRouteDelegate scanDelegate = scanManager.getScanDelegate(messagingServiceId);
        routeBundles = scanManager.getRouteBundles(entityTypes, List.of(), messagingServiceId, scanDelegate);

        extractScanTypes(routeBundles, extractedScanTypes);

        topicDetails.put("orgId", orgId);
        topicDetails.put("emaId", message.getEmaId());
        topicDetails.put("messagingServiceId", messagingServiceId);

        ScanTypesMessage scanTypesMessage = new ScanTypesMessage(orgId, message.getScanId(), messagingServiceId, extractedScanTypes);

        log.debug("Will publish scanTypesMessage: {}", scanTypesMessage);

        scanDataCollectionTypesPublisher.sendScanDataCollectionTypes(scanTypesMessage, topicDetails);
    }

    private void extractScanTypes(List<RouteBundle> routeBundles, List<String> extractedRouteBundles) {
        for (RouteBundle r : routeBundles) {
            extractedRouteBundles.add(r.getScanType());
            extractScanTypes(r.getRecipients(), extractedRouteBundles);
        }
    }
}
