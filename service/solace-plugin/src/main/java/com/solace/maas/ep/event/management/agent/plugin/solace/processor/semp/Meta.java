package com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp;

import lombok.Data;

@Data
public class Meta {
    private Request request;
    private Integer responseCode;
    private Paging paging;
}
