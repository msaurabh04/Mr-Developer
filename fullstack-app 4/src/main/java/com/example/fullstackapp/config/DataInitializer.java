package com.example.fullstackapp.config;

import com.example.fullstackapp.entity.Role;
import com.example.fullstackapp.entity.User;
import com.example.fullstackapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = User.builder()
                .fullName("Administrator")
                .username("admin")
                .email("admin@example.com")
                // Default password: Admin@123 - CHANGE THIS after first login in any real deployment
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build();

        userRepository.save(admin);

        System.out.println("======================================================");
        System.out.println(" Default admin account created:");
        System.out.println("   username: admin");
        System.out.println("   password: Admin@123");
        System.out.println(" Please change this password after first login.");
        System.out.println("======================================================");
    }
}
