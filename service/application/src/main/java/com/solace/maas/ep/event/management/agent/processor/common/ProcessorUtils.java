package com.solace.maas.ep.event.management.agent.processor.common;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public final class ProcessorUtils {

    private ProcessorUtils() {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }


    public static String determineOrganizationId(EventPortalProperties eventPortalProperties,
                                                 Exchange exchange) {

        Validate.notNull(eventPortalProperties, "eventPortalProperties must not be null");
        Validate.notNull(exchange, "exchange must not be null");
        if (Boolean.TRUE.equals(eventPortalProperties.getManaged()) && StringUtils.equals(eventPortalProperties.getOrganizationId(), "*")) {
            return (String) exchange.getIn().getHeaders().get(RouteConstants.ORG_ID);
        } else {
            return eventPortalProperties.getOrganizationId();
        }

    }
}
