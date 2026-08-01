package com.example.fullstackapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class FullstackAppApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the Spring application context starts successfully
        // using the in-memory H2 "dev" profile.
    }
}
