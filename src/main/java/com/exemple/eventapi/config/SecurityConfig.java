package com.exemple.eventapi.config;
/**
 * Configuration de sécurité avec un utilisateur basique en mémoire.
 * - Login : admin
 * - Mot de passe : password (crypté avec BCrypt)
 * - Rôle : ADMIN
 */


import com.exemple.eventapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // 🔑 active @PreAuthorize
public class SecurityConfig {

    @Bean public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); // encodage sécurisé 
        }

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // désactive CSRF pour les APIs REST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll() // login accessible sans token
                .requestMatchers("/events/**").authenticated() // endpoints /events nécessitent un token
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // 🔑 ajoute ton filtre JWT
            .build();
    }

    // 🔑 Fournit un AuthenticationManager si tu en as besoin ailleurs
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    

    
}
