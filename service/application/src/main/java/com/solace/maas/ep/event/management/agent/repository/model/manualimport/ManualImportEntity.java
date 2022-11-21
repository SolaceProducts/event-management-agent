package com.solace.maas.ep.event.management.agent.repository.model.manualimport;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "MANUAL_IMPORT")
@Entity
public class ManualImportEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String id;

    @Column(name = "FILE_PATH")
    private String fileName;

    @Column(name = "SCHEDULE_ID")
    private String groupId;

    @Column(name = "SCAN_ID")
    private String scanId;

    @Column(name = "SCAN_TYPE")
    private String scanType;

    @Override
    public String toString() {
        return "ManualImportEntity{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", scanId='" + scanId + '\'' +
                ", scanType='" + scanType + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
