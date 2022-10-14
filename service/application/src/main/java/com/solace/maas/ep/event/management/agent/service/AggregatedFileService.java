package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.file.aggregation.AggregatedFileRepository;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.file.aggregation.AggregatedFileEntity;
import com.solace.maas.ep.event.management.agent.plugin.constants.AggregationConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.output.file.event.FileDetailsAggregationEvent;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AggregatedFileService {
    private final AggregatedFileRepository repository;

    private final ProducerTemplate producerTemplate;

    @Autowired
    public AggregatedFileService(AggregatedFileRepository repository, ProducerTemplate producerTemplate) {
        this.repository = repository;
        this.producerTemplate = producerTemplate;
    }

    public AggregatedFileEntity save(AggregatedFileEntity aggregatedFileEntity) {
        return repository.save(aggregatedFileEntity);
    }

    public Optional<AggregatedFileEntity> findByPath(String path) {
        return repository.findByPath(path);
    }

    public void aggregate(List<DataCollectionFileEntity> files) {
        List<FileDetailsAggregationEvent> events = files.stream()
                .map(file -> {
                    Path path = Paths.get(file.getPath());

                    return FileDetailsAggregationEvent.builder()
                            .id(file.getId())
                            .key(path.getFileName().toString().replaceFirst("\\.json", ""))
                            .name(path.getFileName().toString())
                            .path(path.getParent().toString())
                            .build();
                }).collect(Collectors.toUnmodifiableList());

        producerTemplate.asyncSend("seda:fileReader", exchange -> {
            exchange.setProperty("FILES", events);
            exchange.getIn().setHeader(RouteConstants.AGGREGATION_ID, UUID.randomUUID().toString());
            exchange.getIn().setHeader(AggregationConstants.AGGREGATION_SIZE, events.size());
        });

    }
}
