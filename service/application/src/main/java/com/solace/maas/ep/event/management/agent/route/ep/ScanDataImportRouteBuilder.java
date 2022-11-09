package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.processor.ScanDataImportFileProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportPublishProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanDataImportStatusProcessor;
import com.solace.maas.ep.event.management.agent.route.ep.exceptionHandlers.ScanDataImportExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import static org.apache.camel.support.builder.PredicateBuilder.and;
import static org.apache.camel.support.builder.PredicateBuilder.not;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanDataImportRouteBuilder extends RouteBuilder {

    private final ScanDataImportPublishProcessor scanDataImportPublishProcessor;
    private final ScanDataImportFileProcessor scanDataImportFileProcessor;
    private final ScanDataImportStatusProcessor scanDataImportStatusProcessor;

    @Autowired
    public ScanDataImportRouteBuilder(ScanDataImportPublishProcessor scanDataImportPublishProcessor,
                                      ScanDataImportFileProcessor scanDataImportFileProcessor,
                                      ScanDataImportStatusProcessor scanDataImportStatusProcessor) {
        super();
        this.scanDataImportPublishProcessor = scanDataImportPublishProcessor;
        this.scanDataImportFileProcessor = scanDataImportFileProcessor;
        this.scanDataImportStatusProcessor = scanDataImportStatusProcessor;
    }

    @Override
    public void configure() {
        from("seda:manualImport?blockWhenFull=true&size=100")
                .routeId("manualImport")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .process(scanDataImportPublishProcessor)
                .split(new ZipSplitter())
                .streaming()
                .filter(and(header("CamelFileName").contains("json"),
                        not(header("CamelFileName").contains("META_INF"))))

                .process(scanDataImportStatusProcessor)
                .to("seda:scanStatusPublisher")

                .process(scanDataImportFileProcessor)
                .split().tokenize("\\n").streaming()

                .to("seda:processImportFiles");


        from("seda:processImportFiles")
                .routeId("processImportFiles")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .to("seda:eventPortal")

                .choice().when(header(Exchange.SPLIT_COMPLETE).isEqualTo(true))
                .to("seda:processEndOfFileImportStatus");


        from("seda:processEndOfFileImportStatus")
                .routeId("processEndOfImportStatus")
                .setHeader("FILE_IMPORTING_COMPLETE", constant(true))
                .to("seda:processScanStatus");

    }
}
