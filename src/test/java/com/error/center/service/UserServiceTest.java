package com.error.center.service;

import com.error.center.entity.User;
import com.error.center.repository.UserRepository;
import org.junit.jupiter.api.*;
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
        Assertions.assertEquals(response.getEmail(), EMAIL);
        Assertions.assertEquals(response.getPassword(), PASSWORD);
    }

    private User getMockUser() {
        return new User(UUID.randomUUID(), EMAIL, PASSWORD);
    }
}
