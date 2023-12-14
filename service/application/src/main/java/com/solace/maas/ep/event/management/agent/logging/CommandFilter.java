package com.solace.maas.ep.event.management.agent.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CommandFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {

        String commandCorrelationId = event.getMDCPropertyMap().get(RouteConstants.COMMAND_CORRELATION_ID);

        if (StringUtils.isNotEmpty(commandCorrelationId)) {
            return FilterReply.ACCEPT;
        }

        return FilterReply.DENY;
    }
}
