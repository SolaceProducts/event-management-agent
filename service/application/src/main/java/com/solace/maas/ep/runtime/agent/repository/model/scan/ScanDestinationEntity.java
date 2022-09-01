package com.solace.maas.ep.runtime.agent.repository.model.scan;

import com.solace.maas.ep.runtime.agent.repository.model.route.RouteEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SCAN_DESTINATION")
@Entity
@SuppressWarnings("CPD-START")
public class ScanDestinationEntity {
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

    public String toString() {
        return "ScanDestinationEntity " + scanDestinationId;
    }
}
