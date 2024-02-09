package com.solace.maas.ep.event.management.agent.plugin.exception;

public class PluginClientException extends RuntimeException {
    public PluginClientException() {
        super();
    }

    public PluginClientException(String message, Exception e) {
        super(message, e);
    }

    public PluginClientException(String message) {
        super(message);
    }

    public PluginClientException(Exception ex) {
        super(ex);
    }
}
