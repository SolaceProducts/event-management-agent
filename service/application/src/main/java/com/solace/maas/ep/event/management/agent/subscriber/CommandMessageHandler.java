package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.maas.ep.common.messages.Authentication;
import com.solace.maas.ep.common.messages.CommandMessage;
import com.solace.maas.ep.common.messages.ConnectionDetail;
import com.solace.maas.ep.common.messages.Credential;
import com.solace.maas.ep.common.messages.MessagingServiceConfiguration;
import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.config.SolaceConfiguration;
import com.solace.maas.ep.event.management.agent.event.MessagingServiceEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.AuthenticationOperationDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.ConnectionDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.CredentialDetailsEvent;
import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import com.solace.maas.ep.event.management.agent.service.MessagingServiceDelegateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name = "event-portal.gateway.messaging.standalone", havingValue = "false")
public class CommandMessageHandler extends SolaceMessageHandler<CommandMessage> {

    private final CommandManager commandManager;

    @Autowired
    private MessagingServiceDelegateServiceImpl messagingServiceDelegateService;

    public CommandMessageHandler(
            SolaceConfiguration solaceConfiguration,
            SolaceSubscriber solaceSubscriber, CommandManager commandManager) {
        super(solaceConfiguration.getTopicPrefix() + "command/v1/>", solaceSubscriber);
        this.commandManager = commandManager;
    }

    @Override
    public void receiveMessage(String destinationName, CommandMessage message) {
        log.debug("receiveMessage {}\n{}", destinationName, message);
        //Hack
        MessagingServiceEvent toAdd = mapToMessagingServiceEvent(message.getResources().get(0));
        messagingServiceDelegateService.addMessagingServices(List.of(toAdd));
        commandManager.execute(message);
    }

    /*
       This is a hack. Need better code organization
     */
    private MessagingServiceEvent mapToMessagingServiceEvent(MessagingServiceConfiguration messagingServiceConfiguration) {
        MessagingServiceEvent serviceEvent = new MessagingServiceEvent();
        serviceEvent.setMessagingServiceType(messagingServiceConfiguration.getBrokerType());
        serviceEvent.setId(messagingServiceConfiguration.getId());
        serviceEvent.setName(messagingServiceConfiguration.getName());

        ConnectionDetailsEvent connectionDetailsEvent = new ConnectionDetailsEvent();
        ConnectionDetail connectionDetail = messagingServiceConfiguration.getConnections().get(0);

        connectionDetailsEvent.setName(messagingServiceConfiguration.getName());
        connectionDetailsEvent.setUrl(connectionDetail.getUrl());
        connectionDetailsEvent.setMessagingServiceId(messagingServiceConfiguration.getId());
        connectionDetailsEvent.setProperties(List.of(
                EventProperty.builder()
                        .name("sempPageSize")
                        .value("100")
                        .build(),
                EventProperty.builder()
                        .name("msgVpn")
                        .value(connectionDetail.getMsgVpn())
                        .build())

        );
        Authentication authentication = connectionDetail.getAuthentication();
        AuthenticationDetailsEvent authEvent = new AuthenticationDetailsEvent();
        authEvent.setProtocol(authentication.getProtocol());
        authEvent.setProperties(List.of(
                EventProperty.builder()
                        .name("type")
                        .value("basicAuthentication")
                        .build()
        ));

        CredentialDetailsEvent credentialDetailsEvent = new CredentialDetailsEvent();
        Credential c = authentication.getCredential();
        credentialDetailsEvent.setSource("ENVIRONMENT_VARIABLE");
        credentialDetailsEvent.setOperations(List.of(AuthenticationOperationDetailsEvent.builder()
                .name("ALL")
                .build()));
        credentialDetailsEvent.setProperties(
                List.of(
                        EventProperty.builder()
                                .name("username")
                                .value(c.getUserName())
                                .build(),
                        EventProperty.builder()
                                .name("password")
                                .value(c.getPassword())
                                .build()

                )
        );
        authEvent.setCredentials(
                List.of(credentialDetailsEvent)

        );
        connectionDetailsEvent.setAuthenticationDetails(List.of(authEvent));
        serviceEvent.setConnectionDetails(List.of(connectionDetailsEvent));
        return serviceEvent;
    }
}
