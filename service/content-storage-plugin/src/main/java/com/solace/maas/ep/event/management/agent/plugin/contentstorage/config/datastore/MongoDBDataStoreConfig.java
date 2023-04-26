package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.datastore;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;
import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.oak.plugins.document.mongo.MongoBlobStore;
import org.apache.jackrabbit.oak.spi.blob.BlobStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "ema.content.storage", havingValue = "MongoDB")
@ConfigurationProperties(prefix = "ema.content")
public class MongoDBDataStoreConfig {
    private Map<String, String> properties;

    @Bean
    public BlobStore dataStore() throws DataStoreException {
        String host = properties.get("host");
        String port = properties.get("port");
        String username = properties.get("username");
        String password = properties.get("password");
        String authDb = properties.get("authDb");
        String database = properties.get("database");

        MongoClient client = new MongoClient(new ServerAddress(host + ":" + port),
                MongoCredential.createCredential(username, authDb, password.toCharArray()),
                MongoClientOptions.builder().build());

        return new MongoBlobStore(client.getDatabase(database));
    }
}
