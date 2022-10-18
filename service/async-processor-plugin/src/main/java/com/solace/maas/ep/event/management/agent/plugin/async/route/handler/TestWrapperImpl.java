package com.solace.maas.ep.event.management.agent.plugin.async.route.handler;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import reactor.core.Disposable;

@AllArgsConstructor
@Data
@Builder
public class TestWrapperImpl implements AsyncWrapper {
    private final Disposable asyncProcess;

    @Override
    public void terminate() {
        asyncProcess.dispose();
    }
}
