package com.exemple.eventapi.controller;

import com.exemple.eventapi.dto.ParticipantDTO;
import com.exemple.eventapi.service.ParticipantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les participants liés à un événement.
 */
@RestController
@RequestMapping("/api/v1/events/{eventId}/participants")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ParticipantDTO register(@PathVariable Long eventId, @RequestBody ParticipantDTO dto) {
        return participantService.register(eventId, dto);
    }

    @GetMapping
    public List<ParticipantDTO> getParticipants(@PathVariable Long eventId) {
        return participantService.findByEvent(eventId);
    }

    @DeleteMapping("/{participantId}")
    public void unregister(@PathVariable Long eventId, @PathVariable Long participantId) {
        participantService.unregister(eventId, participantId);
    }
}
