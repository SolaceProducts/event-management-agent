package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.datastore;

import lombok.Getter;
import lombok.Setter;
import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.oak.blob.cloud.azure.blobstorage.AzureConstants;
import org.apache.jackrabbit.oak.blob.cloud.azure.blobstorage.AzureDataStore;
import org.apache.jackrabbit.oak.plugins.blob.datastore.DataStoreBlobStore;
import org.apache.jackrabbit.oak.spi.blob.BlobStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "ema.content.storage", havingValue = "Azure")
@ConfigurationProperties(prefix = "ema.content")
public class AzureDataStoreConfig {
    @Value("${ema.content.homeDir}")
    private String homeDir;

    private Map<String, String> properties;

    @Bean
    public BlobStore dataStore() throws DataStoreException {
        Properties props = new Properties();
        props.putAll(properties);

        AzureDataStore store = new AzureDataStore();
        store.setProperties(props);
        store.init(homeDir);

        return new DataStoreBlobStore(store);
    }
}
