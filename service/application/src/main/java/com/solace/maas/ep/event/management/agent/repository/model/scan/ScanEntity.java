package com.solace.maas.ep.event.management.agent.repository.model.scan;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column(name = "EVENTMANAGEMENT_AGENT_ID", nullable = false)
    private String emaId;

    @Column(name = "TRACE_ID")
    private String traceId;

    @Column(name = "ACTOR_ID")
    private String actorId;

    @OneToMany(mappedBy = "scan", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<ScanTypeEntity> scanTypes;

    @ManyToMany(fetch = FetchType.LAZY)
    @CollectionTable(name = "SCAN_ROUTE", joinColumns = @JoinColumn(name = "ROUTE_ID"))
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "MESSAGING_SERVICE_ID", referencedColumnName = "ID", nullable = false)
    private MessagingServiceEntity messagingService;

    @Override
    public String toString() {
        return "ScanEntity " + id;
    }

}
