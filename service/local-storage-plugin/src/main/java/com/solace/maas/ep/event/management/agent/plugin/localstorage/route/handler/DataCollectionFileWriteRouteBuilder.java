package com.solace.maas.ep.event.management.agent.plugin.localstorage.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.localstorage.processor.output.file.DataCollectionFileWriteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.ScanCompleteProcessor;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionFileWriteRouteBuilder extends RouteBuilder {
    private final DataCollectionFileWriteProcessor processor;

    private final RouteCompleteProcessor routeCompleteProcessor;

    private final ScanCompleteProcessor scanCompleteProcessor;

    private final MDCProcessor mdcProcessor;

    @Autowired
    public DataCollectionFileWriteRouteBuilder(DataCollectionFileWriteProcessor processor,
                                               RouteCompleteProcessor routeCompleteProcessor,
                                               ScanCompleteProcessor scanCompleteProcessor,
                                               MDCProcessor mdcProcessor) {
        super();
        this.processor = processor;
        this.routeCompleteProcessor = routeCompleteProcessor;
        this.scanCompleteProcessor = scanCompleteProcessor;
        this.mdcProcessor = mdcProcessor;
    }

    @Override
    public void configure() throws Exception {
        interceptFrom()
                .process(mdcProcessor);

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
                .process((Processor) scanCompleteProcessor);

    }
}
