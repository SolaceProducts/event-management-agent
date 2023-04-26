package com.solace.maas.ep.event.management.agent.plugin.contentstorage.config;

import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStoreBuilder;
import org.apache.jackrabbit.oak.plugins.document.DocumentStore;
import org.apache.jackrabbit.oak.spi.blob.BlobStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jcr.Repository;

@Configuration
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class OakConfig {
    private final BlobStore blobStore;

    private final DocumentStore documentStore;

    public OakConfig(BlobStore blobStore, DocumentStore documentStore) {
        this.blobStore = blobStore;
        this.documentStore = documentStore;
    }

    @Bean
    public DocumentNodeStore nodeStore() {
        return DocumentNodeStoreBuilder.newDocumentNodeStoreBuilder()
                .setDocumentStore(documentStore)
                .setBlobStore(blobStore)
                .build();
    }

    @Bean
    public Repository repository() {
        return new Jcr(new Oak(nodeStore())).createRepository();
    }
}
