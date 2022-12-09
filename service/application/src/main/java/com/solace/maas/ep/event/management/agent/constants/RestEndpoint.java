package com.solace.maas.ep.event.management.agent.constants;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class RestEndpoint {
    public static final String BASE_URL = "/api/v2/ema";
    public static final String MESSAGING_SERVICE_URL = BASE_URL + "/messagingServices";
    public static final String SCAN_URL = BASE_URL + "/scan";
}
