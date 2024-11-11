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
        public boolean onPhaseChange(InboundMessage message, PersistentMessageHandlerObserverPhase phase) {
            switch (phase) {
                case RECEIVED:
                    receivedMessages.add(message);
                    break;
                case PROCESSING_INITIATED:
                    initiatedMessages.add(message);
                    break;
                case PROCESSOR_COMPLETED:
                    completedMessages.add(message);
                    break;
                case ACKNOWLEDGED:
                    acknowledgedMessages.add(message);
                    break;
                case FAILED:
                    failedMessages.add(message);
                    break;
                default:
                    break;
            }
            return true;
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

}
