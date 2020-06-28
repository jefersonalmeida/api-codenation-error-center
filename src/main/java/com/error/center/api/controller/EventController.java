package com.error.center.api.controller;

import com.error.center.api.model.EventInput;
import com.error.center.api.model.EventModel;
import com.error.center.api.model.EventSampleModel;
import com.error.center.api.response.ResponsePage;
import com.error.center.api.response.ResponseResult;
import com.error.center.domain.model.Event;
import com.error.center.domain.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(path = "events", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Events", description = "Recurso para gestão de logs.")
public class EventController {

    private final EventService eventService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Cadastrar evento de log", description = "Cria ou acrescenta evento de log.")
    @PostMapping
    public ResponseEntity<ResponseResult<EventSampleModel>> store(@Valid @RequestBody EventInput input) {
        ResponseResult<EventSampleModel> responseResult = new ResponseResult<>();
        Event event = eventService.save(toEntity(input));
        responseResult.setData(toModelSample(event));
        return ResponseEntity.status(event.getQuantity() <= 1 ? HttpStatus.CREATED : HttpStatus.OK).body(responseResult);
    }

    @Operation(summary = "Lista eventos de log", description = "Busca os eventos de log, filtra pelos parametros. " +
            "Caso queira fazer uma busca dentro de cada nível, pode-se passar o parametro level e continuar a busca.")
    @GetMapping
    public ResponseEntity<ResponsePage<List<EventSampleModel>>> index(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "level", required = false, defaultValue = "") String level,
            @RequestParam(name = "order", required = false, defaultValue = "date") String order,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort
    ) {
        Page<Event> items;

        if (!level.isEmpty()) {
            items = eventService.searchByLevel(search, level, order, sort, page);
        } else {
            items = eventService.search(search.toLowerCase(), order, sort, page);
        }

        return ResponseEntity.ok().body(toPaginateModel(items));
    }

    @Operation(summary = "Busca evento pelo ID", description = "Busca um evento de log pelo ID.")
    @GetMapping(value = "{id}")
    public ResponseEntity<ResponseResult<EventModel>> find(@PathVariable(name = "id") UUID id) {

        ResponseResult<EventModel> responseResult = new ResponseResult<>();

        Optional<Event> event = eventService.find(id);
        event.ifPresent(value -> responseResult.setData(toModel(value)));
        return ResponseEntity.status(HttpStatus.OK).body(responseResult);
    }

    @Operation(summary = "Lista eventos de log por data", description = "Busca os eventos de log pela data inicial e final.")
    @GetMapping(value = "date/{init}/{end}")
    public ResponseEntity<ResponsePage<List<EventSampleModel>>> findByDate(
            @PathVariable(name = "init") @DateTimeFormat(pattern = "yyyy-MM-dd") Date init,
            @PathVariable(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            @RequestParam(name = "search", required = false, defaultValue = "") String search,

            @RequestParam(name = "order", required = false, defaultValue = "date")
            @Pattern(regexp = "^(level|description|log|origin|date|quantity)$", message = "Para o order somente são aceitos os valores level|description|log|origin|date|quantity") String order,

            @RequestParam(name = "sort", required = false, defaultValue = "DESC")
            @Pattern(regexp = "^(ASC|DESC)$", message = "Para o order somente são aceitos os valores ASC|DESC") String sort,

            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {

        Page<Event> items = eventService.searchByDate(search, init, end, order, sort, page);
        return ResponseEntity.ok().body(toPaginateModel(items));
    }


    private EventModel toModel(Event model) {
        return modelMapper.map(model, EventModel.class);
    }

    private EventSampleModel toModelSample(Event model) {
        return modelMapper.map(model, EventSampleModel.class);
    }

    private List<EventSampleModel> toCollectionModel(List<Event> events) {
        return events.stream().map(this::toModelSample).collect(Collectors.toList());
    }

    private ResponsePage<List<EventSampleModel>> toPaginateModel(Page<Event> events) {
        ResponsePage<List<EventSampleModel>> page = new ResponsePage<>();
        page.setSize(events.getSize());
        page.setNumber(events.getNumber());
        page.setTotalElements(events.getTotalElements());
        page.setTotalPages(events.getTotalPages());
        page.setData(toCollectionModel(events.getContent()));
        return page;
    }

    private Event toEntity(EventInput input) {
        return modelMapper.map(input, Event.class);
    }
}
