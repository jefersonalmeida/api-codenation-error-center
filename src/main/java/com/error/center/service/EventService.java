package com.error.center.service;

import com.error.center.entity.Event;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Optional<Event> checkExists(Event entity);

    Event save(Event object);

    Page<Event> search(String search, String order, String sort, int page);

    Page<Event> searchByLevel(String search, String level, String order, String sort, int page);

    Page<Event> searchByDate(String search, Date init, Date end, String order, String sort, int page);

    Optional<Event> find(UUID id);
}
