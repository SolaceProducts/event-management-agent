package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import reactor.core.Disposable;

import java.util.concurrent.Future;

@AllArgsConstructor
@Data
@Builder
public class TopicWrapperImpl implements AsyncWrapper {
    private final SolaceSubscriberNoThread solaceSubscriber;

    @Override
    public void terminate() {
        solaceSubscriber.stop();
    }
}
