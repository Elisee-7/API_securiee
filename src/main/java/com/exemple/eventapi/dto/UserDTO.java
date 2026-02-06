package com.exemple.eventapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour exposer ou recevoir les données d'un utilisateur
 * sans exposer directement l'entité JPA (notamment le mot de passe).
 */
public record UserDTO(
    Long id,

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    String username,

    @Email(message = "L'email doit être valide")
    String email,

    @NotBlank(message = "Le rôle est obligatoire")
    String role
) {}
