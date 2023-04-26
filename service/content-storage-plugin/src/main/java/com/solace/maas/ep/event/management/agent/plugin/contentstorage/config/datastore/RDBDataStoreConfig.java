package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.datastore;

import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBBlobStore;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDataSourceFactory;
import org.apache.jackrabbit.oak.spi.blob.BlobStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "ema.content.storage", havingValue = "RDB")
public class RDBDataStoreConfig {
    @Value("${ema.content.datasource.url}")
    private String url;

    @Value("${ema.content.datasource.username}")
    private String username;

    @Value("${ema.content.datasource.password}")
    private String password;

    @Bean
    public BlobStore dataStore() throws DataStoreException {
        DataSource dataSource = RDBDataSourceFactory.forJdbcUrl(url, username, password);

        return new RDBBlobStore(dataSource);
    }
}
