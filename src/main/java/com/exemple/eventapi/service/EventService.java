package com.exemple.eventapi.service;

import com.exemple.eventapi.dto.EventDTO;
import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.entity.User;
import com.exemple.eventapi.repository.EventRepository;
import com.exemple.eventapi.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer la logique métier des événements.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /** Conversion utilitaire vers DTO */
    private EventDTO toDTO(Event event) {
        return new EventDTO(
            event.getId(),
            event.getTitre(),
            event.getDescription(),
            event.getDate(),
            event.getLieu(),
            event.getCapacite(),
            event.getOrganizer() != null ? event.getOrganizer().getId() : null
        );
    }

    /** Récupérer tous les événements */
    public List<EventDTO> findAll() {
        return eventRepository.findAll()
                              .stream()
                              .map(this::toDTO)
                              .collect(Collectors.toList());
    }

    /** Récupérer un événement par ID */
    public EventDTO findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));
        return toDTO(event);
    }

    /** Récupérer les événements créés par l’utilisateur connecté (utile pour ORGANIZER) */
    public List<EventDTO> findMyEvents() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User organizer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return eventRepository.findByOrganizer(organizer)
                              .stream()
                              .map(this::toDTO)
                              .collect(Collectors.toList());
    }

    /** Créer un événement (lié à l’organisateur connecté) */
    public EventDTO save(Event event) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User organizer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Organisateur introuvable"));

        event.setOrganizer(organizer);
        Event saved = eventRepository.save(event);
        return toDTO(saved);
    }

    /** Mettre à jour un événement (ADMIN peut tout, ORGANIZER seulement ses propres événements) */
    public EventDTO update(Long id, Event event) {
        Event existant = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Vérification des droits
        if (!currentUser.getRole().equals("ADMIN") &&
            !existant.getOrganizer().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé : vous ne pouvez modifier que vos propres événements");
        }

        existant.setTitre(event.getTitre());
        existant.setDescription(event.getDescription());
        existant.setDate(event.getDate());
        existant.setLieu(event.getLieu());
        existant.setCapacite(event.getCapacite());

        Event updated = eventRepository.save(existant);
        return toDTO(updated);
    }

    /** Supprimer un événement (ADMIN peut tout, ORGANIZER seulement ses propres événements) */
    public void delete(Long id) {
        Event existant = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!currentUser.getRole().equals("ADMIN") &&
            !existant.getOrganizer().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé : vous ne pouvez supprimer que vos propres événements");
        }

        eventRepository.delete(existant);
    }

    /** 🔎 Recherche et filtrage par date, lieu, mot-clé */
    public List<EventDTO> search(String date, String lieu, String keyword) {
        List<Event> results;

        if (date != null && lieu != null) {
            results = eventRepository.findByDateAndLieu(LocalDate.parse(date), lieu);
        } else if (date != null) {
            results = eventRepository.findByDate(LocalDate.parse(date));
        } else if (lieu != null) {
            results = eventRepository.findByLieuContainingIgnoreCase(lieu);
        } else if (keyword != null) {
            results = eventRepository.findByTitreContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        } else {
            results = eventRepository.findAll();
        }

        return results.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
