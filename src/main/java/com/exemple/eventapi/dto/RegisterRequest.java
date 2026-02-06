package com.exemple.eventapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour l'inscription d'un utilisateur.
 */
public record RegisterRequest(
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    String username,

    @Email(message = "L'email doit être valide")
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    String password,

    @NotBlank(message = "Le rôle est obligatoire")
    String role // USER, ADMIN, ORGANIZER
) {}
