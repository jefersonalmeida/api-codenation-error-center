package com.error.center.repository;

import com.error.center.domain.model.User;
import com.error.center.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles(value = "test")
public class UserRepositoryTest {

    private static final String NAME = "Test";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "123456";

    @Autowired
    UserRepository repository;

    @BeforeEach
    public void setUp() {
        repository.save(new User(null, NAME, EMAIL, PASSWORD));
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testSave() {
        User u = new User(null, NAME, "1" + EMAIL, PASSWORD);
        User response = repository.save(u);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(u.getId(), response.getId());
    }

    @Test
    public void testUpdate() {
        Optional<User> user = repository.findByEmailEquals(EMAIL);
        Assertions.assertTrue(user.isPresent());

        user.get().setPassword("123456789");
        User response = repository.save(user.get());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getPassword());
    }

    @Test
    public void testFindByEmail() {
        Optional<User> response = repository.findByEmailEquals(EMAIL);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get().getEmail(), EMAIL);
    }

    @Test
    public void testDelete() {
        Optional<User> response = repository.findByEmailEquals(EMAIL);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(response.get().getEmail(), EMAIL);

        repository.deleteById(response.get().getId());
        Optional<User> deleted = repository.findByEmailEquals(EMAIL);
        Assertions.assertFalse(deleted.isPresent());
    }
}
