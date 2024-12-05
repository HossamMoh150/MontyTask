package com.example.montytask.config;

import com.example.montytask.models.entity.AppUser;
import com.example.montytask.reposetories.UserRepository;
import com.example.montytask.service.AuthService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger("AuditLogger");

    @Autowired
    UserRepository authService ;
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String username = getAuthenticatedUsername();
        Long userId = getAuthenticatedUserId();

        String message = String.format("User: %s (ID: %d) is executing method: %s | Timestamp: %s",
                username, userId, joinPoint.getSignature().toShortString(), LocalDateTime.now());
        logger.info(message);

        saveAuditLog(username, userId, "Invoked " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(value = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String username = getAuthenticatedUsername();
        Long userId = getAuthenticatedUserId();

        String message = String.format("User: %s (ID: %d) executed method: %s | Result: %s | Timestamp: %s",
                username, userId, joinPoint.getSignature().toShortString(), result, LocalDateTime.now());
        logger.info(message);

        saveAuditLog(username, userId, "Successfully executed " + joinPoint.getSignature().toShortString());
    }

    private void saveAuditLog(String username, Long userId, String operation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("audit.log", true))) {
            String logEntry = String.format("User: %s (ID: %d) | Operation: %s | Timestamp: %s%n",
                    username, userId, operation, LocalDateTime.now());
            writer.write(logEntry);
        } catch (IOException e) {
            logger.error("Failed to write audit log", e);
        }
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User ?
                ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername() : "Unknown";
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof User) {
            AppUser appUser = authService.findByUsername(((User) auth.getPrincipal()).getUsername()).orElse(null);
           if(appUser!=null)
            return appUser.getId();

        }
        return -1L; // Default ID for unauthenticated users
    }
}
