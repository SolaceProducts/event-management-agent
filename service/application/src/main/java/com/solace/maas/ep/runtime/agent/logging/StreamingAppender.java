package com.solace.maas.ep.runtime.agent.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.runtime.agent.repository.model.route.RouteEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;

import static com.solace.maas.ep.runtime.agent.plugin.constants.RouteConstants.SCAN_ID;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class StreamingAppender extends AppenderBase<ILoggingEvent> {

    protected ProducerTemplate producerTemplate;
    protected Level logLevel = Level.DEBUG;
    protected String groupId;
    protected String scanId;
    protected String messagingServiceId;
    protected RouteEntity route;
    protected Boolean standalone;

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLevel().isGreaterOrEqual(logLevel)) {
            if (MDC.get(SCAN_ID).equals(scanId)) {
                if (!standalone) {
                    sendLogsAsync(event);
                }
            }
        }
    }

    public void sendLogsAsync(ILoggingEvent event) {
        producerTemplate.asyncSend(route.getId(), exchange -> {
            // Need to set headers to let the Route have access to the Scan ID, Group ID, and Messaging Service ID.
            exchange.getIn().setHeader(SCAN_ID, scanId);
            exchange.getIn().setHeader(RouteConstants.SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(RouteConstants.MESSAGING_SERVICE_ID, messagingServiceId);

            exchange.getIn().setBody(event);
        }).whenComplete((exchange, exception) -> {
            if (exception != null) {
                log.error("Exception occurred while executing route publishLogs for scan {}.", scanId, exception);
            } else {
                log.debug("Successfully completed route publishLogs for scan {}", scanId);
            }
        });
    }
}
