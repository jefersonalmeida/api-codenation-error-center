package com.error.center.repository;

import com.error.center.entity.Event;
import com.error.center.util.enums.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = "test")
public class EventRepositoryTest {

    private static final Level LEVEL = Level.ERROR;
    private static final String DESCRIPTION = "Erro ao acessar o sistema com usuário cadastrado";
    private static final String LOG = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos";
    private static final String ORIGIN = "Origem do problema (SISTEMA X)";
    private static final Date DATE = new Date();
    private static final Integer QUANTITY = 1;
    @Autowired
    EventRepository repository;
    private UUID eventId = null;

    @BeforeEach
    public void setUp() {
        Event event = repository.save(getMockEvent());
        eventId = event.getId();
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    @WithMockUser
    public void testSave() {
        Event u = getMockEvent();
        u.setLevel(Level.WARNING);

        Event response = repository.save(u);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(u.getId(), response.getId());
        Assertions.assertEquals(u.getLevel(), response.getLevel());
    }

    @Test
    @WithMockUser
    public void testFindById() {

        Optional<Event> response = repository.findById(eventId);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get().getId(), eventId);
        Assertions.assertEquals(response.get().getLevel(), LEVEL);
        Assertions.assertEquals(response.get().getOrigin(), ORIGIN);
    }

    @Test
    @WithMockUser
    public void testCheckExists() {
        Optional<Event> response = repository.findByLevelEqualsAndOriginEqualsAndDateEquals(LEVEL, ORIGIN, DATE);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get().getOrigin(), ORIGIN);
        Assertions.assertEquals(response.get().getLevel(), LEVEL);
    }

    @Test
    @WithMockUser
    public void search() {

        LocalDateTime date = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Date currentDatePlusFiveDays = Date.from(date.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
        Date currentDatePlusSevenDays = Date.from(date.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());

        repository.save(new Event(null, Level.INFO, DESCRIPTION, LOG, ORIGIN, currentDatePlusFiveDays, QUANTITY));
        repository.save(new Event(null, LEVEL, DESCRIPTION, LOG, ORIGIN, currentDatePlusSevenDays, QUANTITY));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Event> response = repository.search("", pageRequest);

        Assertions.assertEquals(response.getContent().size(), 3);
        Assertions.assertEquals(response.getTotalElements(), 3);
        Assertions.assertEquals(response.getContent().get(0).getId(), eventId);
        Assertions.assertEquals(response.getContent().get(1).getLevel(), Level.INFO);
    }

    @Test
    @WithMockUser
    public void searchByLevel() {

        LocalDateTime date = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Date currentDatePlusFiveDays = Date.from(date.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
        Date currentDatePlusSevenDays = Date.from(date.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());

        Event event = repository.save(new Event(null, Level.INFO, DESCRIPTION, LOG, ORIGIN, currentDatePlusFiveDays, QUANTITY));
        repository.save(new Event(null, LEVEL, DESCRIPTION, LOG, ORIGIN, currentDatePlusSevenDays, QUANTITY));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Event> response = repository.searchByLevel("", Level.INFO, pageRequest);

        Assertions.assertEquals(response.getContent().size(), 1);
        Assertions.assertEquals(response.getTotalElements(), 1);
        Assertions.assertEquals(response.getContent().get(0).getId(), event.getId());
        Assertions.assertEquals(response.getContent().get(0).getLevel(), Level.INFO);
    }

    @Test
    @WithMockUser
    public void searchByDate() {

        LocalDateTime date = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Date currentDatePlusFiveDays = Date.from(date.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
        Date currentDatePlusSevenDays = Date.from(date.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());

        repository.save(new Event(null, LEVEL, DESCRIPTION, LOG, ORIGIN, currentDatePlusFiveDays, QUANTITY));
        repository.save(new Event(null, LEVEL, DESCRIPTION, LOG, ORIGIN, currentDatePlusSevenDays, QUANTITY));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Event> response = repository.searchByDate("", DATE, currentDatePlusFiveDays, pageRequest);

        Assertions.assertEquals(response.getContent().size(), 2);
        Assertions.assertEquals(response.getTotalElements(), 2);
        Assertions.assertEquals(response.getContent().get(0).getId(), eventId);
    }

    @Test
    @WithMockUser
    public void testDelete() {

        Optional<Event> response = repository.findById(eventId);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get().getLevel(), LEVEL);
        Assertions.assertEquals(response.get().getOrigin(), ORIGIN);

        repository.deleteById(response.get().getId());
        Optional<Event> deleted = repository.findById(eventId);
        Assertions.assertFalse(deleted.isPresent());
    }

    private Event getMockEvent() {
        return new Event(null, LEVEL, DESCRIPTION, LOG, ORIGIN, DATE, QUANTITY);
    }
}
