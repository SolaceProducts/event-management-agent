package com.solace.maas.ep.event.management.agent.repository.model.mesagingservice;

import com.solace.maas.ep.event.management.agent.plugin.messagingService.event.EventProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Table(name = "CREDENTIAL_PROPERTIES")
@Entity
public class CredentialPropertiesEntity extends EventProperty implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "[VALUE]", nullable = false)
    private String value;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CREDENTIAL_DETAILS_ID", referencedColumnName = "ID", nullable = false)
    private CredentialDetailsEntity credentials;
}
