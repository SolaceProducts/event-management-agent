package com.solace.maas.ep.event.management.agent.subscriber.messageProcessors;

import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessage;

public interface MessageProcessor<T extends MOPMessage> {

    void processMessage(T message);

    Class supportedClass();

    T castToMessageClass(Object message);

    void onFailure(Exception e, T message);
}
