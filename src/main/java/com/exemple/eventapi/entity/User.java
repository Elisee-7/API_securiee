package com.exemple.eventapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    private String email;

    @Column(nullable = false)
    private String role; // ADMIN, ORGANIZER, USER

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    @JsonIgnore   // ✅ évite la boucle infinie
    private List<Event> events;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore   // ✅ évite la boucle infinie
    private List<Participant> participations;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Event> getEvents() { return events; }
    public void setEvents(List<Event> events) { this.events = events; }

    public List<Participant> getParticipations() { return participations; }
    public void setParticipations(List<Participant> participations) { this.participations = participations; }

    public String getEmail() { return email; }   // ✅ corrige le getter
    public void setEmail(String email) { this.email = email; } // ✅ setter
}
