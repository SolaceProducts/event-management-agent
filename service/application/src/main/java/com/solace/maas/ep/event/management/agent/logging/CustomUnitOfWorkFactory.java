package com.solace.maas.ep.event.management.agent.logging;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import org.apache.camel.Exchange;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.spi.UnitOfWorkFactory;
import org.springframework.stereotype.Component;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Component
public class CustomUnitOfWorkFactory implements UnitOfWorkFactory {
    @Override
    public UnitOfWork createUnitOfWork(Exchange exchange) {
        return new CustomUnitOfWork(exchange);
    }
}