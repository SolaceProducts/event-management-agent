package com.solace.maas.ep.event.management.agent.plugin.confluentSchemaRegistry.exception;

public class ClientException extends RuntimeException {
    public ClientException() {
        super();
    }

    public ClientException(String message) {
        super(message);
    }
}
