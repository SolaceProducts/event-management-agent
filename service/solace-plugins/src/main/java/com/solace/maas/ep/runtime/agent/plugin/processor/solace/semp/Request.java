package com.solace.maas.ep.runtime.agent.plugin.processor.solace.semp;

import lombok.Data;

@Data
public class Request {
    private String method;
    private String uri;
}
