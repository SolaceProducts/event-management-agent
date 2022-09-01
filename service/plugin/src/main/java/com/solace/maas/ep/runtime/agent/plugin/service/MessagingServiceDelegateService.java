package com.solace.maas.ep.runtime.agent.plugin.service;

public interface MessagingServiceDelegateService {
    <T> T getMessagingServiceClient(String messagingServiceId);
}
