package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.messaging.receiver.InboundMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class simulates a restart of the EMA by returning false when the phase changes to PRE_ACKNOWLEDGED
 */
@Setter
@Slf4j
public class EmaRestartSimulatingPersistentMessageHandlerObserver implements SolacePersistentMessageHandlerObserver {

    private boolean restartSimulated;

    @Override
    public boolean onPhaseChange(InboundMessage message, PersistentMessageHandlerObserverPhase phase) {
        if (!restartSimulated) {
            return true;
        }
        if (phase == PersistentMessageHandlerObserverPhase.PRE_PROCESSOR_EXECUTION || phase == PersistentMessageHandlerObserverPhase.PRE_ACKNOWLEDGED) {
            log.info("PRE_PROCESSOR_EXECUTION | PRE_ACKNOWLEDGED - stopping processing of message to simulate a restart of the EMA");
            // Simulate a restart of the EMA by returning false
            return false;
        }
        return true;
    }

}