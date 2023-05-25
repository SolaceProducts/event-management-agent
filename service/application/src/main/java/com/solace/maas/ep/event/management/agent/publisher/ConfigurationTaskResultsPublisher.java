package com.solace.maas.ep.event.management.agent.publisher;

import com.solace.maas.ep.common.messages.ConfigurationTaskResultMessage;
import com.solace.maas.ep.common.messages.ScanDataStatusMessage;
import com.solace.maas.ep.common.messages.ScanStatusMessage;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.plugin.constants.ScanStatus;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.publisher.SolacePublisher;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ClientException;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ConfigurationTaskStatusException;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ScanStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
@ExcludeFromJacocoGeneratedReport
@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ConfigurationTaskResultsPublisher {
    private static final String TOPIC_SUFFIX = "config/v1/response/%s/%s";
    private final SolacePublisher solacePublisher;

    private final SolaceConfiguration solaceConfiguration;

    public ConfigurationTaskResultsPublisher(SolaceConfiguration solaceConfiguration,  SolacePublisher solacePublisher) {
        this.solacePublisher = solacePublisher;
        this.solaceConfiguration = solaceConfiguration;
    }

    /**
     * Sends the status of the overall scan.
     * topic:
     * sc/ep/runtime/{orgId}/{runtimeAgentId}/scan/status/v1/{messagingServiceId}/{scanId}
     */
    public void sendConfigurationTaskResult(ConfigurationTaskResultMessage message, Map<String, String> topicDetails) {
        String taskId = topicDetails.get("taskId");
        String configType = topicDetails.get("configType");
        TaskResult status = message.getTaskResults().isEmpty()?null:message.getTaskResults().get(0);

        String topicPrefix = String.format(solaceConfiguration.getTopicPrefix(), topicDetails.get("orgId"),
                topicDetails.get("runtimeAgentId") );
        String topicSuffix = String.format(TOPIC_SUFFIX, topicDetails.get("messagingServiceId"), taskId);

        try {
            solacePublisher.publish(message, topicPrefix + topicSuffix);
        } catch (Exception e) {
            throw new ConfigurationTaskStatusException("Over all config exception: " + e.getMessage(),
                    Map.of(taskId, List.of(e)), "Overall status", Arrays.asList(configType.split(",")), status);
        }
    }

 }
