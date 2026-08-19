package com.solace.maas.ep.event.management.agent.repository.messagingservice;

import com.solace.maas.ep.event.management.agent.repository.model.mesagingservice.MessagingServiceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessagingServiceRepository extends JpaRepository<MessagingServiceEntity, String> {

    // Pessimistic write lock (SELECT ... FOR UPDATE) to serialize concurrent upserts of the same broker.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM MessagingServiceEntity m WHERE m.id = :id")
    Optional<MessagingServiceEntity> findAndLockById(@Param("id") String id);
}
