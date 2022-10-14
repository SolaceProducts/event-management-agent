package com.solace.maas.ep.event.management.agent.repository.model.scan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScanLifecycleEntity {
    private String scanId;

    private int numExpectedCompletionMessages;

    private final AtomicInteger receivedCompletionMessagesCount = new AtomicInteger(0);

    public void incrementReceivedCompletionMessagesCount() {
        receivedCompletionMessagesCount.incrementAndGet();
    }

    public boolean isScanComplete(){
        return receivedCompletionMessagesCount.get() >= numExpectedCompletionMessages;
    }
}
