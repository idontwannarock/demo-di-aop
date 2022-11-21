package com.example.demodiaop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoDiAopApplicationTest {

    @Test
    void context() {
        // app start test
        String hello = "Hello World!";
        String world = "Hello World!";
        Assertions.assertThat(hello).isEqualTo(world);
    }
}
