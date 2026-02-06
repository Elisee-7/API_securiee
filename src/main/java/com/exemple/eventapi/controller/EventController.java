
/**
 * Contrôleur REST pour gérer les événements.
 */

package com.exemple.eventapi.controller;

import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.service.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventService.findById(id);
    }

    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Event createEvent(@RequestBody Event event) {
        System.out.println(">>> createEvent appelé par: " +
            SecurityContextHolder.getContext().getAuthentication().getName() +
            " avec rôles: " +
            SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            System.out.println(">>> Contrôleur: " + SecurityContextHolder.getContext().getAuthentication());
        return eventService.save(event);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")   // 🔑 restriction aux ADMIN
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.update(id, event);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")   // 🔑 restriction aux ADMIN
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }


}
