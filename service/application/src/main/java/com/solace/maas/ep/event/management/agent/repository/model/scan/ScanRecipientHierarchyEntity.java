package com.solace.maas.ep.event.management.agent.repository.model.scan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
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
