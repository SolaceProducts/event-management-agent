package com.solace.maas.ep.event.management.agent.plugin.contentstorage.service;

import com.solace.maas.ep.event.management.agent.plugin.contentstorage.config.JcrSessionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

@Service
@ConditionalOnProperty(name = "ema.content.enabled", havingValue = "true")
public class JcrSessionService {
    private final JcrSessionConfig jcrSessionConfig;

    private final Repository repository;

    @Autowired
    public JcrSessionService(JcrSessionConfig jcrSessionConfig, Repository repository) {
        this.jcrSessionConfig = jcrSessionConfig;
        this.repository = repository;
    }

    public Session createSession() throws RepositoryException {
        return repository.login(new SimpleCredentials(jcrSessionConfig.getUsername(),
                jcrSessionConfig.getPassword().toCharArray()));
    }
}
