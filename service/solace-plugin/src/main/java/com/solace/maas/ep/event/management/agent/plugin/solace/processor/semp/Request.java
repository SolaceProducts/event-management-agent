package com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp;

import lombok.Data;

@Data
public class Request {
    private String method;
    private String uri;
}
