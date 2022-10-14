package com.solace.maas.ep.event.management.agent.service.rtoMessagingServiceCallbacks;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solacesystems.solclientj.core.SolEnum;
import com.solacesystems.solclientj.core.Solclient;
import com.solacesystems.solclientj.core.event.MessageCallback;
import com.solacesystems.solclientj.core.handle.Handle;
import com.solacesystems.solclientj.core.handle.MessageHandle;
import com.solacesystems.solclientj.core.handle.MessageSupport;
import com.solacesystems.solclientj.core.handle.MutableLong;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Data
@ExcludeFromJacocoGeneratedReport
public class MessageCallbackImpl implements MessageCallback {
    private final boolean logCallbacks;
    private String callBackId;
    private boolean keepRxMessage = false;
    private int messageCount = 0;
    private List<MessageHandle> _rxMessages = new LinkedList<>();

    public MessageCallbackImpl(String id, boolean logCalls) {
        callBackId = id;
        logCallbacks = logCalls;
    }

    @Override
    public void onMessage(Handle handle) {
        MessageSupport messageSupport = (MessageSupport) handle;
        setMessageCount(getMessageCount() + 1);

        if (keepRxMessage) {
            if (logCallbacks) {
                log.info(callBackId + " -> Received message [" + messageCount
                        + "], adding it to received messages list");
            }
            MessageHandle takenMessage = Solclient.Allocator
                    .newMessageHandle();
            messageSupport.takeRxMessage(takenMessage);
            _rxMessages.add(takenMessage);
        } else {
            if (logCallbacks) {
                MessageHandle rxMessage = messageSupport.getRxMessage();
                MutableLong seq = new MutableLong();
                if (SolEnum.ReturnCode.OK == rxMessage.getSequenceNumber(seq)) {
                    log.info(callBackId + " -> Received message [" + messageCount
                            + "]SeqNo("
                            + seq.getValue() + "), dumping content");
                } else {
                    log.info(callBackId + " -> Received message [" + messageCount
                            + "], dumping content");
                }
                log.info(rxMessage.dump(SolEnum.MessageDumpMode.BRIEF));
            }
        }
    }

    public void keepRxMessages(boolean keep) {
        keepRxMessage = keep;
    }

}
