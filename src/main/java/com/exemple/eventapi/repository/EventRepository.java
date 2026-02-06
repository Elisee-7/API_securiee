package com.exemple.eventapi.repository;

import com.exemple.eventapi.entity.Event;
import com.exemple.eventapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour l'entité Event.
 * Hérite de JpaRepository afin de fournir automatiquement
 * les opérations CRUD (Create, Read, Update, Delete).
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /** 🔑 Événements créés par un organisateur */
    List<Event> findByOrganizer(User organizer);

    /** 🔎 Recherche par date exacte */
    List<Event> findByDate(LocalDate date);

    /** 🔎 Recherche par lieu (contient, insensible à la casse) */
    List<Event> findByLieuContainingIgnoreCase(String lieu);

    /** 🔎 Recherche par mot-clé dans titre ou description */
    List<Event> findByTitreContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titre, String description);

    /** 🔎 Recherche combinée date + lieu */
    List<Event> findByDateAndLieu(LocalDate date, String lieu);

    /** 🔎 Recherche combinée date + lieu + mot-clé */
    List<Event> findByDateAndLieuAndTitreContainingIgnoreCase(LocalDate date, String lieu, String keyword);
}
