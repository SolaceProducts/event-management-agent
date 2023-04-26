package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.datastore;

import lombok.Getter;
import lombok.Setter;
import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.oak.blob.cloud.s3.S3DataStore;
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
@ConditionalOnProperty(name = "ema.content.storage", havingValue = "S3")
@ConfigurationProperties(prefix = "ema.content")
public class S3DataStoreConfig {
    @Value("${ema.content.homeDir}")
    private String homeDir;

    private Map<String, String> properties;

    @Bean
    public BlobStore dataStore() throws DataStoreException {
        Properties props = new Properties();
        props.putAll(properties);

        S3DataStore store = new S3DataStore();
        store.setProperties(props);
        store.setCacheSize(68719476736L);
        store.init(homeDir);

        return new DataStoreBlobStore(store);
    }
}
