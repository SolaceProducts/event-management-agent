package com.solace.maas.ep.event.management.agent.realTests;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;

@Slf4j
@ActiveProfiles("offlinetests")
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfflineRealTest {

    @Autowired
    private EventPortalProperties eventPortalProperties;

    @Test
    public void applicationStartsInOfflineMode() {
        assertTrue(eventPortalProperties.getGateway().getMessaging().isStandalone());
    }

}