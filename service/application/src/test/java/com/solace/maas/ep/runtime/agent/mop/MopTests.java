package com.solace.maas.ep.runtime.agent.mop;

import com.solace.maas.ep.common.messages.HeartbeatMessage;
import com.solace.maas.ep.runtime.agent.plugin.mop.AckStrategy;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPMessage;
import com.solace.maas.ep.runtime.agent.plugin.mop.MOPSvcType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MopTests {

    @Test
    public void testSvcType() {
        String id = MOPSvcType.maasGateway.getId();
        assertEquals("maas-gateway", id);
        MOPSvcType type = MOPSvcType.getMOPSvcTypeById("maas-gateway");
        assertEquals(MOPSvcType.maasGateway, type);
        type = MOPSvcType.getMOPSvcTypeById("bogus");
        assertNull(type);
    }

    @Test
    public void testMopMessage() {
        MOPMessage message = new HeartbeatMessage();
        message.setOrigType(MOPSvcType.maasCore);
        message.withCorrelationId("id")
                .isReplyMessage(false)
                .withPriority(1000)
                .withOrigId("foo")
                .withOrigSvcType(MOPSvcType.maasGateway)
                .withOrigSvcId("id")
                .withOrigMailbox("mail")
                .withOrigProvider("provider")
                .withOrigDcId("dc");
        message.ack();
        message.setAckStrategy(new AckStrategy() {
            @Override
            public void ack() {
            }
        });
        message.ack();
        message.setRepeat(null);
        message.getRepeat();
        message.setRepeat(true);
        message.getRepeat();
        String id = message.toLog();
        assertNull(id);
    }
}
