package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.messaging.receiver.InboundMessage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestingSupportSolacePersistentMessageHandlerObserver implements SolacePersistentMessageHandlerObserver {


        private final Set<InboundMessage> receivedMessages = Collections.synchronizedSet(new HashSet<>());
        private final Set<InboundMessage> initiatedMessages = Collections.synchronizedSet(new HashSet<>());
        private final Set<InboundMessage> completedMessages = Collections.synchronizedSet(new HashSet<>());
        private final Set<InboundMessage> acknowledgedMessages = Collections.synchronizedSet(new HashSet<>());
        private final Set<InboundMessage> failedMessages =Collections.synchronizedSet(new HashSet<>());

         @Override
        public void onMessageReceived(InboundMessage message) {
            receivedMessages.add(message);
        }

        @Override
        public void onMessageProcessingInitiated(InboundMessage message) {
            initiatedMessages.add(message);
        }

        @Override
        public void onMessageProcessingCompleted(InboundMessage message) {
            completedMessages.add(message);
        }

        @Override
        public void onMessageProcessingAcknowledged(InboundMessage message) {
            acknowledgedMessages.add(message);
        }

        @Override
        public void onMessageProcessingFailed(InboundMessage message) {
            failedMessages.add(message);
        }

        public boolean hasReceivedMessage(InboundMessage message) {
            return receivedMessages.contains(message);
        }

        public boolean hasInitiatedMessageProcessing(InboundMessage message) {
            return initiatedMessages.contains(message);
        }

        public boolean hasCompletedMessageProcessing(InboundMessage message) {
            return completedMessages.contains(message);
        }

        public boolean hasAcknowledgedMessage(InboundMessage message) {
            return acknowledgedMessages.contains(message);
        }

        public boolean hasFailedMessage(InboundMessage message) {
            return failedMessages.contains(message);
        }

        public void clear() {
            receivedMessages.clear();
            initiatedMessages.clear();
            completedMessages.clear();
            acknowledgedMessages.clear();
            failedMessages.clear();
        }
}
