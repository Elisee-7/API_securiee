package com.exemple.eventapi.repository;

import com.exemple.eventapi.entity.Participant;
import com.exemple.eventapi.entity.ParticipantStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.entity.User;
import java.util.Optional;
import java.util.List;

/**
 * Repository pour l'entité Participant.
 * Permet de gérer les opérations CRUD et de retrouver
 * les participants liés à un événement.
 */



public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByEvent(Event event);
    List<Participant> findByUser(User user);
    Optional<Participant> findByEventAndUser(Event event, User user);
    
    List<Participant> findByEventAndStatus(Event event, ParticipantStatus status);
    List<Participant> findByUserAndStatus(User user, ParticipantStatus status);
}

