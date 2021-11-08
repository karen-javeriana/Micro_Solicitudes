package com.solicitudes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = { "com.solicitudes.controller", "com.solicitudes.dao", "com.solicitudes.services",
		"com.solicitudes.config"})
@EntityScan(basePackages = { "com.solicitudes.model" })
@EnableJpaRepositories(basePackages = { "com.solicitudes.dao" })
@EnableMongoRepositories(basePackages = { "com.solicitudes.model", "com.solicitudes.dao" })	
@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class })
public class MicroservicioSolicitudesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioSolicitudesApplication.class, args);
	}

	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}

}