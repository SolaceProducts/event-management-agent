package com.solace.maas.ep.event.management.agent.processor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.solace.maas.ep.common.messages.CommandLogMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.constants.Command;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.publisher.CommandLogsPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandLogsProcessor implements Processor {
    private final String orgId;
    private final String runtimeAgentId;

    private final CommandLogsPublisher logDataPublisher;

    @Autowired
    public CommandLogsProcessor(CommandLogsPublisher logDataPublisher, EventPortalProperties eventPortalProperties) {
        super();

        this.logDataPublisher = logDataPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        ILoggingEvent event = (ILoggingEvent) exchange.getIn().getBody();
        String commandCorrelationId = (String) properties.get(RouteConstants.COMMAND_CORRELATION_ID);
        String traceId = (String) properties.get(RouteConstants.TRACE_ID);
        String actorId = (String) properties.get(RouteConstants.ACTOR_ID);
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        CommandLogMessage logDataMessage = new CommandLogMessage(orgId, commandCorrelationId, traceId, actorId, event.getLevel().toString(),
                String.format("%s%s", event.getFormattedMessage(), "\n"), event.getTimeStamp(), runtimeAgentId);

        topicDetails.put("orgId", orgId);
        topicDetails.put("runtimeAgentId", runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);
        topicDetails.put(Command.COMMAND_CORRELATION_ID, commandCorrelationId);

        logDataPublisher.sendCommandLogData(logDataMessage, topicDetails);
    }
}
