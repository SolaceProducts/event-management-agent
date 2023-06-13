package com.solace.maas.ep.event.management.agent.processor;

import com.solace.maas.ep.common.messages.ConfigurationTaskResultMessage;
import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.task.TaskResult;
import com.solace.maas.ep.event.management.agent.publisher.ConfigurationTaskResultsPublisher;
import com.solace.maas.ep.event.management.agent.route.ep.exceptions.ConfigurationTaskException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${eventPortal.gateway.messaging.standalone} == false")
public class ConfigurationTaskResultProcessor implements Processor {

    private final ConfigurationTaskResultsPublisher configurationTaskResultPublisher;
    private final String orgId;
    private final String runtimeAgentId;

    @Autowired
    public ConfigurationTaskResultProcessor(ConfigurationTaskResultsPublisher configurationTaskResultPublisher, EventPortalProperties eventPortalProperties) {
        super();

        this.configurationTaskResultPublisher = configurationTaskResultPublisher;

        orgId = eventPortalProperties.getOrganizationId();
        runtimeAgentId = eventPortalProperties.getRuntimeAgentId();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> topicDetails = new HashMap<>();

        Map<String, Object> properties = exchange.getIn().getHeaders();
        List<TaskResult> body = (List<TaskResult>) exchange.getIn().getBody();

        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);
        String taskId = (String) properties.get(RouteConstants.CONFIG_TASK_ID);
        String configType = (String) properties.get(RouteConstants.CONFIG_TASK_TYPE);
        ConfigurationTaskResultMessage msg = new ConfigurationTaskResultMessage();
        msg.setMessagingServiceId(messagingServiceId);
        msg.setTaskId(taskId);
        msg.setTaskResults(body);
        msg.setConfigType(configType);
        msg.setMopVer("0");
        msg.setMopProtocol(MOPProtocol.event);
        msg.setMopMsgType(MOPMessageType.generic);

        topicDetails.put("taskId", taskId);
        topicDetails.put("configType", configType);
        topicDetails.put("orgId", this.orgId);
        topicDetails.put("runtimeAgentId", this.runtimeAgentId);
        topicDetails.put("messagingServiceId", messagingServiceId);

        try {
            this.configurationTaskResultPublisher.sendConfigurationTaskResult(msg, topicDetails);
        } catch (Exception e) {
            throw new ConfigurationTaskException("Configuration Task Result exception: " + e, Map.of(taskId, List.of(e)), body);
        }
    }
}
