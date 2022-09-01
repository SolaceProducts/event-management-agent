package com.solace.maas.ep.runtime.agent.plugin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.rto-session", havingValue = "true")
public class EnableRtoCondition {
}
