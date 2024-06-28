package com.solace.maas.ep.event.management.agent.micrometer;

import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdFlavor;
import io.micrometer.statsd.StatsdProtocol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@SuppressWarnings("PMD")
@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "management.metrics.tags.maas_id=my-maas-id",
        "management.metrics.enable.all=false",
        "management.metrics.enable.application.started.time=true",
        "management.metrics.enable.jvm.info=true",
        "management.statsd.metrics.export.enabled=true",
        "management.statsd.metrics.export.flavor=datadog",
        "management.statsd.metrics.export.host=127.0.0.1",
        "management.statsd.metrics.export.port=8128",
        "management.statsd.metrics.export.protocol=udp"
})
@DirtiesContext(classMode = AFTER_CLASS)
class MicrometerStatsDConfigTest {

    @Autowired
    private StatsdConfig statsdConfig;

    @Autowired
    private MetricsProperties metricsProperties;

    @Test
    void testStatsdConfig(){
        assertNotNull(statsdConfig);
        Assertions.assertTrue(statsdConfig.enabled());
        Assertions.assertEquals(StatsdFlavor.DATADOG,statsdConfig.flavor());
        Assertions.assertEquals("127.0.0.1", statsdConfig.host());
        Assertions.assertEquals(8128, statsdConfig.port());
        Assertions.assertEquals(StatsdProtocol.UDP, statsdConfig.protocol());
    }

    @Test
    void testMicrometerRegistryConfig(){
        assertNotNull(metricsProperties);
        Assertions.assertTrue(metricsProperties.getTags().containsKey("maas_id"));
        Assertions.assertEquals("my-maas-id", metricsProperties.getTags().get("maas_id"));
        Assertions.assertTrue(metricsProperties.getEnable().containsKey("all"));
        Assertions.assertEquals(false, metricsProperties.getEnable().get("all"));
        Assertions.assertTrue(metricsProperties.getEnable().containsKey("application.started.time"));
        Assertions.assertEquals(true, metricsProperties.getEnable().get("application.started.time"));
        Assertions.assertTrue(metricsProperties.getEnable().containsKey("jvm.info"));
        Assertions.assertEquals(true, metricsProperties.getEnable().get("jvm.info"));

    }
}
