package com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.client;


import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.AboutApi;
import com.solace.maas.ep.event.management.agent.plugin.solaceconfig.processor.semp.MsgVpnApi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@ExcludeFromJacocoGeneratedReport
@SuppressWarnings("CPD-START")
@Slf4j
@Getter
public class SolaceSempApiClient {
    private SempApiClient sempApiClient;

    public SolaceSempApiClient(SempApiClient sempApiClient) {
        this.sempApiClient = sempApiClient;
    }

    public AboutApi getAboutApi() {
        return new AboutApi(this.sempApiClient.getApiClient());
    }

    public MsgVpnApi getMsgVpnApi() {
        MsgVpnApi api = new MsgVpnApi(this.sempApiClient.getApiClient());
        return api;
    }

}
