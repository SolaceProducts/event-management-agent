package com.solace.maas.ep.common.model;

import org.junit.jupiter.api.Test;

public class SempEntityTypeTest {

    @Test
    void testFromValue() {
        SempEntityType entityType = SempEntityType.fromValue("solaceQueue");
        assert entityType == SempEntityType.solaceQueue;
    }

    @Test
    void testGetValue() {
        SempEntityType entityType = SempEntityType.solaceQueue;
        assert entityType.getValue().equals("solaceQueue");
    }

    @Test
    void testUnsupportedEntityType() {
        try {
            SempEntityType.fromValue("unsupported");
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Unsupported entity type: unsupported");
        }
    }

}
