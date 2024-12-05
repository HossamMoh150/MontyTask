package com.example.montytask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MontyTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MontyTaskApplication.class, args);
    }

}
