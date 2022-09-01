package com.solace.maas.ep.runtime.agent.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long createdTime;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long updatedTime;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String createdBy;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String updatedBy;
}
