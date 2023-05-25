package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.route.RouteBundle;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import com.solace.maas.ep.event.management.agent.util.IDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.MESSAGING_SERVICE_ID;
import static com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants.SCHEDULE_ID;

/**
 * Responsible for initiating and managing Messaging Service scans.
 */
@Slf4j
@Service
public class ConfigurationTaskService {

    private final RouteService routeService;

    private final ProducerTemplate producerTemplate;

    public ConfigurationTaskService(RouteService routeService, ProducerTemplate producerTemplate) {
        this.routeService = routeService;
        this.producerTemplate = producerTemplate;
    }

    public boolean execute(List<RouteBundle> routeBundles, String groupId, String taskId,
                             List<TaskConfig<?>> taskConfig) {
        for (RouteBundle routeBundle : routeBundles) {
            log.trace("Processing RouteBundles: {}", routeBundle);

            RouteEntity route = routeService.findById(routeBundle.getRouteId())
                    .orElseThrow();
            this.executeRoute(groupId, taskId, route, routeBundle.getMessagingServiceId(), taskConfig.get(0));
        }
        return true;
    }


    protected Exchange executeRoute(String groupId, String taskId, RouteEntity route,
                               String messagingServiceId, TaskConfig taskConfig) {
        return producerTemplate.send("seda:" + route.getId(), exchange -> {
            exchange.getIn().setHeader(RouteConstants.TASK_ID, taskId);
            exchange.getIn().setHeader(RouteConstants.SCAN_ID, taskId);
            exchange.getIn().setHeader(SCHEDULE_ID, groupId);
            exchange.getIn().setHeader(MESSAGING_SERVICE_ID, messagingServiceId);
            exchange.getIn().setBody(taskConfig);
        });
    }

}
