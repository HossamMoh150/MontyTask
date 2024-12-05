package com.example.montytask.service;


import com.example.montytask.models.DTO.AuthResponse;
import com.example.montytask.models.DTO.LoginRequest;
import com.example.montytask.models.DTO.RegisterRequest;
import com.example.montytask.models.entity.AppUser;
import com.example.montytask.reposetories.UserRepository;
import com.example.montytask.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        Optional<AppUser> byUsername = userRepository.findByUsername(request.getUsername());
        if(byUsername.isPresent())
        {
            throw new RuntimeException("username is already taken");
        }


        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singletonList(request.getRole()));
        userRepository.save(user);

        String token = JwtUtil.generateToken(user.getUsername(), user.getRoles().get(0));
        return new AuthResponse(token, user.getUsername(), user.getRoles().get(0));
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRoles().get(0));
        return new AuthResponse(token, user.getUsername(),  user.getRoles().get(0));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("ROLE_"+ Arrays.toString(user.getRoles().toArray(new String[0])))
                .build();
    }



}
