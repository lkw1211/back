package com.boardgaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRedisRepositories(basePackages = {"com.boardgaming.*"})
@EnableJpaRepositories(basePackages = {"com.boardgaming.*"})
@EntityScan(basePackages = {"com.boardgaming.*"})
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.boardgaming.*")
public class GomokuWsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GomokuWsApplication.class, args);
    }
}
