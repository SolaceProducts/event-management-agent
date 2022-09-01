package com.solace.maas.ep.runtime.agent.plugin.route;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteBundle {
    private List<RouteBundle> destinations;
    private List<RouteBundle> recipients;
    private String messagingServiceId;
    private String routeId;
    private String scanType;
    private boolean firstRouteInChain;
}
