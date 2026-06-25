package com.trace.dashboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final RestTemplate restTemplate;

    @PreAuthorize("hasAuthority('DASHBOARD_VIEW')")
    @GetMapping("/dashboard")
    public String dashboard() {
        log.info("Accès à la page du dashboard");
        return "dashboard";
    }

    private boolean checkAllServices() {
        log.debug("Vérification du statut de tous les services");
        try {
            restTemplate.getForObject("http://localhost:8761/eureka/apps", String.class);
            restTemplate.getForObject("http://localhost:8081/actuator/health", String.class);
            restTemplate.getForObject("http://localhost:8083/actuator/health", String.class);
            log.debug("Tous les services sont opérationnels");
            return true;
        } catch (Exception e) {
            log.warn("Erreur lors de la vérification des services: {}", e.getMessage());
            return false;
        }
    }

    private Map<String, Boolean> checkServiceStatuses() {
        log.debug("Vérification détaillée du statut de chaque service");
        Map<String, Boolean> statuses = new HashMap<>();
        try {
            restTemplate.getForObject("http://localhost:8761/eureka/apps", String.class);
            statuses.put("Eureka Server", true);
        } catch (Exception e) {
            statuses.put("Eureka Server", false);
            log.warn("Eureka Server: DOWN - {}", e.getMessage());
        }
        try {
            restTemplate.getForObject("http://localhost:8081/actuator/health", String.class);
            statuses.put("Core Service", true);
        } catch (Exception e) {
            statuses.put("Core Service", false);
            log.warn("Core Service: DOWN - {}", e.getMessage());
        }
        try {
            restTemplate.getForObject("http://localhost:8083/actuator/health", String.class);
            statuses.put("Dashboard Service", true);
        } catch (Exception e) {
            statuses.put("Dashboard Service", false);
            log.warn("Dashboard Service: DOWN - {}", e.getMessage());
        }
        return statuses;
    }
}
