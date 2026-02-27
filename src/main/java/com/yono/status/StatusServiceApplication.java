package com.yono.status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatusServiceApplication.class, args);
    }
}
