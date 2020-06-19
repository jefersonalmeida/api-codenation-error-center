package com.error.center.controller;

import com.error.center.dto.EventDTO;
import com.error.center.entity.Event;
import com.error.center.service.EventService;
import com.error.center.util.enums.Level;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class EventControllerTest {

    private static final UUID ID = UUID.fromString("53e2a983-5b29-4527-8f59-e64c3b756f15");
    private static final String LEVEL = Level.ERROR.toString();
    private static final String DESCRIPTION = "Erro ao acessar o sistema com usuário cadastrado";
    private static final String LOG = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos";
    private static final String ORIGIN = "Origem do problema (SISTEMA X)";
    private static final Date DATE = new Date();
    private static final Integer QUANTITY = 1;
    private static final LocalDate TODAY = LocalDate.now();
    private static final String URL_BASE = "/events";

    @MockBean
    EventService eventService;

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    public void testSave() throws Exception {

        BDDMockito.given(eventService.save(Mockito.any(Event.class))).willReturn(getMockEvent());

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                .content(getJsonPayload(LEVEL, DESCRIPTION, LOG, ORIGIN, DATE))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.level").value(LEVEL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.log").value(LOG))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.origin").value(ORIGIN))
        ;
    }

    @Test
    @WithMockUser
    public void testSaveInvalidLevel() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                .content(getJsonPayload("ERRO", DESCRIPTION, LOG, ORIGIN, DATE))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]")
                        .value("Para o nível somente são aceitos os valores: ERROR|WARNING|INFO"))
        ;
    }

    @Test
    @WithMockUser
    public void testSearch() throws Exception {
        List<Event> list = new ArrayList<>();
        list.add(getMockEvent());
        Page<Event> page = new PageImpl<>(list);

        BDDMockito.given(eventService.search(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        )).willReturn(page);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].level").value(LEVEL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].description").value(DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].log").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].origin").value(ORIGIN))
        ;
    }

    @Test
    @WithMockUser
    public void testSearchLevel() throws Exception {
        List<Event> list = new ArrayList<>();
        list.add(getMockEvent());
        Page<Event> page = new PageImpl<>(list);

        BDDMockito.given(eventService.searchByLevel(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        )).willReturn(page);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "?level=" + LEVEL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].level").value(LEVEL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].description").value(DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].log").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].origin").value(ORIGIN))
        ;
    }


    @Test
    @WithMockUser
    public void testSearchByDate() throws Exception {
        List<Event> list = new ArrayList<>();
        list.add(getMockEvent());
        Page<Event> page = new PageImpl<>(list);

        String init = TODAY.format(getDateFormater());
        String end = TODAY.plusDays(5).format(getDateFormater());

        BDDMockito.given(eventService.searchByDate(
                Mockito.anyString(),
                Mockito.any(Date.class),
                Mockito.any(Date.class),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        )).willReturn(page);

        mvc.perform(MockMvcRequestBuilders.get(URL_BASE + "/date/" + init + "/" + end)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].level").value(LEVEL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].description").value(DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].log").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].origin").value(ORIGIN))
        ;
    }

    private Event getMockEvent() {
        return new Event(ID, Level.getEnum(LEVEL), DESCRIPTION, LOG, ORIGIN, DATE, QUANTITY);
    }

    public String getJsonPayload(
            String level,
            String description,
            String log,
            String origin,
            Date date
    ) throws JsonProcessingException {

        EventDTO dto = new EventDTO();
        dto.setLevel(level);
        dto.setDescription(description);
        dto.setLog(log);
        dto.setOrigin(origin);
        dto.setDate(date);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }

    private DateTimeFormatter getDateFormater() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
}
