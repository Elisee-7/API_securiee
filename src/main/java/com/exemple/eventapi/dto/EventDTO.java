package com.exemple.eventapi.dto;

import java.time.LocalDate;

/**
 * DTO pour exposer un événement sans renvoyer directement l'entité JPA.
 */
public record EventDTO(
    Long id,
    String titre,
    String description,
    LocalDate date,
    String lieu,
    int capacite
) {}
