package com.error.center.domain.service;

import com.error.center.domain.enums.Level;
import com.error.center.domain.model.Event;
import com.error.center.domain.repository.EventRepository;
import com.error.center.domain.validator.EnumValidator;
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
public class EventService {

    private static final String VALIDATION_REGEX_ORDER = "^(level|description|log|origin|date|quantity)$";

    @Autowired
    private EventRepository eventRepository;

    @Value("${pagination.items_per_page}")
    private int itemsPerPage;

    public Event save(Event object) {
        object.setQuantity(1);
        System.out.println(object);
        Optional<Event> exists = this.findDuplicate(object);
        if (exists.isPresent()) {
            Event eventUpdated = exists.get();
            eventUpdated.increment();
            return eventRepository.save(eventUpdated);
        }
        return eventRepository.save(object);
    }

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
    }

    public Optional<Event> find(UUID id) {
        return eventRepository.findById(id);
    }

    public Page<Event> findAll() {
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, itemsPerPage, Sort.Direction.ASC, "name");
        return new PageImpl<>(eventRepository.findAll(), pageRequest, itemsPerPage);
    }

    public Optional<Event> findDuplicate(Event entity) {
        return eventRepository.findByLevelAndDescriptionIgnoreCaseAndLogIgnoreCaseAndOriginIgnoreCase(entity.getLevel(), entity.getDescription(), entity.getLog(), entity.getOrigin());
    }
}
