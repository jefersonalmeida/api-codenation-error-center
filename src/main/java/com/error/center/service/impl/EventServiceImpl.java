package com.error.center.service.impl;

import com.error.center.entity.Event;
import com.error.center.repository.EventRepository;
import com.error.center.service.EventService;
import com.error.center.util.enums.Level;
import com.error.center.validators.enums.EnumValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private static final String VALIDATION_REGEX_ORDER = "^(level|description|log|origin|date|quantity)$";
    private static final String VALIDATION_REGEX_SORT = "^(ASC|DESC)$";

    @Autowired
    private EventRepository eventRepository;

    @Value("${pagination.items_per_page}")
    private int itemsPerPage;

    @Override
    public Event save(Event object) {

        Optional<Event> exists = this.checkExists(object);
        if (exists.isPresent()) {
            Event eventUpdated = exists.get();
            eventUpdated.increment();
            return eventRepository.save(eventUpdated);
        }
        return eventRepository.save(object);
    }

    @Override
    public Page<Event> search(
            String search,
            String order,
            String sort,
            int page) {

        order = checkOrder(order);
        PageRequest pageRequest = PageRequest.of(
                page,
                itemsPerPage,
                sort.toUpperCase().equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                order.toLowerCase()
        );

        return eventRepository.search(search, pageRequest);
    }

    @Override
    public Page<Event> searchByLevel(
            String search,
            @EnumValidator(enumClass = Level.class, ignoreCase = true, message = "Valor escolhido para level é inválido") String level,
            String order,
            String sort,
            int page) {
        order = checkOrder(order);
        PageRequest pageRequest = PageRequest.of(
                page,
                itemsPerPage,
                sort.toUpperCase().equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                order.toLowerCase()
        );
        return this.eventRepository.searchByLevel(search, Level.getEnum(level), pageRequest);
    }

    @Override
    public Page<Event> searchByDate(String search, Date init, Date end, String order, String sort, int page) {
        order = checkOrder(order);
        PageRequest pageRequest = PageRequest.of(
                page,
                itemsPerPage,
                sort.toUpperCase().equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                order.toLowerCase()
        );
        return this.eventRepository.searchByDate(search, init, end, pageRequest);
    }

    @NotNull
    private String checkOrder(String order) {
        return order.toLowerCase().matches(VALIDATION_REGEX_ORDER) ? order : "date";
        // return VALIDATION_REGEX_ORDER.matches(order.toLowerCase()) ? order : "date";
    }

    @Override
    public Optional<Event> find(UUID id) {
        return eventRepository.findById(id);
    }

    public Page<Event> findAll() {
        int page = 0;
        PageRequest pageRequest = PageRequest.of(
                page,
                itemsPerPage,
                Sort.Direction.ASC,
                "name");
        return new PageImpl<>(eventRepository.findAll(), pageRequest, itemsPerPage);
    }

    @Override
    public Optional<Event> checkExists(Event entity) {
        return eventRepository.findByLevelEqualsAndOriginEqualsAndDateEquals(entity.getLevel(), entity.getOrigin(), entity.getDate());
    }
}
