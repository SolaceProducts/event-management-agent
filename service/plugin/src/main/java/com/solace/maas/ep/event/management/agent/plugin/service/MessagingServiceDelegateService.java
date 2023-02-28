package com.solace.maas.ep.event.management.agent.plugin.service;

public interface MessagingServiceDelegateService {
    <T> T getMessagingServiceById(String messagingServiceId);

    <T> T getMessagingServiceClient(String messagingServiceId);
}
