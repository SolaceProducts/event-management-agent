package com.solace.maas.ep.event.management.agent.repository.model.scan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SCAN_RECIPIENT_HIERARCHY")
@Entity
public class ScanRecipientHierarchyEntity {

    @Id
    @Column(name = "SCAN_ID")
    private String scanId;

    @ElementCollection
    @MapKeyColumn(name = "STORE_KEY")
    @Column(name = "PATH")
    @CollectionTable(name = "SCAN_RECIPIENT_STORE", joinColumns = @JoinColumn(name = "SCAN_ID"))
    private Map<String, String> store;
}
