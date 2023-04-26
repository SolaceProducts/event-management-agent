package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.document;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.apache.jackrabbit.oak.plugins.document.DocumentStore;
import org.apache.jackrabbit.oak.plugins.document.mongo.MongoDocumentNodeStoreBuilder;
import org.apache.jackrabbit.oak.plugins.document.mongo.MongoDocumentStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "ema.content.database", havingValue = "MongoDB")
@ConfigurationProperties(prefix = "ema.content")
public class MongoDBDocumentStoreConfig {
    private Map<String, String> datasource;

    @Bean
    public DocumentStore documentStore() {
        String host = datasource.get("host");
        String port = datasource.get("port");
        String username = datasource.get("username");
        String password = datasource.get("password");
        String authDb = datasource.get("authDb");
        String database = datasource.get("database");

        MongoClient client = new MongoClient(new ServerAddress(host + ":" + port),
                MongoCredential.createCredential(username, authDb, password.toCharArray()),
                MongoClientOptions.builder().build());

        MongoDatabase db = client.getDatabase(database);

        return new MongoDocumentStore(client, db,
                MongoDocumentNodeStoreBuilder.newMongoDocumentNodeStoreBuilder());
    }
}
