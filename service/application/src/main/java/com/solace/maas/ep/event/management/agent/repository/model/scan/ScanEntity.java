package com.solace.maas.ep.event.management.agent.repository.model.scan;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SCAN")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ScanEntity implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @Column(name = "SCAN_TYPE", nullable = false)
    private String scanType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID", referencedColumnName = "ROUTE_ID")
    private List<RouteEntity> route;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<ScanDestinationEntity> destinations;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<ScanRecipientEntity> recipients;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<DataCollectionFileEntity> dataCollectionFiles;

    @ManyToOne(fetch = FetchType.LAZY)
    private MessagingServiceEntity messagingService;

    public String toString() {
        return "ScanEntity " + id;
    }

}
