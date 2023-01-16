package com.solace.maas.ep.event.management.agent.plugin.solace.route.handler.topicSubscriber;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SolaceTopicWrapperImpl implements AsyncWrapper {
    private final SolaceTopicSubscriberNoThread solaceSubscriber;

    @Override
    public void terminate() {
        solaceSubscriber.stop();
    }
}
