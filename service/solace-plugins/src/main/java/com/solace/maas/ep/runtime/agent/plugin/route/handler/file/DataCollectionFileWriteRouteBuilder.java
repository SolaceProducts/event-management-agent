package com.solace.maas.ep.runtime.agent.plugin.route.handler.file;

import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.plugin.processor.logging.RouteCompleteProcessor;
import com.solace.maas.ep.runtime.agent.plugin.processor.logging.ScanCompleteProcessor;
import com.solace.maas.ep.runtime.agent.plugin.processor.output.file.DataCollectionFileWriteProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.LoggerFactory;
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
