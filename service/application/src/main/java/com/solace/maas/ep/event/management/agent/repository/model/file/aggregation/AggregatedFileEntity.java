package com.solace.maas.ep.event.management.agent.repository.model.file.aggregation;

import com.solace.maas.ep.event.management.agent.repository.model.file.DataCollectionFileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "AGGREGATED_FILE")
@Entity
public class AggregatedFileEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String id;

    @Column(name = "PATH", nullable = false, unique = true)
    private String path;

    @Column(name = "PURGED", nullable = false)
    private boolean purged;

    @ManyToMany(targetEntity = DataCollectionFileEntity.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<DataCollectionFileEntity> files;
}
