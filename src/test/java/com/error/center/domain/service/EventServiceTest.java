package com.error.center.domain.service;

import com.error.center.api.model.EventModel;
import com.error.center.domain.enums.Level;
import com.error.center.domain.model.Event;
import com.error.center.domain.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest
@ActiveProfiles(value = "test")
public class EventServiceTest {

    private static final UUID ID = UUID.randomUUID();
    private static final Level LEVEL = Level.ERROR;
    private static final String DESCRIPTION = "Erro ao acessar o sistema com usuário cadastrado";
    private static final String LOG = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos";
    private static final String ORIGIN = "Origem do problema (SISTEMA X)";
    private static final Date DATE = new Date();
    private static final Integer QUANTITY = 1;
    @MockBean
    EventRepository repository;
    @Autowired
    EventService service;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        BDDMockito.given(repository.findById(ID)).willReturn(Optional.of(getMockEvent()));
        BDDMockito.given(repository.save(Mockito.any(Event.class))).willReturn(getMockEvent());
        BDDMockito.given(repository.findByLevelAndDescriptionIgnoreCaseAndLogIgnoreCaseAndOriginIgnoreCase(Mockito.any(Level.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).willReturn(Optional.of(getMockEvent()));
    }

    @Test
    public void testFindById() {
        Optional<Event> response = service.find(ID);
        Assertions.assertTrue(response.isPresent());
        EventModel dto = toModel(response.get());
        Assertions.assertEquals(dto.getLog(), LOG);
    }

    @Test
    public void testSearch() {
        List<Event> list = new ArrayList<>();
        list.add(getMockEvent());
        Page<Event> page = new PageImpl<>(list);

        BDDMockito.given(repository
                .search(
                        Mockito.anyString(),
                        Mockito.any(PageRequest.class)
                ))
                .willReturn(page);

        Page<Event> response = service.search("", "date", "DESC", 0);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getContent().size(), 1);
        Assertions.assertEquals(response.getContent().get(0).getDescription(), DESCRIPTION);
        Assertions.assertEquals(response.getContent().get(0).getLevel(), LEVEL);
        Assertions.assertEquals(response.getContent().get(0).getLog(), LOG);
    }

    @Test
    public void testSearchByLevel() {
        List<Event> list = new ArrayList<>();
        list.add(getMockEvent());
        Page<Event> page = new PageImpl<>(list);

        BDDMockito.given(repository
                .searchByLevel(
                        Mockito.anyString(),
                        Mockito.any(Level.class),
                        Mockito.any(PageRequest.class)
                ))
                .willReturn(page);

        Page<Event> response = service.searchByLevel("", Level.ERROR.getValue(), "date", "DESC", 0);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getContent().size(), 1);
        Assertions.assertEquals(response.getContent().get(0).getDescription(), DESCRIPTION);
        Assertions.assertEquals(response.getContent().get(0).getLevel(), LEVEL);
        Assertions.assertEquals(response.getContent().get(0).getLog(), LOG);
    }

    @Test
    public void testSearchByDate() {
        List<Event> list = new ArrayList<>();
        list.add(getMockEvent());
        Page<Event> page = new PageImpl<>(list);

        BDDMockito.given(repository
                .searchByDate(
                        Mockito.anyString(),
                        Mockito.any(Date.class),
                        Mockito.any(Date.class),
                        Mockito.any(PageRequest.class)
                ))
                .willReturn(page);

        Page<Event> response = service.searchByDate("", new Date(), new Date(), "date", "DESC", 0);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getContent().size(), 1);
        Assertions.assertEquals(response.getContent().get(0).getDescription(), DESCRIPTION);
        Assertions.assertEquals(response.getContent().get(0).getLog(), LOG);
    }

    @Test
    public void testSave() {

        Event response = service.save(new Event());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getLevel(), LEVEL);
        Assertions.assertEquals(response.getDescription(), DESCRIPTION);
        Assertions.assertEquals(response.getLog(), LOG);
        Assertions.assertEquals(response.getOrigin(), ORIGIN);
        Assertions.assertEquals(response.getDate(), DATE);
        Assertions.assertEquals(response.getQuantity(), QUANTITY);
    }

    @Test
    public void testSaveIncrement() {

        Optional<Event> event1 = service.findDuplicate(getMockEvent());
        event1.ifPresent(event -> service.save(event));

        Assertions.assertNotNull(event1.get());
        Assertions.assertEquals(event1.get().getLevel(), LEVEL);
        Assertions.assertEquals(event1.get().getDescription(), DESCRIPTION);
        Assertions.assertEquals(event1.get().getLog(), LOG);
        Assertions.assertEquals(event1.get().getOrigin(), ORIGIN);
        Assertions.assertEquals(event1.get().getDate(), DATE);
        Assertions.assertEquals(event1.get().getQuantity(), QUANTITY + 1);
    }

    private Event getMockEvent() {
        return new Event(ID, LEVEL, DESCRIPTION, LOG, ORIGIN, DATE, QUANTITY);
    }

    private EventModel toModel(Event model) {
        return modelMapper.map(model, EventModel.class);
    }
}
