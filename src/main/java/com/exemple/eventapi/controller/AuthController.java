package com.exemple.eventapi.controller;

import com.exemple.eventapi.dto.RegisterRequest;
import com.exemple.eventapi.dto.UserDTO;
import com.exemple.eventapi.entity.User;
import com.exemple.eventapi.repository.UserRepository;
import com.exemple.eventapi.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(username, user.getRole());
        return Map.of("token", token);
    }



    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newToken = jwtService.refreshToken(refreshToken);
        return Map.of("token", newToken);
    }

   
@PostMapping("/register")
public UserDTO register(@RequestBody RegisterRequest request) {
    // Vérifier si le username existe déjà
    if (userRepository.findByUsername(request.username()).isPresent()) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Nom d'utilisateur déjà utilisé");
    }

    // Vérifier si l'email existe déjà
    if (userRepository.findByEmail(request.email()).isPresent()) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
    }

    User user = new User();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRole(request.role());

    User saved = userRepository.save(user);
    return new UserDTO(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole());
}




}

