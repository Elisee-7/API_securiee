package com.exemple.eventapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour exposer ou recevoir les données d'un participant
 * sans exposer directement l'entité JPA.
 */
public record ParticipantDTO(
    Long id,

    @NotBlank(message = "Le nom est obligatoire")
    String nom,

    @Email(message = "L'email doit être valide")
    String email,

    Long eventId
) {}
