package com.solace.maas.ep.runtime.agent.plugin.solace.processor.semp;

import lombok.Data;

@Data
public class Meta {
    private Request request;
    private Integer responseCode;
    private Paging paging;
}
