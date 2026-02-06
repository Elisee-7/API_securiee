package com.exemple.eventapi.service;

import com.exemple.eventapi.dto.ParticipantDTO;
import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.entity.Participant;
import com.exemple.eventapi.entity.ParticipantStatus;
import com.exemple.eventapi.entity.User;
import com.exemple.eventapi.repository.EventRepository;
import com.exemple.eventapi.repository.ParticipantRepository;
import com.exemple.eventapi.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ParticipantService(ParticipantRepository participantRepository,
                              EventRepository eventRepository,
                              UserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /** Conversion utilitaire vers DTO */
    private ParticipantDTO toDTO(Participant participant) {
        return new ParticipantDTO(
            participant.getId(),
            participant.getUser() != null ? participant.getUser().getId() : null,
            participant.getEvent() != null ? participant.getEvent().getId() : null
        );
    }

    /** 📝 Inscrire l’utilisateur connecté à un événement */
    public ParticipantDTO register(Long eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        // Vérifier si déjà inscrit
        participantRepository.findByEventAndUser(event, user)
                .ifPresent(p -> { throw new RuntimeException("Déjà inscrit à cet événement"); });

        // Vérifier la capacité
        long count = participantRepository.findByEvent(event).size();
        if (event.getCapacite() != null && count >= event.getCapacite()) {
            throw new RuntimeException("Capacité maximale atteinte pour cet événement");
        }

        Participant participant = new Participant();
        participant.setEvent(event);
        participant.setUser(user);
        participant.setStatus(ParticipantStatus.REGISTERED);

        Participant saved = participantRepository.save(participant);
        return toDTO(saved);
    }

    /** ✏️ Changer le statut d’un participant (ADMIN ou ORGANIZER) */
    public ParticipantDTO updateStatus(Long participantId, ParticipantStatus status) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant introuvable"));

        participant.setStatus(status);
        Participant updated = participantRepository.save(participant);
        return toDTO(updated);
    }

    /** 👥 Récupérer les participants d’un événement */
    public List<ParticipantDTO> findByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));
        return participantRepository.findByEvent(event)
                                    .stream()
                                    .map(this::toDTO)
                                    .collect(Collectors.toList());
    }

    /** 👤 Récupérer les événements d’un utilisateur */
    public List<ParticipantDTO> findByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return participantRepository.findByUser(user)
                                    .stream()
                                    .map(this::toDTO)
                                    .collect(Collectors.toList());
    }

    /** 🔎 Récupérer les participants d’un événement par statut */
    public List<ParticipantDTO> findByEventAndStatus(Long eventId, ParticipantStatus status) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));
        return participantRepository.findByEventAndStatus(event, status)
                                    .stream()
                                    .map(this::toDTO)
                                    .collect(Collectors.toList());
    }
}
