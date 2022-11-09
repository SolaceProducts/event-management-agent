package com.solace.maas.ep.event.management.agent.async.manager;

import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AsyncProcessManager {
    private final Map<String, AsyncWrapper> asyncWrapperMap;

    public AsyncProcessManager() {
        this.asyncWrapperMap = new ConcurrentHashMap<>();
    }

    public void addAsyncProcess(String key, AsyncWrapper asyncWrapper) {
        asyncWrapperMap.put(key, asyncWrapper);
    }

    public void terminate(String key) {
        AsyncWrapper asyncWrapper = asyncWrapperMap.remove(key);

        if(Objects.nonNull(asyncWrapper)) {
            asyncWrapper.terminate();
        }
    }
}
