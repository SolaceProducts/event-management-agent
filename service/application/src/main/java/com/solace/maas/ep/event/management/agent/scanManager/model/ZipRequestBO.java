package com.solace.maas.ep.event.management.agent.scanManager.model;

import com.solace.maas.ep.event.management.agent.model.AbstractBaseBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class ZipRequestBO extends AbstractBaseBO<String> {

    private String messagingServiceId;

    private String scanId;

    @Override
    public String toString() {
        return "ZipRequestBO{" +
                "messagingServiceId='" + messagingServiceId + '\'' +
                ", scanId='" + scanId + '\'' +
                '}';
    }
}
