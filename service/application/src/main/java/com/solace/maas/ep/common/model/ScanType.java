package com.solace.maas.ep.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ScanType {
    @JsonProperty("one-time")
    ONETIME,

    @JsonProperty("scheduled")
    SCHEDULED;
}
