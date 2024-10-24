package com.solace.maas.ep.event.management.agent.subscriber;

public enum PersistentMessageHandlerObserverPhase {
    RECEIVED,
    INITIATED,
    COMPLETED,
    ACKNOWLEDGED,
    FAILED
}
