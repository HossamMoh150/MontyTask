package com.example.montytask;

import com.example.montytask.models.DTO.AuthResponse;
import com.example.montytask.models.DTO.RegisterRequest;
import com.example.montytask.models.entity.AppUser;
import com.example.montytask.reposetories.UserRepository;
import com.example.montytask.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("testUser", "password123", "USER");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        AuthResponse response = authService.register(request);

        assertEquals("testUser", response.getUsername());
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void shouldThrowExceptionIfUsernameTaken() {
        RegisterRequest request = new RegisterRequest("testUser", "password123", "USER");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new AppUser()));

        assertThrows(RuntimeException.class, () -> authService.register(request));
    }
}
