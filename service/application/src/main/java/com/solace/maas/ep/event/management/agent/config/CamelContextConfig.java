package com.solace.maas.ep.event.management.agent.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CamelContextConfig {

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.setUseMDCLogging(true);
                // TODO: GREG fix this
                //context.getCamelContextExtension()
                //context.adapt(ExtendedCamelContext.class).setUnitOfWorkFactory(CustomUnitOfWork::new);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }
}