package com.exemple.eventapi.dto;

/**
 * DTO pour exposer les statistiques détaillées d’un événement.
 */
public record EventStatsDTO(
    Long eventId,
    long totalParticipants,
    long confirmedParticipants,
    long cancelledParticipants,
    double fillRate
) {}
