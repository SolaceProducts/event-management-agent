package com.solace.maas.ep.runtime.agent.logging;

import org.apache.camel.Exchange;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.spi.UnitOfWorkFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomUnitOfWorkFactory implements UnitOfWorkFactory {
    @Override
    public UnitOfWork createUnitOfWork(Exchange exchange) {
        return new CustomUnitOfWork(exchange);
    }
}