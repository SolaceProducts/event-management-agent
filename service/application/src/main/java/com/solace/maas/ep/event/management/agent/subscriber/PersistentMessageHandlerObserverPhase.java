package com.solace.maas.ep.event.management.agent.subscriber;

public enum PersistentMessageHandlerObserverPhase {
    //msg received from broker
    RECEIVED,
    // msg dispatched
    PROCESSING_INITIATED,
    // msg processor to be executed
    PRE_PROCESSOR_EXECUTION,
    // msg processor executed
    PROCESSOR_COMPLETED,
    // msg to be acknowledged
    PRE_ACKNOWLEDGED,
    // msg acknowledged
    ACKNOWLEDGED,
    // msg processing failed (either in handler or processor
    FAILED
}
