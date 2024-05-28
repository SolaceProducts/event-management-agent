package com.solace.maas.ep.common.messages.resources;

import lombok.Data;

@Data
public class Credential {
    private String userName;
    private String password;
}
