package com.exemple.eventapi.entity;

import jakarta.persistence.*;

/**
 * Entité représentant un participant inscrit à un événement.
 * Chaque participant possède un nom, un email et est lié à un événement.
 */
@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
