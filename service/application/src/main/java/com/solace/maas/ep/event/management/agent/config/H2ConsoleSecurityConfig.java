package com.solace.maas.ep.event.management.agent.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@Profile("!TEST")
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type:}')")
public class H2ConsoleSecurityConfig {

    @PostConstruct
    public void logSecurityStatus() {
        log.info("H2 Console Security: COMPLETELY DISABLED");
        log.info("H2 Console has been forcibly disabled for security reasons");
    }

    // NOTE (DATAGO-142260): SB4 removed H2 console auto-configuration, so the former @Bean @Primary
    // force-disable override is gone. The spring.h2.console.enabled=true in application*.yml now has
    // no consumer, so the console can't start; the blocking SecurityFilterChain below is the active
    // defense. Security-adjacent — verify live (GET /h2-console/ denied).

    /**
     * Security filter chain that explicitly denies all access to H2 console endpoints.
     * This provides defense-in-depth by blocking access even if H2 console somehow gets enabled.
     */
    @Bean
    public SecurityFilterChain h2ConsoleBlockingSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring H2 Console Blocking Security Filter Chain");

        // Explicitly deny all access to H2 console paths
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/h2/**", "/h2-console/**").denyAll()
                        .anyRequest().permitAll());

        log.info("H2 Console: All access to /h2/** and /h2-console/** endpoints BLOCKED");
        return http.build();
    }
}
