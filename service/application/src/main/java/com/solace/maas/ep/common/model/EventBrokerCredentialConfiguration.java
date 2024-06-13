package com.solace.maas.ep.common.model;

import lombok.Data;

@Data
public class EventBrokerCredentialConfiguration {
    private String userName;
    private String password;
}