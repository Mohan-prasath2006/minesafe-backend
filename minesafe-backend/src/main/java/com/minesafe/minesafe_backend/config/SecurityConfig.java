package com.minesafe.minesafe_backend.config;

import com.minesafe.minesafe_backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =========================
    // SECURITY CONFIGURATION
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // =========================
                // CORS
                // =========================

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                // =========================
                // CSRF
                // =========================

                .csrf(csrf ->
                        csrf.disable()
                )

                // =========================
                // JWT = STATELESS
                // =========================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =========================
                // AUTHORIZATION
                // =========================

                .authorizeHttpRequests(auth -> auth

                        // CORS preflight request
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // =========================
                        // PUBLIC APIs
                        // =========================

                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/verify-email",
                                "/api/users/login",
                                "/api/users/forgot-password",
                                "/api/users/verify-reset-otp",
                                "/api/users/reset-password"
                        ).permitAll()

                        // =========================
                        // PROTECTED API
                        // =========================

                        .requestMatchers(
                                "/api/users/profile"
                        ).authenticated()

                        // Other APIs
                        .anyRequest().permitAll()
                )

                // =========================
                // JWT FILTER
                // =========================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // =========================
    // CORS CONFIGURATION
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // React Vite frontend
        configuration.setAllowedOriginPatterns(
                List.of(
                        "http://localhost:5173",
                        "https://*.trycloudflare.com",
                        "https://localhost",
                        "capacitor://localhost",
                        "http://localhost"
                )
        );

        // Allowed HTTP methods
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // Allowed headers
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type"
                )
        );

        // Allow credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}