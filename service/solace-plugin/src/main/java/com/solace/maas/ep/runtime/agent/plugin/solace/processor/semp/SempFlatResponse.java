package com.solace.maas.ep.runtime.agent.plugin.solace.processor.semp;

import lombok.Data;

import java.util.Map;

@Data
public class SempFlatResponse<T> {
    private T data;
    private Map<Object, Object> links;
    private Meta meta;

}
