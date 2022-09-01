package com.solace.maas.ep.runtime.agent.repository.model.file;

import com.solace.maas.ep.runtime.agent.repository.model.file.aggregation.AggregatedFileEntity;
import com.solace.maas.ep.runtime.agent.repository.model.scan.ScanEntity;
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
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "DATA_COLLECTION_FILE")
@Entity
public class DataCollectionFileEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String id;

    @Column(name = "PATH", nullable = false, unique = true)
    private String path;

    @Column(name = "PURGED", nullable = false)
    private boolean purged;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "SCAN_ID", referencedColumnName = "ID", nullable = false)
    private ScanEntity scan;

    @ManyToMany(targetEntity = AggregatedFileEntity.class, mappedBy = "files", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<AggregatedFileEntity> aggregatedFiles;
}
