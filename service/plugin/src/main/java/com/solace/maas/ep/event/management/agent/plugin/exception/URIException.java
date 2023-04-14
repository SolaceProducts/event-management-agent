package com.solace.maas.ep.event.management.agent.plugin.exception;

public class URIException extends RuntimeException {
    public URIException() {
        super();
    }

    public URIException(String message, Exception e) {
        super(message, e);
    }
}
