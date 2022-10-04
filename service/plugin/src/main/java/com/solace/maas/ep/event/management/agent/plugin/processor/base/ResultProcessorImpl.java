package com.solace.maas.ep.event.management.agent.plugin.processor.base;

import org.apache.camel.Exchange;

/**
 * ResultProcessorImpl is responsible for handling the Camel Exchange.
 *
 * @param <T> The Type definition of the Data Collection Object. In most cases this should
 *           be a Collection of the Templated Type.
 */
public abstract class ResultProcessorImpl<T, Y> implements ResultProcessor<T, Y> {
    @Override
    public void process(Exchange exchange) throws Exception {
        @SuppressWarnings("unchecked")
        T result = handleEvent(exchange.getIn().getHeaders(), (Y) exchange.getIn().getBody());

        exchange.getIn().setBody(result);
    }
}
