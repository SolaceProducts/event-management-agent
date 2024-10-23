package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.messaging.receiver.InboundMessage;

public class DefaultSolacePersistentMessageHandlerObserver implements SolacePersistentMessageHandlerObserver {

    @Override
    public void onMessageReceived(InboundMessage message) {
        // Do nothing
    }

    @Override
    public void onMessageProcessingInitiated(InboundMessage message) {
        // Do nothing
    }

    @Override
    public void onMessageProcessingCompleted(InboundMessage message) {
        // Do nothing
    }

    @Override
    public void onMessageProcessingAcknowledged(InboundMessage message) {
        // Do nothing
    }

    @Override
    public void onMessageProcessingFailed(InboundMessage message) {
        // Do nothing
    }
}
