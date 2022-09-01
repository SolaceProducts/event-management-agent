package com.solace.maas.ep.runtime.agent.repository.model.route;

import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "ROUTE")
@Entity
public class RouteEntity {
    @Id
    @Column(name = "ROUTE_ID")
    private String id;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @Column(name = "CHILD_ROUTE_ID")
    private String childRouteIds;

    @ManyToMany(mappedBy = "route", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<ScanEntity> scans;

    public String toString() {
        return "RouteEntity " + id;
    }
}
