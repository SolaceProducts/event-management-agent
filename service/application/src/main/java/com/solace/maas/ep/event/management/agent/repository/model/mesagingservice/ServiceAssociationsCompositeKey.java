package com.solace.maas.ep.event.management.agent.repository.model.mesagingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class ServiceAssociationsCompositeKey implements Serializable {

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private MessagingServiceEntity parent;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "child_id", referencedColumnName = "id", nullable = false)
    private MessagingServiceEntity child;

}
