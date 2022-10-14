package com.solace.maas.ep.event.management.agent.plugin.rabbitmq.processor.queue;

import com.rabbitmq.http.client.Client;
import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.processor.base.ResultProcessorImpl;
import com.solace.maas.ep.event.management.agent.plugin.rabbitmq.processor.event.queue.RabbitMqQueueEvent;
import com.solace.maas.ep.event.management.agent.plugin.service.MessagingServiceDelegateService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RabbitMqQueueProcessor extends ResultProcessorImpl<List<RabbitMqQueueEvent>, Void> {
    private final MessagingServiceDelegateService messagingServiceDelegateService;

    public RabbitMqQueueProcessor(MessagingServiceDelegateService messagingServiceDelegateService) {
        super();
        this.messagingServiceDelegateService = messagingServiceDelegateService;
    }

    @Override
    public List<RabbitMqQueueEvent> handleEvent(Map<String, Object> properties, Void body) throws Exception {
        String messagingServiceId = (String) properties.get(RouteConstants.MESSAGING_SERVICE_ID);

        Client client = messagingServiceDelegateService.getMessagingServiceClient(messagingServiceId);

        return client.getQueues()
                .stream()
                .map(queueInfo -> RabbitMqQueueEvent.builder()
                        .arguments(queueInfo.getArguments())
                        .name(queueInfo.getName())
                        .autoDelete(queueInfo.isAutoDelete())
                        .exclusive(queueInfo.isExclusive())
                        .exclusiveConsumerTag(queueInfo.getExclusiveConsumerTag())
                        .node(queueInfo.getNode())
                        .state(queueInfo.getState())
                        .vhost(queueInfo.getVhost())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
