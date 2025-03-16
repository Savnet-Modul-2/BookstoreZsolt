package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.User;
import com.project.bookstore.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void givenEmail_FindByEmail_ReturnUser() {
        String testEmail = "test@gmail.com";

        userRepository.save(testUser);
        User expected = userRepository.findByEmail(testEmail).orElse(null);

        Assertions.assertThat(expected).isEqualTo(testUser);
    }

    @Test
    public void givenNothing_FindByEmail_ReturnNull() {
        String testEmail = "test@gmail.com";

        User expected = userRepository.findByEmail(testEmail).orElse(null);

        Assertions.assertThat(expected).isNull();
    }

    @Test
    public void givenEmail_ExistsByEmail_ReturnTrue() {
        String testEmail = "test@gmail.com";

        userRepository.save(testUser);
        boolean expected = userRepository.existsByEmail(testEmail);

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void givenNothing_ExistsByEmail_ReturnFalse() {
        String testEmail = "test@gmail.com";

        boolean expected = userRepository.existsByEmail(testEmail);

        Assertions.assertThat(expected).isFalse();
    }
}


