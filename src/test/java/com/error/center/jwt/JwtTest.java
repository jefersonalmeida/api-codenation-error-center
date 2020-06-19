package com.error.center.jwt;

import com.error.center.entity.User;
import com.error.center.util.enums.Role;
import com.error.center.util.jwt.JwtUser;
import com.error.center.util.jwt.JwtUserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = "test")
public class JwtTest {

    private static final String NAME = "Test";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "123456";

    @Test
    public void testJwtUser() {
        JwtUser response = JwtUserFactory.create(getMockUser());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getUsername(), EMAIL);
        Assertions.assertEquals(response.getPassword(), PASSWORD);
    }

    private User getMockUser() {
        User user = new User(UUID.randomUUID(), NAME, EMAIL, PASSWORD);
        user.setRole(Role.ROLE_ADMIN);
        return user;
    }
}
