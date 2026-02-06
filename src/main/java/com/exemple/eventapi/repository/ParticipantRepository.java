package com.exemple.eventapi.repository;

import com.exemple.eventapi.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Participant.
 * Permet de gérer les opérations CRUD et de retrouver
 * les participants liés à un événement.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByEventId(Long eventId);
}
