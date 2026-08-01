package com.example.fullstackapp.config;

import com.example.fullstackapp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt - one-way hash with built-in salting, industry standard for password storage
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public resources
                        .requestMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // H2 console is only active under the "dev" profile, used for local testing
                        .requestMatchers("/h2-console/**").permitAll()
                        // Only ADMIN can manage users and access the admin dashboard
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        // Product create/edit/delete restricted to ADMIN
                        .requestMatchers("/products/new", "/products/edit/**", "/products/delete/**").hasAuthority("ROLE_ADMIN")
                        // Anyone (including guests) can browse the product catalog
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/products", "/products/*").permitAll()
                        .requestMatchers("/api/products/**").authenticated()
                        .requestMatchers("/products/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                // Allow the H2 console (dev profile only) to render inside a frame
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                // CSRF stays enabled by default for all state-changing form submissions,
                // except for the stateless REST API which is protected by session auth + SameSite cookies.
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-console/**"));

        return http.build();
    }
}
