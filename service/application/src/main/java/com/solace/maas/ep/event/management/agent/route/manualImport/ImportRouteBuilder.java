package com.solace.maas.ep.event.management.agent.route.manualImport;

import com.solace.maas.ep.event.management.agent.plugin.route.exceptionhandlers.ScanDataImportExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.IMPORT_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.TRACE_ID;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ImportRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {

        from("seda:importScanData?blockWhenFull=true&size=100")
                .routeId("importScanData")
                .onException(Exception.class)
                .process(new ScanDataImportExceptionHandler())
                .continued(true)
                .end()
                .setProperty(IMPORT_ID, header(IMPORT_ID))
                .setProperty(TRACE_ID, header(TRACE_ID))
                .setHeader(Exchange.FILE_NAME, simple("${header." + IMPORT_ID + "}.zip"))
                .toD("file://data_collection/import/compressed_data_collection")
                .to("direct:checkZipSizeAndUnzipFiles");

        from("direct:continueParsingUnzippedFiles")
                .routeId("continueParsingUnzippedFiles")
                .to("direct:parseMetaInfoAndPerformHandShakeWithEP");


        from("direct:continueImportingFiles")
                .routeId("continueImportingFiles")
                .to("direct:sendOverAllInProgressImportStatus")
                .to("direct:parseAndStreamImportFiles");
    }
}
