package com.solace.maas.ep.event.management.agent.plugin.localstorage.config;

import com.solace.maas.ep.event.management.agent.plugin.processor.RouteCompleteProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.logging.MDCProcessor;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.AggregatedFileEvent;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.DataCollectionFileEvent;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.FileStoreManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {
    @Bean
    public FileStoreManager fileStoreManager() {
        return new FileStoreManager() {
            @Override
            public void storeRecord(DataCollectionFileEvent dataCollectionFileEvent) {

            }

            @Override
            public void storeRecord(AggregatedFileEvent aggregatedFileEvent) {

            }
        };
    }

    @Bean
    public RouteCompleteProcessor routeCompleteProcessor() {
        return mock(RouteCompleteProcessor.class);
    }

    @Bean
    public MDCProcessor mdcProcessor() {
        return mock(MDCProcessor.class);
    }

}
