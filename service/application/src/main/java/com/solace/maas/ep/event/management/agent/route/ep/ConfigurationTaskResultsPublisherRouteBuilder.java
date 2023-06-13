package com.solace.maas.ep.event.management.agent.route.ep;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.processor.ConfigurationTaskResultProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanStatusOverAllProcessor;
import com.solace.maas.ep.event.management.agent.processor.ScanStatusPerRouteProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
@Profile("!TEST")
public class ConfigurationTaskResultsPublisherRouteBuilder extends AbstractRouteBuilder {

    private final ConfigurationTaskResultProcessor configurationTaskResultProcessor;


    @Autowired
    public ConfigurationTaskResultsPublisherRouteBuilder(ConfigurationTaskResultProcessor configurationTaskResultProcessor) {
        super();
        this.configurationTaskResultProcessor = configurationTaskResultProcessor;
    }

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:configurationTaskResultPublisher")
                .routeId("configurationTaskResultPublisher")
                .process(configurationTaskResultProcessor)
                .end();
    }
}
