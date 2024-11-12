package com.solace.maas.ep.event.management.agent.subscriber;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmRestartSimulatingPersistentMessageHandlerObserverTest {

    @Test
    void testOnPhaseChangeRestartSimulated() {
        EmaRestartSimulatingPersistentMessageHandlerObserver emaRestartSimulatingPersistentMessageHandlerObserver =
                new EmaRestartSimulatingPersistentMessageHandlerObserver();
        emaRestartSimulatingPersistentMessageHandlerObserver.setRestartSimulated(true);
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PRE_PROCESSOR_EXECUTION)).isFalse();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PRE_ACKNOWLEDGED)).isFalse();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.ACKNOWLEDGED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.FAILED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PROCESSOR_COMPLETED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PROCESSING_INITIATED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.RECEIVED)).isTrue();
    }

    @Test
    void testOnPhaseChangeRestartNotSimulated() {
        EmaRestartSimulatingPersistentMessageHandlerObserver emaRestartSimulatingPersistentMessageHandlerObserver =
                new EmaRestartSimulatingPersistentMessageHandlerObserver();
        emaRestartSimulatingPersistentMessageHandlerObserver.setRestartSimulated(false);
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PRE_PROCESSOR_EXECUTION)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PRE_ACKNOWLEDGED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.ACKNOWLEDGED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.FAILED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PROCESSOR_COMPLETED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.PROCESSING_INITIATED)).isTrue();
        assertThat(emaRestartSimulatingPersistentMessageHandlerObserver
                .onPhaseChange(null, PersistentMessageHandlerObserverPhase.RECEIVED)).isTrue();
    }
}
