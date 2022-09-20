package com.solace.maas.ep.runtime.agent.plugin.solace.processor.semp;

import lombok.Data;

@Data
public class Paging {
    private String cursorQuery;
    private String nextPageUri;
}
