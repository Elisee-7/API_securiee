package com.exemple.eventapi.service;

import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service pour gérer la logique métier des événements.
 */
@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public Event update(Long id, Event event) {
        Event existant = findById(id);
        existant.setTitre(event.getTitre());
        existant.setDescription(event.getDescription());
        existant.setDate(event.getDate());
        existant.setLieu(event.getLieu());
        existant.setCapacite(event.getCapacite());
        return eventRepository.save(existant);
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}
