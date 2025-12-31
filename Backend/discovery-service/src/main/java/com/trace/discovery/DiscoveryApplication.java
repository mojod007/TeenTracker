
package com.trace.discovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@Slf4j
public class DiscoveryApplication {
    public static void main(String[] args) {
        log.info("Démarrage du service de découverte Eureka");
        SpringApplication.run(DiscoveryApplication.class, args);
        log.info("Service de découverte Eureka démarré avec succès");
    }
}
