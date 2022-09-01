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
        log.debug("Received scan command message: {}\n{}", message.getMessagingServiceId(), message);
        ScanRequestDTO scanRequestDTO = message.getScanRequest();
        ScanRequestBO scanRequestBO = scanRequestMapper.map(scanRequestDTO);
        scanRequestBO.setMessagingServiceId(message.getMessagingServiceId());
        scanManager.scan(scanRequestBO);
    }
}
