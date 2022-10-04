package com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Builder
public class SempClient {
    private WebClient webClient;
    private String username;
    private String password;
    private String msgVpn;
    private String connectionUrl;
}
