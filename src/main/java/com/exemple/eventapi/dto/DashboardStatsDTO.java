package com.exemple.eventapi.dto;

/**
 * DTO pour exposer les statistiques globales du dashboard admin.
 */
public record DashboardStatsDTO(
    long totalEvents,
    long totalParticipants,
    double averageFillRate
) {}
