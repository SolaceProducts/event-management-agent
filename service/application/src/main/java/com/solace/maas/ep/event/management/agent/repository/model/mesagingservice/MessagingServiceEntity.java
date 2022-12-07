package com.solace.maas.ep.event.management.agent.repository.model.mesagingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

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

    @OneToMany(mappedBy = "messagingService", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConnectionDetailsEntity> connections;
}
