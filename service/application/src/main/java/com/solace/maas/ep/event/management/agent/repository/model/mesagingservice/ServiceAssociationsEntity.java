package com.solace.maas.ep.event.management.agent.repository.model.mesagingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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