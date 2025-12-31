package com.trace.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ProductApplication {
    public static void main(String[] args) {
        log.info("Démarrage du service de produit");
        SpringApplication.run(ProductApplication.class, args);
        log.info("Service de produit démarré avec succès");
    }
}
