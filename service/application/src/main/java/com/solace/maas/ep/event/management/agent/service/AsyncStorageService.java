package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.async.manager.AsyncProcessManager;
import com.solace.maas.ep.event.management.agent.plugin.route.handler.base.AsyncWrapper;
import com.solace.maas.ep.event.management.agent.plugin.route.manager.AsyncManager;
import com.solace.maas.ep.event.management.agent.repository.model.scan.AsyncScanProcessEntity;
import com.solace.maas.ep.event.management.agent.repository.scan.AsyncScanProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AsyncStorageService implements AsyncManager {
    private final AsyncScanProcessRepository repository;

    private final AsyncProcessManager asyncProcessManager;

    @Autowired
    public AsyncStorageService(AsyncScanProcessRepository repository, AsyncProcessManager asyncProcessManager) {
        this.repository = repository;
        this.asyncProcessManager = asyncProcessManager;
    }

    @Override
    public void storeAsync(AsyncWrapper wrapper, String scanId, String scanType) {
        asyncProcessManager.addAsyncProcess(scanId + "_" + scanType, wrapper);

        repository.save(AsyncScanProcessEntity.builder()
                .id(UUID.randomUUID().toString())
                .active(true)
                .scanId(scanId)
                .scanType(scanType)
                .build());
    }

    @Override
    public void stopAsync(String scanId, String scanType) {
        asyncProcessManager.terminate(scanId + "_" + scanType);

        repository.findAsyncScanProcessEntityByScanIdAndScanType(scanId, scanType)
                .ifPresent(asyncScanProcessEntity -> {
                    asyncScanProcessEntity.setActive(false);

                    repository.save(asyncScanProcessEntity);
                });
    }
}
