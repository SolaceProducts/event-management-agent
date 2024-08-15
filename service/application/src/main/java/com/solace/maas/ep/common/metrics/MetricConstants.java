package com.solace.maas.ep.common.metrics;

public class MetricConstants {
    public static final String MAAS_EMA_EVENT_SENT = "maas.ema.event.sent";
    public static final String MAAS_EMA_EVENT_RECEIVED = "maas.ema.event.received";
    public static final String MAAS_EMA_EVENT_PRECYCLE_TIME = "maas.ema.event.pre_cycle_time";
    public static final String MAAS_EMA_EVENT_CYCLE_TIME = "maas.ema.event.cycle_time";
    public static final String ENTITY_TYPE_TAG = "entity_type";
    public static final String STATUS_TAG = "status";
    public static final String ORG_ID_TAG = "org_id";
    public static final String PRECYCLE_TIME_DESCRIPTION = "The producer latency of events. Measures the time from creation to when we pick up the " +
            "event from the queue.";
    public static final String CYCLE_TIME_DESCRIPTION = "The producer throughput of events. Measures the time taken to process an event.";
}
