package com.trace.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HealthCheckController {

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/api/services/status")
    public List<ServiceStatus> getServicesStatus() {
        return discoveryClient.getServices().stream()
                .map(this::getServiceStatus)
                .collect(Collectors.toList());
    }

    private ServiceStatus getServiceStatus(String serviceId) {
        try {
            // Assuming services are running on localhost and are registered with Eureka
            // The URL would be http://<service-id>/actuator/health
            // For simplicity, this example uses a direct call.
            // In a real scenario, you would use the service discovery to get the host and port.
            String url = "http://" + discoveryClient.getInstances(serviceId).get(0).getHost() + ":" + discoveryClient.getInstances(serviceId).get(0).getPort() + "/actuator/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().contains("\"status\":\"UP\"")) {
                return new ServiceStatus(serviceId, "UP");
            }
        } catch (Exception e) {
            // Service is down or not reachable
        }
        return new ServiceStatus(serviceId, "DOWN");
    }

    public static class ServiceStatus {
        private String name;
        private String status;

        public ServiceStatus(String name, String status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }
    }
}
