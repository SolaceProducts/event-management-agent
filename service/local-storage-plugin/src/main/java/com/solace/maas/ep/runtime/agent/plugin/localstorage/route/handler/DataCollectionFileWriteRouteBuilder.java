package com.solace.maas.ep.runtime.agent.plugin.localstorage.route.handler;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.localstorage.processor.output.file.DataCollectionFileWriteProcessor;
import com.solace.maas.ep.runtime.agent.plugin.processor.logging.RouteCompleteProcessor;
import com.solace.maas.ep.runtime.agent.plugin.processor.logging.ScanCompleteProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionFileWriteRouteBuilder extends RouteBuilder {
    private final DataCollectionFileWriteProcessor processor;

    private final RouteCompleteProcessor routeCompleteProcessor;

    private final ScanCompleteProcessor scanCompleteProcessor;


    @Autowired
    public DataCollectionFileWriteRouteBuilder(DataCollectionFileWriteProcessor processor,
                                               RouteCompleteProcessor routeCompleteProcessor,
                                               ScanCompleteProcessor scanCompleteProcessor) {
        super();
        this.processor = processor;
        this.routeCompleteProcessor = routeCompleteProcessor;
        this.scanCompleteProcessor = scanCompleteProcessor;
    }

    @Override
    public void configure() throws Exception {
        interceptFrom()
                .process(exchange -> {
                    MDC.put(RouteConstants.SCAN_ID,
                            exchange.getIn().getHeader(RouteConstants.SCAN_ID, String.class));

                    MDC.put(RouteConstants.MESSAGING_SERVICE_ID,
                            exchange.getIn().getHeader(RouteConstants.MESSAGING_SERVICE_ID, String.class));

                    MDC.put(RouteConstants.SCHEDULE_ID,
                            exchange.getIn().getHeader(RouteConstants.SCHEDULE_ID, String.class));

                    MDC.put(RouteConstants.SCAN_TYPE,
                            exchange.getIn().getHeader(RouteConstants.SCAN_TYPE, String.class));
                });

        from("seda:dataCollectionFileWrite?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .transform(body().append("\n"))
                .to("file://data_collection/?fileExist=append&charset=utf-8&fileName=" +
                        "${header." + RouteConstants.SCHEDULE_ID +
                        "}/${header." + RouteConstants.SCAN_ID +
                        "}/${header." + RouteConstants.SCAN_TYPE + "}.json")
                .choice().when(header("DATA_PROCESSING_COMPLETE").isEqualTo(true))
                .to("seda:dataCollectionDBWriter")
                .endChoice()
                .end();

        from("seda:dataCollectionDBWriter?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .process(processor)
                .to("seda:processEndOfRoute");

        // Now we're at the end of the route. Call the routeCompleteProcessor to let it
        // know we're done. Once all the routes are completed, the scanLifecycleService will
        // mark the scan as completed.
        from("seda:processEndOfRoute")
                .process((Processor) routeCompleteProcessor)
                .choice().when(header("SCAN_COMPLETE").isEqualTo(true))
                .to("seda:processEndOfScan")
                .endChoice()
                .end();

        from("seda:processEndOfScan")
                .log(LoggingLevel.DEBUG, LoggerFactory.getLogger("SiftLogger"), "${body}")
                .to("seda:finishUpTheScan");

        from("seda:finishUpTheScan")
                .process((Processor) scanCompleteProcessor);

    }
}
