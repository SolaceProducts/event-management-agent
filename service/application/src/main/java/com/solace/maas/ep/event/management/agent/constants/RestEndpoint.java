package com.solace.maas.ep.event.management.agent.constants;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public class RestEndpoint {
    public static final String BASE_URL = "/api/v2/ema";
    public static final String MESSAGING_SERVICE_URL = BASE_URL + "/resources";
    public static final String SCAN_URL = BASE_URL + "/scan";
    public static final String DATA_COLLECTION_FILE_URL = BASE_URL + "/file";
    public static final String LIVENESS_URL = BASE_URL + "/liveness";
}
