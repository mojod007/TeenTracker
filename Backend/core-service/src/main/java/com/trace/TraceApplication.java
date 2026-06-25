package com.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class TraceApplication {
    public static void main(String[] args) {
        log.info("Démarrage du Core Service");
        SpringApplication.run(TraceApplication.class, args);
        log.info("Core Service démarré avec succès");
    }
}
