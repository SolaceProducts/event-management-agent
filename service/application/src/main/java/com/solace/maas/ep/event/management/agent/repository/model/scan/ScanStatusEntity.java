package com.solace.maas.ep.event.management.agent.repository.model.scan;

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
@Table(name = "SCAN_STATUS")
@Entity
public class ScanStatusEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String id;

    @Column(name = "SCAN_ID")
    private String scanId;

    @Column(name = "SCAN_TYPE")
    private String scanType;

    @Column(name = "STATUS")
    private String status;

    @Override
    public String toString() {
        return "scanStatusEntity{" +
                "id='" + id + '\'' +
                ", scanId='" + scanId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
