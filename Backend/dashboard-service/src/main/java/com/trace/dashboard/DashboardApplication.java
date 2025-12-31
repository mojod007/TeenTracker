package com.trace.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
public class DashboardApplication {
	public static void main(String[] args) {
		log.info("Démarrage du service de tableau de bord");
		SpringApplication.run(DashboardApplication.class, args);
		log.info("Service de tableau de bord démarré avec succès");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
