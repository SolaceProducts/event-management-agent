package com.solace.maas.ep.event.management.agent.repository.model.manualimport;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "MANUAL_IMPORT_DETAILS")
@Entity
public class ManualImportDetailsEntity implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "SCHEDULE_ID")
    private String scheduleId;

    @Column(name = "SCAN_ID")
    private String scanId;

    @Column(name = "TRACE_ID")
    private String traceId;

    @Column(name = "EMA_ID")
    private String emaId;

    @Column(name = "IMPORT_ID")
    private String importId;
}
