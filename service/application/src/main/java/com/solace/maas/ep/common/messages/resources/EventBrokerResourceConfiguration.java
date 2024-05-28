package com.solace.maas.ep.common.messages;

import lombok.Data;

import java.util.List;

@Data
public class MessagingServiceConfiguration {
    private String id;
    private String brokerType;
    private String name;
    private List<ConnectionDetail> connections;
}
