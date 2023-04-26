package com.solace.maas.ep.event.management.agent.plugin.contentstorage.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.contentstorage.processor.content.storage.ContentStorageProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class ContentStorageRouteBuilder extends RouteBuilder {
    private final ContentStorageProcessor processor;

    private final MDCProcessor mdcProcessor;

    @Autowired
    public ContentStorageRouteBuilder(ContentStorageProcessor processor, MDCProcessor mdcProcessor) {
        this.processor = processor;
        this.mdcProcessor = mdcProcessor;
    }

    @Override
    public void configure() throws Exception {
        interceptFrom()
                .process(mdcProcessor);

        from("direct:contentStorageFileWrite")
                .transform(body().append("\n"))
                .to("file://content_storage/temp/?fileExist=append&charset=utf-8&fileName=" +
                        "${header." + RouteConstants.SCHEDULE_ID +
                        "}_${header." + RouteConstants.SCAN_ID +
                        "}_${header." + RouteConstants.SCAN_TYPE + "}.json")
                .choice().when(header("DATA_PROCESSING_COMPLETE").isEqualTo(true))
                .to("seda:contentStorageBlobWriter")
                .endChoice()
                .end();

        from("seda:contentStorageBlobWriter?blockWhenFull=true&size=" + Integer.MAX_VALUE)
                .setHeader("MIME_TYPE", constant(ContentType.TEXT_PLAIN.getMimeType()))
                .setHeader("FILE_PATH", simple("/content_storage/${header." + RouteConstants.SCHEDULE_ID +
                        "}/${header." + RouteConstants.SCAN_ID +
                        "}/${header." + RouteConstants.SCAN_TYPE + "}.json"))
                .setBody(simple("./content_storage/temp/" +
                        "${header." + RouteConstants.SCHEDULE_ID +
                        "}_${header." + RouteConstants.SCAN_ID +
                        "}_${header." + RouteConstants.SCAN_TYPE + "}.json"))
                .process(processor);
    }
}
