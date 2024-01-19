package com.solace.maas.ep.event.management.agent;

import com.solace.maas.ep.event.management.agent.service.DataCollectionFileService;
import com.solace.maas.ep.event.management.agent.service.ImportService;
import com.solace.maas.ep.event.management.agent.service.ScanService;
import com.solace.maas.ep.event.management.agent.service.ScanStatusService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("SCANTEST")
public class ScanTestConfig {

    @Primary
    @Bean
    public ScanService getScanService() {
        return mock(ScanService.class);
    }

    @Primary
    @Bean
    public ScanStatusService getScanStatusService() {
        return mock(ScanStatusService.class);
    }

    @Primary
    @Bean
    public DataCollectionFileService getDataCollectionFileService() {
        return mock(DataCollectionFileService.class);
    }

    @Primary
    @Bean
    public ImportService getImportService() {
        return mock(ImportService.class);
    }
}
