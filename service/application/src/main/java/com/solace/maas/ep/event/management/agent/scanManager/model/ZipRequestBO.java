package com.solace.maas.ep.event.management.agent.scanManager.model;

import com.solace.maas.ep.event.management.agent.model.AbstractBaseBO;
import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class ZipRequestBO extends AbstractBaseBO<String> {

    private String scanId;
}
