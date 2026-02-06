package com.exemple.eventapi.controller;

import com.exemple.eventapi.dto.EventDTO;
import com.exemple.eventapi.dto.ParticipantDTO;
import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.service.EventService;
import com.exemple.eventapi.service.ParticipantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;
    private final ParticipantService participantService;

    public EventController(EventService eventService, ParticipantService participantService) {
        this.eventService = eventService;
        this.participantService = participantService;
    }

    /** 🔎 Lister tous les événements avec filtrage */
    @GetMapping
    public List<EventDTO> getAllEvents(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) String keyword) {
        return eventService.search(date, lieu, keyword);
    }

    /** 📌 Détail d’un événement */
    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    /** 👤 Événements de l’organisateur connecté */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ORGANIZER')")
    public List<EventDTO> getMyEvents() {
        return eventService.findMyEvents();
    }

    /** ➕ Créer un événement (ADMIN ou ORGANIZER) */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public EventDTO createEvent(@RequestBody Event event) {
        return eventService.save(event);
    }

    /** ✏️ Modifier un événement (ADMIN ou ORGANIZER propriétaire) */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public EventDTO updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.update(id, event);
    }

    /** 🗑️ Supprimer un événement (ADMIN ou ORGANIZER propriétaire) */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }

    /** 📝 Inscrire l’utilisateur connecté à un événement */
    @PostMapping("/{id}/register")
    @PreAuthorize("hasRole('USER')")
    public ParticipantDTO registerToEvent(@PathVariable Long id) {
        return participantService.register(id);
    }

    /** 👥 Liste des participants d’un événement (ADMIN ou ORGANIZER) */
    @GetMapping("/{id}/participants")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public List<ParticipantDTO> getParticipants(@PathVariable Long id) {
        return participantService.findByEvent(id);
    }
}
