package com.solace.maas.ep.event.management.agent.subscriber;

import com.solace.messaging.receiver.InboundMessage;

/**
 * The SolacePersistentMessageHandlerObserver interface defines methods to observe
 * the lifecycle of messages handled by a Solace message handler. Implementers can
 * use this interface to react to various stages of message processing.
 * Primary use case is for testing purposes.<br>
 *
 * */
public interface SolacePersistentMessageHandlerObserver {

     /**
      * This method is called when the phase of the message processing changes.<br>
      * Return false to stop processing the message - this is only useful for testing purposes.<br>
      * @param message
      * @param phase
      * @return true, if SolacePersistentMessageHandler should continue processing the message, false otherwise.
      */
     boolean onPhaseChange(InboundMessage message, PersistentMessageHandlerObserverPhase phase);

}
