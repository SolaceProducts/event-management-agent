package com.solace.maas.ep.event.management.agent.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solace.client.sempv2.ApiException;
import com.solace.client.sempv2.api.RestDeliveryPointApi;
import com.solace.client.sempv2.model.MsgVpnRestDeliveryPointRestConsumer;
import com.solace.maas.ep.common.model.SempEntityType;
import com.solace.maas.ep.common.model.SempRdpRestConsumerPatchRequest;
import com.solace.maas.ep.event.management.agent.command.semp.SempApiProvider;
import com.solace.maas.ep.event.management.agent.plugin.command.model.Command;
import com.solace.maas.ep.event.management.agent.plugin.command.model.SempCommandConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SempPatchCommandManager extends AbstractSempCommandManager {

    private final ObjectMapper objectMapper;

    public SempPatchCommandManager(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @Override
    public String supportedSempCommand() {
        return SempCommandConstants.SEMP_PATCH_OPERATION;
    }

    @Override
    protected void executeSempCommand(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        String entityType = (String) command.getParameters().get(SempCommandConstants.SEMP_COMMAND_ENTITY_TYPE);
        SempEntityType patchEntityType;
        if (entityType == null) {
            throw new IllegalArgumentException("Entity type of a SEMP patch command must not be null");
        }
        try {
            patchEntityType = SempEntityType.valueOf(entityType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported SEMP patch entity type:" + entityType);
        }
        switch (patchEntityType) {
            case solaceRdpRestConsumer:
                executePatchRdpRestConsumer(command, sempApiProvider);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported entity type for patch: " + patchEntityType);
        }
    }



    private void executePatchRdpRestConsumer(Command command, SempApiProvider sempApiProvider) throws ApiException, JsonProcessingException {
        RestDeliveryPointApi restDeliveryPointApi = sempApiProvider.getRestDeliveryPointApi();
        SempRdpRestConsumerPatchRequest request = objectMapper.readValue(
                objectMapper.writeValueAsString(command.getParameters().get(SempCommandConstants.SEMP_COMMAND_DATA)),
                SempRdpRestConsumerPatchRequest.class);
        Validate.notNull(request.getData(), "Semp patch request data must not be null");
        Validate.notEmpty(request.getMsgVpn(), MSG_VPN_EMPTY_ERROR_MSG);
        Validate.notEmpty(request.getRdpName(), "RDP name must not be empty");
        Validate.notEmpty(request.getRestConsumerName(), "Rest Consumer name must not be empty");
        log.info("SEMP patch command: Patching Rest Consumer");
        try {
            MsgVpnRestDeliveryPointRestConsumer toPatch = request.getData();
            restDeliveryPointApi.updateMsgVpnRestDeliveryPointRestConsumer(
                    request.getMsgVpn(),
                    request.getRdpName(),
                    request.getRestConsumerName(),
                    toPatch,
                    null,
                    null
            );
        } catch (ApiException e) {
            handleSempOperationException(e, "Rest Consumer", supportedSempCommand());
        }
    }

}
