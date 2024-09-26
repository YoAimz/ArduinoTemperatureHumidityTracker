package com.example.guppuppgift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.guppuppgift.Repository")
@EnableScheduling
public class GuppuppgiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuppuppgiftApplication.class, args);
    }
}