package com.error.center.repository;

import com.error.center.entity.User;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles(value = "test")
public class UserRepositoryTest {

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "123456";

    @Autowired
    UserRepository repository;

    @BeforeEach
    public void setUp() {
        repository.save(new User(null, EMAIL, PASSWORD));
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testSave() {
        User u = new User(null, "1" + EMAIL, PASSWORD);
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
        Assertions.assertEquals(response.getPassword(), "123456789");
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
