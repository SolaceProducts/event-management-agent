package com.solace.maas.ep.event.management.agent.repository.model.mesagingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SERVICE_ASSOCIATIONS")
@Entity
public class ServiceAssociationsEntity {

    @EmbeddedId
    private ServiceAssociationsCompositeKey serviceAssociationsId;
}