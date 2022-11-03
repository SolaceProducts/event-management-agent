package com.solace.maas.ep.event.management.agent.processor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ScanLogsImportLogEventsProcessor implements Processor {

    private static StringBuilder logMessage = new StringBuilder();
    private static Level parsedLevel = null;
    private static String previousMessage = "";
    private static Level previousLevel = null;

    private static Level parseLogLevel(String body) {

        if (body.contains("[INFO]")) {
            parsedLevel = Level.INFO;
            return parsedLevel;
        } else if (body.contains("[DEBUG]")) {
            parsedLevel = Level.DEBUG;
            return parsedLevel;
        } else if (body.contains("[ERROR]")) {
            parsedLevel = Level.ERROR;
            return parsedLevel;
        } else if (body.contains("[WARN]")) {
            parsedLevel = Level.WARN;
            return parsedLevel;
        } else {
            parsedLevel = Level.TRACE;
            return parsedLevel;
        }
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = (String) exchange.getIn().getBody();

        boolean isNewMessage =
                (body.contains("[INFO]")) ||
                (body.contains("[DEBUG]")) ||
                (body.contains("[ERROR]")) ||
                (body.contains("[WARN]")) ||
                (body.contains("[TRACE]"));

        if (isNewMessage && previousLevel == null) {
            previousMessage = body;
            ILoggingEvent event = prepareLoggingEvent(body);
            exchange.getIn().setBody(event);
        } else {
            previousLevel = parsedLevel;
            logMessage.append(body).append("\n\t");

            if (isNewMessage && previousLevel != null) {
                String res = previousMessage + logMessage.toString();
                prepareLoggingEvent(res);
            }
        }
    }

    private ILoggingEvent prepareLoggingEvent(String body) {
        ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        Level level = parseLogLevel(body);

        return new LoggingEvent(null, logger, level, body,
                new Throwable("throwable message"), null);
    }
}