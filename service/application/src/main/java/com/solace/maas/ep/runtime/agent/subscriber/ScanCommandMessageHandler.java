package com.solace.maas.ep.runtime.agent.subscriber;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.common.model.ScanRequestDTO;
import com.solace.maas.ep.runtime.agent.config.SolaceConfiguration;
import com.solace.maas.ep.runtime.agent.scanManager.ScanManager;
import com.solace.maas.ep.runtime.agent.scanManager.mapper.ScanRequestMapper;
import com.solace.maas.ep.runtime.agent.scanManager.model.ScanRequestBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class ScanCommandMessageHandler extends SolaceMessageHandler<ScanCommandMessage> {

    private final ScanManager scanManager;
    private final ScanRequestMapper scanRequestMapper;

    public ScanCommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber, ScanManager scanManager, ScanRequestMapper scanRequestMapper) {
        super(solaceConfiguration.getTopicPrefix() + "scan/command/>", solaceSubscriber);
        this.scanManager = scanManager;
        this.scanRequestMapper = scanRequestMapper;
    }

    @Override
    public void receiveMessage(String destinationName, ScanCommandMessage message) {
        List<String> destinations = new ArrayList<>();

        log.debug("Received scan command message: {} for messaging service: {}",
                message, message.getMessagingServiceId());

        ScanRequestDTO scanRequestDTO = message.getScanRequest();
        ScanRequestBO scanRequestBO = scanRequestMapper.map(scanRequestDTO);
        scanRequestBO.setMessagingServiceId(message.getMessagingServiceId());

        if (scanRequestBO.getDestinations() == null) {
            destinations.add("DATA_COLLECTION_FILE_WRITER");
        } else if (!scanRequestBO.getDestinations().contains("DATA_COLLECTION_FILE_WRITER")) {
            destinations.add("DATA_COLLECTION_FILE_WRITER");
            destinations.addAll(scanRequestBO.getDestinations());
        }

        List<String> scanRequestDestinations = destinations.stream()
                .distinct()
                .collect(Collectors.toUnmodifiableList());

        scanRequestBO.setDestinations(scanRequestDestinations);

        scanManager.scan(scanRequestBO);
    }
}
