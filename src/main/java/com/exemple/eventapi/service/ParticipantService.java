package com.exemple.eventapi.service;

import com.exemple.eventapi.dto.ParticipantDTO;
import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.entity.Participant;
import com.exemple.eventapi.repository.EventRepository;
import com.exemple.eventapi.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer la logique métier des participants.
 */
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    public ParticipantService(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    // --- Mappers Entity ↔ DTO ---
    private ParticipantDTO toDTO(Participant participant) {
        return new ParticipantDTO(
                participant.getId(),
                participant.getNom(),
                participant.getEmail(),
                participant.getEvent() != null ? participant.getEvent().getId() : null
        );
    }

    private Participant toEntity(ParticipantDTO dto, Event event) {
        Participant participant = new Participant();
        participant.setId(dto.id());
        participant.setNom(dto.nom());
        participant.setEmail(dto.email());
        participant.setEvent(event);
        return participant;
    }

    // --- Logique métier ---
    public ParticipantDTO register(Long eventId, ParticipantDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        if (event.getParticipants().size() >= event.getCapacite()) {
            throw new RuntimeException("Plus de places disponibles pour cet événement");
        }

        Participant participant = toEntity(dto, event);
        Participant saved = participantRepository.save(participant);
        return toDTO(saved);
    }

    public List<ParticipantDTO> findByEvent(Long eventId) {
        return participantRepository.findByEventId(eventId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void unregister(Long eventId, Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));
        if (!participant.getEvent().getId().equals(eventId)) {
            throw new RuntimeException("Ce participant n'est pas inscrit à cet événement");
        }
        participantRepository.delete(participant);
    }
}
