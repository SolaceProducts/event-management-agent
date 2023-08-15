package com.solace.maas.ep.event.management.agent.repository.model.scan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SCAN_RECIPIENT_STORE")
@Entity
public class ScanRecipientStoreEntity {

    @Id
    @Column(name = "SCAN_ID")
    private String scanId;

    @Column(name = "STORE_KEY")
    private String storeKey;

    @Column(name = "PATH")
    private String path;
}
