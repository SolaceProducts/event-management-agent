package com.solace.maas.ep.common.metrics;

public class ObservabilityConstants {
    public static final String MAAS_EMA_SCAN_EVENT_SENT = "maas.ema.scan_event.sent";
    public static final String MAAS_EMA_CONFIG_PUSH_EVENT_SENT = "maas.ema.config_push_event.sent";
    public static final String MAAS_EMA_HEARTBEAT_EVENT_SENT = "maas.ema.heartbeat_event.sent";

    public static final String MAAS_EMA_SCAN_EVENT_RECEIVED = "maas.ema.scan_event.received";
    public static final String MAAS_EMA_CONFIG_PUSH_EVENT_RECEIVED = "maas.ema.config_push_event.received";

    public static final String MAAS_EMA_CONFIG_PUSH_EVENT_CYCLE_TIME = "maas.ema.config_push_event.cycle_time";
    public static final String MAAS_EMA_SCAN_EVENT_CYCLE_TIME = "maas.ema.scan_event.cycle_time";

    public static final String STATUS_TAG = "status";
    public static final String ORG_ID_TAG = "org_id";
    public static final String SCAN_ID_TAG = "scan_id";

    private ObservabilityConstants() {}
}
