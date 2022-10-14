package com.solace.maas.ep.event.management.agent.service.rtoMessagingServiceCallbacks;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solacesystems.solclientj.core.event.SessionEventCallback;
import com.solacesystems.solclientj.core.handle.SessionHandle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ExcludeFromJacocoGeneratedReport
public class SessionCallbackImpl implements SessionEventCallback {

    private final boolean logCallbacks;
    String callBackId;
    private int eventCount = 0;

    public SessionCallbackImpl(String id, boolean logCalls) {
        callBackId = id;
        logCallbacks = logCalls;
    }

    @Override
    public void onEvent(SessionHandle sessionHandle) {
        eventCount++;
        if (logCallbacks) {
            log.info(callBackId + "- Received SessionEvent[" + eventCount + "]:"
                    + sessionHandle.getSessionEvent());
        }
    }
}
