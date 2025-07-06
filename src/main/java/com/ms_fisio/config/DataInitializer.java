package com.ms_fisio.config;

import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;
import com.ms_fisio.shared.domain.enums.AuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialize test data for development
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create test user if not exists
        if (!userRepository.existsByEmail("test@example.com")) {
            UserModel testUser = new UserModel();
            testUser.setFullName("Test User");
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setAuthProvider(AuthProvider.LOCAL);
            testUser.setEmailVerified(true);
            
            userRepository.save(testUser);
            log.info("Created test user: test@example.com / password123");
        }
    }
}
