package com.solace.maas.ep.common.model;

import com.solace.client.sempv2.model.MsgVpnRestDeliveryPointRestConsumer;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SempRdpRestConsumerPatchRequest {
    private String rdpName;
    private String msgVpn;
    private String restConsumerName;
    private MsgVpnRestDeliveryPointRestConsumer data;
}