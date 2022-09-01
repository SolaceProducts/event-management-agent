package com.solace.maas.ep.runtime.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractBaseBO<K> implements BaseBO<K> {

    protected Long createdTime;

    protected Long updatedTime;

    protected String createdBy;

    protected String updatedBy;
}
