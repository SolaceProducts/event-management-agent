package com.solace.maas.ep.event.management.agent.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "logging.log-in-json-format=true",
        "logging.level.root=ERROR",
        "logging.file.name=CEMA-TEST.log",
        "logging.logback.rolling-policy.file-name-pattern=${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz",
        "logging.logback.rolling-policy.max-file-size=1MB",
        "logging.logback.rolling-policy.max-history=30"

})
@DirtiesContext(classMode = AFTER_CLASS)
class LogbackConfigurationTestsWithJsonEncoder {

    @BeforeEach
    void beforeTest() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
    }

    @Test
    //CPD-OFF
    void testRollingFileAppenderWithPatternLayoutEncoder() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            logger.iteratorForAppenders().forEachRemaining(appender -> {
                if (appender.getName().equals("RollingFile")) {
                    commonAssertions(appender);
                    encoderSpecificAssertions((RollingFileAppender) appender);
                }
            });
        }
    }
    //CPD-ON


    private void encoderSpecificAssertions(RollingFileAppender<ILoggingEvent> rollingFileAppender) {
        assertThat(rollingFileAppender.getEncoder().getClass())
                .hasToString("class net.logstash.logback.encoder.LogstashEncoder");
    }

    private void commonAssertions(Appender<ILoggingEvent> appender) {
        RollingFileAppender<ILoggingEvent> rollingFile = (RollingFileAppender) appender;
        assertThat(appender.getContext().getCopyOfPropertyMap().get("LOG_ROOT")).isEqualTo("ERROR");
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = (SizeAndTimeBasedRollingPolicy) rollingFile.getRollingPolicy();
        assertThat(rollingPolicy.getFileNamePattern()).isEqualTo("CEMA-TEST.log.%d{yyyy-MM-dd}.%i.gz");
        assertThat(rollingFile.getFile()).isEqualTo("CEMA-TEST.log");
        assertThat(rollingPolicy.getMaxHistory()).isEqualTo(30);
        assertThat(rollingPolicy.isCleanHistoryOnStart()).isFalse();
    }
}
