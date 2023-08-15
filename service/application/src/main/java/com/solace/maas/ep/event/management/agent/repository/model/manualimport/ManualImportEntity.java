package com.solace.maas.ep.event.management.agent.repository.model.manualimport;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "MANUAL_IMPORT")
@Entity
public class ManualImportEntity implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "SCHEDULE_ID")
    private String groupId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "SCAN_TYPE_ID", referencedColumnName = "ID", nullable = false)
    private ScanTypeEntity scanType;
}
