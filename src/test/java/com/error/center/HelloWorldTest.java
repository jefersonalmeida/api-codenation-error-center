package com.error.center;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "test")
public class HelloWorldTest {

    @Test
    public void testHelloWorld() {
        Assertions.assertEquals(1, 1);
    }
}
