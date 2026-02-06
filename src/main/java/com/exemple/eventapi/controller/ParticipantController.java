package com.exemple.eventapi.controller;

import com.exemple.eventapi.dto.ParticipantDTO;
import com.exemple.eventapi.entity.ParticipantStatus;
import com.exemple.eventapi.service.ParticipantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    /** 📝 Inscrire l’utilisateur connecté à un événement */
    @PostMapping("/register/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ParticipantDTO register(@PathVariable Long eventId) {
        return participantService.register(eventId);
    }

    /** ✏️ Mettre à jour le statut d’un participant (ADMIN ou ORGANIZER) */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ParticipantDTO updateStatus(@PathVariable Long id, @RequestParam ParticipantStatus status) {
        return participantService.updateStatus(id, status);
    }

    /** 👥 Récupérer les participants d’un événement */
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public List<ParticipantDTO> getParticipantsByEvent(@PathVariable Long eventId) {
        return participantService.findByEvent(eventId);
    }

    /** 👤 Récupérer les événements d’un utilisateur */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','ORGANIZER')")
    public List<ParticipantDTO> getParticipantsByUser(@PathVariable String username) {
        return participantService.findByUser(username);
    }

    /** 🔎 Récupérer les participants d’un événement par statut */
    @GetMapping("/event/{eventId}/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public List<ParticipantDTO> getParticipantsByEventAndStatus(@PathVariable Long eventId,
                                                                @PathVariable ParticipantStatus status) {
        return participantService.findByEventAndStatus(eventId, status);
    }
}
