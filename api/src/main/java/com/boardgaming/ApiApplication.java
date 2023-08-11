package com.boardgaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRedisRepositories(basePackages = {"com.boardgaming.domain"})
@EnableJpaRepositories(basePackages = {"com.boardgaming.domain"})
@EntityScan(basePackages = {"com.boardgaming.domain"})
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication(scanBasePackages = {
	"com.boardgaming.domain.config",
	"com.boardgaming.core.config",
	"com.boardgaming.api",
})
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
