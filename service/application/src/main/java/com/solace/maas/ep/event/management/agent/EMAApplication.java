package com.solace.maas.ep.event.management.agent;

import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@ComponentScan({"com.solace.maas"})
@EnableScheduling
public class EMAApplication {

    protected static final Map<String, Object> startupStartedMarker = new HashMap<>();
    protected static final Map<String, Object> startupCompletedMarker = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(EMAApplication.class);

    static {
        startupStartedMarker.put("starting-state", "startup-started");
        startupCompletedMarker.put("starting-state", "startup-completed");
    }

    public static void main(String[] args) throws Exception {
        log.info(Markers.appendEntries(startupStartedMarker), "Starting event-management-agent --> {}", args.length > 0 ? "" : Arrays.toString(args));

        MDC.put("pid", ManagementFactory.getRuntimeMXBean().getName());

        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.CONSOLE)
                .sources(EMAApplication.class)
                .properties(getDefault())
                .run(args);

        log.info(Markers.appendEntries(startupCompletedMarker), "Started event-management-agent --> {}", args.length > 0 ? "" : Arrays.toString(args));
    }

    public static Properties getDefault() {
        Properties properties = new Properties();

        properties.put("spring.application.name", "event-management-agent");
        properties.put("server.port", "8180");
        return properties;
    }
}
