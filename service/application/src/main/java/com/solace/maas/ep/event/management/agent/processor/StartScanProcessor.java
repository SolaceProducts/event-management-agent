package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.event.management.agent.scanManager.ScanManager;
import com.solace.maas.ep.event.management.agent.scanManager.model.ScanRequestBO;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartScanProcessor implements Processor {
    private final ScanManager scanManager;

    @Autowired
    public StartScanProcessor(ScanManager scanManager) {
        this.scanManager = scanManager;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        ScanRequestBO scanRequestBO = exchange.getIn().getBody(ScanRequestBO.class);

        scanManager.scan(scanRequestBO);
    }
}
