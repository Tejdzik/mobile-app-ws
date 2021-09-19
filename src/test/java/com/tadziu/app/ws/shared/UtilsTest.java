package com.tadziu.app.ws.shared;

import com.tadziu.app.ws.SpringApplicationContext;
import com.tadziu.app.ws.shared.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Utils.class)
class UtilsTest {

    @Autowired
    Utils utils;


    @BeforeEach
    void setUp() throws Exception {

    }


    @Test
    final void testGenerateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);
        assertNotNull(userId);
        assertNotNull(userId2);
        assertTrue(userId.length() == 30);
        assertTrue(!userId2.equals(userId));
    }


    //TODO: enable below test methods & readjust Integration tests
    @Test
    @Disabled
    final void testHasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("4yr65nyhijdj84");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);

    }

    @Test
    @Disabled

    final void testHasTokenExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImV4cCI6MTUzMjc3Nzc3NX0.cdudUo3pwZLN9UiTuXiT7itpaQs6BgUPU0yWbNcz56-l1Z0476N3H_qSEHXQI5lUfaK2ePtTWJfROmf0213UJA";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);

        assertTrue(hasTokenExpired);

    }
}

