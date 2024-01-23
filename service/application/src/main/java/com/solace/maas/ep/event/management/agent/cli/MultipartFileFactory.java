package com.solace.maas.ep.event.management.agent.cli;

import org.springframework.stereotype.Component;

@Component
public class MultipartFileFactory {
    public MultipartFileWrapper create(String absolutePath) {
        return MultipartFileWrapper.builder().absolutePath(absolutePath).build();
    }
}
