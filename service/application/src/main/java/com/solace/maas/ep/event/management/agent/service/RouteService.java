package com.solace.maas.ep.event.management.agent.service;

import com.solace.maas.ep.event.management.agent.repository.route.RouteRepository;
import com.solace.maas.ep.event.management.agent.repository.model.route.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Manges the creation and removal of Camel Routes.
 */
@Service
public class RouteService {
    private final RouteRepository repository;

    @Autowired
    public RouteService(RouteRepository repository) {
        this.repository = repository;
    }

    /**
     * Sets up a Camel Route.
     *
     * @param routeId The ID of the Route.
     * @return An Entity containing information about this Route.
     * @throws Exception Any exception that could occur.
     */
    public RouteEntity setupRoute(String routeId) {
        return findById(routeId)
                .orElse(save(RouteEntity.builder()
                        .id(routeId)
                        .childRouteIds("")
                        .active(true)
                        .build()));
    }

    public Optional<RouteEntity> findById(String id) {
        return repository.findById(id);
    }

    /**
     * Attempts to stop a Camel Route. If its successful we'll mark the Route as "inactive" and then save the
     * entry to the Database.
     *
     * @param route The Route to attempt to stop.
     */
    public void stopRoute(RouteEntity route) {
        route.setActive(false);

        save(route);
    }

    /**
     * Saves a Route's information to the Database.
     *
     * @param routeEntity The Route information to save.
     * @return The saved Route information.
     */
    protected RouteEntity save(RouteEntity routeEntity) {
        return repository.save(routeEntity);
    }
}
