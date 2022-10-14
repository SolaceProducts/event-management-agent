package com.solace.maas.ep.event.management.agent.repository.route;

import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends CrudRepository<RouteEntity, String> {
}
