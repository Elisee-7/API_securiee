package com.exemple.eventapi.repository;

import com.exemple.eventapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Event.
 * Hérite de JpaRepository afin de fournir automatiquement
 * les opérations CRUD (Create, Read, Update, Delete).
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
