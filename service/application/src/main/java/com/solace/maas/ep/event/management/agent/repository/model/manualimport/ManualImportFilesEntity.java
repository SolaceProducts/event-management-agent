package com.solace.maas.ep.event.management.agent.repository.model.manualimport;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "MANUAL_IMPORT_FILES")
@Entity
public class ManualImportFilesEntity implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "DATA_ENTITY_TYPE")
    private String dataEntityType;

    @Column(name = "SCAN_ID")
    private String scanId;
}
