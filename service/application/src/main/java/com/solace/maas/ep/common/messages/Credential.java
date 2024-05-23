package com.solace.maas.ep.common.messages;

import lombok.Data;

@Data
public class Credential {
    private String userName;
    private String password;
}
