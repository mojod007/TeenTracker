package com.trace.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class GatewayApplication {
    public static void main(String[] args) {
        log.info("Démarrage du service de passerelle Gateway");
        SpringApplication.run(GatewayApplication.class, args);
        log.info("Service de passerelle Gateway démarré avec succès");
    }
}
