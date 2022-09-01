package com.solace.maas.ep.runtime.agent.logging;


import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.engine.DefaultUnitOfWork;
import org.apache.camel.spi.UnitOfWork;
import org.slf4j.MDC;

public class CustomUnitOfWork extends DefaultUnitOfWork implements UnitOfWork {
    public CustomUnitOfWork(Exchange exchange) {
        super(exchange);
    }

    @Override
    public AsyncCallback beforeProcess(Processor processor, Exchange exchange, AsyncCallback callback) {
        return new MyMDCCallback(callback);
    }


    private static final class MyMDCCallback implements AsyncCallback {
        private final AsyncCallback delegate;
        private final String breadcrumbId;
        private final String exchangeId;
        private final String messageId;
        private final String correlationId;
        private final String routeId;
        private final String camelContextId;

        private MyMDCCallback(AsyncCallback delegate) {
            this.delegate = delegate;
            this.exchangeId = MDC.get(MDC_EXCHANGE_ID);
            this.messageId = MDC.get(MDC_MESSAGE_ID);
            this.breadcrumbId = MDC.get(MDC_BREADCRUMB_ID);
            this.correlationId = MDC.get(MDC_CORRELATION_ID);
            this.camelContextId = MDC.get(MDC_CAMEL_CONTEXT_ID);
            this.routeId = MDC.get(MDC_ROUTE_ID);
        }

        public void done(boolean doneSync) {
            try {
                if (!doneSync) {
                    // when done asynchronously then restore information from
                    // previous thread
                    if (breadcrumbId != null) {
                        MDC.put(MDC_BREADCRUMB_ID, breadcrumbId);
                    }
                    if (exchangeId != null) {
                        MDC.put(MDC_EXCHANGE_ID, exchangeId);
                    }
                    if (messageId != null) {
                        MDC.put(MDC_MESSAGE_ID, messageId);
                    }
                    if (correlationId != null) {
                        MDC.put(MDC_CORRELATION_ID, correlationId);
                    }
                    if (camelContextId != null) {
                        MDC.put(MDC_CAMEL_CONTEXT_ID, camelContextId);
                    }
                }
                // need to setup the routeId finally
                if (routeId != null) {
                    MDC.put(MDC_ROUTE_ID, routeId);
                }
            } finally {
                // muse ensure delegate is invoked
                delegate.done(doneSync);
            }
        }
    }
}
