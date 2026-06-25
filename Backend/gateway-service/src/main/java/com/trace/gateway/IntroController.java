package com.trace.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IntroController {

    private final RestTemplate restTemplate;

    @GetMapping("/")
    public String intro(Model model) {
        log.info("Accès à la page d'introduction");
        // Check status of each service
        Map<String, Boolean> serviceStatuses = checkServiceStatuses();
        model.addAttribute("serviceStatuses", serviceStatuses);
        boolean allServicesUp = serviceStatuses.values().stream().allMatch(Boolean::booleanValue);
        model.addAttribute("allServicesUp", allServicesUp);
        log.info("Statut des services vérifié: tous actifs = {}", allServicesUp);
        return "intro";
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
