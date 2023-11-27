package com.solace.maas.ep.event.management.agent.plugin.terraform.client;

import org.springframework.stereotype.Service;

@Service
public class TerraformClientFactoryImpl implements TerraformClientFactory {
    @Override
    public TerraformClient createClient() {
        return new TerraformClient();
    }
}
