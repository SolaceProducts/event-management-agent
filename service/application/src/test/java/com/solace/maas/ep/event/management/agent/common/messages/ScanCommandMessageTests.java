package com.solace.maas.ep.event.management.agent.common.messages;

import com.solace.maas.ep.common.messages.ScanCommandMessage;
import com.solace.maas.ep.common.model.ResourceConfigurationType;
import com.solace.maas.ep.common.model.ScanDestination;
import com.solace.maas.ep.common.model.ScanType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPMessageType;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPProtocol;
import com.solace.maas.ep.event.management.agent.plugin.mop.MOPUHFlag;
import com.solace.maas.ep.event.management.agent.subscriber.messageProcessors.EventBrokerResourceConfigTestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ScanCommandMessageTests {

    @Test
    void instantiateScanCommandMessage(){

        ScanCommandMessage scanCommandMessage = new ScanCommandMessage(
                "messagingServiceId1",
                "scanId1",
                List.of(ScanType.SOLACE_ALL),
                List.of(ScanDestination.EVENT_PORTAL, ScanDestination.FILE_WRITER)
        );
        assertNotNull(scanCommandMessage);
        assertEquals(MOPMessageType.generic,scanCommandMessage.getMopMsgType());
        assertEquals(MOPProtocol.scanDataControl,scanCommandMessage.getMopProtocol());
        assertEquals("1",scanCommandMessage.getMopVer());
        assertEquals(MOPUHFlag.ignore,scanCommandMessage.getMsgUh());

        assertEquals("messagingServiceId1", scanCommandMessage.getMessagingServiceId());
        assertEquals("scanId1", scanCommandMessage.getScanId());
        assertEquals(List.of(ScanType.SOLACE_ALL), scanCommandMessage.getScanTypes());
        assertEquals(List.of(ScanDestination.EVENT_PORTAL, ScanDestination.FILE_WRITER), scanCommandMessage.getDestinations());

        assertNull(scanCommandMessage.getResources());

    }

    @Test
    void instantiateScanCommandMessageWithResources(){

        ScanCommandMessage scanCommandMessage = new ScanCommandMessage(
                "messagingServiceId1",
                "scanId1",
                List.of(ScanType.SOLACE_ALL),
                List.of(ScanDestination.EVENT_PORTAL, ScanDestination.FILE_WRITER),
                List.of(EventBrokerResourceConfigTestHelper.buildResourceConfiguration(ResourceConfigurationType.SOLACE))
        );
        assertNotNull(scanCommandMessage);
        assertEquals(MOPMessageType.generic,scanCommandMessage.getMopMsgType());
        assertEquals(MOPProtocol.scanDataControl,scanCommandMessage.getMopProtocol());
        assertEquals("1",scanCommandMessage.getMopVer());
        assertEquals(MOPUHFlag.ignore,scanCommandMessage.getMsgUh());

        assertEquals("messagingServiceId1", scanCommandMessage.getMessagingServiceId());
        assertEquals("scanId1", scanCommandMessage.getScanId());
        assertEquals(List.of(ScanType.SOLACE_ALL), scanCommandMessage.getScanTypes());
        assertEquals(List.of(ScanDestination.EVENT_PORTAL, ScanDestination.FILE_WRITER), scanCommandMessage.getDestinations());

        assertNotNull(scanCommandMessage.getResources());
        assertEquals(1,scanCommandMessage.getResources().size());

    }
}
