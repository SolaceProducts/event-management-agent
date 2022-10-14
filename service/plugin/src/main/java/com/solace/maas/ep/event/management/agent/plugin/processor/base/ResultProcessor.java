package com.solace.maas.ep.event.management.agent.plugin.processor.base;

import org.apache.camel.Processor;

import java.util.Map;

public interface ResultProcessor<T, Y> extends Processor {
    T handleEvent(Map<String, Object> properties, Y body) throws Exception;
}
