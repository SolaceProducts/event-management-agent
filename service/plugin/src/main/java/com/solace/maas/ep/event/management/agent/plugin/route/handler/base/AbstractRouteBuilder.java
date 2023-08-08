package com.solace.maas.ep.event.management.agent.plugin.route.handler.base;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.ScanTypeDescendentsProcessor;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptionhandlers.GeneralExceptionHandler;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptionhandlers.ScanDataExceptionHandler;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptionhandlers.ScanStatusExceptionHandler;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptions.ScanDataException;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptions.ScanOverallStatusException;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptions.ScanStatusException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public abstract class AbstractRouteBuilder extends RouteBuilder {

    private final ScanTypeDescendentsProcessor scanTypeDescendentsProcessor;

    @Value("${camel.errorHandling.maximumRedeliveries:10}")
    private int maximumRedeliveries;

    @Value("${camel.errorHandling.maximumRedeliveryDelay:60000}")
    private int maximumRedeliveryDelay;

    @Value("${camel.errorHandling.redeliveryDelay:1000}")
    private int redeliveryDelay;

    protected AbstractRouteBuilder(ScanTypeDescendentsProcessor scanTypeDescendentsProcessor) {
        super();
        this.scanTypeDescendentsProcessor = scanTypeDescendentsProcessor;
    }

    @Override
    public void configure() throws Exception {

        // NetworkClient Selectable network i/o exception + Kafka Future exceptions
        onException(IOException.class, InterruptedException.class, ExecutionException.class, TimeoutException.class)
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .maximumRedeliveryDelay(maximumRedeliveryDelay)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .handled(true)
                .process(new GeneralExceptionHandler())

                // Check for any descendants for the failed scanType
                .setHeader(RouteConstants.IS_EMPTY_SCAN_TYPES, constant(false))
                .process(scanTypeDescendentsProcessor)

                // Send failed status for each failed scan type
                .split(simple("${header." + RouteConstants.SCAN_TYPE + "}"))
                .setHeader(RouteConstants.SCAN_TYPE, simple("${body}"))
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: The status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [${header." + RouteConstants.SCAN_STATUS + "}].")
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false")
                .end()
                .setHeader(RouteConstants.SCAN_TYPE, simple("${header." + RouteConstants.SCAN_TYPE + "}", String.class))
                .to("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false");


        onException(ScanOverallStatusException.class)
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .maximumRedeliveryDelay(maximumRedeliveryDelay)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .handled(true)
                .process(new ScanStatusExceptionHandler())
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: The overall status is: [${header." + RouteConstants.SCAN_STATUS + "}].")
                .split(simple("${header." + RouteConstants.SCAN_TYPE + "}")).delimiter(",")
                .setHeader(RouteConstants.SCAN_TYPE, simple("${body}"))
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false")
                .end()
                .to("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false");


        onException(ScanStatusException.class)
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .maximumRedeliveryDelay(maximumRedeliveryDelay)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .handled(true)
                .process(new ScanStatusExceptionHandler())

                // Check for any descendants for the failed scanType
                .setHeader(RouteConstants.IS_EMPTY_SCAN_TYPES, constant(false))
                .process(scanTypeDescendentsProcessor)

                // Send failed status for each failed scan type
                .split(simple("${header." + RouteConstants.SCAN_TYPE + "}"))
                .setHeader(RouteConstants.SCAN_TYPE, simple("${body}"))
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: Error determining scan status, the status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [${header." + RouteConstants.SCAN_STATUS + "}].")
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false")
                .end()
                .setHeader(RouteConstants.SCAN_TYPE, simple("${header." + RouteConstants.SCAN_TYPE + "}", String.class))
                .to("direct:overallScanStatusPublisher?block=false&failIfNoConsumers=false");


        onException(ScanDataException.class)
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .maximumRedeliveryDelay(maximumRedeliveryDelay)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .handled(true)
                .process(new ScanDataExceptionHandler())
                .log("Scan request [${header." + RouteConstants.SCAN_ID + "}], trace ID [${header." +
                        RouteConstants.TRACE_ID + "}]: Error streaming scan data, the status of [${header." +
                        RouteConstants.SCAN_TYPE + "}]" + " is: [${header." + RouteConstants.SCAN_STATUS + "}].")
                .to("direct:perRouteScanStatusPublisher?block=false&failIfNoConsumers=false");
    }
}
