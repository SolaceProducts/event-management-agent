package com.solace.maas.ep.event.management.agent.repository.model.scan;

import com.solace.maas.ep.event.management.agent.plugin.jacoco.ExcludeFromJacocoGeneratedReport;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@ExcludeFromJacocoGeneratedReport
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SCAN_DESTINATION")
@Entity
@SuppressWarnings("CPD-START")
public class ScanDestinationEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String scanDestinationId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "SCAN_ID", referencedColumnName = "ID", nullable = false)
    private ScanEntity scan;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID", referencedColumnName = "ROUTE_ID", nullable = false)
    private RouteEntity route;

    @Column(name = "DESTINATION", nullable = false)
    private String destination;

    @Override
    public String toString() {
        return "ScanDestinationEntity " + scanDestinationId;
    }
}
