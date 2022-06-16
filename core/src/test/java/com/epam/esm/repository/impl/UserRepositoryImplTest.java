package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentConfig;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("dev")
public class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("Anna");
        assertNotNull(user);
    }

    @Test
    void findByUsernameFalse() {
        User user = userRepository.findByUsername("Ann");
        assertNull(user);
    }

}
