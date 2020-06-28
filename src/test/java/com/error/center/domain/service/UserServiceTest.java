package com.error.center.domain.service;

import com.error.center.domain.model.User;
import com.error.center.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = "test")
public class UserServiceTest {

    private static final String NAME = "Test";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "123456";

    @MockBean
    UserRepository repository;

    @Autowired
    UserService service;

    @BeforeEach
    public void setUp() {
        BDDMockito.given(repository.findByEmailEquals(Mockito.anyString())).willReturn(Optional.of(new User()));
        BDDMockito.given(repository.save(Mockito.any(User.class))).willReturn(getMockUser());
    }

    @Test
    public void testFindByEmail() {
        Optional<User> response = service.findByEmail(EMAIL);
        Assertions.assertTrue(response.isPresent());
    }

    @Test
    public void testSave() {

        User response = service.save(new User());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getName(), NAME);
        Assertions.assertEquals(response.getEmail(), EMAIL);
        Assertions.assertNotNull(response.getPassword());
    }

    private User getMockUser() {
        return new User(UUID.randomUUID(), NAME, EMAIL, PASSWORD);
    }
}
