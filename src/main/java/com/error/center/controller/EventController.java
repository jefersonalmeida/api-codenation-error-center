package com.error.center.controller;

import com.error.center.dto.EventDTO;
import com.error.center.dto.EventListDTO;
import com.error.center.dto.PageDTO;
import com.error.center.entity.Event;
import com.error.center.mapper.EventMapper;
import com.error.center.response.Response;
import com.error.center.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Events", description = "Recurso para gestão de logs.")
public class EventController {

    private final EventMapper eventMapper = EventMapper.INSTANCE;
    private final EventService eventService;

    @Operation(summary = "Cadastrar evento de log", description = "Cria ou acrescenta evento de log.")
    @PostMapping
    public ResponseEntity<Response<EventListDTO>> store(@Valid @RequestBody EventDTO dto, BindingResult result) {

        Response<EventListDTO> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Event event = eventService.save(eventMapper.toEntity(dto));
        response.setData(eventMapper.toListDTO(event));
        return ResponseEntity.status(event.getQuantity() <= 1 ? HttpStatus.CREATED : HttpStatus.OK).body(response);
    }

    @Operation(summary = "Lista eventos de log", description = "Busca os eventos de log, filtra pelos parametros. " +
            "Caso queira fazer uma busca dentro de cada nível, pode-se passar o parametro level e continuar a busca.")
    @GetMapping
    public ResponseEntity<Response<PageDTO<EventListDTO>>> index(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "level", required = false, defaultValue = "") String level,
            @RequestParam(name = "order", required = false, defaultValue = "date") String order,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort
    ) {

        Response<PageDTO<EventListDTO>> response = new Response<>();

        Page<Event> items;

        if (!level.isEmpty()) {
            items = eventService.searchByLevel(search, level, order, sort, page);
        } else {
            items = eventService.search(search.toLowerCase(), order, sort, page);
        }

        PageDTO<EventListDTO> dto = eventMapper.page(items);
        response.setData(dto);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Busca evento pelo ID", description = "Busca um evento de log pelo ID.")
    @GetMapping(value = "{id}")
    public ResponseEntity<Response<EventDTO>> find(@PathVariable(name = "id") UUID id) {

        Response<EventDTO> response = new Response<>();

        Optional<Event> event = eventService.find(id);
        event.ifPresent(value -> response.setData(eventMapper.toDTO(value)));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Lista eventos de log por data", description = "Busca os eventos de log pela data inicial e final.")
    @GetMapping(value = "date/{init}/{end}")
    public ResponseEntity<Response<PageDTO<EventListDTO>>> findByDate(
            @PathVariable(name = "init") @DateTimeFormat(pattern = "yyyy-MM-dd") Date init,
            @PathVariable(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,

            @RequestParam(name = "order", required = false, defaultValue = "date")
            @Pattern(regexp = "^(level|description|log|origin|date|quantity)$", message = "Para o order somente são aceitos os valores level|description|log|origin|date|quantity") String order,

            @RequestParam(name = "sort", required = false, defaultValue = "DESC")
            @Pattern(regexp = "^(ASC|DESC)$", message = "Para o order somente são aceitos os valores ASC|DESC") String sort,

            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {

        Response<PageDTO<EventListDTO>> response = new Response<>();

        Page<Event> items = eventService.searchByDate(search, init, end, order, sort, page);

        PageDTO<EventListDTO> dto = eventMapper.page(items);
        response.setData(dto);
        return ResponseEntity.ok().body(response);
    }
}
