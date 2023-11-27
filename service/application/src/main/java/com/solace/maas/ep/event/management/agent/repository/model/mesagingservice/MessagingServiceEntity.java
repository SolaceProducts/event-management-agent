package com.solace.maas.ep.event.management.agent.repository.model.mesagingservice;

import com.solace.maas.ep.event.management.agent.repository.model.scan.ScanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "MESSAGING_SERVICE")
@Entity
public class MessagingServiceEntity implements Serializable {
    @Id
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "MESSAGING_SERVICE_TYPE", nullable = false)
    private String type;

    @ToString.Exclude
    @OneToMany(mappedBy = "messagingService", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConnectionDetailsEntity> connections;

    @ToString.Exclude
    @OneToMany(mappedBy = "messagingService", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<ScanEntity> scanEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessagingServiceEntity that = (MessagingServiceEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
