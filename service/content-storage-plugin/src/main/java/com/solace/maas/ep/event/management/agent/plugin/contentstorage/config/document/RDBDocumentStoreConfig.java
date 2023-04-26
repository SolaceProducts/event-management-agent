package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.document;

import org.apache.jackrabbit.oak.plugins.document.DocumentStore;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDataSourceFactory;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDocumentNodeStoreBuilder;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDocumentStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "ema.content.database", havingValue = "RDB")
public class RDBDocumentStoreConfig {
    @Value("${ema.content.datasource.url}")
    private String url;

    @Value("${ema.content.datasource.username}")
    private String username;

    @Value("${ema.content.datasource.password}")
    private String password;

    @Bean
    public DocumentStore documentStore() {
        DataSource dataSource = RDBDataSourceFactory.forJdbcUrl(url, username, password);
        return new RDBDocumentStore(dataSource, RDBDocumentNodeStoreBuilder.newRDBDocumentNodeStoreBuilder());
    }
}
