package com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp;

import lombok.Data;

@Data
public class Paging {
    private String cursorQuery;
    private String nextPageUri;
}
