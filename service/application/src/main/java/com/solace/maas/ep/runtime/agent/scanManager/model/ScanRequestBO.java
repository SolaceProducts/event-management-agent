package com.solace.maas.ep.runtime.agent.scanManager.model;

import com.solace.maas.ep.runtime.agent.model.AbstractBaseBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("PMD")
public class ScanRequestBO extends AbstractBaseBO<String> {

    private String messagingServiceId;

    private String scanId;

    private List<String> scanTypes;

    private List<String> destinations;

}
