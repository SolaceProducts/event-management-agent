package com.solace.maas.ep.event.management.agent.repository.model.scan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "ASYNC_SCAN_PROCESS")
public class AsyncScanProcessEntity {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "SCAN_ID")
    private String scanId;

    @Column(name = "SCAN_TYPE")
    private String scanType;

    @Column(name = "ACTIVE")
    private Boolean active;
}
