package com.solace.maas.ep.event.management.agent.plugin.localstorage.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.localstorage.processor.output.file.DataCollectionFileWriteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataCollectionFileWriteRouteBuilder extends RouteBuilder {
    private final DataCollectionFileWriteProcessor processor;

    private final MDCProcessor mdcProcessor;

    @Autowired
    public DataCollectionFileWriteRouteBuilder(DataCollectionFileWriteProcessor processor,
                                               MDCProcessor mdcProcessor) {
        super();
        this.processor = processor;
        this.mdcProcessor = mdcProcessor;
    }

    @Override
    public void configure() throws Exception {
        interceptFrom()
                .process(mdcProcessor);

        from("direct:dataCollectionFileWrite")
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
                .process(processor);
    }
}
