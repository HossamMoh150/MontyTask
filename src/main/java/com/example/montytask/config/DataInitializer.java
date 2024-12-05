package com.example.montytask.config;


import com.example.montytask.models.entity.AppUser;
import com.example.montytask.reposetories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                AppUser admin =  AppUser.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("password123"))
                        .roles((List.of("ADMIN"))).build();


                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                AppUser user =  AppUser.builder()
                        .username("user")
                        .password(passwordEncoder.encode("password123"))
                        .roles((List.of("USER"))).build();
                userRepository.save(user);
            }
        };
    }
}
